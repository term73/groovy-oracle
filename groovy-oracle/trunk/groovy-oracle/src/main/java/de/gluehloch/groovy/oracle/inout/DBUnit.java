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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
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
	 * @param ds Eine Oracle {@link DataSource}.
	 * @param schema Schema Name.
	 * @param tableNames Die zu exportierenden Tabellen.
	 * @param exportFile In diese Datei das Exportergebnis schreiben.
	 * @throws Exception Da ging was schief.
	 */
	public static void xmlExport(final DataSource ds, final String schema,
			final String[] tableNames, final File exportFile) throws Exception {

		DataSourceDatabaseTester jdbcDatabaseTester = new DataSourceDatabaseTester(
			ds);
		jdbcDatabaseTester.setSchema(schema);
		IDatabaseConnection iDatabaseConnection = jdbcDatabaseTester
			.getConnection();
		DatabaseConfig config = iDatabaseConnection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());

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
	 * @param ds Eine Oracle {@link DataSource}.
	 * @param schema Schema Name.
	 * @param importFile Aus dieser Datei die Importinformationen holen.
	 * @throws Exception Da ging was schief.
	 */
	public static void xmlImport(final DataSource ds, final String schema,
			final File importFile) throws Exception {

		DataSourceDatabaseTester jdbcDatabaseTester = new DataSourceDatabaseTester(
			ds);
		jdbcDatabaseTester.setSchema(schema);
		IDatabaseConnection iDatabaseConnection = jdbcDatabaseTester
			.getConnection();
		DatabaseConfig config = iDatabaseConnection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new OracleDataTypeFactory());

//		IDatabaseConnection connection = new DatabaseConnection(iDatabaseConnection.getConnection(), schema);
//		DatabaseConfig config = connection.getConfig();

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
