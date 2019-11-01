CREATE TABLE "clients" (
  "id" SERIAL PRIMARY KEY,
  "email" varchar,
  "password" varchar,
  "authorities" varchar,
  "created_time" timestamp,
  "update_time" timestamp
);

CREATE TABLE "accounts" (
  "id" SERIAL PRIMARY KEY,
  "client_id" long,
  "created_time" timestamp,
  "update_time" timestamp
);

CREATE TABLE "transactions" (
  "id" SERIAL PRIMARY KEY,
  "account_id" long,
  "amount" decimal,
  "balance" decimal,
  "created_time" timestamp,
  "update_time" timestamp
);

ALTER TABLE "accounts" ADD FOREIGN KEY ("client_id") REFERENCES "clients" ("id");

ALTER TABLE "transactions" ADD FOREIGN KEY ("account_id") REFERENCES "accounts" ("id");
