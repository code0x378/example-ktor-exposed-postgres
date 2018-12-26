CREATE USER taco WITH PASSWORD 'taco';

CREATE DATABASE taco;
GRANT ALL PRIVILEGES ON DATABASE taco to taco;

-- DROP TABLE public.notes;
CREATE TABLE public.notes (
	id uuid NOT NULL,
	title varchar(255) NULL,
	"ref" varchar(255) NULL,
	"version" int4 NOT NULL,
	created_at date NOT NULL,
	created_by varchar(50) NOT NULL,
	updated_at date NOT NULL,
	updated_by varchar(50) NOT NULL,
	body text NULL
);