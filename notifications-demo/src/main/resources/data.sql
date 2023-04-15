INSERT INTO `customer` (`first_name`, `last_name`) VALUES ('Philip', 'Adams');
INSERT INTO `customer` (`first_name`, `last_name`) VALUES ('Kemp', 'Snider');
INSERT INTO `customer` (`first_name`, `last_name`) VALUES ('Daniel', 'Ortega');
INSERT INTO `customer` (`first_name`, `last_name`) VALUES ('Magnus', 'Alexander');
INSERT INTO `customer` (`first_name`, `last_name`) VALUES ('Ivory', 'Bird');

INSERT INTO `notification` (`customer_id`, `title`, `description`) 
  VALUES (1, 'AAA', 'aaa');
INSERT INTO `notification` (`customer_id`, `title`, `description`) 
  VALUES (1, 'BBB', 'bbb');
INSERT INTO `notification` (`customer_id`, `title`, `description`) 
  VALUES (1, 'CCC', 'ccc');

INSERT INTO `notification` (`customer_id`, `title`, `description`) 
  VALUES (2, 'AAA', 'aaa');
INSERT INTO `notification` (`customer_id`, `title`, `description`) 
  VALUES (2, 'BBB', 'bbb');

INSERT INTO `notification` (`customer_id`, `title`, `description`) 
  VALUES (3, 'AAA', 'aaa');
