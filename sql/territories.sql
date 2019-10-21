create table territories ( id serial PRIMARY KEY, name VARCHAR , continent_id INT REFERENCES continents , 

pawn INT default 0  , player_id INT default null REFERENCES players

) ;

​

INSERT INTO territories (name, continent_id) VALUES ('Cerveau_1', 1);

INSERT INTO territories (name, continent_id) VALUES ('Cerveau_2', 2);

INSERT INTO territories (name, continent_id) VALUES ('Cerveau_3', 3);

INSERT INTO territories (name, continent_id) VALUES ('Cerveau_4', 4);

INSERT INTO territories (name, continent_id) VALUES ('Cerveau_5', 5);

INSERT INTO territories (name, continent_id) VALUES ('Cerveau_6', 6);

​

INSERT INTO territories (name, continent_id) VALUES ('Poumon_droit_1', 7);

INSERT INTO territories (name, continent_id) VALUES ('Poumon_droit_2', 8);

INSERT INTO territories (name, continent_id) VALUES ('Poumon_droit_3', 9);

​

INSERT INTO territories (name, continent_id) VALUES ('Poumon_gauche_1', 10);

INSERT INTO territories (name, continent_id) VALUES ('Poumon_gauche_2', 11);

INSERT INTO territories (name, continent_id) VALUES ('Poumon_gauche_3', 12);

​

INSERT INTO territories (name, continent_id) VALUES ('Coeur_1', 13);

INSERT INTO territories (name, continent_id) VALUES ('Coeur_2', 14);

​

INSERT INTO territories (name, continent_id) VALUES ('Foie_1', 15);

INSERT INTO territories (name, continent_id) VALUES ('Foie_2', 16);

INSERT INTO territories (name, continent_id) VALUES ('Foie_3', 17);

​

INSERT INTO territories (name, continent_id) VALUES ('Rein_1', 18);

INSERT INTO territories (name, continent_id) VALUES ('Rein_2', 19);

INSERT INTO territories (name, continent_id) VALUES ('Rein_3', 20);

​

INSERT INTO territories (name, continent_id) VALUES ('Estomac_1', 21);

INSERT INTO territories (name, continent_id) VALUES ('Estomac_2', 22);

​

INSERT INTO territories (name, continent_id) VALUES ('Gros_intestin_1', 23);

INSERT INTO territories (name, continent_id) VALUES ('Gros_intestin_2', 24);

INSERT INTO territories (name, continent_id) VALUES ('Gros_intestin_3', 25);

​

INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_1', 26);

INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_2', 27);

INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_3', 28);

INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_4', 29);


​
