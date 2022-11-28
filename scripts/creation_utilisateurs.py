#!/usr/bin/env python

from random import choices, randint
from sys import argv

def main():
    """
    Fonction main
    """
    N = int(argv[1])
    res = []
    
    with open("liste_adresses.txt", 'r', encoding="UTF8") as f:
        lines = f.readlines()
        adresses_choisies = choices(lines, k=N)
    
    with open("liste_noms.txt", 'r', encoding="UTF8") as f:
        lines = f.readlines()
        nom_choisis = choices(lines, k=N)
            
    with open("liste_prenoms.txt", 'r', encoding="UTF8") as f:
        lines = f.readlines()
        prenom_choisis = choices(lines, k=N)

        for i in range(N):
            string = "INSERT INTO UTILISATEURS VALUES ('" + str(i) + "', "
            string += "'" + prenom_choisis[i][:-1] + "." + nom_choisis[i][:-1] + "@ggmail.com', " # Adresse mail
            string += "'" + nom_choisis[i][:-1] + prenom_choisis[i][0] + "', " # MDP
            string += "'" + nom_choisis[i][:-1] + "', " # Nom
            string += "'" + prenom_choisis[i][:-1] + "', " # Prénom
            string += "'" + str(randint(1, 299)) + " " + adresses_choisies[i][:-1] + "');" # Adresse postale
            res.append(string)
        
        for ligne in res:
            print(ligne)

if __name__ == '__main__':
    if (len(argv) < 1):
        print("Usage: ./creation_utilisateurs.py <number>")
    else:
        main()