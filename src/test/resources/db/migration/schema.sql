SET MODE MySQL;

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
  token_id varchar(256) DEFAULT NULL,
  token blob,
  authentication blob
);

CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_name varchar(256) NOT NULL,
  client_id varchar(256) NOT NULL,
  resource_ids varchar(256) DEFAULT NULL,
  client_secret varchar(256) DEFAULT NULL,
  scope varchar(256) DEFAULT NULL,
  authorized_grant_types varchar(256) DEFAULT NULL,
  web_server_redirect_uri varchar(256) DEFAULT NULL,
  authorities varchar(256) DEFAULT NULL,
  access_token_validity int(11) DEFAULT NULL,
  refresh_token_validity int(11) DEFAULT NULL,
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

CREATE TABLE IF NOT EXISTS UserConnection (
  userId varchar(255) not null,
  providerId varchar(255) not null,
  providerUserId varchar(255),
  rank int not null,
  displayName varchar(255),
  profileUrl varchar(512),
  imageUrl varchar(512),
  accessToken varchar(512) not null,
  secret varchar(512),
  refreshToken varchar(512),
  expireTime bigint,
  primary key (userId, providerId, providerUserId)
);
create unique index UserConnectionRank on UserConnection(userId, providerId, rank);

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
  user_status_id varchar(36) NOT NULL,
  provider varchar(12) NOT NULL,
  provider_user_id varchar(36),
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP NOT NULL DEFAULT now(),
  updated_by varchar(36) NULL,
  version int(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ix_username_provider (username, provider),
  KEY ix_provider (provider),
  KEY ix_username (username),
  CONSTRAINT fk_user_user_status FOREIGN KEY (user_status_id) REFERENCES user_status (id)
);

CREATE TABLE IF NOT EXISTS user_password_tokens (
  id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  token varchar(255) NOT NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  UNIQUE KEY ix_user_password_token_user_id (user_id),
  CONSTRAINT fk_user_password_token_users FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS roles (
  id varchar(36) NOT NULL,
  name varchar(255) NOT NULL,
  description varchar(255) NOT NULL,
  is_selectable bit(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ix_role_name (name)
);

CREATE TABLE IF NOT EXISTS user_roles (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  role_id varchar(36) NOT NULL,
  user_id varchar(36) NOT NULL,
  created_ts TIMESTAMP NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  KEY ix_user_id (user_id),
  KEY fk_user_roles_role (role_id),
  CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles (id),
  CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users (id)
);