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
        System.out.println("====            TEST PLATEAU             ====");
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
        System.out.println("test avec l'origine (cree au debut) :");
        Case expResult = new Case(point);
        assertTrue(expResult.equals(instance.getCase(point)));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test avec une case éloigner (pas encore crée) :");
        point = new Point3DH(2, 5, 6);
        expResult = new Case(point);
        assertTrue(expResult.equals(instance.getCase(point)));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        System.out.println("");
    }

    // ajout et delete insecte deja testé dans Testcase
    /**
     * Test of getCasesVoisines method, of class Plateau.
     */
    @Test
    public void testGetCasesVoisines() {
        System.out.println("=============================================");
        System.out.println("Test getCasesVoisines ======================>\n");
        Point3DH orig = new Point3DH(0,0,0);
        Plateau instance = new Plateau();
        
        System.out.println("test avec l'origine, libre ou non :");
        ArrayList<Case> expected = new ArrayList<>();
        expected.add(new Case(orig.voisinBas()));
        expected.add(new Case(orig.voisinDroiteBas()));
        expected.add(new Case(orig.voisinDroiteHaut()));
        expected.add(new Case(orig.voisinGaucheBas()));
        expected.add(new Case(orig.voisinGaucheHaut()));
        expected.add(new Case(orig.voisinHaut()));
        
        ArrayList<Case> res = (ArrayList<Case>) instance.getCasesVoisines(new Case(orig), false);
        
        arrayCorresponds(res,expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("");
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
