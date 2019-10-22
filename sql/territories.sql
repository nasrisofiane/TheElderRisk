create table territories ( id serial PRIMARY KEY, name VARCHAR , continent_id INT REFERENCES continents , 
pawn INT default 0  , player_id INT default null REFERENCES players
) ;

INSERT INTO territories (name, continent_id) VALUES ('Cerveau_1', 1);
INSERT INTO territories (name, continent_id) VALUES ('Cerveau_2', 1);
INSERT INTO territories (name, continent_id) VALUES ('Cerveau_3', 1);
INSERT INTO territories (name, continent_id) VALUES ('Cerveau_4', 1);
INSERT INTO territories (name, continent_id) VALUES ('Cerveau_5', 1);
INSERT INTO territories (name, continent_id) VALUES ('Cerveau_6', 1);

INSERT INTO territories (name, continent_id) VALUES ('Poumon_droit_1', 2);
INSERT INTO territories (name, continent_id) VALUES ('Poumon_droit_2', 2);
INSERT INTO territories (name, continent_id) VALUES ('Poumon_droit_3', 2);

INSERT INTO territories (name, continent_id) VALUES ('Poumon_gauche_1', 3);
INSERT INTO territories (name, continent_id) VALUES ('Poumon_gauche_2', 3);
INSERT INTO territories (name, continent_id) VALUES ('Poumon_gauche_3', 3);

INSERT INTO territories (name, continent_id) VALUES ('Coeur_1', 4);
INSERT INTO territories (name, continent_id) VALUES ('Coeur_2', 4);

INSERT INTO territories (name, continent_id) VALUES ('Foie_1', 5);
INSERT INTO territories (name, continent_id) VALUES ('Foie_2', 5);
INSERT INTO territories (name, continent_id) VALUES ('Foie_3', 5);

INSERT INTO territories (name, continent_id) VALUES ('Rein_1', 6);
INSERT INTO territories (name, continent_id) VALUES ('Rein_2', 6);
INSERT INTO territories (name, continent_id) VALUES ('Rein_3', 6);

INSERT INTO territories (name, continent_id) VALUES ('Estomac_1', 7);
INSERT INTO territories (name, continent_id) VALUES ('Estomac_2', 7);

INSERT INTO territories (name, continent_id) VALUES ('Gros_intestin_1', 8);
INSERT INTO territories (name, continent_id) VALUES ('Gros_intestin_2', 8);
INSERT INTO territories (name, continent_id) VALUES ('Gros_intestin_3', 8);

INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_1', 9);
INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_2', 9);
INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_3', 9);
INSERT INTO territories (name, continent_id) VALUES ('Petit_intestin_4', 9);


