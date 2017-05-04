/*
Tables to be dropped must be listed in a logical order based on dependency.
UserFile and UserPhoto depend on User. Therefore, they must be dropped before User.
*/
DROP TABLE IF EXISTS UserFile;
DROP TABLE IF EXISTS UserPhoto;
DROP TABLE IF EXISTS AnsweredQuestion;
DROP TABLE IF EXISTS Score;
DROP TABLE IF EXISTS Question;
DROP TABLE IF EXISTS Test;
DROP TABLE IF EXISTS User;


/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (32) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    middle_name VARCHAR (32),
    last_name VARCHAR (32) NOT NULL,
    address1 VARCHAR (128) NOT NULL,
    address2 VARCHAR (128),
    city VARCHAR (64) NOT NULL,
    state VARCHAR (2) NOT NULL,
    zipcode VARCHAR (10) NOT NULL, /* e.g., 24060-1804 */
    security_question INT NOT NULL, /* Refers to the number of the selected security question */
    security_answer VARCHAR (128) NOT NULL,
    email VARCHAR (128) NOT NULL,      
    usertype ENUM('student', 'teacher') NOT NULL,
    course_grade FLOAT,
    PRIMARY KEY (id)
);

/* The UserPhoto table contains attributes of interest of a user's photo. */
CREATE TABLE UserPhoto
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Test
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       test_name VARCHAR (32) NOT NULL,
       num_questions INT UNSIGNED NOT NULL,
       total_points INT UNSIGNED NOT NULL,
       user_id INT UNSIGNED NOT NULL,
       due_date DATETIME,
       open BOOLEAN NOT NULL,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Question
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       question TEXT NOT NULL,
       summary VARCHAR(128) NOT NULL,
       question_type ENUM('multiple', 'short', 'free_response', 'true_false') NOT NULL, 
       choices VARCHAR(2048),
       correct_answer VARCHAR(2048),
       media_path VARCHAR(2048),
       points INT UNSIGNED,
       test_id INT UNSIGNED NOT NULL,
       FOREIGN KEY (test_id) REFERENCES Test(id) ON DELETE CASCADE
);

CREATE TABLE AnsweredQuestion
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       answer TEXT NOT NULL,
       points INT,
       comment VARCHAR(2048),
       user_id INT UNSIGNED NOT NULL,
       question_id INT UNSIGNED NOT NULL,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
       FOREIGN KEY (question_id) REFERENCES Question(id) ON DELETE CASCADE
);

CREATE TABLE Score
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       score INT UNSIGNED,
       user_id INT UNSIGNED NOT NULL,
       test_id INT UNSIGNED NOT NULL,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
       FOREIGN KEY (test_id) REFERENCES Test(id) ON DELETE CASCADE
);
