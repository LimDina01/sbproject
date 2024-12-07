CREATE TABLE payment_types
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    payment_type_name VARCHAR(100),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);