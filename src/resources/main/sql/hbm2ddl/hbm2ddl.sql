
    create table CATEGORY (
        ID number(19,0) not null,
        CODE varchar2(255 char),
        primary key (ID)
    );

    create table CREDITSENTRY (
        ID number(19,0) not null,
        NUMBER_OF_CREDITS number(10,0),
        EMPLOYMENTID number(19,0) not null,
        CREDITSPERIODID number(19,0) not null,
        CREDITS_ENTRY_TYPE varchar2(255 char) not null,
        GRANTED_STATUS varchar2(255 char) not null,
        NOTES varchar2(255 char),
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

    create table DEPARTMENT (
        ID number(19,0) not null,
        CODE varchar2(255 char),
        NAME varchar2(255 char),
        PARENT_ID number(19,0),
        primary key (ID)
    );

    create table DEPARTMENT_CREDITSENTRY (
        ID number(19,0) not null,
        NUMBER_OF_CREDITS number(10,0) not null,
        DEPARTMENT_ID number(19,0) not null,
        CREATED_BY_DEPARTMENT_ID number(19,0) not null,
        CREDITSPERIOD_ID number(19,0) not null,
        DEPARTMENT_CREDITS_ENTRY_TYPE varchar2(255 char) not null,
        CREDITS_ENTRY_TRANSACTION_TYPE varchar2(255 char) not null,
        GRANTED_STATUS varchar2(255 char) not null,
        NOTES varchar2(255 char),
        primary key (ID)
    );

    create table EMPLOYMENT (
        ID number(19,0) not null,
        STARTDATE timestamp,
        ENDDATE timestamp,
        STATUS varchar2(255 char),
        PERSONID number(19,0) not null,
        SUBDEPARTMENTID number(19,0) not null,
        CATEGORYID number(19,0) not null,
        PREVIOUS_EMPLOYMENT_ID number(19,0),
        OCCUPATIONAL_GROUP_ID number(19,0) not null,
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

    create table PERSON (
        ID number(19,0) not null,
        APELLIDONOMBRE varchar2(255 char),
        CUIL varchar2(255 char),
        primary key (ID)
    );

    create table SUBDEPARTMENT (
        ID number(19,0) not null,
        CODIGOCENTRO varchar2(255 char),
        CODIGOSECTOR varchar2(255 char),
        NOMBRECENTRO varchar2(255 char),
        NOMBRESECTOR varchar2(255 char),
        DEPARTMENTID number(19,0),
        primary key (ID)
    );

    alter table CREDITSENTRY 
        add constraint fk_MovimientoCredito_CreditsPeriod 
        foreign key (CREDITSPERIODID) 
        references CREDITSPERIOD;

    alter table CREDITSENTRY 
        add constraint fk_CreditsEntry_Employment 
        foreign key (EMPLOYMENTID) 
        references EMPLOYMENT;

    alter table CREDITSPERIOD 
        add constraint fk_CRPERIOD_CRPERIOD_PREVIOUS 
        foreign key (PREVIOUS_CREDITSPERIOD_ID) 
        references CREDITSPERIOD;

    alter table DEPARTMENT 
        add constraint fk_DepartmentParent 
        foreign key (PARENT_ID) 
        references DEPARTMENT;

    alter table DEPARTMENT_CREDITSENTRY 
        add constraint fk_DepCredEntry_CredPeriod 
        foreign key (CREDITSPERIOD_ID) 
        references CREDITSPERIOD;

    alter table DEPARTMENT_CREDITSENTRY 
        add constraint fk_DepCredEntry_Dep 
        foreign key (DEPARTMENT_ID) 
        references DEPARTMENT;

    alter table DEPARTMENT_CREDITSENTRY 
        add constraint fk_DepCredEntryCreatedByDep 
        foreign key (CREATED_BY_DEPARTMENT_ID) 
        references DEPARTMENT;

    alter table EMPLOYMENT 
        add constraint fk_Employment_Category 
        foreign key (CATEGORYID) 
        references CATEGORY;

    alter table EMPLOYMENT 
        add constraint fk_Employment_Person 
        foreign key (PERSONID) 
        references PERSON;

    alter table EMPLOYMENT 
        add constraint fk_Employment_SubDepartment 
        foreign key (SUBDEPARTMENTID) 
        references SUBDEPARTMENT;

    alter table EMPLOYMENT 
        add constraint fk_Employment_Previous_Employment 
        foreign key (PREVIOUS_EMPLOYMENT_ID) 
        references EMPLOYMENT;

    alter table EMPLOYMENT 
        add constraint fk_Employment_OccupationalGroup 
        foreign key (OCCUPATIONAL_GROUP_ID) 
        references OCCUPATIONAL_GROUP;

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

    alter table SUBDEPARTMENT 
        add constraint fk_DepartmentSubDepartment 
        foreign key (DEPARTMENTID) 
        references DEPARTMENT;

    create sequence CATEGORY_SEQ;

    create sequence CREDITSENTRY_SEQ;

    create sequence CREDITSPERIOD_SEQ;

    create sequence DEPARTMENT_CREDITSENTRY_SEQ;

    create sequence DEPARTMENT_SEQ;

    create sequence EMPLOYMENT_SEQ;

    create sequence OCCUPATIONAL_GROUP_SEQ;

    create sequence PERSON_SEQ;

    create sequence SUBDEPARTMENT_SEQ;
