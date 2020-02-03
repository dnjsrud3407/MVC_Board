CREATE TABLE board (
	board_num INT PRIMARY KEY AUTO_INCREMENT,
	board_name VARCHAR(20) NOT NULL,
	board_pass VARCHAR(15) NOT NULL,
	board_subject VARCHAR(50) NOT NULL,
	board_content VARCHAR(2000) NOT NULL,
	board_file VARCHAR(50),
	board_re_ref INT NOT NULL,
	board_re_lev INT NOT NULL,
	board_re_seq INT NOT NULL,
	board_readcount INT DEFAULT 0,
	board_date DATE
);

SELECT * FROM board;

DELETE FROM board;
