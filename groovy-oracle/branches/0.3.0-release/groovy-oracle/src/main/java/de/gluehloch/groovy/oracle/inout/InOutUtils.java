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

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class for IO processing of database data.
 * 
 * @author $Author$
 * @version $Revision$ $Date: 2009-04-17 17:44:57 +0200 (Fr, 17 Apr 2009)$
 */
public class InOutUtils {

	/*
	 * Konvertierungsformat Oracle-Date-Type -> String
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/*
	 * Konvertierungsformat String -> Oracle-Date-Type
	 */
	public static final String ORACLE_DATE_FORMAT = "yyyy-MM-dd HH24:MI:ss";

	/**
	 * Convert a string into a date type. Throws a runtime exception if string
	 * is unparseable.
	 * 
	 * @param _text
	 *            The string to parse.
	 * 
	 * @return The date object.
	 */
	public static Date toDate(final String _text) {
		return (toDate(_text, DATE_FORMAT));
	}

	/**
	 * Convert a string into a date type. Throws a runtime exception if string
	 * is not to parse.
	 * 
	 * @param _text
	 *            The string to parse.
	 * 
	 * @param _dateForamt
	 *            A format like <code>yyyy-MM-dd HH24:MI:ss</code>
	 * 
	 * @return The date object.
	 */
	public static Date toDate(final String _text, final String _dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(_dateFormat);
		try {
			return formatter.parse(_text);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Convert a date type into a <code>java.sql.Date</code>.
	 * 
	 * @param _date
	 *            The date to convert.
	 * 
	 * @return The java.sql.Date object.
	 */
	public static java.sql.Date toSqlDate(final Date _date) {
		return new java.sql.Date(_date.getTime());
	}

	/**
	 * Convert a string into a <code>java.sql.Date</code>.
	 * 
	 * @param _text
	 *            The string to parse.
	 * 
	 * @return The java.sql.Date object.
	 */
	public static java.sql.Date toSqlDate(final String _text) {
		return toSqlDate(toDate(_text));
	}

	/**
	 * Convert a string into a <code>java.sql.Date</code>.
	 * 
	 * @param _text
	 *            The string to parse.
	 * 
	 * @return The java.sql.Date object.
	 */
	public static java.sql.Date toSqlDate(final String _text,
			final String _dateFormat) {
		return toSqlDate(toDate(_text, _dateFormat));
	}

	/**
	 * Convert a date type into a <code>oracle.sql.DATE</code>.
	 * 
	 * @param _date
	 *            The date to convert.
	 * 
	 * @return The java.sql.Date object.
	 */
	public static oracle.sql.DATE toOracleDate(final Date _date) {
		return new oracle.sql.DATE(toSqlDate(_date));
	}

	/**
	 * Convert a string into a <code>oracle.sql.DATE</code>.
	 * 
	 * @param _text
	 *            The string to parse.
	 * 
	 * @return The java.sql.Date object.
	 */
	public static oracle.sql.DATE toOracleDate(final String _text) {
		return new oracle.sql.DATE(toSqlDate(_text));
	}

	/**
	 * Convert a string into a <code>oracle.sql.DATE</code>.
	 * 
	 * @param _text
	 *            The string to parse.
	 * @param _dateFormat
	 *            The date format.
	 * 
	 * @return The java.sql.Date object.
	 */
	public static oracle.sql.DATE toOracleDate(final String _text,
			final String _dateFormat) {

		return new oracle.sql.DATE(toSqlDate(_text, _dateFormat));
	}

	/**
	 * Convert a date type into a <code>oracle.sql.TIMESTAMP</code>.
	 * 
	 * @param _date
	 *            The date type to convert.
	 * 
	 * @return A oracle.sql.TIMESTAMP object.
	 */
	public static oracle.sql.TIMESTAMP toOracleTimeStamp(final Date _date) {
		return new oracle.sql.TIMESTAMP(toOracleDate(_date));
	}

	/**
	 * Convert a date type into a <code>oracle.sql.TIMESTAMP</code>.
	 * 
	 * @param _text
	 *            The string to parse.
	 * 
	 * @return A oracle.sql.TIMESTAMP object.
	 */
	public static oracle.sql.TIMESTAMP toOracleTimeStamp(final String _text) {
		return new oracle.sql.TIMESTAMP(toOracleDate(_text));
	}

	/**
	 * Convert a date type into a string.
	 * 
	 * @param _date
	 *            The date to convert.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final Date _date) {
		return toString(_date, DATE_FORMAT);
	}

	/**
	 * Convert a date type into a string.
	 * 
	 * @param _date
	 *            The date to convert.
	 * @param _dateFormat
	 *            The date format.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final Date _date, final String _dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(_dateFormat);
		return formatter.format(_date);
	}

	/**
	 * Convert a java.sql.Date type into a string.
	 * 
	 * @param _date
	 *            The date to convert.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final java.sql.Date _date) {
		return toString(_date, DATE_FORMAT);
	}

	/**
	 * Convert a java.sql.Date type into a string.
	 * 
	 * @param _date
	 *            The date to convert.
	 * @param _dateFormat
	 *            The date format.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final java.sql.Date _date,
			final String _dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(_dateFormat);
		return formatter.format(_date);
	}

	/**
	 * Convert a oracle.sql.DATE type into a string.
	 * 
	 * @param _timestamp
	 *            The TIMESTAMP to convert.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final oracle.sql.DATE _timestamp) {
		try {
			return InOutUtils.toString(_timestamp.dateValue());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Convert a oracle.sql.DATE type into a string.
	 * 
	 * @param _timestamp
	 *            The TIMESTAMP to convert.
	 * @param _dateFormat
	 *            The date format.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final oracle.sql.DATE _timestamp,
			final String _dateFormat) {
		try {
			return InOutUtils.toString(_timestamp.dateValue(), _dateFormat);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Convert a oracle.sql.TIMESTAMP type into a string.
	 * 
	 * @param _timestamp
	 *            The TIMESTAMP to convert.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final oracle.sql.TIMESTAMP _timestamp) {
		try {
			return InOutUtils.toString(_timestamp.dateValue());
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Convert a oracle.sql.TIMESTAMP type into a string.
	 * 
	 * @param _timestamp
	 *            The TIMESTAMP to convert.
	 * @param _dateFormat
	 *            The date format.
	 * 
	 * @return The converted string.
	 */
	public static String toString(final oracle.sql.TIMESTAMP _timestamp,
			final String _dateFormat) {

		try {
			return InOutUtils.toString(_timestamp.dateValue(), _dateFormat);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Splits a text String into pieces. Example:
	 * 
	 * <pre>
	 * Groovy: def expected = [&quot;&quot;, &quot;a&quot;, &quot;b&quot;,
	 * &quot;c&quot;, &quot;&quot;] assert expected ==
	 * InOutUtils.split(&quot;|a|b|c|&quot;, &quot;|&quot;)
	 * </pre>
	 * 
	 * @param _text
	 *            The text for splitting.
	 * 
	 * @param _seperator
	 *            The seperator.
	 * 
	 * @return The splitted string.
	 */
	public static String[] split(final String _text, final String _seperator) {
		return StringUtils.splitPreserveAllTokens(_text, _seperator);
	}

	/**
	 * Concatenats a string list into a single string. Example:
	 * 
	 * <pre>
	 * Groovy: assert &quot;a|b|c||&quot; == InOutUtils.toString(['a',
	 * 'b', 'c', ''])
	 * </pre>
	 * 
	 * @param _strings
	 *            The strings to concatenate.
	 * 
	 * @param _seperator
	 *            The separator for concatenation.
	 * 
	 * @return The concatenated string.
	 */
	public static String toString(final List<String> _strings,
			final String _separator) {

		StringBuilder sb = new StringBuilder();
		int counter = 0;
		for (String str : _strings) {
			if (StringUtils.isNotEmpty(str)) {
				sb.append(str);
			}
			counter++;
			if (counter < _strings.size()) {
				sb.append(_separator);
			}
		}
		return sb.toString();
	}

	/**
	 * Transforms a text into a data map. Example:
	 * 
	 * <pre>
	 * Groovy: def text = &quot;v1|v2|v3&quot; def columns =
	 * [&quot;col_a&quot;, &quot;col_b&quot;, &quot;col_c&quot;] assert
	 * mapping(text, columns) == [c1: 'v1', c2: 'v2', c3: 'v3'];
	 * </pre>
	 * 
	 * It is possible to return <code>null</code>, if the text parameters starts
	 * as a comment line.
	 * 
	 * @param _text
	 *            An input text.
	 * 
	 * @param _columns
	 *            The column names as a key for the values.
	 * 
	 * @return A data map.
	 */
	public static Map<String, String> mapping(final String _text,
			final List<String> _columns, final String _separator) {

		if (_text.trim().startsWith("#")) {
			return null;
		}

		Map<String, String> keyValues = new LinkedHashMap<String, String>();
		String[] values = split(_text, _separator);

		if (_columns.size() != values.length) {
			throw new IllegalArgumentException(
					"Number of tokens is not equal to number of columns.");
		}

		for (int i = 0; i < _columns.size(); i++) {
			keyValues.put(_columns.get(i), values[i]);
		}

		return keyValues;
	}

	public static String toString(final int _columnType, final Object _value) {
		StringBuilder sb = new StringBuilder();
		if (_value == null) {
			sb.append("null");
		} else if (_columnType == java.sql.Types.BIGINT) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.BINARY) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.BINARY' is not supported!");
		} else if (_columnType == java.sql.Types.BIT) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.BLOB) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.BLOB' is not supported!");
		} else if (_columnType == java.sql.Types.BOOLEAN) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.CHAR) {
			sb.append("'").append(_value.toString()).append("'");
		} else if (_columnType == java.sql.Types.CLOB) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.CLOB' is not supported!");
		} else if (_columnType == java.sql.Types.DATALINK) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.DATALINK' is not supported!");
		} else if (_columnType == java.sql.Types.DATE) {
			sb.append(toString((java.sql.Date) _value));
		} else if (_columnType == java.sql.Types.DECIMAL) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.DISTINCT) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.DISTINCT' is not supported!");
		} else if (_columnType == java.sql.Types.DOUBLE) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.FLOAT) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.INTEGER) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.JAVA_OBJECT) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.JAVA_OBJECT' is not supported!");
		} else if (_columnType == java.sql.Types.LONGNVARCHAR) {
			sb.append("'").append(_value.toString()).append("'");
		} else if (_columnType == java.sql.Types.LONGVARBINARY) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.LONGVARBINARY' is not supported!");
		} else if (_columnType == java.sql.Types.LONGVARCHAR) {
			sb.append("'").append(_value.toString()).append("'");
		} else if (_columnType == java.sql.Types.NCHAR) {
			sb.append("'").append(_value.toString()).append("'");
		} else if (_columnType == java.sql.Types.NCLOB) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.NCLOB' is not supported!");
		} else if (_columnType == java.sql.Types.NULL) {
			sb.append("NULL");
		} else if (_columnType == java.sql.Types.NUMERIC) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.NVARCHAR) {
			sb.append("'").append(_value.toString()).append("'");
		} else if (_columnType == java.sql.Types.OTHER) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.OTHER' is not supported!");
		} else if (_columnType == java.sql.Types.REAL) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.REF) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.REF' is not supported!");
		} else if (_columnType == java.sql.Types.ROWID) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.SMALLINT) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.SQLXML) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.SQLXML' is not supported!");
		} else if (_columnType == java.sql.Types.STRUCT) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.STRUCT' is not supported!");
		} else if (_columnType == java.sql.Types.TIME) {
			sb.append("'").append(toString((java.sql.Time) _value)).append("'");
		} else if (_columnType == java.sql.Types.TIMESTAMP) {
			sb.append("'").append(toString((java.sql.Timestamp) _value))
					.append("'");
		} else if (_columnType == java.sql.Types.TINYINT) {
			sb.append(_value);
		} else if (_columnType == java.sql.Types.VARBINARY) {
			throw new IllegalStateException(
					"The type 'java.sql.Types.VARBINARY' is not supported!");
		} else if (_columnType == java.sql.Types.VARCHAR) {
			sb.append("'").append(_value.toString()).append("'");
		} else {
			throw new IllegalStateException("The type '" + _columnType
					+ "' is not supported!");
		}

		return sb.toString();
	}

}
