CREATE TABLE IF NOT EXISTS game (
    id SERIAL PRIMARY KEY,
    name varchar(50),
    rows integer NOT NULL,
    columns integer NOT NULL,
    status varchar(20) NOT NULL,
    last_started_time timestamptz,
    elapsed_time_milliseconds bigint DEFAULT 0,
    user_id bigint,
    CONSTRAINT fk_game_user FOREIGN KEY (user_id) REFERENCES user_account (id)
);

CREATE TABLE IF NOT EXISTS cell (
    id SERIAL PRIMARY KEY,
    row_position bigint NOT NULL,
    column_position bigint NOT NULL,
    status varchar(20) NOT NULL,
    mines_around integer,
    is_mine boolean,
    game_id bigint,
    CONSTRAINT fk_cell_game FOREIGN KEY (game_id) REFERENCES game (id)
);

CREATE TABLE IF NOT EXISTS user_account (
    id  SERIAL PRIMARY KEY,
    username varchar(128) NOT NULL,
    password varchar(512) NOT NULL
);
