/*
 * $Id$
 * ============================================================================
 * Project groovy-oracle
 * Copyright (c) 2008-2009 by Andre Winkler. All rights reserved.
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

package de.gluehloch.groovy.oracle

import java.sql.*

import groovy.sql.Sql

/**
 * Utilities für den Umgang mit Oracle. Die Methode <code>purgeRecyclebin</code>
 * wird nur ausgeführt, wenn die System-Eigenschaft
 * <code>groovy.oracle.purge_recyclebin</code> gesetzt ist.
 */
class OraUtils {

	static final DRIVER_NAME = 'oracle.jdbc.driver.OracleDriver'

	static def dataSource

	static def getConnection(user, password, url) {
		if (dataSource == null) {
			dataSource = new oracle.jdbc.pool.OracleDataSource()
			dataSource.setURL(url)
		}
		def conn = dataSource.getConnection(user, password)
		conn.setAutoCommit(false)
		return conn
	}

    static def createSql(_user, _password, _url, _port, _sid) {
        return createSql(_user, _password, "${_url}:${_port}:${_sid}")
    }

    static def createSql(_user, _password, _url) {
        def sql
    	try {
    		sql = new Sql(getConnection(
    			_user, _password, "jdbc:oracle:thin:${_user}/${_password}@${_url}"))
    	} catch (SQLException ex) {
    	    println ex.getMessage()
    		throw ex
    	}
    	return sql
    }

    static def dispose() {
    	dataSource?.close()
    	dataSource = null
    }

    static void purgeRecyclebin(sql) {
    	if (System.getProperty('groovy.oracle.purge_recyclebin') == 'true') {
    		sql.execute "purge recyclebin"
    	}
    }

    static def checkValidPackages(sql) {
    	def invalidPackages = []
    	sql.eachRow("""
                SELECT object_name
                FROM user_objects
                WHERE status = 'INVALID'
                    AND object_type IN ('PACKAGE', 'PACKAGE BODY')
    	    """) {
    		invalidPackages << it.object_name
    	}
    	return invalidPackages
    }

    static def checkValidProcedures(sql) {
        def invalidProcedures = []
        sql.eachRow("""
                SELECT object_name
                FROM user_objects
                WHERE status = 'INVALID'
                    AND object_type = 'PROCEDURE'
            """) {
        	invalidProcedures << it.object_name
        }
        return invalidProcedures
    }

}
