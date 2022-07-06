CREATE TABLE cars
(
    id bigserial NOT NULL,
    brand varchar(65) NOT NULL,
    model varchar(65) NOT NULL,
    cost NUMERIC (19,2),
    primary key (id)
);

CREATE TABLE humans
(
    id bigserial NOT NULL,
    name varchar(25) NOT NULL,
    age smallint CHECK ( age > 18 ) DEFAULT 18,
    can_drive BOOLEAN DEFAULT true,
    car_id int8 NOT NULL,
    primary key (id),
    CONSTRAINT car_id_fk FOREIGN KEY (car_id) REFERENCES cars (id)
);