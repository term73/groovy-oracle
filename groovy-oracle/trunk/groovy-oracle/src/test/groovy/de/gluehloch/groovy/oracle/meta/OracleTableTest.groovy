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

package de.gluehloch.groovy.oracle.meta

import org.junit.Test
import org.junit.Before

class OracleTableTest {

    final def nl = System.getProperty("line.separator")
    def oracleTable

    @Test
    void testOracleTableCopy() {
        def clone = oracleTable.copy()
        assert clone == oracleTable
    }

    @Test
    void testOracleTableToColumnList() {
        assert oracleTable.toColumnList() == "col_1, col_2, col_3"

        def col4 = new OracleColumn(columnName: "an_other_column")
        oracleTable.columnMetaData << col4
        assert oracleTable.toColumnList() == "col_1, col_2, col_3, an_other_column"
        assert oracleTable.toColumnList(["col_1", "col_3"]) == "col_2, an_other_column"
        assert oracleTable.toColumnList([]) == "col_1, col_2, col_3, an_other_column"
        assert oracleTable.toColumnList(null) == "col_1, col_2, col_3, an_other_column"
    }

    @Test
    void testOracleTableToScript() {
        def expectedScript = "CREATE TABLE \"name_of_table\"(${nl}\t\"col_1\" VARCHAR2(10 BYTE) DEFAULT Hallo,${nl}\t\"col_2\" VARCHAR2(10 BYTE) NOT NULL ENABLE,${nl}\t\"col_3\" NUMBER(12,2) NOT NULL ENABLE${nl})"
        assert expectedScript.toString() == oracleTable.toScript().toString()
    }

    @Before
    void setUp() {
        def col1 = new OracleColumn(columnName: "col_1",
                dataType: "VARCHAR2", dataLength: 10, nullable: true,
                dataDefault: "Hallo")
        def col2 = new OracleColumn(columnName: "col_2",
                dataType: "VARCHAR2", dataLength: 10, nullable: false)
        def col3 = new OracleColumn(columnName: "col_3",
                dataType: "NUMBER", dataPrecision: 12, dataScale: 2,
                nullable: false)

        oracleTable = new OracleTable(tableName: "name_of_table",
                columnMetaData: [col1, col2, col3])
    }

}
