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

package de.awtools.grooocle.meta

import org.junit.Test

/**
 * TODO
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class PrimaryKeyTest {

	@Test
    void testPrimaryKeyToColumnList() {
        def pk = new PrimaryKey(name: "PRIMARY_KEY",
                columnNames: ["PK1", "PK2", "PK3"])
        assert pk.toColumnList() == "PK1, PK2, PK3"
    }

	@Test
	void testPrimaryKeyCopy() {
		def pk = new PrimaryKey(name: "PRIMARY_KEY",
		        columnNames: ["PK1", "PK2", "PK3"])
		def clonePk = pk.copy()

		assert clonePk.name == "PRIMARY_KEY"
		assert clonePk.columnNames == ["PK1", "PK2", "PK3"]

		assert clonePk == pk
		assert pk != new ForeignKey(name: "FOREIGN_KEY")
	}

}
