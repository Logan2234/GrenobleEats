-- Parcours de catégories et sous-catégories
-- On regarde le parcours de catégorie comme un menu déroulant affichant les résultats petit à petit.

BEGIN;

-- On se connecte en tant que USER (on obtient son UID qu'on sauvegarde dans l'API)
SELECT U_id FROM UTILISATEURS WHERE UMail = 'Nicolas.Carpentier@ggmail.com' AND UMdp = 'CarpentierN';
-- L'ID est ici égal à 82

-- Si on parcourt suivant les recommendations de l'utilisateur:
SELECT CATEGORIESRESTAURANT.CatNom FROM CATEGORIESRESTAURANT
JOIN CATEGORIESRESTAURANT ON CATEGORIESRESTAURANT.RMail = RESTAURANTS.RMail 
JOIN PLATSCOMMANDE ON PLATSCOMMANDE.PRestaurant = RESTAURANTS.RMail 
JOIN COMMANDES ON COMMANDES.Cid = PLATSCOMMANDE.Cid 
WHERE COMMANDES.U_id = '82' 
ORDER BY COMMANDES.CDate DESC LIMIT 5;

-- Sinon, on parcourt les catégories, de la catégorie mère jusqu'à une sous-catégorie
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = '_'; -- '_' désignant la racine. Parmi les résultats se trouve "Par pays"
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'Par pays'; -- Parmi les résultats se trouve "francaise"

-- On regarde quels sont les restaurants français
SELECT RESTAURANTS.RMail, RNom, RNum, RAdresse, Places, Presentation, RNote FROM RESTAURANTS 
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail 
WHERE CatNom = 'francaise'
ORDER BY RNote DESC, RNom ASC;

-- Il y en a trop, donc on retourne en arrière et on demande les sous-catégories
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'francaise';

-- Et on veut voir les sous-catégories de la cuisine alpine
SELECT CatNom FROM CATEGORIEPARENT WHERE CatNomMere = 'alpine';

-- La dauphinoise semble bien, donc on la sélectionne pour voir les restaurants associés
SELECT RESTAURANTS.RMail, RNom, RNum, RAdresse, Places, Presentation, RNote FROM RESTAURANTS 
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail
WHERE CatNom = 'dauphinoise'
ORDER BY RNote DESC, RNom ASC;

-- En fait, on vient de se rappeler qu'on veut passer commande dans un restaurant français ouvert un mardi midi, peu importe si c'est dauphinois ou pas
SELECT RESTAURANTS.RMail, RNom, RNum, RAdresse, Places, Presentation, RNote FROM RESTAURANTS 
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail
JOIN HORAIRESRESTAURANT ON RESTAURANTS.RMail = HORAIRESRESTAURANT.RMail
WHERE CatNom = 'francaise' AND JourPlage= 'MaM'
ORDER BY RNote DESC, RNom ASC;

-- En fait, on cherche un plat en particulier, on veut donc consulter la liste des plats de chaque restaurant français ouvert ce moment là
SELECT RESTAURANTS.RNom, PNom, PDescription, PPrix FROM PLATS
JOIN RESTAURANTS ON PLATS.PRestaurant = RESTAURANTS.RMail
JOIN CATEGORIESRESTAURANT ON RESTAURANTS.RMail = CATEGORIESRESTAURANT.RMail
JOIN HORAIRESRESTAURANT ON RESTAURANTS.RMail = HORAIRESRESTAURANT.RMail
WHERE CatNom = 'francaise' AND JourPlage= 'MaM'
GROUP BY RESTAURANTS.RNom, PNom, PDescription, PPrix
ORDER BY RESTAURANTS.RNom ASC;

ROLLBACK;