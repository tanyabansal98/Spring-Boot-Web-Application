-- ═══════════════════════════════════════════════
--  Auth Service – Oracle Schema Setup
-- ═══════════════════════════════════════════════
-- Run this with your ADMIN user (not MVC_APP).
-- This creates a separate AUTH_USER schema for
-- the auth-service microservice.
-- ═══════════════════════════════════════════════

-- 1. Create the AUTH_USER schema
CREATE USER AUTH_USER IDENTIFIED BY Auth_Password123
    DEFAULT TABLESPACE DATA
    TEMPORARY TABLESPACE TEMP
    QUOTA UNLIMITED ON DATA;

-- 2. Grant privileges
GRANT CONNECT, RESOURCE TO AUTH_USER;
GRANT CREATE SESSION TO AUTH_USER;
GRANT CREATE TABLE TO AUTH_USER;
GRANT CREATE SEQUENCE TO AUTH_USER;

-- 3. Connect as AUTH_USER and create the table
-- (or run this part while connected as ADMIN, prefixed with AUTH_USER.)

CREATE TABLE AUTH_USER.App_Users (
    User_Id      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Username     VARCHAR2(50)   NOT NULL UNIQUE,
    Password_Hash VARCHAR2(200) NOT NULL,
    Role         VARCHAR2(20)   DEFAULT 'USER' NOT NULL,
    Created_At   TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);
