CREATE TABLE positions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(64) UNIQUE NOT NULL,
    description TEXT 
);

CREATE TABLE employees (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(64) NOT NULL,
    middle_name VARCHAR(64), 
    last_name VARCHAR(64) NOT NULL,
    birth_date DATE,
    phone_number VARCHAR(20),
    email VARCHAR(128) UNIQUE NOT NULL,
    address VARCHAR(255), 
    position_id UUID NOT NULL,
    employment_status VARCHAR(32) CHECK (employment_status IN ('active', 'on leave', 'terminated')),
    hire_date TIMESTAMP,  
    termination_date TIMESTAMP, 
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  
    FOREIGN KEY (position_id) REFERENCES positions(id)  
);


CREATE TABLE readers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    fullname VARCHAR(128) NOT NULL,
    email VARCHAR(128) UNIQUE NOT NULL,  
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE authors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(64) NOT NULL,
    middle_name VARCHAR(64),
    last_name VARCHAR(64),
    birth_year INT  
);

CREATE TABLE books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    author_id UUID NOT NULL,
    published_year INT,  
    available_copies INT CHECK (available_copies >= 0),  
    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);


CREATE TABLE genres (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(64) UNIQUE NOT NULL
);


CREATE TABLE book_genres (
    book_id UUID NOT NULL,
    genre_id UUID NOT NULL,
    PRIMARY KEY (book_id, genre_id),
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);


CREATE TABLE borrowings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reader_id UUID NOT NULL,
    book_id UUID NOT NULL,
    borrowed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date DATE,  
    returned_at TIMESTAMP CHECK (returned_at IS NULL OR returned_at >= borrowed_at),  
    FOREIGN KEY (reader_id) REFERENCES readers(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);


CREATE TABLE book_inspections (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    book_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    inspected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    condition VARCHAR(64) NOT NULL CHECK (condition IN ('good', 'damaged', 'needs repair', 'lost', 'under review')),
    comment VARCHAR(256),
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);