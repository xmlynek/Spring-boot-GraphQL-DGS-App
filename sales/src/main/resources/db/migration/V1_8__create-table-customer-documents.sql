CREATE TABLE IF NOT EXISTS "customer_documents" (
  "uuid"                      uuid NOT NULL PRIMARY KEY,
  "customer_uuid"             uuid references "customers",
  "document_type"             varchar NOT NULL,
  "document_path"             varchar NOT NULL
);
