/* START */

/* A minimalist version of our schema */

begin
   execute immediate 'drop table "ORDER" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "CUSTOMER" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "CUSTOMERDETAIL" cascade constraints';
exception
   when others then null;
end; 
/
begin
   execute immediate 'drop table "EMPLOYEE" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "MANAGER" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "OFFICE" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "OFFICE_HAS_MANAGER" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "ORDERDETAIL" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "PAYMENT" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "PRODUCT" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "PRODUCTLINE" cascade constraints';
exception
   when others then null;
end;
/
begin
   execute immediate 'drop table "SALE" cascade constraints';
exception
   when others then null;
end;
/
    
    create table "ORDER" (
       order_id number(19,0) generated by default on null as identity,
        comments clob default null,
        order_date date not null,
        required_date date not null,
        shipped_date date,
        status varchar2(50 char) not null,
        customer_number number(19,0),
        primary key (order_id)
    );
    
    create table customer (
       customer_number number(19,0) not null,
        contact_first_name varchar2(50 char) not null,
        contact_last_name varchar2(50 char) not null,
        credit_limit float,
        customer_name varchar2(50 char) not null,
        phone varchar2(50 char) not null,
        sales_rep_employee_number number(19,0),
        primary key (customer_number)
    );
    
    create table customerdetail (
       customer_number number(19,0) not null,
        address_line_first varchar2(50 char) not null,
        address_line_second varchar2(50 char),
        city varchar2(50 char) not null,
        country varchar2(50 char) not null,
        postal_code varchar2(15 char),
        state varchar2(50 char),
        primary key (customer_number)
    );
    
    create table employee (
       employee_number number(19,0) not null,
        email varchar2(100 char) not null,
        extension varchar2(10 char) not null,
        first_name varchar2(50 char) not null,
        job_title varchar2(50 char) not null,
        last_name varchar2(50 char) not null,
        salary number(10,0) not null,
        office_code varchar2(10 char),
        reports_to number(19,0),
        primary key (employee_number)
    );
    
    create table manager (
       manager_id number(19,0) generated by default on null as identity,
        manager_name varchar2(50 char) not null,
        primary key (manager_id)
    );
    
    create table office (
       office_code varchar2(10 char) not null,
        address_line_first varchar2(50 char) not null,
        address_line_second varchar2(50 char),
        city varchar2(50 char) not null,
        country varchar2(50 char) not null,
        phone varchar2(50 char) not null,
        postal_code varchar2(15 char) not null,
        state varchar2(50 char),
        territory varchar2(10 char) not null,
        primary key (office_code)
    );
    
    create table office_has_manager (
       offices_office_code varchar2(10 char) not null,
        managers_manager_id number(19,0) not null,
        primary key (offices_office_code, managers_manager_id)
    );
    
    create table orderdetail (
       order_id number(19,0) not null,
        product_id number(19,0) not null,
        order_line_number number(5,0) not null,
        price_each float not null,
        quantity_ordered number(10,0) not null,
        primary key (order_id, product_id)
    );
    
    create table payment (
       check_number varchar2(50 char) not null,
        customer_number number(19,0) not null,
        caching_date date,
        invoice_amount float not null,
        payment_date date not null,
        primary key (check_number, customer_number)
    );
    
    create table product (
       product_id number(19,0) generated by default on null as identity,
        buy_price float not null,
        msrp float not null,
        product_description clob not null,
        product_name varchar2(70 char) not null,
        product_scale varchar2(10 char) not null,
        product_vendor varchar2(50 char) not null,
        quantity_in_stock number(5,0) not null,
        product_line varchar2(50 char),
        primary key (product_id)
    );
    
    create table productline (
       product_line varchar2(50 char) not null,
        html_description clob,
        image blob,
        text_description varchar2(4000 char),
        primary key (product_line)
    );
    
    create table sale (
       sale_id number(19,0) generated by default on null as identity,
        fiscal_year number(10,0) not null,
        sale float not null,
        employee_number number(19,0),
        primary key (sale_id)
    );

    alter table "ORDER" 
       add constraint FKro9uh5sg5ig4ei75p2h20lbby 
       foreign key (customer_number) 
       references customer;
    
    alter table customer 
       add constraint FKfwit3pchrvpph3tvj4tufuiny 
       foreign key (sales_rep_employee_number) 
       references employee;
    
    alter table customerdetail 
       add constraint FKpincwfvtd0nvcjwwy3d8kj5lb 
       foreign key (customer_number) 
       references customer;
    
    alter table employee 
       add constraint FKnxm4u35m7qqbnqbvtb8bj2tk9 
       foreign key (office_code) 
       references office;
    
    alter table employee 
       add constraint FKghecv11ypswk5w7mmcof2dscg 
       foreign key (reports_to) 
       references employee;
    
    alter table office_has_manager 
       add constraint FK628qu6nufx7jwjohq6uvc1em1 
       foreign key (managers_manager_id) 
       references manager;
    
    alter table office_has_manager 
       add constraint FKhbn3wp02ixbhw84c3vsm7x5cs 
       foreign key (offices_office_code) 
       references office;
    
    alter table orderdetail 
       add constraint FK3qd9bud6u1ggm33opobeopnea 
       foreign key (order_id) 
       references "ORDER";
    
    alter table orderdetail 
       add constraint FKdubukg3j0fymgci0idnd72k0 
       foreign key (product_id) 
       references product;
    
    alter table payment 
       add constraint FK4yvong4nch792htadwtaex6dr 
       foreign key (customer_number) 
       references customer;
    
    alter table product 
       add constraint FK71ytjxwdqw3950xadqx4e6dj3 
       foreign key (product_line) 
       references productline;
    
    alter table sale 
       add constraint FKgph5i8g999x8104dkjfysyw7h 
       foreign key (employee_number) 
       references employee;

/* END */

/*
*********************************************************************
http://www.mysqltutorial.org
*********************************************************************
Name: MySQL Sample Database classicmodels
Link: http://www.mysqltutorial.org/mysql-sample-database.aspx
*********************************************************************

This is a modified version of the original schema for MySQL
*/