select * from users;

alter table users modify role varchar(20);

select  * from information_schema.check_constraints;

update users set role='ROLE_ADMIN' where user_id=6;