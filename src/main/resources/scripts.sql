select * from students where age > 10 and age < 20;

select name from students;

select * from students where lower(name) like lower('O%') or lower(name) like lower('%o%') or lower(name) like lower('%o');

select * from students where age < id;

select * from students order by age;