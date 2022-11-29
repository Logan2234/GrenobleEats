-- Parcours de catégories et sous-catégories

BEGIN;

-- On se connecte en tant que USER (on obtient son UID)
SELECT U_id FROM UTILISATEURS WHERE UMail = 'Nicolas.Carpentier@ggmail.com' AND UMdp = 'CarpentierN';

-- Parcours des catégories, de la catégorie mère jusqu'à une sous-catégorie
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = '_';
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'Par pays'; -- Parmi les résultats se trouve "francaise"

-- On regarde quels sont les restaurants français
SELECT RESTAURANTS.RMail, RNom, RNum, RAdresse, Places, Presentation, RNote FROM RESTAURANTS 
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail 
WHERE CatNom = 'francaise'
ORDER BY RNote DESC, RNom ASC;

-- Il y en a trop, donc on demande les sous-catégories
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'francaise';
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'alpine';

-- L'alpine semble bien 
SELECT RESTAURANTS.RMail, RNom, RNum, RAdresse, Places, Presentation, RNote FROM RESTAURANTS 
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail
WHERE CatNom = 'alpine'
ORDER BY RNote DESC, RNom ASC;

-- En fait on veut les restaurants français ouvert un samedi soir
SELECT RESTAURANTS.RMail, RNom, RNum, RAdresse, Places, Presentation, RNote FROM RESTAURANTS 
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail
JOIN HORAIRESRESTAURANT ON RESTAURANTS.RMail = HORAIRESRESTAURANT.RMail
WHERE CatNom = 'francaise' AND JourPlage= 'SM'
ORDER BY RNote DESC, RNom ASC;

-- Si on veut consulter les liste des plats
SELECT RESTAURANTS.RNom, PNom, PDescription, PPrix FROM PLATS
JOIN RESTAURANTS ON PLATS.PRestaurant = RESTAURANTS.RMail
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail
JOIN HORAIRESRESTAURANT ON RESTAURANTS.RMail = HORAIRESRESTAURANT.RMail
WHERE CatNom = 'francaise' AND JourPlage= 'SM';

ROLLBACK;

--------------------- EXEMPLE D'UTILISATION AVEC L'APPLI JAVA ---------------------------------

-- Que voulez-vous faire ?

-- 1) Parcourir les catégories
-- 2) Suggestions de catégories
-- 3) Retour

-- >> 1

-- Comment souhaitez-vous parcourir les catégories ?

-- 1) Par pays -- SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'Par pays'; 
-- 2) Par type de restaurant -- SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'Par catégorie'; 
-- 3) Retour

-- >> 1

-- Quel pays ?

-- 1) France 
-- 2) Italie
-- 3) Espagne
-- 4) Japon
-- 5) Retour

-- >> 1

-- Que souhaitez-vous faire ?

-- 1) Voir les restaurant français -- SELECT * FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CatNom = 'francaise';
-- 2) Voir les sous-catégories -- SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'francaise';
-- 3) Retour

-- >> 1 -- Affiche les résultats
-- OU
-- >> 2

-- 1) alpine -- SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'alpine';
-- 2) nordique -- SELECT * FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CatNom = 'nordique' ORDER BY RNote DESC, RNom ASC;
-- 3) provencale -- SELECT * FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CatNom = 'provencale' ORDER BY RNote DESC, RNom ASC;
-- 4) Retour

-- >> 1

-- Que souhaitez-vous faire ?

-- 1) Voir les restaurant alpins -- SELECT * FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CatNom = 'alpine' ORDER BY RNote DESC, RNom ASC;
-- 2) Voir les sous-catégories -- SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'alpine';
-- 3) Retour

-- >> 2

-- 1) savoyarde -- SELECT * FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CatNom = 'savoyarde' ORDER BY RNote DESC, RNom ASC;
-- 2) dauphinoise -- SELECT * FROM RESTAURANTS JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail WHERE CatNom = 'dauphinoise' ORDER BY RNote DESC, RNom ASC;
-- 3) Retour

-- >> 1

-- RESULTATS AFFICHES