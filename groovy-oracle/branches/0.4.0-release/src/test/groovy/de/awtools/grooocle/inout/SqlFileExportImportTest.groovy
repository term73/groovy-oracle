/*
 * $Id$
 * ============================================================================
 * Project grooocle
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

package de.awtools.grooocle.inout

import org.junit.Test
import org.junit.Afterimport org.junit.Before
import de.gluehloch.groovy.oracle.OraUtils
import de.gluehloch.groovy.oracle.meta.*
/**
 * Testcase for the classes SqlFileExporter and SqlFileImporter.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class SqlFileExportImportTest extends TestDatabaseUtility {
    
    @Test
    void testDatabaseExportImport() {
        new SqlFileImporter(
        sql: sql,
        tableName: 'XXX_TEST_RUN_2',
        fileName: 'XXX_TEST_RUN.dat',
        createInsertFile: './target/tmp_insert.log').load()
        
        assert 6 == sql.firstRow(
            "SELECT COUNT(*) as counter FROM XXX_TEST_RUN_2").counter
        
        assert 123.456 == sql.firstRow(
            "SELECT v_numeric FROM XXX_TEST_RUN_2 where id = 1").v_numeric
        
        assert 666.626 == sql.firstRow(
            "SELECT v_numeric FROM XXX_TEST_RUN_2 where id = 2").v_numeric
        
        assert '1971-03-24 17:05:05' == sql.firstRow(
            """
                SELECT
                    TO_CHAR(stichtag, ${InOutUtils.ORACLE_DATE_FORMAT}) as stichtag
                FROM
                    XXX_TEST_RUN_2
                WHERE
                    id = 1
            """).stichtag
        
        def ex = new SqlFileExporter(
                sql: sql, query: 'XXX_TEST_RUN_2', fileName: 'XXX_TEST_RUN_2.dat')
        ex.export()
    }
    
    @Test
    void testDatabaseImportWithTableHasMoreColumnsThanFileRows() {
        new SqlFileImporter(
        sql: sql,
        tableName: 'XXX_TEST_RUN_3',
        fileName: 'XXX_TEST_RUN.dat',
        createInsertFile: './target/tmp_insert.log').load()
        
        assert 6 == sql.firstRow(
            "SELECT COUNT(*) as counter FROM XXX_TEST_RUN_3").counter
    }
    
    @Test
    void testDatabaseImportWithSeparatorAtEndOfLine() {
        new SqlFileImporter(
            sql: sql,
            tableName: 'XXX_TEST_RUN_2',
            fileName: 'XXX_TEST_RUN_SEPARATOR_AT_END.dat',
            createInsertFile: './target/tmp_insert.log').load()

        assert 6 == sql.firstRow(
            "SELECT COUNT(*) as counter FROM XXX_TEST_RUN_2").counter
        
    }
    
    @Test
    void testDatabaseImportWithoutTableName() {
        new SqlFileImporter(
            sql: sql,
            fileName: 'XXX_TEST_RUN_SEPARATOR_AT_END.dat',
            createInsertFile: './target/tmp_insert.log').load()

        assert 6 == sql.firstRow(
            "SELECT COUNT(*) as counter FROM XXX_TEST_RUN_2").counter
        
    }
    
    @Before
    void setUp() {
        sql.execute("DELETE FROM XXX_TEST_RUN_2")
        sql.execute("DELETE FROM XXX_TEST_RUN_3")
    }
    
}
