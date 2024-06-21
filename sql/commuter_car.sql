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
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bus` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `license_plate` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '车牌号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常，1-被删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus`
--

LOCK TABLES `bus` WRITE;
/*!40000 ALTER TABLE `bus` DISABLE KEYS */;
INSERT INTO `bus` VALUES (1,'陕A·A82HI','2024-04-19 20:12:51','2024-04-19 20:12:51',0),(2,'陕A·KM829','2024-04-28 15:08:04','2024-04-28 15:08:04',0),(3,'陕A·J1798','2024-04-28 15:08:14','2024-04-28 15:08:14',0),(4,'陕A·Q20V2','2024-04-28 15:09:11','2024-04-28 15:09:11',0),(5,'陕A·X8G08','2024-04-28 15:09:32','2024-04-28 15:09:32',0);
/*!40000 ALTER TABLE `bus` ENABLE KEYS */;
UNLOCK TABLES;

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
  `route_id` bigint NOT NULL DEFAULT '0' COMMENT '行驶路线id（只有选择工单后才会有）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常，1-被删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `passenger_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='司机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (1,'2020303390','$2a$10$MHFKJHXX/VN3b99oYaW43.dyxXll/Gnp3XeZQ10yJnlSx3IByZWja','胡世豪','13962628721',0,'2024-03-30 21:27:14','2024-06-02 02:47:57',0),(2,'2020303391','$2a$10$a3gtXcrDfnJ8cusgmRt2Vu.SnmQhiOunFGpc3ocbVU.A./lKKeJY.','伏世豪','13962628721',0,'2024-03-30 21:28:00','2024-06-02 02:47:57',0),(5,'2020302748','$2a$10$x0dXD40QEj3FsDXTtz3ykOiEB/h12eObabiBudVK8DWpYoZowcauG','胡思豪','18391032488',0,'2024-04-12 17:44:01','2024-06-21 13:54:12',0),(6,'2020302222','$2a$10$I14ds4.W0wgVyzsk2E43Te2izfIxhKjhafVboiFzWL8d5c43xtUVG','qqq','18399999999',0,'2024-04-30 19:11:31','2024-06-02 02:47:57',0);
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
  `geton_station_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '上车站点名称',
  `getoff_station_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '下车站点名称',
  `driver_id` bigint NOT NULL DEFAULT '0' COMMENT '乘客状态：0-不在车上，1+（显示的对应司机的id，表示在哪个司机的车上）',
  `route_id` bigint NOT NULL DEFAULT '0' COMMENT '乘坐的路线id（只有选择路线后才有）',
  `money` decimal(9,2) NOT NULL DEFAULT '0.00' COMMENT '钱包余额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常，1-被删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `passenger_pk` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乘客表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passenger`
--

LOCK TABLES `passenger` WRITE;
/*!40000 ALTER TABLE `passenger` DISABLE KEYS */;
INSERT INTO `passenger` VALUES (9,'2020302759','$2a$10$XlRKbsuiBsS/fYI9bIKqq.LHyOeLHEGoORwcdf6M0FYkhZe9HVnZC','hcy','18391032482',NULL,NULL,0,0,60.00,'2024-03-30 22:25:54','2024-06-21 13:57:00',0),(10,'2020302646','$2a$10$JB0jQnahVhktwM0RBZ/K6u9F09gTajBlyQwuBayU0UWBAAqjDX1Yi','weng','18151195179',NULL,NULL,0,0,160.15,'2024-03-31 17:05:13','2024-06-02 02:48:02',0),(11,'2020302744','$2a$10$lrQy6aXboVeFpm5QkNICfeqPoNSY1cTA9QmtYakpMDkio/qjK0UP2','ccc','18391032444',NULL,NULL,0,0,5.00,'2024-04-15 17:19:20','2024-06-02 02:48:02',0),(12,'2020302777','$2a$10$z3Y8cJQnhpujIr3kdTd.pu8Att0jL25uLaIK9WJQyCX3xXvqXQIvu','www','18391111111',NULL,NULL,0,0,0.00,'2024-04-30 19:15:40','2024-06-02 02:48:02',0);
/*!40000 ALTER TABLE `passenger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `route`
--

DROP TABLE IF EXISTS `route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `route` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `number` tinyint NOT NULL COMMENT '路线号(1~4)',
  `changan` int NOT NULL DEFAULT '0' COMMENT '长安校区上车人数',
  `dongmen` int NOT NULL DEFAULT '0' COMMENT '长安校区东门上下车人数',
  `guojiyi` int NOT NULL DEFAULT '0' COMMENT '国际医上下车人数',
  `ziwei` int NOT NULL DEFAULT '0' COMMENT '紫薇站上下车人数',
  `gaoxin` int NOT NULL DEFAULT '0' COMMENT '高新站上下车人数',
  `laodong` int NOT NULL DEFAULT '0' COMMENT '劳动南路站上下车人数',
  `youyi` int NOT NULL DEFAULT '0' COMMENT '友谊校区上下车人数',
  `yun` int NOT NULL DEFAULT '0' COMMENT '云天苑下车人数',
  `jiaoxi` int NOT NULL DEFAULT '0' COMMENT '教学西楼下车人数',
  `hai` int NOT NULL DEFAULT '0' COMMENT '海天苑下车人数',
  `qixiang` int NOT NULL DEFAULT '0' COMMENT '启翔楼下车人数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='路线表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `route`
--

LOCK TABLES `route` WRITE;
/*!40000 ALTER TABLE `route` DISABLE KEYS */;
INSERT INTO `route` VALUES (1,1,11,3,0,2,1,0,11,0,0,0,0,'2024-04-20 06:41:52','2024-06-21 05:58:02',0),(2,3,0,0,0,0,3,0,0,0,0,0,1,'2024-04-20 06:42:11','2024-06-21 05:51:22',0),(3,4,0,0,0,0,0,0,0,0,0,0,0,'2024-04-28 08:32:19','2024-06-21 05:38:23',0),(4,2,0,0,0,0,0,0,0,0,0,0,0,'2024-04-28 08:36:34','2024-06-21 05:38:35',0),(5,2,0,0,0,0,0,0,0,0,0,0,0,'2024-04-28 08:42:51','2024-06-21 05:38:40',0),(6,3,0,0,0,0,0,0,0,0,0,0,0,'2024-04-28 08:53:31','2024-04-30 08:37:46',0),(7,2,0,0,0,0,0,5,0,0,0,0,0,'2024-04-28 08:53:31','2024-05-08 09:37:31',0),(8,3,0,0,0,0,0,0,0,0,0,0,0,'2024-04-28 09:14:17','2024-04-30 08:39:49',1),(9,3,0,0,0,0,0,0,1,0,0,0,0,'2024-04-30 08:42:24','2024-05-10 07:51:59',0),(10,3,0,0,0,2,0,0,1,0,0,0,0,'2024-05-10 06:52:18','2024-05-10 13:38:47',0),(11,1,1,0,0,0,0,0,0,0,0,0,0,'2024-05-30 12:23:32','2024-06-21 05:38:45',0),(12,2,0,0,0,0,0,0,0,0,0,0,0,'2024-06-21 05:58:31','2024-06-21 05:58:31',1);
/*!40000 ALTER TABLE `route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `time` datetime NOT NULL COMMENT '发车时间',
  `driver_id` bigint DEFAULT NULL COMMENT '司机id',
  `route_id` bigint NOT NULL COMMENT '路线id',
  `bus_id` bigint NOT NULL COMMENT '车辆id',
  `status` tinyint NOT NULL COMMENT '工单状态（0-未分配，1-已分配，2-已执行）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '0-正常，1-被删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,'2024-06-01 07:00:00',5,1,1,1,'2024-04-20 14:38:44','2024-06-21 13:58:02',0),(2,'2024-06-01 07:00:00',5,2,2,2,'2024-04-24 17:22:20','2024-06-21 13:53:53',0),(3,'2024-06-01 12:00:00',5,3,1,1,'2024-04-28 16:33:00','2024-06-21 13:38:23',0),(4,'2024-06-01 16:00:00',5,4,2,1,'2024-04-28 16:36:40','2024-06-21 13:38:35',0),(5,'2024-06-01 18:00:00',2,5,3,1,'2024-04-28 16:42:51','2024-06-21 13:38:40',0),(6,'2024-06-02 07:00:00',NULL,6,4,0,'2024-04-28 16:53:31','2024-05-09 19:30:23',0),(7,'2024-06-02 10:00:00',1,7,4,1,'2024-04-28 16:53:31','2024-05-08 17:37:31',0),(8,'2024-06-03 09:00:00',5,8,5,1,'2024-04-28 17:14:18','2024-04-30 16:39:49',1),(9,'2024-06-02 15:00:00',NULL,9,5,0,'2024-04-30 16:42:24','2024-05-08 19:28:21',0),(10,'2024-06-01 09:00:00',1,10,4,1,'2024-05-10 14:52:18','2024-05-10 14:52:18',0),(11,'2024-06-02 07:00:00',5,11,3,1,'2024-05-30 20:23:32','2024-06-21 13:38:45',0),(12,'2024-06-01 08:00:00',1,12,3,1,'2024-06-21 13:58:31','2024-06-21 13:58:31',1);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-21 14:27:48
