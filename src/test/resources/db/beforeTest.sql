DELETE FROM cell;
DELETE FROM game;
DELETE FROM user_account;

INSERT INTO user_account (user_id, username, password)
VALUES (1, 'username1', '$2a$10$/cicMdbiRPu39/hhb/mAGeH56sFqYuDYsFMW69cNRoT2flW8NICpK'),
       (99, 'username99', 'password99');

INSERT INTO game (game_id, name, rows, columns, status, last_started_time, elapsed_time_milliseconds, user_id)
VALUES (1, 'game1', 10, 10, 'CREATED', null, 0, 99),
       (2, 'game2', 3, 3, 'PLAYING', null, 0, 99),
       (3, 'game3', 10, 10, 'LOST', null, 0, 99);

INSERT INTO cell (cell_id, row_position, column_position, status, mines_around, is_mine, game_id)
VALUES (1, 0, 0, 'HIDDEN', 0, false, 2),
       (2, 0, 1, 'HIDDEN', 0, false, 2),
       (3, 0, 2, 'REVEALED', 0, false, 2),
       (4, 1, 0, 'HIDDEN', 0, false, 2),
       (5, 1, 1, 'HIDDEN', 1, false, 2),
       (6, 1, 2, 'HIDDEN', 1, false, 2),
       (7, 2, 0, 'HIDDEN', 0, false, 2),
       (8, 2, 1, 'HIDDEN', 1, false, 2),
       (9, 2, 2, 'HIDDEN', null, true, 2);