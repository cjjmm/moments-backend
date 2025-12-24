-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: moments_app
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `post_id` bigint NOT NULL COMMENT '内容ID',
  `user_id` bigint NOT NULL COMMENT '评论用户ID',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-已删除，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`comment_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,2,6,'棒棒棒',1,'2025-12-22 20:41:23'),(2,3,6,'你好',1,'2025-12-22 20:54:59'),(3,7,3,'加油',1,'2025-12-24 18:52:26');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `like_record`
--

DROP TABLE IF EXISTS `like_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `like_record` (
  `like_id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
  `post_id` bigint NOT NULL COMMENT '内容ID',
  `user_id` bigint NOT NULL COMMENT '点赞用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`like_id`),
  UNIQUE KEY `uk_post_user` (`post_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `like_record`
--

LOCK TABLES `like_record` WRITE;
/*!40000 ALTER TABLE `like_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `like_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media`
--

DROP TABLE IF EXISTS `media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media` (
  `media_id` bigint NOT NULL AUTO_INCREMENT COMMENT '媒体ID',
  `post_id` bigint NOT NULL COMMENT '所属内容ID',
  `media_url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '媒体文件URL',
  `media_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '媒体类型：IMAGE-图片, VIDEO-视频',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小（字节）',
  `sort_order` int DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`media_id`),
  KEY `idx_post_id` (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='媒体文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media`
--

LOCK TABLES `media` WRITE;
/*!40000 ALTER TABLE `media` DISABLE KEYS */;
INSERT INTO `media` VALUES (1,1,'http://localhost:8080/uploads/image/2025/12/22/d48ede2a969b4de59d15e3b261cfab4d.png','IMAGE',0,0,'2025-12-22 20:41:02'),(2,2,'http://localhost:8080/uploads/image/2025/12/22/71bc49a1eab2424f92ef079c44a77278.png','IMAGE',0,0,'2025-12-22 20:41:06'),(3,3,'http://localhost:8080/uploads/video/2025/12/22/6f69f98859a2417daefff8358071e4c3.mp4','VIDEO',0,0,'2025-12-22 20:43:56'),(4,4,'http://localhost:8080/uploads/video/2025/12/22/d493d35afbd4410a819d88df20e48582.mp4','VIDEO',0,0,'2025-12-22 20:46:38'),(5,5,'http://localhost:8080/uploads/image/2025/12/22/512a4ae612a144939388828d327fb42e.jpg','IMAGE',0,0,'2025-12-22 20:55:15'),(6,6,'http://localhost:8080/uploads/video/2025/12/22/1a455345617a4c50a68a3e83038c61f9.mp4','VIDEO',0,0,'2025-12-22 21:16:29'),(7,7,'http://localhost:8080/uploads/image/2025/12/24/a8bec1c0e6c74330b676165c89ed3dc4.png','IMAGE',0,0,'2025-12-24 18:52:06'),(8,8,'http://localhost:8080/uploads/video/2025/12/24/173a0e7bed9840c69383b46aa36f8a55.mp4','VIDEO',0,0,'2025-12-24 20:42:15');
/*!40000 ALTER TABLE `media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `user_id` bigint NOT NULL COMMENT '发布用户ID',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '文本内容',
  `media_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'TEXT' COMMENT '媒体类型：TEXT-纯文本, IMAGE-图片, VIDEO-视频',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `view_count` int DEFAULT '0' COMMENT '浏览数',
  `avg_rating` decimal(3,2) DEFAULT '0.00' COMMENT '平均评分',
  `rating_count` int DEFAULT '0' COMMENT '评分人数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-已删除，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`post_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,6,'yes','IMAGE',0,0,1,0.00,0,0,'2025-12-22 20:41:02','2025-12-22 20:41:02'),(2,6,'yes','IMAGE',0,1,4,3.00,1,0,'2025-12-22 20:41:06','2025-12-22 20:41:06'),(3,6,'11','VIDEO',0,1,6,4.00,1,0,'2025-12-22 20:43:56','2025-12-22 20:43:56'),(4,6,'11','VIDEO',0,0,4,0.00,0,0,'2025-12-22 20:46:38','2025-12-22 20:46:38'),(5,6,'11','IMAGE',0,0,3,0.00,0,0,'2025-12-22 20:55:15','2025-12-22 20:55:15'),(6,6,'1','VIDEO',0,0,1,0.00,0,0,'2025-12-22 21:16:29','2025-12-22 21:16:29'),(7,3,'1','IMAGE',0,1,10,4.00,1,0,'2025-12-24 18:52:06','2025-12-24 18:52:06'),(8,7,'Q','VIDEO',0,0,3,0.00,0,1,'2025-12-24 20:42:15','2025-12-24 20:42:15');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_like`
--

DROP TABLE IF EXISTS `post_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_like` (
  `like_id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`like_id`),
  UNIQUE KEY `uk_post_user` (`post_id`,`user_id`),
  KEY `idx_post_id` (`post_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_like`
--

LOCK TABLES `post_like` WRITE;
/*!40000 ALTER TABLE `post_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_tag`
--

DROP TABLE IF EXISTS `post_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `post_id` bigint NOT NULL COMMENT '内容ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_tag` (`post_id`,`tag_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_tag`
--

LOCK TABLES `post_tag` WRITE;
/*!40000 ALTER TABLE `post_tag` DISABLE KEYS */;
INSERT INTO `post_tag` VALUES (1,3,9,'2025-12-22 20:43:56'),(2,4,9,'2025-12-22 20:46:38'),(3,5,9,'2025-12-22 20:55:15'),(4,5,10,'2025-12-22 20:55:15'),(5,7,11,'2025-12-24 18:52:06'),(6,8,1,'2025-12-24 20:42:15');
/*!40000 ALTER TABLE `post_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating` (
  `rating_id` bigint NOT NULL AUTO_INCREMENT COMMENT '评分ID',
  `post_id` bigint NOT NULL COMMENT '内容ID',
  `user_id` bigint NOT NULL COMMENT '评分用户ID',
  `score` tinyint NOT NULL COMMENT '评分：1-5分',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`rating_id`),
  UNIQUE KEY `uk_post_user` (`post_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评分表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
INSERT INTO `rating` VALUES (1,2,6,3,'2025-12-22 20:41:18','2025-12-22 20:41:18'),(2,3,6,4,'2025-12-22 20:54:47','2025-12-22 20:54:47'),(3,7,3,4,'2025-12-24 18:52:16','2025-12-24 18:52:16');
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensitive_word`
--

DROP TABLE IF EXISTS `sensitive_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sensitive_word` (
  `word_id` bigint NOT NULL AUTO_INCREMENT COMMENT '敏感词ID',
  `word` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '敏感词',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`word_id`),
  UNIQUE KEY `uk_word` (`word`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='敏感词表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensitive_word`
--

LOCK TABLES `sensitive_word` WRITE;
/*!40000 ALTER TABLE `sensitive_word` DISABLE KEYS */;
INSERT INTO `sensitive_word` VALUES (1,'测试敏感词1','2025-12-20 16:11:19'),(2,'测试敏感词2','2025-12-20 16:11:19'),(3,'广告','2025-12-20 16:11:19'),(4,'垃圾','2025-12-20 16:11:19');
/*!40000 ALTER TABLE `sensitive_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `tag_id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标签名称',
  `use_count` int DEFAULT '0' COMMENT '使用次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `uk_tag_name` (`tag_name`),
  KEY `idx_use_count` (`use_count`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
INSERT INTO `tag` VALUES (1,'生活',1,'2025-12-20 16:11:19'),(2,'美食',0,'2025-12-20 16:11:19'),(3,'旅游',0,'2025-12-20 16:11:19'),(4,'摄影',0,'2025-12-20 16:11:19'),(5,'音乐',0,'2025-12-20 16:11:19'),(6,'电影',0,'2025-12-20 16:11:19'),(7,'运动',0,'2025-12-20 16:11:19'),(8,'学习',0,'2025-12-20 16:11:19'),(9,'1',3,'2025-12-22 20:43:56'),(10,'撒大苏打',1,'2025-12-22 20:55:15'),(11,'靶场',1,'2025-12-24 18:52:06');
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密存储）',
  `nickname` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `role` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'USER' COMMENT '角色：USER-普通用户, ADMIN-管理员',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'user001','$2a$10$ME.rnqL4AUPZ47Q0XWAd7uBPxy4kXPfhh8cdtO6FrpjQmcZzFGHye','测试用户1',NULL,'USER',1,'2025-12-20 16:11:19','2025-12-24 18:46:39'),(3,'user002','$2a$10$ME.rnqL4AUPZ47Q0XWAd7uBPxy4kXPfhh8cdtO6FrpjQmcZzFGHye','测试用户2',NULL,'USER',1,'2025-12-20 16:11:19','2025-12-22 20:40:03'),(5,'admin','$2a$10$ME.rnqL4AUPZ47Q0XWAd7uBPxy4kXPfhh8cdtO6FrpjQmcZzFGHye','管理员',NULL,'ADMIN',1,'2025-12-22 19:15:28','2025-12-22 19:27:12'),(6,'cjj','$2a$10$rmlaHujbQKZ12yI.cE.IROF7fHwDmIKSYem9OZTAb4wQ.b.tZGph2','cjj',NULL,'USER',1,'2025-12-22 20:40:22','2025-12-22 20:40:22'),(7,'112','$2a$10$mcg.Kksxud6OhdHIm59kNO54wAvc99sCsaCoksCRbSCukltE1B0wy','nihao',NULL,'USER',1,'2025-12-24 20:41:37','2025-12-24 20:41:37');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-24 21:36:51
