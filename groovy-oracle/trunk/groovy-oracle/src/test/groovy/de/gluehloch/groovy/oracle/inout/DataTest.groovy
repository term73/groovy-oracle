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

package de.gluehloch.groovy.oracle.inout

import org.apache.commons.lang.StringUtils

import org.junit.Test
/**
 * Test of class TextFileExporter.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class DataTest {

	 @Test
	 void testDataToText() {
		 def data = new Data()
		 assert 'value_1|value_2' == data.toText(['col_1': 'value_1', 'col_2': 'value_2'])
		 assert "value_1||value_3" == data.toText([col_1: 'value_1', col_2: null, col_3: 'value_3'])
		 assert "value_1|value_2|" == data.toText([col_1: 'value_1', col_2: 'value_2', col_3: null])
		 assert "|value_2|value_3" == data.toText([col_1: null, col_2: 'value_2', col_3: 'value_3'])
	 }

	 @Test
	 void testDataExport() {
         def data = Data.createData('tableName', {
	         [
	             [col_1: 'value_1',  col_2: 'value_2'],
	             [col_1: 'value_3',  col_2: 'value_4'],
	             [col_1: 'value_5',  col_2: 'value_6'],
	             [col_1: 'value_7',  col_2: 'value_8',  col3: 'value9'],
	             [col_1: 'value_7',  col_2: null,       col3: 'value9'],
	             [col_1: 'value_10', col_2: 'value_11', col3: null],
	             [col_1: null, col_2: 'value_12', col3: 'value_13'],
	             [col_1: 'value_10', col_2: null, col3: null],
	             [col_1: null, col_2: null, col3: null]
	         ]
	     })

         data.export('testexport.txt')
	 }

	 @Test
	 void testDataSplitText() {
		 assert StringUtils.splitPreserveAllTokens('|a|b|c|', '|') == ['', 'a', 'b', 'c', '']
	     assert StringUtils.splitPreserveAllTokens('a|b|c|', '|') == ['a', 'b', 'c', '']
	 }

	 @Test
	 void testDataToData() {
		 def data = new Data()
		 def dataMap = data.toData('a|b|c', ['col_a', 'col_b', 'col_c'])
		 assert dataMap == ['col_a': 'a', 'col_b': 'b', 'col_c': 'c']
	 }

}
