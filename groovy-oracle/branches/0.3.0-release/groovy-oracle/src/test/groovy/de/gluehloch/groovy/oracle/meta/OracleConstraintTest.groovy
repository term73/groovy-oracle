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

class OracleConstraintTest {

    def oracleConstraint

    @Test
    void testOracleConstraintCopy() {
    	def clone = oracleConstraint.copy()
    	assert oracleConstraint == clone
    }

    @Test
	void testOracleConstraintPrimaryKey() {
        println oracleConstraint.toScriptPrimaryKey()

        assert oracleConstraint.toScriptPrimaryKey() ==
            "ALTER TABLE table_under_test ADD (CONSTRAINT name_of_primaryKey PRIMARY KEY (pk1,pk2))"
	}

    @Test
    void testOracleConstraintPrimaryKeyIsNull() {
        oracleConstraint.primaryKey = null

        assert oracleConstraint.toScriptPrimaryKey() == null
    }

    @Test
    void testOracleConstraintForeingKey() {
        println oracleConstraint.toScriptForeignKey()

        def scripts = ["ALTER TABLE table_under_test ADD (CONSTRAINT name_of_foreignKey FOREIGN KEY(FK_col1,FK_col2) REFERENCES name_of_referencedTableName(PK_col1,PK_col2))",
                       "ALTER TABLE table_under_test ADD (CONSTRAINT name_of_foreignKey_2 FOREIGN KEY(FK_col1,FK_col2) REFERENCES name_of_referencedTableName_2(PK_col1,PK_col2))"]

        def alterTables = oracleConstraint.toScriptForeignKey()
        assert scripts[0] == alterTables[0]
        assert scripts[1] == alterTables[1]
    }

    @Before
	void setUp() {
        def foreignKeys = [
                           new ForeignKey(name: "name_of_foreignKey",
                                   rConstraintName: "name_of_referencedPrimaryKey",
                                   referencedTableName: "name_of_referencedTableName",
                                   columnNames: ["FK_col1", "FK_col2"],
                                   referencedColumnNames: ["PK_col1", "PK_col2"]),
                           new ForeignKey(name: "name_of_foreignKey_2",
                                   rConstraintName: "name_of_referencedPrimaryKey_2",
                                   referencedTableName: "name_of_referencedTableName_2",
                                   columnNames: ["FK_col1", "FK_col2"],
                                   referencedColumnNames: ["PK_col1", "PK_col2"])
                           ]

        oracleConstraint = new OracleConstraint(
                tableName: "table_under_test",
                primaryKey: new PrimaryKey(name: "name_of_primaryKey",
                        columnNames: ["pk1", "pk2"]),
                foreignKeys: foreignKeys)
	}

}
