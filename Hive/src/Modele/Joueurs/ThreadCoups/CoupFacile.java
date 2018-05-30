/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Joueurs.ThreadCoups;

import Modele.Case;
import Modele.HexaPoint;
import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author firmyn
 */
public class CoupFacile extends AbstractCoup {

    public CoupFacile(Plateau plateau, JoueurIA joueur, Joueur adverse) {
        super(plateau, joueur, adverse);
    }

    
   
    @Override
    protected boolean coup() {
        System.out.println("IA FACILE");
        if (this.joueur.reineBloquee()) {
            return false;
        }

        boolean b = false;
        Random r = new Random();
        Insecte insecte;
        boolean bloquee = true;

        Iterator<Insecte> it = this.joueur.getPions().iterator();
        Insecte i;
        while (it.hasNext() && bloquee) {
            i = it.next();
            if (i.getEmplacement() == null || !i.deplacementPossible(plateau).isEmpty()) {
                bloquee = false;
            }
        }

        if (bloquee) {
            return false;
        }

        if (!this.joueur.tousPionsPosables()) {
            this.jouerReine();
            return true;
        }else if(coupGagnant()) {
        	return true;
        }
        do {
            insecte = this.joueur.getPions().get(r.nextInt(this.joueur.getPions().size()));
            if (insecte.getEmplacement() == null) {
                ArrayList<HexaPoint> coordPlacement = plateau.casesVidePlacement(this.joueur);
                ArrayList<Case> casePlacement = new ArrayList<>();
                for (HexaPoint p : coordPlacement) {
                    casePlacement.add(this.plateau.getCase(p));
                }
                if (!casePlacement.isEmpty()) {
                    HexaPoint p = casePlacement.get(r.nextInt(casePlacement.size())).getCoordonnees();
                    this.joueur.coupChoisi(insecte, p, true);
                    System.out.println(insecte.getClass() + " en " + p);
                    return true;
                }
            } else if (this.joueur.reinePosee()) {
                b = !insecte.deplacementPossible(plateau).isEmpty();
            }
        } while (!b);

        ArrayList<Case> deplacement = (ArrayList<Case>) insecte.deplacementPossible(plateau);
        HexaPoint p = deplacement.get(r.nextInt(deplacement.size())).getCoordonnees();
        this.joueur.coupChoisi(insecte, p, false);
        System.out.println(insecte.getClass() + " en " + p);
        return true;    
    }
    

    
}
