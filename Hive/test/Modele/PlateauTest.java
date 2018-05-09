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
        System.out.println("=============================================");
        System.out.println("====               TEST PLATEAU          ====");
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
     * Test of getCase method, of class Plateau.
     */
    @Test
    public void testGetCase() {
        System.out.println("=============================================");
        System.out.println("Test getCase ===============================>\n");
        Point3DH point = new Point3DH(0, 0, 0);
        Plateau instance = new Plateau();
        Case expResult = instance.getCase(point);
        Case result = instance.getCase(point);
        assertEquals(expResult, result);

    }
    
}
