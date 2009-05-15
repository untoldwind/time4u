create database time4u;

create user 'time4u'@'localhost' identified by 'time4u';

grant all on time4u.* to 'time4u'@'localhost';

flush privileges;
