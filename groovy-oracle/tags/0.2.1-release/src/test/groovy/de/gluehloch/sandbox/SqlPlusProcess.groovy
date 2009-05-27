/*
 * $Id: LoaderTest.groovy 122 2009-03-18 10:11:13Z andre.winkler@web.de $
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

package de.gluehloch.sandbox

/**
 * Wrapper for calling Oracle´s SQL*Plus command line tool. This one makes
 * currently some problems.
 *
 * TODO: Do i need this class?
 * 
 * @author  $Author: andre.winkler@web.de $
 * @version $Revision: 104 $ $Date: 2009-03-04 15:17:30 +0100 (Mi, 04 Mrz 2009) $
 */
class SqlPlusProcess {

    def sqlplusExecutable = 'sqlplus'
    def user
    def password
    def script
    def tnsName
    def dir

    /** Output StringBuffer. */
    def sout

    /** Error-Output as StringBuffer. */
    def serr

    /**
     * Executes a sql script with SQL*Plus.
     * 
     * @return Returns 0, if everything was fine. 
     */
    def start() {
        sout = new StringBuffer()
        serr = new StringBuffer()

        try {
            Process p = "${sqlplusExecutable} ${user}/${password}@${tnsName}".execute()
            p.consumeProcessOutput(sout, serr)
            p.withWriter { writer ->
                writer << "select * from cptasklist;"
                writer << 'exit;'
            }
            p.waitFor()
            return 0
        } catch (IOException ex) {
            println 'Executable of sqlplus not found!'
            return -1
        }
        println sout
    }

}
