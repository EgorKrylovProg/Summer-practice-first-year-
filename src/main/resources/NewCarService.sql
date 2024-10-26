PGDMP                      |            NewCarService    16.3 (Debian 16.3-1.pgdg120+1)    16.3     0           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            1           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            2           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            3           1262    16616    NewCarService    DATABASE     z   CREATE DATABASE "NewCarService" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'en_US.utf8';
    DROP DATABASE "NewCarService";
                postgres    false            �            1259    16663    Applications    TABLE     �   CREATE TABLE public."Applications" (
    id bigint NOT NULL,
    date_applications timestamp without time zone NOT NULL,
    breakdown character varying(100) NOT NULL,
    customer_id bigint,
    car_id bigint NOT NULL
);
 "   DROP TABLE public."Applications";
       public         heap    postgres    false            �            1259    16624    Cars    TABLE       CREATE TABLE public."Cars" (
    id bigint NOT NULL,
    manufacturer character varying(30) NOT NULL,
    color character varying(10) NOT NULL,
    horsepower integer,
    date_production date,
    number_car character varying(20) NOT NULL,
    customer_id bigint
);
    DROP TABLE public."Cars";
       public         heap    postgres    false            �            1259    16623 
   Car_id_seq    SEQUENCE     �   ALTER TABLE public."Cars" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Car_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    218            �            1259    16618 	   Customers    TABLE     �   CREATE TABLE public."Customers" (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    surname character varying(30) NOT NULL,
    age smallint,
    birth_date date
);
    DROP TABLE public."Customers";
       public         heap    postgres    false            �            1259    16617    Customer_id_seq    SEQUENCE     �   ALTER TABLE public."Customers" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public."Customer_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    216            �            1259    16662    applications_id_seq    SEQUENCE     �   ALTER TABLE public."Applications" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.applications_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    220            -          0    16663    Applications 
   TABLE DATA           _   COPY public."Applications" (id, date_applications, breakdown, customer_id, car_id) FROM stdin;
    public          postgres    false    220   �       +          0    16624    Cars 
   TABLE DATA           o   COPY public."Cars" (id, manufacturer, color, horsepower, date_production, number_car, customer_id) FROM stdin;
    public          postgres    false    218   �       )          0    16618 	   Customers 
   TABLE DATA           I   COPY public."Customers" (id, name, surname, age, birth_date) FROM stdin;
    public          postgres    false    216   �       4           0    0 
   Car_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public."Car_id_seq"', 2, true);
          public          postgres    false    217            5           0    0    Customer_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public."Customer_id_seq"', 2, true);
          public          postgres    false    215            6           0    0    applications_id_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.applications_id_seq', 1, false);
          public          postgres    false    219            �           2606    16622    Customers Customer_pkey 
   CONSTRAINT     Y   ALTER TABLE ONLY public."Customers"
    ADD CONSTRAINT "Customer_pkey" PRIMARY KEY (id);
 E   ALTER TABLE ONLY public."Customers" DROP CONSTRAINT "Customer_pkey";
       public            postgres    false    216            �           2606    16667    Applications pk_applications 
   CONSTRAINT     \   ALTER TABLE ONLY public."Applications"
    ADD CONSTRAINT pk_applications PRIMARY KEY (id);
 H   ALTER TABLE ONLY public."Applications" DROP CONSTRAINT pk_applications;
       public            postgres    false    220            �           2606    16676    Cars pk_car 
   CONSTRAINT     K   ALTER TABLE ONLY public."Cars"
    ADD CONSTRAINT pk_car PRIMARY KEY (id);
 7   ALTER TABLE ONLY public."Cars" DROP CONSTRAINT pk_car;
       public            postgres    false    218            �           2606    16630    Cars unique_car_number 
   CONSTRAINT     Y   ALTER TABLE ONLY public."Cars"
    ADD CONSTRAINT unique_car_number UNIQUE (number_car);
 B   ALTER TABLE ONLY public."Cars" DROP CONSTRAINT unique_car_number;
       public            postgres    false    218            �           2606    16661    Cars unique_id_customer_id 
   CONSTRAINT     b   ALTER TABLE ONLY public."Cars"
    ADD CONSTRAINT unique_id_customer_id UNIQUE (id, customer_id);
 F   ALTER TABLE ONLY public."Cars" DROP CONSTRAINT unique_id_customer_id;
       public            postgres    false    218    218            �           2606    16668 )   Applications fk_applications_customer_car    FK CONSTRAINT     �   ALTER TABLE ONLY public."Applications"
    ADD CONSTRAINT fk_applications_customer_car FOREIGN KEY (customer_id, car_id) REFERENCES public."Cars"(customer_id, id);
 U   ALTER TABLE ONLY public."Applications" DROP CONSTRAINT fk_applications_customer_car;
       public          postgres    false    3220    220    220    218    218            �           2606    16631    Cars fk_car_customer    FK CONSTRAINT        ALTER TABLE ONLY public."Cars"
    ADD CONSTRAINT fk_car_customer FOREIGN KEY (customer_id) REFERENCES public."Customers"(id);
 @   ALTER TABLE ONLY public."Cars" DROP CONSTRAINT fk_car_customer;
       public          postgres    false    218    216    3214            -      x������ � �      +      x������ � �      )      x������ � �     