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
public class JoueurTest {

    public JoueurTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("=============================================");
        System.out.println("====            TEST JOUEUR              ====");
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
     * Test of reinePosee method, of class Joueur.
     */
    @Test
    public void testReinePosee() {
        System.out.println("=============================================");
        System.out.println("Test reinePosee  ===========================>\n");

        Plateau plat = new Plateau();
        JoueurHumain instance = new JoueurHumain(plat);
        Fourmi fourmi = new Fourmi(instance);
        Reine reine = new Reine(instance);
        
        instance.pions.add(fourmi);
        instance.pions.add(reine);
        
        System.out.println("test avec plateau vide :");
        assertFalse(instance.reinePosee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test avec une fourmi posé :");
        plat.ajoutInsecte(fourmi, new Point3DH(0,0,0));
        assertFalse(instance.reinePosee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test avec la reine posé :");
        plat.ajoutInsecte(reine, new Point3DH(0,1,0));
        assertTrue(instance.reinePosee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
    }

}
