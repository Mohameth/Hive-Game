/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Joueurs;

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

    public boolean estHumain() {
        return this.contenu < 3;
    }
    
    public int getDifficulte(){
        switch (contenu) {
            case 3:
            case 4:
                return 1;
            case 5:
            case 6:
                return 2;
            case 7:
            case 8:
                return 3;
            default:
                try {
                    throw new Exception("Ce joueur n'est pas une IA");
                } catch (Exception e){
                    System.err.println("ERREUR Clone plateau : " + e);
                }
        }
        return 0;
    }
}
