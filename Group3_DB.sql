-- Class: ISTE-330
-- Date: 11/20/2023
-- Group: 3
-- Members: Angie Li, Ata Noor, Hritish Mahajan, Ivan Li, Kelly Wu, Tryder Kulbacki
-- Professor Habermas

-- Drop the database if it exists
DROP DATABASE IF EXISTS Group3_DB;

-- Create the database
CREATE DATABASE Group3_DB;

-- Use the Faculty_DB database
USE Group3_DB;
-- Create the Faculty tables
CREATE TABLE Faculty(
    username VARCHAR(7) NOT NULL, -- rit username format (abc1234)
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    password VARCHAR(45) NOT NULL,
    CONSTRAINT Faculty_pk PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Abstract(
    abstractID INT NOT NULL AUTO_INCREMENT,
    abstractTitle VARCHAR(100) NOT NULL,
    abstract VARCHAR(5000) NOT NULL,
    CONSTRAINT Abstract_pk PRIMARY KEY (abstractID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Interest(
    interest_id INT AUTO_INCREMENT,
    keyword VARCHAR(50) NOT NULL,
    CONSTRAINT Faculty_Interest_pk PRIMARY KEY (interest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Contact(
    username VARCHAR(7) NOT NULL,
    firstContact VARCHAR(12) NOT NULL, -- format like 000-000-0000
    email VARCHAR(35) NOT NULL,
    officeNumber VARCHAR(3),  -- format like 000
    buildingNumber VARCHAR(8),  -- format like xxx-0000
    officeHours VARCHAR(100) NOT NULL,
    alternativeNumber VARCHAR(12),
    CONSTRAINT Faculty_Contact_pk PRIMARY KEY (username),
    CONSTRAINT Faculty_Contact_Faculty_fk FOREIGN KEY (username) REFERENCES Faculty(username)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Interests(
    username VARCHAR(7) NOT NULL,
    interest_id INT NOT NULL,
    CONSTRAINT Faculty_Interests_pk PRIMARY KEY (username, interest_id),
    CONSTRAINT Faculty_Interests_Faculty_Interest_fk FOREIGN KEY (interest_id) REFERENCES Faculty_Interest(interest_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT Faculty_Interests_Faculty_fk FOREIGN KEY (username) REFERENCES Faculty(username)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Abstracts(
    username VARCHAR(7) NOT NULL,
    abstractID INT NOT NULL,
    CONSTRAINT Faculty_Abstract_pk PRIMARY KEY (username, abstractID),
    CONSTRAINT Faculty_Abstract_Abstract_fk FOREIGN KEY (abstractID) REFERENCES Abstract(abstractID)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT Faculty_Abstract_Faculty_fk FOREIGN KEY (username) REFERENCES Faculty(username)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create the Student Tables
CREATE TABLE Student (
    username VARCHAR(7) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    password VARCHAR(45) NOT NULL,
    CONSTRAINT student_pk PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student_Interest (
    interest_id INT AUTO_INCREMENT,
    keyword VARCHAR(50),
    CONSTRAINT student_interest_pk PRIMARY KEY (interest_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student_Interests (
    username VARCHAR(7),
    interest_id INT NOT NULL,
    CONSTRAINT student_interests_pk PRIMARY KEY (username, interest_id),
    CONSTRAINT student_fk FOREIGN KEY (username) REFERENCES Student(username)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT interest_fk FOREIGN KEY (interest_id) REFERENCES Student_Interest(interest_id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student_Contact (
    username VARCHAR(7),
    email VARCHAR(50),
    portfolio VARCHAR(50),
    CONSTRAINT student_contact_pk PRIMARY KEY (username),
    CONSTRAINT student_contact_fk FOREIGN KEY (username) REFERENCES Student(username)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create the Outside Organization Table
CREATE TABLE Outside_Organization (
    username VARCHAR(7) NOT NULL,
    name VARCHAR(50),
    interest VARCHAR(50),
    CONSTRAINT outside_org_pk PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Insert mock data for Faculty_Interest
INSERT INTO Faculty_Interest (keyword) VALUES
    ('Research'),
    ('Teaching'),
    ('MachineLearning'),
    ('DataScience'),
    ('ArtificialIntelligence'),
    ('Cybersecurity'),
    ('Networking'),
    ('DatabaseManagement');

-- Insert mock data for Student_Interest
INSERT INTO Student_Interest (keyword) VALUES
    ('Programming'),
    ('WebDevelopment'),
    ('MobileApps'),
    ('GraphicDesign'),
    ('Cybersecurity'),
    ('DataAnalysis'),
    ('Robotics'),
    ('AI');

-- Insert mock data for Faculty
INSERT INTO Faculty (username, firstName, lastName, password) VALUES
    ('fac001', 'John', 'Doe', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('fac002', 'Jane', 'Smith', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('fac003', 'Robert', 'Johnson', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('fac004', 'Brandon', 'White', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('fac005', 'Jessica', 'White', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('fac006', 'Sue', 'Smith', '40bd001563085fc35165329ea1ff5c5ecbdbbeef');

-- Insert mock data for Student
INSERT INTO Student (username, firstName, lastName, password) VALUES
    ('stu001', 'Alice', 'Johnson', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('stu002', 'Bob', 'Williams', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('stu003', 'Charlie', 'Davis', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('stu004', 'David', 'Miller', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('stu005', 'Eva', 'Jones', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('stu006', 'Charlotte', 'Davis', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('stu007', 'Erin', 'White', '40bd001563085fc35165329ea1ff5c5ecbdbbeef'),
    ('stu008', 'Sylvia', 'Jones', '40bd001563085fc35165329ea1ff5c5ecbdbbeef');

-- Insert mock data for Abstract
INSERT INTO Abstract (abstractTitle, abstract) VALUES
    ('Research on Machine Learning', 'This research focuses on exploring various machine learning algorithms and their applications in real-world scenarios. The study investigates the effectiveness of different models and their impact on data-driven decision-making.'),
    ('Advancements in Cybersecurity', 'This abstract discusses the latest advancements in cybersecurity technologies and strategies. It covers topics such as threat detection, vulnerability analysis, and proactive measures to safeguard digital assets.'),
    ('Data Science for Business Analytics', 'The abstract delves into the role of data science in business analytics, emphasizing the extraction of actionable insights from large datasets. The study explores statistical methods, predictive modeling, and data visualization techniques.');

-- Insert mock data for Faculty_Contact
INSERT INTO Faculty_Contact (username, firstContact, email, officeNumber, buildingNumber, officeHours, alternativeNumber) VALUES
    ('fac001', '123-456-7890', 'john.doe@example.com', '101', 'LIB-1000', 'Mon-Fri, 9 am - 5 pm', '987-654-3210'),
    ('fac002', '234-567-8901', 'jane.smith@example.com', '202', 'ENG-2000', 'Tue-Thu, 10 am - 6 pm', '876-543-2109'),
    ('fac003', '345-678-9012', 'robert.johnson@example.com', '303', 'SCI-3000', 'Wed-Fri, 8 am - 4 pm', '765-432-1098'),
    ('fac004', '000-456-7890', 'bwhite@example.com', '404', 'GOL-1000', 'Mon-Fri, 11 am - 5 pm', '000-654-3210'),
    ('fac005', '111-567-8901', 'jwhite@example.com', '505', 'GOL-2000', 'Tue-Thu, 8 am - 6 pm', '111-543-2109'),
    ('fac006', '222-678-9012', 'ssmith@example.com', '606', 'GOL-3000', 'Wed-Fri, 3 am - 4 pm', '222-432-1098');

-- Insert mock data for Faculty_Interests
INSERT INTO Faculty_Interests (username, interest_id) VALUES
    ('fac001', 1),
    ('fac001', 3),
    ('fac002', 2),
    ('fac002', 4),
    ('fac003', 5),
    ('fac003', 7),
    ('fac004', 6),
    ('fac004', 7),
    ('fac005', 1),
    ('fac005', 2),
    ('fac006', 3),
    ('fac006', 4);

-- Insert mock data for Faculty_Abstracts
INSERT INTO Faculty_Abstracts (username, abstractID) VALUES
    ('fac001', 1),
    ('fac002', 2),
    ('fac003', 3),
    ('fac004', 1),
    ('fac005', 2),
    ('fac006', 3);

-- Insert mock data for Student_Interests
INSERT INTO Student_Interests (username, interest_id) VALUES
    ('stu001', 1),
    ('stu001', 2),
    ('stu002', 3),
    ('stu002', 4),
    ('stu003', 5),
    ('stu003', 6),
    ('stu004', 7),
    ('stu004', 8),
    ('stu005', 1),
    ('stu005', 3),
    ('stu006', 1),
    ('stu006', 6),
    ('stu007', 2),
    ('stu007', 4),
    ('stu008', 7),
    ('stu008', 8);

-- Insert mock data for Student_Contact
INSERT INTO Student_Contact (username, email, portfolio) VALUES
    ('stu001', 'alice.johnson@example.com', 'github.com/alice'),
    ('stu002', 'bob.williams@example.com', 'github.com/bob'),
    ('stu003', 'charlie.davis@example.com', 'github.com/charlie'),
    ('stu004', 'david.miller@example.com', 'github.com/david'),
    ('stu005', 'eva.jones@example.com', 'github.com/eva'),
    ('stu006', 'charlotte.davis@example.com', 'github.com/charlotte'),
    ('stu007', 'ewhite@example.com', 'github.com/erin'),
    ('stu008', 'sylvia.jones@example.com', 'github.com/sylvia');

-- Insert mock data for Outside_Organization
INSERT INTO Outside_Organization (username, name, interest) VALUES
    ('org001', 'Rochester Public Library', 'AI'),
    ('org002', 'Google', 'WebDevelopment'),
    ('org003', 'FBI', 'DataScience');
