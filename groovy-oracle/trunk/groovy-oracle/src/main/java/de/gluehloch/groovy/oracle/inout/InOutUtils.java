/*
 * $Id: InOutUtils.java 140 2009-04-17 15:44:57Z andre.winkler@web.de $
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
 * @author $Author: andre.winkler@web.de $
 * @version $Revision: 140 $ $Date: 2009-04-17 17:44:57 +0200 (Fr, 17 Apr 2009) $
 */
public class InOutUtils {

	/**
	 * Konvertierungsformat Oracle-Date-Type -> String
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * Konvertierungsformat String -> Oracle-Date-Type
	 */
	public static final String ORACLE_DATE_FORMAT = "yyyy-MM-dd HH24:MI:ss";

	/**
	 * Convert a string into a date type. Throws a runtime exception if string
	 * is unparseable.
	 *
	 * @param _text The string to parse.
	 * @return The date object.
	 */
	public static Date toDate(final String _text) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		try {
			return formatter.parse(_text);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Convert a date type into a <code>java.sql.Date</code>.
	 *
	 * @param _date The date to convert.
	 * @return The java.sql.Date object.
	 */
	public static java.sql.Date toSqlDate(final Date _date) {
		return new java.sql.Date(_date.getTime());
	}

	/**
	 * Convert a string into a <code>java.sql.Date</code>.
	 *
	 * @param _text The string to parse.
	 * @return The java.sql.Date object.
	 */
	public static java.sql.Date toSqlDate(final String _text) {
		return toSqlDate(toDate(_text));
	}

	/**
	 * Convert a date type into a <code>oracle.sql.DATE</code>.
	 *
	 * @param _date The date to convert.
	 * @return The java.sql.Date object.
	 */
	public static oracle.sql.DATE toOracleDate(final Date _date) {
		return new oracle.sql.DATE(toSqlDate(_date));
	}

	/**
	 * Convert a string into a <code>oracle.sql.DATE</code>.
	 *
	 * @param _text The string to parse.
	 * @return The java.sql.Date object.
	 */
	public static oracle.sql.DATE toOracleDate(final String _text) {
		return new oracle.sql.DATE(toSqlDate(_text));
	}

	/**
	 * Convert a date type into a <code>oracle.sql.TIMESTAMP</code>.
	 *
	 * @param _date The date type to convert.
	 * @return A oracle.sql.TIMESTAMP object.
	 */
	public static oracle.sql.TIMESTAMP toOracleTimeStamp(final Date _date) {
		return new oracle.sql.TIMESTAMP(toOracleDate(_date));
	}

	/**
	 * Convert a date type into a <code>oracle.sql.TIMESTAMP</code>.
	 *
	 * @param _text The string to parse.
	 * @return A oracle.sql.TIMESTAMP object.
	 */
	public static oracle.sql.TIMESTAMP toOracleTimeStamp(final String _text) {
		return new oracle.sql.TIMESTAMP(toOracleDate(_text));
	}

	/**
	 * Convert a date type into a string.
	 *
	 * @param _date The date to convert.
	 * @return The converted string.
	 */
	public static String toString(final Date _date) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.format(_date);
	}

	/**
	 * Convert a java.sql.Date type into a string.
	 *
	 * @param _date The date to convert.
	 * @return The converted string.
	 */
	public static String toString(final java.sql.Date _date) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.format(_date);
	}

	/**
	 * Convert a java.sql.Timestamp type into a string.
	 *
	 * @param _timestamp The TIMESTAMP to convert.
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
	 * Splits a text String into pieces. Example:
	 * 
	 * <pre>
	 * Groovy:
	 * def expected = [&quot;&quot;, &quot;a&quot;, &quot;b&quot;, &quot;c&quot;, &quot;&quot;]
	 * assert expected == InOutUtils.split(&quot;|a|b|c|&quot;, &quot;|&quot;)
	 * </pre>
	 * 
	 * @param _text The text for splitting.
	 * @param _seperator The seperator.
	 * @return The splitted string.
	 */
	public static String[] split(final String _text, final String _seperator) {
		return StringUtils.splitPreserveAllTokens(_text, _seperator);
	}

	/**
	 * Concatenats a string list into a single string. Example:
	 * 
	 * <pre>
	 * Groovy:
	 * assert &quot;a|b|c||&quot; == InOutUtils.toString(['a', 'b', 'c', ''])
	 * </pre>
	 * 
	 * @param _strings The strings to concatenate.
	 * @param _seperator The separator for concatenation.
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
	 * Groovy:
	 * def text = &quot;v1|v2|v3&quot;
	 * def columns = [&quot;col_a&quot;, &quot;col_b&quot;, &quot;col_c&quot;]
	 * assert mapping(text, columns) == [c1: 'v1', c2: 'v2', c3: 'v3'];
	 * </pre>
	 * 
	 * It is possible to return <code>null</code>, if the text parameters starts
	 * as a comment line.
	 * 
	 * @param _text An input text.
	 * @param _columns The column names as a key for the values.
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

}
