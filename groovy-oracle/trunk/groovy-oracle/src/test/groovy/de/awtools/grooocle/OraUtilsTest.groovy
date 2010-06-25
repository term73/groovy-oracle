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

import org.junit.Test
import org.junit.Before

/**
 * Testet die Klasse OraUtils.
 *
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class OraUtilsTest {

	def user
	def pwd
	def url

	@Test
	void testOraUtilsCreateSql() {
		if (!user || !pwd || !url) {
			println """Check your .settings.xml:
  <profiles>
    <profile>
      <id>default</id>
      <properties>
        <compiler.encoding>UTF-8</compiler.encoding>
        <groovy.oracle.test.user>user</groovy.oracle.test.user>
        <groovy.oracle.test.password>password</groovy.oracle.test.password>
        <groovy.oracle.test.url>host:port:sid</groovy.oracle.test.url>
        <groovy.oracle.test.purge_recyclebin>true</groovy.oracle.test.purge_recyclebin>
      </properties>
    </profile>
  </profiles>
"""
			throw new IllegalStateException()
		}
	
		def sql = OraUtils.createSql(user, pwd, url);
		assert sql != null
	}

	@Test
	void testOraUtilsInvalidPackages() {
		def sql = OraUtils.createSql(user, pwd, url);

		def invalidPackages = OraUtils.checkValidPackages(sql)
		assert invalidPackages != null

		def invalidProcedures = OraUtils.checkValidProcedures(sql)
		assert invalidProcedures != null
	}

	@Before
	void setUp() {
        user = System.getProperty('groovy.oracle.test.user')
        pwd = System.getProperty('groovy.oracle.test.password')
        url = System.getProperty('groovy.oracle.test.url')		
	}

}
