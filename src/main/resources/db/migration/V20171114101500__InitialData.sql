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


INSERT INTO services
(id, name, display_name, description, parent_id, is_active)
VALUES
  ("21410261-3dcc-466a-97ae-0fa92b682d69", "MORTGAGES", "Mortgages", "", null, true),
  ("c6b10469-f7ce-4070-81e0-64abe999fd58", "FINANCIAL_PLANNING", "Financial Planning", "", null, true),
  ("7353dac6-779a-4bb5-8a45-ec92ae14c4a2", "INVESTMENTS", "Investments", "", null, true),
  ("eb65fbba-b1f5-4b0a-b772-c1027a62f08e", "SAVINGS", "Savings", "", null, true),
  ("78ab5721-0be5-4048-8dcd-34cf150d624b", "INSURANCE_AND_PROTECTION", "Insurance and Protection", "", null, true),
  ("e54879bc-4012-4fec-89ec-645395f25fa9", "TAX_AND_TRUST_PLANNING", "Tax and Trust Planning", "", null, true),
  ("4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", "BUSINESS", "Business", "", null, true),
  ("e802d869-57dd-4f1b-af79-a4a55f7fe460", "LONG_TERM_CARE", "Long Term Care", "", null, true);

INSERT INTO services
(id, name, display_name, description, parent_id, is_active)
VALUES
  ("aa4e0da0-45a1-4ecf-b627-fd41458a3853", "MORTGAGE_AND_REMORTGAGES", "Mortgage and Remortgages", "", "21410261-3dcc-466a-97ae-0fa92b682d69", true),
  ("0d8d13f2-a5cc-41d7-bc49-1a63401e7b0d", "BUY_TO_LET", "Buy To Let", "", "21410261-3dcc-466a-97ae-0fa92b682d69", true),
  ("a15fdceb-28ae-434e-8a39-4b34ddb0aaca", "FIRST/NEXT_TIME_BUYERS", "First/Next Time Buyers", "", "21410261-3dcc-466a-97ae-0fa92b682d69", true),
  ("17b455c5-cd71-4c1c-bf79-480750af0cb7", "HELP_TO_BUY", "Help To Buy", "", "21410261-3dcc-466a-97ae-0fa92b682d69", true);

INSERT INTO services
(id, name, display_name, description, parent_id, is_active)
VALUES
  ("0a4f1019-6ff2-40e7-ac64-165e60cd1d22", "INVESTING_FOR_INCOME", "Investing for Income", "", "7353dac6-779a-4bb5-8a45-ec92ae14c4a2", true),
  ("9c4e9a7d-ca0d-479d-9494-20f42b869b2a", "ISAS", "ISAs", "", "7353dac6-779a-4bb5-8a45-ec92ae14c4a2", true),
  ("a19d134d-20cb-4a94-ba94-864f5ff3d548", "INVESTMENT_TRUSTS", "Investing Trusts", "", "7353dac6-779a-4bb5-8a45-ec92ae14c4a2", true),
  ("e4506825-cd20-42db-ae23-1981d6aa0fb4", "OEICS", "OEICs", "", "7353dac6-779a-4bb5-8a45-ec92ae14c4a2", true),
  ("1be33674-0c89-40f9-9f7e-2c6785897deb", "UNIT_TRUSTS", "Unit Trusts","",  "7353dac6-779a-4bb5-8a45-ec92ae14c4a2", true);

INSERT INTO services
(id, name, display_name, description, parent_id, is_active)
VALUES
  ("cc06cc30-e19a-4e7f-9fb5-f74f3f368d07", "LUMP_SUM", "Lump Sum", "", "eb65fbba-b1f5-4b0a-b772-c1027a62f08e", true),
  ("b766c859-15c3-46f6-9190-d11697b97e14", "REGULAR_SAVINGS", "Regular Savings", "", "eb65fbba-b1f5-4b0a-b772-c1027a62f08e", true),
  ("557bab0c-5bb2-4f6c-8161-5ce1dcdca66f", "SAVINGS_FOR_CHILDREN", "Savings for Children", "", "eb65fbba-b1f5-4b0a-b772-c1027a62f08e", true),
  ("ac49f012-e51e-4de0-b44f-0aea2adc4491", "NATION_SAVINGS", "National Savings", "", "eb65fbba-b1f5-4b0a-b772-c1027a62f08e", true),
  ("7aa9646b-a32d-4d25-ae42-8349dfed19a4", "SCHOOL_FEES_PLANNING", "School fees planning", "", "eb65fbba-b1f5-4b0a-b772-c1027a62f08e", true);

INSERT INTO services
(id, name, display_name, description, parent_id, is_active)
VALUES
  ("42b07d64-73ec-43f2-ab1d-780cdef9dd19", "LIFE_INSURANCE", "Life Insurance", "", "78ab5721-0be5-4048-8dcd-34cf150d624b", true),
  ("13008cc5-1625-4b38-b470-2b9a3cf8d497", "CRITICAL_ILLNESS", "Critical Illness", "", "78ab5721-0be5-4048-8dcd-34cf150d624b", true),
  ("6f7f9ebe-0a0b-448b-93a5-67c4c80702b8", "BUILDINGS_AND_CONTENTS", "Buildings and contents", "", "78ab5721-0be5-4048-8dcd-34cf150d624b", true),
  ("5cb1c8fc-b9cd-4850-9aa3-20ca5157f254", "ACCIDENTS_AND_SICKNESS", "Accidents and sickness", "", "78ab5721-0be5-4048-8dcd-34cf150d624b", true),
  ("44a2e898-47e3-401d-b9e5-eef24d4b9e27", "PAYMENT_PROTECTION_INSURANCE", "Payment protection insurance", "", "78ab5721-0be5-4048-8dcd-34cf150d624b", true),
  ("e83e4c83-ebf4-4a4f-bc8c-b3f29f11cd5d", "TRAVEL", "Travel", "", "78ab5721-0be5-4048-8dcd-34cf150d624b", true),
  ("718c0b0f-a517-4a4b-a569-b0a43d2b3b59", "HEALTHCARE", "Healthcare", "", "78ab5721-0be5-4048-8dcd-34cf150d624b", true);


INSERT INTO services
(id, name, display_name, description, parent_id, is_active)
VALUES
  ("9ec217fc-1494-45b4-9ec3-3f8f30f948a3", "INCOME_TAX", "Income Tax", "", "e54879bc-4012-4fec-89ec-645395f25fa9", true),
  ("d8c87b37-01dd-4d7f-a1f3-a160f00904db", "ESTATE_PLANNING", "Estate planning", "","e54879bc-4012-4fec-89ec-645395f25fa9", true),
  ("df7f6e33-748a-436c-bcea-33f4a7283d14", "CAPITAL_GAINS_TAX", "Capital gains tax", "","e54879bc-4012-4fec-89ec-645395f25fa9", true),
  ("e0802805-0679-499a-b2fb-f165d58f6ea4", "TAXATION_PLANNING", "Taxation planning", "", "e54879bc-4012-4fec-89ec-645395f25fa9", true);

INSERT INTO services
(id, name, display_name, description, parent_id, is_active)
VALUES
  ("0ae9915c-9d09-4f60-b869-93accceec3ad", "COMMERCIAL_MORTGAGES", "Commercial mortgages", "", "4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", true),
  ("cbb532af-4e63-4915-8dca-b73fa787c7b9", "GROUP_CRITICAL_ILLNESS", "Critical Illness", "", "4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", true),
  ("5685b92a-3f69-40ff-879a-36ca7ba873a1", "GROUP_INCOME_PROTECTION", "Income Protection", "", "4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", true),
  ("cd77df22-bc15-484b-85b9-ac8ca7e10407", "GROUP_LIFE_INSURANCE", "Life Insurance", "", "4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", true),
  ("6a8a56a6-7436-4d01-93aa-4c86ad5ca8f9", "GROUP_PENSIONS", "Pensions", "", "4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", true),
  ("3e8fe0aa-6112-496a-a954-c342a7bb7467", "PRIVATE_MEDICAL_INSURANCE", "Private Medical Insurance", "", "4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", true),
  ("72be4bc7-57ff-4879-9899-c324ee9d6a81", "PROTECTION", "Protection", "", "4ca1d8eb-b0c3-4d03-a0d7-7ddee502c04d", true);