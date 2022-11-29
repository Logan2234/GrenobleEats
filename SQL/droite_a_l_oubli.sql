-- Droit à l'oubli

BEGIN;

-- On se connecte en tant que USER (on obtient son U_id ce qui montre qu'il existe)
SELECT U_id
FROM UTILISATEURS
WHERE UMail = 'Nicolas.Carpentier@ggmail.com'
    AND UMdp = 'CarpentierN';

-- Ici l'utilisateur dit qu'il a envie de supprimer toutes ses données
UPDATE UTILISATEURS
SET UMail = NULL,
    UMdp = NULL,
    UNom = NULL,
    UPrenom = NULL,
    UAddresse = NULL
WHERE UMail = 'Nicolas.Carpentier@ggmail.com'
    AND UMdp = 'CarpentierN';

SELECT * FROM UTILISATEURS WHERE U_id = '82'; -- U_id de Nicolas Carpentier

ROLLBACK;