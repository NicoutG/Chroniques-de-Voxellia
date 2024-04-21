# Type de blocs

## Type 0

Type par défaut.

Aucun paramètres

## Type 1

L'ensemble des blocs qui subissent la gravité et qui peuvent être poussé par le joueur.

0 à 1 paramètres

    Paramètre 1 : Id du bloc (Si on veut qu'un mécanisme ne soit activable que par un seul bloc) (-1 si non précisé)

## Type 2

Les blocs de levier.

2 paramètres

    Paramètre 1 : Etat du levier (t = activé, f = désactivé)
    Paramètre 2 : Id de groupe
                    - -1 : accorde la victoire
                    - autres : active les blocs activables du même Id

## Type 3

Les blocs de plaque.

2 à 3 paramètres

    Paramètre 1 : Etat par défaut de la plaque non préssée (t = activé, f = désactivé)
    Paramètre 2 : Id de groupe (-1 accorde la victoire, autre active les blocs activables du même Id)
    Paramètre 3 : Id d'activation (-2 par defaut)
                    - -2 : activable par le joueur et les blocs mouvants
                    - -1 : activable par toutes les blocs mouvant
                    - autres : activable par les blocs mouvants du même Id

## Type 4

Les blocs de téléportation

3 paramètres

    Paramètre 1 : Etat par défautt du téléporteur (t = activé, f = désactivé)
    Paramètre 2 : Id de groupe (pour son activation et sa désactivation)
    Paramètre 3 : Id de téléportation (pour repérer le téléporteur de destination)

## Type 5

Les blocs de piston

3 paramètres

    Paramètre 1 : Etat par défautt du téléporteur (t = activé, f = désactivé)
    Paramètre 2 : Id de groupe (pour son activation et sa désactivation)
    Paramètre 3 : L'orientation du piston
                    - 0 : haut
                    - 1 : bas
                    - 2 : devant
                    - 3 : derrière
                    - 4 : gauche
                    - 5 : droite

## Type 6

Les ennemis qui suivent le joueur

0 à 1 paramètres

    Paramètre 1 : Id du bloc (Si on veut qu'un mécanisme ne soit activable que par un seul bloc) (-1 si non précisé)

