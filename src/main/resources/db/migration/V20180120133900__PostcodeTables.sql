SET default_storage_engine=ARIA;

CREATE TABLE IF NOT EXISTS address (
  id varchar(36) NOT NULL PRIMARY KEY,
  address varchar(255) NOT NULL,
  city_id varchar(36) NOT NULL,
  state_id varchar(36) NOT NULL,
  postcode_id varchar(36) NOT NULL,
  UNIQUE KEY ux_address (address, city_id, state_id, postcode_id),
  CONSTRAINT fk_address_city FOREIGN KEY (city_id) REFERENCES city (id),
  CONSTRAINT fk_address_state FOREIGN KEY (state_id) REFERENCES state (id),
  CONSTRAINT fk_address_postcode FOREIGN KEY (postcode_id) REFERENCES postcode (id)
);

CREATE TABLE IF NOT EXISTS state (
  id varchar(36) NOT NULL PRIMARY KEY,
  name varchar(100) NOT NULL,
  UNIQUE KEY ux_name (name)
);

CREATE TABLE IF NOT EXISTS city (
  id varchar(36) NOT NULL PRIMARY KEY ,
  name varchar(100) NOT NULL,
  state_id varchar(36) NOT NULL,
  UNIQUE KEY ux_name (name),
  CONSTRAINT fk_city_state FOREIGN KEY (state_id) REFERENCES state (id)
);

CREATE TABLE IF NOT EXISTS outcode (
  id varchar(36) NOT NULL PRIMARY KEY,
  city_id varchar(36) NOT NULL,
  outcode varchar(4) NOT NULL,
  coordinates POINT NOT NULL,
  is_active bit(1) NOT NULL,
  SPATIAL INDEX(coordinates),
  UNIQUE KEY ux_outcode (outcode),
  CONSTRAINT fk_outcode_city FOREIGN KEY (city_id) REFERENCES city (id)
);

CREATE TABLE IF NOT EXISTS postcode (
  id varchar(36) NOT NULL PRIMARY KEY,
  postcode varchar(7) NOT NULL,
  outcode_id varchar(36) NOT NULL,
  coordinates POINT NOT NULL,
  is_active bit(1) NOT NULL,
  SPATIAL INDEX(coordinates),
  UNIQUE KEY ux_postcode (postcode),
  CONSTRAINT fk_postcode_outcode FOREIGN KEY (outcode_id) REFERENCES outcode (id)
);