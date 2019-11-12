
create table players ( id serial PRIMARY KEY ,
name VARCHAR);



create table continents ( id serial PRIMARY KEY , name VARCHAR);



create table territories ( id serial PRIMARY KEY, name VARCHAR , continent_id INT REFERENCES continents , 
pawn INT default 0  , player_id INT default null REFERENCES players
) ;





create table territories_adjacent

(

  territory_a INT NOT NULL REFERENCES territories,

  territory_b INT NOT NULL REFERENCES territories,

  CONSTRAINT territories_adjacent_pkey PRIMARY KEY (territory_a, territory_b)

);




