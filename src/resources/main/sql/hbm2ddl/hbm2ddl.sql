
    create table AGENTE (
        ID number(19,0) not null,
        APELLIDONOMBRE varchar2(255 char),
        CUIL varchar2(255 char),
        CONDICION varchar2(255 char),
        primary key (ID)
    );

    create table CATEGORY (
        ID number(19,0) not null,
        CODE varchar2(255 char),
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
        PREVIOUS_CREDITSPERIOD_ID number(19,0),
        primary key (ID)
    );

    create table EMPLEO (
        ID number(19,0) not null,
        FECHAINICIO timestamp,
        FECHAFIN timestamp,
        ESTADO varchar2(255 char),
        AGENTEID number(19,0) not null,
        CENTROSECTORID number(19,0) not null,
        CATEGORYID number(19,0) not null,
        EMPLEO_ANTERIOR_ID number(19,0),
        OCCUPATIONAL_GROUP_ID number(19,0) not null,
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

    create table OCCUPATIONAL_GROUP (
        ID number(19,0) not null,
        CODE varchar2(255 char) not null,
        NAME varchar2(255 char) not null,
        DESCRIPTION varchar2(255 char),
        PARENT_OCCUP_GROUP_ID number(19,0),
        MINIMUM_CATEGORY_ID number(19,0) not null,
        MAXIMUM_CATEGORY_ID number(19,0) not null,
        primary key (ID)
    );

    create table REPARTICION (
        ID number(19,0) not null,
        CODE varchar2(255 char),
        NOMBRE varchar2(255 char),
        primary key (ID)
    );

    alter table CENTROSECTOR 
        add constraint fk_ReparticionCentroSector 
        foreign key (REPARTICIONID) 
        references REPARTICION;

    alter table CREDITSPERIOD 
        add constraint fk_CRPERIOD_CRPERIOD_PREVIOUS 
        foreign key (PREVIOUS_CREDITSPERIOD_ID) 
        references CREDITSPERIOD;

    alter table EMPLEO 
        add constraint fk_Empleo_CentroSector 
        foreign key (CENTROSECTORID) 
        references CENTROSECTOR;

    alter table EMPLEO 
        add constraint fk_Empleo_Agente 
        foreign key (AGENTEID) 
        references AGENTE;

    alter table EMPLEO 
        add constraint fk_Empleo_Category 
        foreign key (CATEGORYID) 
        references CATEGORY;

    alter table EMPLEO 
        add constraint fk_Employment_OccupationalGroup 
        foreign key (OCCUPATIONAL_GROUP_ID) 
        references OCCUPATIONAL_GROUP;

    alter table EMPLEO 
        add constraint fk_Empleo_Empleo_Anterior 
        foreign key (EMPLEO_ANTERIOR_ID) 
        references EMPLEO;

    alter table MOVIMIENTOCREDITOS 
        add constraint fk_MovimientoCreditos_Empleo 
        foreign key (EMPLEOID) 
        references EMPLEO;

    alter table MOVIMIENTOCREDITOS 
        add constraint fk_MovimientoCredito_CreditsPeriod 
        foreign key (CREDITSPERIODID) 
        references CREDITSPERIOD;

    alter table OCCUPATIONAL_GROUP 
        add constraint fk_occgroup_max_cat 
        foreign key (MAXIMUM_CATEGORY_ID) 
        references CATEGORY;

    alter table OCCUPATIONAL_GROUP 
        add constraint fk_occgroup_min_cat 
        foreign key (MINIMUM_CATEGORY_ID) 
        references CATEGORY;

    alter table OCCUPATIONAL_GROUP 
        add constraint fk_OccGroup_parentOccGroup 
        foreign key (PARENT_OCCUP_GROUP_ID) 
        references OCCUPATIONAL_GROUP;

    create sequence AGENTE_SEQ;

    create sequence CATEGORY_SEQ;

    create sequence CENTROSECTOR_SEQ;

    create sequence CREDITSPERIOD_SEQ;

    create sequence EMPLEO_SEQ;

    create sequence MOVIMIENTOCREDITOS_SEQ;

    create sequence OCCUPATIONAL_GROUP_SEQ;

    create sequence REPARTICION_SEQ;
