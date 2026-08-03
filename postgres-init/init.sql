-- postgres-init/init.sql

-- Create the Scheduling Database
CREATE DATABASE scheduling_db;

-- Create the Billing Database
CREATE DATABASE billing_db;

-- Grant privileges to the default user
GRANT ALL PRIVILEGES ON DATABASE scheduling_db TO clinic_admin;
GRANT ALL PRIVILEGES ON DATABASE billing_db TO clinic_admin;