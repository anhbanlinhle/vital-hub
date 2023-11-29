-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (x86_64)
--
-- Host: localhost    Database: vital_hub
-- ------------------------------------------------------
-- Server version	8.2.0

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

DROP DATABASE IF EXISTS `vital_hub`;
CREATE DATABASE IF NOT EXISTS `vital_hub`;
USE `vital_hub`;
--
-- Table structure for table `bicycling`
--

DROP TABLE IF EXISTS `bicycling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bicycling` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exercise_id` bigint DEFAULT NULL,
  `distance` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bic_fk_1` (`exercise_id`),
  CONSTRAINT `bic_fk_1` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bicycling`
--

LOCK TABLES `bicycling` WRITE;
/*!40000 ALTER TABLE `bicycling` DISABLE KEYS */;
INSERT INTO `bicycling` VALUES (1,2,500.123),(2,10,3.14254),(3,12,10.513),(4,13,0),(5,37,0),(6,49,0.489272),(7,52,0),(8,53,0),(9,59,500.123),(10,60,5932.34),(11,61,4813.12),(12,62,1321.12),(13,63,11031.1),(14,64,7031.12),(15,125,1234.56),(16,127,7890.1);
/*!40000 ALTER TABLE `bicycling` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `post_id` bigint DEFAULT NULL,
  `content` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `cmt_fk_1` (`user_id`),
  KEY `cmt_fk_2` (`post_id`),
  CONSTRAINT `cmt_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `cmt_fk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,1,1,'dep trai 1','2023-11-26 05:21:16','2023-11-26 05:21:16',0),(2,2,2,'dep trai 2','2023-11-26 05:21:16','2023-11-26 05:21:16',0),(3,1,1,'dep trai 3','2023-11-26 05:21:16','2023-11-26 05:21:16',0),(4,2,1,'dep trai 4','2023-11-26 05:21:16','2023-11-26 05:21:16',0),(5,2,3,'dep trai 5','2023-11-26 05:21:16','2023-11-26 05:21:16',0),(6,3,4,'dep trai 6','2023-11-26 05:21:16','2023-11-26 05:21:16',0),(7,6,1,'dep trai 5','2023-11-29 07:20:53','2023-11-29 07:20:53',0);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compe_ex`
--

DROP TABLE IF EXISTS `compe_ex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compe_ex` (
  `exercise_id` bigint NOT NULL,
  `compe_id` bigint NOT NULL,
  PRIMARY KEY (`exercise_id`,`compe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compe_ex`
--

LOCK TABLES `compe_ex` WRITE;
/*!40000 ALTER TABLE `compe_ex` DISABLE KEYS */;
INSERT INTO `compe_ex` VALUES (1,1),(3,2),(6,1),(8,1),(49,6),(95,2),(119,7),(120,7),(121,7),(122,7),(123,7),(125,9),(126,2),(127,9);
/*!40000 ALTER TABLE `compe_ex` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `competition`
--

DROP TABLE IF EXISTS `competition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `competition` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `host_id` bigint DEFAULT NULL,
  `title` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `started_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ended_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  `type` enum('RUNNING','BICYCLING','PUSHUP') DEFAULT NULL,
  `duration` time DEFAULT NULL,
  `background` text,
  PRIMARY KEY (`id`),
  KEY `compe_fk_1` (`host_id`),
  CONSTRAINT `compe_fk_1` FOREIGN KEY (`host_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `competition`
--

LOCK TABLES `competition` WRITE;
/*!40000 ALTER TABLE `competition` DISABLE KEYS */;
INSERT INTO `competition` VALUES (1,2,'thắng đường đua thua đường tình','2023-11-26 05:21:16','2023-11-26 05:21:16','2023-12-26 05:21:16',0,'BICYCLING','00:30:00','https://nld.mediacdn.vn/2020/12/8/13047134238384917728367743121356127102584377n-1607402858403113582018.jpg'),(2,5,'hít đất hay hất đít','2023-11-26 05:21:16','2023-11-26 05:21:16','2023-12-26 05:21:16',0,'PUSHUP','00:30:00','https://i.ibb.co/M9f9X6w/404658845-359825989907707-6091956744813618528-n.jpg'),(3,1,'rượt đuổi với thời gian','2023-11-26 19:44:21','2023-11-26 19:44:21','2023-12-28 19:44:21',0,'RUNNING','01:00:00','https://img.universitystudent.org/1/4/3153/life-at-university-deadlines-sleep-responsibilities-meme.jpg'),(6,6,'xe mẹ mua ko đua mẹ buồn','2023-11-28 19:44:21','2023-11-28 19:43:52','2024-02-16 19:43:54',0,'BICYCLING','01:30:00','https://bayngaymuasam.com/wp-content/uploads/2018/10/anh_6_docq.jpg'),(7,3,'chống đẩy thực tế ảo','2023-11-28 19:44:21','2023-11-28 19:43:52','2024-02-16 19:43:54',0,'PUSHUP','01:30:00','https://i.ibb.co/FXFP0yx/image.png'),(8,5,'làm sao để hít đất đúng cách?','2023-11-28 19:44:21','2023-11-28 19:43:52','2024-02-16 19:43:54',0,'PUSHUP','01:30:00','https://images.newscientist.com/wp-content/uploads/2020/04/06123708/gettyimages-661778881_web.jpg'),(9,1,'cuộc đời là những chuyến đi','2023-11-29 17:00:44','2023-11-29 17:00:19','2023-12-16 17:00:21',0,'BICYCLING','01:30:00','https://i.ibb.co/pXKCXM0/image.png'),(10,3,'chạy như naruto lên phố đi bộ','2023-11-29 17:00:44','2023-11-29 17:00:19','2023-12-16 17:00:21',0,'RUNNING','01:30:00','https://i.kinja-img.com/image/upload/c_fill,h_675,pg_1,q_80,w_1200/dbzjosytoalne9kbqsdw.gif'),(11,5,'đạp xe chổng ngược như CR7','2023-11-29 17:00:44','2023-11-29 17:00:19','2023-12-16 17:00:21',0,'BICYCLING','01:30:00','https://images2.thanhnien.vn/zoom/686_429/Uploaded/gianglao/2023_01_23/ronaldo-1756.jpeg'),(12,6,'chạy như sinh viên','2023-11-29 17:00:44','2023-11-29 17:00:19','2023-12-16 17:00:21',0,'RUNNING','01:30:00','https://i.ibb.co/pLhnnsj/image.png');
/*!40000 ALTER TABLE `competition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exercise`
--

DROP TABLE IF EXISTS `exercise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exercise` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `started_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ended_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `type` enum('RUNNING','BICYCLING','PUSHUP','GYM') DEFAULT NULL,
  `calo` float DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ex_fk_1` (`user_id`),
  CONSTRAINT `ex_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exercise`
--

LOCK TABLES `exercise` WRITE;
/*!40000 ALTER TABLE `exercise` DISABLE KEYS */;
INSERT INTO `exercise` VALUES (1,'2023-11-26 05:21:16','2023-11-26 05:21:16','RUNNING',300,1),(2,'2023-11-26 05:21:16','2023-11-26 05:21:16','BICYCLING',300,1),(3,'2023-11-26 05:21:16','2023-11-26 05:21:16','PUSHUP',300,1),(4,'2023-11-26 05:21:16','2023-11-26 05:21:16','GYM',300,1),(5,'2023-11-26 05:21:16','2023-11-26 05:21:16','RUNNING',300,1),(6,'2023-11-26 05:21:16','2023-11-26 05:21:16','BICYCLING',300,2),(7,'2023-11-26 05:21:16','2023-11-26 05:21:16','RUNNING',300,2),(8,'2023-11-26 05:21:16','2023-11-26 05:21:16','RUNNING',300,3),(9,'2023-11-26 16:58:00','2023-11-26 16:58:02','RUNNING',12.09,6),(10,'2023-11-27 18:40:02','2023-11-27 18:40:27','BICYCLING',0.0722784,6),(11,'2023-11-27 18:42:42','2023-11-27 18:42:53','GYM',370,6),(12,'2023-11-28 04:06:17','2023-11-28 04:06:52','BICYCLING',0.241799,6),(13,'2023-11-28 04:36:28','2023-11-28 04:36:32','BICYCLING',0,6),(14,'2023-11-28 14:31:31','2023-11-28 14:31:42','PUSHUP',3.5,6),(15,'2023-11-28 14:31:31','2023-11-28 14:31:45','PUSHUP',3.5,6),(16,'2023-11-28 14:31:31','2023-11-28 14:31:48','PUSHUP',3.5,6),(17,'2023-11-28 14:31:31','2023-11-28 14:31:50','PUSHUP',3.5,6),(18,'2023-11-28 14:31:31','2023-11-28 14:31:52','PUSHUP',3.5,6),(19,'2023-11-28 14:32:45','2023-11-28 14:32:51','PUSHUP',3.5,6),(20,'2023-11-28 14:32:45','2023-11-28 14:32:53','PUSHUP',3.5,6),(21,'2023-11-28 14:32:45','2023-11-28 14:32:56','PUSHUP',3.5,6),(22,'2023-11-28 14:32:45','2023-11-28 14:32:58','PUSHUP',3.5,6),(23,'2023-11-28 14:32:45','2023-11-28 14:33:01','PUSHUP',3.5,6),(24,'2023-11-28 14:32:45','2023-11-28 14:33:03','PUSHUP',3.5,6),(25,'2023-11-28 14:56:10','2023-11-28 14:56:14','PUSHUP',3.5,6),(26,'2023-11-28 14:58:28','2023-11-28 14:58:32','PUSHUP',3.5,6),(27,'2023-11-28 14:58:28','2023-11-28 14:58:40','PUSHUP',3.5,6),(28,'2023-11-28 14:58:28','2023-11-28 14:58:44','PUSHUP',3.5,6),(29,'2023-11-28 15:01:07','2023-11-28 15:01:19','PUSHUP',3.5,6),(30,'2023-11-28 15:02:53','2023-11-28 15:03:00','PUSHUP',2.5,6),(31,'2023-11-28 15:05:08','2023-11-28 15:05:15','PUSHUP',3.5,6),(32,'2023-11-28 15:06:26','2023-11-28 15:06:30','PUSHUP',2.5,6),(33,'2023-11-28 15:06:26','2023-11-28 15:06:34','PUSHUP',2.5,6),(34,'2023-11-28 15:06:26','2023-11-28 15:06:46','PUSHUP',2.5,6),(35,'2023-11-28 15:06:26','2023-11-28 15:06:47','PUSHUP',2.5,6),(36,'2023-11-28 15:06:26','2023-11-28 15:06:48','PUSHUP',2.5,5),(37,'2023-11-29 09:17:07','2023-11-29 09:17:20','BICYCLING',0,6),(38,'2023-11-29 09:32:00','2023-11-29 09:32:07','PUSHUP',2.5,6),(39,'2023-11-29 09:32:00','2023-11-29 09:32:15','PUSHUP',2.5,6),(40,'2023-11-29 09:32:00','2023-11-29 09:32:15','PUSHUP',2.5,6),(41,'2023-11-29 09:32:00','2023-11-29 09:32:15','PUSHUP',2.5,6),(42,'2023-11-29 09:32:00','2023-11-29 09:32:15','PUSHUP',2.5,6),(43,'2023-11-29 09:32:00','2023-11-29 09:32:15','PUSHUP',2.5,6),(44,'2023-11-29 09:32:00','2023-11-29 09:32:15','PUSHUP',2.5,6),(45,'2023-11-29 09:32:00','2023-11-29 09:32:15','PUSHUP',2.5,6),(46,'2023-11-29 09:32:00','2023-11-29 09:32:16','PUSHUP',2.5,6),(47,'2023-11-29 09:32:00','2023-11-29 09:32:16','PUSHUP',2.5,6),(48,'2023-11-29 09:32:00','2023-11-29 09:32:17','PUSHUP',2.5,6),(49,'2023-11-29 09:33:21','2023-11-29 09:33:35','BICYCLING',0.0112533,6),(50,'2023-11-29 09:43:03','2023-11-29 09:43:14','GYM',370,6),(51,'2023-11-29 09:43:46','2023-11-29 09:43:58','GYM',470,6),(52,'2023-11-29 10:37:20','2023-11-29 10:37:25','BICYCLING',0,6),(53,'2023-11-29 10:38:27','2023-11-29 10:38:33','BICYCLING',0,6),(54,'2023-11-28 05:21:16','2023-11-28 05:21:16','RUNNING',500,6),(55,'2023-11-26 17:00:00','2023-11-27 16:59:59','RUNNING',200,6),(56,'2023-11-26 17:00:00','2023-11-27 16:59:59','RUNNING',340,6),(57,'2023-11-27 17:00:00','2023-11-28 16:59:59','RUNNING',600,6),(58,'2023-11-27 17:00:00','2023-11-28 16:59:59','RUNNING',302,6),(59,'2023-11-27 06:19:39','2023-11-27 07:19:39','BICYCLING',300,6),(60,'2023-11-27 03:19:39','2023-11-27 05:10:39','BICYCLING',220,6),(61,'2023-11-28 03:19:39','2023-11-28 05:10:39','BICYCLING',490,6),(62,'2023-11-28 03:19:39','2023-11-28 03:50:31','BICYCLING',100,6),(63,'2023-11-29 13:19:39','2023-11-29 14:50:31','BICYCLING',500,6),(64,'2023-11-30 07:19:39','2023-11-30 08:50:11','BICYCLING',222,6),(65,'2023-11-28 17:00:00','2023-11-29 16:59:59','RUNNING',600,6),(66,'2023-11-28 17:00:00','2023-11-29 16:59:59','RUNNING',302,6),(67,'2023-11-27 07:19:39','2023-11-27 07:39:39','PUSHUP',350,6),(68,'2023-11-28 14:41:39','2023-11-28 15:00:00','PUSHUP',275,6),(69,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',365,6),(70,'2023-11-27 09:41:39','2023-11-27 10:41:39','GYM',300,6),(71,'2023-11-27 03:41:39','2023-11-27 04:21:39','GYM',350,6),(72,'2023-11-28 03:41:39','2023-11-28 05:21:39','GYM',310,6),(73,'2023-11-28 03:41:39','2023-11-28 05:21:39','GYM',200,6),(74,'2023-11-29 16:53:16','2023-11-29 16:53:20','PUSHUP',3.5,6),(75,'2023-11-29 16:53:16','2023-11-29 16:53:22','PUSHUP',3.5,6),(76,'2023-11-29 16:53:16','2023-11-29 16:53:24','PUSHUP',3.5,6),(77,'2023-11-29 16:53:16','2023-11-29 16:53:25','PUSHUP',3.5,6),(78,'2023-11-29 16:53:16','2023-11-29 16:53:26','PUSHUP',3.5,6),(79,'2023-11-29 16:53:16','2023-11-29 16:53:27','PUSHUP',3.5,6),(80,'2023-11-29 16:53:16','2023-11-29 16:53:28','PUSHUP',3.5,6),(81,'2023-11-29 16:53:16','2023-11-29 16:53:29','PUSHUP',3.5,6),(82,'2023-11-29 16:53:16','2023-11-29 16:53:29','PUSHUP',3.5,6),(83,'2023-11-29 16:53:16','2023-11-29 16:53:30','PUSHUP',3.5,6),(84,'2023-11-29 16:53:16','2023-11-29 16:53:31','PUSHUP',3.5,6),(85,'2023-11-29 16:53:16','2023-11-29 16:53:32','PUSHUP',3.5,6),(86,'2023-11-29 16:53:16','2023-11-29 16:53:33','PUSHUP',3.5,6),(87,'2023-11-29 16:53:16','2023-11-29 16:53:33','PUSHUP',3.5,6),(88,'2023-11-29 16:53:16','2023-11-29 16:53:34','PUSHUP',3.5,6),(89,'2023-11-29 16:53:16','2023-11-29 16:53:34','PUSHUP',3.5,6),(90,'2023-11-29 16:53:16','2023-11-29 16:53:35','PUSHUP',3.5,6),(91,'2023-11-29 16:53:16','2023-11-29 16:53:36','PUSHUP',3.5,6),(92,'2023-11-29 16:53:16','2023-11-29 16:53:37','PUSHUP',3.5,6),(93,'2023-11-29 16:53:16','2023-11-29 16:53:37','PUSHUP',3.5,6),(94,'2023-11-29 16:53:16','2023-11-29 16:53:38','PUSHUP',3.5,6),(95,'2023-11-29 16:54:20','2023-11-29 16:54:27','PUSHUP',6,6),(96,'2023-11-29 16:54:20','2023-11-29 16:54:28','PUSHUP',6,6),(97,'2023-11-29 16:54:20','2023-11-29 16:54:29','PUSHUP',6,6),(98,'2023-11-29 16:54:20','2023-11-29 16:54:30','PUSHUP',6,6),(99,'2023-11-29 16:54:20','2023-11-29 16:54:31','PUSHUP',6,6),(100,'2023-11-29 16:54:20','2023-11-29 16:54:32','PUSHUP',6,6),(101,'2023-11-29 16:54:20','2023-11-29 16:54:33','PUSHUP',6,6),(102,'2023-11-29 16:54:20','2023-11-29 16:54:34','PUSHUP',6,6),(103,'2023-11-29 16:54:20','2023-11-29 16:54:35','PUSHUP',6,6),(104,'2023-11-29 16:54:20','2023-11-29 16:54:35','PUSHUP',6,6),(105,'2023-11-29 16:54:20','2023-11-29 16:54:36','PUSHUP',6,6),(106,'2023-11-29 16:54:20','2023-11-29 16:54:37','PUSHUP',6,6),(107,'2023-11-29 16:54:20','2023-11-29 16:54:37','PUSHUP',6,6),(108,'2023-11-29 16:54:20','2023-11-29 16:54:38','PUSHUP',6,6),(109,'2023-11-29 16:54:20','2023-11-29 16:54:39','PUSHUP',6,6),(110,'2023-11-29 16:54:20','2023-11-29 16:54:40','PUSHUP',6,6),(111,'2023-11-29 16:54:20','2023-11-29 16:54:40','PUSHUP',6,6),(112,'2023-11-29 16:54:20','2023-11-29 16:54:41','PUSHUP',6,6),(113,'2023-11-29 16:54:20','2023-11-29 16:54:41','PUSHUP',6,6),(114,'2023-11-29 16:54:20','2023-11-29 16:54:42','PUSHUP',6,6),(115,'2023-11-29 16:54:20','2023-11-29 16:54:43','PUSHUP',6,6),(116,'2023-11-29 16:54:20','2023-11-29 16:54:43','PUSHUP',6,6),(117,'2023-11-29 16:54:20','2023-11-29 16:54:44','PUSHUP',6,6),(118,'2023-11-28 17:00:00','2023-11-29 16:58:02','RUNNING',55.25,6),(119,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',365,1),(120,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',365,2),(121,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',365,3),(122,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',365,5),(123,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',365,6),(124,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',365,6),(125,'2023-11-29 09:41:39','2023-11-29 10:11:00','BICYCLING',365,6),(126,'2023-11-29 09:41:39','2023-11-29 10:11:00','PUSHUP',265,5),(127,'2023-11-29 09:41:39','2023-11-29 10:11:00','BICYCLING',365,5);
/*!40000 ALTER TABLE `exercise` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend` (
  `first_user_id` bigint NOT NULL,
  `second_user_id` bigint NOT NULL,
  `status` enum('PENDING','ACCEPTED') DEFAULT NULL,
  PRIMARY KEY (`first_user_id`,`second_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
INSERT INTO `friend` VALUES (1,2,'ACCEPTED'),(3,2,'ACCEPTED'),(3,4,'ACCEPTED'),(5,1,'ACCEPTED'),(5,2,'ACCEPTED'),(5,3,'ACCEPTED'),(6,1,'ACCEPTED'),(6,2,'ACCEPTED'),(6,3,'ACCEPTED'),(6,5,'ACCEPTED');
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym`
--

DROP TABLE IF EXISTS `gym`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gym` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exercise_id` bigint DEFAULT NULL,
  `group_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gym_fk_1` (`exercise_id`),
  CONSTRAINT `gym_fk_1` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym`
--

LOCK TABLES `gym` WRITE;
/*!40000 ALTER TABLE `gym` DISABLE KEYS */;
INSERT INTO `gym` VALUES (1,4,4),(2,11,1),(3,50,1),(4,51,2),(5,70,1),(6,71,3),(7,72,4),(8,73,2);
/*!40000 ALTER TABLE `gym` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `participants`
--

DROP TABLE IF EXISTS `participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `participants` (
  `participant_id` bigint NOT NULL,
  `comp_id` bigint NOT NULL,
  PRIMARY KEY (`participant_id`,`comp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `participants`
--

LOCK TABLES `participants` WRITE;
/*!40000 ALTER TABLE `participants` DISABLE KEYS */;
INSERT INTO `participants` VALUES (1,1),(1,2),(1,3),(1,7),(1,8),(1,9),(2,1),(2,2),(2,7),(2,8),(2,9),(3,1),(3,2),(3,7),(3,8),(3,9),(3,10),(5,7),(5,8),(5,9),(5,11),(6,2),(6,4),(6,5),(6,6),(6,7),(6,8),(6,9),(6,12);
/*!40000 ALTER TABLE `participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `exercise_id` bigint DEFAULT NULL,
  `title` text,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  `image` text,
  PRIMARY KEY (`id`),
  KEY `p_fk_1` (`user_id`),
  CONSTRAINT `p_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,1,1,'lorem ipsum','2023-11-26 05:21:16','2023-11-26 05:21:16',1,'https://c02.purpledshub.com/uploads/sites/48/2023/02/why-sky-blue-2db86ae.jpg'),(2,5,36,'đang cảm thấy sung sức tại 103G2','2023-11-26 05:21:16','2023-11-26 05:21:16',0,'https://i.ibb.co/ZN5C7zL/pu-pose0.png'),(3,1,1,'lorem ipsum','2023-11-26 05:21:16','2023-11-26 05:21:16',1,'https://baocantho.com.vn/image/fckeditor/upload/2018/20180829/images/malone.jpg'),(4,1,1,'lorem ipsum','2023-11-26 05:21:16','2023-11-26 05:21:16',1,'https://staticg.sportskeeda.com/editor/2023/09/f29a8-16954713783435-1920.jpg?w=840'),(5,1,1,'lorem ipsum','2023-11-26 05:21:16','2023-11-26 05:21:16',1,'https://storage.googleapis.com/cdn-entrade/bovagau-meme/ban-qua-hu-ban-se-bi-dam-daddy-p_1680063754'),(6,1,4,'trưởng thành hơn, khỏe mạnh hơn','2023-11-26 05:21:16','2023-11-26 05:21:16',0,'https://origympersonaltrainercourses.co.uk/files/img_cache/2124/450_450__1554970272_wokroutmeme6.jpg'),(7,6,9,'...','2023-11-28 16:33:39','2023-11-28 16:33:39',1,'https://i.ibb.co/ct0T9Kd/334943619-590393196309255-5947932258672819023-n.png'),(8,6,9,'okkkk','2023-11-28 16:34:00','2023-11-28 16:34:00',1,'https://i.ibb.co/sJNWRLL/11c40b61bfe6.jpg'),(9,6,10,'yêu dân tổ có khổ 0 em','2023-11-29 07:15:05','2023-11-29 07:15:05',0,'https://i.pinimg.com/564x/db/6e/b7/db6eb7a48ddd4660e1b5e5656e988a6d.jpg'),(10,2,7,'thể thao lành mạnh','2023-11-26 05:21:16','2023-11-28 16:33:39',0,'https://i.ibb.co/ct0T9Kd/334943619-590393196309255-5947932258672819023-n.png'),(11,2,6,'đạp xe tiếp sức cùng em TÚân','2023-11-26 05:21:19','2023-11-28 15:33:39',1,'https://i.ibb.co/myyxH7m/369313917-1117441892757159-4795899419008470900-n.jpg'),(12,2,6,'em tú','2023-11-26 05:21:19','2023-11-28 15:33:39',1,'https://i.ibb.co/myyxH7m/369313917-1117441892757159-4795899419008470900-n.jpg'),(13,5,122,'đạp xe giải cứu em TÚân','2023-11-26 05:21:19','2023-11-28 15:33:39',0,'https://i.ibb.co/myyxH7m/369313917-1117441892757159-4795899419008470900-n.jpg');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `push_up`
--

DROP TABLE IF EXISTS `push_up`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `push_up` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exercise_id` bigint DEFAULT NULL,
  `rep` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pu_fk_1` (`exercise_id`),
  CONSTRAINT `pu_fk_1` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `push_up`
--

LOCK TABLES `push_up` WRITE;
/*!40000 ALTER TABLE `push_up` DISABLE KEYS */;
INSERT INTO `push_up` VALUES (1,3,500),(2,14,7),(3,15,7),(4,16,7),(5,17,7),(6,18,7),(7,19,7),(8,20,7),(9,21,7),(10,22,7),(11,23,7),(12,24,7),(13,25,7),(14,26,7),(15,27,7),(16,28,7),(17,29,7),(18,30,5),(19,31,7),(20,32,5),(21,33,5),(22,34,5),(23,35,5),(24,36,5),(25,38,5),(26,39,5),(27,40,5),(28,41,5),(29,42,5),(30,43,5),(31,44,5),(32,45,5),(33,46,5),(34,47,5),(35,48,5),(36,67,500),(37,68,315),(38,69,515),(39,74,7),(40,75,7),(41,76,7),(42,77,7),(43,78,7),(44,79,7),(45,80,7),(46,81,7),(47,82,7),(48,83,7),(49,84,7),(50,85,7),(51,86,7),(52,87,7),(53,88,7),(54,89,7),(55,90,7),(56,91,7),(57,92,7),(58,93,7),(59,94,7),(60,95,12),(61,96,12),(62,97,12),(63,98,12),(64,99,12),(65,100,12),(66,101,12),(67,102,12),(68,103,12),(69,104,12),(70,105,12),(71,106,12),(72,107,12),(73,108,12),(74,109,12),(75,110,12),(76,111,12),(77,112,12),(78,113,12),(79,114,12),(80,115,12),(81,116,12),(82,117,12),(83,119,12),(84,120,12),(85,121,10),(86,122,12),(87,123,11),(88,124,12),(89,126,25);
/*!40000 ALTER TABLE `push_up` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `running`
--

DROP TABLE IF EXISTS `running`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `running` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `exercise_id` bigint DEFAULT NULL,
  `step` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `run_fk_1` (`exercise_id`),
  CONSTRAINT `run_fk_1` FOREIGN KEY (`exercise_id`) REFERENCES `exercise` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `running`
--

LOCK TABLES `running` WRITE;
/*!40000 ALTER TABLE `running` DISABLE KEYS */;
INSERT INTO `running` VALUES (1,1,5000),(2,5,4000),(3,6,3000),(4,7,4200),(5,8,10000),(6,9,6868),(7,54,3500),(8,55,4000),(9,56,3500),(10,57,5040),(11,58,3070),(12,65,2000),(13,66,4500),(14,118,850);
/*!40000 ALTER TABLE `running` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gmail` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `sex` enum('MALE','FEMALE') DEFAULT NULL,
  `phone_no` varchar(15) DEFAULT NULL,
  `avatar` text,
  `dob` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `gmail` (`gmail`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'teoemga2003@gmail.com','Thủy thần thành Vinh','MALE','12312422','https://i.ibb.co/kDmg1DK/379635439-1776563489430013-9113407012311746702-n.jpg','2003-12-28'),(2,'minhtuan3154@gmail.com','Hoàng tử Yên Phụ','MALE','12312422','https://i.ibb.co/WW39wyT/342210546-120192407708675-2942428996201967091-n.png','2003-12-28'),(3,'nhimcoi262@gmail.com','Lính Lê Văn Thiêm','MALE','12312422','https://i.ibb.co/vDnj6Kz/336393037-764999541634602-4370966108860378010-n.png','2003-12-28'),(5,'kobehng@gmail.com','Hoàng đế An Dương','MALE','12312422','https://i.ibb.co/jH5Gpwz/329105807-2112059349183889-6448693875391137282-n.jpg','2003-12-28'),(6,'linhyeunguyen1111@gmail.com','Công tước Hàng Tre','MALE','12312422','https://lh3.googleusercontent.com/a/ACg8ocJ60CxgbXERIgV_Kj73FR--zto_qiwz2dYBrjuJmuIyVrc=s480-p-k-rw-no','2003-12-28');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_detail`
--

DROP TABLE IF EXISTS `user_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `current_height` float DEFAULT NULL,
  `current_weight` float DEFAULT NULL,
  `exercise_days_per_week` int DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `ud_fk_1` (`user_id`),
  CONSTRAINT `ud_fk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_detail`
--

LOCK TABLES `user_detail` WRITE;
/*!40000 ALTER TABLE `user_detail` DISABLE KEYS */;
INSERT INTO `user_detail` VALUES (1,1,100,100,5,'test description'),(2,2,100,100,5,'test2 description'),(3,3,100,100,5,'quan'),(5,5,100,100,5,'an'),(6,6,100,100,5,'linh');
/*!40000 ALTER TABLE `user_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workout_exercises`
--

DROP TABLE IF EXISTS `workout_exercises`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `workout_exercises` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_id` bigint DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` text,
  `sets` int DEFAULT NULL,
  `reps_per_set` int DEFAULT NULL,
  `total_calo` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workout_exercises`
--

LOCK TABLES `workout_exercises` WRITE;
/*!40000 ALTER TABLE `workout_exercises` DISABLE KEYS */;
INSERT INTO `workout_exercises` VALUES (1,1,'Push Ups','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,60),(2,1,'Dumbbell Bench Press','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,12,80),(3,1,'Incline Dumbbell','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,70),(4,1,'Dumbbell Fly','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,15,100),(5,1,'Cable Crossovers','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,8,60),(6,2,'Pull Ups','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,60),(7,2,'Wide Grip Lat Pull Downs','Lorem ipsum dolor sit amet, consectetur adipiscing elit',3,20,120),(8,2,'Seated Row Machine','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,70),(9,2,'Bent Over Row','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,12,120),(10,2,'Straight Bar Curl','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,100),(11,3,'Squats','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,120),(12,3,'Leg Press Machine','Lorem ipsum dolor sit amet, consectetur adipiscing elit',3,20,120),(13,3,'Walking Lunges','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,100),(14,3,'Leg Extension Machine','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,12,120),(15,3,'Straight Leg Deadlifts','Lorem ipsum dolor sit amet, consectetur adipiscing elit',3,10,100),(16,4,'Handstand Push Up','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,80),(17,4,'Military Press','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,100),(18,4,'Upright Rows','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,15,100),(19,4,'Lateral Raises','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,20,80),(20,4,'Rear Delt Raise','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,80),(21,5,'Tricep Pullovers','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,60),(22,5,'Tricep Kickbacks','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,80),(23,5,'Dips','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,15,80),(24,5,'Concentration Curls','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,12,80),(25,5,'Preacher Curl','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,70),(26,6,'Preacher Curl','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,70),(27,6,'Rear Delt Raise','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,80),(28,6,'Handstand Push Up','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,80),(29,6,'Bent Over Row','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,12,120),(30,6,'Cable Crossovers','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,8,60),(31,7,'Straight Bar Curl','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,100),(32,7,'Wide Grip Lat Pull Downs','Lorem ipsum dolor sit amet, consectetur adipiscing elit',3,20,120),(33,7,'Dumbbell Fly','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,15,100),(34,7,'Cable Crossovers','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,8,60),(35,7,'Dips','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,15,80),(36,8,'Seated Row Machine','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,70),(37,8,'Tricep Kickbacks','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,80),(38,8,'Walking Lunges','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,10,100),(39,8,'Lateral Raises','Lorem ipsum dolor sit amet, consectetur adipiscing elit',4,20,80),(40,8,'Incline Dumbbell','Lorem ipsum dolor sit amet, consectetur adipiscing elit',5,10,70);
/*!40000 ALTER TABLE `workout_exercises` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workout_mapping`
--

DROP TABLE IF EXISTS `workout_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `workout_mapping` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bmi_upper_bound` float DEFAULT NULL,
  `bmi_lower_bound` float DEFAULT NULL,
  `body_status` enum('SUPER_THIN','THIN','NORMAL','FAT','OBESITY') DEFAULT NULL,
  `suggest_group_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workout_mapping`
--

LOCK TABLES `workout_mapping` WRITE;
/*!40000 ALTER TABLE `workout_mapping` DISABLE KEYS */;
INSERT INTO `workout_mapping` VALUES (1,17,0,'SUPER_THIN',5),(2,18.5,17,'THIN',7),(3,25,18.5,'NORMAL',2),(4,30,25,'FAT',1),(5,1000,30,'OBESITY',3);
/*!40000 ALTER TABLE `workout_mapping` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-30  3:53:18
