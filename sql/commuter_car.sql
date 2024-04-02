-- MySQL dump 10.13  Distrib 8.3.0, for Win64 (x86_64)
--
-- Host: 8.130.35.16    Database: commuter_car
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `commuter_car`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `commuter_car` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `commuter_car`;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号/工号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电话号',
  `stop_id` bigint NOT NULL COMMENT '站点表id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常，1-被删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `passenger_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乘客表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'2020303390','$2a$10$MHFKJHXX/VN3b99oYaW43.dyxXll/Gnp3XeZQ10yJnlSx3IByZWja','胡世豪','13962628721',7,'2024-03-30 21:27:14','2024-03-30 21:27:14',0),(2,'2020303391','$2a$10$a3gtXcrDfnJ8cusgmRt2Vu.SnmQhiOunFGpc3ocbVU.A./lKKeJY.','胡世豪','13962628721',8,'2024-03-30 21:28:00','2024-03-30 21:28:00',0),(3,'2020302748','$2a$10$/pUvIgzVpZjmfI44wjvqTenIz6BQGCBjMWncD5jDo/gDFZYgMRqX2','fff','18391034600',9,'2024-04-01 10:02:57','2024-04-01 10:02:57',0);
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passenger`
--

DROP TABLE IF EXISTS `passenger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `passenger` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学号/工号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电话号',
  `station_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '默认下车站点名称',
  `driver_id` bigint NOT NULL DEFAULT '0' COMMENT '乘客状态：0-不在车上，1+（显示的对应司机的id，表示在哪个司机的车上）',
  `money` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '钱包余额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常，1-被删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `passenger_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乘客表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passenger`
--

LOCK TABLES `passenger` WRITE;
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
INSERT INTO `passenger` VALUES (9,'2020302759','$2a$10$toehaMGe1lTodYl5wp6.HuxrxwDA4bn7OUy75wUXQwmxFAq2syGEa','hcy','18391032482',NULL,0,0.00,'2024-03-30 22:25:54','2024-03-30 22:25:54',0),(10,'2020302646','$2a$10$EGoks70ghIkJaRf6XgdmTeRInb9d2G75ykhBFCR4.58QSdXBZvkOG','weng','18151195179',NULL,0,0.00,'2024-03-31 17:05:13','2024-03-31 17:05:13',0);
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stop`
--

DROP TABLE IF EXISTS `stop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stop` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `changan` int NOT NULL DEFAULT '0' COMMENT '长安校区下车人数',
  `guojiyi` int NOT NULL DEFAULT '0' COMMENT '国际医下车人数',
  `ziwei` int NOT NULL DEFAULT '0' COMMENT '紫薇站下车人数',
  `gaoxin` int NOT NULL DEFAULT '0' COMMENT '高新站下车人数',
  `laodong` int NOT NULL DEFAULT '0' COMMENT '劳动南路站下车人数',
  `youyi` int NOT NULL DEFAULT '0' COMMENT '友谊校区下车人数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stop`
--

LOCK TABLES `stop` WRITE;
/*!40000 ALTER TABLE `stop` DISABLE KEYS */;
INSERT INTO `stop` VALUES (7,0,0,0,0,0,0,'2024-03-30 13:27:14','2024-03-30 13:27:14',0),(8,0,0,0,0,0,0,'2024-03-30 13:28:00','2024-03-30 13:28:00',0),(9,0,0,0,0,0,0,'2024-04-01 02:02:57','2024-04-01 02:02:57',0);
/*!40000 ALTER TABLE `stop` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-02  1:03:32
