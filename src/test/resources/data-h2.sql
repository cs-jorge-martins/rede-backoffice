/*© Copyright 2016 Rede S.A.
=============================================
Autor: Karlisson Bezerra <kbezerra@userede.com.br>
Data: 27/12/2016
Empresa: Thoughtworks
Descrição: Este arquivo é executado durante os testes para inicializar os dados da base de teste.*/


DROP TABLE USER IF EXISTS;
CREATE TABLE USER (
  ID BIGINT NOT NULL AUTO_INCREMENT,
  EMAIL VARCHAR(255),
  PRIMARY KEY (ID));

ALTER TABLE IF EXISTS USER ADD COLUMN PRIMARY_USER_ID BIGINT;
ALTER TABLE IF EXISTS USER ADD CONSTRAINT FK_USER_PRIMARY_USER_ID FOREIGN KEY (PRIMARY_USER_ID) REFERENCES USER (ID);

DROP TABLE USER_PV IF EXISTS;
CREATE TABLE USER_PV (
  USER_ID BIGINT NOT NULL,
  PV_ID BIGINT NOT NULL,
  PRIMARY KEY (USER_ID, PV_ID));

DROP TABLE ACQUIRER IF EXISTS;
CREATE TABLE ACQUIRER (
  ID BIGINT NOT NULL,
  NAME VARCHAR(50),
  PRIMARY KEY (ID));


ALTER TABLE IF EXISTS USER ADD CONSTRAINT EMAIL_UNIQUE UNIQUE (EMAIL);

ALTER TABLE IF EXISTS USER_PV ADD CONSTRAINT FK_PV FOREIGN KEY (PV_ID) REFERENCES PV (ID);

ALTER TABLE IF EXISTS USER_PV ADD CONSTRAINT FK_USER FOREIGN KEY (USER_ID) REFERENCES USER (ID);

-- ACQUIRER
INSERT INTO ACQUIRER (ID, NAME) VALUES (1, 'REDE');
INSERT INTO ACQUIRER (ID, NAME) VALUES (2, 'CIELO');

-- PV
INSERT INTO PV (ID, CODE, ACQUIRER_ID) VALUES (1, '12345678', 1);
INSERT INTO PV (ID, CODE, ACQUIRER_ID, HEADQUARTER_ID) VALUES (2, '22345678', 1, 1);
INSERT INTO PV (ID, CODE, ACQUIRER_ID, HEADQUARTER_ID) VALUES (3, '32345678', 1, 2);
INSERT INTO PV (ID, CODE, ACQUIRER_ID) VALUES (4, '42345678', 1);
INSERT INTO PV (ID, CODE, ACQUIRER_ID) VALUES (5, '52345678', 2);
INSERT INTO PV (ID, CODE, ACQUIRER_ID) VALUES (6, '92315670', 2);

-- USER
INSERT INTO USER (EMAIL) VALUES ('foo@bar.com');
INSERT INTO USER (EMAIL) VALUES ('bar@foo.com');

-- USER_PV
INSERT INTO USER_PV (USER_ID, PV_ID) VALUES (1, 1);
INSERT INTO USER_PV (USER_ID, PV_ID) VALUES (1, 3);
INSERT INTO USER_PV (USER_ID, PV_ID) VALUES (1, 5);
INSERT INTO USER_PV (USER_ID, PV_ID) VALUES (2, 5);
INSERT INTO USER_PV (USER_ID, PV_ID) VALUES (2, 6);
