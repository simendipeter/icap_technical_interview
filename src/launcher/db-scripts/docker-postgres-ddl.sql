CREATE USER projections WITH PASSWORD 'projections' CREATEDB;
CREATE USER sessions WITH PASSWORD 'sessions' CREATEDB;
CREATE USER axon WITH PASSWORD 'axon' CREATEDB;

CREATE DATABASE axon;
CREATE DATABASE projections;
CREATE DATABASE filemappings;
CREATE DATABASE sessions;

ALTER DATABASE axon owner to axon;
ALTER DATABASE projections owner to projections;
ALTER DATABASE sessions owner to sessions;
