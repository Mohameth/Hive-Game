/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

/**
 *
 * @author Jeremy
 */
public enum NumJoueur {
    JOUEUR1(1), JOUEUR2(2);
    
    int contenu;
    
    NumJoueur(int i) {
        contenu = i;
    }

}
