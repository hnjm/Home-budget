-- MySQL Workbench Synchronization
-- Generated: 2015-04-17 11:44
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: Dominik

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `u906935981_pz` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;

CREATE TABLE IF NOT EXISTS `u906935981_pz`.`user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `token` VARCHAR(32) NOT NULL,
  `savings` DOUBLE NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `u906935981_pz`.`category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_user` INT(11) NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `type` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_category_user_idx` (`id_user` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `u906935981_pz`.`element` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_category` INT(11) NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `value` DOUBLE NULL DEFAULT NULL,
  `const` TINYINT(1) NULL DEFAULT NULL,
  `date` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_element_category_idx` (`id_category` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;

CREATE TABLE IF NOT EXISTS `u906935981_pz`.`settings` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_user` INT(11) NULL DEFAULT NULL,
  `auto_delete` TINYINT(1) NULL DEFAULT NULL,
  `auto_savings` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_settings_user_idx_idx` (`id_user` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_general_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
