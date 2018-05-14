/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Insectes.*;
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
        plat.ajoutInsecte(fourmi, new Point3DH(0, 0, 0));
        assertFalse(instance.reinePosee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test avec la reine posé :");
        plat.ajoutInsecte(reine, new Point3DH(0, -1, 1));
        assertTrue(instance.reinePosee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
    }

    /**
     * Test of pionsEnMain method, of class Joueur.
     */
    @Test
    public void testPionsEnMain() {
        System.out.println("=============================================");
        System.out.println("Test pionsEnMain  ==========================>\n");

        System.out.println("Pions en Mains : 2xScarabee 3xFourmis 1xReine 2xSauterelle\n");

        Plateau plat = new Plateau();
        JoueurHumain instance = new JoueurHumain(plat);
        Fourmi fourmi1 = new Fourmi(instance);
        Fourmi fourmi2 = new Fourmi(instance);
        Fourmi fourmi3 = new Fourmi(instance);
        Reine reine = new Reine(instance);
        Scarabee scarabee1 = new Scarabee(instance);
        Scarabee scarabee2 = new Scarabee(instance);
        Sauterelle sauterelle1 = new Sauterelle(instance);
        Sauterelle sauterelle2 = new Sauterelle(instance);
        
        instance.pions.add(fourmi1);
        instance.pions.add(fourmi2);
        instance.pions.add(fourmi3);
        instance.pions.add(reine);
        instance.pions.add(scarabee1);
        instance.pions.add(scarabee2);
        instance.pions.add(sauterelle1);
        instance.pions.add(sauterelle2);
        
        System.out.println("test avec tout en main :");
        
        ArrayList<Insecte> expected = new ArrayList<>();
        expected.add(fourmi1);
        expected.add(fourmi2);
        expected.add(fourmi3);
        expected.add(reine);
        expected.add(scarabee1);
        expected.add(scarabee2);
        expected.add(sauterelle1);
        expected.add(sauterelle2);

        ArrayList<Insecte> res = instance.pionsEnMain();
        
        arrayCorresponds(res,expected);
        
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test en ayant posé la reine :");
        
        expected = new ArrayList<>();
        expected.add(fourmi1);
        expected.add(fourmi2);
        expected.add(fourmi3);
        expected.add(scarabee1);
        expected.add(scarabee2);
        expected.add(sauterelle1);
        expected.add(sauterelle2);

        plat.ajoutInsecte(reine, new Point3DH(0,0,0));
        
        res = instance.pionsEnMain();
        
        arrayCorresponds(res,expected);
        
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test en ayant posé la reine les scarabee et une sauterelle");
        
         expected = new ArrayList<>();
        expected.add(fourmi1);
        expected.add(fourmi2);
        expected.add(fourmi3);
        expected.add(sauterelle2);
        
        plat.ajoutInsecte(scarabee1, new Point3DH(0,0,0).voisinBas());
        plat.ajoutInsecte(scarabee2, new Point3DH(0,0,0).voisinDroiteBas());
        plat.ajoutInsecte(sauterelle1, new Point3DH(0,0,0).voisinDroiteHaut());
        
        res = instance.pionsEnMain();
        
        arrayCorresponds(res,expected);
        
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test en ayant tout posé :");
        
        expected = new ArrayList<>();
        
        plat.ajoutInsecte(fourmi1, new Point3DH(0,0,0).voisinGaucheHaut());
        plat.ajoutInsecte(fourmi2, new Point3DH(0,0,0).voisinHaut());
        plat.ajoutInsecte(fourmi3, new Point3DH(0,0,0).voisinHaut().voisinHaut());
        plat.ajoutInsecte(sauterelle2, new Point3DH(0,0,0).voisinGaucheBas());
        
        res = instance.pionsEnMain();
        
        arrayCorresponds(res,expected);
        
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("");
    }
    
    private void arrayCorresponds(ArrayList<Insecte> result, ArrayList<Insecte> Expected) {
        assertTrue(Expected.size() == result.size());
        for (Insecte i : result) {
            assertTrue(Expected.contains(i));
            Expected.remove(i);
        }

        assertTrue(Expected.isEmpty());
    }
}
