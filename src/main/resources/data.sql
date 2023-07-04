insert into member(member_id, deleted, name, created_date, last_modified_date)
values (1, false, '우경서', now(), now()),
       (2, false, '김경서', now(), now()),
       (3, false, '서경서', now(), now());

insert into post(title, content, member_id, created_date, last_modified_date)
values ('제목1', '내용1', 1, now(), now()),
       ('제목2', '내용2', 2, now(), now()),
       ('제목3', '내용3', 2, now(), now());