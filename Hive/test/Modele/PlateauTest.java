/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Insectes.Fourmi;
import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PlateauTest {
    
    public PlateauTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
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
     * Test of getCase method, of class Plateau.
     */
    @Test
    public void testGetCase() {
       System.out.println("getCase");
        Point3DH point = new Point3DH(0,0,0);
        Plateau instance = new Plateau();
        Case expResult = instance.getCase(point);
        Case result = instance.getCase(point);
        assertEquals(expResult, result);
        
    }
    
        
     /**
      * TODO : Put into other test file
     * Test of deplacementPossible method, of class Reine.
     */
    @Test
    public void testDeplacementReine() {
        System.out.println("deplacementReine");
        Point3DH point = new Point3DH(0,0,0);
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
            fail();
        }
        
        result = new ArrayList<>(); 
        result.addAll(r.deplacementPossible(instance));
        
        arrayCorresponds(result, expectedWithGate);
        
    }
    
    private void arrayCorresponds(ArrayList<Case> result, ArrayList<Point3DH> pointsExpected) {
        assertTrue(pointsExpected.size() == result.size());
        for (Case c : result) {
           assertTrue(pointsExpected.contains(c.getCoordonnees()));
           pointsExpected.remove(c.getCoordonnees());
        }
        
        assertTrue(pointsExpected.isEmpty());
    }
    /**
     * Test of caseEstVide method, of class Plateau.
     */
    /*@Test
    public void testCaseEstVide() {
        System.out.println("caseEstVide");
        Point3D point = null;
        Plateau instance = new Plateau();
        boolean expResult = false;
        boolean result = instance.caseEstVide(point);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of ajoutInsecte method, of class Plateau.
     */
    /*@Test
    public void testAjoutInsecte() {
        System.out.println("ajoutInsecte");
        Insecte insecte = null;
        Point3D position = null;
        Plateau instance = new Plateau();
        instance.ajoutInsecte(insecte, position);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteInsecte method, of class Plateau.
     */
    /*@Test
    public void testDeleteInsecte() {
        System.out.println("deleteInsecte");
        Insecte insecte = null;
        Point3D position = null;
        Plateau instance = new Plateau();
        instance.deleteInsecte(insecte, position);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
    
}
