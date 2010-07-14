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

import groovy.sql.Sql

/**
 * Factory für das Erstellen von OracleTable Objekten.
 *
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class OracleMetaDataFactory {
    
    private final OracleColumnFinder ocf = new OracleColumnFinder()
    
    /**
     * Create an OracleSchema object.
     * 
     * @param sql The database connection.
     * @return An oracle schema.
     */
    def createOracleSchema(Sql sql) {
        def os = new OracleSchema(
            sql: sql,
            tables: new OracleColumnFinder().getTables(sql)
        )
        return os
    }
    
    /**
     * Liefert die Tabellenbeschreibungen für eine Liste von Tabellennamen.
     *
     * @param sql Verbindung zur Datenbank.
     * @param tableNames Die Namen der Tabellen.
     * @return Eine Liste von OracleTables.
     */
    def createOracleTables(Sql sql, List tableNames) {
        def oracleTables = []
        tableNames.each { tableName ->
            oracleTables.add(createOracleTable(sql, tableName))
        }
        return oracleTables
    }
    
    /**
     * Liefert eine Tabellenbeschreibung.
     *
     * @param sql Verbindung zur Datenbank.
     * @param tableName Der Name der Tabelle.
     * @return Ein OracleTable.
     */
    OracleTable createOracleTable(Sql sql, String tableName) {
        def columnData = ocf.getColumns(sql, tableName)
        if (columnData == null || columnData.size() == 0) {
            throw new IllegalArgumentException("Unknown table ${tableName}")
        }

        def constraint = ocf.getConstraint(sql, tableName)
        def ot = new OracleTable(tableName: tableName,
                columnMetaData: columnData, constraint: constraint)
        return ot
    }
    
}
