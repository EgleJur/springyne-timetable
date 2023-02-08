CREATE TABLE ROOM (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(255) NOT NULL,
    BUILDING VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255),
    DELETED BOOLEAN,
    LAST_MODIFIED_DATE TIMESTAMP,

    CONSTRAINT ROOM_PK PRIMARY KEY (ID)
);
