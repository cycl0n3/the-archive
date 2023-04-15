CREATE TABLE `customer` (
  `id`   INTEGER NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(128) NOT NULL,
  `last_name` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `notification` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `customer_id` INTEGER NOT NULL,
  `created_date` DATETIME NOT NULL DEFAULT NOW(),
  `read_date` DATETIME NULL,
  `title` VARCHAR(128) NOT NULL,
  `description` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`)
);
