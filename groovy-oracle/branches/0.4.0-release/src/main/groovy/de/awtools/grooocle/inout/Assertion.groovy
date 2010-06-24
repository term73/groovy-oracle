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

package de.awtools.grooocle.inout

import java.sql.SQLException

/**
 * Compares expectations and database reality.
 */
class Assertion {

	/**
	 * Compares a database result set with an expectation. Throws an assertion
	 * exception if necessary.<br/>
	 * Example:
	 * <pre>
	 * import de.gluehloch.groovy.oracle.inout.*
	 * 
	 * def sql = OraUtils.createSql(user, password, url)
	 * def expectation = Data.createData('a_table',
	 *     [
	 *         [col1: 1, col2: 'ein_test_1', col3: 1971],
	 *         [col1: 2, col2: 'ein_test_2', col3: 1972],
	 *         [col1: 3, col2: 'ein_test_3', col3: 1973],
	 *     ])
	 * assertRowEquals(sql, expectation, 'select col1, col2, col3 from a_table order by id')
	 * </pre>
	 *
	 * @param sql A Groovy sql object.
	 * @param expectation A Data object.
	 * @param query A sql query.
	 */
    static def assertRowEquals(sql, expectation, query) {
	    def index = 0
	    sql.eachRow(query) { row ->
	        expectation.rows[index++].each() {
	        	def colValue = null
	        	try {
	        	    colValue = row[it.key]
	        	} catch (SQLException ex) {
	        		throw new SQLException("The row ${it.key} is not defined by query ${query}")
	        	}

	        	//
	        	// TODO That looks strange!
	        	//
	        	if (it.value != null && colValue != null) {
	        		assert it.value == colValue, "At row ${index} at column ${it.key} expected '${it.value}' but was '${colValue}'."
	        	} else if (it.value == null && colValue != null) {
	        		assert false, "At row ${index} at column ${it.key} expected '${it.value}' but was '${colValue}'."
	            } else if (it.value != null && colValue == null) {
	            	if (it.value == '') {
	            		assert true
	            	} else {
	            		assert false, "At row ${index} at column ${it.key} expected '${it.value}' but was '${colValue}'."
	            	}
	            } else {
	            	assert true
	            }
	        }
	    }
	}

}
