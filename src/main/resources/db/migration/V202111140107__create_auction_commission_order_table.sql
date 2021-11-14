CREATE TABLE `auction_commission_order`
(
    `id`                    bigint          NOT NULL AUTO_INCREMENT,
    `bank_account`          varchar(20)     NOT NULL,
    `transaction_amount`    decimal(20,2),
    primary key (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
