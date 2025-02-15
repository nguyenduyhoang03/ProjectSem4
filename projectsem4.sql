-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 15, 2025 lúc 02:42 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `projectsem4`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customworkouts`
--

CREATE TABLE `customworkouts` (
  `CustomWorkoutID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `goal` varchar(100) DEFAULT NULL,
  `custom_workoutid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `enum_tables`
--

CREATE TABLE `enum_tables` (
  `leaderboard_Rank_enum` enum('Bronze','Silver','Gold','Diamond') DEFAULT NULL,
  `notifications_Type_enum` enum('Reminder','NutritionAdvice') DEFAULT NULL,
  `pointstransactions_Type_enum` enum('Earn','Spend') DEFAULT NULL,
  `rewards_Type_enum` enum('Item','VIP') DEFAULT NULL,
  `users_AccountType_enum` enum('Basic','Intermediate','Professional') DEFAULT NULL,
  `vipsubscriptions_Status_enum` enum('Active','Expired') DEFAULT NULL,
  `workouts_Type_enum` enum('Push-ups','Sit-ups','Squats','Running') DEFAULT NULL,
  `workouts_Difficulty_enum` enum('Easy','Medium','Hard','Extreme') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `healthdata`
--

CREATE TABLE `healthdata` (
  `HealthDataID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `HealthMetrics` text NOT NULL,
  `health_dataid` int(11) NOT NULL,
  `health_metrics` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `history`
--

CREATE TABLE `history` (
  `HistoryID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `Activity` varchar(255) NOT NULL,
  `Date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `leaderboard`
--

CREATE TABLE `leaderboard` (
  `UserID` int(11) NOT NULL,
  `LifePoints` int(11) DEFAULT 0,
  `Power` int(11) DEFAULT 0,
  `rank` varchar(100) DEFAULT 'Bronze',
  `life_points` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notifications`
--

CREATE TABLE `notifications` (
  `NotificationID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `content` varchar(100) NOT NULL,
  `type` varchar(100) NOT NULL,
  `Date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `payments`
--

CREATE TABLE `payments` (
  `PaymentID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `RewardID` int(11) NOT NULL,
  `Amount` decimal(10,2) NOT NULL,
  `Date` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `pointstransactions`
--

CREATE TABLE `pointstransactions` (
  `TransactionID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `type` varchar(100) NOT NULL,
  `Points` int(11) NOT NULL,
  `Date` datetime DEFAULT current_timestamp(),
  `description` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `posts`
--

CREATE TABLE `posts` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `type` enum('COMMENT','LIKE','POST') DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `posts`
--

INSERT INTO `posts` (`id`, `content`, `created_at`, `parent_id`, `type`, `user_id`, `title`) VALUES
(2, 'Tập gym giúp cải thiện sức khỏe, tăng cường cơ bắp và giúp tinh thần sảng khoái hơn mỗi ngày.', '2025-02-13 01:43:00.000000', NULL, 'POST', NULL, 'Lợi Ích Của Việc Tập Gym Mỗi Ngày'),
(3, 'Tập gym giúp cải thiện sức khỏe, tăng cường cơ bắp và giúp tinh thần sảng khoái hơn mỗi ngày.', '2025-02-13 01:51:44.000000', NULL, 'POST', NULL, 'EXAMPLE'),
(4, 'Tập gym giúp cải thiện sức khỏe, tăng cường cơ bắp và giúp tinh thần sảng khoái hơn mỗi ngày.', '2025-02-13 01:51:50.000000', NULL, 'POST', NULL, 'EXAMPLE'),
(5, 'Tập gym giúp cải thiện sức khỏe, tăng cường cơ bắp và giúp tinh thần sảng khoái hơn mỗi ngày.', '2025-02-13 10:42:37.000000', NULL, 'POST', NULL, 'EXAMPLE');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post_img_url`
--

CREATE TABLE `post_img_url` (
  `post_id` bigint(20) NOT NULL,
  `img_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `post_img_url`
--

INSERT INTO `post_img_url` (`post_id`, `img_url`) VALUES
(2, 'https://example.com/gym1.jpg'),
(2, 'https://example.com/gym2.jpg'),
(3, 'https://example.com/gym1.jpg'),
(3, 'https://example.com/gym2.jpg'),
(4, 'https://example.com/gym1.jpg'),
(4, 'https://example.com/gym2.jpg'),
(5, 'https://example.com/gym1.jpg'),
(5, 'https://example.com/gym2.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `progress`
--

CREATE TABLE `progress` (
  `ProgressID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `WorkoutID` int(11) NOT NULL,
  `Date` date NOT NULL,
  `Strength` int(11) DEFAULT NULL,
  `Endurance` int(11) DEFAULT NULL,
  `Health` int(11) DEFAULT NULL,
  `Agility` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `rewards`
--

CREATE TABLE `rewards` (
  `RewardID` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `PointsRequired` int(11) DEFAULT NULL,
  `Price` decimal(10,2) DEFAULT NULL,
  `type` varchar(100) NOT NULL,
  `DurationMonths` int(11) DEFAULT NULL,
  `duration_months` int(11) DEFAULT NULL,
  `points_required` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `storeitems`
--

CREATE TABLE `storeitems` (
  `ItemID` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `PointsRequired` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `points_required` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `Email` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `AccountType` enum('Basic','Intermediate','Professional') DEFAULT 'Basic',
  `Points` int(11) DEFAULT 0,
  `Level` int(11) DEFAULT 1,
  `account_type` varchar(100) DEFAULT 'Basic'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`UserID`, `Name`, `Email`, `Password`, `AccountType`, `Points`, `Level`, `account_type`) VALUES
(1, 'Duy Hoàng', 'hoang123@gmail.com', '123456', 'Basic', NULL, NULL, NULL),
(166139826, 'Nguyễn Duy Hoàng', 'hoang11@gmail.com', '123456', 'Basic', NULL, NULL, NULL),
(287854638, 'Nguyen Duy Hoàng', '123456789@gmail.com', '12345678', 'Basic', NULL, NULL, NULL),
(338071797, 'Duy Hoàng', 'nguyenhoang1999@gmail.com', '$2a$10$XHKSxc0Ix/JjbMiSNRwr3eZeecbsZNA7eAziAqhXxU.UaRSmn.wzi', 'Basic', NULL, NULL, NULL),
(570876167, 'Nguyeexn Duy Hoàng', '123123123@gmail.com', '12345678', 'Basic', NULL, NULL, NULL),
(639897089, 'Duy Hoàng', 'n23052003@gmail.com', '$2a$10$s/ZkeH6kL36gWFl46QZYiOIKAEIC6MRZ/U4ahhdU.8nTUBVLKJ7aa', 'Basic', NULL, NULL, NULL),
(669955558, 'Nguyeexn Duy Hoàng', '123123@gmail.com', '12345678', 'Basic', NULL, NULL, NULL),
(806447209, 'admin', 'admin@gmail.com', '$2a$10$FrS82kk71hHxTVKrvR31VuyPbIKzZgjipgeNizoPnJPEGcJ4hp17e', 'Basic', NULL, NULL, NULL),
(951660217, 'Duy Hoàng', 'nguyenhoang@gmail.com', '$2a$10$7dEhHx3p8mhufGbEMGoebuMZ.YZ1I65KRV/hYdQFTbzW.KCoe8rDS', 'Basic', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user_roles`
--

CREATE TABLE `user_roles` (
  `user_userid` int(11) NOT NULL,
  `roles` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `user_roles`
--

INSERT INTO `user_roles` (`user_userid`, `roles`) VALUES
(338071797, 'USER'),
(806447209, 'ADMIN'),
(639897089, 'USER');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vipsubscriptions`
--

CREATE TABLE `vipsubscriptions` (
  `SubscriptionID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `RewardID` int(11) NOT NULL,
  `StartDate` date NOT NULL,
  `EndDate` date NOT NULL,
  `status` varchar(100) DEFAULT 'Active',
  `end_date` date NOT NULL,
  `start_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `workouts`
--

CREATE TABLE `workouts` (
  `WorkoutID` int(11) NOT NULL,
  `Name` varchar(255) NOT NULL,
  `type` varchar(100) NOT NULL,
  `difficulty` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `customworkouts`
--
ALTER TABLE `customworkouts`
  ADD PRIMARY KEY (`CustomWorkoutID`),
  ADD KEY `UserID` (`UserID`);

--
-- Chỉ mục cho bảng `healthdata`
--
ALTER TABLE `healthdata`
  ADD PRIMARY KEY (`HealthDataID`),
  ADD KEY `UserID` (`UserID`);

--
-- Chỉ mục cho bảng `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`HistoryID`),
  ADD KEY `UserID` (`UserID`);

--
-- Chỉ mục cho bảng `leaderboard`
--
ALTER TABLE `leaderboard`
  ADD PRIMARY KEY (`UserID`);

--
-- Chỉ mục cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`NotificationID`),
  ADD KEY `UserID` (`UserID`);

--
-- Chỉ mục cho bảng `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`PaymentID`),
  ADD KEY `UserID` (`UserID`),
  ADD KEY `RewardID` (`RewardID`);

--
-- Chỉ mục cho bảng `pointstransactions`
--
ALTER TABLE `pointstransactions`
  ADD PRIMARY KEY (`TransactionID`),
  ADD KEY `UserID` (`UserID`);

--
-- Chỉ mục cho bảng `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `post_img_url`
--
ALTER TABLE `post_img_url`
  ADD KEY `FKi3fxobdhgwkpex9vd57lc3gsb` (`post_id`);

--
-- Chỉ mục cho bảng `progress`
--
ALTER TABLE `progress`
  ADD PRIMARY KEY (`ProgressID`),
  ADD KEY `UserID` (`UserID`),
  ADD KEY `WorkoutID` (`WorkoutID`);

--
-- Chỉ mục cho bảng `rewards`
--
ALTER TABLE `rewards`
  ADD PRIMARY KEY (`RewardID`);

--
-- Chỉ mục cho bảng `storeitems`
--
ALTER TABLE `storeitems`
  ADD PRIMARY KEY (`ItemID`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Chỉ mục cho bảng `user_roles`
--
ALTER TABLE `user_roles`
  ADD KEY `FKd2jtfymh8fka6oh4q9p2689j` (`user_userid`);

--
-- Chỉ mục cho bảng `vipsubscriptions`
--
ALTER TABLE `vipsubscriptions`
  ADD PRIMARY KEY (`SubscriptionID`),
  ADD KEY `UserID` (`UserID`),
  ADD KEY `RewardID` (`RewardID`);

--
-- Chỉ mục cho bảng `workouts`
--
ALTER TABLE `workouts`
  ADD PRIMARY KEY (`WorkoutID`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `customworkouts`
--
ALTER TABLE `customworkouts`
  MODIFY `CustomWorkoutID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `healthdata`
--
ALTER TABLE `healthdata`
  MODIFY `HealthDataID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `history`
--
ALTER TABLE `history`
  MODIFY `HistoryID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `notifications`
--
ALTER TABLE `notifications`
  MODIFY `NotificationID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `payments`
--
ALTER TABLE `payments`
  MODIFY `PaymentID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `pointstransactions`
--
ALTER TABLE `pointstransactions`
  MODIFY `TransactionID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `posts`
--
ALTER TABLE `posts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `progress`
--
ALTER TABLE `progress`
  MODIFY `ProgressID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `rewards`
--
ALTER TABLE `rewards`
  MODIFY `RewardID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `storeitems`
--
ALTER TABLE `storeitems`
  MODIFY `ItemID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=956432629;

--
-- AUTO_INCREMENT cho bảng `vipsubscriptions`
--
ALTER TABLE `vipsubscriptions`
  MODIFY `SubscriptionID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `workouts`
--
ALTER TABLE `workouts`
  MODIFY `WorkoutID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `customworkouts`
--
ALTER TABLE `customworkouts`
  ADD CONSTRAINT `customworkouts_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `healthdata`
--
ALTER TABLE `healthdata`
  ADD CONSTRAINT `healthdata_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `history_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `leaderboard`
--
ALTER TABLE `leaderboard`
  ADD CONSTRAINT `leaderboard_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`RewardID`) REFERENCES `rewards` (`RewardID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `pointstransactions`
--
ALTER TABLE `pointstransactions`
  ADD CONSTRAINT `pointstransactions_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `post_img_url`
--
ALTER TABLE `post_img_url`
  ADD CONSTRAINT `FKi3fxobdhgwkpex9vd57lc3gsb` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

--
-- Các ràng buộc cho bảng `progress`
--
ALTER TABLE `progress`
  ADD CONSTRAINT `progress_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `progress_ibfk_2` FOREIGN KEY (`WorkoutID`) REFERENCES `workouts` (`WorkoutID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKd2jtfymh8fka6oh4q9p2689j` FOREIGN KEY (`user_userid`) REFERENCES `users` (`UserID`);

--
-- Các ràng buộc cho bảng `vipsubscriptions`
--
ALTER TABLE `vipsubscriptions`
  ADD CONSTRAINT `vipsubscriptions_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `vipsubscriptions_ibfk_2` FOREIGN KEY (`RewardID`) REFERENCES `rewards` (`RewardID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
