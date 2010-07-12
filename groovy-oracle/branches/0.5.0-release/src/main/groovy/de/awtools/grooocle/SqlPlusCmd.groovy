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

package de.awtools.grooocle

/**
 * Wrapper for calling Oracle´s SQL*Plus command line tool. This is a
 * convenience class and a wrapper of class SqlPlus. The user specifies his
 * sql commands with the list property <code>command</code>. Additionally some
 * logs are generated for every statement.
 * 
 * @author  $Author$
 * @version $Revision$ $Date$
 */
public class SqlPlusCmd extends SqlPlus {

    /** A SQL*Plus command or SQL statement terminated by a ';' */
    def command = ["set serveroutput on size unlimited;"]

    /**
     * Executes a sql script with SQL*Plus.
     * 
     * @return Returns 0, if everything was fine. 
     */
    def start() {
        script = File.createTempFile('tmp_rms', '.sql')
        command?.each { script << (it + LINE_SEPARATOR) }

        dir = script.getParent()
        script = script.toString()

        super.start()
	}

    def leftShift(sqlCmd) {
    	command << "exec dbms_output.put_line('${sqlCmd}');"
        command << sqlCmd
    }

}
