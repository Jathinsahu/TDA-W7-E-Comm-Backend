CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    category_id BIGINT NOT NULL,
    image_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) 
        REFERENCES categories(id)
);

-- Create indexes
CREATE INDEX idx_products_name ON products(name);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_active ON products(is_active);
CREATE INDEX idx_products_price ON products(price);

-- Insert sample products
INSERT INTO products (name, description, price, stock, category_id, is_active) VALUES
('iPhone 15 Pro', 'Latest Apple smartphone with advanced camera system', 999.99, 50, 1, true),
('Samsung Galaxy S24', 'Flagship Android smartphone with AI features', 899.99, 30, 1, true),
('MacBook Pro 14"', 'Professional laptop for developers and creators', 1999.99, 20, 1, true),
('Cotton T-Shirt', 'Comfortable cotton t-shirt in various colors', 29.99, 100, 2, true),
('Denim Jeans', 'Classic denim jeans for everyday wear', 79.99, 75, 2, true),
('Spring Gardening Kit', 'Complete gardening tools for spring planting', 49.99, 40, 4, true);