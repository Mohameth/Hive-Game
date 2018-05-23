package Modele;

import Modele.IA.Coup;
import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import java.util.ArrayList;
import java.util.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JoueurIA extends Joueur {

    private int difficulte;
    private Joueur adverse;
    private long tempsDebut;
    private long tempsFin;

    public JoueurIA(Plateau p, int difficulte, boolean extensions, NumJoueur numJoueur, Joueur adverse) {
        super(p, extensions, numJoueur);
        this.difficulte = difficulte;
        this.adverse = adverse;
        this.resetTemps();
    }

    public JoueurIA(Plateau p, int difficulte, NumJoueur numJoueur, boolean extensions) {
        super(p, extensions, numJoueur);
        this.difficulte = difficulte;
        this.adverse = null;
    }

    public void addJoueurAdverse(Joueur j) {
        this.adverse = j;
    }

    public Joueur getAdverse() {
        return adverse;
    }

    
    @Override
    public boolean coup(Insecte insecte, HexaPoint cible) {
        this.tempsDebut = System.nanoTime();
        if (difficulte == 1) {
            return coupFacile();
        } else if (difficulte == 2) {
            return coupNormal();
        } else {
            return coupDifficile();
        }
        
    }

    private boolean coupFacile() {
        
        if (this.reineBloquee()) {
            this.tourJoueur++;
            return false;
        }

        boolean b = false;
        Random r = new Random();
        Insecte insecte;
        boolean bloquee = true;

        Iterator<Insecte> it = this.pions.iterator();
        Insecte i;
        while (it.hasNext() && bloquee) {
            i = it.next();
            if (i.getEmplacement() == null || !i.deplacementPossible(plateau).isEmpty()) {
                bloquee = false;
            }
        }

        if (bloquee) {
            this.tourJoueur++;
            return false;
        }

        if (!this.tousPionsPosables()) {
            this.tourJoueur++;
            jouerReine();
            return true;
        }
        this.tourJoueur++;
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
                    this.attente();
                    this.plateau.ajoutInsecte(insecte, p);
                    System.out.println(insecte.getClass() + " en " + p);
                    return true;
                }
            } else if (this.reinePosee()) {
                b = !insecte.deplacementPossible(plateau).isEmpty();
            }
        } while (!b);

        ArrayList<Case> deplacement = (ArrayList<Case>) insecte.deplacementPossible(plateau);
        HexaPoint p = deplacement.get(r.nextInt(deplacement.size())).getCoordonnees();
        this.dernierDeplacement = new Deplacement(insecte, insecte.getEmplacement().getCoordonnees(), p);
        this.attente();
        insecte.deplacement(plateau, p);
        System.out.println(insecte.getClass() + " en " + p);
        
        return true;
    }
    
    private boolean coupNormal() {
        boolean b = false;
        Random r = new Random();
        Insecte insecte;

        return false;
    }

    private boolean coupDifficile() {

        if (this.reineBloquee()) {
            return false;
        }

        if (!this.tousPionsPosables()) {
            jouerReine();
            return true;
        }

        if (this.coupGagnant()) {
            return true;
        }

        Noeud noeud = new Noeud(plateau, this.pionsEnMain(), adverse.pionsEnMain(), this.pionsEnJeu(), adverse.pionsEnJeu());
        MonteCarlo monteCarlo = new MonteCarlo(this.tourJoueur, noeud, this);

        do {
            Noeud noeud2 = monteCarlo.selection();
            noeud2 = monteCarlo.Expansion(noeud2);
            boolean b = monteCarlo.simulation(noeud2);
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

        CoupleCaesInsecte coupleCaesInsecte = getCouple(noeud, noeud.getListeFils().get(indice),
                noeud.getPossiblilites().get(indice).getInsecte(), noeud.getPossiblilites().get(indice).getCase(),
                noeud.getPossiblilites().get(indice).getAncienneCase());
        if (coupleCaesInsecte.getInsecte().getEmplacement() == null) {
            this.dernierDeplacement = new Deplacement(coupleCaesInsecte.getInsecte(), null, coupleCaesInsecte.getCase().getCoordonnees());
            noeud.getPlateau().ajoutInsecte(coupleCaesInsecte.getInsecte(), coupleCaesInsecte.getCase().getCoordonnees());
        } else {
            this.dernierDeplacement = new Deplacement(coupleCaesInsecte.getInsecte(), coupleCaesInsecte.getInsecte().getEmplacement().getCoordonnees(), coupleCaesInsecte.getCase().getCoordonnees());
            coupleCaesInsecte.getInsecte().deplacement(noeud.getPlateau(), coupleCaesInsecte.getCase().getCoordonnees());
        }
        
        this.tourJoueur++;
        return true;
    }

    private void jouerReine() {
        Reine r = getReine();
        Random ra = new Random();
        ArrayList<Case> cases = plateau.pointVersCase(plateau.casesVidePlacement(this));
        HexaPoint c = cases.get(ra.nextInt(cases.size())).getCoordonnees();
                    
        this.dernierDeplacement = new Deplacement(r, null, c);
        this.plateau.ajoutInsecte(r, c);
        this.attente();
        r.deplacement(plateau,c );
    }

    private boolean coupGagnant() {
        if (!adverse.reinePresqueBloquee()) {
            return false;
        }
        Case c = ((ArrayList<Case>) plateau.getCasesVoisines(adverse.getReine().getEmplacement(), true)).get(0);
        ArrayList<Insecte> in = this.pionsEnJeu();
        for (int i = 0; i < in.size(); i++) {
            if (in.get(i).deplacementPossible(plateau).contains(c)) {
                if (!in.get(i).getEmplacement().estVoisin(c) || in.get(i).getEmplacement().getNbInsectes() > 1) {
                    this.dernierDeplacement = new Deplacement(in.get(i), in.get(i).getEmplacement().getCoordonnees() , c.getCoordonnees());
                    in.get(i).deplacement(plateau, c.getCoordonnees());
                    return true;
                }
            }
        }
        return false;
    }

    private CoupleCaesInsecte getCouple(Noeud pere, Noeud fils, Insecte in, Case c, Case c2) {

        if (c2 == null) {
            return new CoupleCaesInsecte(getInsecte2(pere.getMainIA(), in), getCase2(pere.getPlateau(), c), null);
        }

        return new CoupleCaesInsecte(getCase2(pere.getPlateau(), c2).getInsecteOnTop(),
                getCase2(pere.getPlateau(), c), null);
        
    }

    private Case getCase2(Plateau p1, Case c) {
        Map<HexaPoint, Case> cases = p1.getCases();
        for (Map.Entry<HexaPoint, Case> e : cases.entrySet()) {
            if (e.getValue().equals(c)) {
                return e.getValue();
            }
        }
        return null;
    }

    private Insecte getInsecte2(ArrayList<Insecte> insecte, Insecte in) {
        for (int i = 0; i < insecte.size(); i++) {
            if (insecte.get(i).equals(in)) {
                return insecte.get(i);
            }
        }
        return null;
    }

    private void resetTemps() {
        this.tempsDebut = 0;
        this.tempsFin = 0;
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
}
