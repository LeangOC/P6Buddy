-- ==== INSERT DATA ====
USE P6Buddy
-- Users
INSERT INTO Users (username, email, password) VALUES
('Mei', 'mei@oc.com', '123'),
('Bob', 'bob@oc.com', '123'),
('Ken', 'ken@oc.com', '123');

-- Accounts (solde initial)
INSERT INTO Account (user_id, balance) VALUES
(1, 100.00),  -- Alice
(2, 50.00),   -- Bob
(3, 75.00);   -- Charlie

-- Buddies (Alice est amie avec Bob et Charlie, Bob est ami avec Charlie)
INSERT INTO Buddy (user_id, buddy_id) VALUES
(1, 2),  -- Alice <-> Bob
(1, 3),  -- Alice <-> Charlie
(2, 3);  -- Bob <-> Charlie

-- Transactions
INSERT INTO Transaction (sender, receiver, description, amount) VALUES
(1, 2, 'Repas CROUS', 3.00),   -- Alice envoie 3€ à Bob
(2, 3, 'Covoiturage', 10.00),        -- Bob envoie 10€ à Charlie
(3, 1, 'Cadeau', 20.00);             -- Charlie envoie 20€ à Alice
