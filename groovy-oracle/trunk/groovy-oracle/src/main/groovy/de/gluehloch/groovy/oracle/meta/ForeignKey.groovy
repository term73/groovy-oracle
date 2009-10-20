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

/**
 * Informationen über einen Fremdschlüsselbeziehung in einer Oracle-Datenbank.
 */
class ForeignKey {

    String name
    String rConstraintName
    def columnNames = []
    String tableName
    String referencedTableName
    def referencedColumnNames = []

    /**
     * Kopiert die Eigenschaften von diesem Objekt in ein neue erstelltes
     * Objekt 'ForeignKey'
     *
     * @return Ein geclonter ForeignKey.
     */
    def copy() {
        def foreignKey = new ForeignKey(name: name,
                rConstraintName: rConstraintName,
                tableName: tableName,
                referencedTableName: referencedTableName)

        def columnNames2 = []
        columnNames.each { columnNames2 << it }

        def referencedColumnNames2 = []
        referencedColumnNames.each { referencedColumnNames2 << it }

        foreignKey.columnNames = columnNames2
        foreignKey.referencedColumnNames = referencedColumnNames2

        return foreignKey
    }

    String toString() {
    	return "${tableName}#${name}" 
    }

    boolean equals(def object) {
        if (!(object instanceof ForeignKey)) {
            return false
        }

        def result = true
        result = result && (name == object.name)
        result = result && (rConstraintName == object.rConstraintName)
        result = result && (tableName == object.tableName)
        result = result && (referencedTableName == object.referencedTableName)
        result = result && (columnNames == object.columnNames) 
        result = result && (referencedColumnNames == object.referencedColumnNames)

        return result
    }

}
