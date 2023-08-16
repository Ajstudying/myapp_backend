use mypet;

select * from post;

select * from post_comment;

select * from user;

select * from profile;

truncate table profile;

truncate table post_comment;

truncate table post;

-- 외래키 체크 False
set FOREIGN_KEY_CHECKS = 0;

-- 작업
truncate table user;

-- 외래키 체크 True
set FOREIGN_KEY_CHECKS = 1;


INSERT INTO post(no, content, created_time, image, nickname, title) value(1, "Lala", 2, "", "Lola","Lily");