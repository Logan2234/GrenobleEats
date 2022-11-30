-- Droit à l'oubli

-- On suppose que quand on se connecte sur l'API on sauvegarde l'U_id de la personne connectée.
-- Ainsi on peut retrouver 

BEGIN;

-- On se connecte en tant que USER (on obtient son U_id ce qui montre qu'il existe)
SELECT U_id FROM UTILISATEURS
WHERE UMail = 'Nicolas.Carpentier@ggmail.com' AND UMdp = 'CarpentierN';

-- Ici l'utilisateur dit qu'il a envie de supprimer toutes ses données
UPDATE UTILISATEURS
SET UMail = NULL, 
    UMdp = NULL, 
    UNom = NULL, 
    UPrenom = NULL, 
    UAddresse = NULL
WHERE U_id = (SELECT U_id FROM UTILISATEURS WHERE U_id = '82'); -- Cet U_id sera déjà enregistré dans une variable

SELECT * FROM UTILISATEURS WHERE U_id = '82'; -- U_id de Nicolas Carpentier, on voit que tout est supprimé.

ROLLBACK;