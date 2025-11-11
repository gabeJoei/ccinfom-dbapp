
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ccinfom
-- ------------------------------------------------------
-- Server version	8.0.43


-- Location table
CREATE TABLE IF NOT EXISTS `location` (
  `locationID` int NOT NULL,
  `cityName` varchar(45) NOT NULL,
  `provinceName` varchar(45) NOT NULL,
  `regionName` varchar(45) NOT NULL,
  PRIMARY KEY (`locationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Branch table
CREATE TABLE IF NOT EXISTS `branch` (
  `branchID` int NOT NULL,
  `branchName` varchar(45) NOT NULL,
  `branchAddress` varchar(45) NOT NULL,
  `locationID` int NOT NULL,
  PRIMARY KEY (`branchID`),
  KEY `locationID_idx` (`locationID`),
  CONSTRAINT `locationID` FOREIGN KEY (`locationID`) REFERENCES `location` (`locationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Bike table
CREATE TABLE IF NOT EXISTS `bike` (
  `bikeID` int NOT NULL,
  `branchIDNum` int NOT NULL,
  `bikeAvailability` enum('Yes','No') NOT NULL DEFAULT 'Yes',
  `bikeModel` varchar(50) NOT NULL,
  `hourlyRate` decimal(7,2) DEFAULT NULL,
  `dailyRate` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`bikeID`),
  KEY `branchIDNum_idx` (`branchIDNum`),
  CONSTRAINT `branchIDNum` FOREIGN KEY (`branchIDNum`) REFERENCES `branch` (`branchID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Customer table
CREATE TABLE IF NOT EXISTS `customer` (
  `customerAccID` int NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `customerEmail` varchar(45) NOT NULL,
  `phoneNumber` varchar(15) NOT NULL,
  PRIMARY KEY (`customerAccID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Reservation table 
CREATE TABLE IF NOT EXISTS `reservation` (
  `reservationReferenceNum` int NOT NULL AUTO_INCREMENT,
  `customerAccID` int NOT NULL,
  `bikeID` int NOT NULL,
  `branchID` int NOT NULL,
  `reservationDate` datetime NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL,
  `dateReturned` datetime DEFAULT NULL,
  `reservationStatus` enum('pending','ongoing','completed','cancelled') NOT NULL DEFAULT 'pending',
  PRIMARY KEY (`reservationReferenceNum`),
  KEY `customerAccID_idx` (`customerAccID`),
  KEY `bikeID_idx` (`bikeID`),
  KEY `branchID_idx` (`branchID`),
  CONSTRAINT `reservation_customerAccID` FOREIGN KEY (`customerAccID`) REFERENCES `customer` (`customerAccID`),
  CONSTRAINT `reservation_bikeID` FOREIGN KEY (`bikeID`) REFERENCES `bike` (`bikeID`),
  CONSTRAINT `reservation_branchID` FOREIGN KEY (`branchID`) REFERENCES `branch` (`branchID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- Payment table
CREATE TABLE IF NOT EXISTS `payment` (
  `paymentReferenceNum` int NOT NULL AUTO_INCREMENT,
  `customerID` int NOT NULL,
  `reservationReferenceNum` int DEFAULT NULL,
  `bikeID` int NOT NULL,
  `branchID` int NOT NULL,
  `paymentDate` datetime NOT NULL,
  `paymentAmount` double NOT NULL,
  PRIMARY KEY (`paymentReferenceNum`),
  KEY `customerAccID_idx` (`customerID`),
  KEY `reservationReferenceNum_idx` (`reservationReferenceNum`),
  CONSTRAINT `payment_customerAccID` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerAccID`),
  CONSTRAINT `payment_reservationReferenceNum` FOREIGN KEY (`reservationReferenceNum`) REFERENCES `reservation` (`reservationReferenceNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

