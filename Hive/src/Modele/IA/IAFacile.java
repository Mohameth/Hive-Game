/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Deplacement;
import Modele.HexaPoint;
import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.NumJoueur;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class IAFacile extends Joueur {
    Joueur adverse;
    
    public IAFacile(Plateau plateau, boolean extensions, NumJoueur numJoueur, Joueur adverse) {
        super(plateau, extensions, numJoueur);
        this.adverse = adverse;
    }

    public void setAdversaire(Joueur j) {
        this.adverse = j;
    }
   
    @Override
    public boolean coup(Insecte ins, HexaPoint cible) {
        if (this.reineBloquee()) {
            return false;
        }

        boolean b = false;
        Random r = new Random();
        Insecte insecte;
        boolean bloquee = true;

        Iterator<Insecte> it = this.getPions().iterator();
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

        if (!this.tousPionsPosables()) {
            this.jouerReine();
            this.incrementeTour();
            return true;
        }else if(coupGagnant()) {
            this.incrementeTour();
            return true;
        }
        do {
            insecte = this.getPions().get(r.nextInt(this.getPions().size()));
            if (insecte.getEmplacement() == null) {
                ArrayList<HexaPoint> coordPlacement = plateau.casesVidePlacement(this);
                ArrayList<Case> casePlacement = new ArrayList<>();
                for (HexaPoint p : coordPlacement) {
                    casePlacement.add(this.plateau.getCase(p));
                }
                if (!casePlacement.isEmpty()) {
                    HexaPoint p = casePlacement.get(r.nextInt(casePlacement.size())).getCoordonnees();
                    
                    insecte.setEmplacement(this.plateau.getCase(p));
                    this.plateau.ajoutInsecte(insecte, p);
                    
                    this.incrementeTour();
                    return true;
                }
            } else if (this.reinePosee()) {
                b = !insecte.deplacementPossible(plateau).isEmpty();
            }
        } while (!b);

        ArrayList<Case> deplacement = (ArrayList<Case>) insecte.deplacementPossible(plateau);
        HexaPoint p = deplacement.get(r.nextInt(deplacement.size())).getCoordonnees();
        
        this.plateau.deleteInsecte(insecte, insecte.getEmplacement().getCoordonnees());
        this.plateau.deplaceInsecte(insecte, p);
        
        this.incrementeTour();
        return true;    
    }
    
    public void jouerReine() {
        Reine r = this.getReine(this.pionsEnMain());
        Random ra = new Random();
        ArrayList<Case> cases = plateau.pointVersCase(plateau.casesVidePlacement(this));
        HexaPoint c = cases.get(ra.nextInt(cases.size())).getCoordonnees();
        
        r.setEmplacement(this.plateau.getCase(c));
        this.plateau.ajoutInsecte(r, c);
        
        this.setDernierDeplacement(new Deplacement(r, null, c));
    }
    
    protected boolean coupGagnant() {
        if (!adverse.reinePresqueBloquee()) {
            return false;
        }
        Reine reine=adverse.getReine(adverse.pionsEnJeu());
        if(reine==null) {
        	return false;
        }
        ArrayList<Case> c1=(ArrayList<Case>) plateau.getCasesVoisines(reine.getEmplacement(), true);
        Case c = c1.get(0);
        ArrayList<Insecte> in = this.pionsEnJeu();
        for (int i = 0; i < in.size(); i++) {
            if (in.get(i).deplacementPossible(plateau).contains(c)) {
                if (!in.get(i).getEmplacement().estVoisin(c) || in.get(i).getEmplacement().getNbInsectes() > 1) {
                    this.setDernierDeplacement(new Deplacement(in.get(i), in.get(i).getEmplacement().getCoordonnees(), c.getCoordonnees()));
                    in.get(i).deplacement(plateau, c.getCoordonnees());
                    this.incrementeTour();
                    return true;
                }
            }
        }
        
        this.incrementeTour();
        return false;
    }
    
}
