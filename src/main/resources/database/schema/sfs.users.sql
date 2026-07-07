create schema if not exists sfs;

create table if not exists sfs.users (
	username text not null,
	password text not null,
	enabled boolean not null,
	locked boolean not null,
	last_login_time timestamp not null,
	last_password_time timestamp not null,
	authorities text array not null,
	primary key (username)
);

comment on table sfs.users is 'User information table';
comment on column sfs.users.username is 'Username';
comment on column sfs.users.password is 'Password';
comment on column sfs.users.enabled is 'Whether enabled';
comment on column sfs.users.locked is 'Whether locked';
comment on column sfs.users.last_login_time is 'Last login time';
comment on column sfs.users.last_password_time is 'Last password update time';
comment on column sfs.users.authorities is 'User authorities';
