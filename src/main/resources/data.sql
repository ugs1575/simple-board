insert into member(deleted, name, created_date, last_modified_date)
values (false, '우경서', now(), now()),
       (false, '김경서', now(), now()),
       (false, '서경서', now(), now());

insert into post(title, content, member_id, registered_date_time, created_date, last_modified_date)
values ('제목1', '내용1', 1, now(), now(), now()),
       ('제목2', '내용2', 2, now(), now(), now()),
       ('제목3', '내용3', 2, now(), now(), now());