/*
 * $Id: SqlFileExportImportTest.groovy 140 2009-04-17 15:44:57Z andre.winkler@web.de $
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
 * @author  $Author: andre.winkler@web.de $
 * @version $Revision: 104 $ $Date: 2009-03-04 15:17:30 +0100 (Mi, 04 Mrz 2009) $
 */
class DBUnitTest extends TestDatabaseUtility {

	 @Test
	 void testDBUnit() {
		 DBUnit.xmlExport(OraUtils.dataSource, user,
			 ['XXX_TEST_RUN'] as String[], new File('dbunit.xml'))		 
	 }

     @Before
     void setUp() {
         sql = TestDatabaseUtility.createConnection()
         sql.execute("""CREATE TABLE XXX_TEST_RUN (
                          ID NUMBER(38,0),
                          TRIGGER_TYPE CHAR(1 BYTE) NOT NULL ENABLE,
                          STICHTAG DATE NOT NULL ENABLE,
                          DB_USER VARCHAR2(256 BYTE),
                          DATUM_START TIMESTAMP (6) DEFAULT SYSTIMESTAMP,
                          VKEY_BL VARCHAR2(10 BYTE),
                          BL_RUN_ID NUMBER(38,0),
                          V_NUMERIC NUMBER(10,3),
                          CONSTRAINT PK_XXX_TEST_RUN PRIMARY KEY (ID))""")
         new SqlFileImporter(sql: sql, tableName: 'XXX_TEST_RUN',
        	 fileName: 'XXX_TEST_RUN.dat').load()
     }

     @After
     void tearDown() {
         sql.execute('DROP TABLE XXX_TEST_RUN')
     }
	
}
