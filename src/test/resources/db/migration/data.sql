SET MODE MySQL;

INSERT INTO roles(id, name, description, is_selectable) VALUES ('1', 'ROLE_ADMIN', 'Indicates the role is for site admin', true);
INSERT INTO roles(id, name, description, is_selectable) VALUES ('2', 'ROLE_USER', 'Indicates the role is for club admin', true);

INSERT INTO user_status(id, name, description, is_selectable) VALUES ('1', 'ACTIVE', 'Indicates the userId is active', true);
INSERT INTO user_status(id, name, description, is_selectable) VALUES ('2', 'PENDING', 'Indicates the userId is pending', true);
INSERT INTO user_status(id, name, description, is_selectable) VALUES ('3', 'SUSPENDED', 'Indicates the userId is suspended', true);
INSERT INTO user_status(id, name, description, is_selectable) VALUES ('4', 'BLACKLISTED', 'Indicates the userId is blacklisted', true);

--Functional Test admin
INSERT INTO users(id, username, password, user_status_id, provider, updated_by, updated_ts, version)
VALUES ('1', 'admin', '5f4dcc3b5aa765d61d8327deb882cf99', '1', 'LOCAL','3', '2014-09-12 16:46:45', 0);
INSERT INTO user_roles(id, role_id, user_id) VALUES ('1', '1', '1');

INSERT INTO users(id, username, password, user_status_id, provider, updated_by, updated_ts, version)
VALUES ('2', 'test2', '5f4dcc3b5aa765d61d8327deb882cf99', '1', 'LOCAL','3', '2014-09-12 16:46:45', 0);
INSERT INTO user_roles(id, role_id, user_id) VALUES ('2', '2', '2');