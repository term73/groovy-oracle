/*
 * $Id: SqlFileExporter.groovy 123 2009-03-18 11:38:24Z andre.winkler@web.de $
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
 * Exports the result set of a sql statement to the file system.
 * 
 * @author  $Author: andre.winkler@web.de $
 * @version $Revision: 123 $ $Date: 2009-03-18 12:38:24 +0100 (Mi, 18 Mrz 2009) $
 */
class SqlFileExporter {

	def sql
	def query
	def fileName
	def columnSeperator = '|'

	def export() {
		def fileWriter = new GFileWriter(fileName)

		sql.eachRow(query) { row ->
		    def string = ""
		    for (i in 1 .. row.getMetaData().getColumnCount()) {
		    	def columnName = row.getMetaData().getColumnName(i)
		    	def columnType = row.getMetaData().getColumnType(i)
		    	switch (columnType)
		    	{
		    	case java.sql.Types.DATE:
		    		string += InOutUtils.toString(row."${columnName}")
		    		break
                case java.sql.Types.TIMESTAMP:
                    string += InOutUtils.toString(row."${columnName}")
                    break
                default:
                	string += row."${columnName}"
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
