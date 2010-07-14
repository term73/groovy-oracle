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

import groovy.sql.Sql;

/**
 * Holds the meta informations of a database schema.
 *
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class OracleSchema {

    /**
     * The Groovy SQL connection to the database. This connection was used to
     * collect the meta data.
     */
    Sql sql

    /**
     * Oracle table informations. The table name is the key and the object
     * {@link OracleTable} is the value.
     */
	def tables = [:]

    /**
     * Oracle sequences.
     */
    def sequences = []

    /**
     * Holds all primary keys of the schema. The key is the name of the
     * primary key and the object {@link PrimaryKey} is the value.
     */
    def primaryKeys = [:]

}
