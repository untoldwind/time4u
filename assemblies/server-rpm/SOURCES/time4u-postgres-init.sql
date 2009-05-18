create database time4u;

create role time4u with login encrypted password 'time4u';

\connect time4u

create schema time4u authorization time4u;

grant all on schema time4u to group time4u;
