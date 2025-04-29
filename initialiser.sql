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
(9, 'player8', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'player8@example.com', 'Player Eight', 'PLAYER');

INSERT INTO user (id, username, password, email, full_name, role) VALUES
(10, 'referee1', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref1@example.com', 'Ref One', 'REFEREE'),
(11, 'referee2', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref2@example.com', 'Ref Two', 'REFEREE'),
(12, 'referee3', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref3@example.com', 'Ref Three', 'REFEREE'),
(13, 'referee4', '$2a$12$YsZeWnot2HoZE6lEFrcZCe89bBD8IGOnbDO6MUDPGUo8PRO0tJrpm', 'ref4@example.com', 'Ref Four', 'REFEREE');

INSERT INTO tournament (id, name, location, start_date) VALUES
(1, 'Wimbledon', 'London', '2025-06-29'),
(2, 'Roland Garros', 'Paris', '2025-05-26');
