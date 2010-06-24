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

package de.awtools.grooocle.inout;

import org.junit.Before;
import org.junit.Test;

import de.awtools.grooocle.meta.TestDatabaseUtility;

/**
 * Testcase for the SqlFileImporter and the multiple table import function.
 * 
 * @author  $Author: andre.winkler@web.de $
 * @version $Revision: 103 $ $Date: 2010-02-03 16:03:09 +0100 (Mi, 03 Feb 2010) $
 */
class MultipleSqlFileImportTest extends TestDatabaseUtility {
   
    @Test
    void testMultipleSqlFileImport() {
        new SqlFileImporter(
                sql: sql,
                deleteTableBefore: true,
                fileName: './src/test/resources/de/awtools/grooocle/inout/more_than_one_table_import.dat',
                createInsertFile: './target/tmp_insert.log').load()
		assert 2 == sql.firstRow(
                "SELECT COUNT(*) as counter FROM XXX_TEST_RUN").counter
        assert 2 == sql.firstRow(
                "SELECT COUNT(*) as counter FROM XXX_HIERARCHIE").counter
    }
    
    @Before
    void setUp() {
        sql.execute("DELETE FROM XXX_HIERARCHIE")
        sql.execute("DELETE FROM XXX_TEST_RUN")
    }

}
