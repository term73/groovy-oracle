/*
 * $Id: GFileWriter.java 104 2009-03-04 14:17:30Z andre.winkler@web.de $
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

import java.io.IOException;

import org.apache.commons.io.output.FileWriterWithEncoding;

/**
 * Acts as a wrapper for writing to a file.
 * 
 * @author $Author: andre.winkler@web.de $
 * @version $Revision: 104 $ $Date: 2009-03-04 15:17:30 +0100 (Mi, 04 Mrz 2009) $
 */
public class GFileWriter {

	private String lineSeperator = System.getProperty("line.separator");

	private final FileWriterWithEncoding fileWriter;

	public GFileWriter(final String _fileName) throws IOException {
		this(_fileName, System.getProperty("file.encoding"));
	}

	public GFileWriter(final String _fileName, final String _encoding)
			throws IOException {

		fileWriter = new FileWriterWithEncoding(_fileName, _encoding);
	}

	public GFileWriter write(final String _text) throws IOException {
		fileWriter.write(_text);
		return this;
	}

	public GFileWriter writeln(final String _text) throws IOException {
		fileWriter.write(_text);
		fileWriter.write(lineSeperator);
		fileWriter.flush();
		return this;
	}

    public void flush() throws IOException {
    	fileWriter.flush();
    }
 
	public void close() throws IOException {
		fileWriter.close();
	}

}
