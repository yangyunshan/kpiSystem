CREATE TABLE USER(tid VARCHAR(255) NOT NULL PRIMARY KEY, username VARCHAR(255) NOT NULL, password VARCHAR(255), sex VARCHAR(10), phone VARCHAR(50), email VARCHAR(255), role INT, status INT);

CREATE TABLE FILE(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), path VARCHAR(255), itemid VARCHAR(255), tid VARCHAR(255));

CREATE TABLE ITEM(itemid VARCHAR(255), itemname VARCHAR(255), deptno INT, participantno INT, `rank` INT, count INT, description VARCHAR(255), score FLOAT, tid VARCHAR(255));