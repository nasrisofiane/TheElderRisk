create table territories ( id serial PRIMARY KEY, name VARCHAR , continent_id INT REFERENCES continents , 
pawn INT default 0  , player_id INT default null REFERENCES players
) ;

INSERT INTO territories (name, continent_id) VALUES ('Egypte', 1);


