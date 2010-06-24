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

import de.awtools.grooocle.OraUtils

import groovy.sql.Sql

/**
 * Präpariert die Datenbank zur Ausführung von Unit-Tests. Vorausgesetzt
 * wird ein Account TOOLBOXTEST auf der TITICACA. Wird das Skript mit dem
 * Parameter 'clean' aufgerufen, werden die Tabellen des Benutzers gelöscht.
 *
 * @author  $Author$
 * @version $Revision$ $Date$
 */
class PrepareUnitTestDatabase {

    def sql

    static void main(String[] args) {
        def putd = new PrepareUnitTestDatabase(sql: TestDatabaseUtility.createConnection())

        try {
            if (args.length > 0 && args[0] == "clean") {
                putd.cleanUp()
                println "Database deleted!"
            } else {
                putd.setUp()
                println "Database created!"
            }
        } finally {
            putd.sql?.close()
        }
    }

    void setUp() {
    	sql.execute("""CREATE SEQUENCE "XXX_SQ" 
                           START WITH 1 
                           CACHE 20
                           MINVALUE 1
                           MAXVALUE 99999999999
                           CYCLE""")
        sql.execute("""CREATE TABLE XXX_TEST_RUN (
                           ID NUMBER(38,0),
                           TRIGGER_TYPE CHAR(1 BYTE) NOT NULL ENABLE,
                           STICHTAG DATE NOT NULL ENABLE,
                           DB_USER VARCHAR2(256 BYTE),
                           DATUM_START TIMESTAMP (6) DEFAULT SYSTIMESTAMP,
                           VKEY_BL VARCHAR2(10 BYTE),
                           BL_RUN_ID NUMBER(38,0),
                           V_NUMERIC NUMBER(10,3),
                           CONSTRAINT PK_XXX_TEST_RUN PRIMARY KEY (ID))""")
        sql.execute("""CREATE TABLE XXX_HIERARCHIE (
                           TEST_ID NUMBER(38,0) NOT NULL ENABLE,
                           FK_RUN_ID NUMBER(38,0) NOT NULL ENABLE,
                           VEKNKDNR VARCHAR2(16 BYTE),
                           KDNR NUMBER(11,0),
                           DWHKNKEN NUMBER(15,0),
                           GBKDNR NUMBER(11,0),
                           CONSTRAINT PK_XXX_HIERARCHIE PRIMARY KEY (VEKNKDNR, TEST_ID),
                           CONSTRAINT FK_XXX_HIERARCHIE FOREIGN KEY (FK_RUN_ID) REFERENCES XXX_TEST_RUN (ID))""")
        sql.execute("""CREATE TABLE XXX_KUNDE (
                           TEST_ID NUMBER(38,0) NOT NULL ENABLE,
                           FK_RUN_ID NUMBER(38,0) NOT NULL ENABLE,
                           VEKNKDNR VARCHAR2(16 BYTE),
                           KDNR NUMBER(11,0),
                           DWHKNKEN NUMBER(15,0),
                           GBKDNR NUMBER(11,0),
                           CONSTRAINT PK_XXX_KUNDE PRIMARY KEY (VEKNKDNR, KDNR, TEST_ID),
                           CONSTRAINT FK_XXX_KUNDE FOREIGN KEY (FK_RUN_ID) REFERENCES XXX_TEST_RUN (ID),
                           CONSTRAINT FK_XXX_KUNDE_HIERARCHIE FOREIGN KEY (VEKNKDNR, TEST_ID) REFERENCES XXX_HIERARCHIE (VEKNKDNR, TEST_ID))""")
        sql.execute("""insert into XXX_TEST_RUN(
                           ID,
                           TRIGGER_TYPE,
                           STICHTAG,
                           DB_USER,
                           DATUM_START,
                           VKEY_BL,
                           BL_RUN_ID,
                           V_NUMERIC)
                       values(
                           1,
                           'M',
                           to_date('29.03.1971', 'dd.mm.yyyy'),
                           'eh2wdre',
                           to_date('29.03.1971', 'dd.mm.yyyy'),
                           4711,
                           1001,
                           123.456)""")
        sql.execute("""insert into XXX_TEST_RUN(
                           ID,
                           TRIGGER_TYPE,
                           STICHTAG,
                           DB_USER,
                           DATUM_START,
                           VKEY_BL,
                           BL_RUN_ID,
                           V_NUMERIC)
                       values(
                           2,
                           'M',
                           to_date('30.03.1971', 'dd.mm.yyyy'),
                           'eh2wdre',
                           to_date('30.03.1971', 'dd.mm.yyyy'),
                           4711,
                           1001,
                           666.6262)""")
        sql.execute("""insert into XXX_HIERARCHIE(
                           TEST_ID,
                           FK_RUN_ID,
                           VEKNKDNR,
                           KDNR,
                           DWHKNKEN,
                           GBKDNR)
                       values(
                           1,
                           1,
                           '4711',
                           4712,
                           4713,
                           4714)""")
         sql.execute("""CREATE TABLE XXX_TEST_RUN_2 (
                          ID NUMBER(38,0),
                          TRIGGER_TYPE CHAR(1 BYTE) NOT NULL ENABLE,
                          STICHTAG DATE NOT NULL ENABLE,
                          DB_USER VARCHAR2(256 BYTE),
                          DATUM_START TIMESTAMP (6) DEFAULT SYSTIMESTAMP,
                          VKEY_BL VARCHAR2(10 BYTE),
                          BL_RUN_ID NUMBER(38,0),
                          V_NUMERIC NUMBER(10,3),
                          CONSTRAINT PK_XXX_TEST_RUN_2 PRIMARY KEY (ID))""")
         sql.execute("""CREATE TABLE XXX_TEST_RUN_3 (
                          ID NUMBER(38,0),
                          TRIGGER_TYPE CHAR(1 BYTE) NOT NULL ENABLE,
                          STICHTAG DATE NOT NULL ENABLE,
                          DB_USER VARCHAR2(256 BYTE),
                          DATUM_START TIMESTAMP (6) DEFAULT SYSTIMESTAMP,
                          VKEY_BL VARCHAR2(10 BYTE),
                          BL_RUN_ID NUMBER(38,0),
                          V_NUMERIC NUMBER(10,3),
        		          SOME_NEW_COLUMN VARCHAR2(20 BYTE),
                          CONSTRAINT PK_XXX_TEST_RUN_3 PRIMARY KEY (ID))""")
    }

    void cleanUp() {
    	sql.execute("drop table XXX_KUNDE cascade constraints")
        sql.execute("drop table XXX_HIERARCHIE cascade constraints")
        sql.execute("drop table XXX_TEST_RUN cascade constraints")
        sql.execute("drop table XXX_TEST_RUN_2 cascade constraints")
		sql.execute("drop table XXX_TEST_RUN_3 cascade constraints")
        sql.execute("drop sequence XXX_SQ")
        OraUtils.purgeRecyclebin(sql)
    }

}
