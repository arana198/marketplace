INSERT INTO oauth_client_details
  (client_name, client_id, client_secret, resource_ids,  scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES
  ("ios", "4f7ec648a48b9d3fa239b497f7b6b4d8019697bd", "$2a$10$er7kPyZHaX2Js3NYQ/8xQejUR/F78hytrxNVOZd5MB5QwsIBf0BH6", null, "all,read,write", "password,refresh_token", null, "ROLE_CLIENT", 21600, 2592000, null, true),
  ("android", "7f218cc429d37e03c75e6c417874e48f5fe90860", "$2y$10$SFe5bxSuskrV7XcmLCc2CuQk0JsUp3dk4wAsgae.Lw3is9d1GcKYO", null, "all,read,write", "password,refresh_token", null, "ROLE_CLIENT", 21600, 2592000, null, true);

INSERT INTO roles
  (id, name, description, is_selectable)
VALUES
  ("28877514-273c-47f1-9204-e9bed72edc24", "ROLE_ADMIN", "Indicates the role is for site admin", true),
  ("8ca15385-75be-4957-a5c2-943b5cc88758", "ROLE_COMPANY_ADMIN", "Indicates the role is for company admin", true),
  ("1c3a75e9-8d6e-451f-a420-951b0efddd67", "ROLE_BROKER", "Indicates the role is for a broker", true),
  ("55853649-d8d0-4648-b7bf-bbdd26b0f1da", "ROLE_USER", "Indicates the role is for normal user", true);

INSERT INTO user_status
  (id, name, description, is_selectable)
VALUES
  ("09d48b3d-b9c4-4f31-a9a1-b6bec8669c5e", "ACTIVE", "Indicates the user is active", true),
  ("797b6729-f45a-494a-b525-ff954f9662be", "PENDING", "Indicates the user is pending", true),
  ("be1ab17e-6e53-4bfb-a8c7-7aea96f41e88", "PENDING_CLOSED", "Indicates the user was in pending status before he was closed", true),
  ("288628b2-9a35-425f-8ebe-298952507b34", "CLOSED", "Indicates the user is closed", true),
  ("25fdbc34-029f-49eb-8be8-b1dd8527e2cc", "SUSPENDED", "Indicates the user is suspended", true),
  ("18ddf85e-4445-4909-8663-a63467372210", "BLACKLISTED", "Indicates the user is blacklisted", true);