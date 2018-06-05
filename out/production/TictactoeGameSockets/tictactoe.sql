-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 05, 2018 at 04:30 PM
-- Server version: 10.1.32-MariaDB
-- PHP Version: 7.2.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `tictactoe`
--

-- --------------------------------------------------------

--
-- Table structure for table `games`
--

CREATE TABLE `games` (
  `autoID` int(11) NOT NULL,
  `p1` int(11) DEFAULT NULL,
  `p2` int(11) DEFAULT NULL,
  `gameState` tinyint(4) DEFAULT '-1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `games`
--

INSERT INTO `games` (`autoID`, `p1`, `p2`, `gameState`) VALUES
(7, 15, 15, 1),
(8, 13, 15, 1),
(9, 15, 13, 1),
(10, 1, NULL, -1),
(12, 15, NULL, -1),
(14, 15, 15, 1),
(15, 13, 15, 1),
(16, 15, 15, 1),
(17, 15, 15, 1),
(19, 13, NULL, -1),
(23, 13, NULL, -1),
(24, 15, NULL, -1),
(26, 15, NULL, -1),
(27, 13, NULL, -1),
(29, 13, 15, 1),
(30, 13, NULL, -1),
(31, 13, 15, 1),
(32, 15, NULL, -1),
(33, 13, 15, 1),
(34, 15, 13, 1),
(35, 15, 13, 1),
(36, 15, 13, 1),
(37, 15, 13, 1),
(38, 13, 15, 1),
(39, 15, 13, 1),
(40, 15, 13, 1),
(42, 15, NULL, -1),
(43, 13, NULL, -1),
(44, 13, 15, 1),
(46, 15, NULL, -1),
(48, 13, NULL, -1),
(49, 15, NULL, -1),
(51, 15, NULL, -1),
(52, 13, NULL, -1),
(54, 13, NULL, -1),
(56, 13, NULL, -1),
(58, 13, NULL, -1),
(61, 13, NULL, -1),
(63, 13, NULL, -1),
(65, 15, NULL, -1),
(68, 13, 15, 1),
(69, 13, 15, 1),
(70, 13, 15, 1),
(72, 15, NULL, -1),
(73, 13, 15, 1),
(74, 13, 15, 1);

-- --------------------------------------------------------

--
-- Table structure for table `moves`
--

CREATE TABLE `moves` (
  `autoID` int(11) NOT NULL,
  `gID` int(11) NOT NULL,
  `pID` int(11) NOT NULL,
  `x` tinyint(4) DEFAULT NULL,
  `y` tinyint(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `moves`
--

INSERT INTO `moves` (`autoID`, `gID`, `pID`, `x`, `y`) VALUES
(43, 8, 13, 0, 0),
(44, 8, 15, 1, 1),
(45, 9, 15, 0, 0),
(46, 9, 13, 2, 0),
(52, 14, 15, 0, 0),
(53, 14, 15, 1, 1),
(54, 15, 13, 0, 2),
(55, 15, 15, 1, 1),
(56, 16, 15, 1, 1),
(57, 16, 15, 2, 1),
(58, 17, 15, 1, 1),
(59, 17, 15, 2, 0),
(94, 38, 13, 0, 0),
(166, 74, 13, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `autoID` int(11) NOT NULL,
  `name` varchar(15) NOT NULL,
  `surname` varchar(15) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(50) NOT NULL,
  `isactive` tinyint(4) DEFAULT '1',
  `access_lvl` tinyint(4) DEFAULT '1',
  `datejoined` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`autoID`, `name`, `surname`, `username`, `password`, `email`, `isactive`, `access_lvl`, `datejoined`) VALUES
(1, 'berkay', 'ersengun', 'berkayersengun', '*16BDC19438399DCF73FC3497845F8520375B9B34', 'berkayersengun@gmail.com', 1, 1, '2018-03-26 01:35:15'),
(2, 'eli', 'hart', 'elihart', '*ED033949CD52CB18FEB75010D0B0A8473036F02A', 'elihart@gmail.com', 1, 1, '2018-03-26 01:46:10'),
(3, 'mike', 'jag', 'mikejag', '*BA87F07589739DFA1FE583C58D25981AD7159F2C', 'mikejag@gmail.com', 1, 1, '2018-03-26 01:49:11'),
(4, 'jack', 'sparrow', 'jacksparrow', '*8D9E90F150C36E73EEE55F8E67DB5D041EF5119E', 'jacksparrow@gmail.com', 1, 1, '2018-03-26 17:11:28'),
(5, 'chip', 'munk', 'chipmunk', '*1B4BC466FAA07B85F9DF040F4AE80EC7043D7CFA', 'chipmunk@gmail.com', 1, 1, '2018-03-26 17:37:03'),
(6, 'foo', 'bar', 'foobar', '*A668070BBEF3F68C99DB581819A17584B771B1A3', 'foobar@gmail.com', 1, 1, '2018-03-26 20:14:10'),
(7, 'gordon', 'freeman', 'gordonfreeman', '*89B5F85710EAE1CD073C5CBB553BED37D6784A0B', 'gordonfreeman', 1, 1, '2018-03-26 23:41:16'),
(9, 'john', 'blah', 'johnblah', '*4CDEC13E804CB2A54F15FB4562ED69B33C18BCD6', 'johnblah@gmail.com', 1, 1, '2018-03-28 19:45:08'),
(13, 'e', 'r', 'q', '*19A4904A05D0ECDE0903BCF4B5072C531715E2E5', 'v', 1, 1, '2018-03-29 23:13:49'),
(14, '1', '2', '3', '*908BE2B7EB7D7567F7FF98716850F59BA69AA9DB', '5', 1, 1, '2018-03-30 17:28:01'),
(15, '1', '', '', '', '', 1, 1, '2018-03-31 23:43:16'),
(16, '1', '2', '3', '*908BE2B7EB7D7567F7FF98716850F59BA69AA9DB', '1', 1, 1, '2018-03-31 23:49:40'),
(17, 'a', 's', 'f', '', '', 1, 1, '2018-04-01 18:08:10'),
(18, '123', '234', '345', '', '', 1, 1, '2018-04-01 18:11:13'),
(19, 't', 'y', 'u', '', '', 1, 1, '2018-04-01 18:12:50'),
(20, '1', '', '', '', '', 1, 1, '2018-04-01 18:13:22'),
(21, '', 'a', 's', '', '', 1, 1, '2018-04-01 18:52:52'),
(22, '8', '9', '0', '', '', 1, 1, '2018-04-01 18:54:57');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `games`
--
ALTER TABLE `games`
  ADD PRIMARY KEY (`autoID`),
  ADD KEY `p1` (`p1`),
  ADD KEY `p2` (`p2`);

--
-- Indexes for table `moves`
--
ALTER TABLE `moves`
  ADD PRIMARY KEY (`autoID`),
  ADD KEY `gID` (`gID`),
  ADD KEY `pID` (`pID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`autoID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `games`
--
ALTER TABLE `games`
  MODIFY `autoID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;

--
-- AUTO_INCREMENT for table `moves`
--
ALTER TABLE `moves`
  MODIFY `autoID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=259;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `autoID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `games`
--
ALTER TABLE `games`
  ADD CONSTRAINT `games_ibfk_1` FOREIGN KEY (`p1`) REFERENCES `users` (`autoID`),
  ADD CONSTRAINT `games_ibfk_2` FOREIGN KEY (`p2`) REFERENCES `users` (`autoID`);

--
-- Constraints for table `moves`
--
ALTER TABLE `moves`
  ADD CONSTRAINT `moves_ibfk_1` FOREIGN KEY (`gID`) REFERENCES `games` (`autoID`),
  ADD CONSTRAINT `moves_ibfk_2` FOREIGN KEY (`pID`) REFERENCES `users` (`autoID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
