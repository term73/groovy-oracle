/*
 * $Id$
 * ============================================================================
 * Project groovy-oracle
 * Copyright (c) 2008-2010 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU LESSER GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.awtools.grooocle.inout

import groovy.sql.Sql

import de.gluehloch.groovy.oracle.*

import de.gluehloch.groovy.oracle.meta.*

/**
 * Imports data from the file system to the database.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class SqlFileImporter {
    
    def sql
    
    /** The name of the database table to import for. */
    def tableName
    
    /**
     * Set this property to true, if you want to delete the table before an
     * import.
     */ 
    def deleteTableBefore
    
    /**
     * The file name for the import.
     */
    def fileName
    
    /**
     * The default column seperator. If you need another one, change it here.
     */
    def columnSeperator = '|'
    
    /**
     * If you do not want to loose leading and trailing spaces, then set this
     * property to false.
     */
    def trim = true
    
    /**
     * Set this property to true, if you do not want any database change (or
     * a dry run).
     */
    def logOnly = false
    
    /** After #commitLimit INSERTS, i will execute a commit. */
    def commitLimit = 1000
    
    /** Counting the number of INSERTS. */
    def insertCounter = 0
    
    /** If you need a special data formatter, than set it here. */
    def dateFormat = InOutUtils.ORACLE_DATE_FORMAT
    
    /**
     * Set this property if you want to get a SQL file with all generated
     * INSERT statements. This property takes a file name.
     */
    def createInsertFile
    
    /** Creates the meta data model of a database table. */
    final def omdf = new OracleMetaDataFactory()
    
    def load() {
        sql.getConnection().setAutoCommit(false)
        
        def fileWriter = null
        if (createInsertFile) {
            fileWriter = new GFileWriter(createInsertFile)
        }
        
        if (fileName instanceof File) {
            fileName = fileName.getAbsolutePath()
        }
        
        def tableMetaData = null
        def insertConst = null
        
        if (tableName) {
            tableMetaData = init(tableName, fileWriter)
            insertConst = "INSERT INTO ${tableName}(${tableMetaData.toColumnList()}) VALUES("
        }
        
        new File(fileName).eachLine { line ->
            if (line =~ /^### TAB /) {
                def tokens = line.tokenize()
                def findTableName = tokens[tokens.findIndexOf { it == 'TAB' } + 1]
                
                tableMetaData = init(findTableName, fileWriter)
                insertConst = "INSERT INTO ${findTableName}(${tableMetaData.toColumnList()}) VALUES("
            }
            
            if (tableMetaData && !(line.trim().isEmpty()) && !(line =~ /^(\s)*#/)) {
                
                def values = InOutUtils.split(line, columnSeperator)
                def insert = insertConst
                def columns = tableMetaData.columnMetaData.size()
                tableMetaData.columnMetaData.eachWithIndex { column, index ->
                    if (index < values.size()) {
                        if (!values.getAt(index).trim()) {
                            insert += "NULL"
                        } else if (column.isNumber()) {
                            insert += "${values.getAt(index).trim()}".replace(",", ".")
                        } else if (column.isDate()) {
                            insert += "to_date('${values.getAt(index).trim()}', '${dateFormat}')"
                        } else {
                            def value = values.getAt(index)?.replaceAll("'", "''")
                            if (trim) {
                                value = value.trim()
                            }
                            insert += "'${value}'"
                        }
                    } else {
                        // The import table has more columns than the line rows, so
                        // i fill up the gap with 'NULL'.
                        insert += "NULL"
                    }
                    
                    if (index + 1 < columns) {
                        insert += ", "
                    }
                }
                insert += ")"
                
                if (createInsertFile) {
                    fileWriter.writeln "${insert};"
                }
                
                if (!logOnly) {
                    insertCounter++
                    sql.executeInsert(insert.toString())
                }
                
                if (insertCounter > commitLimit) {
                    insertCounter = 0
                    sql.commit()            	
                }
            }
        }
        sql.commit()
        
        if (createInsertFile) {
            fileWriter.close()
        }
    }
    
    private def init(table, fileWriter) {
        if (deleteTableBefore) {
            sql.call('DELETE FROM ' + table)
            sql.commit()
            if (createInsertFile) {
                fileWriter.writeln "DELETE FROM ${table};"
                fileWriter.writeln "COMMIT;"
            }
        }
        
        omdf.createOracleTable(sql, table as String)
    }
    
}
