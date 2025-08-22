CREATE DATABASE  IF NOT EXISTS `P6Buddy`;
use P6Buddy;
-- Table User
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Table Compte
CREATE TABLE Compte (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    solde DOUBLE DEFAULT 0,
    CONSTRAINT fk_compte_user FOREIGN KEY (user_id) REFERENCES User(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Table Ami
CREATE TABLE Ami (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    ami_id INT NOT NULL,
    CONSTRAINT fk_ami_user FOREIGN KEY (user_id) REFERENCES User(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_ami_ami FOREIGN KEY (ami_id) REFERENCES User(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT unique_ami UNIQUE (user_id, ami_id)
);

-- Table Transaction
CREATE TABLE Transaction (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender INT NOT NULL,
    receiver INT NOT NULL,
    description VARCHAR(255),
    amount DOUBLE NOT NULL CHECK (amount > 0),
    CONSTRAINT fk_transaction_sender FOREIGN KEY (sender) REFERENCES User(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_transaction_receiver FOREIGN KEY (receiver) REFERENCES User(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
