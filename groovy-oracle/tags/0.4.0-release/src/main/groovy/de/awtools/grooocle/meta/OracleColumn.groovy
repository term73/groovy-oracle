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

import org.apache.commons.lang.StringUtils;

/**
 * Verwaltet die Meta-Informationen einer Oracle Datenbankspalte.<br>
 *
 * Anmerkung: Alle Bezeichner sind in GROSSBUCHSTABEN in den Oracle
 * Schema-Tabellen abgelegt.
 */
class OracleColumn {
    
    String columnName;
    String dataType;
    Integer dataLength;
    Integer dataPrecision;
    Integer dataScale;
    boolean nullable;
    Integer columnId;
    String dataDefault;
    
    /**
     * Kopiert die Eigenschaften von diesem Objekt in ein neu erstelltes
     * Objekt 'OracleColumn'
     *
     * @return Ein geclonter OracleColumn.
     */
    def copy() {
        def clone = new OracleColumn(columnName: columnName,
            dataType: dataType, dataLength: dataLength,
            dataPrecision: dataPrecision, dataScale: dataScale,
            nullable: nullable, columnId: columnId,
            dataDefault: dataDefault)
        return clone
    }
    
    /**
     * Liefert den SQL Typ dieser Spalte.
     *
     * @return Der SQL Typ der Spalte.
     */
    def toType() {
        def snippet = ''
        if (dataType.startsWith("TIMESTAMP")) {
            dataType = "TIMESTAMP"
        }

        switch (dataType) {
            case 'VARCHAR2':
                snippet += dataType + "(${dataLength} BYTE)"
                break
            case 'CHAR':
                snippet += dataType + "(${dataLength} BYTE)"
                break
            case 'DATE':
                snippet += dataType
                break
            case 'TIMESTAMP':
                snippet += dataType + "(${dataScale})"
                break
            case 'NUMBER':
                snippet += dataType
                if (dataPrecision != null && dataScale != null) {
                    snippet += "(${dataPrecision},${dataScale})"
                }
                break
            case 'UROWID':
                snippet += dataType + "(${dataLength})"
                break
            default:
                throw new RuntimeException("Unknown datatype: ${dataType}.")
        }
        return snippet
    }

    def isNumber() {
        def number = false
        if (dataType.startsWith("TIMESTAMP")) {
            dataType = "TIMESTAMP"
        }

        return (dataType == 'NUMBER' || dataType == 'UROWID')
    }
    
    def isDate() {
        return (dataType == 'DATE' || dataType == 'TIMESTAMP')
    }

    def isString() {
        return (dataType == 'VARCHAR2' || dataType == 'CHAR')
    }

    /**
     * Liefert die Beschreibung der Datenbankspalte. Diese Beschreibung kann
     * in einem 'CREATE TABLE' Befehl eingesetzt werden.
     *
     * @return SQL Beschreibung der Spalte.
     */
    def toScript() {
        if (!columnName)
            throw new IllegalArgumentException("ColumnName not specified!")
        
        def snippet = "\"${columnName}\" "
        snippet += toType()
        
        if (dataDefault != null) {
            snippet = snippet + " DEFAULT " + dataDefault
        }
        
        if (!nullable) {
            snippet = snippet + " NOT NULL ENABLE"
        }
        
        return snippet;
    }

    String toString() {
        return toScript().toString()
    }

    boolean equals(def object) {
        if (!(object instanceof OracleColumn)) {
            return false
        }
        
        def result = true
        result = result && (columnName == object.columnName)
        result = result && (dataType == object.dataType)
        result = result && (dataLength == object.dataLength)
        result = result && (dataScale == object.dataScale)
        result = result && (nullable == object.nullable)
        result = result && (columnId == object.columnId)
        result = result && (dataDefault == object.dataDefault)
        
        return result
    }
    
}
