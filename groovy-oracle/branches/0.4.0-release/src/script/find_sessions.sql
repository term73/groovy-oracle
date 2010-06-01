ttitle "Lock Listing"
set linesize 150
set echo off
col oruser format a16 heading "Oracle Username"
col osuser format a13 heading "O/S Username"
col obj format a20 heading "Locked Object"
col ss heading "SID/Ser#" format a12
col time heading "Logon Date/Time" format a19
col rs heading "RBS|Name" format a4
col unix heading "O/S|Process" format a9
col computer heading "Machine name|of Locker" format a20
set linesize 120
select     owner||'.'||object_name obj
   ,oracle_username||' ('||s.status||')' oruser
   ,os_user_name osuser
   ,machine computer
   ,l.process unix
   ,''''||s.sid||','||s.serial#||'''' ss
   ,r.name rs
   ,to_char(s.logon_time,'yyyy/mm/dd hh24:mi:ss') time
from       v$locked_object l
   ,dba_objects o
   ,v$session s
   ,v$transaction t
   ,v$rollname r
where l.object_id = o.object_id
  and s.sid=l.session_id
  and s.taddr=t.addr
  and t.xidusn=r.usn
order by osuser, ss, obj
/
ttitle off
set linesize 132
