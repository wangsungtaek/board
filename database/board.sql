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

CREATE TABLE boardfile(
	NO NUMBER PRIMARY KEY,
	fname VARCHAR2(200),
	pathname VARCHAR2(500),
	content VARCHAR2(2000),
	credte DATE
);
ALTER TABLE boardfile DROP PRIMARY KEY;
SELECT * FROM boardfile;

-- content, fname
CREATE TABLE fileexp(
	content varchar2(200),
	fname varchar2(100)
);
SELECT * FROM fileexp;
SELECT * FROM boardfile;
SELECT * FROM board;

SELECT * FROM (
	SELECT rownum rn, LEVEL, a.*
	  FROM board a
	START WITH refno = 0
	CONNECT BY PRIOR NO = refno
	ORDER siblings BY NO DESC
)
WHERE rn BETWEEN 1 AND 5;