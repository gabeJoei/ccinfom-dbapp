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
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `adminID` int NOT NULL AUTO_INCREMENT,
  `adminUsername` varchar(45) NOT NULL,
  `adminPassword` varchar(255) NOT NULL,
  `adminEmail` varchar(45) NOT NULL,
  `adminFirstName` varchar(45) NOT NULL,
  `adminLastName` varchar(45) NOT NULL,
  PRIMARY KEY (`adminID`),
  UNIQUE KEY `adminUsername` (`adminUsername`),
  UNIQUE KEY `adminEmail_UNIQUE` (`adminEmail`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','admin123','admin@bikerental.com','System','Administrator'),(2,'manager1','manager123','manager1@bikerental.com','Branch','Manager'),(3,'staff1','staff123','staff1@bikerental.com','Staff','Member');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bike`
--

DROP TABLE IF EXISTS `bike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bike` (
  `bikeID` int NOT NULL AUTO_INCREMENT,
  `branchIDNum` int NOT NULL,
  `bikeAvailability` enum('Yes','No') NOT NULL DEFAULT 'Yes',
  `bikeModel` varchar(50) NOT NULL,
  `hourlyRate` decimal(7,2) DEFAULT NULL,
  `dailyRate` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`bikeID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bike`
--

LOCK TABLES `bike` WRITE;
/*!40000 ALTER TABLE `bike` DISABLE KEYS */;
INSERT INTO `bike` VALUES (1,1,'No','Mountain Bike',25.00,150.00),(2,1,'Yes','Road Bike',20.00,120.00),(3,2,'Yes','Bike with E-assist',30.00,180.00),(4,3,'Yes','tandem Bike',22.50,135.00),(5,2,'Yes','E-Bike',28.00,160.00),(6,1,'Yes','BMX bike',18.00,100.00),(7,1,'No','Mountain Bike',25.00,150.00),(8,2,'Yes','Mountain Bike',25.00,150.00),(13,3,'Yes','Mountain Bike',25.00,150.00),(14,3,'Yes','Mountain Bike',25.00,150.00),(15,2,'Yes','Bike with E-assist',30.00,180.00),(16,3,'Yes','Bike with E-assist',30.00,180.00),(17,1,'No','Bike with E-assist',30.00,180.00),(18,2,'Yes','Bike with E-assist',30.00,180.00),(19,3,'Yes','tandem Bike',22.50,135.00),(20,2,'Yes','E-Bike',28.00,160.00),(21,1,'Yes','BMX bike',18.00,100.00),(25,1,'Yes','Mountain Bike',12.00,12.00),(26,1,'No','Mountain Bike',12.00,12.00),(27,1,'No','Mountain Bike',12.00,12.00),(28,8,'Yes','E-Bike',10.00,100.00);
/*!40000 ALTER TABLE `bike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch` (
  `branchID` int NOT NULL AUTO_INCREMENT,
  `branchName` varchar(45) NOT NULL,
  `branchAddress` varchar(45) NOT NULL,
  PRIMARY KEY (`branchID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch`
--

LOCK TABLES `branch` WRITE;
/*!40000 ALTER TABLE `branch` DISABLE KEYS */;
INSERT INTO `branch` VALUES (1,'Metro Central Hub','123 Ayala Ave, Makati City'),(2,'Cebu IT Park','456 Tech Drive, Cebu City'),(3,'Davao Downtown','789 Magsaysay St, Davao City'),(6,'BGC High Street','101 30th St, Taguig City');
/*!40000 ALTER TABLE `branch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customerAccID` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(45) NOT NULL,
  `lastName` varchar(45) NOT NULL,
  `customerEmail` varchar(45) NOT NULL,
  `phoneNumber` varchar(15) NOT NULL,
  `customerPass` varchar(45) NOT NULL,
  PRIMARY KEY (`customerAccID`),
  UNIQUE KEY `customerEmail_UNIQUE` (`customerEmail`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'Test','User','test@gmail.com','09761235515','testing'),(2,'John','Doe','johndoe@gmail.com','09761234567','pass'),(3,'Catherine','G','catherine_g@gmail.com','09919919919','cathGcath'),(4,'Gabrielle','Bactong','deleted_LWgh5oMKUxZa5PhU9too','09761235515','fI4192exfiNxZsbi68Cp');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
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
  KEY `reservationReferenceNum_idx` (`reservationReferenceNum`,`customerID`) /*!80000 INVISIBLE */,
  CONSTRAINT `payment_reservationReferenceNum` FOREIGN KEY (`reservationReferenceNum`) REFERENCES `reservation` (`reservationReferenceNum`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (25,1,40,25,1,'2025-11-19 18:24:20',12),(26,1,41,6,1,'2025-11-19 18:24:42',54),(27,1,42,16,3,'2025-11-19 18:24:59',180),(28,1,40,25,1,'2025-11-20 18:25:24',75),(29,1,41,6,1,'2025-11-20 18:25:50',285),(30,1,NULL,28,8,'2025-11-19 18:59:53',100);
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
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (40,1,25,1,'2025-11-19 18:24:19','2025-11-20 12:00:00','2025-11-20 13:00:00','2025-11-20 18:25:24','completed'),(41,1,6,1,'2025-11-19 18:24:40','2025-11-19 20:00:00','2025-11-19 23:00:00','2025-11-20 18:25:50','completed'),(42,1,16,3,'2025-11-19 18:24:57','2025-11-28 12:00:00','2025-11-28 20:00:00','2025-11-28 18:26:03','completed');
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

-- Dump completed on 2025-11-19 19:08:29
