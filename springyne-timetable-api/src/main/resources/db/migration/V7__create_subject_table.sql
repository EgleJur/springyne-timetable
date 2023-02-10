CREATE TABLE IF NOT EXISTS SUBJECT_TABLE (
	ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
	NAME VARCHAR(255),
	DESCRIPTION VARCHAR(255),
	LAST_UPDATED TIMESTAMP,
	DELETED BOOLEAN,
	CONSTRAINT SUBJECT_PK PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS SUBJECT_AND_MODULES (
	SUBJECT_ID BIGINT ,
	MODULE_ID BIGINT
);

CREATE TABLE IF NOT EXISTS SUBJECTS_IN_ROOMS (
	SUBJECT_ID BIGINT ,
	ROOM_ID BIGINT
);


