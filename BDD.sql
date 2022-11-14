-- Création du schéma physique de la BD

CREATE TABLE RESTAURANT(
    RMail VARCHAR(50),
    RNom VARCHAR(30) NOT NULL,
    Num VARCHAR(12) NOT NULL,
    RAdresse VARCHAR(50) NOT NULL,
    Places int NOT NULL,
    Presentation VARCHAR(200),
    PRIMARY KEY(RMail)
)

-- CREATE TABLE MILIEU(
--     composition varchar(30) NOT NULL CHECK (composition IN ('acide', 'basique', 'neutre')),
--     PRIMARY KEY (composition)
-- );

-- CREATE TABLE TYPEMISSION(
--     typeMis varchar(30) NOT NULL CHECK (typeMis IN ('transport', 'combat', 'pillage', 'interception')),
--     PRIMARY KEY(typeMis)
-- );

-- CREATE TABLE GALAXIE(
--     codeGal INTEGER NOT NULL,
--     nomGal varchar(30),
--     distanceGal INTEGER CHECK(distanceGal > 0),
--     PRIMARY KEY(codeGal),
-- );

-- CREATE TABLE NAVIRE(
--     codeNav INTEGER NOT NULL,
--     rayonAc INTEGER CHECK(rayonAc > 0),
--     nbPil INTEGER CHECK(nbPil > 0),
--     vMax INTEGER CHECK(vMax > 0),
--     PRIMARY KEY(codeNav)
-- );

-- CREATE TABLE MISSION(
--     codeMis INTEGER NOT NULL,
--     dateMis DATE,
--     nbNav INTEGER CHECK(nbNav > 0),
--     vMin INTEGER CHECK(vMin > 0),
--     rayonEng INTEGER CHECK(rayonEng > 0),
--     typeMis varchar(30) NOT NULL,
--     PRIMARY KEY(codeMis),
--     FOREIGN KEY(typeMis) REFERENCES TYPEMISSION(typeMis)
-- );

-- CREATE TABLE PILOTE(
--     codePil INTEGER NOT NULL,
--     nomPil varchar(30),
--     prenomPil varchar(30),
--     age INTEGER CHECK(age > 0),
--     grade varchar(30),
--     PRIMARY KEY(codePil)
-- );

-- CREATE TABLE EQUIPAGE(
--     codeEq INTEGER NOT NULL,
--     effectif INTEGER CHECK(effectif > 0),
--     PRIMARY KEY(codeEq)
-- );

-- CREATE TABLE NAVIRET(
--     codeNavT INTEGER NOT NULL,
--     capac INTEGER CHECK(capac > 0),
--     PRIMARY KEY(codeNavT),
--     FOREIGN KEY(codeNavT) REFERENCES NAVIRE(codeNav)
-- );

-- CREATE TABLE NAVIREC(
--     codeNavC INTEGER NOT NULL,
--     tailleMin INTEGER CHECK(tailleMin > 0),
--     tailleMax INTEGER CHECK(tailleMax > 0),
--     PRIMARY KEY(codeNavC)
--     FOREIGN KEY(codeNavC) REFERENCES NAVIRE(codeNav)
-- );

-- CREATE TABLE PLANETE(
--     codeGal INTEGER NOT NULL,
--     codePLa INTEGER NOT NULL,
--     nomPla varchar(30),
--     vlib INTEGER CHECK(vlib > 0),
--     statut varchar(30) CHECK (statut IN ('nonExpl', 'reconnue', 'intégrée')),
--     composition varchar(30) NOT NULL,
--     PRIMARY KEY(codeGal, codePLa),
--     FOREIGN KEY(codeGal) REFERENCES GALAXIE(codeGal),
--     FOREIGN KEY(composition) REFERENCES MILIEU(composition)
-- );

-- CREATE TABLE CIBLE(
--     codeMis INTEGER NOT NULL,
--     codeGal INTEGER,
--     codePla INTEGER,
--     PRIMARY KEY(codeMis),
--     FOREIGN KEY(codeMis) REFERENCES MISSION(codeMis),
--     FOREIGN KEY(codeGal, codePla) REFERENCES PLANETE(codeGal, codePla)
-- );

-- CREATE TABLE AFFECTATIONPILOTE(
--     codePil int NOT NULL,
--     codeNav int NOT NULL,
--     codeMis int NOT NULL,
--     PRIMARY KEY(codePil, codeNav, codeMis),
--     FOREIGN KEY(codePil) REFERENCES PILOTE(codePil),
--     FOREIGN KEY(codeNav) REFERENCES NAVIRE(codeNav),
--     FOREIGN KEY(codeMis) REFERENCES MISSION(codeMis),
-- );

-- CREATE TABLE AFFECTATIONEQUIPAGE(
--     codeEq int NOT NULL,
--     codeNav int NOT NULL,
--     codeMis int NOT NULL,
--     PRIMARY KEY(codeEq, codeNav, codeMis),
--     FOREIGN KEY(codeEq) REFERENCES EQUIPAGE(codeEq),
--     FOREIGN KEY(codeNav) REFERENCES NAVIRE(codeNav),
--     FOREIGN KEY(codeMis) REFERENCES MISSION(codeMis)
-- );

-- CREATE TABLE COMPATIBLE(
--     codeNav int NOT NULL,
--     composition varchar NOT NULL,
--     PRIMARY KEY(codeNav, composition),
--     FOREIGN KEY(codeNav) REFERENCES NAVIRE(codeNav),
--     FOREIGN KEY(composition) REFERENCES MILIEU(composition)

-- );

-- CREATE TABLE PILOTEFORMEA(
--     codePil int NOT NULL,
--     typeMis varchar(30),
--     PRIMARY KEY(codePil, typeMis),
--     FOREIGN KEY(codePil) REFERENCES PILOTE(codePil),
--     FOREIGN KEY(typeMis) REFERENCES TYPEMISSION(typeMis)
-- );

-- CREATE TABLE EQUIPAGEPEUTREMPLIR(
--     codeEq int NOT NULL,
--     typeMis varchar(30) NOT NULL,
--     PRIMARY KEY(codeEq, typeMis),
--     FOREIGN KEY(codeEq) REFERENCES EQUIPAGE(codeEq),
--     FOREIGN KEY(typeMis) REFERENCES TYPEMISSION(typeMis),
-- )