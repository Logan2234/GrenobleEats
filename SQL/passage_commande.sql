-- Passage de la commande
-- TODO : tester le dernier cas avec plusieurs commandes dans la base

/*
On supposera que l'utilisateur U_Id, le restaurant RMail et les plats (Pid, PRestaurant) sont connus via Java.

Si le restaurant ne prend pas le type de commande demandé, la commande n'est pas créée (dans notre exemple ils sont compatibles).
Si les plats ou le restaurant choisi n'existe pas, la commande passée sera vide.
On supposera que le restaurant aura accès aux commandes et s'occupera d'actualiser leur statut.
La conversion TIMESTAMP->JourPlage est faite par Java.

Dans le cas de la commande sur place, on a supposé que l'on souhaite réservé pour l'instant T, il conviendra de mettre la date choisie par l'utilisateur 
avec l'usage de l'API. De même ce sera l'API qui s'occupera de regarder si la réservation sur place est possible (en comptant le nombre de personnes au moment en question)
*/

BEGIN;

TRUNCATE TABLE COMMANDESLIVREES;
TRUNCATE TABLE COMMANDESEMPORTEES;
TRUNCATE TABLE COMMANDESSURPLACE;
TRUNCATE TABLE PLATSCOMMANDE;
TRUNCATE TABLE COMMANDES;


-- COMMANDE EN LIVRAISON (cas de base avec deux plats commandés une seule fois)
-- Création de la commande pour l'utilisateur d'identifiant 1
INSERT INTO COMMANDES VALUES ('0', CURRENT_TIMESTAMP, '0', '1',
                            (SELECT TypeCommande FROM TYPESRESTAURANT 
                                WHERE RMail = 'croquettes@resto.com' AND TypeCommande = 'livraison'));
                            -- Le SELECT permet de vérifier que le restaurant accepte effectivement la livraison, 
                            -- cette partie n'est pas nécessaire car en réalité, l'API s'occupera de vérifier ce genre de contraintes.

-- Choix des plats
INSERT INTO PLATSCOMMANDE VALUES ('0', '1', 'croquettes@resto.com', '1');
INSERT INTO PLATSCOMMANDE VALUES ('0', '2', 'croquettes@resto.com', '1');

-- Calcul du prix en SQL qui peut également se faire via l'API
UPDATE COMMANDES SET CPrix = (SELECT SUM(PPrix * NbPlat) FROM PLATS
    INNER JOIN PLATSCOMMANDE ON PLATSCOMMANDE.Pid=PLATS.Pid AND PLATSCOMMANDE.PRestaurant=PLATS.PRestaurant
    WHERE Cid = '0')
WHERE Cid = '0';

-- Ajout de la commande à livrer
INSERT INTO COMMANDESLIVREES VALUES ('0', (SELECT UAdresse FROM UTILISATEURS WHERE U_id='1'), 'Niet', NULL, 'Attente');


-- COMMANDE A EMPORTER
-- Création de la commande pour l'utilisateur d'identifiant 5
INSERT INTO COMMANDES VALUES ('1', CURRENT_TIMESTAMP, '0', '5',
                            (SELECT TypeCommande FROM TYPESRESTAURANT
                                WHERE RMail = 'instant@resto.com' AND TypeCommande = 'emporter'));
                            -- Le SELECT fait la même chose que pour la partie précédente

-- Choix des plats
INSERT INTO PLATSCOMMANDE VALUES ('1', '0',  'instant@resto.com', '3');

-- Calcul du prix en SQL qui peut également se faire via l'API
UPDATE COMMANDES SET CPrix = (SELECT SUM(PPrix * NbPlat) FROM PLATS
    INNER JOIN PLATSCOMMANDE ON PLATSCOMMANDE.Pid=PLATS.Pid AND PLATSCOMMANDE.PRestaurant=PLATS.PRestaurant
        WHERE Cid = '1')
    WHERE Cid = '1';

-- Ajout de la commande à emporter
INSERT INTO COMMANDESEMPORTEES VALUES ('1', 'Attente');


-- COMMANDE SUR PLACE
-- Création de la commande pour l'utilisateur d'identifiant 6
INSERT INTO COMMANDES VALUES ('2', CURRENT_TIMESTAMP, '0', '6',
    (SELECT TypeCommande FROM TYPESRESTAURANT
        WHERE RMail = 'gyoza@resto.com' AND TypeCommande = 'place'));

-- Choix des plats
INSERT INTO PLATSCOMMANDE VALUES ('2', '0', 'gyoza@resto.com', '1');
INSERT INTO PLATSCOMMANDE VALUES ('2', '1', 'gyoza@resto.com', '1');
INSERT INTO PLATSCOMMANDE VALUES ('2', '2', 'gyoza@resto.com', '2');
INSERT INTO PLATSCOMMANDE VALUES ('2', '3', 'gyoza@resto.com', '1');

-- Calcul du prix en SQL qui peut également se faire via l'API
UPDATE COMMANDES SET CPrix = (SELECT SUM(PPrix * NbPlat) FROM PLATS
    INNER JOIN PLATSCOMMANDE ON PLATSCOMMANDE.Pid=PLATS.Pid AND PLATSCOMMANDE.PRestaurant=PLATS.PRestaurant
        WHERE Cid = '2')
    WHERE Cid = '2';

-- Ajout de la commande sur place pour 5 personnes réservant pour la date CURRENT_TIMESTAMP
INSERT INTO COMMANDESSURPLACE VALUES ('2', '5', CURRENT_TIMESTAMP, 'Attente');

ROLLBACK;