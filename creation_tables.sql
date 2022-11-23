-- Création du schéma physique de la BD

CREATE TABLE RESTAURANTS(
    RMail VARCHAR(50),
    RNom VARCHAR(30) NOT NULL,
    RNum VARCHAR(12) NOT NULL,
    RAdresse VARCHAR(50) NOT NULL,
    Places int NOT NULL,
    Presentation VARCHAR(300),
    RNote FLOAT,
    PRIMARY KEY(RMail)
);

CREATE TABLE HORAIRES(
    JourPlage VARCHAR(3) CHECK (JourPlage IN ('LM', 'LS', 'MaM', 'MaS', 'MeM', 'MeS', 'JM', 'JS', 'VM', 'VS', 'SM', 'SS', 'DM', 'DS')),
    PRIMARY KEY(JourPlage)
);

CREATE TABLE TYPESCOMMANDE(
    TypeCommande VARCHAR(9) CHECK (TypeCommande IN ('livraison', 'place', 'emporter')),
    PRIMARY KEY(TypeCommande)
);

CREATE TABLE CATEGORIES (
    CatNom VARCHAR(30),
    PRIMARY KEY(CatNom)
);

CREATE TABLE ALLERGENES(
    ANom VARCHAR(20),
    PRIMARY KEY(ANom)
);

CREATE TABLE COMMANDES (
    Cid INT,
    CDate DATE NOT NULL,
    CHeure TIMESTAMP NOT NULL,
    CPrix FLOAT,
    U_id INT,
    TypeCommande VARCHAR(9),
    PRIMARY KEY(Cid),
    FOREIGN KEY(U_id) REFERENCES UTILISATEURS(U_id),
    FOREIGN KEY(TypeCommande) REFERENCES TYPESCOMMANDE(TypeCommande)
);

CREATE TABLE UTILISATEURS (
    U_id INT,
    UMail VARCHAR(50),
    UMdp VARCHAR(30),
    UNom VARCHAR(20),
    UPrenom VARCHAR(20),
    UAddresse VARCHAR(50),
    PRIMARY KEY(U_id)
);

CREATE TABLE EVALUATIONS (
    Eid INT,
    EDate DATE NOT NULL,
    EHeure TIMESTAMP NOT NULL,
    Avis VARCHAR(300),
    ENote INT NOT NULL,
    Cid INT,
    PRIMARY KEY(Eid),
    FOREIGN KEY(Cid) REFERENCES COMMANDES(Cid)
);

CREATE TABLE COMMANDESEMPORTEES (
    CEid INT,
    CEStatut VARCHAR(10) CHECK (CEStatut IN ('Attente', 'Validee', 'Disponible', 'AnnuleeC', 'AnnuleeR', 'Terminee')) NOT NULL,
    PRIMARY KEY(CEid)
);

CREATE TABLE COMMANDESLIVREES (
    CLid INT,
    CLAdresse VARCHAR(50),
    Indications VARCHAR(300),
    CLArrivee TIMESTAMP,
    CLStatut VARCHAR(11) CHECK (CLStatut IN ('Attente', 'Validee', 'enLivraison', 'AnnuleeC', 'AnnuleeR', 'Terminee')) NOT NULL,
    PRIMARY KEY(CLid)
);

CREATE TABLE COMMANDESSURPLACE (
    CPid INT,
    NbPers INT NOT NULL,
    CPArrivee TIMESTAMP NOT NULL,
    CPStatut VARCHAR(8) CHECK (CPStatut IN ('Attente', 'Validee', 'AnnuleeC', 'AnnuleeR', 'Terminee')) NOT NULL,
    PRIMARY KEY(CPid)
);

CREATE TABLE PLATS(
    Pid INT,
    PRestaurant VARCHAR(50),
    PNom VARCHAR(20) NOT NULL,
    PDescription VARCHAR(300),
    PPrix FLOAT NOT NULL,
    PRIMARY KEY(Pid, PRestaurant),
    FOREIGN KEY(PRestaurant) REFERENCES RESTAURANTS(RMail)
);

CREATE TABLE CATEGORIEPARENT (
    CatNom VARCHAR(30),
    CatNomMere VARCHAR(30),
    PRIMARY KEY(CatNom, CatNomMere),
    FOREIGN KEY(CatNom) REFERENCES CATEGORIES(CatNom),
    FOREIGN KEY(CatNomMere) REFERENCES CATEGORIES(CatNom)
);

CREATE TABLE HORAIRESRESTAURANT (
    RMail VARCHAR(50),
    JourPlage VARCHAR(3),
    PRIMARY KEY(RMail, JourPlage),
    FOREIGN KEY(RMail) REFERENCES RESTAURANTS(RMail),
    FOREIGN KEY(JourPlage) REFERENCES HORAIRES(JourPlage)
);

CREATE TABLE CATEGORIESRESTAURANT (
    RMail VARCHAR(50),
    CatNom VARCHAR(30),
    PRIMARY KEY(RMail, CatNom),
    FOREIGN KEY(RMail) REFERENCES RESTAURANTS(RMail),
    FOREIGN KEY(CatNom) REFERENCES CATEGORIES(CatNom)
);

CREATE TABLE TYPESRESTAURANT (
    RMail VARCHAR(50),
    TypeCommande VARCHAR(9),
    PRIMARY KEY(RMail, TypeCommande),
    FOREIGN KEY(RMail) REFERENCES RESTAURANTS(RMail),
    FOREIGN KEY(TypeCommande) REFERENCES TYPESCOMMANDE(TypeCommande)
);

CREATE TABLE ALLERGENESPLAT (
    Pid INT,
    PRestaurant VARCHAR(50),
    ANom VARCHAR(20),
    PRIMARY KEY(Pid, PRestaurant, ANom),
    FOREIGN KEY(Pid, PRestaurant) REFERENCES PLATS(Pid, PRestaurant),
    FOREIGN KEY(ANom) REFERENCES ALLERGENES(ANom)
);

CREATE TABLE PLATSCOMMANDE (
    Cid INT,
    Pid INT,
    PRestaurant VARCHAR(50),
    PRIMARY KEY(Cid, Pid, PRestaurant),
    FOREIGN KEY(Cid) REFERENCES COMMANDES(Cid),
    FOREIGN KEY(Pid, PRestaurant) REFERENCES PLATS(Pid, PRestaurant)
);
