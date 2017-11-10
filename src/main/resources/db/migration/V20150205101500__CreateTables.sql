SET default_storage_engine=ARIA;

/*UserRequest Service*/
CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id varchar(256) DEFAULT NULL,
  token blob,
  authentication_id varchar(256) DEFAULT NULL,
  user_name varchar(256) DEFAULT NULL,
  client_id varchar(256) DEFAULT NULL,
  authentication blob,
  refresh_token varchar(256) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id varchar(255) DEFAULT NULL,
  token blob,
  authentication blob
);

CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_name varchar(255) NOT NULL,
  client_id varchar(255) NOT NULL,
  resource_ids varchar(255) DEFAULT NULL,
  client_secret varchar(255) DEFAULT NULL,
  scope varchar(255) DEFAULT NULL,
  authorized_grant_types varchar(255) DEFAULT NULL,
  web_server_redirect_uri varchar(255) DEFAULT NULL,
  authorities varchar(255) DEFAULT NULL,
  access_token_validity bigint(11) DEFAULT NULL,
  refresh_token_validity bigint(11) DEFAULT NULL,
  additional_information varchar(4096) DEFAULT NULL,
  autoapprove bit(1) DEFAULT NULL,
  PRIMARY KEY (client_id)
);

CREATE TABLE IF NOT EXISTS oauth_approvals (
	userId VARCHAR(256),
	clientId VARCHAR(256),
	scope VARCHAR(256),
	status VARCHAR(10),
	expiresAt TIMESTAMP,
	lastModifiedAt TIMESTAMP NOT NULL DEFAULT now()
);

create table UserConnection (
  id BIGINT(11) AUTO_INCREMENT PRIMARY KEY,
  userId varchar(50) not null,
  providerId varchar(36) not null,
  providerUserId varchar(36),
  rank int not null,
  displayName varchar(255),
  profileUrl varchar(512),
  imageUrl varchar(512),
  accessToken varchar(512) not null,
  secret varchar(512),
  refreshToken varchar(512),
  expireTime bigint,
  UNIQUE key ux_user_provider_providerUserId (userId, providerId, providerUserId),
  UNIQUE KEY ux_user_connection_rank (userId, providerId),
  UNIQUE KEY ux_provider_user (providerId, providerUserId)
);

CREATE TABLE IF NOT EXISTS user_status (
  id varchar(36) NOT NULL,
  name varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  is_selectable bit(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ix_name (name),
  KEY ix_is_selectable (is_selectable)
);

CREATE TABLE IF NOT EXISTS users (
  id varchar(36) NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(255) NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_by varchar(36) NULL,
  version int(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ix_username_provider (username)
);

CREATE TABLE IF NOT EXISTS user_password_tokens (
  id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  token varchar(36) NOT NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  UNIQUE KEY ix_user_password_token_user_id (user_id),
  CONSTRAINT fk_users_password_token_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS roles (
  id varchar(36) NOT NULL,
  name varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  is_selectable bit(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_role_name (name)
);

CREATE TABLE IF NOT EXISTS users (
  id varchar(36) NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(255) NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_by varchar(36) NULL,
  version int(11) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ix_username_provider (username, provider),
  KEY ix_provider (provider),
  KEY ix_username (username),
  CONSTRAINT fk_user_user_status FOREIGN KEY (user_status_id) REFERENCES user_status (id)
);

CREATE TABLE IF NOT EXISTS user_roles (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  role_id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  user_status_id varchar(36) NOT NULL,
  provider varchar(12) NOT NULL,
  provider_user_id varchar(36),
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  KEY ix_user_id (user_id),
  CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id),
  CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_user_roles_user_status FOREIGN KEY (user_status_id) REFERENCES user_status (id)
);

/*Profile Service*/
CREATE TABLE IF NOT EXISTS user_profiles (
  id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  email varchar(255) NOT NULL,
  firstname varchar(25) NOT NULL,
  lastname varchar(40) NOT NULL,
  mobile_number varchar(15) NOT NULL,
  dob TIMESTAMP NOT NULL,
  postcode varchar(255) NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_by varchar(36) NULL,
  version int(11) DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_user_id (user_id),
  INDEX ix_mobile_number (mobile_number),
  CONSTRAINT fk_user_profiles_users FOREIGN KEY (user_id) REFERENCES users (id)
);

/*Broker*/
CREATE TABLE IF NOT EXISTS companies (
  id varchar(36) NOT NULL,
  name varchar(255) NOT NULL,
  company_number varchar(15) NOT NULL,
  vat_number varchar(15) NOT NULL,
  logo_url varchar(500) NULL,
  website_url varchar(500) NULL,
  is_active varchar(255) NOT NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_by varchar(36) NULL,
  version int(11) DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_company_number (company_number),
  UNIQUE KEY ux_vat_number (vat_number),
  INDEX ix_company_number_vat_number (company_number, vat_number),
  FULLTEXT (name)
);

CREATE TABLE IF NOT EXISTS company_employee (
  id varchar(36) NOT NULL,
  company_id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  is_admin varchar(36) NOT NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_by varchar(36) NULL,
  version int(11) DEFAULT 0,
  PRIMARY KEY (id),
  INDEX ux_company_id (company_id),
  INDEX ux_user_id (user_id),
  INDEX ux_company_id_is_admin (company_id, is_admin),
  CONSTRAINT fk_company_employee_invite_users FOREIGN KEY (company_id) REFERENCES companies (id),
  CONSTRAINT fk_company_employee_invite_users FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS company_employee_invite (
  id varchar(36) NOT NULL,
  company_id varchar(36) NOT NULL,
  email varchar(255) NOT NULL,
  token varchar(36) NOT NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  UNIQUE KEY ux_company_id_email (company_id, email),
  CONSTRAINT fk_company_employee_invite_users FOREIGN KEY (company_id) REFERENCES companies (id),
  CONSTRAINT fk_company_employee_invite_users FOREIGN KEY (user_id) REFERENCES users (id)
);