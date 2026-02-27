/*
delete from account;
delete from credit_institute;
*/

--select * from credit_institute
insert into credit_institute (id, name, bic) values (1, 'Volksbank Wendeburg', 'GENODEF1WBU');

--select * from account
insert into account (id, name, credit_institute_id, identifier, import_type) values (1, 'Giro-Konto', 1, 'GIROVOBA', 'CSV');
insert into account (id, name, credit_institute_id, identifier, import_type) values (2, 'Teilhabe', 1, 'THVOBA', 'CSV');

------------------------------------------------------------------------------------------
--- purpose_category
------------------------------------------------------------------------------------------

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