CREATE TABLE board (
	NO NUMBER PRIMARY KEY,
	refno NUMBER,
	subject VARCHAR2(200),
	content VARCHAR2(2000),
	writer VARCHAR2(100),
	readcnt NUMBER,
	regdte DATE,
	uptdte DATE
);
CREATE SEQUENCE board_seq
	START WITH 1
	MINVALUE 1
	INCREMENT BY 1
	MAXVALUE 999999;

INSERT INTO board VALUES(board_seq.nextval, 0, '첫번째 글', '내용',
	'홍길동', 0, sysdate, sysdate);

SELECT * FROM board;

