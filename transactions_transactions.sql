-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: transactions
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `description` varchar(255) NOT NULL,
  `amount` double NOT NULL,
  `is_expense` tinyint(1) NOT NULL,
  `date` mediumtext,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `login`.`users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,1,'Groceries',50,1,'2023-05-03'),(2,2,'Gaming',500,1,'2023-05-03'),(4,2,'Gaming',90,1,'2023-05-03'),(5,2,'Groceries',45.23,1,'2023-05-02'),(6,3,'Clothing',89.99,1,'2023-05-01'),(7,1,'Entertainment',25.5,1,'2023-04-29'),(8,2,'Travel',320,1,'2023-04-30'),(9,3,'Electronics',1200,1,'2023-04-28'),(10,1,'Groceries',65.75,1,'2023-04-27'),(11,2,'Gasoline',50,1,'2023-04-25'),(12,3,'Home Improvement',150,1,'2023-04-26'),(13,1,'Dining Out',75,1,'2023-04-24'),(14,2,'Books',20,1,'2023-04-23'),(15,3,'Healthcare',350,1,'2023-04-22'),(16,1,'Clothing',100,1,'2023-04-21'),(17,2,'Gifts',75,1,'2023-04-20'),(18,3,'Sports Equipment',200,1,'2023-04-19'),(19,1,'Entertainment',50,1,'2023-04-18'),(20,2,'Travel',450,1,'2023-04-17'),(21,3,'Home Improvement',75,1,'2023-04-16'),(23,2,'Entertainment',122,1,NULL),(24,2,'Videogame',100,0,NULL),(25,2,'Groceries',77,1,NULL);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-03  1:23:52
