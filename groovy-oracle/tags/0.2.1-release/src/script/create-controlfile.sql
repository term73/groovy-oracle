set echo off ver off feed off pages 0 
accept tname prompt 'Enter Name of Table: ' 
accept dformat prompt 'Enter Format to Use for Date Columns: ' 

spool &tname..ctl

select 'LOAD DATA'|| chr (10) ||
       'INFILE ''' || lower (table_name) || '.dat''' || chr (10) ||
       'INTO TABLE '|| table_name || chr (10)||
       'FIELDS TERMINATED BY '','''|| chr (10)||
       'TRAILING NULLCOLS' || chr (10) || '(' 
from   user_tables
where  table_name = upper ('&tname');

select decode (rownum, 1, '   ', ' , ') ||
       rpad (column_name, 33, ' ')      ||
       decode (data_type,
           'VARCHAR2', 'CHAR NULLIF ('||column_name||'=BLANKS)', 
           'FLOAT',    'DECIMAL EXTERNAL NULLIF('||column_name||'=BLANKS)',
           'NUMBER',   decode (data_precision, 0, 
                       'INTEGER EXTERNAL NULLIF ('||column_name||
                       '=BLANKS)', decode (data_scale, 0, sqlplus 
                       'INTEGER EXTERNAL NULLIF ('||
                       column_name||'=BLANKS)', 
                       'DECIMAL EXTERNAL NULLIF ('||
                       column_name||'=BLANKS)')), 
           'DATE',     'DATE "&dformat" NULLIF ('||column_name||'=BLANKS)', null) 
from   user_tab_columns 
where  table_name = upper ('&tname') 
order  by column_id;

select ')'  
from dual;
spool off
