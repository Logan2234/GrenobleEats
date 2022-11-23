-- Création des entrées de la table des TYPESCOMMANDE

INSERT INTO TYPESCOMMANDE VALUES ('emporter');
INSERT INTO TYPESCOMMANDE VALUES ('place');
INSERT INTO TYPESCOMMANDE VALUES ('livraison');

-- Création des entrées de la table des CATEGORIES

INSERT INTO CATEGORIES VALUES ('_');

INSERT INTO CATEGORIES VALUES ('nationale');
INSERT INTO CATEGORIES VALUES ('francaise');
INSERT INTO CATEGORIES VALUES ('alpine');
INSERT INTO CATEGORIES VALUES ('provencale');
INSERT INTO CATEGORIES VALUES ('nordique');
INSERT INTO CATEGORIES VALUES ('savoyarde');
INSERT INTO CATEGORIES VALUES ('dauphinoise');
INSERT INTO CATEGORIES VALUES ('japonaise');
INSERT INTO CATEGORIES VALUES ('espagnole');
INSERT INTO CATEGORIES VALUES ('basque');
INSERT INTO CATEGORIES VALUES ('valencienne');
INSERT INTO CATEGORIES VALUES ('tapas');
INSERT INTO CATEGORIES VALUES ('madridenne');
INSERT INTO CATEGORIES VALUES ('andalouse');
INSERT INTO CATEGORIES VALUES ('pintxo');
INSERT INTO CATEGORIES VALUES ('italienne');
INSERT INTO CATEGORIES VALUES ('sicilienne');
INSERT INTO CATEGORIES VALUES ('romaine');
INSERT INTO CATEGORIES VALUES ('tuscane');

INSERT INTO CATEGORIES VALUES ('fastfood');
INSERT INTO CATEGORIES VALUES ('regionale');
INSERT INTO CATEGORIES VALUES ('gastronomie');
INSERT INTO CATEGORIES VALUES ('expérimentale');

-- Création des entrées de la table des CATEGORIEPARENT

INSERT INTO CATEGORIEPARENT VALUES ('nationale','_');
INSERT INTO CATEGORIEPARENT VALUES ('categorie','_');
INSERT INTO CATEGORIEPARENT VALUES ('fastfood','categorie');
INSERT INTO CATEGORIEPARENT VALUES ('gastronomie','categorie');
INSERT INTO CATEGORIEPARENT VALUES ('expérimentale','categorie');
INSERT INTO CATEGORIEPARENT VALUES ('regionale','categorie');

INSERT INTO CATEGORIEPARENT VALUES ('francaise','nationale');
INSERT INTO CATEGORIEPARENT VALUES ('provencale','francaise');
INSERT INTO CATEGORIEPARENT VALUES ('nordique','francaise');
INSERT INTO CATEGORIEPARENT VALUES ('alpine','francaise');
INSERT INTO CATEGORIEPARENT VALUES ('savoyarde','alpine');
INSERT INTO CATEGORIEPARENT VALUES ('dauphinoise','alpine');

INSERT INTO CATEGORIEPARENT VALUES ('japonaise','nationale');

INSERT INTO CATEGORIEPARENT VALUES ('espagnole','nationale');
INSERT INTO CATEGORIEPARENT VALUES ('basque','espagnole');
INSERT INTO CATEGORIEPARENT VALUES ('valencienne','espagnole');
INSERT INTO CATEGORIEPARENT VALUES ('tapas','espagnole');
INSERT INTO CATEGORIEPARENT VALUES ('madridenne','tapas');
INSERT INTO CATEGORIEPARENT VALUES ('andalouse','tapas');
INSERT INTO CATEGORIEPARENT VALUES ('pinxto','tapas');

INSERT INTO CATEGORIEPARENT VALUES ('italienne','nationale');
INSERT INTO CATEGORIEPARENT VALUES ('sicilienne','italienne');
INSERT INTO CATEGORIEPARENT VALUES ('romaine','italienne');
INSERT INTO CATEGORIEPARENT VALUES ('tuscane','italienne');

-- Création des entrées de la table des RESTAURANTS
-- Restaurants espagnols
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('croquettes@resto.com', 'Croquettes de chez vous', '0000000', '12 rue Croquettes 38000', '20', 'On vous sert des croquettes');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('instant@resto.com', 'L instant espagnol', '0000001', '12 rue Instant 38000', '30', 'On vous sert de la bouffe espagnole');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('saveur@resto.com', 'La saveur iberique', '0000003', '12 rue Iberique 38000', '30', 'On vous sert de la bouffe espagnole');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('tapas@resto.com', 'Bières et tapas', '0000004', '12 rue Tapas 38000', '40', 'On vous sert des tapas');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('paella@resto.com', 'Paëlla maison', '0000005', '12 rue Paella 38000', '20', 'On vous sert de la paella');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('tortilla@resto.com', 'La buena tortilla', '0000006', '12 rue Tortilla 38000', '30', 'On vous sert des tortillas');
-- Restaurants japonais
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('ramen@resto.com', 'Le Ramen de Shibuya', '0000007', '13 rue Ramen 38000', '20', 'On vous sert des ramens');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('gyoza@resto.com', 'Gyoza de Ginza', '0000008', '13 rue Gyozas 38000', '50', 'On vous sert des gyozas');
-- Restaurants italiens
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('pizza@resto.com', 'Pasta Pizza', '0000009', '14 rue PizzaPasta 38000', '10', 'On vous sert des pizzas');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('gorgonzola@resto.com', 'Il Gorgonzola', '00000010', '14 rue Gorgonbzola 38000', '20', 'On vous sert du fromage italien');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('torre@resto.com', 'Torre di Pizza', '00000011', '14 rue Torre 38000', '15', 'On vous sert des torre pizza');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('spaghetti@resto.com', 'Only Spaghetti', '00000012', '14 rue Spaghetti 38000', '10', 'On vous sert des pates');
-- Restaurants français
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('chefcuisine@resto.com', 'La cuisine du chef', '00000013', '15 rue cuisine 38000', '10', 'On vous sert de la bonne cuisine');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('cochonou@resto.com', 'Au cochon qui dore', '00000014', '15 rue cochon dore 38000', '20', 'On vous sert de la grosse viande');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('gustatif@resto.com', 'L arret gustatif', '00000015', '15 rue arret 38000', '25', 'On vous sert de la cuisine de qualite');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('rizkiki@resto.com', 'Riz Kiki', '00000016', '15 rue kiki 38000', '10', 'On vous sert du riz');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('grandbleu@resto.com', 'La table du grand bleu', '00000017', '15 rue fromage 38000', '19', 'On vous sert du bratin dauphinois');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('festin@resto.com', 'Le festin royal', '00000018', '14 rue fetsin 38000', '10', 'On vous sert de la grosse bouffe');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('fourchette@resto.com', 'La fourchette forestière', '00000019', '14 rue Spaghetti 38000', '30', '');

