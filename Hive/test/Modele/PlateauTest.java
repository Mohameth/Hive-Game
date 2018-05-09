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
        Reine reine = new Reine(new JoueurHumain(instance));
        
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
        
        System.out.println("test avec l'origine, libre seulement (tout est libre, case du haut crée) :");
        expected = new ArrayList<>();
        expected.add(new Case(orig.voisinBas()));
        expected.add(new Case(orig.voisinDroiteBas()));
        expected.add(new Case(orig.voisinDroiteHaut()));
        expected.add(new Case(orig.voisinGaucheBas()));
        expected.add(new Case(orig.voisinGaucheHaut()));
        expected.add(new Case(orig.voisinHaut()));
        
        instance.getCase(orig.voisinHaut());
        
        res = (ArrayList<Case>) instance.getCasesVoisines(new Case(orig), true);
        
        arrayCorresponds(res,expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test avec l'origine, libre seulement (haut et bas occupé) :");
        expected = new ArrayList<>();
        expected.add(new Case(orig.voisinDroiteBas()));
        expected.add(new Case(orig.voisinDroiteHaut()));
        expected.add(new Case(orig.voisinGaucheBas()));
        expected.add(new Case(orig.voisinGaucheHaut()));
        
        instance.ajoutInsecte(reine, orig.voisinHaut());
        instance.ajoutInsecte(reine, orig.voisinBas());
        
        res = (ArrayList<Case>) instance.getCasesVoisines(new Case(orig), true);
        
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

    
     /**
     * Test of getCasesVoisinesOccupees method, of class Plateau.
     */
    @Test
    public void testGetCasesVoisinesOccupees() {
        System.out.println("=============================================");
        System.out.println("Test getCasesVoisinesOccupees ==============>\n");
        
        Point3DH orig = new Point3DH(0,0,0);
        Plateau instance = new Plateau();
        Reine reine = new Reine(new JoueurHumain(instance));
        
        System.out.println("test avec l'origine, haut et bas occupé :");
        ArrayList<Case> expected = new ArrayList<>();
        expected.add(new Case(orig.voisinBas()));
        expected.add(new Case(orig.voisinHaut()));
        
        instance.ajoutInsecte(reine, orig.voisinBas());
        instance.ajoutInsecte(reine, orig.voisinHaut());
        
        ArrayList<Case> res = (ArrayList<Case>) instance.getCasesVoisinesOccupees(new Case(orig));
        
        arrayCorresponds(res,expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test avec l'origine, tout est occupé :");
        expected = new ArrayList<>();
        expected.add(new Case(orig.voisinBas()));
        expected.add(new Case(orig.voisinDroiteBas()));
        expected.add(new Case(orig.voisinDroiteHaut()));
        expected.add(new Case(orig.voisinGaucheBas()));
        expected.add(new Case(orig.voisinGaucheHaut()));
        expected.add(new Case(orig.voisinHaut()));
        
        instance.ajoutInsecte(reine, orig.voisinDroiteBas());
        instance.ajoutInsecte(reine, orig.voisinDroiteHaut());
        instance.ajoutInsecte(reine, orig.voisinGaucheBas());
        instance.ajoutInsecte(reine, orig.voisinGaucheHaut());
        
        res = (ArrayList<Case>) instance.getCasesVoisinesOccupees(new Case(orig));
        
        arrayCorresponds(res,expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test avec l'origine, tout est libre :");
        expected = new ArrayList<>();
        
        instance.deleteInsecte(reine, orig.voisinHaut());
        instance.deleteInsecte(reine, orig.voisinGaucheHaut());
        instance.deleteInsecte(reine, orig.voisinGaucheBas());
        instance.deleteInsecte(reine, orig.voisinDroiteHaut());
        instance.deleteInsecte(reine, orig.voisinDroiteBas());
        instance.deleteInsecte(reine, orig.voisinBas());
        
        res = (ArrayList<Case>) instance.getCasesVoisinesOccupees(new Case(orig));
        
        arrayCorresponds(res,expected);
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("");
    }
    
    /**
     * Test of gateBetween method, of class Plateau.
     */
    @Test
    public void testGateBetween() {
        System.out.println("=============================================");
        System.out.println("Test gateBetween ===========================>\n");
        
        Point3DH orig = new Point3DH(0,0,0);
        Plateau instance = new Plateau();
        Reine reine = new Reine(new JoueurHumain(instance));
        
        instance.ajoutInsecte(reine, orig);
        instance.ajoutInsecte(reine, orig.voisinDroiteBas());
        instance.ajoutInsecte(reine, orig.voisinGaucheBas());
        instance.ajoutInsecte(reine, orig.voisinBas().voisinBas());
        
        System.out.println("Test dans une configuration (ocupper: origine -> bas droite -> bas gauche -> bas bas) :\n");
        
        System.out.println("test deplacement origine vers le bas (impossible) : ");
        assertTrue(instance.gateBetween(new Case(orig), new Case(orig.voisinBas())));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test deplacement origine.basdroite vers haut droit (possible) :");
        assertFalse(instance.gateBetween(new Case(orig.voisinDroiteBas()), new Case(orig.voisinDroiteBas().voisinDroiteHaut())));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        instance.deleteInsecte(reine, orig);
        instance.ajoutInsecte(reine, orig.voisinBas());
        System.out.println("nouvelle configuration (ocupper: bas -> bas droite -> bas gauche -> bas bas) :\n");
        
        System.out.println("test deplacement bas vers bas.bas droite (impossible) :");
        assertTrue(instance.gateBetween(new Case(orig.voisinBas()), new Case(orig.voisinBas().voisinDroiteBas())));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test deplacement bas vers bas.bas gauche (impossible) :");
        assertTrue(instance.gateBetween(new Case(orig.voisinBas()), new Case(orig.voisinBas().voisinGaucheBas())));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
     
        System.out.println("");
    }
    
    /**
     * Test of rucheBrisee method, of class Plateau.
     */
    @Test
    public void testRucheBrisee() {
        System.out.println("=============================================");
        System.out.println("Test rucheBrisee ===========================>\n");
        
        Point3DH orig = new Point3DH(0,0,0);
        Plateau instance = new Plateau();
        Reine reine = new Reine(new JoueurHumain(instance));
        
        System.out.println("test sur une ruche vide :");
        assertFalse(instance.rucheBrisee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test sur une ruche avec un insecte :");
        instance.ajoutInsecte(reine, orig);
        assertFalse(instance.rucheBrisee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test sur une ruche avec deux insectes collés :");
        instance.ajoutInsecte(reine, orig.voisinBas());
        assertFalse(instance.rucheBrisee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        
        System.out.println("test sur une ruche avec deux insectes non collés :");
        instance.deleteInsecte(reine, orig.voisinBas());
        instance.ajoutInsecte(reine, orig.voisinBas().voisinBas());
        assertTrue(instance.rucheBrisee());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
    }
}
