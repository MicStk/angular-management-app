-- insert initial test data
-- the IDs are hardcoded to enable references between further test data
-- negative IDs are used to not interfere with user-entered data and allow clean deletion of test data

DELETE FROM horse where id < 0;

DELETE FROM owner where id < 0;

INSERT INTO owner (id, first_name, last_name, email)
VALUES (-1, 'Max', 'Mustermann', 'max@email.com'),
(-2, 'Thomas', 'Reiter', 'thomas@email.com'),
(-3, 'Maria', 'Musterfrau', 'maria.musterfrau@email.com'),
(-4, 'Anton', 'Mann', 'anton2@mail.at'),
(-5, 'Anna', 'Schneider', null),
(-6, 'Annette', 'Schuchmacher', 'user@mail.com'),
(-7, 'Mario', 'Muller', null),
(-8, 'Anton', 'Becker', 'anton.becker@mail.at'),
(-9, 'Anne', 'Frau', 'anne@mail.at'),
(-10, 'Aaron', 'Klein', null)
;

INSERT INTO horse (id, name, description, date_of_birth, sex, owner_id, mother_id, father_id)
VALUES (-1, 'Wendy', 'The famous one!', '2012-12-12', 'FEMALE', null, null, null),
(-2, 'Michael', 'A fast horse', '2015-01-06', 'MALE', -1, -1, null),
(-3, 'Rose', 'Beautiful horse', '2010-10-12', 'FEMALE', -1, null, null),
(-4, 'Wendy', 'A big horse', '2018-12-12', 'FEMALE', -10, -3, null),
(-5, 'Spirit', 'An old horse', '2000-01-01', 'MALE', -10, null, null),
(-6, 'Blitz', 'As fast as lightning!', '2022-05-03', 'MALE', -5, -2, -5),
(-7, 'Destiny', '5-time competition winner!', '2015-07-07', 'FEMALE', -9, -3, -5),
(-8, 'Tommy', 'The small one!', '2017-01-12', 'MALE', null, -7, -2),
(-9, 'Werner', null, '2022-09-10', 'MALE', -4, -4, -8),
(-10, 'Tanya', null, '2012-12-12', 'FEMALE', null, -3, -2)
;
