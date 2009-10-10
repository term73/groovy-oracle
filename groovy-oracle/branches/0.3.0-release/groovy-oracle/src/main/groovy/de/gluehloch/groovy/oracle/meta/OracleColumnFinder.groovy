/*
 * $Id: OracleColumnFinder.groovy 135 2009-04-14 18:02:37Z andre.winkler@web.de $
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
 * Ermittelt die Spalten und Constraints einer Datenbanktabelle.
 */
class OracleColumnFinder {

    /**
     * Liefert eine Liste aller Spaltenbeschreibungen einer Oracle Datenbank
     * Tabelle. Es wird eine leere Liste zurückgeliefert, wenn eine
     * enstprechende Tabelle nicht gefunden werden konnte.
     *
     * @param sql Ein Groovy SQL
     * @param tableName Der Name der Tabelle.
     * @return Eine Liste von OracleColumns.
     */
    def getColumns(def sql, def tableName) {
        def columns = []
        sql.eachRow("select * from user_tab_columns where table_name = ${tableName} order by column_id") {
            def oc = new OracleColumn(columnName: it.column_name,
                    dataType: it.data_type, dataLength: it.data_length,
                    dataPrecision: it.data_precision, dataScale: it.data_scale,
                    nullable: it.nullable, columnId: it.column_id,
                    dataDefault: it.data_default);
            columns.add(oc);
        }
        return columns
    }

    /**
     * Liefert die Constraints zu einer Oracle Datenbank Tabelle.
     *
     * @param sql Ein Groovy SQL
     * @param tableName Der Name der Tabelle.
     * @return Eine Liste von OracleConstraint (Primary- und Foreign Keys).
     */
    def getConstraint(def sql, def tableName) {
        def query = """
            SELECT
                a.constraint_name, a.constraint_type, a.table_name,
                a.search_condition, a.r_constraint_name,
                b.position, b.column_name,
                c.constraint_name as ref_constraint, c.table_name as ref_table
            FROM
                user_cons_columns b,
                user_constraints a
                LEFT OUTER JOIN user_constraints c ON (c.constraint_name = a.r_constraint_name)
            WHERE
                a.constraint_name = b.constraint_name
                AND a.table_name = ${tableName}
                AND a.constraint_type IN ('R', 'P')
            ORDER BY
                a.constraint_type,
                a.constraint_name,
                b.position
        """

        OracleConstraint constraint = new OracleConstraint(tableName : tableName);

        sql.eachRow(query) {
            if (it.constraint_type == "P") {
                if (constraint.primaryKey == null) {
                    constraint.primaryKey = new PrimaryKey(name: it.constraint_name);
                }
                constraint.primaryKey.columnNames.add(it.column_name);

            } else if (it.constraint_type == "R") {
                def constraintName = it.constraint_name;
                ForeignKey foreignKey = constraint.foreignKeys.find { fk -> fk.name == constraintName }

                if (!foreignKey) {
                    foreignKey = new ForeignKey(name: it.constraint_name,
                            rConstraintName: it.r_constraint_name,
                            referencedTableName: it.ref_table);
                    constraint.foreignKeys.add(foreignKey);
                }

                foreignKey.columnNames.add(it.column_name);
            }
        }

        constraint.foreignKeys.each { foreignKey ->
            // Die referenzierten Spalten identifizieren.
            def pkQuery = """
                SELECT
                    a.constraint_name, a.constraint_type, a.table_name,
                    b.column_name, a.search_condition, a.r_constraint_name
                FROM
                    user_constraints a, user_cons_columns b
                WHERE
                      a.constraint_name = b.constraint_name 
                  AND b.constraint_name = ${foreignKey.rConstraintName}
                ORDER BY position
            """
            sql.eachRow(pkQuery) { row ->
                foreignKey.referencedColumnNames.add(row.column_name);
            }
        }
       
        return constraint;
    }

}
