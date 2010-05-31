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

package de.gluehloch.groovy.oracle.inout

import org.junit.Test
import org.junit.After
import org.junit.Before

import de.gluehloch.groovy.oracle.OraUtils
import de.gluehloch.groovy.oracle.meta.*

/**
 * Test for class DBUnit.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class DBUnitTest extends TestDatabaseUtility {
    
    @Test
    void testDBUnit() {
        DBUnit.xmlExport(OraUtils.dataSource, user,
            ['XXX_TEST_RUN_2'] as String[], new File('dbunit.xml'))
        sql.execute("DELETE FROM XXX_TEST_RUN_2")
        sql.commit()

        DBUnit.xmlImport(OraUtils.dataSource, user, new File('dbunit.xml'),
                DBUnit.DBUnitOperation.INSERT)
        def counter = sql.firstRow("SELECT COUNT(*) as counter FROM XXX_TEST_RUN_2").counter
        assert counter == 6
    }
    
    @Before
    void setUp() {
        sql = TestDatabaseUtility.createConnection()
        new SqlFileImporter(sql: sql, tableName: 'XXX_TEST_RUN_2',
                fileName: 'XXX_TEST_RUN.dat').load()
        sql.commit()
    }
    
    @After
    void tearDown() {
    }
    
}
