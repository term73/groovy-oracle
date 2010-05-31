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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Defines the command line of an IO operation.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
public class InOutOptions {

	private static final Options options;
	private static final CommandLineParser parser = new PosixParser();

	private String user;
	private String password;
	private String url;

	static {
		options = new Options();
		options.addOption("u", true, "Oracle user name");
		options.addOption("p", true, "Oracle user password");
		options.addOption("url", true, "URL of the oracle database.");
	}

	public static InOutOptions options(final String[] args) {
		InOutOptions ioo = new InOutOptions();
		CommandLine cl = null;
		try {
			cl = parser.parse(options, args);
		} catch (ParseException ex) {
			throw new RuntimeException(ex);
		}
		ioo.user = cl.getOptionValue("u");
		ioo.password = cl.getOptionValue("p");
		ioo.url = cl.getOptionValue("url");
		return ioo;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getUrl() {
		return url;
	}

}
