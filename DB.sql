-- --------------------------------------------------------
-- Máy chủ:                      localhost
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Phiên bản:           12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table projectsem4.customworkouts
CREATE TABLE IF NOT EXISTS `customworkouts` (
  `CustomWorkoutID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `description` tinytext DEFAULT NULL,
  `goal` tinytext DEFAULT NULL,
  `custom_workoutid` int(11) NOT NULL,
  PRIMARY KEY (`CustomWorkoutID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `customworkouts_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.enum_tables
CREATE TABLE IF NOT EXISTS `enum_tables` (
  `leaderboard_Rank_enum` enum('Bronze','Silver','Gold','Diamond') DEFAULT NULL,
  `notifications_Type_enum` enum('Reminder','NutritionAdvice') DEFAULT NULL,
  `pointstransactions_Type_enum` enum('Earn','Spend') DEFAULT NULL,
  `rewards_Type_enum` enum('Item','VIP') DEFAULT NULL,
  `users_AccountType_enum` enum('Basic','Intermediate','Professional') DEFAULT NULL,
  `vipsubscriptions_Status_enum` enum('Active','Expired') DEFAULT NULL,
  `workouts_Type_enum` enum('Push-ups','Sit-ups','Squats','Running') DEFAULT NULL,
  `workouts_Difficulty_enum` enum('Easy','Medium','Hard','Extreme') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.healthdata
CREATE TABLE IF NOT EXISTS `healthdata` (
  `HealthDataID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `HealthMetrics` text NOT NULL,
  `health_dataid` int(11) NOT NULL,
  `health_metrics` tinytext NOT NULL,
  PRIMARY KEY (`HealthDataID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `healthdata_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.history
CREATE TABLE IF NOT EXISTS `history` (
  `HistoryID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `Activity` varchar(255) NOT NULL,
  `Date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`HistoryID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `history_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.leaderboard
CREATE TABLE IF NOT EXISTS `leaderboard` (
  `UserID` int(11) NOT NULL,
  `LifePoints` int(11) DEFAULT 0,
  `Power` int(11) DEFAULT 0,
  `rank` tinytext DEFAULT 'Bronze',
  `life_points` int(11) DEFAULT 0,
  PRIMARY KEY (`UserID`),
  CONSTRAINT `leaderboard_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.notifications
CREATE TABLE IF NOT EXISTS `notifications` (
  `NotificationID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `content` tinytext NOT NULL,
  `type` tinytext NOT NULL,
  `Date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`NotificationID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.payments
CREATE TABLE IF NOT EXISTS `payments` (
  `PaymentID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `RewardID` int(11) NOT NULL,
  `Amount` decimal(10,2) NOT NULL,
  `Date` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`PaymentID`),
  KEY `UserID` (`UserID`),
  KEY `RewardID` (`RewardID`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`RewardID`) REFERENCES `rewards` (`RewardID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.pointstransactions
CREATE TABLE IF NOT EXISTS `pointstransactions` (
  `TransactionID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `type` tinytext NOT NULL,
  `Points` int(11) NOT NULL,
  `Date` datetime DEFAULT current_timestamp(),
  `description` tinytext DEFAULT NULL,
  PRIMARY KEY (`TransactionID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `pointstransactions_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.posts
CREATE TABLE IF NOT EXISTS `posts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `type` enum('COMMENT','LIKE','POST') DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.post_img_url
CREATE TABLE IF NOT EXISTS `post_img_url` (
  `post_id` bigint(20) NOT NULL,
  `img_url` varchar(255) DEFAULT NULL,
  KEY `FKi3fxobdhgwkpex9vd57lc3gsb` (`post_id`),
  CONSTRAINT `FKi3fxobdhgwkpex9vd57lc3gsb` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.progress
CREATE TABLE IF NOT EXISTS `progress` (
  `ProgressID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `WorkoutID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Strength` int(11) DEFAULT NULL,
  `Endurance` int(11) DEFAULT NULL,
  `Health` int(11) DEFAULT NULL,
  `Agility` int(11) DEFAULT NULL,
  PRIMARY KEY (`ProgressID`),
  KEY `UserID` (`UserID`),
  KEY `WorkoutID` (`WorkoutID`),
  CONSTRAINT `progress_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  CONSTRAINT `progress_ibfk_2` FOREIGN KEY (`WorkoutID`) REFERENCES `workouts` (`WorkoutID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.rewards
CREATE TABLE IF NOT EXISTS `rewards` (
  `RewardID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `PointsRequired` int(11) DEFAULT NULL,
  `Price` decimal(10,2) DEFAULT NULL,
  `type` tinytext NOT NULL,
  `DurationMonths` int(11) DEFAULT NULL,
  `duration_months` int(11) DEFAULT NULL,
  `points_required` int(11) DEFAULT NULL,
  PRIMARY KEY (`RewardID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.storeitems
CREATE TABLE IF NOT EXISTS `storeitems` (
  `ItemID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `PointsRequired` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `description` tinytext DEFAULT NULL,
  `points_required` int(11) NOT NULL,
  PRIMARY KEY (`ItemID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.users
CREATE TABLE IF NOT EXISTS `users` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `AccountType` enum('Basic','Intermediate','Professional') DEFAULT 'Basic',
  `Points` int(11) DEFAULT 0,
  `Level` int(11) DEFAULT 1,
  `account_type` tinytext DEFAULT 'Basic',
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=956432629 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.user_roles
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_userid` int(11) NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  KEY `FKd2jtfymh8fka6oh4q9p2689j` (`user_userid`),
  CONSTRAINT `FKd2jtfymh8fka6oh4q9p2689j` FOREIGN KEY (`user_userid`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.vipsubscriptions
CREATE TABLE IF NOT EXISTS `vipsubscriptions` (
  `SubscriptionID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `RewardID` int(11) NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `status` tinytext DEFAULT 'Active',
  `end_date` date NOT NULL,
  `start_date` date NOT NULL,
  PRIMARY KEY (`SubscriptionID`),
  KEY `UserID` (`UserID`),
  KEY `RewardID` (`RewardID`),
  CONSTRAINT `vipsubscriptions_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  CONSTRAINT `vipsubscriptions_ibfk_2` FOREIGN KEY (`RewardID`) REFERENCES `rewards` (`RewardID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table projectsem4.workouts
CREATE TABLE IF NOT EXISTS `workouts` (
  `WorkoutID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) NOT NULL,
  `type` tinytext NOT NULL,
  `difficulty` tinytext NOT NULL,
  PRIMARY KEY (`WorkoutID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Data exporting was unselected.

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
