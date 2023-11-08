CREATE TABLE IF NOT EXISTS image (
    exhibition_image_id INT AUTO_INCREMENT PRIMARY KEY,
    unique_name VARCHAR(255) NOT NULL,
    origin_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    nickname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS exhibition (
    exhibition_id INT AUTO_INCREMENT PRIMARY KEY,
    exhibition_name VARCHAR(255) NOT NULL,
    description CLOB,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    location VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image_id INT,
    FOREIGN KEY (image_id) REFERENCES image (exhibition_image_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_time DATETIME NOT NULL,
    member_id INT,
    exhibition_id INT,
    FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE CASCADE,
    FOREIGN KEY (exhibition_id) REFERENCES exhibition (exhibition_id) ON DELETE CASCADE
);



