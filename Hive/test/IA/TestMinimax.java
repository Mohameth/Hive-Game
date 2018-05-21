/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IA;

import Modele.IA.IAMinimax;
import Modele.Insectes.Insecte;
import Modele.JoueurIA;
import Modele.Plateau;

/**
 *
 * @author moham
 */
public class TestMinimax {
    public static void main(String[] args) {
        Plateau plateau = new Plateau();
        IAMinimax joueur1 = new IAMinimax(plateau, false);
        JoueurIA joueur2 = new JoueurIA(plateau, 1, false);
        //joueur2.setAdversaire(joueur1);
        joueur1.setAdversaire(joueur2);

        boolean fini = false;
        int coup = 0;
        while (!fini && coup <= 1000) {
            joueur1.coup(null, null);
            System.out.println("J1 à joué ");
            if (joueur1.reineBloquee() || joueur2.reineBloquee()) {
                fini = true;
            } else {
                joueur2.coup(null, null);
                System.out.println("J2 à joué");
                if (joueur1.reineBloquee() || joueur2.reineBloquee()) {
                    fini = true;
                }
            }
            coup++;
        }
        
        System.out.println("partie fini en " + coup);
        if (joueur1.reineBloquee()) {
            System.out.println("J2 à gagné");
        } else if (joueur2.reineBloquee()){
            System.out.println("J1 à gagné");
        } else {
            System.out.println("match NULL");
        }
        
        System.out.println("Pions du J1 :");
        for (Insecte i : joueur1.getPions()) {
            if(i.getEmplacement() != null)
                System.out.println(i.getEmplacement());
        }
        
        System.out.println("Pions de J2 :");
        for (Insecte i : joueur2.getPions()) {
            if(i.getEmplacement() != null)
                System.out.println(i.getEmplacement());
        }
        
    }
}
