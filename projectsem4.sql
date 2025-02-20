-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th2 20, 2025 lúc 03:11 PM
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
  `custom_workoutid` int(11) NOT NULL,
  `description` tinytext DEFAULT NULL,
  `goal` tinytext DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `userid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `healthdata`
--

CREATE TABLE `healthdata` (
  `health_dataid` int(11) NOT NULL,
  `date` date NOT NULL,
  `health_metrics` tinytext NOT NULL,
  `userid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `history`
--

CREATE TABLE `history` (
  `historyid` int(11) NOT NULL,
  `activity` varchar(255) NOT NULL,
  `date` datetime(6) DEFAULT current_timestamp(6),
  `userid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `invalidated_token`
--

CREATE TABLE `invalidated_token` (
  `id` varchar(255) NOT NULL,
  `expiry_time` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `leaderboard`
--

CREATE TABLE `leaderboard` (
  `userid` int(11) NOT NULL,
  `life_points` int(11) DEFAULT 0,
  `power` int(11) DEFAULT 0,
  `rank` tinytext DEFAULT 'Bronze'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notifications`
--

CREATE TABLE `notifications` (
  `notificationid` int(11) NOT NULL,
  `content` tinytext NOT NULL,
  `date` datetime(6) DEFAULT current_timestamp(6),
  `type` tinytext NOT NULL,
  `userid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `payments`
--

CREATE TABLE `payments` (
  `paymentid` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `date` datetime(6) DEFAULT current_timestamp(6),
  `rewardid` int(11) NOT NULL,
  `userid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `permission`
--

CREATE TABLE `permission` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `permission`
--

INSERT INTO `permission` (`name`, `description`) VALUES
('CREATE_DATA', 'Read data permission'),
('DELELE_DATA', 'Read data permission'),
('READ_DATA', 'Read data permission'),
('UPDATE_DATA', 'Read data permission');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `pointstransactions`
--

CREATE TABLE `pointstransactions` (
  `transactionid` int(11) NOT NULL,
  `date` datetime(6) DEFAULT current_timestamp(6),
  `description` tinytext DEFAULT NULL,
  `points` int(11) NOT NULL,
  `type` tinytext NOT NULL,
  `userid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `posts`
--

CREATE TABLE `posts` (
  `id` bigint(20) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `posts`
--

INSERT INTO `posts` (`id`, `content`, `created_at`, `title`, `user_id`) VALUES
(1, 'gym giúp cải thiện sức khỏe, tăng cường cơ bắp và giúp tinh thần sảng khoái hơn mỗi ngày.', '2025-02-19 16:30:07.000000', 'Đây là Bài Viết ID 10', 426515034);

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
(1, 'https://example.com/gym1.jpg'),
(1, 'https://example.com/gym2.jpg');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `post_video_url`
--

CREATE TABLE `post_video_url` (
  `post_id` bigint(20) NOT NULL,
  `video_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `post_video_url`
--

INSERT INTO `post_video_url` (`post_id`, `video_url`) VALUES
(1, 'https://www.youtube.com/watch?v=1V7U1XQ-eSw&t=1120s'),
(1, 'https://www.youtube.com/watch?v=1V7U1XQ-eSw&t=1120s');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `progress`
--

CREATE TABLE `progress` (
  `progressid` int(11) NOT NULL,
  `agility` int(11) DEFAULT NULL,
  `date` date NOT NULL,
  `endurance` int(11) DEFAULT NULL,
  `health` int(11) DEFAULT NULL,
  `strength` int(11) DEFAULT NULL,
  `userid` bigint(20) NOT NULL,
  `workoutid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `rewards`
--

CREATE TABLE `rewards` (
  `rewardid` int(11) NOT NULL,
  `duration_months` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `points_required` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `type` tinytext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

CREATE TABLE `role` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `role`
--

INSERT INTO `role` (`name`, `description`) VALUES
('ADMIN', 'ADMIN role'),
('USER', 'ADMIN role');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role_permissions`
--

CREATE TABLE `role_permissions` (
  `role_name` varchar(255) NOT NULL,
  `permissions_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `role_permissions`
--

INSERT INTO `role_permissions` (`role_name`, `permissions_name`) VALUES
('ADMIN', 'CREATE_DATA'),
('ADMIN', 'DELELE_DATA'),
('ADMIN', 'READ_DATA'),
('ADMIN', 'UPDATE_DATA'),
('USER', 'READ_DATA');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `storeitems`
--

CREATE TABLE `storeitems` (
  `itemid` int(11) NOT NULL,
  `description` tinytext DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `points_required` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `userid` bigint(20) NOT NULL,
  `account_type` tinytext DEFAULT 'Basic',
  `email` varchar(255) NOT NULL,
  `level` int(11) DEFAULT 1,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `points` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`userid`, `account_type`, `email`, `level`, `name`, `password`, `points`) VALUES
(426515034, NULL, 'admin@gmail.com', NULL, 'Nguyen Duy Hoang', '$2a$10$j2EBbFTFZbOvOw.fY4mlKeE19/IHh8yqGiugy4zp..8cKDsLWE6xe', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users_roles`
--

CREATE TABLE `users_roles` (
  `user_userid` bigint(20) NOT NULL,
  `roles_name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `users_roles`
--

INSERT INTO `users_roles` (`user_userid`, `roles_name`) VALUES
(426515034, 'ADMIN');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vipsubscriptions`
--

CREATE TABLE `vipsubscriptions` (
  `subscriptionid` int(11) NOT NULL,
  `end_date` date NOT NULL,
  `start_date` date NOT NULL,
  `status` tinytext DEFAULT 'Active',
  `rewardid` int(11) NOT NULL,
  `userid` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `workouts`
--

CREATE TABLE `workouts` (
  `workoutid` int(11) NOT NULL,
  `difficulty` tinytext NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` tinytext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `customworkouts`
--
ALTER TABLE `customworkouts`
  ADD PRIMARY KEY (`custom_workoutid`),
  ADD KEY `FKdjeoo2m9b8i108hn9q42nxg7l` (`userid`);

--
-- Chỉ mục cho bảng `healthdata`
--
ALTER TABLE `healthdata`
  ADD PRIMARY KEY (`health_dataid`),
  ADD KEY `FKolpt86qhw2ua67slq9ss2glxx` (`userid`);

--
-- Chỉ mục cho bảng `history`
--
ALTER TABLE `history`
  ADD PRIMARY KEY (`historyid`),
  ADD KEY `FKmb7t8prh90uivujo2ml3bsr6l` (`userid`);

--
-- Chỉ mục cho bảng `invalidated_token`
--
ALTER TABLE `invalidated_token`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `leaderboard`
--
ALTER TABLE `leaderboard`
  ADD PRIMARY KEY (`userid`);

--
-- Chỉ mục cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`notificationid`),
  ADD KEY `FKtockhlhmgah7lpxrernp6a34` (`userid`);

--
-- Chỉ mục cho bảng `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`paymentid`),
  ADD KEY `FKl00mixmsrje2fp2lsplhqe68y` (`rewardid`),
  ADD KEY `FK1jxw5xo4c3brnjc065ypc7j0m` (`userid`);

--
-- Chỉ mục cho bảng `permission`
--
ALTER TABLE `permission`
  ADD PRIMARY KEY (`name`);

--
-- Chỉ mục cho bảng `pointstransactions`
--
ALTER TABLE `pointstransactions`
  ADD PRIMARY KEY (`transactionid`),
  ADD KEY `FK8drylpod8jq2vs4br56r8wgww` (`userid`);

--
-- Chỉ mục cho bảng `posts`
--
ALTER TABLE `posts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5lidm6cqbc7u4xhqpxm898qme` (`user_id`);

--
-- Chỉ mục cho bảng `post_img_url`
--
ALTER TABLE `post_img_url`
  ADD KEY `FKi3fxobdhgwkpex9vd57lc3gsb` (`post_id`);

--
-- Chỉ mục cho bảng `post_video_url`
--
ALTER TABLE `post_video_url`
  ADD KEY `FK8nt7naqterv6byel4kid3opeu` (`post_id`);

--
-- Chỉ mục cho bảng `progress`
--
ALTER TABLE `progress`
  ADD PRIMARY KEY (`progressid`),
  ADD KEY `FK42bl2jfhbklced2jmsg1aubss` (`userid`),
  ADD KEY `FKrnkc2amm5usmtg8d0w4g20pgl` (`workoutid`);

--
-- Chỉ mục cho bảng `rewards`
--
ALTER TABLE `rewards`
  ADD PRIMARY KEY (`rewardid`);

--
-- Chỉ mục cho bảng `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`name`);

--
-- Chỉ mục cho bảng `role_permissions`
--
ALTER TABLE `role_permissions`
  ADD PRIMARY KEY (`role_name`,`permissions_name`),
  ADD KEY `FKf5aljih4mxtdgalvr7xvngfn1` (`permissions_name`);

--
-- Chỉ mục cho bảng `storeitems`
--
ALTER TABLE `storeitems`
  ADD PRIMARY KEY (`itemid`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userid`);

--
-- Chỉ mục cho bảng `users_roles`
--
ALTER TABLE `users_roles`
  ADD PRIMARY KEY (`user_userid`,`roles_name`),
  ADD KEY `FK7tacasmhqivyolfjjxseeha5c` (`roles_name`);

--
-- Chỉ mục cho bảng `vipsubscriptions`
--
ALTER TABLE `vipsubscriptions`
  ADD PRIMARY KEY (`subscriptionid`),
  ADD KEY `FKls982g5x9mqw28ugndgm5gego` (`rewardid`),
  ADD KEY `FKhgip502b7idcusxmxaoyow7ak` (`userid`);

--
-- Chỉ mục cho bảng `workouts`
--
ALTER TABLE `workouts`
  ADD PRIMARY KEY (`workoutid`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `posts`
--
ALTER TABLE `posts`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `customworkouts`
--
ALTER TABLE `customworkouts`
  ADD CONSTRAINT `FKdjeoo2m9b8i108hn9q42nxg7l` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `healthdata`
--
ALTER TABLE `healthdata`
  ADD CONSTRAINT `FKolpt86qhw2ua67slq9ss2glxx` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `history`
--
ALTER TABLE `history`
  ADD CONSTRAINT `FKmb7t8prh90uivujo2ml3bsr6l` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `FKtockhlhmgah7lpxrernp6a34` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `FK1jxw5xo4c3brnjc065ypc7j0m` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKl00mixmsrje2fp2lsplhqe68y` FOREIGN KEY (`rewardid`) REFERENCES `rewards` (`rewardid`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `pointstransactions`
--
ALTER TABLE `pointstransactions`
  ADD CONSTRAINT `FK8drylpod8jq2vs4br56r8wgww` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `posts`
--
ALTER TABLE `posts`
  ADD CONSTRAINT `FK5lidm6cqbc7u4xhqpxm898qme` FOREIGN KEY (`user_id`) REFERENCES `users` (`userid`);

--
-- Các ràng buộc cho bảng `post_img_url`
--
ALTER TABLE `post_img_url`
  ADD CONSTRAINT `FKi3fxobdhgwkpex9vd57lc3gsb` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

--
-- Các ràng buộc cho bảng `post_video_url`
--
ALTER TABLE `post_video_url`
  ADD CONSTRAINT `FK8nt7naqterv6byel4kid3opeu` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`);

--
-- Các ràng buộc cho bảng `progress`
--
ALTER TABLE `progress`
  ADD CONSTRAINT `FK42bl2jfhbklced2jmsg1aubss` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKrnkc2amm5usmtg8d0w4g20pgl` FOREIGN KEY (`workoutid`) REFERENCES `workouts` (`workoutid`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `role_permissions`
--
ALTER TABLE `role_permissions`
  ADD CONSTRAINT `FKcppvu8fk24eqqn6q4hws7ajux` FOREIGN KEY (`role_name`) REFERENCES `role` (`name`),
  ADD CONSTRAINT `FKf5aljih4mxtdgalvr7xvngfn1` FOREIGN KEY (`permissions_name`) REFERENCES `permission` (`name`);

--
-- Các ràng buộc cho bảng `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `FK7tacasmhqivyolfjjxseeha5c` FOREIGN KEY (`roles_name`) REFERENCES `role` (`name`),
  ADD CONSTRAINT `FKa447bcrqmmjlpn3gnn89gstbl` FOREIGN KEY (`user_userid`) REFERENCES `users` (`userid`);

--
-- Các ràng buộc cho bảng `vipsubscriptions`
--
ALTER TABLE `vipsubscriptions`
  ADD CONSTRAINT `FKhgip502b7idcusxmxaoyow7ak` FOREIGN KEY (`userid`) REFERENCES `users` (`userid`) ON DELETE CASCADE,
  ADD CONSTRAINT `FKls982g5x9mqw28ugndgm5gego` FOREIGN KEY (`rewardid`) REFERENCES `rewards` (`rewardid`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
