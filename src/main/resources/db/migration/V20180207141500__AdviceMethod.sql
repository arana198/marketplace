SET default_storage_engine=ARIA;

CREATE TABLE IF NOT EXISTS advice_methods (
  id varchar(36) NOT NULL PRIMARY KEY ,
  name varchar(255) NOT NULL,
  display_name varchar(255) NOT NULL,
  description text,
  is_active bit(1) NOT NULL,
  UNIQUE KEY ux_advice_methods (name)
);

CREATE TABLE IF NOT EXISTS company_advice_methods (
  id varchar(36) NOT NULL PRIMARY KEY ,
  company_id varchar(36) NOT NULL,
  advice_id varchar(36) NOT NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE KEY ux_company_advice_methods (company_id, advice_id),
  CONSTRAINT fk_company_advice_methods_company FOREIGN KEY (company_id) REFERENCES companies (id),
  CONSTRAINT fk_company_advice_methods_services FOREIGN KEY (advice_id) REFERENCES advice_methods (id)
);

INSERT INTO advice_methods
(id, name, display_name, description, is_active)
VALUES
  ("c23b7fe1-ad91-41f8-959c-a9bc746ee39e", "ONLINE", "Online", "Indicates the advisor can advice via online methods", true),
  ("30eabd19-60df-479e-9ed2-6d2f4558af35", "TELEPHONE", "Telephone", "Indicates the advisor can advice via telephone method", true),
  ("b3dbfd94-4be3-4719-91da-8051b0324302", "FACE_TO_FACE", "Face to Face", "Indicates the advisor can advice through face to face meeting", true);