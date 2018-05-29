/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import Modele.IA.IAFacile;
import Modele.IA.IAMinimax;
import Modele.Insectes.Insecte;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.NumJoueur;
import Modele.Plateau;
import java.util.HashMap;

/**
 *
 * @author moham
 */
public class TestMinimax {
    
    
    public static void main(String[] args) {
        HashMap<Integer, Integer> coupParPartie = new HashMap<>();
        int nbWinJ1 = 0;
        int nbWinJ2 = 0;
        int nbDraw = 0;
        int nbRealDraw = 0;
        
        int x = 0;
        for (int j = 0; j < 1000; j++) {
            Plateau plateau = new Plateau();
            IAFacile  joueur1 = new IAFacile(plateau, true, NumJoueur.JOUEUR1, null);
            IAMinimax joueur2 = new IAMinimax(plateau, true, NumJoueur.JOUEUR2, joueur1);
            joueur1.setAdversaire(joueur2);
            
            boolean fini = false;
            int coup = 0;
            while (!fini && coup <= 500) {
                joueur1.coup(null, null);
                //System.out.println("J1 à joué ");
                if ((joueur1.reineBloquee() || joueur2.reineBloquee())) {
                    fini = true;
                } else if (!fini) {
                    joueur2.coup(null, null);
                    //System.out.println("J2 à joué");
                    if ((joueur1.reineBloquee() || joueur2.reineBloquee())) {
                        fini = true;
                    }
                }
                
                System.out.println(coup);
                coup++;
            }

            coupParPartie.put(j, coup);
            if (joueur1.reineBloquee() && joueur2.reineBloquee()) {
                nbRealDraw++;
            }
            else if (joueur1.reineBloquee()) {
                nbWinJ2++;
            } else if (joueur2.reineBloquee()) {
                nbWinJ1++;
            } else {
                nbDraw++;
            }
            System.out.println(" --> "+j);
            System.out.println("Victoires J1: "+nbWinJ1);
            System.out.println("Victoires J2: "+nbWinJ2);
            System.out.println("Draw (+500) : " + nbDraw);
            System.out.println("Draw        : " + nbRealDraw);
        }
        
        System.out.println(coupParPartie);
        System.out.println("Victoires J1: "+nbWinJ1);
        System.out.println("Victoires J2: "+nbWinJ2);
        System.out.println("Draw (+500) : " + nbDraw);
        System.out.println("Draw        : " + nbRealDraw);
    }
}
