/*
 * $Id: OracleSchema.groovy 118 2009-03-17 14:46:40Z andre.winkler@web.de $
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

package de.gluehloch.groovy.oracle.meta

/**
 * Liefert die Namen der Oracle Sequenzen.
 * 
 * @author  $Author: andre.winkler@web.de $
 * @version $Revision: 104 $ $Date: 2009-03-04 15:17:30 +0100 (Mi, 04 Mrz 2009) $
 */
class OracleSequenceFinder {
	
	/**
	 * Liefert eine Liste aller Sequenzen.
	 *
	 * @param sql Ein Groovy SQL
	 * @return Eine Liste von Sequenz-Namen.
	 */
	def getSequences(def sql) {
		def sequences = []
		sql.eachRow("""
            select
                object_name as name
            from
                user_objects
            where
                object_type = 'SEQUENCE'
            order by
                object_name
        """) {
			sequences << it.name
		}
		return sequences
	}
	
}
