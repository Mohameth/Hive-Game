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
import Modele.Point3DH;
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
        Point3DH point = new Point3DH(0, 0, 0);
        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance);

        Case caseReine = instance.getCase(point);
        Reine r = new Reine(j1);
        instance.ajoutInsecte(r, point);
        r.setEmplacement(caseReine);

        ArrayList<Case> result = new ArrayList<>();
        result.addAll(r.deplacementPossible(instance));

        System.out.println("test avec tout les voisin libre :");
        ArrayList<Point3DH> expected = new ArrayList<>();
        expected.add(new Point3DH(0, +1, -1));
        expected.add(new Point3DH(+1, 0, -1));
        expected.add(new Point3DH(+1, -1, 0));
        expected.add(new Point3DH(0, -1, +1));
        expected.add(new Point3DH(-1, 0, +1));
        expected.add(new Point3DH(-1, +1, 0));

        arrayCorresponds(result, expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test avec 2 case occupé et 1 case inaccessible :");
        //TEST GATE
        ArrayList<Point3DH> expectedWithGate = new ArrayList<>();
        //expectedWithGate.add(new Point3DH(0, +1, -1)); //--> Case occupe
        //expectedWithGate.add(new Point3DH(+1, 0, -1)); //--> Inaccessible (gate)
        //expectedWithGate.add(new Point3DH(+1, -1, 0)); //--> Case occupe
        //expectedWithGate.add(new Point3DH(0, -1, +1));
        //expectedWithGate.add(new Point3DH(-1, 0, +1));
        //expectedWithGate.add(new Point3DH(-1, +1, 0));

        Case caseOccupe1 = instance.getCase(new Point3DH(0, 1, -1));
        Case caseOccupe2 = instance.getCase(new Point3DH(1, -1, 0));

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
        Point3DH point = new Point3DH(0, 0, 0);
        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance);
        
        Scarabee s = new Scarabee(j1);
        instance.ajoutInsecte(s, point);

        System.out.println("test déplacement avec seulement le scarabee :");
        ArrayList<Case> result = new ArrayList<>();
        result.addAll(s.deplacementPossible(instance));

        ArrayList<Point3DH> expected = new ArrayList<>();
        expected.add(new Point3DH(0, +1, -1));
        expected.add(new Point3DH(+1, 0, -1));
        expected.add(new Point3DH(+1, -1, 0));
        expected.add(new Point3DH(0, -1, +1));
        expected.add(new Point3DH(-1, 0, +1));
        expected.add(new Point3DH(-1, +1, 0));

        arrayCorresponds(result, expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        expected.remove(new Point3DH(+1, 0, -1));

        Point3DH Occupe1 = new Point3DH(0, 1, -1);
        Point3DH Occupe2 = new Point3DH(1, -1, 0);

        instance.ajoutInsecte(new Fourmi(j1), Occupe1);
        instance.ajoutInsecte(new Fourmi(j1), Occupe2);

        System.out.println("test deplacement avec 2 cases occupées :");
        result = new ArrayList<>();
        result.addAll(s.deplacementPossible(instance));

        arrayCorresponds(result, new ArrayList<>());
        s.deplacement(instance, Occupe2);

        ArrayList<Point3DH> newExpectation = new ArrayList<>();
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
        Joueur j1 = new JoueurHumain(instance);

        Case caseFourmi = instance.getCase(new Point3DH(0, 0, 0));
        Fourmi f = new Fourmi(j1);
        instance.ajoutInsecte(f, new Point3DH(0, 0, 0));
        f.setEmplacement(caseFourmi);
        
        System.out.println("test avec la fourmis seulement :");

        ArrayList<Point3DH> expected = new ArrayList<>();
        expected.add(new Point3DH(0, +1, -1));
        expected.add(new Point3DH(+1, 0, -1));
        expected.add(new Point3DH(+1, -1, 0));
        expected.add(new Point3DH(0, -1, +1));
        expected.add(new Point3DH(-1, 0, +1));
        expected.add(new Point3DH(-1, +1, 0));

        arrayCorresponds(f.deplacementPossible(instance), expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        Case caseOccupe1 = instance.getCase(new Point3DH(0, 1, -1));
        Case caseOccupe2 = instance.getCase(new Point3DH(1, -1, 0));

        try {
            Scarabee s1 = new Scarabee(j1); s1.setEmplacement(caseOccupe1);
            Scarabee s2 = new Scarabee(j1); s2.setEmplacement(caseOccupe2);
            instance.ajoutInsecte(s1, caseOccupe1.getCoordonnees());
            instance.ajoutInsecte(s2, caseOccupe2.getCoordonnees());
        } catch (Exception ex) {
            fail("L'ajout d'insecte a échoué");
        }

        System.out.println("test deplacement avec 2 cases et 1 déplacement impossible occupées :");
//        expected.remove(new Point3DH(0, 1, -1)); // Case occupée
//        expected.remove(new Point3DH(1, 0, -1)); // RUCHE BRISE
//        expected.remove(new Point3DH(1, -1, 0)); // Case occupée

        
        arrayCorresponds(f.deplacementPossible(instance), new ArrayList<>());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("");
    }
    
    private void arrayCorresponds(Collection<Case> result, ArrayList<Point3DH> pointsExpected) {
        ArrayList<Point3DH> copie = new ArrayList<>(pointsExpected);
        assertTrue(pointsExpected.size() == result.size());
        for (Case c : result) {
            assertTrue(pointsExpected.contains(c.getCoordonnees()));
            copie.remove(c.getCoordonnees());
        }

        assertTrue(copie.isEmpty());
    }
}
