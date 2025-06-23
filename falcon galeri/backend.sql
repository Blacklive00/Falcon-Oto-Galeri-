USE `bitirme_projesi`;

-- Yabancı anahtar denetimlerini devre dışı bırakıyoruz
SET foreign_key_checks = 0;

-- Var olan tabloları sil
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `cart_item`;

-- Yabancı anahtar denetimlerini tekrar aç
SET foreign_key_checks = 1;

-- user tablosunu oluştur
CREATE TABLE `user` (
  `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` CHAR(80) NOT NULL,
  `email` VARCHAR(200) NOT NULL UNIQUE,
  `enabled` TINYINT NOT NULL DEFAULT 1,
  `reset_token` VARCHAR(255) DEFAULT NULL,
  `token_expiry_date` DATETIME(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- products tablosunu oluştur
CREATE TABLE `products` (
  `id` BIGINT(11) AUTO_INCREMENT PRIMARY KEY,
  `product_name` VARCHAR(100) NOT NULL,
  `stock` INT NOT NULL,
  `price` INT NOT NULL,
  `url` VARCHAR(255) NOT NULL,
  `info` VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- role tablosunu oluştur
CREATE TABLE `role` (
  `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- users_roles tablosunu oluştur
CREATE TABLE `users_roles` (
  `user_id` BIGINT(11) NOT NULL,
  `role_id` BIGINT(11) NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `FK_ROLE_idx` (`role_id`),
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Verileri ekle (user tablosu için)
INSERT INTO `user` (`username`, `password`, `email`, `enabled`, `reset_token`, `token_expiry_date`)
VALUES 
('emir', '$2a$10$Eig/Y4iKPihYplx3WK7ZMeuSHtx6Gj9ybEb4JcAR997qgXHUa4S.y', 'eogemir@gmail.com', 1, NULL, NULL),
('zeynep', '$2a$10$Eig/Y4iKPihYplx3WK7ZMeuSHtx6Gj9ybEb4JcAR997qgXHUa4S.y', 'zk020166@gmail.com', 1, NULL, NULL),
('hakan', '$2a$10$Eig/Y4iKPihYplx3WK7ZMeuSHtx6Gj9ybEb4JcAR997qgXHUa4S.y', 'babahakangs@gmail.com', 1, NULL, NULL);

-- Verileri ekle (role tablosu için)
INSERT INTO `role` (name)
VALUES 
('ROLE_USER'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

-- Verileri ekle (users_roles tablosu için)
INSERT INTO `users_roles` (user_id, role_id)
VALUES 
(1, 1), (2, 1), (2, 2), (3, 1), (1, 2), (1, 3);

-- cart tablosunu oluştur
CREATE TABLE `cart` (
  `id` BIGINT(11) AUTO_INCREMENT PRIMARY KEY,
  `user_id` BIGINT(11) UNIQUE NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- cart_item tablosunu oluştur
CREATE TABLE `cart_item` (
  `id` BIGINT(11) AUTO_INCREMENT PRIMARY KEY,
  `cart_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL,
  `quantity` INT NOT NULL CHECK (quantity > 0),
  FOREIGN KEY (`cart_id`) REFERENCES `cart`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

