-- MySQL dump 10.13  Distrib 9.5.0, for macos15.4 (arm64)
--
-- Host: localhost    Database: ccinfom
-- ------------------------------------------------------
-- Server version	9.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bike`
--
use ccinfom;

DROP TABLE IF EXISTS `bike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bike` (
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bike`
--

LOCK TABLES `bike` WRITE;
/*!40000 ALTER TABLE `bike` DISABLE KEYS */;
/*!40000 ALTER TABLE `bike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch` (
  `branchID` int NOT NULL,
  `branchName` varchar(45) NOT NULL,
  `branchAddress` varchar(45) NOT NULL,
  `locationID` int NOT NULL,
  PRIMARY KEY (`branchID`),
  KEY `locationID_idx` (`locationID`),
  CONSTRAINT `locationID` FOREIGN KEY (`locationID`) REFERENCES `location` (`locationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customerAccID` int NOT NULL,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `customerEmail` varchar(45) NOT NULL,
  `phoneNumber` varchar(15) NOT NULL,
  `customerPass` varchar(45) NOT NULL,
  PRIMARY KEY (`customerAccID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (100001,'Gabrielle','Bactong','gjbac@gmail.com','09761245576','jaifhe9872'),(100002,'Grant','Rosas','grantrosasc@gmail.com','09826123556','ndaki&827'),(100003,'Cath','Gunita','gunita@gmail.com','09726876596','ajshdakdsd'),(100004,'Ilagan','Ian','ilagan@gmail.com','09876752253','asdj*(*@(');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `location` (
  `locationID` int NOT NULL,
  `cityName` varchar(45) NOT NULL,
  `provinceName` varchar(45) NOT NULL,
  `regionName` varchar(45) NOT NULL,
  PRIMARY KEY (`locationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `location`
--

LOCK TABLES `location` WRITE;
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
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
  CONSTRAINT `reservation_bikeID` FOREIGN KEY (`bikeID`) REFERENCES `bike` (`bikeID`),
  CONSTRAINT `reservation_branchID` FOREIGN KEY (`branchID`) REFERENCES `branch` (`branchID`),
  CONSTRAINT `reservation_customerAccID` FOREIGN KEY (`customerAccID`) REFERENCES `customer` (`customerAccID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-13 10:13:28
