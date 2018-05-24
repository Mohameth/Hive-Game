/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Joueurs.ThreadCoups;

import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Plateau;
import java.util.Random;

/**
 *
 * @author firmyn
 */
public class CoupMoyen extends AbstractCoup{

    public CoupMoyen(Plateau plateau, JoueurIA joueur, Joueur adverse) {
        super(plateau, joueur, adverse);
    }
    
    
    @Override
    protected boolean coup() {
        boolean b = false;
        Random r = new Random();
        Insecte insecte;

        return false;
    }
    
}
