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

import org.apache.commons.lang.StringUtils

/**
 * Verwaltet die Daten einer Oracle Datenbanktabelle.
 */
class OracleTable {

	def nl = System.getProperty("line.separator");

    String tableName;

    /** Eine Liste mit OracleColumn Einträgen. */
    def columnMetaData = [];

    /** Ein OracleConstraint Objekt. */
    def constraint;

    /**
     * Kopiert die Eigenschaften von diesem Objekt in ein neu erstelltes
     * Objekt 'OracleTable'
     *
     * @return Ein geclonter OracleTable.
     */
    def copy() {
        def clone = new OracleTable(tableName: tableName,
        		constraint: constraint?.copy())

        columnMetaData.each {
        	clone.columnMetaData << it.copy()
        }
        return clone
    }

    /**
     * Liefert eine kommaseparierte Aufzählung aller Spalten.
     * Z.B: attr1, attr2, ..., attrN
     */
    def toColumnList() {
        return columnMetaData.collect { it.columnName }.join(", ")
    }

    /**
     * Liefert eine kommaseparierte Aufzählung aller Spalten.
     * Z.B: attr1, attr2, ..., attrN
     *
     * @param ignoreColumns Eine Liste der zu ignorierenden Spalten.
     */
    def toColumnList(def ignoreColumns) {
        def columns = columnMetaData.collect { it.columnName }
        (columns - ignoreColumns).join(", ")
    }

    /**
     * Liefert einen 'CREATE TABLE...' Befehl zur Neuanlage dieser Oracle
     * Datenbanktabelle. Der Befehl wird ohne ';' abgeschlossen.
     *
     * @return Ein Skript zur Neuanlage einer Datenbanktabelle ohne Primary-Key
     *     und Foreign-Key Befehle.
     */
    def toScript() {
         def snippet = "CREATE TABLE \"${tableName}\"(${nl}";
         columnMetaData.eachWithIndex { column, index ->
             snippet = snippet + "\t${column.toScript()}";
             if (index + 1 < columnMetaData.size()) {
            	 snippet = snippet + ",${nl}"
             } else {
            	 snippet = snippet + "${nl})"
             }
         }
         return snippet;
     }

    boolean equals(def object) {
        if (!(object instanceof OracleTable)) {
            return false
        }

        def result = true
        result = result && (tableName == object.tableName)
        result = result && (columnMetaData == object.columnMetaData)
        result = result && (constraint == object.constraint)

        return result
    }

    String toString() {
        return toScript().toString();
    }

}