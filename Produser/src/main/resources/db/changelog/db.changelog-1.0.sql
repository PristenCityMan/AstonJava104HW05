--liquibase formatted sql

--changeset timonovs:1
CREATE TABLE IF NOT EXISTS public.users
(
    id bigserial NOT NULL,
    name character varying(100),
    email character varying(100),
    age integer,
    created_at timestamp with time zone,
    PRIMARY KEY (id)
);

--changeset timonovs:2
INSERT INTO public.users (id, name, email, age, created_at) VALUES (1, 'Sergey', 'Sergey@mail.ru', 38, '2026-10-01 00:00:00+03') ON CONFLICT DO NOTHING;
INSERT INTO public.users (id, name, email, age, created_at) VALUES (2, 'DDD', 'D@D.ru', 31, '2026-08-15 16:14:27.204809+03') ON CONFLICT DO NOTHING;
ALTER SEQUENCE users_id_seq RESTART WITH 3;