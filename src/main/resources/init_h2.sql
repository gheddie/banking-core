CREATE SEQUENCE "BOOKING_ID_SEQ" 
MINVALUE 1 
MAXVALUE 999999999 
INCREMENT BY 1 
START WITH 1234
NOCACHE 
NOCYCLE;

CREATE TABLE credit_institute (
    id bigint,
    bic varchar(32) not null,
    name varchar(255) not null,
    import_type varchar(32) not null,
	primary key (id),
	CONSTRAINT BIC_UNIQUE UNIQUE(bic)
);

CREATE TABLE account (
    id bigint,
    name varchar(255) not null,
	identifier varchar(32) not null,
    credit_institute_id bigint not null,
	primary key (id),
	FOREIGN KEY (credit_institute_id) REFERENCES credit_institute(id)
);

CREATE TABLE purpose_category (
    id bigint,
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
    id bigint,
    trading_key varchar(255) not null,
    purpose_category_id bigint null,
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
    id bigint,
    text varchar(255) not null,
    custom_remark varchar(255) null,
    account_id bigint not null,
    amount DECIMAL(10, 2) not null,
    amount_after_booking DECIMAL(10, 2) null,
    booking_date DATE not null,
    purpose_of_use varchar(1024) not null,
    trading_partner_id bigint not null,
    purpose_category_id bigint null,
	primary key (id),
	FOREIGN KEY (account_id) REFERENCES account(id),
	FOREIGN KEY (trading_partner_id) REFERENCES trading_partner(id),
	FOREIGN KEY (purpose_category_id) REFERENCES purpose_category(id)
);

CREATE TABLE trading_partner_booking_history (
	id bigint,
	trading_partner_id bigint not null,
	booking_id bigint not null,
	primary key (id),
	FOREIGN KEY (trading_partner_id) REFERENCES trading_partner(id),
	FOREIGN KEY (booking_id) REFERENCES booking(id)
);

CREATE TABLE booking_import (
	id bigint,
	file_name varchar(255) not null,
	import_date date not null,
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
    id bigint,
    planning_month integer not null,
    planning_year integer not null,
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
left join booking_import bi on (bi.id = bii.booking_import_id);

insert into recurring_position (id, incoming, recurring_interval) values (1, 1, 'MONTHLY');
insert into recurring_position (id, incoming, recurring_interval) values (2, 0, 'MONTHLY');

insert into purpose_category (id, purpose_key) values (1,'Altersvorsorge');
insert into purpose_category (id, purpose_key) values (2,'Apotheke');
insert into purpose_category (id, purpose_key) values (3,'Band');
insert into purpose_category (id, purpose_key) values (4,'Einkauf');
insert into purpose_category (id, purpose_key) values (5,'Eltern');
insert into purpose_category (id, purpose_key) values (6,'Essen gehen');
insert into purpose_category (id, purpose_key) values (7,'Fahrrad');
insert into purpose_category (id, purpose_key) values (8,'Fast Food');
insert into purpose_category (id, purpose_key) values (9,'Fitness');
insert into purpose_category (id, purpose_key) values (10,'Gehalt');
insert into purpose_category (id, purpose_key) values (11,'Google Play');
insert into purpose_category (id, purpose_key) values (12,'Kinder');
insert into purpose_category (id, purpose_key) values (13,'Klamotten');
insert into purpose_category (id, purpose_key) values (14,'Miete');
insert into purpose_category (id, purpose_key) values (15,'Parken');
insert into purpose_category (id, purpose_key) values (16,'Paypal');
insert into purpose_category (id, purpose_key) values (17,'Rauchen');
insert into purpose_category (id, purpose_key) values (18,'Rundfunk');
insert into purpose_category (id, purpose_key) values (19,'Sonstiges');
insert into purpose_category (id, purpose_key) values (20,'Sparkasse');
insert into purpose_category (id, purpose_key) values (21,'Spenden');
insert into purpose_category (id, purpose_key) values (22,'Tanken');
insert into purpose_category (id, purpose_key) values (23,'Technik');
insert into purpose_category (id, purpose_key) values (24,'Telefonie/Internet');
insert into purpose_category (id, purpose_key) values (25,'Unterhalt');
insert into purpose_category (id, purpose_key) values (26,'Versicherung');
insert into purpose_category (id, purpose_key) values (27,'Urlaub');