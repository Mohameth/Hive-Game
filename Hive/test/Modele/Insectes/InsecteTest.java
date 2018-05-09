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
     *  Test des déplacements de la reine
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
        expectedWithGate.add(new Point3DH(0, -1, +1));
        expectedWithGate.add(new Point3DH(-1, 0, +1));
        expectedWithGate.add(new Point3DH(-1, +1, 0));

        Case caseOccupe1 = instance.getCase(new Point3DH(0, 1, -1));
        Case caseOccupe2 = instance.getCase(new Point3DH(1, -1, 0));

        try {
            caseOccupe1.addInsecte(new Fourmi(j1));
            caseOccupe2.addInsecte(new Fourmi(j1));
        } catch (Exception ex) {
            fail("exception anormal");
        }

        result = new ArrayList<>();
        result.addAll(r.deplacementPossible(instance));

        arrayCorresponds(result, expectedWithGate);
        
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

    }

    private void arrayCorresponds(ArrayList<Case> result, ArrayList<Point3DH> pointsExpected) {
        assertTrue(pointsExpected.size() == result.size());
        for (Case c : result) {
            assertTrue(pointsExpected.contains(c.getCoordonnees()));
            pointsExpected.remove(c.getCoordonnees());
        }

        assertTrue(pointsExpected.isEmpty());
    }
    
}
