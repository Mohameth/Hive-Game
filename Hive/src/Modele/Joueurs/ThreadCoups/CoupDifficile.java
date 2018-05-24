/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Joueurs.ThreadCoups;

import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Plateau;

/**
 *
 * @author firmyn
 */
public class CoupDifficile extends AbstractCoup{

    public CoupDifficile(Plateau plateau, JoueurIA joueur, Joueur adverse) {
        super(plateau, joueur, adverse);
    }

    
        
    @Override
    protected boolean coup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
