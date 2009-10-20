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

class OracleColumnTest {

	@Test
	void testOracleColumnCopy() {
        def oracleColumn = new OracleColumn(columnName: "name_of_column",
                dataType: "VARCHAR2", dataLength: 10, nullable: true,
                dataDefault: "Hallo")
        def clone = oracleColumn.copy()
        assert oracleColumn.columnName == clone.columnName
        assert oracleColumn.dataType == clone.dataType
        assert oracleColumn.dataLength == clone.dataLength
        assert oracleColumn.nullable == clone.nullable
        assert oracleColumn.dataDefault == clone.dataDefault

        assert oracleColumn == clone
	}

	@Test
    void testOracleColumnToScript() {
        def oracleColumn

        oracleColumn = new OracleColumn(columnName: "name_of_column",
                dataType: "VARCHAR2", dataLength: 10, nullable: true,
                dataDefault: "Hallo")
        assert oracleColumn.toScript() == "\"name_of_column\" VARCHAR2(10 BYTE) DEFAULT Hallo"

        oracleColumn = new OracleColumn(columnName: "name_of_column",
                dataType: "VARCHAR2", dataLength: 10, nullable: false,
                dataDefault: "Hallo")
        assert oracleColumn.toScript() == "\"name_of_column\" VARCHAR2(10 BYTE) DEFAULT Hallo NOT NULL ENABLE"

        oracleColumn = new OracleColumn(columnName: "name_of_column",
                dataType: "VARCHAR2", dataLength: 10, nullable: false)
        assert oracleColumn.toScript() == "\"name_of_column\" VARCHAR2(10 BYTE) NOT NULL ENABLE"

        oracleColumn = new OracleColumn(columnName: "name_of_column",
                dataType: "NUMBER", dataPrecision: 12, dataScale: 2,
                nullable: false)
        assert oracleColumn.toScript() == "\"name_of_column\" NUMBER(12,2) NOT NULL ENABLE"

        oracleColumn = new OracleColumn()
        try {
            oracleColumn.toScript()
            fail("Expected an IllegalArgumentException")
        } catch (IllegalArgumentException ex) {
            // Ok
        }
    }

}
