
    create table AGENTE (
        ID number(19,0) not null,
        APELLIDONOMBRE varchar2(255 char),
        CUIL varchar2(255 char),
        CONDICION varchar2(255 char),
        primary key (ID)
    );

    create table CATEGORIA (
        ID number(19,0) not null,
        CODIGO varchar2(255 char),
        primary key (ID)
    );

    create table CENTROSECTOR (
        ID number(19,0) not null,
        CODIGOCENTRO varchar2(255 char),
        CODIGOSECTOR varchar2(255 char),
        NOMBRECENTRO varchar2(255 char),
        NOMBRESECTOR varchar2(255 char),
        REPARTICIONID number(19,0),
        primary key (ID)
    );

    create table CREDITSPERIOD (
        ID number(19,0) not null,
        NAME varchar2(255 char),
        DESCRIPTION varchar2(255 char),
        STARTDATE timestamp,
        ENDDATE timestamp,
        STATUS varchar2(255 char) not null,
        primary key (ID)
    );

    create table EMPLEO (
        ID number(19,0) not null,
        FECHAINICIO timestamp,
        FECHAFIN timestamp,
        ESTADO varchar2(255 char),
        AGENTEID number(19,0) not null,
        CENTROSECTORID number(19,0) not null,
        CATEGORIAID number(19,0) not null,
        EMPLEO_ANTERIOR_ID number(19,0),
        primary key (ID)
    );

    create table MOVIMIENTOCREDITOS (
        ID number(19,0) not null,
        CANTIDADCREDITOS number(10,0),
        EMPLEOID number(19,0),
        CREDITSPERIODID number(19,0) not null,
        TIPOMOVIMIENTOCREDITOS varchar2(255 char) not null,
        GRANTED_STATUS varchar2(255 char) not null,
        OBSERVACIONES varchar2(255 char),
        primary key (ID)
    );

    create table REPARTICION (
        ID number(19,0) not null,
        NOMBRE varchar2(255 char),
        primary key (ID)
    );

    alter table CENTROSECTOR 
        add constraint fk_ReparticionCentroSector 
        foreign key (REPARTICIONID) 
        references REPARTICION;

    alter table EMPLEO 
        add constraint fk_Empleo_CentroSector 
        foreign key (CENTROSECTORID) 
        references CENTROSECTOR;

    alter table EMPLEO 
        add constraint fk_Empleo_Agente 
        foreign key (AGENTEID) 
        references AGENTE;

    alter table EMPLEO 
        add constraint fk_Empleo_Categoria 
        foreign key (CATEGORIAID) 
        references CATEGORIA;

    alter table EMPLEO 
        add constraint fk_Empleo_Empleo_Anterior 
        foreign key (EMPLEO_ANTERIOR_ID) 
        references CENTROSECTOR;

    alter table MOVIMIENTOCREDITOS 
        add constraint fk_MovimientoCreditos_Empleo 
        foreign key (EMPLEOID) 
        references EMPLEO;

    alter table MOVIMIENTOCREDITOS 
        add constraint fk_MovimientoCredito_CreditsPeriod 
        foreign key (CREDITSPERIODID) 
        references CREDITSPERIOD;

    create sequence AGENTE_SEQ;

    create sequence CATEGORIA_SEQ;

    create sequence CENTROSECTOR_SEQ;

    create sequence CREDITSPERIOD_SEQ;

    create sequence EMPLEO_SEQ;

    create sequence MOVIMIENTOCREDITOS_SEQ;

    create sequence REPARTICION_SEQ;
