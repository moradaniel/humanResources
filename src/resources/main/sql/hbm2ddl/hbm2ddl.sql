
    create table PERSON (
        ID number(19,0) not null,
        APELLIDONOMBRE varchar2(255 char),
        CUIL varchar2(255 char),
        CONDITION varchar2(255 char),
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

    create table EMPLOYMENT (
        ID number(19,0) not null,
        FECHAINICIO timestamp,
        FECHAFIN timestamp,
        ESTADO varchar2(255 char),
        AGENTEID number(19,0) not null,
        CENTROSECTORID number(19,0) not null,
        CATEGORYID number(19,0) not null,
        PREVIOUS_EMPLOYMENT_ID number(19,0),
        OCCUPATIONAL_GROUP_ID number(19,0) not null,
        primary key (ID)
    );

    create table CREDITSENTRY (
        ID number(19,0) not null,
        CANTIDADCREDITOS number(10,0),
        EMPLOYMENTID number(19,0),
        CREDITSPERIODID number(19,0) not null,
        CREDITS_ENTRY_TYPE varchar2(255 char) not null,
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

    alter table EMPLOYMENT 
        add constraint fk_Employment_CentroSector 
        foreign key (CENTROSECTORID) 
        references CENTROSECTOR;

    alter table EMPLOYMENT 
        add constraint fk_Employment_Person 
        foreign key (PERSONID) 
        references PERSON;

    alter table EMPLOYMENT 
        add constraint fk_Employment_Category 
        foreign key (CATEGORYID) 
        references CATEGORY;

    alter table EMPLOYMENT 
        add constraint fk_Employment_OccupationalGroup 
        foreign key (OCCUPATIONAL_GROUP_ID) 
        references OCCUPATIONAL_GROUP;

    alter table EMPLOYMENT 
        add constraint fk_Employment_Previous_Employment 
        foreign key (PREVIOUS_EMPLOYMENT_ID) 
        references EMPLOYMENT;

    alter table CREDITSENTRY 
        add constraint fk_CreditsEntry_Employment 
        foreign key (EMPLOYMENTID) 
        references EMPLOYMENT;

    alter table CREDITSENTRY 
        add constraint fk_CreditsEntry_CreditsPeriod 
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

    create sequence PERSON_SEQ;

    create sequence CATEGORY_SEQ;

    create sequence CENTROSECTOR_SEQ;

    create sequence CREDITSPERIOD_SEQ;

    create sequence EMPLOYMENT_SEQ;

    create sequence CREDITSENTRY_SEQ;

    create sequence OCCUPATIONAL_GROUP_SEQ;

    create sequence REPARTICION_SEQ;
