--liquibase formatted: sql
--changeset <shmax>:<add-external-id-to-movie-character>

ALTER TABLE public.movie_character
ADD external_id bigint;

--rollback ALTER TABLE public.movie_character DROP COLUMN external_id;