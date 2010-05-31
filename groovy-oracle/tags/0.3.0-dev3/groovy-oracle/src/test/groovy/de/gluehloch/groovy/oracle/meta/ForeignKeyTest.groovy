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

class ForeignKeyTest {

	@Test
    void testForeignKeyCopy() {
        def fk = new ForeignKey(name: "FOREIGN_KEY",
                rConstraintName: "CONSTRAINT_NAME",
                referencedTableName: "REFERENCED_TABLE_NAME",
                columnNames: ["COL1", "COL2", "COL3", "COL4"],
                referencedColumnNames: ["FK_COL1", "FK_COL2"])

        def cloneFk = fk.copy()

        assert cloneFk.columnNames == ["COL1", "COL2", "COL3", "COL4"]
        assert cloneFk.referencedColumnNames == ["FK_COL1", "FK_COL2"]
        assert cloneFk.name == "FOREIGN_KEY"
        assert cloneFk.rConstraintName == "CONSTRAINT_NAME"
        assert cloneFk.referencedTableName == "REFERENCED_TABLE_NAME"

        assert cloneFk == fk
        assert ["A", "B"] == ["A", "B"]
        assert ["A", "B"] != ["B", "A"]
        assert fk != new PrimaryKey(name: "Winkler")
    }

}
