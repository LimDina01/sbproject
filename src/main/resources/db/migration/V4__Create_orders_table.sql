CREATE TABLE orders
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT,
    total_amount DECIMAL(10, 2) NOT NULL,
    status       VARCHAR(50) DEFAULT 'Pending',
    created_at   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);