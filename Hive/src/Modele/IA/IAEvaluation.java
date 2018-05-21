/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.IA;

import Modele.Case;
import Modele.Insectes.Insecte;
import Modele.Joueur;
import Modele.Plateau;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author moham
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

    public IAEvaluation(Plateau jeu, Joueur joueur, Joueur adversaire) {
        this.jeu = jeu;
        this.joueur = joueur;
        this.adversaire = adversaire;
        
        myPiecesValues = new HashMap<>();
        opponentPiecesValues = new HashMap<>();
    }
    
    /**
     * 
     * @return evaluation courante de la position
     */
    public int getEvaluation() {
        initPiecesValues(myPiecesValues, false);
        initPiecesValues(opponentPiecesValues, true);
        updatePositionValues(myPiecesValues);
        updatePositionValues(opponentPiecesValues);
        
        int m = getSumEvaluation(myPiecesValues);
        int h = getSumEvaluation(opponentPiecesValues);
        return m - h;
        /*return getSumEvaluation(myPiecesValues) 
                - getSumEvaluation(opponentPiecesValues);*/
    }
    
    private Boolean insecteBelongsToOpponent(Insecte i) {
        return !i.getJoueur().equals(joueur);
    }
    
    private void initPiecesValues(HashMap<Insecte, Integer> piecesValues, Boolean opponent) {
        for (Insecte i : getTousInsecte()) {
            if (insecteBelongsToOpponent(i).equals(opponent)) {//si opponent, nos pieces ne serons pas initialisés
                switch(i.getType()) {
                    case REINE: 
                        piecesValues.put(i, 50);
                        if (opponent) reineAdverse = i;
                        else maReine = i;
                        break;
                    case SCARABEE: 
                        piecesValues.put(i, 20);
                        break;
                    case SAUTERELLE: 
                        piecesValues.put(i, 20);
                        break;
                    case ARAIGNEE: 
                        piecesValues.put(i, 10);
                        break;    
                    case FOURMI: 
                        piecesValues.put(i, 30);
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
    
    private Boolean estRecouvert(Insecte i) {
        return !i.getEmplacement().getInsecteOnTop().equals(i);
    }
    
    private boolean estImmobilise(Insecte i) {
        return i.deplacementPossible(jeu).isEmpty();
    }
    
    private boolean estDansLaMain(Insecte i) {
        return i.getEmplacement() == null;
    }
    
    private boolean entoureReineAdverse(Insecte i) {
        //En train de bloquer la reine adverse + 15
        Insecte reine;
        if (insecteBelongsToOpponent(i))    reine = maReine;
        else                                reine = reineAdverse;
        
        return (jeu.estVoisin(i, reine));
    }
    
    private int mobilite(Insecte i) {
        return i.deplacementPossible(jeu).size();
    }
    
    private boolean aGagne() {
        return false;
    }
    
    private boolean aPerdu() {
        return false;
    }
    
    private void updatePositionValues(HashMap<Insecte, Integer> piecesValues) {
        for (Insecte i : piecesValues.keySet()) {
            Integer value = piecesValues.get(i);
            if (estDansLaMain(i)) 
                value = 0;
            else {
                if (estImmobilise(i)) {
                    value -= 10;
                    if (estRecouvert(i))    value -= 20;
                }
                else value += mobilite(i);
            }
            
            //if (entoureReineAdverse(i)) value += 15;
            if (aGagne())               value += 100;
            if (aPerdu())               value -= 100;
            
            
            /*if (bloqueFourmiAdverse(i)) value += 5;
            value += capaciteABloquerReineAdverse(i);
            */
            
            //TODO: Gérer les pouvoirs spéciaux et ne pas mettre les valeurs en bruts.
            piecesValues.put(i, value);
        }
    }
    
    private int getSumEvaluation(HashMap<Insecte, Integer> piecesValues) {
        int sum = 0;
        for (int v : piecesValues.values()) {
            sum += v;
        }
        
        return sum;
    }
}
