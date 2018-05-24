/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Joueurs.ThreadCoups;

import Modele.Case;
import Modele.Deplacement;
import Modele.HexaPoint;
import Modele.Insectes.Reine;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author firmyn
 */
public abstract class AbstractCoup implements Runnable{
    
    protected long tempsDebut;
    protected long tempsFin;
    protected Plateau plateau;
    protected JoueurIA joueur;
    protected Joueur adverse;


    public AbstractCoup(Plateau plateau, JoueurIA joueur, Joueur adverse) {
        this.plateau = plateau;
        this.joueur = joueur;
        this.adverse = adverse;
        this.resetTemps();
    }
    
    
    
    protected abstract boolean coup();
    
    @Override
    public void run() {
        this.tempsDebut = System.nanoTime();
        this.coup();
        
    }
    
    private void attente() {
        this.tempsFin = System.nanoTime();
        if ((this.tempsFin - this.tempsDebut) < 2000000000) {
            try {
                TimeUnit.NANOSECONDS.sleep(2000000000 - (this.tempsFin - this.tempsDebut));
                this.resetTemps();
            } catch (InterruptedException ex) {
                System.err.println("ERREUR attente IA : " + ex);
            }
        }
    }

    private void resetTemps() {
        this.tempsDebut = 0;
        this.tempsFin = 0;
    }

    public void jouerReine() {
        Reine r = this.joueur.getReine();
        Random ra = new Random();
        ArrayList<Case> cases = plateau.pointVersCase(plateau.casesVidePlacement(this.joueur));
        HexaPoint c = cases.get(ra.nextInt(cases.size())).getCoordonnees();
                    
        this.joueur.setDernierDeplacement(new Deplacement(r, null, c));
        this.plateau.ajoutInsecte(r, c);
        r.deplacement(plateau,c );
    }

}
