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

INSERT INTO CATEGORIEPARENT VALUES ('francaise','nationale');
INSERT INTO CATEGORIEPARENT VALUES ('fastfood','francaise');
INSERT INTO CATEGORIEPARENT VALUES ('gastronomie','francaise');
INSERT INTO CATEGORIEPARENT VALUES ('expérimentale','francaise');
INSERT INTO CATEGORIEPARENT VALUES ('regionale','francaise');
INSERT INTO CATEGORIEPARENT VALUES ('provencale','regionale');
INSERT INTO CATEGORIEPARENT VALUES ('nordique','regionale');
INSERT INTO CATEGORIEPARENT VALUES ('alpine','regionale');
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

INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('croquettes@resto.com', 'Croquettes de chez vous', '0000000', '12 rue Croquettes 38000', '20', 'On vous sert des croquettes','');
INSERT INTO RESTAURANTS (RMail, RNom, RNum, RAdresse, Places, Presentation) VALUES 
    ('instant@resto.com', 'L instant espagnol', '0000001', '12 rue Instant 38000', '30', 'On vous sert de la bouffe espagnole','');
