create schema if not exists sfs;

create table if not exists sfs.files (
    id uuid not null,
	name text not null,
	hash text not null,
	media_type text not null,
	size bigint not null,
	absolute_path text not null,
    uploader text not null,
	upload_time timestamp not null,
	primary key (id),
	unique (hash, name)
);

comment on table sfs.files is 'File information table';
comment on column sfs.files.id is 'Primary key';
comment on column sfs.files.name is 'File name';
comment on column sfs.files.hash is 'File hash';
comment on column sfs.files.media_type is 'Media type';
comment on column sfs.files.size is 'File size';
comment on column sfs.files.absolute_path is 'File absolute path';
comment on column sfs.files.uploader is 'Uploader name';
comment on column sfs.files.upload_time is 'File upload time';

create index on sfs.files(uploader, name);
