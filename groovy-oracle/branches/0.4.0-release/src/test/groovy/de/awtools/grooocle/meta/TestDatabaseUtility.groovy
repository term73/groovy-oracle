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

package de.awtools.grooocle.meta

import de.gluehloch.groovy.oracle.OraUtils
import org.junit.Ignore
import groovy.sql.Sql

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

class TestDatabaseUtility { // extends GroovyTestCase {

    static def sql
    static def user

    /**
     * Liefert ein Groovy SQL Objekt.
     *
     * @return Ein Groovy SQL Objekt.
     */
    static def createConnection() {
        user = System.getProperty('groovy.oracle.test.user')
        def pwd = System.getProperty('groovy.oracle.test.password')
        def url = System.getProperty('groovy.oracle.test.url')

        println "The test runner is using Oracle: ${user}/${pwd}@${url}"

        if (!user || !pwd || !url) {
            println """
Check your maven .settings.xml or update your system properties: 
  <profiles>
    <profile>
      <id>default</id>
      <properties>
        <compiler.encoding>UTF-8</compiler.encoding>
        <groovy.oracle.test.user>user</groovy.oracle.test.user>
        <groovy.oracle.test.password>password</groovy.oracle.test.password>
        <groovy.oracle.test.url>host:port:sid</groovy.oracle.test.url>
        <groovy.oracle.purge_recyclebin>true</groovy.oracle.purge_recyclebin>
      </properties>
    </profile>
  </profiles>

or define the following VM arguments:

-Dgroovy.oracle.test.user=user 
-Dgroovy.oracle.test.password=password
-Dgroovy.oracle.test.url=host:port:sid
-Dgroovy.oracle.purge_recyclebin=false
"""
            throw new IllegalStateException()
        }
    
        OraUtils.createSql(user, pwd, url)
    }

    @Test @Ignore
    void testIrgendwas() {
    	// Der doofe Testrunner...
    }
 
    @BeforeClass
    static void beforeClass() {
    	sql = createConnection()
    	new PrepareUnitTestDatabase(sql: sql).setUp()
    }

    @AfterClass
    static void afterClass() {
    	new PrepareUnitTestDatabase(sql: sql).cleanUp()
        sql?.close()
    }

}
