CREATE TABLE `auction_item`
(
    `id`                    bigint              NOT NULL AUTO_INCREMENT,
    `name`                  varchar(128)        NOT NULL,
    `type`                  varchar(64)         NOT NULL,
    `description`           varchar(512),
    primary key (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
