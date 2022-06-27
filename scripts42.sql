ALTER TABLE students ADD CONSTRAINT age_constraint CHECK (age > 15);

ALTER TABLE students ALTER COLUMN name SET NOT NULL;

ALTER TABLE students ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE faculties ADD CONSTRAINT color_name_unique UNIQUE (color, name);

ALTER TABLE students ALTER COLUMN age SET DEFAULT 20;