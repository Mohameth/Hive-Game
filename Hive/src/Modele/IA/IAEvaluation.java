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
import java.util.Iterator;

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
        if (aGagne()) {
            return 1000;
        }
        if (aPerdu()) {
            return -1000;
        }

        initPiecesValues(myPiecesValues, false);
        initPiecesValues(opponentPiecesValues, true);
        updatePositionValues(myPiecesValues);
        updatePositionValues(opponentPiecesValues);

        return getSumEvaluation(myPiecesValues)
                - getSumEvaluation(opponentPiecesValues);
    }

    private Boolean insecteBelongsToOpponent(Insecte i) {
        return !(i.getJoueur().getNumJoueur() == joueur.getNumJoueur());
    }

    private void initPiecesValues(HashMap<Insecte, Integer> piecesValues, Boolean opponent) {
        for (Insecte i : getTousInsecte()) {
            if (insecteBelongsToOpponent(i).equals(opponent)) {//si opponent, nos pieces ne serons pas initialisés
                switch (i.getType()) {
                    case REINE:
                        piecesValues.put(i, 50);
                        if (opponent) {
                            reineAdverse = i;
                        } else {
                            maReine = i;
                        }
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
        if (insecteBelongsToOpponent(i)) {
            reine = maReine;
        } else {
            reine = reineAdverse;
        }

        if (reine == null || reine.getEmplacement() == null) {
            return false;
        }

        return jeu.estVoisin(reine, i);
    }

    private boolean maReineEstEntoure(Insecte i) {
        //En train de bloquer la reine adverse + 15
        Insecte reine;
        if (insecteBelongsToOpponent(i)) {
            reine = reineAdverse;
        } else {
            reine = maReine;
        }

        if (reine == null || reine.getEmplacement() == null) {
            return false;
        }

        return jeu.estVoisin(reine, i);
    }

    private int mobilite(Insecte i) {
        return i.deplacementPossible(jeu).size();
    }

    private boolean aGagne() {
        return adversaire.reineBloquee();
    }

    private boolean aPerdu() {
        return joueur.reineBloquee();
    }

    private void updatePositionValues(HashMap<Insecte, Integer> piecesValues) {
        HashMap<Insecte, Integer> piecesAjout = new HashMap<>();
        Iterator<Insecte> it = piecesValues.keySet().iterator();
        while (it.hasNext()) {
            Insecte i = it.next();
            Integer value = piecesValues.get(i);
            /*if (value == null) {
                value = 0;
            }*/
            if (estDansLaMain(i)) {
                value = 0;
            } else if (estImmobilise(i)) {
                value -= 10;
                if (estRecouvert(i)) {
                    value -= 20;
                }
            } else {
                value += mobilite(i);
                if (entoureReineAdverse(i)) {
                    value += 15;
                }
                if (maReineEstEntoure(i)) {
                    value -= 50;
                }
            }

            /*if (bloqueFourmiAdverse(i)) value += 5;
            value += capaciteABloquerReineAdverse(i);
             */
            //TODO: Gérer les pouvoirs spéciaux et ne pas mettre les valeurs en bruts.
            
            piecesAjout.put(i, value);
        }
        piecesValues.putAll(piecesAjout);
    }

    private int getSumEvaluation(HashMap<Insecte, Integer> piecesValues) {
        int sum = 0;
        for (int v : piecesValues.values()) {
            sum += v;
        }

        return sum;
    }
}
