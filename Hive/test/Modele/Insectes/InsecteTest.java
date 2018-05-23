/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele.Insectes;

import Modele.Case;
import Modele.Joueur;
import Modele.JoueurHumain;
import Modele.Plateau;
import Modele.HexaPoint;
import Modele.NumJoueur;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author boby
 */
public class InsecteTest {

    public InsecteTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("=============================================");
        System.out.println("====           TEST INSECTE              ====");
        System.out.println("=============================================");
        System.out.println("");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * TODO : Verifier que le déplacement n'implique pas la fissure de la ruche
     * Test des déplacements de la reine
     */
    @Test
    public void testDeplacementReine() {
        System.out.println("=============================================");
        System.out.println("Test deplacementReine ======================>\n");
        HexaPoint point = new HexaPoint(0, 0, 0);
        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance,true,NumJoueur.JOUEUR1);

        Case caseReine = instance.getCase(point);
        Reine r = new Reine(j1);
        instance.ajoutInsecte(r, point);
        r.setEmplacement(caseReine);

        ArrayList<Case> result = new ArrayList<>();
        result.addAll(r.deplacementPossible(instance));

        System.out.println("test avec tous les voisins libres :");
        ArrayList<HexaPoint> expected = new ArrayList<>();
//        expected.add(new Point3DH(0, +1, -1));
//        expected.add(new Point3DH(+1, 0, -1));
//        expected.add(new Point3DH(+1, -1, 0));
//        expected.add(new Point3DH(0, -1, +1));
//        expected.add(new Point3DH(-1, 0, +1));
//        expected.add(new Point3DH(-1, +1, 0));

        arrayCorresponds(result, expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test avec 2 cases occupées et 1 case inaccessible :");
        //TEST GATE
        ArrayList<HexaPoint> expectedWithGate = new ArrayList<>();
        //expectedWithGate.add(new Point3DH(0, +1, -1)); //--> Case occupe
        //expectedWithGate.add(new Point3DH(+1, 0, -1)); //--> Inaccessible (gate)
        //expectedWithGate.add(new Point3DH(+1, -1, 0)); //--> Case occupe
        //expectedWithGate.add(new Point3DH(0, -1, +1));
        //expectedWithGate.add(new Point3DH(-1, 0, +1));
        //expectedWithGate.add(new Point3DH(-1, +1, 0));
        
        Case caseOccupe1 = instance.getCase(new HexaPoint(0, 1, -1));
        Case caseOccupe2 = instance.getCase(new HexaPoint(1, -1, 0));

        try {
            Fourmi f1 = new Fourmi(j1); f1 .setEmplacement(caseOccupe1);
            Fourmi f2 = new Fourmi(j1); f2 .setEmplacement(caseOccupe2);
            instance.ajoutInsecte(f1, caseOccupe1.getCoordonnees());
            instance.ajoutInsecte(f2, caseOccupe2.getCoordonnees());
        } catch (Exception ex) {
            fail("L'ajout d'insecte a échoué");
        }

        result = new ArrayList<>();
        result.addAll(r.deplacementPossible(instance));

        arrayCorresponds(result, expectedWithGate);

        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

    }

    

    @Test
    public void testDeplacementScarabee() {
        System.out.println("=============================================");
        System.out.println("Test deplacementScarabee ======================>\n");
        HexaPoint point = new HexaPoint(0, 0, 0);
        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance,true,NumJoueur.JOUEUR1);
        
        Scarabee s = new Scarabee(j1);
        instance.ajoutInsecte(s, point);

        System.out.println("test déplacement avec seulement le scarabee :");
        ArrayList<Case> result = new ArrayList<>();
        result.addAll(s.deplacementPossible(instance));

        ArrayList<HexaPoint> expected = new ArrayList<>();
//        expected.add(new Point3DH(0, +1, -1));
//        expected.add(new Point3DH(+1, 0, -1));
//        expected.add(new Point3DH(+1, -1, 0));
//        expected.add(new Point3DH(0, -1, +1));
//        expected.add(new Point3DH(-1, 0, +1));
//        expected.add(new Point3DH(-1, +1, 0));

        arrayCorresponds(result, expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

//        expected.remove(new Point3DH(+1, 0, -1));


        creeCaseEtPlaceInsecte(instance, new Fourmi(j1), 0, 1, -1);
        creeCaseEtPlaceInsecte(instance, new Fourmi(j1), 1, -1, 0);

        System.out.println("test deplacement avec 2 cases occupées :");
        result = new ArrayList<>();
        result.addAll(s.deplacementPossible(instance));

        arrayCorresponds(result, new ArrayList<HexaPoint>());
        s.deplacement(instance, new HexaPoint(1, -1, 0));

        ArrayList<HexaPoint> newExpectation = new ArrayList<>();
//        newExpectation.add(new Point3DH(0, 0, 0));
//        newExpectation.add(new Point3DH(1, 0, -1));
//        newExpectation.add(new Point3DH(2, -1, -1));
//        newExpectation.add(new Point3DH(2, -2, 0));
//        newExpectation.add(new Point3DH(1, -2, 1));
//        newExpectation.add(new Point3DH(0, -1, 1));

        result = new ArrayList<>();
        result.addAll(s.deplacementPossible(instance));

        arrayCorresponds(result, newExpectation);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("");
    }

    @Test
    public void testDeplacementFourmi() {
        System.out.println("=============================================");
        System.out.println("Test deplacementFourmi ======================>\n");

        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance,true,NumJoueur.JOUEUR1);

        Case caseFourmi = instance.getCase(new HexaPoint(0, 0, 0));
        Fourmi f = new Fourmi(j1);
        instance.ajoutInsecte(f, new HexaPoint(0, 0, 0));
        f.setEmplacement(caseFourmi);
        
        System.out.println("test avec la fourmis seulement :");

        ArrayList<HexaPoint> expected = new ArrayList<>();
//        expected.add(new Point3DH(0, +1, -1));
//        expected.add(new Point3DH(+1, 0, -1));
//        expected.add(new Point3DH(+1, -1, 0));
//        expected.add(new Point3DH(0, -1, +1));
//        expected.add(new Point3DH(-1, 0, +1));
//        expected.add(new Point3DH(-1, +1, 0));

        arrayCorresponds(f.deplacementPossible(instance), expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        Case caseOccupe1 = instance.getCase(new HexaPoint(0, 1, -1));
        Case caseOccupe2 = instance.getCase(new HexaPoint(1, -1, 0));

        try {
            Scarabee s1 = new Scarabee(j1); s1.setEmplacement(caseOccupe1);
            Scarabee s2 = new Scarabee(j1); s2.setEmplacement(caseOccupe2);
            instance.ajoutInsecte(s1, caseOccupe1.getCoordonnees());
            instance.ajoutInsecte(s2, caseOccupe2.getCoordonnees());
        } catch (Exception ex) {
            fail("L'ajout d'insecte a échoué");
        }

        System.out.println("test deplacement avec 2 cases occupées et 1 déplacement impossible  :");
//        expected.remove(new Point3DH(0, 1, -1)); // Case occupée
//        expected.remove(new Point3DH(1, 0, -1)); // RUCHE BRISE
//        expected.remove(new Point3DH(1, -1, 0)); // Case occupée

        
        arrayCorresponds(f.deplacementPossible(instance), new ArrayList<>());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("");
    }
    
    @Test
    public void testDeplacementSauterelle() {
        System.out.println("=============================================");
        System.out.println("Test deplacementSauterelle =================>\n");

        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance,true,NumJoueur.JOUEUR1);

        HexaPoint orig = new HexaPoint(0,0,0);
        
        Reine reine = new Reine(j1);
        Sauterelle sauterelle = new Sauterelle(j1);
        
        ArrayList<Case> expected;
        ArrayList<Case> res;
        
        System.out.println("test reine à l'origine et sauterelle au-dessus :");
        instance.ajoutInsecte(reine, orig);
        instance.ajoutInsecte(sauterelle, orig.voisinHaut());
        
        expected = new ArrayList<>();
        expected.add(new Case(orig.voisinBas()));
        
        res = (ArrayList<Case>) sauterelle.deplacementPossible(instance);
        
        arrayCorresponds(res,expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test reine à l'origine et sauterelle au-dessus et reine encore au-dessus :");
        instance.ajoutInsecte(reine, orig.voisinHaut().voisinHaut());
        expected = new ArrayList<>();
        
        
        res = (ArrayList<Case>) sauterelle.deplacementPossible(instance);
        
        arrayCorresponds(res,expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("");
    }
    
    private void creeCaseEtPlaceInsecte(Plateau plateau, Insecte insecte, int x, int y, int z) {
        HexaPoint p = new HexaPoint(x, y, z);
        plateau.ajoutInsecte(insecte, p);
        
        insecte.setEmplacement(plateau.getCase(p));
    }
    
    @Test public void testDeplacementGrosseRuche() {
        //Ruche p.17; Sauterelle en 0, 0, 0 et fourmi blanche au dessus
        System.out.println("=============================================");
        System.out.println("Test deplacementGrooseRuche ======================>\n");

        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance,true,NumJoueur.JOUEUR1);
        Joueur j2 = new JoueurHumain(instance,true,NumJoueur.JOUEUR1);

        Sauterelle sauterelleBlanche = new Sauterelle(j1);
        Fourmi fourmiBlanche = new Fourmi(j1);
        Araignee araigneeBlanche = new Araignee(j1);
        Reine reineBlanche = new Reine(j1);
        Sauterelle sauterelleBlanche2 = new Sauterelle(j1);
        Sauterelle sauterelleBlanche3 = new Sauterelle(j1);
        Scarabee scarabeeNoire = new Scarabee(j2);
        Sauterelle sauterelleNoire = new Sauterelle(j2);
        Fourmi fourmiNoire = new Fourmi(j2);
        Reine reineNoire = new Reine(j2);
        Araignee araigneeNoire = new Araignee(j2);
        
        creeCaseEtPlaceInsecte(instance, sauterelleBlanche, 0, 0, 0);
        creeCaseEtPlaceInsecte(instance, fourmiBlanche, 0, 1, -1);
        creeCaseEtPlaceInsecte(instance, araigneeBlanche, -1, 1, 0);
        creeCaseEtPlaceInsecte(instance, araigneeNoire, 1, 1, -2);
        creeCaseEtPlaceInsecte(instance, reineBlanche, -2, 1, 1);
        creeCaseEtPlaceInsecte(instance, araigneeNoire, 1, 1, -2);
        creeCaseEtPlaceInsecte(instance, sauterelleBlanche2, 2, 0, -2);
        creeCaseEtPlaceInsecte(instance, sauterelleBlanche3, -2, 0, 2);
        creeCaseEtPlaceInsecte(instance, reineNoire, 2, -1, -1);
        creeCaseEtPlaceInsecte(instance, scarabeeNoire, 0, -1, 1);
        creeCaseEtPlaceInsecte(instance, sauterelleNoire, 1, -1, 0);
        creeCaseEtPlaceInsecte(instance, fourmiNoire, 2, -2, 0);
         
        ArrayList<HexaPoint> expectedFourmi = new ArrayList<>();
        expectedFourmi.add(new HexaPoint(1, 2, -3));
        expectedFourmi.add(new HexaPoint(2, 1, -3));
        expectedFourmi.add(new HexaPoint(3, 0, -3));
        expectedFourmi.add(new HexaPoint(3, -1, -2));
        expectedFourmi.add(new HexaPoint(3, -2, -1));
        expectedFourmi.add(new HexaPoint(3, -3, 0));
        expectedFourmi.add(new HexaPoint(2, -3, 1));
        expectedFourmi.add(new HexaPoint(1, -2, 1));
        expectedFourmi.add(new HexaPoint(0, -2, 2));
        expectedFourmi.add(new HexaPoint(-1, -1, 2));
        expectedFourmi.add(new HexaPoint(-2, -1, 3));
        expectedFourmi.add(new HexaPoint(-3, 0, 3));
        expectedFourmi.add(new HexaPoint(-3, 1, 2));
        expectedFourmi.add(new HexaPoint(-3, 2, 1));
        expectedFourmi.add(new HexaPoint(-2, 2, 0));
        expectedFourmi.add(new HexaPoint(-1, 2, -1));
        expectedFourmi.add(new HexaPoint(0, 2, -2));
        
        System.out.println("test deplacement Fourmi blanche sur grosse ruche :");
        arrayCorresponds(fourmiBlanche.deplacementPossible(instance), expectedFourmi);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        expectedFourmi = new ArrayList<>();
        expectedFourmi.add(new HexaPoint(1, 2, -3));
        expectedFourmi.add(new HexaPoint(2, 1, -3));
        expectedFourmi.add(new HexaPoint(3, 0, -3));
        expectedFourmi.add(new HexaPoint(3, -1, -2));
        expectedFourmi.add(new HexaPoint(3, -2, -1));
        expectedFourmi.add(new HexaPoint(1, -2, 1));
        expectedFourmi.add(new HexaPoint(0, -2, 2));
        expectedFourmi.add(new HexaPoint(-1, -1, 2));
        expectedFourmi.add(new HexaPoint(-2, -1, 3));
        expectedFourmi.add(new HexaPoint(-3, 0, 3));
        expectedFourmi.add(new HexaPoint(-3, 1, 2));
        expectedFourmi.add(new HexaPoint(-3, 2, 1));
        expectedFourmi.add(new HexaPoint(-2, 2, 0));
        expectedFourmi.add(new HexaPoint(-1, 2, -1));
        expectedFourmi.add(new HexaPoint(0, 2, -2));
        
        System.out.println("test deplacement Fourmi noire sur grosse ruche :");
        arrayCorresponds(fourmiNoire.deplacementPossible(instance), expectedFourmi);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedReineBlanche = new ArrayList<>();
        
        System.out.println("test deplacement Reine blanche sur grosse ruche :");
        arrayCorresponds(reineBlanche.deplacementPossible(instance), expectedReineBlanche);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedReineNoire = new ArrayList<>();
        expectedReineNoire.add(new HexaPoint(3, -1, -2));
        expectedReineNoire.add(new HexaPoint(3, -2, -1));
        
        System.out.println("test deplacement Reine noire sur grosse ruche :");
        arrayCorresponds(reineNoire.deplacementPossible(instance), expectedReineNoire);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedScarabeeNoire = new ArrayList<>();
        expectedScarabeeNoire.add(new HexaPoint(0, 0, 0));
        expectedScarabeeNoire.add(new HexaPoint(1, -1, 0));
        expectedScarabeeNoire.add(new HexaPoint(1, -2, 1));
        expectedScarabeeNoire.add(new HexaPoint(-1, 0, 1));
        
        System.out.println("test deplacement Scarabee noire sur grosse ruche :");
        arrayCorresponds(scarabeeNoire.deplacementPossible(instance), expectedScarabeeNoire);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedAraigneeNoire = new ArrayList<>();
        expectedAraigneeNoire.add(new HexaPoint(3, -1, -2));
        expectedAraigneeNoire.add(new HexaPoint(-2, 2, 0));
        
        System.out.println("test deplacement araignée noire sur grosse ruche :");
        ArrayList<Case> res = (ArrayList<Case>) araigneeNoire.deplacementPossible(instance);
        arrayCorresponds(res, expectedAraigneeNoire);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test deplacement araignée blanche sur grosse ruche :");
        arrayCorresponds(araigneeBlanche.deplacementPossible(instance), new ArrayList<>());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedSauterelleBlanche1 = new ArrayList<>();
        expectedSauterelleBlanche1.add(new HexaPoint(-2, 2, 0));
        expectedSauterelleBlanche1.add(new HexaPoint(0, -2, 2));
        expectedSauterelleBlanche1.add(new HexaPoint(3, -3, 0));
        expectedSauterelleBlanche1.add(new HexaPoint(0, 2, -2));
        
        System.out.println("test Sauterelle blanche n°1 sur grosse ruche :");
        arrayCorresponds(sauterelleBlanche.deplacementPossible(instance), expectedSauterelleBlanche1);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedSauterelleBlanche2 = new ArrayList<>();
        expectedSauterelleBlanche2.add(new HexaPoint(2, -3, 1));
        expectedSauterelleBlanche2.add(new HexaPoint(0, 2, -2));
        
        System.out.println("test Sauterelle blanche n°2 sur grosse ruche :");
        arrayCorresponds(sauterelleBlanche2.deplacementPossible(instance), expectedSauterelleBlanche2);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedSauterelleBlanche3 = new ArrayList<>();
        expectedSauterelleBlanche3.add(new HexaPoint(-2, 2, 0));
        
        System.out.println("test Sauterelle blanche n°3 sur grosse ruche :");
        arrayCorresponds(sauterelleBlanche3.deplacementPossible(instance), expectedSauterelleBlanche3);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        ArrayList<HexaPoint> expectedSauterelleNoire = new ArrayList<>();
        expectedSauterelleNoire.add(new HexaPoint(3, -1, -2));
        expectedSauterelleNoire.add(new HexaPoint(3, -3, 0));
        expectedSauterelleNoire.add(new HexaPoint(-2, 2, 0));
        expectedSauterelleNoire.add(new HexaPoint(-1, -1, 2));
        
        System.out.println("test Sauterelle noire sur grosse ruche :");
        arrayCorresponds(sauterelleNoire.deplacementPossible(instance), expectedSauterelleNoire);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
    }
    
    @Test public void testDeplacementGrosseRuche2() {
        
    }
    
    private void arrayCorresponds(Collection<Case> result, ArrayList<HexaPoint> pointsExpected) {
        ArrayList<HexaPoint> copie = new ArrayList<>(pointsExpected);
        assertTrue(pointsExpected.size() == result.size());
        for (Case c : result) {
            assertTrue(pointsExpected.contains(c.getCoordonnees()));
            copie.remove(c.getCoordonnees());
        }

        assertTrue(copie.isEmpty());
    }
    
    private void arrayCorresponds(ArrayList<Case> result, ArrayList<Case> Expected) {
        assertTrue(Expected.size() == result.size());
        for (Case c : result) {
            assertTrue(Expected.contains(c));
            Expected.remove(c);
        }

        assertTrue(Expected.isEmpty());
    }
}
