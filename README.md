# Minesweeper - Deviget coding challenge

## 1. Introduction

This development was done following this [challenge](https://github.com/deviget/minesweeper-API) in the context of a job interview for [Deviget](https://www.deviget.com) company.

## 2. Technologies

Java 8, Spring Boot, PostgreSQL, Lombok, JUnit 4 and Maven. Documented with Swagger [here](https://minesweeper-leocanalejo.herokuapp.com/api/swagger-ui/index.html).

## 3. Deployment

### 3.1 Local Deployment
In order to run a working copy of the application, you will need to follow these steps:

#### 1. Clone this repository
```
$ git clone https://github.com/leocanalejo/minesweeper-deviget.git
$ cd minesweeper-deviget
```

#### 2. Create database

Create a PostgreSQL database and user, and configure it [here](src/main/resources/application.yml)

#### 3. Run the API
```
$ mvn spring-boot run
```

### 3.2 Cloud Test Environment
You can use the API through the endpoints using Swagger that have been deployed on the cloud for testing purposes.

This environment relies in [Heroku](https://www.heroku.com/) service for hosting the application API. 
You can access the API anytime by using the endpoints with base url `https://minesweeper-leocanalejo.herokuapp.com/api`
and also access the Swagger documentation (where you can also try the endpoints) [https://minesweeper-leocanalejo.herokuapp.com/api/swagger-ui/index.html](https://minesweeper-leocanalejo.herokuapp.com/api/swagger-ui/index.html).

## 4. Use
Once you have deployed the application you can play the game following Swagger documentation:

You have 3 options to do it:
1. Locally
Running the API locally and using `localhost:8000/api` as base url.
2. Cloud Test Environment
Using `https://minesweeper-leocanalejo.herokuapp.com/api` as base url.
3. Using `Try it out` Swagger functionality.

Basic steps to play:

1. Create a new user using `POST {baseUrl}/users`.
2. Authenticate using `POST {baseUrl}/authenticate` in order to get a JWT to use it as Authentication header.
3. Create a new game using `POST {baseUrl}/games`.
4. Start that game using `PATCH {baseUrl}/games/{gameId}/start`.
5. Reveal a cell using `PATCH {baseUrl}/cells/{cellId}/reveal`.

You can also pause a game, get a game, get paginated games, delete a game and flag and unflag a cell. See more details [here](https://minesweeper-leocanalejo.herokuapp.com/api/swagger-ui/index.html).

## 5. Decisions and notes

#### 1. Technologies:

I decided make this project using Java and Spring Boot because they are the language and framework with which I feel more comfortable. So first of all, I created the base project using [Spring Initializr](https://start.spring.io/).

#### 2. Security

I included all the necessary logic and configuration to use JWT as authentication method, using Spring Security. And also I added a user creation functionality.

``` java
public class UserEntity {
    private Long id;
    private String username;
    private String password;
}
```

#### 3. Database

I chose PostgreSQL as the database management system, because I thought the solution as a relationship between Game(or board) and Cells entities. So, a SQL database could help me to handle that easily.

``` sql
CREATE TABLE IF NOT EXISTS user_account (
    user_id SERIAL PRIMARY KEY,
    username varchar(128) UNIQUE NOT NULL,
    password varchar(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS game (
    game_id SERIAL PRIMARY KEY,
    name varchar(50),
    rows integer NOT NULL,
    columns integer NOT NULL,
    status varchar(20) NOT NULL,
    last_started_time timestamptz,
    elapsed_time_milliseconds bigint DEFAULT 0,
    user_id bigint,
    CONSTRAINT fk_game_users FOREIGN KEY (user_id) REFERENCES user_account (user_id),
    CONSTRAINT unq_game_name_user_id UNIQUE (name, user_id)
);

CREATE TABLE IF NOT EXISTS cell (
    cell_id SERIAL PRIMARY KEY,
    row_position bigint NOT NULL,
    column_position bigint NOT NULL,
    status varchar(20) NOT NULL,
    mines_around integer,
    is_mine boolean,
    game_id bigint,
    CONSTRAINT fk_cell_game FOREIGN KEY (game_id) REFERENCES game (game_id)
);
```

#### 4. Entities

``` java
public class CellEntity {
    private Long id;
    private Integer rowPosition;
    private Integer columnPosition;
    private CellStatus cellStatus; // Enum [HIDDEN, REVEALED, FLAGGED]
    private Integer minesAround;
    private Boolean isMine;
    private GameEntity game;
}

public class GameEntity {
    private Long id;
    private String name;
    private Integer rows;
    private Integer columns;
    private GameStatus gameStatus; // Enum [CREATED, PLAYING, PAUSED, WON, LOST]
    private LocalDateTime lastStartedTime;
    private Long elapsedTimeInMilliseconds;
    private List<CellEntity> cells;
    private UserEntity user;
}
```

Basically I created a Game entity with a list of Cells. Also a gameStatus property to track the game status at any time (CREATED, PLAYING, PAUSED, WON, LOST) and to handle some restrictions in the logic, e.g: you can not reveal a flag if the game status is not PLAYING. <br>
Cells have a row and column position, and a status (HIDDEN, REVEALED, FLAGGED). Also a boolean field isMine, and, in case it is not a mine, the number of mines around it (minesAround field).

#### 5. Game time tracking

To track the playing time, we have two properties: lastStartedTime and elapsedTimeInMilliseconds. The first one save the UTC datetime when the game was last started and the second one save the elapsed time between the last startedStartedTime and the time when the game go from PLAYING to PAUSED, WON or LOST status. 

For example:

1. Create game: status(CREATED), lastStartedTime(null), elapsedTimeInMilliseconds(0) <br>
2. Start game (at 8pm): status(PLAYING), lastStartedTime(2000-01-01T20:00:00-00:00), elapsedTimeInMilliseconds(0). To track the elapsed time in live, you have to sum elapsedTimeInMilliseconds plus the difference between now and lastStartedTime. <br>
3. Pause game (at 9pm): status(PAUSED), lastStartedTime(null), elapsedTimeInMilliseconds(3600000) <br>
4. Start game again (at 10pm): status(PLAYING), lastStartedTime(2000-01-01T22:00:00-00:00), elapsedTimeInMilliseconds(3600000). To track the elapsed time in live, you have to sum elapsedTimeInMilliseconds plus the difference between now and lastStartedTime. <br>
5. You won the game (at 11pm): status(WON), lastStartedTime(null), elapsedTimeInMilliseconds(7200000) <br>

#### 6. Game creation

1. To create a game randomly, I created an auxiliary matrix of integers to save the number of mines around that cell (min 0, max 8), or in case the cell is a mine the number 99999. <br>
2. At the time of placing a mine, the place is got randomly, so I had to be careful not to put two mines in the same cell. Therefore, we will have to repeat the result until we find a mine-free site. <br>
3. In any case, it is important to check that the number of mines is less than or equal to the total number of cells, otherwise an infinite loop could occur. This verification is done when the game is created. <br>
4. Obtaining the values 1-8 of the cells around the mines can be done in two ways: calculate it at the end of putting all the mines or increase the minesAround value when it is a mine. I decided to use the second option. As we are using a matrix, when iterating it we must be careful not to leave the board in the case of putting the mine on a side or corner. The intuitive solution is to use an IF checking that it will not go out of the vector. However, I used a shorter solution that is minimum and maximum functions. <br>

#### 7. Revealing a cell

The game must discover more cells in case there are no mines nearby. As the area to be iterated in most of the cases will be asymmetric, I decided to use a recursive procedure, with a cut condition when a cell is already in status REVEALED. 

## 6. Author

This project was developed by [Leonardo Canalejo](https://www.linkedin.com/in/leonardo-canalejo-882706100/).