/*
delete from booking;
delete from account;
delete from credit_institute;
*/

--select * from credit_institute
insert into credit_institute (id, name, bic) values (1, 'Volksbank Wendeburg', 'GENODEF1WBU');

--select * from account
insert into account (id, name, credit_institute_id, identifier, import_type) values (1, 'Giro-Konto', 1, 'GIROVOBA', 'CSV');
insert into account (id, name, credit_institute_id, identifier, import_type) values (2, 'Teilhabe', 1, 'THVOBA', 'CSV');

--select * from booking
insert into booking (id, text, account_id) values (1, 'Booking 1', 1);
insert into booking (id, text, account_id) values (2, 'Booking 2', 1);

--select * from purpose_category
insert into purpose_category (id, purpose_key) values (1, 'Einkauf');
insert into purpose_category (id, purpose_key) values (2, 'Miete');
insert into purpose_category (id, purpose_key) values (3, 'Altersvorsorge');