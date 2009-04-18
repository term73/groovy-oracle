/*
 * $Id: SqlFileExporter.groovy 104 2009-03-04 14:17:30Z andre.winkler@web.de $
 * ============================================================================
 * Project groovy-oracle
 * Copyright (c) 2008 by Andre Winkler. All rights reserved.
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

package de.gluehloch.groovy.oracle.inout

import groovy.sql.Sql

import de.gluehloch.groovy.oracle.*

import de.gluehloch.groovy.oracle.meta.*

/**
 * Imports data from the file system to the database.
 * 
 * @author  $Author: andre.winkler@web.de $
 * @version $Revision: 104 $ $Date: 2009-03-04 15:17:30 +0100 (Mi, 04 Mrz 2009) $
 */
class SqlFileImporter {

    def sql
    def tableName
	def fileName
	def columnSeperator = '|'	

	def load() {
    	def omdf = new OracleMetaDataFactory()
    	def tableMetaData = omdf.createOracleTable(sql, tableName as String)
    	sql.getConnection().setAutoCommit(false)
    
        new File(fileName).eachLine { line ->
            def values = InOutUtils.split(line, columnSeperator)
            def insert = "INSERT INTO ${tableName}(${tableMetaData.toColumnList()}) VALUES("
            def columns = tableMetaData.columnMetaData.size()
            tableMetaData.columnMetaData.eachWithIndex { column, index ->
                if (column.isNumber()) {
                	insert += "${values.getAt(index)}"
                } else if (column.isDate()) {
                	insert += "to_date('${values.getAt(index)}', '${InOutUtils.ORACLE_DATE_FORMAT}')"
                } else {
                	insert += "'${values.getAt(index)}'"
                }
                if (index + 1 < columns) {
                	insert += ", "
                }
            }
            insert += ")"
            println insert
            sql.executeInsert(insert.toString())
        }
    	sql.commit()
    }

}
