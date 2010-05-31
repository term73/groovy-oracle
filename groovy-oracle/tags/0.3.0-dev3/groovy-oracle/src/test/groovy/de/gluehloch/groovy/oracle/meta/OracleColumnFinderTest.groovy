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

class OracleColumnFinderTest extends TestDatabaseUtility {

	@Test
    void testOracleColumnGetColumns() {
        def ocf = new OracleColumnFinder()
        def oracleColumns = ocf.getColumns(sql, "XXX_TEST_RUN")

        assert "ID" == oracleColumns[0].columnName
        assert "TRIGGER_TYPE" == oracleColumns[1].columnName
        assert "STICHTAG" == oracleColumns[2].columnName
        assert "DB_USER" == oracleColumns[3].columnName
        assert "DATUM_START" == oracleColumns[4].columnName
        assert "VKEY_BL" == oracleColumns[5].columnName
        assert "BL_RUN_ID" == oracleColumns[6].columnName

        assert "NUMBER" == oracleColumns[0].dataType
        assert "CHAR" == oracleColumns[1].dataType
        assert "DATE" == oracleColumns[2].dataType
        assert "VARCHAR2" == oracleColumns[3].dataType
        assert "TIMESTAMP(6)" == oracleColumns[4].dataType
        assert "VARCHAR2" == oracleColumns[5].dataType
        assert "NUMBER" == oracleColumns[6].dataType
    }

    @Test
    void testOracleColumnGetColumnsTableNotFound() {
        def ocf = new OracleColumnFinder()
        def oracleColumns = ocf.getColumns(sql, "not_available")

        assert 0 == oracleColumns.size()
    }

	@Test
    void testOracleColumnGetConstraint() {
        def ocf = new OracleColumnFinder()
        def oracleConstraints = ocf.getConstraint(sql, "XXX_TEST_RUN")
        assert oracleConstraints.primaryKey != null
        assert oracleConstraints.primaryKey.name == "PK_XXX_TEST_RUN"
        assert oracleConstraints.primaryKey.columnNames == ["ID"]

        oracleConstraints = ocf.getConstraint(sql, "XXX_HIERARCHIE")
        assert oracleConstraints.primaryKey != null
        assert oracleConstraints.primaryKey.name == "PK_XXX_HIERARCHIE"
        assert oracleConstraints.primaryKey.columnNames == ["VEKNKDNR", "TEST_ID"]
        assert oracleConstraints.foreignKeys.size() == 1
        assert oracleConstraints.foreignKeys[0].name == "FK_XXX_HIERARCHIE"
        assert oracleConstraints.foreignKeys[0].columnNames == ["FK_RUN_ID"]
        assert oracleConstraints.foreignKeys[0].referencedColumnNames == ["ID"]

        oracleConstraints = ocf.getConstraint(sql, "XXX_KUNDE")
        assert oracleConstraints.primaryKey != null
        assert oracleConstraints.primaryKey.name == "PK_XXX_KUNDE"
        assert oracleConstraints.primaryKey.columnNames == ["VEKNKDNR", "KDNR", "TEST_ID"]
        assert oracleConstraints.foreignKeys.size() == 2

        def fk = oracleConstraints.foreignKeys.find { it.name == 'FK_XXX_KUNDE_HIERARCHIE' }
        assert fk.name == 'FK_XXX_KUNDE_HIERARCHIE'
        assert fk.tableName == 'XXX_KUNDE'
        assert fk.referencedTableName == 'XXX_HIERARCHIE'
        assert fk.columnNames == ['VEKNKDNR', 'TEST_ID']
        assert fk.referencedColumnNames == ['VEKNKDNR', 'TEST_ID']

        fk = oracleConstraints.foreignKeys.find { it.name == 'FK_XXX_KUNDE' }
        assert fk.name == "FK_XXX_KUNDE"
        assert fk.columnNames == ["FK_RUN_ID"]
        assert fk.referencedColumnNames == ["ID"]

        oracleConstraints = ocf.getConstraint(sql, "not_available")
        assert oracleConstraints != null
    }

}
