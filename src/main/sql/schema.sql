/*
 * 
 * To find which tnsnames.ora is being used
 * c:\instantclient_11_2\tnsping XE_ISIS
 * 
 * 
 * 
 * http://www.oracle-base.com/articles/10g/Auditing_10gR2.php
 * 
 * 
 * initialization file init.ora
 * 
 * sqlplus SYS/password@XE_ISIS as sysdba
 * 
 * SQL> show parameter audit;
 * 
SQL> ALTER SYSTEM SET audit_trail=db,extended SCOPE=SPFILE;

System altered.

SQL> SHUTDOWN
Database closed.
Database dismounted.
ORACLE instance shut down.
SQL> STARTUP
ORACLE instance started.

Total System Global Area  289406976 bytes
Fixed Size                  1248600 bytes
Variable Size              71303848 bytes
Database Buffers          213909504 bytes
Redo Buffers                2945024 bytes
Database mounted.
Database opened.
SQL>
 * 
 * SQL> AUDIT ALL BY CREDITOS BY ACCESS;
 * SQL> AUDIT SELECT TABLE, UPDATE TABLE, INSERT TABLE, DELETE TABLE BY CREDITOS BY ACCESS;
 * SQL> AUDIT EXECUTE PROCEDURE BY CREDITOS BY ACCESS;
 * 
 * 
 * 
 * Ver vista DBA_COMMON_AUDIT_TRAIL
 *
 */

/*
 * alter database datafile 'C:\oraclexe\oradata\XE\SYSTEM.DBF' resize 4G;
 * alter database datafile 'C:\oraclexe\oradata\XE\UNDO.DBF' resize 1024M;
 * alter database datafile 'C:\ORACLEXE\ORADATA\XE\SYSTEM.DBF'  autoextend on;
 * 
 * 
 * 
 * Para deshabilitar audit
 * 
 * SQL> ALTER SYSTEM SET audit_trail=none SCOPE=SPFILE;
 * 
 * */


/* Reparticion */

CREATE TABLE CREDITOS.REPARTICION
(
	ID number(10,0) not null, 
	NOMBRE varchar2(255 char) NOT NULL,
	primary key (ID)

);

		
CREATE SEQUENCE CREDITOS.REPARTICION_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.REPARTICION_TRG
before insert ON CREDITOS.REPARTICION for each row
WHEN (
new.id is null
      )
begin
    select REPARTICION_SEQ.nextval into :new.id from dual;
end;
/ 

/* CentroSector */

CREATE TABLE CREDITOS.CENTROSECTOR
(
  ID            NUMBER(10)                      NOT NULL,
  CODIGOCENTRO  VARCHAR2(255 BYTE)              NOT NULL,
  CODIGOSECTOR  VARCHAR2(255 BYTE)              NOT NULL,
  NOMBRECENTRO  VARCHAR2(255 BYTE)              		,
  NOMBRESECTOR  VARCHAR2(255 BYTE)              		,
  REPARTICIONID NUMBER(10)                      NOT NULL,
  primary key (ID)
);

CREATE SEQUENCE CREDITOS.CENTROSECTOR_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.CENTROSECTOR_TRG
before insert ON CREDITOS.CENTROSECTOR for each row
WHEN (
new.id is null
      )
begin
    select CREDITOS.CENTROSECTOR_SEQ.nextval into :new.id from dual;
end;
/

ALTER TABLE CREDITOS.CENTROSECTOR ADD (
  CONSTRAINT "fk_ReparticionCentroSector" 
  FOREIGN KEY (REPARTICIONID) 
  REFERENCES CREDITOS.REPARTICION (ID));
  

  
create table PERSON (ID number(19,0) not null, 
	APELLIDONOMBRE varchar2(255 char), 
	CUIL varchar2(255 char), 
	CONDICION varchar2(255 char),
	primary key (ID));

CREATE SEQUENCE CREDITOS.PERSON_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.PERSON_TRG
	before insert ON CREDITOS.PERSON for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITOS.PERSON_SEQ.nextval into :new.id from dual;
	end;
/
	
create table CATEGORY (ID number(19,0) not null, 
		CODE varchar2(255 char), 
		primary key (ID));

		
CREATE SEQUENCE CREDITOS.CATEGORY_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.CATEGORY_TRG
	before insert ON CREDITOS.CATEGORY for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITOS.CATEGORY_SEQ.nextval into :new.id from dual;
	end;
/	
	

create table EMPLEO (ID number(19,0) not null, 
	FECHAINICIO     TIMESTAMP(7),
	FECHAFIN        TIMESTAMP(7),
	PERSONID number(19,0) not null, 
	CENTROSECTORID number(19,0) not null, 
	CATEGORYID number(19,0) not null,
	ESTADO  VARCHAR2(255 BYTE) NOT NULL,
	EMPLEO_ANTERIOR_ID number(19,0),
	OCCUPATIONAL_GROUP_ID number(19,0),
	primary key (ID));

CREATE SEQUENCE CREDITOS.EMPLEO_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.EMPLEO_TRG
	before insert ON CREDITOS.EMPLEO for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITOS.EMPLEO_SEQ.nextval into :new.id from dual;
	end; 
/
	
alter table EMPLEO add constraint fk_Empleo_CentroSector foreign key (CENTROSECTORID) references CENTROSECTOR;
alter table EMPLEO add constraint fk_Empleo_Person foreign key (PERSONID) references PERSON;	
alter table EMPLEO add constraint fk_Empleo_Category foreign key (CATEGORYID) references CATEGORY;


create table MOVIMIENTOCREDITOS (ID number(19,0) not null, 
	CANTIDADCREDITOS number(10,0) not null, 
	EMPLEOID number(19,0) not null, 
	TIPOMOVIMIENTOCREDITOS varchar2(255 char) not null, 
	OBSERVACIONES  varchar2(255 char),
	CREDITSPERIODID number(19,0) not null,
	GRANTED_STATUS varchar2(255 char) not null,
	primary key (ID));
	
alter table MOVIMIENTOCREDITOS add constraint fk_MovimientoCreditos_Empleo 
	foreign key (EMPLEOID) references EMPLEO;
	
CREATE SEQUENCE CREDITOS.MOVIMIENTOCREDITOS_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.MOVIMIENTOCREDITOS_TRG
	before insert ON CREDITOS.MOVIMIENTOCREDITOS for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITOS.MOVIMIENTOCREDITOS_SEQ.nextval into :new.id from dual;
	end; 
/	

-- SECURITY -------------------------------------------------------------------------------------------------

CREATE TABLE "CREDITOS"."SEC_ACCOUNT" 
   (	"ID" NUMBER(10,0) NOT NULL ENABLE, 
	"NAME" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"PASSWORD" VARCHAR2(255) NOT NULL ENABLE, 
	"ENABLED" NUMBER(1,0) DEFAULT 1 NOT NULL ENABLE, 
	"UNLOCKED" NUMBER(1,0) DEFAULT 1 NOT NULL ENABLE, 
	"EXPIRE" TIMESTAMP (7), 
	"EXPIREPASSWORD" TIMESTAMP (7), 
	 PRIMARY KEY ("ID"));

CREATE SEQUENCE CREDITOS.SEC_ACCOUNT_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.SEC_ACCOUNT_TRG
	before insert ON CREDITOS.SEC_ACCOUNT for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITOS.SEC_ACCOUNT_SEQ.nextval into :new.id from dual;
	end; 	
/
	
CREATE TABLE "CREDITOS"."SEC_ROLE" 
   (	"ID" NUMBER(10,0) NOT NULL ENABLE, 
	"NAME" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(500) NOT NULL ENABLE, 
	"ENABLED" NUMBER(1,0) DEFAULT 1 NOT NULL ENABLE, 
	"SORTORDER" NUMBER(10,0) DEFAULT 1 NOT NULL ENABLE, 
	 PRIMARY KEY ("ID")
);	
	
	
CREATE SEQUENCE CREDITOS.SEC_ROLE_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.SEC_ROLE_TRG
	before insert ON CREDITOS.SEC_ROLE for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITOS.SEC_ROLE_SEQ.nextval into :new.id from dual;
	end; 
/	
	

-- ---------------------------------------------------------------------------------

CREATE TABLE CREDITOS.SEC_ACCOUNT_ROLE
(
  SORTORDER  NUMBER(10)                         DEFAULT 1                     NOT NULL,
  ACCOUNTID  NUMBER(10)                         NOT NULL,
  ROLEID     NUMBER(10)                         NOT NULL,
  PRIMARY KEY ("ACCOUNTID", "ROLEID")
);


CREATE INDEX CREDITOS."fk_AccountRole__Role" ON CREDITOS.SEC_ACCOUNT_ROLE(ROLEID);


CREATE INDEX CREDITOS."idx_accountRoleSortOrder" ON CREDITOS.SEC_ACCOUNT_ROLE
(SORTORDER);

CREATE UNIQUE INDEX "CREDITOS"."accountId" ON "CREDITOS"."SEC_ACCOUNT_ROLE" ("ACCOUNTID", "SORTORDER");



ALTER TABLE CREDITOS.SEC_ACCOUNT_ROLE ADD (
  CONSTRAINT "fk_AccountRole__Account" 
  FOREIGN KEY (ACCOUNTID) 
  REFERENCES CREDITOS.SEC_ACCOUNT (ID)
  ENABLE VALIDATE,
  CONSTRAINT "fk_AccountRole__Role" 
  FOREIGN KEY (ROLEID) 
  REFERENCES CREDITOS.SEC_ROLE (ID)
  ENABLE VALIDATE);


-- ---------------------------------------------------------------------------------

CREATE TABLE "CREDITOS"."SEC_ROLE_AGGR_ROLE" 
   (	"ROLEID" NUMBER(10,0), 
	"AGGRROLEID" NUMBER(10,0), 
	"SORTORDER" NUMBER(10,0) NOT NULL
   );

CREATE INDEX "CREDITOS"."fk_RoleAggrRole__AggrRole" ON "CREDITOS"."SEC_ROLE_AGGR_ROLE" ("AGGRROLEID") ;

CREATE INDEX "CREDITOS"."idx_aggrRoleSortOrder" ON "CREDITOS"."SEC_ROLE_AGGR_ROLE" ("SORTORDER");



  
ALTER TABLE CREDITOS.SEC_ROLE_AGGR_ROLE ADD (
  CONSTRAINT SEC_ROLE_AGGR_ROLE_PK
  PRIMARY KEY
  (ROLEID, AGGRROLEID)
  );


	
CREATE TABLE "CREDITOS"."SEC_PERMISSION_CONTEXT" 
   (	
   	"ID" NUMBER(10,0) NOT NULL ENABLE, 
	"NAME" VARCHAR2(255) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(500), 
	"SORTORDER" NUMBER(10,0), 
	"ENABLED" NUMBER(1,0) NOT NULL ENABLE, 
	 PRIMARY KEY ("ID"));
	 
CREATE UNIQUE INDEX CREDITOS."idx_name" ON CREDITOS.SEC_PERMISSION_CONTEXT(NAME);
CREATE INDEX CREDITOS."idx_permissionContextSortOrder" ON CREDITOS.SEC_PERMISSION_CONTEXT(SORTORDER);
	
CREATE SEQUENCE CREDITOS.SEC_PERMISSION_CONTEXT_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.SEC_PERMISSION_CONTEXT_TRG
	before insert ON CREDITOS.SEC_PERMISSION_CONTEXT for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITOS.SEC_PERMISSION_CONTEXT_SEQ.nextval into :new.id from dual;
	end; 
/	
	

CREATE TABLE CREDITOS.SEC_PERMISSION_BIT
(
  CONTEXTID    NUMBER(10)                       NOT NULL,
  POSITION     NUMBER(11)                       NOT NULL,
  NAME         VARCHAR2(255 BYTE)               NOT NULL,
  DESCRIPTION  VARCHAR2(500 BYTE),
  SORTORDER    NUMBER(11)
);



CREATE UNIQUE INDEX CREDITOS."idx_contextId" ON CREDITOS.SEC_PERMISSION_BIT
(CONTEXTID, NAME);


CREATE INDEX CREDITOS."idx_permissionBitName" ON CREDITOS.SEC_PERMISSION_BIT
(NAME);


CREATE INDEX CREDITOS."idx_permissionBitSortOrder" ON CREDITOS.SEC_PERMISSION_BIT
(SORTORDER);


ALTER TABLE CREDITOS.SEC_PERMISSION_BIT ADD (
  PRIMARY KEY
  (CONTEXTID, POSITION));

ALTER TABLE CREDITOS.SEC_PERMISSION_BIT ADD (
  CONSTRAINT "fk_PermBit_PermissionContext" 
  FOREIGN KEY (CONTEXTID) 
  REFERENCES CREDITOS.SEC_PERMISSION_CONTEXT (ID));
  
  
  
  
CREATE TABLE CREDITOS.SEC_PERMISSION_GRANTED
(
  ROLEID       NUMBER(10)                       NOT NULL,
  CONTEXTID    NUMBER(10)                       NOT NULL,
  PERMISSIONS  NUMBER(20)                       NOT NULL,
  DENY         NUMBER(1)                        NOT NULL
);



CREATE INDEX CREDITOS."fk_PermissionGranted__Permissi" ON CREDITOS.SEC_PERMISSION_GRANTED
(CONTEXTID);


ALTER TABLE CREDITOS.SEC_PERMISSION_GRANTED ADD (
  PRIMARY KEY
  (ROLEID, CONTEXTID, DENY)
  );

ALTER TABLE CREDITOS.SEC_PERMISSION_GRANTED ADD (
  CONSTRAINT "fk_PermGrnted_PermContext" 
  FOREIGN KEY (CONTEXTID) 
  REFERENCES CREDITOS.SEC_PERMISSION_CONTEXT (ID)
  ENABLE VALIDATE,
  CONSTRAINT "fk_PermGrnted_Role" 
  FOREIGN KEY (ROLEID) 
  REFERENCES CREDITOS.SEC_ROLE (ID)
  ENABLE VALIDATE);

  
--  -------------------------- SEC_ACCOUNT_SETTING ---------------------------------

  CREATE TABLE CREDITOS.SEC_ACCOUNT_SETTING
(
  ACCOUNTID      NUMBER(10)                     NOT NULL,
  SETTING_TAG    VARCHAR2(255 BYTE)             NOT NULL,
  SETTING_VALUE  CLOB
);




ALTER TABLE CREDITOS.SEC_ACCOUNT_SETTING ADD (
  PRIMARY KEY
  (ACCOUNTID, SETTING_TAG));

ALTER TABLE CREDITOS.SEC_ACCOUNT_SETTING ADD (
  CONSTRAINT "fk_AccountSetting__Account" 
  FOREIGN KEY (ACCOUNTID) 
  REFERENCES CREDITOS.SEC_ACCOUNT (ID)
  ENABLE VALIDATE);

  
CREATE TABLE CREDITOS.REPARTICION_ACCOUNT
(
  ACCOUNTID      NUMBER(10)                     NOT NULL,
  REPARTICIONID  NUMBER(10)                     NOT NULL
);



ALTER TABLE CREDITOS.REPARTICION_ACCOUNT ADD (
  PRIMARY KEY
  (ACCOUNTID, REPARTICIONID)
);

ALTER TABLE CREDITOS.REPARTICION_ACCOUNT ADD (
  CONSTRAINT "fk_RepAccount__Account" 
  FOREIGN KEY (ACCOUNTID) 
  REFERENCES CREDITOS.SEC_ACCOUNT (ID)
  ENABLE VALIDATE,
  CONSTRAINT "fk_RepAccount__Reparticion" 
  FOREIGN KEY (REPARTICIONID) 
  REFERENCES CREDITOS.REPARTICION (ID)
  ENABLE VALIDATE);
  
  
	create table CREDITSPERIOD (
        ID number(19,0) not null,
        NAME varchar2(255 char),
        DESCRIPTION varchar2(255 char),
        STARTDATE TIMESTAMP(7),
        ENDDATE TIMESTAMP(7),
        STATUS varchar2(255 char) not null,
        PREVIOUS_CREDITSPERIOD_ID number(19,0),
        primary key (ID)
    );
    
 CREATE SEQUENCE CREDITOS.CREDITSPERIOD_SEQ
	  START WITH 1
	  MAXVALUE 9999999999999999999999999999
	  MINVALUE 1
	  NOCYCLE
	  CACHE 20
	  NOORDER;
	
	
 CREATE OR REPLACE TRIGGER CREDITOS.CREDITSPERIOD_TRG
	before insert ON CREDITOS.CREDITSPERIOD for each row
	WHEN (
	new.id is null
	      )
	begin
	    select CREDITSPERIOD_SEQ.nextval into :new.id from dual;
	end;
	/ 

alter table CREDITOS.MOVIMIENTOCREDITOS 
        add constraint fk_MovimientoCredito_CreditsPeriod 
        foreign key (CREDITSPERIODID) 
        references CREDITSPERIOD;
        
        
alter table CREDITOS.EMPLEO 
        add constraint fk_Empleo_Empleo_Anterior 
        foreign key (EMPLEO_ANTERIOR_ID) 
        references Empleo;        

      
alter table CREDITOS.CREDITSPERIOD 
        add constraint fk_CRPERIOD_CRPERIOD_PREVIOUS 
        foreign key (PREVIOUS_CREDITSPERIOD_ID) 
        references CREDITSPERIOD;
        
        
/* Occupational group*/
        
create table CREDITOS.OCCUPATIONAL_GROUP (
        ID number(19,0) not null,
        CODE varchar2(255 char) not null,
        NAME varchar2(255 char) not null,
        DESCRIPTION varchar2(255 char),
        PARENT_OCCUP_GROUP_ID number(19,0),
		MINIMUM_CATEGORY_ID number(19,0) not null,
        MAXIMUM_CATEGORY_ID number(19,0) not null,
        primary key (ID)
    );
       
alter table CREDITOS.OCCUPATIONAL_GROUP 
        add constraint fk_OccupGroup_parentOccupGroup 
        foreign key (PARENT_OCCUP_GROUP_ID) 
        references OCCUPATIONAL_GROUP;

alter table CREDITOS.EMPLEO 
        add constraint fk_Employment_OccupGroup 
        foreign key (OCCUPATIONAL_GROUP_ID) 
        references OCCUPATIONAL_GROUP;

alter table CREDITOS.OCCUPATIONAL_GROUP 
        add constraint fk_occgroup_max_cat 
        foreign key (MAXIMUM_CATEGORY_ID) 
        references CATEGORY;

alter table CREDITOS.OCCUPATIONAL_GROUP 
        add constraint fk_occgroup_min_cat 
        foreign key (MINIMUM_CATEGORY_ID) 
        references CATEGORY;

        
CREATE SEQUENCE CREDITOS.OCCUPATIONAL_GROUP_SEQ
  START WITH 1
  MAXVALUE 9999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;


CREATE OR REPLACE TRIGGER CREDITOS.OCCUPATIONAL_GROUP_TRG
before insert ON CREDITOS.OCCUPATIONAL_GROUP for each row
WHEN (
new.id is null
      )
begin
    select CREDITOS.OCCUPATIONAL_GROUP_SEQ.nextval into :new.id from dual;
end;
/ 
  