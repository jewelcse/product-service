ALTER TABLE products.products
ADD FOREIGN KEY (seller_id) REFERENCES products.sellers(id); 