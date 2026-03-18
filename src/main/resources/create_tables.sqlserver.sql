---------------------------------------------------------------------------------------------------------------
--- Sequenz(en)
---------------------------------------------------------------------------------------------------------------

CREATE SEQUENCE booking_id_seq
    START WITH 1
    INCREMENT BY 1;
GO

---------------------------------------------------------------------------------------------------------------
--- Tabellen
---------------------------------------------------------------------------------------------------------------

/*
--tables
drop table booking_import_item;
drop table booking;
drop table booking_import;
drop table standing_order;
drop table account;
drop table credit_institute;
drop table trading_partner;
drop table purpose_category;
drop table budget_planning_item;
drop table budget_planning;
drop table recurring_position;
--views
drop view booking_view;
*/

CREATE TABLE credit_institute (
    id BIGINT,
    bic varchar(32) not null,
    name varchar(255) not null,
	import_type varchar(32) not null,
	primary key (id),
	CONSTRAINT BIC_UNIQUE UNIQUE(bic)
);

CREATE TABLE account (
    id BIGINT,
    name varchar(255) not null,
	identifier varchar(32) not null,
    credit_institute_id BIGINT not null,    
	primary key (id),
	FOREIGN KEY (credit_institute_id) REFERENCES credit_institute(id)
);

CREATE TABLE purpose_category (
    id BIGINT,
    purpose_key varchar(255) not null,
	primary key (id),
	CONSTRAINT purpose_key_unique UNIQUE(purpose_key)
);

CREATE TABLE recurring_position (
    id BIGINT,
    incoming bit not null,
	recurring_interval varchar(32) not null,
	primary key (id)
);

CREATE TABLE trading_partner (
    id BIGINT,
    trading_key varchar(255) not null,
    purpose_category_id BIGINT null,
    recurring_position_id BIGINT null,
    parent_trading_partner_id BIGINT null,
	primary key (id),
	FOREIGN KEY (purpose_category_id) REFERENCES purpose_category(id),
	FOREIGN KEY (recurring_position_id) REFERENCES recurring_position(id),
	FOREIGN KEY (parent_trading_partner_id) REFERENCES trading_partner(id)
);

CREATE TABLE standing_order (
    id bigint,
    description varchar(255) not null,
    trading_partner_id bigint null,
    account_id bigint not null,
	primary key (id),
	FOREIGN KEY (trading_partner_id) REFERENCES trading_partner(id),	
	FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE booking (
    id BIGINT,
    text varchar(255) not null,
    custom_remark varchar(255) null,
    account_id BIGINT not null,
    amount DECIMAL(10, 2) not null,
    amount_after_booking DECIMAL(10, 2) null,
    booking_date DATE not null,
    purpose_of_use varchar(1024) not null,
    trading_partner_id BIGINT not null,
    purpose_category_id BIGINT null,
	primary key (id),
	FOREIGN KEY (account_id) REFERENCES account(id),
	FOREIGN KEY (trading_partner_id) REFERENCES trading_partner(id),
	FOREIGN KEY (purpose_category_id) REFERENCES purpose_category(id)
);

CREATE TABLE booking_import (
	id bigint,
	file_name varchar(255) not null,
	import_date DATE not null,
	account_id bigint not null,
	primary key (id),
	FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE booking_import_item (
	id bigint,
	booking_id bigint not null,
	booking_import_id bigint not null,
	item_pos int not null,
	primary key (id),
	FOREIGN KEY (booking_id) REFERENCES booking(id),
	FOREIGN KEY (booking_import_id) REFERENCES booking_import(id)
);

CREATE TABLE budget_planning (
    id BIGINT,
    planning_month int not null,
    planning_year int not null,
	primary key (id)
);

CREATE TABLE budget_planning_item (
    id BIGINT,
	amount DECIMAL(10, 2) not null,
	budget_planning_id BIGINT not null,
	purpose_category_id BIGINT not null,
	primary key (id),
	FOREIGN KEY (budget_planning_id) REFERENCES budget_planning(id)
);

---------------------------------------------------------------------------------------------------------------
--- Unique Constraints
---------------------------------------------------------------------------------------------------------------

--budget_planning
ALTER TABLE budget_planning ADD CONSTRAINT uc_month_year UNIQUE (planning_month, planning_year);

--budget_planning_item
ALTER TABLE budget_planning_item ADD CONSTRAINT uc_budgetplanning_purposecategory UNIQUE (budget_planning_id, purpose_category_id);  	

---------------------------------------------------------------------------------------------------------------
--- Views
---------------------------------------------------------------------------------------------------------------

--select * from booking_view
--drop view booking_view
CREATE VIEW booking_view AS
select
b.id,
b.text,
b.amount,
b.amount_after_booking,
b.booking_date,
b.purpose_of_use,
b.custom_remark,
pc.purpose_key,
pc_booking.purpose_key as booking_purpose_key,
a.id AS account_id,
tp.id AS trading_partner_id,
tp.trading_key AS trading_partner_key,
bi.file_name
from booking b 
inner join account a on (a.id = b.account_id)
inner join trading_partner tp on (tp.id = b.trading_partner_id )
left join purpose_category pc on (pc.id = tp.purpose_category_id)
left join purpose_category pc_booking on (pc_booking.id = b.purpose_category_id)
left join booking_import_item bii on (bii.booking_id = b.id)
left join booking_import bi on (bi.id = bii.booking_import_id)