/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

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
public class Point3DHTest {

    public Point3DHTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("=============================================");
        System.out.println("====          TEST Point3DH              ====");
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
     * Test of rucheBrisee method, of class Plateau.
     */
    @Test
    public void testVoisin() {
        System.out.println("=============================================");
        System.out.println("Test getVoisin =============================>\n");
        Point3DH orig = new Point3DH(0, 0, 0);

        ArrayList<Point3DH> expected = new ArrayList<>();
        expected.add(new Point3DH(0, -1, 1)); //Bas
        expected.add(new Point3DH(1, -1, 0)); //Bas droite
        expected.add(new Point3DH(1, 0, -1)); //Haut droite
        expected.add(new Point3DH(0, 1, -1)); //Haut
        expected.add(new Point3DH(-1, 1, 0)); //Haut gauche
        expected.add(new Point3DH(-1, 0, 1)); //Bas gauche

        System.out.println("test des voisins :");
        ArrayList<Point3DH> res = (ArrayList<Point3DH>) orig.coordonneesVoisins();

        arrayCorresponds(res, expected);

        expected = new ArrayList<>();
        expected.add(new Point3DH(0, -1, 1)); //Bas
        expected.add(new Point3DH(1, -1, 0)); //Bas droite
        expected.add(new Point3DH(1, 0, -1)); //Haut droite
        expected.add(new Point3DH(0, 1, -1)); //Haut
        expected.add(new Point3DH(-1, 1, 0)); //Haut gauche
        expected.add(new Point3DH(-1, 0, 1)); //Bas gauche
        
        res = new ArrayList<>();
        res.add(orig.voisinBas());
        res.add(orig.voisinDroiteBas());
        res.add(orig.voisinDroiteHaut());
        res.add(orig.voisinHaut());
        res.add(orig.voisinGaucheHaut());
        res.add(orig.voisinGaucheBas());

        arrayCorresponds(res, expected);
        
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        System.out.println("");
    }

    private void arrayCorresponds(ArrayList<Point3DH> result, ArrayList<Point3DH> Expected) {
        assertTrue(Expected.size() == result.size());
        for (Point3DH p : result) {
            assertTrue(Expected.contains(p));
            Expected.remove(p);
        }

        assertTrue(Expected.isEmpty());
    }

}
