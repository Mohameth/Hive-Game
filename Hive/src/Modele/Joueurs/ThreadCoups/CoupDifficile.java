/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Joueurs.ThreadCoups;

import Modele.CoupleCaesInsecte;
import Modele.Deplacement;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.MonteCarlo;
import Modele.Joueurs.Noeud;
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
        if (this.joueur.reineBloquee()) {
            return false;
        }

        if (!this.joueur.tousPionsPosables()) {
            jouerReine();
            return true;
        }

        if (this.coupGagnant()) {
            return true;
        }
        System.out.println(joueur.getTourJoueur()+","+adverse.getTourJoueur());
        Noeud noeud = new Noeud(joueur.getTourJoueur(),adverse.getTourJoueur(),plateau, this.joueur.pionsEnMain(), adverse.pionsEnMain(), this.joueur.pionsEnJeu(), adverse.pionsEnJeu());
        MonteCarlo monteCarlo = new MonteCarlo(this.joueur.getTourJoueur(), noeud, this.joueur);

        do {
            
            Noeud noeud2 = monteCarlo.selection();
            noeud2 = monteCarlo.Expansion(noeud2);
            System.out.println("Noeuds :" + monteCarlo.getNbNoeuds() + " " + monteCarlo.getNbNoeudsMax());
            double b = monteCarlo.simulation(noeud2);
            monteCarlo.miseAjour(noeud2, b);
            
        } while (monteCarlo.getNbNoeuds() < monteCarlo.getNbNoeudsMax());
        
        double max = 0.0;
        int indice = 0;

        for (int i = 0; i < noeud.getNbFils(); i++) {
            Noeud fils = noeud.getListeFils().get(i);
            if (max < fils.getUSB()) {
                max = fils.getUSB();
                indice = i;
            }
        }

        
        /*CoupleCaesInsecte coupleCaesInsecte = getCouple(
                noeud, 
                noeud.getListeFils().get(indice),
                noeud.getPossiblilites().get(indice).getInsecte(), 
                noeud.getPossiblilites().get(indice).getCase(),
                noeud.getPossiblilites().get(indice).getAncienneCase());
        if (coupleCaesInsecte.getInsecte().getEmplacement() == null) {*/
            //this.joueur.setDernierDeplacement(new Deplacement(coupleCaesInsecte.getInsecte(), null, coupleCaesInsecte.getCase().getCoordonnees()));
        joueur.coupChoisi(noeud.getPossiblilites().get(indice).getInsecte(), noeud.getPossiblilites().get(indice).getCase().getCoordonnees(), true);
            //noeud.getPlateau().ajoutInsecte(coupleCaesInsecte.getInsecte(), coupleCaesInsecte.getCase().getCoordonnees());
        //} else {
            //this.joueur.setDernierDeplacement(new Deplacement(coupleCaesInsecte.getInsecte(), coupleCaesInsecte.getInsecte().getEmplacement().getCoordonnees(), coupleCaesInsecte.getCase().getCoordonnees()));
        joueur.coupChoisi(noeud.getPossiblilites().get(indice).getInsecte(), noeud.getPossiblilites().get(indice).getCase().getCoordonnees(), false);
            //coupleCaesInsecte.getInsecte().deplacement(noeud.getPlateau(), coupleCaesInsecte.getCase().getCoordonnees());
        //}
        
        //this.joueur.incrementeTour();
        return true;
    }
    
}
