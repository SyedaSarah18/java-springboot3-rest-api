DROP TABLE IF EXISTS Walk;

CREATE TABLE IF NOT EXISTS Walk (
  id INT NOT NULL,
  title varchar(250) NOT NULL,
  started_on timestamp NOT NULL,
  completed_on timestamp NOT NULL,
  distance_km INT NOT NULL,
  location varchar(10) NOT NULL,
--  version INT,
  PRIMARY KEY (id)

);