alter table `auction_commission_order` add column `auction_item_id` bigint not null;
alter table `auction_commission_order` add column `is_transaction_paid` boolean default false;
alter table `auction_commission_order` add column `is_appraisal_application_submitted` boolean default false;
