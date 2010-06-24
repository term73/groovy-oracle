/*
 * $Id$
 * ============================================================================
 * Project grooocle
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

package de.awtools.grooocle.inout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

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
 * @author  $Author$
 * @version $Revision$ $Date$
 */
public class DBUnit {

	public enum DBUnitOperation {
		INSERT(DatabaseOperation.INSERT), CLEAN_INSERT(
				DatabaseOperation.CLEAN_INSERT), UPDATE(
				DatabaseOperation.UPDATE);

		private final DatabaseOperation operation;

		private DBUnitOperation(DatabaseOperation _operation) {
			operation = _operation;
		}

		DatabaseOperation getDatabaseOperation() {
			return operation;
		}
	};

	/**
	 * Exports a given table.
	 * 
	 * @param ds Oracle DB {@link DataSource}.
	 * @param schema schema name.
	 * @param tableNames Table for export.
	 * @param exportFile Write export data to this file.
	 * @throws Exception Some problems.
	 */
	public static void xmlExport(final DataSource ds, final String schema,
			final String[] tableNames, final File exportFile) throws Exception {

		DataSourceDatabaseTester jdbcDatabaseTester = new DataSourceDatabaseTester(
			ds);
		jdbcDatabaseTester.setSchema(schema);
		IDatabaseConnection iDatabaseConnection = jdbcDatabaseTester
			.getConnection();
		DatabaseConfig config = iDatabaseConnection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
			new OracleDataTypeFactory());

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
	 * Import of xml data. No data will be deleted!
	 * 
	 * @param ds An Oracle {@link DataSource}.
	 * @param schema Schema name.
	 * @param importFile import file.
	 * @param operation database operation
	 * @throws Exception something is not OK.
	 */
	public static void xmlImportInsert(final DataSource ds,
			final String schema, final File importFile,
			final DBUnitOperation operation) throws Exception {

		xmlImport(ds, schema, importFile, operation);
	}

	/**
	 * Import of xml data.
	 * 
	 * @param ds An Oracle {@link DataSource}.
	 * @param schema Schema name.
	 * @param importFile import file.
	 * @param operation database operation
	 * @throws Exception something is not OK.
	 */
	public static void xmlImport(final DataSource ds, final String schema,
			final File importFile, final DBUnitOperation operation)
		throws Exception {

		File dtdFile = new File(importFile.getAbsoluteFile() + ".dtd");
		xmlImport(ds, schema, new FileInputStream(importFile),
			new FileInputStream(dtdFile), operation);
	}

	/**
	 * Import of xml data.
	 * 
	 * @param ds An Oracle {@link DataSource}.
	 * @param schema Schema name.
	 * @param xmlInputStream XML import stream.
	 * @param dtdInputStream DTD for the xml stream.
	 * @param operation database operation CLEAN_INSERT or INSERT
	 * @throws Exception something is not OK.
	 */
	public static void xmlImport(final DataSource ds, final String schema,
			final InputStream xmlInputStream, final InputStream dtdInputStream,
			final DBUnitOperation operation) throws Exception {

		DataSourceDatabaseTester jdbcDatabaseTester = new DataSourceDatabaseTester(
			ds);
		jdbcDatabaseTester.setSchema(schema);
		IDatabaseConnection iDatabaseConnection = jdbcDatabaseTester
			.getConnection();
		DatabaseConfig config = iDatabaseConnection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
			new OracleDataTypeFactory());

		try {
			FlatXmlDataSet fxdt = new FlatXmlDataSet(xmlInputStream,
				dtdInputStream);

			jdbcDatabaseTester.setDataSet(fxdt);
			jdbcDatabaseTester.setSetUpOperation(operation
				.getDatabaseOperation());

			operation.getDatabaseOperation().execute(iDatabaseConnection, fxdt);
		} finally {
			jdbcDatabaseTester.closeConnection(iDatabaseConnection);
		}
	}

}
