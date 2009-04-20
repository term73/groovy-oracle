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

package de.gluehloch.groovy.oracle.inout;

import groovy.sql.Sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.dbunit.operation.DatabaseOperation;

/**
 * TODO
 * 
 * @author  $Author: andre.winkler@web.de $
 * @version $Revision: 104 $ $Date: 2009-03-04 15:17:30 +0100 (Mi, 04 Mrz 2009) $
 */
public class DBUnit {

	/**
	 * Exportiert die angegebenen Tabellen.
	 * 
	 * @param tableNames Die zu exportierenden Tabellen.
	 * @param exportFile In diese Datei das Exportergebnis schreiben.
	 * @throws Exception Da ging was schief.
	 */
	public static void xmlExport(final DataSource ds,
			final String[] tableNames, final File exportFile) throws Exception {

		//		JdbcDatabaseTester t = new JdbcDatabaseTester(driver, url, user, password, schema);
		DataSourceDatabaseTester jdbcDatabaseTester = new DataSourceDatabaseTester(
			ds);
		jdbcDatabaseTester.setSchema("TEST");
		IDatabaseConnection iDatabaseConnection = jdbcDatabaseTester
			.getConnection();

		try {
			ITableFilter databaseSequenceFilter = new DatabaseSequenceFilter(
				iDatabaseConnection, tableNames);

			IDataSet dataset = new FilteredDataSet(databaseSequenceFilter,
				iDatabaseConnection.createDataSet());

			File dtdFile = new File(exportFile.getAbsoluteFile() + ".dtd");
			FlatDtdDataSet.write(dataset, new FileOutputStream(dtdFile));
			FlatXmlWriter writer = new FlatXmlWriter(new FileOutputStream(
				exportFile));
			writer.setDocType(dtdFile.getName());
			writer.write(dataset);
		} finally {
			jdbcDatabaseTester.closeConnection(iDatabaseConnection);
		}
	}

	/**
	 * Importiert die angegebenen XML-Datei.
	 * 
	 * @param importFile Aus dieser Datei die Importinformationen holen.
	 * @throws Exception Da ging was schief.
	 */
	public static void xmlImport(final DataSource ds, final File importFile)
		throws Exception {

		DataSourceDatabaseTester jdbcDatabaseTester = new DataSourceDatabaseTester(
			ds);
		IDatabaseConnection iDatabaseConnection = jdbcDatabaseTester
			.getConnection();

		try {
			File dtdFile = new File(importFile.getAbsoluteFile() + ".dtd");
			FlatXmlDataSet fxdt = new FlatXmlDataSet(new FileInputStream(
				importFile), new FileInputStream(dtdFile));

			jdbcDatabaseTester.setDataSet(fxdt);
			jdbcDatabaseTester
				.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);

			DatabaseOperation.CLEAN_INSERT.execute(iDatabaseConnection, fxdt);
		} finally {
			jdbcDatabaseTester.closeConnection(iDatabaseConnection);
		}
	}

}
