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

package de.gluehloch.groovy.oracle.varray;

import static org.junit.Assert.assertEquals;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.lang.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testet den Umgang mit Oracles VARRAYs.
 * <pre>Code in Pre</pre>
 * Oder direkt im Text: <code>Code in Code</code>.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
public class VarrayTest {

	private static final String ORACLE_URL = "jdbc:oracle:thin:${user}/${password}@${url}";

	private static Connection conn;

	@Test
	public void testOraclesVarray() throws Exception {
		CallableStatement cs = null;
		try {
			String arrayElements[] = { "Test3", "Test4", "Test5" };
			ArrayDescriptor desc = ArrayDescriptor.createDescriptor(
				"T_STRING_VARRAY", conn);
			ARRAY newArray = new ARRAY(desc, conn, arrayElements);

			String spCall = "{ call call_me(?, ?) }";
			cs = conn.prepareCall(spCall);
			cs.setArray(1, newArray);
			cs.registerOutParameter(2, java.sql.Types.INTEGER);

			cs.execute();
			assertEquals(3, cs.getInt(2));
		} finally {
			if (cs != null) {
				cs.close();
			}
		}

	}

	@Test @Ignore
	public void testOraclesVarrayWithPackage() throws Exception {
		CallableStatement cs = null;
		try {
			String arrayElements[] = { "Test3", "Test4", "Test5" };
//			int n = OracleTypeCOLLECTION.TYPE_PLSQL_INDEX_TABLE;
//			ArrayDescriptor desc = ArrayDescriptor.createDescriptor();
			ArrayDescriptor desc = ArrayDescriptor.createDescriptor(
				"CP_TEST.t_ROWNUMBER", conn);
			ARRAY newArray = new ARRAY(desc, conn, arrayElements);

			String spCall = "{ call CP_TEST.call_me(?, ?) }";
			cs = conn.prepareCall(spCall);
			cs.setArray(1, newArray);
			cs.registerOutParameter(2, java.sql.Types.INTEGER);

			cs.execute();
			assertEquals(3, cs.getInt(2));
		} finally {
			if (cs != null) {
				cs.close();
			}
		}

	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		String[] tokens = new String[] { "${user}", "${pwd}", "${url}" };

		String user = System.getProperty("groovy.oracle.test.user");
		String pwd = System.getProperty("groovy.oracle.test.password");
		String url = System.getProperty("groovy.oracle.test.url");

		String[] replacements = new String[] { user, pwd, url };

		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		String oracleUrl = StringUtils.replaceEach(ORACLE_URL, tokens,
			replacements);

		conn = DriverManager.getConnection(oracleUrl, user, pwd);
		conn.setAutoCommit(false);

		Statement stmt = conn.createStatement();
		stmt
			.execute("CREATE TYPE t_string_varray AS VARRAY(10) OF VARCHAR2(100)");
		stmt
			.execute("CREATE PROCEDURE call_me(p_strings IN t_string_varray, p_retcode OUT NUMBER) IS BEGIN p_retcode := p_strings.COUNT; END call_me;");
		// Doesn´t work:
//		stmt.execute("CREATE OR REPLACE PACKAGE TEST_PACKAGE IS PROCEDURE log(p_stichmon IN NUMBER); END;");
//		stmt.execute("CREATE OR REPLACE PACKAGE BODY TEST_PACKAGE IS PROCEDURE log(p_stichmon IN NUMBER) IS BEGIN NULL; END log; END ;");
		stmt.execute("ALTER PROCEDURE call_me COMPILE");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		try {
			if (conn != null) {
				Statement stmt = conn.createStatement();
				stmt.execute("DROP PROCEDURE call_me");
				stmt.execute("DROP TYPE t_string_varray");
			}
		} finally {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		}
	}

}
