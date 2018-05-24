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
    JOUEUR1(1), JOUEUR2(2), IAFACILE1(3), IAFACILE2(4), IAMOYEN1(5), IAMOYEN2(6), IADIFFICILE1(7), IADIFFICILE2(8);
    
    int contenu;
    
    NumJoueur(int i) {
        contenu = i;
    }

    public int getContenu() {
        return contenu;
    }
    
    public boolean estBlanc() {
        return (this.contenu % 2) != 0;
    }
}
