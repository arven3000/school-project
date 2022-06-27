-- liquibase formatted sql

-- changeSet nakremneva:1
CREATE INDEX students_name_index ON students (name);

-- changeSet nakremneva:2
CREATE INDEX faculties_name_color_index ON faculties (name, color);
