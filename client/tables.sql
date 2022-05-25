/*
  DB
*/
CREATE DATABASE asaf;

/*
  Users
*/
CREATE TABLE `asaf`.`users` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `user_Id` VARCHAR(45) NOT NULL UNIQUE,
    `first_name` VARCHAR(45) NOT NULL,
    `last_name` VARCHAR(45) NOT NULL,
    `phone_Number` VARCHAR(45) NULL,
    `email` VARCHAR(45) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `gender` VARCHAR(45) NOT NULL,
    `Image_Id` VARCHAR(100) DEFAULT NULL,
    `user_Avatar` VARCHAR(100) DEFAULT NULL,
    `degree` VARCHAR(45) DEFAULT NULL,
    `createdAt` DATE DEFAULT NULL,
    PRIMARY KEY (`id`, `user_Id`, `email`));

/*
  geoLocation
*/
CREATE TABLE `asaf`.`geoLocation` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `geoLocation_Id` VARCHAR(45) UNIQUE NOT NULL,
    `latitude` VARCHAR(100) NOT NULL,
    `longitude` VARCHAR(100) NOT NULL,
    `nameLocation` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`, `geoLocation_Id`));

/*
  Drives
*/
CREATE TABLE `asaf`.`drives` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `drive_Id` VARCHAR(45) NOT NULL UNIQUE,
    `geoLocationSrc_Id` VARCHAR(45) NOT NULL,
    `geoLocationDest_Id` VARCHAR(45) NOT NULL,
    `passengers` VARCHAR(200) DEFAULT NULL,
    `num_seat_available` INT DEFAULT 4,
    `price` VARCHAR(45) DEFAULT NULL,
    `AVG_Price` VARCHAR(45) DEFAULT NULL,
    `upcoming_Drives` VARCHAR(45) DEFAULT NULL,
    `createdAt` DATE DEFAULT NULL,
    PRIMARY KEY (`id`,`drive_Id`), 
    FOREIGN KEY (`geoLocationSrc_Id`) REFERENCES geoLocation(`geoLocation_Id`),
    FOREIGN KEY (`geoLocationDest_Id`) REFERENCES geoLocation(`geoLocation_Id`));


/*
  Universities
*/
CREATE TABLE `asaf`.`universities` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `university_Id` VARCHAR(45) NOT NULL UNIQUE,
    `university_name` VARCHAR(45) NOT NULL,
    `geoLocation_Id` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`, `university_Id`, `geoLocation_Id`), 
    FOREIGN KEY (`geoLocation_Id`) REFERENCES geoLocation(`geoLocation_Id`));



/*
  Users_Universities
*/
CREATE TABLE `asaf`.`users_Universities` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `user_Id` VARCHAR(45) NOT NULL,
    `university_Id` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`, `user_Id`, `university_Id`), 
    FOREIGN KEY (`user_Id`) REFERENCES users(`user_Id`),
    FOREIGN KEY (`university_Id`) REFERENCES universities(`university_Id`));


/*
  Users_Drives
*/
CREATE TABLE `asaf`.`users_Drives` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `user_Id` VARCHAR(45) NOT NULL,
    `drive_Id` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`, `user_Id`, `drive_Id`), 
    FOREIGN KEY (`user_Id`) REFERENCES users(`user_Id`),
    FOREIGN KEY (`drive_Id`) REFERENCES drives(`drive_Id`));


CREATE TABLE `asaf`.`nodes` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `node_Id` INT NOT NULL,
    `latitude` DECIMAL(10,10) NOT NULL,
    `longitude` DECIMAL(10,10) NOT NULL,
    `tags` JSON DEFAULT NULL,
    `degree` DECIMAL(10,10) DEFAULT NULL
    PRIMARY KEY (`id`, `node_Id`,`latitude`,`longitude`,`tags`,`degree`));



/*
  nodes
*/
CREATE TABLE `asaf`.`nodes` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `node_Id` INT NOT NULL UNIQUE,
    `latitude` DECIMAL(10,10) UNIQUE NOT NULL,
    `longitude` DECIMAL(10,10) UNIQUE NOT NULL,
    `degree` DECIMAL(10,10) DEFAULT NULL,
    `edges` JSON,
    `tags` JSON,
    PRIMARY KEY (`id`, `node_Id`,`latitude`,`longitude`));



/*
  edges
*/
CREATE TABLE `asaf`.`edges` (
    `id` INT NOT NULL UNIQUE AUTO_INCREMENT,
    `edge_Id` INT NOT NULL UNIQUE,
    `startNodeId` INT NOT NULL,
    `endNodeId` INT NOT NULL,
    `distance` DECIMAL(10,10) DEFAULT NULL,
    `weight` DECIMAL(10,10) DEFAULT NULL,
    `name` VARCHAR(100) DEFAULT NULL,
    `highwayType` VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (`id`, `edge_Id`, `startNodeId`, `endNodeId`), 
    FOREIGN KEY (`startNodeId`) REFERENCES nodes(`node_Id`),
    FOREIGN KEY (`endNodeId`) REFERENCES nodes(`node_Id`));
