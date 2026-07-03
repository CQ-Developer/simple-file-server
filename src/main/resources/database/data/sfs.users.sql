-- admin:sfs_admin
insert into sfs.users (username, password, enabled, locked, last_login_time, last_password_time, authorities)
select 'admin',
       '$2b$12$n1Pzo1XElPWkSDdIHcmH4.EFX6Ah3HaYwprM0y/Ir4wD3EU7KnH.u',
       true,
       false,
       current_timestamp,
       current_timestamp,
       array['sfs:file:upload', 'sfs:file:download', 'sfs:file:delete']
where not exists (select 1 from sfs.users where username = 'admin');
