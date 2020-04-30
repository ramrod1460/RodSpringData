 DROP TABLE IF EXISTS todo;
 DROP TABLE IF EXISTS to_do;
 
CREATE TABLE todo
(
  id varchar(36) not null primary key,
  description varchar(255) not null,
  created timestamp,
  modified timestamp,
  completed boolean
);


