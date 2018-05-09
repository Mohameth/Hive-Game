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
        r.setEmplacement(caseReine);

        ArrayList<Case> result = new ArrayList<>();
        result.addAll(r.deplacementPossible(instance));

        ArrayList<Point3DH> expected = new ArrayList<>();
        expected.add(new Point3DH(0, +1, -1));
        expected.add(new Point3DH(+1, 0, -1));
        expected.add(new Point3DH(+1, -1, 0));
        expected.add(new Point3DH(0, -1, +1));
        expected.add(new Point3DH(-1, 0, +1));
        expected.add(new Point3DH(-1, +1, 0));

        arrayCorresponds(result, expected);

        //TEST GATE
        ArrayList<Point3DH> expectedWithGate = new ArrayList<>();
        //expectedWithGate.add(new Point3DH(0, +1, -1)); //--> Case occupe
        //expectedWithGate.add(new Point3DH(+1, 0, -1)); //--> Inaccessible (gate)
        //expectedWithGate.add(new Point3DH(+1, -1, 0)); //--> Case occupe
        expectedWithGate.add(new Point3DH(0, -1, +1));
        expectedWithGate.add(new Point3DH(-1, 0, +1));
        expectedWithGate.add(new Point3DH(-1, +1, 0));

        Case caseOccupe1 = instance.getCase(new Point3DH(0, 1, -1));
        Case caseOccupe2 = instance.getCase(new Point3DH(1, -1, 0));

        try {
            caseOccupe1.addInsecte(new Fourmi(j1));
            caseOccupe2.addInsecte(new Fourmi(j1));
        } catch (Exception ex) {
            fail("L'ajout d'insecte a échoué");
        }

        result = new ArrayList<>();
        result.addAll(r.deplacementPossible(instance));

        arrayCorresponds(result, expectedWithGate);

    }

    private void arrayCorresponds(ArrayList<Case> result, ArrayList<Point3DH> pointsExpected) {
        ArrayList<Point3DH> copie = new ArrayList<>(pointsExpected);
        assertTrue(pointsExpected.size() == result.size());
        for (Case c : result) {
            assertTrue(pointsExpected.contains(c.getCoordonnees()));
            copie.remove(c.getCoordonnees());
        }

        assertTrue(copie.isEmpty());
    }
    
    @Test
    public void testDeplacementScarabee() {
        System.out.println("=============================================");
        System.out.println("Test deplacementScarabee ======================>\n");
        Point3DH point = new Point3DH(0, 0, 0);
        Plateau instance = new Plateau();
        Joueur j1 = new JoueurHumain(instance);

        Case caseScarabee = instance.getCase(point);
        Scarabee s = new Scarabee(j1);
        s.setEmplacement(caseScarabee);

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
        
        expected.remove(new Point3DH(+1, 0, -1));
        
        Case caseOccupe1 = instance.getCase(new Point3DH(0, 1, -1));
        Case caseOccupe2 = instance.getCase(new Point3DH(1, -1, 0));

        try {
            caseOccupe1.addInsecte(new Fourmi(j1));
            caseOccupe2.addInsecte(new Fourmi(j1));
        } catch (Exception ex) {
            fail("L'ajout d'insecte a échoué");
        }

        result = new ArrayList<>();
        result.addAll(s.deplacementPossible(instance));
        
        arrayCorresponds(result, expected);
        s.deplacement(instance, caseOccupe2);
        
        ArrayList<Point3DH> newExpectation = new ArrayList<>();
        newExpectation.add(new Point3DH(0, 0, 0));
        newExpectation.add(new Point3DH(1, 0, -1));
        newExpectation.add(new Point3DH(2, -1, -1));
        newExpectation.add(new Point3DH(2, -2, 0));
        newExpectation.add(new Point3DH(1, -2, 1));
        newExpectation.add(new Point3DH(0, -1, 1));
        
        result = new ArrayList<>();
        result.addAll(s.deplacementPossible(instance));
        
        arrayCorresponds(result, expected);
    }
}