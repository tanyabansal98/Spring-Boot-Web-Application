create user MVC_APP identified by "MVC_Password@123";

grant create session to MVC_APP;
grant create table, create sequence, create trigger to MVC_APP;
grant unlimited tablespace to MVC_APP;