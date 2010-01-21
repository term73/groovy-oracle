/*
 * $Id$
 * ============================================================================
 * Project groovy-oracle
 * Copyright (c) 2008 by Andre Winkler. All rights reserved.
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

package de.gluehloch.groovy.oracle.meta

/**
 * Die Klasse schaltet alle Constraints in einem User-Schema EIN.
 */
class EnableConstraints {

    def sql

    def run() {
        sql.call("""
declare
  v_cmd VARCHAR2(1024);
  procedure enable_foreign_keys(p_table_name VARCHAR2) is
  begin
    for i_constraint in (
      select *
        from user_constraints uc
        where uc.table_name = p_table_name
          and uc.constraint_type = 'R' and uc.status = 'DISABLED'
    )
    loop
      v_cmd := 'ALTER TABLE ' || p_table_name || ' ENABLE CONSTRAINT ' || i_constraint.constraint_name;
      --DBMS_OUTPUT.PUT_LINE(v_cmd);
      execute immediate v_cmd;
    end loop;
  end;
  procedure enable_primary_keys(p_table_name VARCHAR2) is
  begin
    for i_constraint in (
      select *
        from user_constraints uc
        where uc.table_name = p_table_name
          and uc.constraint_type = 'P' and uc.status = 'DISABLED'
    )
    loop
      v_cmd := 'ALTER TABLE ' || p_table_name || ' ENABLE CONSTRAINT ' || i_constraint.constraint_name;
      --DBMS_OUTPUT.PUT_LINE(v_cmd);
      execute immediate v_cmd;
    end loop;
  end;
begin
  for i_table in (
    select * from user_tables
  )
  loop
    enable_primary_keys(i_table.table_name);
  end loop;

  for i_table in (
    select * from user_tables
  )
  loop
    enable_foreign_keys(i_table.table_name);
  end loop;
end;
""")
    }

}