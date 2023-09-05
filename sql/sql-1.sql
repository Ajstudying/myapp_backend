use mypet;

select * from post;


select * from profile;
select * from schedule;

select * from board;
select * from board_comment;

select * from board_Comment where board_no order by id asc;
-- ALTER TABLE board MODIFY COLUMN comment_cnt INT NULL;

DELETE FROM user WHERE id = '4' AND userid = 'cookie';
DELETE FROM profile WHERE id = '18';

select * from user;
select * from likes;

select * from profile;
select * from schedule;




truncate table schedule;
truncate table likes;
truncate table profile;
truncate table board;
truncate table board_comment;

truncate table post;
truncate table schedule;
-- 외래키 체크 False
set FOREIGN_KEY_CHECKS = 0;

-- 작업
truncate table user;

-- 외래키 체크 True
set FOREIGN_KEY_CHECKS = 1;

select * from Post where no = 7;

INSERT INTO post(no, content, created_time, image, nickname, title) value(1, "Lala", 2, "", "Lola","Lily");

CREATE TABLE post (
    no BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    nickname VARCHAR(255) NOT NULL,
    image VARCHAR(52428800),
    created_time BIGINT,
    petname VARCHAR(255) NOT NULL,
    like_count BIGINT,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);