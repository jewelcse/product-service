ALTER TABLE ecom_service_db.products
    ADD FOREIGN KEY (seller_id) REFERENCES ecom_service_db.sellers(id);