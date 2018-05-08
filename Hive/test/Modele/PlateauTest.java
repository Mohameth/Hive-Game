/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Insectes.Insecte;
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
