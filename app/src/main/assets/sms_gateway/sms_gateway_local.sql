-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Aug 11, 2018 at 06:49 PM
-- Server version: 10.1.30-MariaDB
-- PHP Version: 5.6.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sms_gateway_local`
--

-- --------------------------------------------------------

--
-- Table structure for table `data_inbox`
--

CREATE TABLE `data_inbox` (
  `id` int(11) NOT NULL,
  `waktu` datetime NOT NULL,
  `nomor` char(50) NOT NULL,
  `pesan` varchar(222) NOT NULL,
  `status` enum('new','sent') NOT NULL,
  `update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `generated_id` char(222) NOT NULL,
  `email` char(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_inbox`
--

INSERT INTO `data_inbox` (`id`, `waktu`, `nomor`, `pesan`, `status`, `update`, `generated_id`, `email`) VALUES
(1, '2018-08-11 06:57:46', ' 6285207117555', 'Oke', 'sent', '2018-08-11 04:57:46', '1b7a3ef', 'djsamm88.web.id@gmail.com'),
(2, '2018-08-11 06:59:07', ' 6285207117555', 'Solved', 'sent', '2018-08-11 04:59:07', '57edec1', 'djsamm88.web.id@gmail.com'),
(3, '2018-08-11 18:26:28', ' 6285207117555', 'Treatatst', 'sent', '2018-08-11 16:26:28', '1371a72', 'djsamm88.web.id@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `data_outbox`
--

CREATE TABLE `data_outbox` (
  `id` int(11) NOT NULL,
  `waktu` datetime NOT NULL,
  `nomor` char(50) NOT NULL,
  `pesan` varchar(222) NOT NULL,
  `status` enum('new','sent') NOT NULL,
  `update` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `generated_id` char(55) NOT NULL,
  `email` char(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_outbox`
--

INSERT INTO `data_outbox` (`id`, `waktu`, `nomor`, `pesan`, `status`, `update`, `generated_id`, `email`) VALUES
(1, '2018-08-09 00:00:00', '085207117555', 'testing', 'sent', '2018-08-11 04:58:17', '', ''),
(3, '2018-08-10 00:00:00', '085207117555', 'hell yeah..\r\nallcool...', 'sent', '2018-08-09 09:05:43', '', ''),
(4, '2018-08-15 00:00:00', '085207117555', 'Horas boss....', 'sent', '2018-08-09 09:30:43', '', ''),
(7, '2018-08-11 00:00:00', '085207117555', 'Okeh solved', 'sent', '2018-08-11 16:27:17', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `data_inbox`
--
ALTER TABLE `data_inbox`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `data_outbox`
--
ALTER TABLE `data_outbox`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `data_inbox`
--
ALTER TABLE `data_inbox`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `data_outbox`
--
ALTER TABLE `data_outbox`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
