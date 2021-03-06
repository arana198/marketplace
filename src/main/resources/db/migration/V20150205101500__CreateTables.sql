SET default_storage_engine = ARIA;

/*UserRequest Service*/
CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id          varchar(256) DEFAULT NULL,
  token             blob,
  authentication_id varchar(256) DEFAULT NULL,
  user_name         varchar(256) DEFAULT NULL,
  client_id         varchar(256) DEFAULT NULL,
  authentication    blob,
  refresh_token     varchar(256) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id       varchar(255) DEFAULT NULL,
  token          blob,
  authentication blob
);

CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_name             varchar(255) NOT NULL,
  client_id               varchar(255) NOT NULL,
  resource_ids            varchar(255)  DEFAULT NULL,
  client_secret           varchar(255)  DEFAULT NULL,
  scope                   varchar(255)  DEFAULT NULL,
  authorized_grant_types  varchar(255)  DEFAULT NULL,
  web_server_redirect_uri varchar(255)  DEFAULT NULL,
  authorities             varchar(255)  DEFAULT NULL,
  access_token_validity   bigint(11)    DEFAULT NULL,
  refresh_token_validity  bigint(11)    DEFAULT NULL,
  additional_information  varchar(4096) DEFAULT NULL,
  autoapprove             bit(1)        DEFAULT NULL,
  PRIMARY KEY (client_id)
);

CREATE TABLE IF NOT EXISTS oauth_approvals (
  userId         VARCHAR(256),
  clientId       VARCHAR(256),
  scope          VARCHAR(256),
  status         VARCHAR(10),
  expiresAt      TIMESTAMP,
  lastModifiedAt TIMESTAMP NOT NULL DEFAULT now()
);

create table UserConnection (
  id             BIGINT(11) AUTO_INCREMENT PRIMARY KEY,
  userId         varchar(50)  not null,
  providerId     varchar(36)  not null,
  providerUserId varchar(36),
  rank           int          not null,
  displayName    varchar(255),
  profileUrl     varchar(512),
  imageUrl       varchar(512),
  accessToken    varchar(512) not null,
  secret         varchar(512),
  refreshToken   varchar(512),
  expireTime     bigint,
  UNIQUE key ux_user_provider_providerUserId (userId, providerId, providerUserId),
  UNIQUE KEY ux_user_connection_rank (userId, providerId),
  UNIQUE KEY ux_provider_user (providerId, providerUserId)
);

CREATE TABLE IF NOT EXISTS user_status (
  id            varchar(36)  NOT NULL,
  name          varchar(255) NOT NULL,
  description   varchar(255) NOT NULL,
  is_selectable bit(1)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ix_name (name),
  KEY ix_is_selectable (is_selectable)
);

CREATE TABLE IF NOT EXISTS users (
  id                varchar(36)  NOT NULL,
  username          varchar(255) NOT NULL,
  password          varchar(255) NULL,
  is_email_verified bit(1)       NOT NULL,
  created_ts        TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts        TIMESTAMP    NOT NULL DEFAULT now(),
  updated_by        varchar(36)  NULL,
  version           int(11)      NOT NULL,
  PRIMARY KEY (id),
  INDEX ix_username_is_email_verified (username, is_email_verified),
  INDEX ix_username (username)
);

CREATE TABLE IF NOT EXISTS email_verification_tokens (
  id         BIGINT(11)  NOT NULL AUTO_INCREMENT,
  user_id    varchar(36) NOT NULL,
  token      varchar(36) NOT NULL,
  created_ts TIMESTAMP   NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  UNIQUE KEY ix_email_verification_tokens_user_id (user_id),
  INDEX ix_email_verification_tokens_token (token),
  CONSTRAINT fk_email_verification_tokens_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_password_tokens (
  id         BIGINT(11)  NOT NULL AUTO_INCREMENT,
  user_id    varchar(36) NOT NULL,
  token      varchar(36) NOT NULL,
  created_ts TIMESTAMP   NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  INDEX ix_user_password_tokens_token (token),
  INDEX ix_user_password_tokens_user_id_token (user_id, token),
  UNIQUE KEY ix_user_password_token_user_id (user_id),
  CONSTRAINT fk_user_password_token_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS roles (
  id            varchar(36)  NOT NULL,
  name          varchar(255) NOT NULL,
  description   varchar(255) NOT NULL,
  is_selectable bit(1)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_role_name (name)
);

CREATE TABLE IF NOT EXISTS user_roles (
  id               bigint(11)  NOT NULL AUTO_INCREMENT,
  role_id          varchar(36) NOT NULL,
  user_id          varchar(36) NOT NULL,
  user_status_id   varchar(36) NOT NULL,
  provider         varchar(12) NOT NULL,
  provider_user_id varchar(36),
  created_ts       TIMESTAMP   NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  KEY ix_user_id (user_id),
  UNIQUE KEY ux_user_id_role_id (user_id, role_id),
  CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id),
  CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_user_roles_user_status FOREIGN KEY (user_status_id) REFERENCES user_status (id)
);

/*Profile Service*/
CREATE TABLE IF NOT EXISTS user_profiles (
  id            varchar(36)  NOT NULL,
  user_id       varchar(36)  NOT NULL,
  email         varchar(255) NOT NULL,
  firstname     varchar(25)  NOT NULL,
  lastname      varchar(40)  NOT NULL,
  mobile_number varchar(15)  NOT NULL,
  dob           TIMESTAMP    NOT NULL,
  postcode      varchar(255) NULL,
  created_ts    TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts    TIMESTAMP    NOT NULL DEFAULT now(),
  updated_by    varchar(36)  NULL,
  version       int(11)               DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_user_id (user_id),
  INDEX ix_mobile_number (mobile_number),
  CONSTRAINT fk_user_profiles_users FOREIGN KEY (user_id) REFERENCES users (id)
);

/*Broker*/
CREATE TABLE IF NOT EXISTS companies (
  id             varchar(36)  NOT NULL,
  name           varchar(255) NOT NULL,
  company_number varchar(15)  NOT NULL,
  vat_number     varchar(15)  NOT NULL,
  fca_number     varchar(15)  NOT NULL,
  logo_url       varchar(500) NULL,
  website_url    varchar(500) NULL,
  is_active      bit(1)       NOT NULL,
  created_ts     TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts     TIMESTAMP    NOT NULL DEFAULT now(),
  updated_by     varchar(36)  NULL,
  version        int(11)               DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_company_number (company_number),
  UNIQUE KEY ux_vat_number (vat_number),
  INDEX ix_company_number_vat_number (company_number, vat_number),
  FULLTEXT (name)
);

CREATE TABLE IF NOT EXISTS company_validators (
  id                     varchar(36) NOT NULL,
  company_id             varchar(36) NOT NULL,
  fca_number_verified    bit(1)      NOT NULL,
  billing_active         bit(1)      NOT NULL,
  fca_number_verified_ts TIMESTAMP   NULL,
  created_ts             TIMESTAMP   NOT NULL DEFAULT now(),
  updated_ts             TIMESTAMP   NOT NULL DEFAULT now(),
  updated_by             varchar(36) NULL,
  version                int(11)              DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_company_id (company_id),
  CONSTRAINT fk_company_validators_companies FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE TABLE IF NOT EXISTS broker_profiles (
  id            varchar(36)  NOT NULL,
  user_id       varchar(36)  NOT NULL,
  company_id    varchar(36)  NOT NULL,
  firstname     varchar(25)  NOT NULL,
  lastname      varchar(40)  NOT NULL,
  mobile_number varchar(15)  NOT NULL,
  about_me      TEXT         NULL,
  image_url     varchar(500) NULL,
  is_admin      bit(1)       NOT NULL,
  is_active     bit(1)       NOT NULL,
  created_ts    TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts    TIMESTAMP    NOT NULL DEFAULT now(),
  updated_by    varchar(36)  NULL,
  version       int(11)               DEFAULT 0,
  PRIMARY KEY (id),
  INDEX ix_company_id (company_id),
  INDEX ix_user_id (user_id),
  INDEX ix_user_id_is_active (user_id, is_active),
  INDEX ix_company_id_is_admin (company_id, is_admin),
  CONSTRAINT fk_broker_profiles_companies FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE TABLE IF NOT EXISTS company_employee_invite (
  id         BIGINT(11)   NOT NULL AUTO_INCREMENT,
  company_id varchar(36)  NOT NULL,
  email      varchar(255) NOT NULL,
  token      varchar(36)  NOT NULL,
  created_ts TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP    NOT NULL DEFAULT now(),
  PRIMARY KEY (id),
  UNIQUE KEY ux_company_id_email (company_id, email),
  CONSTRAINT fk_company_employee_invite_users FOREIGN KEY (company_id) REFERENCES companies (id)
);

CREATE TABLE IF NOT EXISTS services (
  id           varchar(36)  NOT NULL PRIMARY KEY,
  name         varchar(255) NOT NULL,
  display_name varchar(255) NOT NULL,
  description  text,
  parent_id    varchar(36),
  is_active    bit(1)       NOT NULL,
  created_ts   TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts   TIMESTAMP    NOT NULL DEFAULT now(),
  UNIQUE KEY ux_services_name (name),
  CONSTRAINT fk_services FOREIGN KEY (parent_id) REFERENCES services (id)
);

CREATE TABLE IF NOT EXISTS company_services (
  id         varchar(36) NOT NULL PRIMARY KEY,
  company_id varchar(36) NOT NULL,
  service_id varchar(36) NOT NULL,
  created_ts TIMESTAMP   NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP   NOT NULL DEFAULT now(),
  UNIQUE KEY ux_company_services (company_id, service_id),
  CONSTRAINT fk_company_services_company FOREIGN KEY (company_id) REFERENCES companies (id),
  CONSTRAINT fk_company_services_services FOREIGN KEY (service_id) REFERENCES services (id)
);

CREATE TABLE IF NOT EXISTS broker_documents (
  id                varchar(36) NOT NULL,
  broker_profile_id varchar(36) NOT NULL,
  file_id           varchar(36) NOT NULL,
  is_verified       bit(1)      NOT NULL,
  created_ts        TIMESTAMP   NOT NULL DEFAULT now(),
  updated_ts        TIMESTAMP   NOT NULL DEFAULT now(),
  updated_by        varchar(36) NULL,
  version           int(11)              DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_file_store_id (file_id),
  UNIQUE KEY ux_bucket_id_file_store_id (broker_profile_id, file_id),
  CONSTRAINT fk_broker_profiles_broker_documents FOREIGN KEY (broker_profile_id) REFERENCES broker_profiles (id)
);

CREATE TABLE IF NOT EXISTS broker_validators (
  id                   varchar(36) NOT NULL,
  broker_profile_id    varchar(36) NOT NULL,
  certificate_verified bit(1)      NOT NULL,
  created_ts           TIMESTAMP   NOT NULL DEFAULT now(),
  updated_ts           TIMESTAMP   NOT NULL DEFAULT now(),
  updated_by           varchar(36) NULL,
  version              int(11)              DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_broker_profile_id (broker_profile_id),
  CONSTRAINT fk_broker_profiles_broker_documents FOREIGN KEY (broker_profile_id) REFERENCES broker_profiles (id)
);

CREATE TABLE IF NOT EXISTS email_notifications (
  id         varchar(36) NOT NULL PRIMARY KEY,
  sent_to    varchar(36) NULL,
  email      JSON        NOT NULL,
  /*toAddress VARCHAR(255) AS (JSON_VALUE(email, '$.toAddress')),
  status VARCHAR(10) AS (JSON_VALUE(email, '$.status')),*/
  created_ts TIMESTAMP   NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP   NOT NULL DEFAULT now(),
  INDEX ix_sent_to (sent_to),
  /*INDEX ix_sent_to_status (sent_to, status),
  INDEX ix_toAddress (toAddress),*/
  CHECK (JSON_VALID(email))
);

/*Files*/
CREATE TABLE IF NOT EXISTS file_stores (
  id          varchar(36)  NOT NULL PRIMARY KEY,
  name        varchar(128) NOT NULL,
  description TEXT         NULL,
  type        varchar(100) NOT NULL,
  format      varchar(100) NOT NULL,
  data        longblob,
  created_ts  TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts  TIMESTAMP    NOT NULL DEFAULT now(),
  INDEX ix_name (name)
);

CREATE TABLE IF NOT EXISTS buckets (
  id         varchar(36)  NOT NULL PRIMARY KEY,
  type       varchar(100) NOT NULL,
  created_ts TIMESTAMP    NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP    NOT NULL DEFAULT now(),
  INDEX ix_name (type)
);

CREATE TABLE IF NOT EXISTS bucket_permissions (
  id         varchar(36) NOT NULL,
  bucket_id  varchar(36) NOT NULL,
  user_id    varchar(36) NOT NULL,
  created_ts TIMESTAMP   NOT NULL DEFAULT now(),
  updated_ts TIMESTAMP   NOT NULL DEFAULT now(),
  updated_by varchar(36) NULL,
  version    int(11)              DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_bucket_user (bucket_id, user_id),
  CONSTRAINT fk_buckets_bucket_permissions FOREIGN KEY (bucket_id) REFERENCES buckets (id)
);

CREATE TABLE IF NOT EXISTS bucket_files (
  id            varchar(36) NOT NULL,
  bucket_id     varchar(36) NOT NULL,
  file_store_id varchar(36) NOT NULL,
  is_public     bit(1)      NOT NULL,
  created_ts    TIMESTAMP   NOT NULL DEFAULT now(),
  updated_ts    TIMESTAMP   NOT NULL DEFAULT now(),
  updated_by    varchar(36) NULL,
  version       int(11)              DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY ux_bucket_id_file_store_id (bucket_id, file_store_id),
  CONSTRAINT fk_buckets_bucket_files FOREIGN KEY (bucket_id) REFERENCES buckets (id),
  CONSTRAINT fk_buckets_file_stores FOREIGN KEY (file_store_id) REFERENCES file_stores (id)
);