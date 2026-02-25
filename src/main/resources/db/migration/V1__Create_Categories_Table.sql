CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    parent_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    
    CONSTRAINT fk_category_parent
        FOREIGN KEY (parent_id) 
        REFERENCES categories(id)
);

-- Create indexes
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_categories_parent ON categories(parent_id);
CREATE INDEX idx_categories_active ON categories(is_active);

-- Insert root categories
INSERT INTO categories (name, description, is_active) VALUES 
('Electronics', 'Electronic devices and accessories', true),
('Clothing', 'Apparel and fashion items', true),
('Books', 'Books and educational materials', true),
('Home & Garden', 'Home improvement and garden supplies', true);