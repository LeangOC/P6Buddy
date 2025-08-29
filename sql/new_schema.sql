CREATE DATABASE  IF NOT EXISTS `MyP6Buddy`;
use MyP6Buddy;
-- Table Users
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Table Account
CREATE TABLE Account (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES Users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table Buddy
CREATE TABLE Buddy (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    buddy_id INT NOT NULL,
    CONSTRAINT fk_buddy_user FOREIGN KEY (user_id) REFERENCES Users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_buddy_buddy FOREIGN KEY (buddy_id) REFERENCES Users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_buddy UNIQUE (user_id, buddy_id)
);

-- Table Transaction
CREATE TABLE Transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender INT NOT NULL,
    receiver INT NOT NULL,
    description VARCHAR(255),
    amount DECIMAL(10,2) NOT NULL CHECK (amount > 0),
    CONSTRAINT fk_transaction_sender FOREIGN KEY (sender) REFERENCES Users(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_transaction_receiver FOREIGN KEY (receiver) REFERENCES Users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
