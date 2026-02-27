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
drop table booking_import_item;
drop table booking;
drop table booking_import;
drop table standing_order;
drop table account;
drop table credit_institute;
drop table trading_partner;
drop table purpose_category;
*/

CREATE TABLE credit_institute (
    id int,
    bic varchar(32) not null,
    name varchar(255) not null,
	primary key (id),
	CONSTRAINT BIC_UNIQUE UNIQUE(bic)
);

CREATE TABLE account (
    id int,
    name varchar(255) not null,
	identifier varchar(32) not null,
    credit_institute_id int not null,
    import_type varchar(32) not null,
    latest_booking_date datetime2(6) null,
	primary key (id),
	FOREIGN KEY (credit_institute_id) REFERENCES credit_institute(id)
);

CREATE TABLE purpose_category (
    id int,
    purpose_key varchar(255) not null,
	primary key (id),
	CONSTRAINT purpose_key_unique UNIQUE(purpose_key)
);

CREATE TABLE trading_partner (
    id int,
    trading_key varchar(255) not null,
    purpose_category_id int null,
	primary key (id),
	FOREIGN KEY (purpose_category_id) REFERENCES purpose_category(id),	
);

CREATE TABLE standing_order (
    id int,
    description varchar(255) not null,
    trading_partner_id int null,
    account_id int not null,
	primary key (id),
	FOREIGN KEY (trading_partner_id) REFERENCES trading_partner(id),	
	FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE booking (
    id int,
    text varchar(255) not null,
    custom_remark varchar(255) null,
    account_id int not null,
    amount DECIMAL(10, 2) not null,
    amount_after_booking DECIMAL(10, 2) not null,
    booking_date datetime2(6) not null,
    purpose_of_use varchar(1024) not null,
    trading_partner_id int not null,
    purpose_category_id int null,
	primary key (id),
	FOREIGN KEY (account_id) REFERENCES account(id),
	FOREIGN KEY (trading_partner_id) REFERENCES trading_partner(id),
	FOREIGN KEY (purpose_category_id) REFERENCES purpose_category(id)
);

CREATE TABLE booking_import (
	id int,
	file_name varchar(255) not null,
	import_date datetime2(6) not null,
	account_id int not null,
	primary key (id),
	FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE booking_import_item (
	id int,
	booking_id int not null,
	booking_import_id int not null,
	item_pos int not null,
	primary key (id),
	FOREIGN KEY (booking_id) REFERENCES booking(id),
	FOREIGN KEY (booking_import_id) REFERENCES booking_import(id)
);

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
a.id AS account_id,
tp.id AS trading_partner_id,
tp.trading_key AS trading_partner_key,
bi.file_name
from booking b 
inner join account a on (a.id = b.account_id)
inner join trading_partner tp on (tp.id = b.trading_partner_id )
left join purpose_category pc on (pc.id = tp.purpose_category_id)
left join booking_import_item bii on (bii.booking_id = b.id)
left join booking_import bi on (bi.id = bii.booking_import_id)