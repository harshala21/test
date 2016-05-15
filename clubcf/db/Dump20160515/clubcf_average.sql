-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: clubcf
-- ------------------------------------------------------
-- Server version	5.7.11-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `average`
--

DROP TABLE IF EXISTS `average`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `average` (
  `s1` varchar(2000) DEFAULT NULL,
  `s2` varchar(2000) DEFAULT NULL,
  `s3` varchar(2000) DEFAULT NULL,
  `s4` varchar(2000) DEFAULT NULL,
  `s5` varchar(2000) DEFAULT NULL,
  `s6` varchar(2000) DEFAULT NULL,
  `s7` varchar(2000) DEFAULT NULL,
  `s8` varchar(2000) DEFAULT NULL,
  `s9` varchar(2000) DEFAULT NULL,
  `s10` varchar(2000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `average`
--

LOCK TABLES `average` WRITE;
/*!40000 ALTER TABLE `average` DISABLE KEYS */;
INSERT INTO `average` VALUES ('1.000','0.000','0.000','0.171','0.196','0.000','0.000','0.000','0.000','0.000'),('0.000','1.000','0.000','0.000','0.000','0.267','0.000','0.000','0.250','0.083'),('0.000','0.000','1.000','0.000','0.000','0.000','0.000','0.000','0.000','0.000'),('0.171','0.000','0.000','1.000','0.321','0.000','0.100','0.000','0.000','0.000'),('0.196','0.000','0.000','0.321','1.000','0.000','0.100','0.083','0.000','0.000'),('0.000','0.267','0.000','0.000','0.000','1.000','0.000','0.000','0.125','0.200'),('0.000','0.000','0.000','0.100','0.100','0.000','1.000','0.125','0.000','0.000'),('0.000','0.000','0.000','0.000','0.083','0.000','0.125','1.000','0.000','0.000'),('0.000','0.250','0.000','0.000','0.000','0.125','0.000','0.000','1.125','0.071'),('0.000','0.083','0.000','0.000','0.000','0.200','0.000','0.000','0.071','1.000');
/*!40000 ALTER TABLE `average` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-15 15:10:19
