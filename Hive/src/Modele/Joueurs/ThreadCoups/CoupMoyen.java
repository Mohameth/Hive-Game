
package Modele.Joueurs.ThreadCoups;

import Modele.HexaPoint;
import Modele.IA.Configuration;
import Modele.IA.Coup;
import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.NumJoueur;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author GRP3
 */
public class CoupMoyen extends AbstractCoup{
    int horizon = 4;
    public Coup lastCoup = null;
    
    /**
     * Joue un coup en utilisant l'algo du minimax
     * 
     * @param plateau le plateau avec les pions
     * @param joueur le joueur courant
     * @param adverse le joueur adverse
     */
    public CoupMoyen(Plateau plateau, JoueurIA joueur, Joueur adverse) {
        super(plateau, joueur, adverse);
    }
    
    /**
     * Màj la profondeur de calcul
     * 
     * @param horizon 
     */
    public void setHorizon(int horizon) {
        this.horizon = horizon;
    }
    
    /**
     * Joue un coup avec le minimax
     * 
     * @return true si la génération de coup a réussi
     */
    @Override
    protected boolean coup() {
        Coup coup = minimax();
        /*if (coup == null) {
            JoueurIA iaf = new JoueurIA(this.plateau,1,false,NumJoueur.JOUEUR2,this.adversaire);
            iaf.setPions(pions);
            return iaf.coup(insecte, cible);
        }*/
        Insecte i;
        
        if (coup.isModePlacement()) {
            i = joueur.getPions().get(coup.getNumDansMain());
            joueur.placementInsecte(i, coup.getCible());
            System.out.println(i.getClass().getName() + " en " + coup.getCible() + " (placement)");
            return true;
        }
        
        i = plateau.getCase(coup.getOrigine()).getInsectes().get(coup.getNiveauInsecte()-1);
        i.deplacement(plateau, coup.getCible()); 
        System.out.println(i.getClass().getName() + " en " + coup.getCible());
        lastCoup = coup;
              
        //joueur.
        return true;
    }
    
    /**
     * 
     * @return le coup trouvé
     */
    public Coup minimax() {
        Configuration parent = new Configuration(plateau, joueur, adverse);
        Configuration meilleurConf = null;
        int oldVal = Integer.MIN_VALUE;
        int newVal;
        
        ArrayList<Configuration> x = parent.getAllCoupsPossibles(false);
        System.out.println("Analyse de " + x.size() + " configurations");
        for (Configuration c : x) {
            newVal = calculJoueurCourant(c, horizon, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (newVal > oldVal) {
                meilleurConf = c;
                oldVal = newVal;
            }
        }
        if (meilleurConf != null) {
            System.out.println("Coup évalué à " + meilleurConf.getEvaluation());
            return meilleurConf.getCoupJoue();
        } else {
            System.out.println("aucune conf : Jeu aleatoire");
            return null;
        }
    }
    
    public int calculJoueurCourant(Configuration conf, int horizon, int alpha, int beta) {
        if (conf.estFeuille() || horizon == 0) {
            return conf.getEvaluation();
        }
        
        int valeur = Integer.MIN_VALUE;
        for (Configuration confCourante : conf.getAllCoupsPossibles(true)) {
            valeur = Integer.max(valeur, calculAdversaire(confCourante, horizon-1, alpha, beta));
            if (valeur > beta) return valeur;
            alpha = Integer.max(alpha, valeur);
        }
        
        return valeur;
    }
    
    public int calculAdversaire(Configuration conf, int horizon, int alpha, int beta) {
        if (conf.estFeuille() || horizon == 0) {
            return conf.getEvaluation();
        } 
        
        int valeur = Integer.MAX_VALUE;
        for (Configuration confCourante : conf.getAllCoupsPossibles(false)) {
            valeur = Integer.min(valeur, calculJoueurCourant(confCourante, horizon-1, alpha, beta));
            if (alpha > valeur) return valeur;
            beta = Integer.min(beta, valeur);
        }
        
        return valeur;
    }
    
}
