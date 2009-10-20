/*
 * $Id$
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

	def export() {
		if (!query) {
			throw new IllegalArgumentException("Property 'query' must be set!")
		}

		def fileWriter = new GFileWriter(fileName)

		def _query = ""
		if (query.startsWith('select') || query.startsWith('SELECT')) {
            _query = query
		} else {
            // Generate the query!
            def columns = []
			def omdf = new OracleMetaDataFactory()
			def oracleTable = omdf.createOracleTable(sql, query)
			oracleTable.columnMetaData.each { column ->
			    if (column.isDate()) {
			    	columns << "TO_CHAR(${column.columnName}, '${InOutUtils.ORACLE_DATE_FORMAT}') as ${column.columnName}"
			    } else {
			    	columns << column.columnName
			    }
            }
            _query = "select ${columns.join(',')} from ${query}".toString()
		}

		def tmp
		sql.eachRow(_query) { row ->
		    def string = ""
		    for (i in 1 .. row.getMetaData().getColumnCount()) {
		    	def columnName = row.getMetaData().getColumnName(i)
		    	def columnType = row.getMetaData().getColumnType(i)
		    	switch (columnType)
		    	{
		    	case java.sql.Types.DATE:
		    		tmp = InOutUtils.toString(row."${columnName}")
		    		if (tmp != null) {
		    			string += tmp
		    		}
		    		break
                case java.sql.Types.TIMESTAMP:
                    tmp = InOutUtils.toString(row."${columnName}")
                    if (tmp != null) {
                        string += tmp
                    }
                    break
                default:
                	tmp = row."${columnName}"
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
