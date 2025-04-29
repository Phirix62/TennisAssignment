DELETE FROM `match`;
DELETE FROM tournament_players;
DELETE FROM tournament;
DELETE FROM user;

INSERT INTO user (id, username, password, email, full_name, role)
VALUES (1, 'admin', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'admin@example.com', 'Admin User', 'ADMIN');


INSERT INTO user (id, username, password, email, full_name, role) VALUES
(2, 'player1', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player1@example.com', 'Player One', 'PLAYER'),
(3, 'player2', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player2@example.com', 'Player Two', 'PLAYER'),
(4, 'player3', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player3@example.com', 'Player Three', 'PLAYER'),
(5, 'player4', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player4@example.com', 'Player Four', 'PLAYER'),
(6, 'player5', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player5@example.com', 'Player Five', 'PLAYER'),
(7, 'player6', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player6@example.com', 'Player Six', 'PLAYER'),
(8, 'player7', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player7@example.com', 'Player Seven', 'PLAYER'),
(9, 'player8', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player8@example.com', 'Player Eight', 'PLAYER'),
(10, 'player9', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player9@example.com', 'Player Nine', 'PLAYER');

INSERT INTO user (id, username, password, email, full_name, role) VALUES
(11, 'referee1', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref1@example.com', 'Ref One', 'REFEREE'),
(12, 'referee2', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref2@example.com', 'Ref Two', 'REFEREE'),
(13, 'referee3', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref3@example.com', 'Ref Three', 'REFEREE'),
(14, 'referee4', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref4@example.com', 'Ref Four', 'REFEREE');

INSERT INTO tournament (id, name, location, start_date) VALUES
(1, 'Wimbledon', 'London', '2025-06-29'),
(2, 'Roland Garros', 'Paris', '2025-05-26');

-- Tournament Players
INSERT INTO tournament_players (tournament_id, players_id) VALUES
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9);

-- POOL A Matches (Players 2-5)
INSERT INTO `match` (tournament_id, player1_id, player2_id, match_time, referee_id) VALUES
(1, 2, 3, '2025-06-29 08:00:00', 14),
(1, 4, 5, '2025-06-29 09:00:00', 11),
(1, 2, 4, '2025-06-29 10:00:00', 12),
(1, 3, 5, '2025-06-29 11:00:00', 13),
(1, 2, 5, '2025-06-29 14:00:00', 14),
(1, 3, 4, '2025-06-29 15:00:00', 11);

-- POOL B Matches (Players 6-9)
INSERT INTO `match` (tournament_id, player1_id, player2_id, match_time, referee_id) VALUES
(1, 6, 7, '2025-06-29 08:00:00', 12),
(1, 8, 9, '2025-06-29 09:00:00', 13),
(1, 6, 8, '2025-06-29 10:00:00', 14),
(1, 7, 9, '2025-06-29 11:00:00', 11),
(1, 6, 9, '2025-06-29 14:00:00', 12),
(1, 7, 8, '2025-06-29 15:00:00', 13);

-- SEMI-FINALS (Assuming Player 2 vs 7 and Player 6 vs 3)
INSERT INTO `match` (tournament_id, player1_id, player2_id, match_time, referee_id) VALUES
(1, 2, 7, '2025-06-29 16:00:00', 14),
(1, 6, 3, '2025-06-29 17:00:00', 11);

-- FINAL + 3rd place
INSERT INTO `match` (tournament_id, player1_id, player2_id, match_time, referee_id) VALUES
(1, 2, 6, '2025-06-29 18:00:00', 12), -- Final
(1, 7, 3, '2025-06-29 19:00:00', 13); -- 3rd place
