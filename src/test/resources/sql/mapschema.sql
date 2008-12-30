CREATE TABLE x_world (
  id int NOT NULL default 0,
  x smallint NOT NULL default 0,
  y smallint NOT NULL default 0,
  tid smallint  NOT NULL default 0,
  vid int NOT NULL default 0,
  village varchar(20) NOT NULL default '',
  uid int NOT NULL default 0,
  player varchar(20) NOT NULL default '',
  aid int NOT NULL default 0,
  alliance varchar(8) NOT NULL default '',
  population smallint NOT NULL default 0,
  PRIMARY KEY (id)
);
