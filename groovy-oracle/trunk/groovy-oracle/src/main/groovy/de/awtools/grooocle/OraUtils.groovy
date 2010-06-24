/*
 * $Id$
 * ============================================================================
 * Project grooocle
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

package de.awtools.grooocle

import de.awtools.grooocle.meta.OracleMetaDataFactory

import java.sql.*

import groovy.sql.Sql

/**
 * Utilities fuer den Umgang mit Oracle. Die Methode <code>purgeRecyclebin</code>
 * wird nur ausgefuehrt, wenn die System-Eigenschaft
 * <code>groovy.oracle.purge_recyclebin</code> gesetzt ist.
 *
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class OraUtils {

    static final DRIVER_NAME = 'oracle.jdbc.driver.OracleDriver'

    static def dataSource

    static def getConnection(user, password, url) {
        def conn = null
        try {
            if (dataSource == null) {
                dataSource = new oracle.jdbc.pool.OracleDataSource()
                dataSource.setURL(url)
            }
            conn = dataSource.getConnection(user, password)
            conn.setAutoCommit(false)
        } catch (SQLException ex) {
            // I am not able to establish a connection, so i reset them all.
            try {
                dataSource?.close()
            } catch (Exception closeException) {
                // ok
            }
            dataSource = null
            conn = null

            throw ex
      	}
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

    static def createOracleSchema(sql) {
    	def factory = new OracleMetaDataFactory() 
    	return factory.createOracleSchema(sql) 
    }

    static def checkValidPackages(sql) {
        def invalidPackages = []
        sql.eachRow(
            """
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
        sql.eachRow(
            """
                SELECT object_name
                FROM user_objects
                WHERE status = 'INVALID'
                    AND object_type = 'PROCEDURE'
            """) {
            invalidProcedures << it.object_name
        }
        return invalidProcedures
    }
    
    /**
     * Returns a list of Strings with the format 'object_name:object_type'.
     *
     * @param sql A Groovy SQL object.
     * @param A list of Strings. See procedure description.
     */
    static def findInvalidObjects(sql) {
        def invalidObjects = []
        sql.eachRow(
            """
                SELECT object_name, object_type
                FROM user_objects
                WHERE status != 'VALID';
            """) {
            invalidObjects << "${it.object_name}:${it.object_type}" 
        }
        return invalidObjects
    }
    
    static def cleanUpSchema(sql) {
        sql.call(
            """
                BEGIN
                    FOR i IN (SELECT object_name FROM user_objects WHERE object_type = 'PACKAGE')
                    LOOP
                        BEGIN
                            EXECUTE IMMEDIATE 'DROP PACKAGE ' || i.object_name;
                        EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                        END;
                    END LOOP;

                    FOR i IN (SELECT object_name FROM user_objects WHERE object_type = 'PROCEDURE')
                    LOOP
                        BEGIN
                            EXECUTE IMMEDIATE 'DROP PROCEDURE ' || i.object_name;
                        EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                        END;
                    END LOOP;

                    FOR i IN (SELECT object_name FROM user_objects WHERE object_type = 'FUNCTION')
                    LOOP
                        BEGIN
                            EXECUTE IMMEDIATE 'DROP FUNCTION ' || i.object_name;
                        EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                        END;
                    END LOOP;

                    FOR i IN (SELECT object_name FROM user_objects WHERE object_type = 'VIEW')
                    LOOP
                        BEGIN
                            EXECUTE IMMEDIATE 'DROP VIEW ' || i.object_name || ' CASCADE CONSTRAINTS';
                        EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                        END;
                    END LOOP;

                    FOR i IN (SELECT object_name FROM user_objects WHERE object_type = 'TABLE')
                    LOOP
                        BEGIN
                            EXECUTE IMMEDIATE 'DROP TABLE ' || i.object_name || ' CASCADE CONSTRAINTS';
                        EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                        END;
                    END LOOP;

                    FOR i IN (SELECT object_name FROM user_objects WHERE object_type = 'TYPE')
                    LOOP
                        BEGIN
                            EXECUTE IMMEDIATE 'DROP TYPE ' || i.object_name;
                        EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                        END;
                    END LOOP;

                    FOR i IN (SELECT object_name FROM user_objects WHERE object_type = 'SEQUENCE')
                    LOOP
                        BEGIN
                            EXECUTE IMMEDIATE 'DROP SEQUENCE ' || i.object_name;
                        EXCEPTION
                            WHEN OTHERS THEN
                                NULL;
                        END;
                    END LOOP;
                END;
            """
        )
    }

}
