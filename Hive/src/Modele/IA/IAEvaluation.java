
package Modele.IA;

import Modele.Case;
import Modele.Insectes.Fourmi;
import Modele.Insectes.Insecte;
import Modele.Joueurs.Joueur;
import Modele.Plateau;
import Modele.Insectes.TypeInsecte;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author GRP3
 */
public class IAEvaluation {

    Plateau jeu;
    Joueur joueur;
    Joueur adversaire;
    Integer evaluationPlateau;
    HashMap<Insecte, Integer> myPiecesValues;
    HashMap<Insecte, Integer> opponentPiecesValues;

    //Stockage des reines pour récup. rapide de leurs positions
    private Insecte maReine;
    private Insecte reineAdverse;

    /**
     * Permet d'évaluer une configuration de jeu
     * 
     * @param jeu le plateau à évaluer
     * @param joueur le joueur courant
     * @param adversaire le joueur adverse
     */
    public IAEvaluation(Plateau jeu, Joueur joueur, Joueur adversaire) {
        this.jeu = jeu;
        this.joueur = joueur;
        this.adversaire = adversaire;

        myPiecesValues = new HashMap<>();
        opponentPiecesValues = new HashMap<>();
    }

    /**
     *
     * @return l'évaluation courante de la position
     */
    public int getEvaluation() {
        if (aGagne()) {
            return Integer.MAX_VALUE;
        }
        if (aPerdu()) {
            return Integer.MIN_VALUE;
        }

        initPiecesValues(myPiecesValues, false);
        initPiecesValues(opponentPiecesValues, true);
        updatePositionValues(myPiecesValues);
        updatePositionValues(opponentPiecesValues);

        return getSumEvaluation(myPiecesValues)
                - getSumEvaluation(opponentPiecesValues);
    }
    
    /**
     * Indique s'il s'agit d'un insecte adverse
     * 
     * @param i insecte
     * @return true si l'insecte appartient au joueur adverse, false sinon
     */
    private Boolean insecteBelongsToOpponent(Insecte i) {
        return !(i.getJoueur().getNumJoueur() == joueur.getNumJoueur());
    }
    
    /**
     * Initialise la listes des pièces ainsi que leurs valeurs
     * 
     * @param piecesValues liste des pieces d'un joueur
     * @param opponent true si on considère le joueur adverse
     */
    private void initPiecesValues(HashMap<Insecte, Integer> piecesValues, Boolean opponent) {
        for (Insecte i : getTousInsecte()) {
            if (insecteBelongsToOpponent(i).equals(opponent)) {//si opponent, nos pieces ne serons pas initialisés
                switch (i.getType()) {
                    case REINE:
                        piecesValues.put(i, 30);
                        if (opponent) {
                            reineAdverse = i;
                        } else {
                            maReine = i;
                        }
                        break;
                    case SCARABEE:
                        piecesValues.put(i, 30);
                        break;
                    case SAUTERELLE:
                        piecesValues.put(i, 20);
                        break;
                    case ARAIGNEE:
                        piecesValues.put(i, 30);
                        break;
                    case FOURMI:
                        piecesValues.put(i, 150);
                        break;

                    case MOUSTIQUE:
                        piecesValues.put(i, 20);//Dépend de l'insecte qu'il copie
                        break;
                    case CLOPORTE:
                        piecesValues.put(i, 15);
                        break;
                    case COCCINELLE:
                        piecesValues.put(i, 15);
                        break;
                }
            }

        }
    }
    
    /**
     * Liste de tous les insectes (à partir des joueurs)
     * 
     * @return une liste de tous les insectes
     */
    private Collection<Insecte> getTousInsecte() {
        Collection<Insecte> res = new ArrayList<>();
        //Ajout des insectes du joueur
        for (Insecte i : joueur.getPions()) {
            res.add(i);
        }
        //Ajout des insectes du joueur adverse
        for (Insecte i : adversaire.getPions()) {
            res.add(i);
        }

        return res;
    }
    
    /**
     * Indique si l'insecte i est recouvert (en dessous d'un autre insecte)
     * 
     * @param i insecte
     * @return si l'insecte i est recouvert
     */
    private Boolean estRecouvert(Insecte i) {
        return !i.getEmplacement().getInsecteOnTop().equals(i);
    }

    private boolean estImmobilise(Insecte i) {
        return i.deplacementPossible(jeu).isEmpty();
    }

    private boolean estDansLaMain(Insecte i) {
        return i.getEmplacement() == null;
    }
    /**
     * Indique si l'insecte i entoure la reine adverse
     * 
     * @param i insecte
     * @return true si i et la reine adverse sont voisin
     */
    private boolean entoureReineAdverse(Insecte i) {
        Insecte reine = insecteBelongsToOpponent(i) ? maReine : reineAdverse;
        
        if (reine == null || reine.getEmplacement() == null) {
            return false;
        }
        
        return jeu.estVoisin(reine, i);
    }

    private boolean maReineEstEntoure(Insecte i) {
        Insecte reine = insecteBelongsToOpponent(i) ? reineAdverse : maReine;

        if (reine == null || reine.getEmplacement() == null) {
            return false;
        }
        
        return jeu.estVoisin(reine, i);
    }
    
    private boolean peutEntourerReineAdverse(Insecte i) {
        return false;
    }
    
    /**
     * Indique la mobilité d'un insecte
     * 
     * @param i insecte
     * @return le nombre de cases vers lequel l'insecte i peut se déplacer 
     */
    private int mobilite(Insecte i) {
        return i.deplacementPossible(jeu).size();
    }
    
    /**
     * Indique si la configuration du plateau est gagnante
     * 
     * @return true si le joueur courant a gagné
     */
    private boolean aGagne() {
        return adversaire.reineBloquee();
    }
    
    /**
     * Indique si la configuration du plateau est perdant
     * 
     * @return true si l'adversaire a gagné
     */
    private boolean aPerdu() {
        return joueur.reineBloquee();
    }
    
    /**
     * Met à jour les valeurs des pièces selon leurs positions (par rapport aux autres pièces)
     * 
     * @param piecesValues liste des pieces + valeurs d'un joueur
     */
    private void updatePositionValues(HashMap<Insecte, Integer> piecesValues) {
        HashMap<Insecte, Integer> piecesAjout = new HashMap<>();
        Iterator<Insecte> it = piecesValues.keySet().iterator();
        while (it.hasNext()) {
            Insecte i = it.next();
            Integer value = piecesValues.get(i);
            
            if (estDansLaMain(i)) {
                value -= value*2;
            } else {
                if (estRecouvert(i))    value /= 5;
                else                                        value += mobilite(i)*2;
                //if (entoureReineAdverse(i))                 value += 10000;
                //else if (peutEntourerReineAdverse(i))       value += 50;
                //if (maReineEstEntoure(i))                   value -= 100;
            }
            
            piecesAjout.put(i, value);
        }
        piecesValues.putAll(piecesAjout);
    }
    
    /**
     * Calcule la somme des valeurs des pièces 
     * 
     * @param piecesValues liste des pieces + valeurs d'un joueur
     * @return la somme des valeurs des pièces
     */
    private int getSumEvaluation(HashMap<Insecte, Integer> piecesValues) {
        int sum = 0;
        for (int v : piecesValues.values()) {
            sum += v;
        }

        return sum;
    }
}
