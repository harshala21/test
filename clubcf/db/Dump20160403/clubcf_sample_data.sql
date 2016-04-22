-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: localhost    Database: clubcf
-- ------------------------------------------------------
-- Server version	5.5.36

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
-- Table structure for table `sample_data`
--

DROP TABLE IF EXISTS `sample_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(2000) DEFAULT NULL,
  `sname` varchar(2000) DEFAULT NULL,
  `api` varchar(2000) DEFAULT NULL,
  `tags` varchar(2000) DEFAULT NULL,
  `stemword` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sample_data`
--

LOCK TABLES `sample_data` WRITE;
/*!40000 ALTER TABLE `sample_data` DISABLE KEYS */;
INSERT INTO `sample_data` VALUES (1,'S1','4wheelzRouteMap','Google Maps','driving,google,maps,streetview','drive,google,map,streetview'),(2,'S2','GuruLib','Amazon Product Advertizing','books,library,videos','book,library,video'),(3,'S3','100Destinations','Google Maps,Twitter','fun,mapping,social,photo,travel','fun,map,social,photo,travel'),(4,'S4','Anuncious total','Google Maps,Twitter','ads,deadpool,shopping','ads,deadpool,shop'),(5,'S5','22books','Amazon Product Advertizing','books,lists,shopping,social','book,list,shop,social'),(6,'S6','Favmvs','Google Search,MTV','deadpool,MTV,music,video','deadpool,MTV,music,video'),(7,'S7','FlickrCash','Flickr','photos,shopping','photo,shop');
/*!40000 ALTER TABLE `sample_data` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-03 18:40:24
