-- Tabla book
CREATE TABLE book (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publication_date DATE,
    category VARCHAR(100),
    isbn VARCHAR(20) UNIQUE NOT NULL,
    rating INTEGER CHECK (rating BETWEEN 1 AND 5),
    visible BOOLEAN DEFAULT TRUE,
    stock INTEGER DEFAULT 0,

    -- Auditoría
    created_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
