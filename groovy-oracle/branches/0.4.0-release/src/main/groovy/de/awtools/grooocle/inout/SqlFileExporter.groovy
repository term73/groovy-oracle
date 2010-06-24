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

import oracle.sql.*

import de.gluehloch.groovy.oracle.*
import de.gluehloch.groovy.oracle.meta.*

/**
 * Exports the result set of a sql statement to the file system. The property
 * <code>query</code> allows two possibilities: A SQL query or a table name.
 * If you specify only a table name, then the method <code>export()</code>
 * will generate a SQL select statement for the full table.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class SqlFileExporter {
    
    def sql
    def query
    def fileName
    def columnSeperator = '|'
    def where
    
    /** If you need a special data formatter, than set it here. */
    def dateFormat = InOutUtils.ORACLE_DATE_FORMAT
    
    def export() {
        if (!query) {
            throw new IllegalArgumentException("Property 'query' must be set!")
        }
        
        def fileWriter = new GFileWriter(fileName)
        
        def _query = ""
        if (query =~ /^(\s)*(?i)select/) {
            _query = query
        } else {
            // Generate the query!
            def columns = []
            def omdf = new OracleMetaDataFactory()
            def oracleTable = omdf.createOracleTable(sql, query)
            
            oracleTable.columnMetaData.each { column ->
                if (column.isDate()) {
                    columns << "TO_CHAR(${column.columnName}, '${dateFormat}') as ${column.columnName}"
                } else {
                    columns << column.columnName
                }
            }
            
            fileWriter.writeln("### TAB ${query}")
            fileWriter.writeln("### ${columns.join(columnSeperator)}")
            
            _query = "select ${columns.join(',')} from ${query}".toString()
            if (where) {
                _query += " WHERE ${where}"
            }
        }
        
        def tmp
        sql.eachRow(_query) { row ->
            def string = ""
            for (i in 1 .. row.getMetaData().getColumnCount()) {
                def columnName = row.getMetaData().getColumnName(i)
                def columnType = row.getMetaData().getColumnType(i)
                tmp = row."${columnName}"
                switch (columnType) {
                    case java.sql.Types.DATE:
                        if (tmp != null) {
                            string += InOutUtils.toString(tmp)
                        }
                        break
                    case java.sql.Types.TIMESTAMP:
                        if (tmp != null) {
                            string += InOutUtils.toString(tmp)
                        }
                        break
                    default:
                        tmp = tmp
                        if (tmp != null) {
                            string += tmp
                        }
                }
                
                if (i < row.getMetaData().getColumnCount()) {
                    string += columnSeperator
                }
            }
            fileWriter.writeln(string)
        }
        fileWriter.close()
    }
    
}
