/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.IA.IAEvaluation;
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
        JoueurHumain instance = new JoueurHumain(plat, true);
        Fourmi fourmi = new Fourmi(instance);
        Reine reine = new Reine(instance);

        instance.pions.clear();
        instance.pions.add(fourmi);
        instance.pions.add(reine);

        System.out.println("test avec plateau vide :");
        assertFalse(instance.reinePosee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test avec une fourmi posé :");
        plat.ajoutInsecte(fourmi, new HexaPoint(0, 0, 0));
        assertFalse(instance.reinePosee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test avec la reine posé :");
        plat.ajoutInsecte(reine, new HexaPoint(0, -1, 1));
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
        JoueurHumain instance = new JoueurHumain(plat, true);
        Fourmi fourmi1 = new Fourmi(instance);
        Fourmi fourmi2 = new Fourmi(instance);
        Fourmi fourmi3 = new Fourmi(instance);
        Reine reine = new Reine(instance);
        Scarabee scarabee1 = new Scarabee(instance);
        Scarabee scarabee2 = new Scarabee(instance);
        Sauterelle sauterelle1 = new Sauterelle(instance);
        Sauterelle sauterelle2 = new Sauterelle(instance);

        instance.pions.clear();
        
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

        arrayCorresponds(res, expected);

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

        plat.ajoutInsecte(reine, new HexaPoint(0, 0, 0));

        res = instance.pionsEnMain();

        arrayCorresponds(res, expected);

        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test en ayant posé la reine les scarabee et une sauterelle");

        expected = new ArrayList<>();
        expected.add(fourmi1);
        expected.add(fourmi2);
        expected.add(fourmi3);
        expected.add(sauterelle2);

        plat.ajoutInsecte(scarabee1, new HexaPoint(0, 0, 0).voisinBas());
        plat.ajoutInsecte(scarabee2, new HexaPoint(0, 0, 0).voisinDroiteBas());
        plat.ajoutInsecte(sauterelle1, new HexaPoint(0, 0, 0).voisinDroiteHaut());

        res = instance.pionsEnMain();

        arrayCorresponds(res, expected);

        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("test en ayant tout posé :");

        expected = new ArrayList<>();

        plat.ajoutInsecte(fourmi1, new HexaPoint(0, 0, 0).voisinGaucheHaut());
        plat.ajoutInsecte(fourmi2, new HexaPoint(0, 0, 0).voisinHaut());
        plat.ajoutInsecte(fourmi3, new HexaPoint(0, 0, 0).voisinHaut().voisinHaut());
        plat.ajoutInsecte(sauterelle2, new HexaPoint(0, 0, 0).voisinGaucheBas());

        res = instance.pionsEnMain();

        arrayCorresponds(res, expected);

        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("");
    }
    
    @Test
    public void testReineBloque() {
        System.out.println("=============================================");
        System.out.println("Test reineBloque  ==========================>\n");

        Plateau plat = new Plateau();
        JoueurHumain j1 = new JoueurHumain(plat, true);
        JoueurHumain j2 = new JoueurHumain(plat, true);
        Fourmi fourmi1 = (Fourmi) j1.getPions().get(5);
        Fourmi fourmi2 = (Fourmi) j1.getPions().get(7);
        Fourmi fourmi3 = (Fourmi) j1.getPions().get(9);
        Reine reineJ1 = (Reine) j1.getPions().get(0);
        Reine reineJ2 = (Reine) j2.getPions().get(0);
        Scarabee scarabee1 = (Scarabee) j1.getPions().get(1);
        Scarabee scarabee2 = (Scarabee) j1.getPions().get(3);
        
        j1.pions.add(fourmi1);
        j1.pions.add(fourmi2);
        j1.pions.add(fourmi3);
        j1.pions.add(reineJ1);
        j2.pions.add(reineJ2);
        j1.pions.add(scarabee1);
        j1.pions.add(scarabee2);

        plat.ajoutInsecte(reineJ2, new HexaPoint(0, 0, 0));
        
        plat.ajoutInsecte(reineJ1, new HexaPoint(0, 1, -1));
        plat.ajoutInsecte(fourmi1, new HexaPoint(1, 0, -1));
        plat.ajoutInsecte(fourmi2, new HexaPoint(1, -1, 0));
        plat.ajoutInsecte(fourmi3, new HexaPoint(0, -1, 1));
        plat.ajoutInsecte(scarabee1, new HexaPoint(-1, 0, 1));
        plat.ajoutInsecte(scarabee2, new HexaPoint(-1, 1, 0));
        
        System.out.println("test reine bloqué :");
        assertTrue(j2.reineBloquee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        int eval1 = new IAEvaluation(plat, j1, j2).getEvaluation();
        int eval2 = new IAEvaluation(plat, j2, j1).getEvaluation();
        System.out.println("J1: " + eval1 + " J2: " + eval2);
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
