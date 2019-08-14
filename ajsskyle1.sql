--
-- PostgreSQL database dump
--

-- Dumped from database version 11.3
-- Dumped by pg_dump version 11.3

-- Started on 2019-08-09 09:25:15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 199 (class 1259 OID 16942)
-- Name: message; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.message (
    id integer NOT NULL,
    sender_id integer NOT NULL,
    receiver_id integer NOT NULL,
    type character varying(255) NOT NULL,
    content text NOT NULL,
    msg_height integer NOT NULL,
    created_date timestamp(0) without time zone NOT NULL,
    file_name character varying(255)
);


ALTER TABLE public.message OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16938)
-- Name: message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.message_id_seq OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16933)
-- Name: migration_versions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.migration_versions (
    version character varying(14) NOT NULL,
    executed_at timestamp(0) without time zone NOT NULL
);


ALTER TABLE public.migration_versions OWNER TO postgres;

--
-- TOC entry 2836 (class 0 OID 0)
-- Dependencies: 196
-- Name: COLUMN migration_versions.executed_at; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.migration_versions.executed_at IS '(DC2Type:datetime_immutable)';


--
-- TOC entry 200 (class 1259 OID 16950)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    birth_date date NOT NULL,
    password character varying(255) NOT NULL,
    legend character varying(255) DEFAULT NULL::character varying,
    avatar_path character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16940)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 2829 (class 0 OID 16942)
-- Dependencies: 199
-- Data for Name: message; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.message (id, sender_id, receiver_id, type, content, msg_height, created_date, file_name) FROM stdin;
\.


--
-- TOC entry 2826 (class 0 OID 16933)
-- Dependencies: 196
-- Data for Name: migration_versions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.migration_versions (version, executed_at) FROM stdin;
\.


--
-- TOC entry 2830 (class 0 OID 16950)
-- Dependencies: 200
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, first_name, last_name, email, birth_date, password, legend, avatar_path) FROM stdin;
9	Ange	Eric	aaa@hkj.com	2013-01-01	azerty	123123	C:\\Users\\AmenJs\\IdeaProjects\\AJSSkyle\\sauvegarde\\avatar/aaa@hkj.com.png
10	Mouton	Belier	mouton@belier.com	2013-01-01	mouton	Mouton	C:\\Users\\AmenJs\\IdeaProjects\\AJSSkyle\\sauvegarde\\avatar/mouton@belier.com.jpg
11	Oeil	Yeux	oeil@yeux.com	2013-01-01	oeil	Yeux	C:\\Users\\AmenJs\\IdeaProjects\\AJSSkyle\\sauvegarde\\avatar/oeil@yeux.com.png
\.


--
-- TOC entry 2837 (class 0 OID 0)
-- Dependencies: 197
-- Name: message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.message_id_seq', 1, false);


--
-- TOC entry 2838 (class 0 OID 0)
-- Dependencies: 198
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 11, true);


--
-- TOC entry 2701 (class 2606 OID 16949)
-- Name: message message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.message
    ADD CONSTRAINT message_pkey PRIMARY KEY (id);


--
-- TOC entry 2699 (class 2606 OID 16937)
-- Name: migration_versions migration_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.migration_versions
    ADD CONSTRAINT migration_versions_pkey PRIMARY KEY (version);


--
-- TOC entry 2704 (class 2606 OID 16958)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2702 (class 1259 OID 16959)
-- Name: uniq_1483a5e9e7927c74; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX uniq_1483a5e9e7927c74 ON public.users USING btree (email);


-- Completed on 2019-08-09 09:25:15

--
-- PostgreSQL database dump complete
--

