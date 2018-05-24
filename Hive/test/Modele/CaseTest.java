/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Modele.Joueurs.NumJoueur;
import Modele.Joueurs.JoueurHumain;
import Modele.Insectes.Fourmi;
import Modele.Insectes.Insecte;
import Modele.Insectes.Moustique;
import Modele.Insectes.Reine;
import Modele.Insectes.Scarabee;
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
public class CaseTest {

    public CaseTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("=============================================");
        System.out.println("====               TEST CASE             ====");
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
     * Test of estVide method, of class Case.
     */
    @Test
    public void testEstVide() {
        System.out.println("=============================================");
        System.out.println("TEST estVide : =============================>\n");
        Case instance = new Case(new HexaPoint(0, 0, 0));
        System.out.println("Test sur une case vide :");
        assertTrue(instance.estVide());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("ajout d'un insecte :");
        try {
            instance.addInsecte(new Reine(new JoueurHumain(new Plateau(), true, NumJoueur.JOUEUR1)));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        } catch (Exception ex) {
            fail("exception anormal");
        }
        System.out.println("Test sur une case non vide :");
        assertFalse(instance.estVide());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        try {
            instance.removeInsecte();
        } catch (Exception ex) {
            fail("exeption anormal");
        }

        System.out.println("Test aprés avoir supprimé l'insecte :");
        assertTrue(instance.estVide());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("");
    }

    /**
     * Test of addInsecte method, of class Case.
     */
    @Test
    public void testAddRemInsecte() {
        System.out.println("=============================================");
        System.out.println("TEST ajout suppretion d'Insecte : ==========>\n");
        JoueurHumain j = new JoueurHumain(new Plateau(), true, NumJoueur.JOUEUR1);
        Reine reine = new Reine(j);
        Case instance = new Case(new HexaPoint(0, 0, 0));
        try {
            System.out.println("ajout de la reine :");
            instance.addInsecte(reine);
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "\t Failed ✖\n");
            fail("ne doit pas lever d'exception");
        }
        assertEquals(reine, instance.getInsectes().get(0));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        Fourmi fourmi = new Fourmi(j);
        Scarabee scarabee = new Scarabee(j);
        try {
            System.out.println("ajout d'une fourmis sur une case non vide : doit echouer (Exception) :");
            instance.addInsecte(fourmi); // doit lever une exeption il faut enlever l'insecte avant d'en ajouté un autre -> evite qu'un insecte soit supprimé du jeu
            System.out.println("\u001B[31m" + "\t Failed ✖\n");
            fail("aucune exeption levé");
        } catch (Exception e) {
            assert (e.getMessage().equals("Ajout impossible sur case non vide \n"+instance.getInsecteOnTop()+ " sur " + instance + " \n"));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        }

        try {
            System.out.println("ajout d'un scarabee sur une case non vide :");
            instance.addInsecte(scarabee); // doit lever une exeption il faut enlever l'insecte avant d'en ajouté un autre -> evite qu'un insecte soit supprimé du jeu
            assertEquals(scarabee, instance.getInsectes().get(1));
            assertEquals(reine, instance.getInsectes().get(0));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");

            System.out.println("supression du scarabee :");
            instance.removeInsecte();
            assertEquals(reine, instance.getInsectes().get(0));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        } catch (Exception e) {
            fail("exception annormal");
        }

        try {
            System.out.println("supression de la reine :");
            instance.removeInsecte();
            assertTrue(instance.estVide());
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        } catch (Exception e) {
            fail("exception annormal");
        }

        try {
            System.out.println("ajout d'une fourmis sur case vide:");
            instance.addInsecte(fourmi);
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "\t Failed ✖\n");
            fail("ne doit pas lever d'exception");
        }
        assertEquals(fourmi, instance.getInsectes().get(0));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        try {
            System.out.println("supression de la fourmi :");
            instance.removeInsecte();
            assertTrue(instance.estVide());
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        } catch (Exception e) {
            fail("exception annormal");
        }

        ArrayList<Insecte> li = new ArrayList<>();
        li.add(new Fourmi(j));
        li.add(new Scarabee(j));
        li.add(new Scarabee(j));
        li.add(new Scarabee(j));
        li.add(new Scarabee(j));
        li.add(new Scarabee(j));
        li.add(new Moustique(j));
        li.add(new Moustique(j));

        try {
            System.out.println("ajout de 5 insectes sur la même case (1 fourmi,4 scarabee) :");
            instance.addInsecte(li.get(0));
            instance.addInsecte(li.get(1));
            instance.addInsecte(li.get(2));
            instance.addInsecte(li.get(3));
            instance.addInsecte(li.get(4));

            assertEquals(li.get(0), instance.getInsectes().get(0));
            assertEquals(li.get(1), instance.getInsectes().get(1));
            assertEquals(li.get(2), instance.getInsectes().get(2));
            assertEquals(li.get(3), instance.getInsectes().get(3));
            assertEquals(li.get(4), instance.getInsectes().get(4));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        } catch (Exception e) {
            System.out.println("\u001B[31m" + "\t Failed ✖\n");
            fail("exception anormal");
        }

        try {
            System.out.println("ajout d'un 8e insecte (doit echouer) :");
            instance.addInsecte(li.get(5));
            instance.addInsecte(li.get(6));
            instance.addInsecte(li.get(7));
            System.out.println("\u001B[31m" + "\t Failed ✖\n");
        } catch (Exception e) {
            assert (e.getMessage().equals("Ajout impossible -> 7 insectes maximum"));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        }

        System.out.println("test getInsectOnTop :");
        assertEquals(li.get(6), instance.getInsecteOnTop());
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        try {
            System.out.println("supression des insectes :");

            instance.removeInsecte();
            instance.removeInsecte();
            instance.removeInsecte();

            assertEquals(li.get(0), instance.getInsectes().get(0));
            assertEquals(li.get(1), instance.getInsectes().get(1));
            assertEquals(li.get(2), instance.getInsectes().get(2));
            assertEquals(li.get(3), instance.getInsectes().get(3));

            assertEquals(li.get(3), instance.getInsecteOnTop());

            instance.removeInsecte();

            assertEquals(li.get(0), instance.getInsectes().get(0));
            assertEquals(li.get(1), instance.getInsectes().get(1));
            assertEquals(li.get(2), instance.getInsectes().get(2));

            assertEquals(li.get(2), instance.getInsecteOnTop());

            instance.removeInsecte();

            assertEquals(li.get(0), instance.getInsectes().get(0));
            assertEquals(li.get(1), instance.getInsectes().get(1));

            assertEquals(li.get(1), instance.getInsecteOnTop());

            instance.removeInsecte();

            assertEquals(li.get(0), instance.getInsectes().get(0));

            assertEquals(li.get(0), instance.getInsecteOnTop());

            instance.removeInsecte();

            assertTrue(instance.estVide());

            assertEquals(null, instance.getInsecteOnTop());

            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        } catch (Exception e) {
            fail("exception anormal");
        }

    }

    /**
     * Test of insecteBloque method, of class Case.
     */
    @Test
    public void testInsecteBloque() {
        System.out.println("=============================================");
        System.out.println("TEST insecteBloque : =======================>\n");
        Fourmi fourmi = new Fourmi(new JoueurHumain(new Plateau(), true, NumJoueur.JOUEUR1));
        Scarabee scarabee1 = new Scarabee(new JoueurHumain(new Plateau(), true, NumJoueur.JOUEUR1));
        Scarabee scarabee2 = new Scarabee(new JoueurHumain(new Plateau(), true, NumJoueur.JOUEUR1));
        Case instance = new Case(new HexaPoint(0, 0, 0));
        System.out.println("test sur une case vide (doit lever une exeption) :");
        try {
            instance.insecteBloque(fourmi);
            System.out.println("\u001B[31m" + "\t Failed ✖\n");
            fail("exception non lever");
        } catch (Exception e) {
            assert (e.getMessage().equals("Case vide"));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        }

        System.out.println("test avec la fourmi après l'avoir posé sur la case :");
        try {
            instance.addInsecte(fourmi);
            assertFalse(instance.insecteBloque(fourmi));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        } catch (Exception e) {
            fail("exception anormal");
        }

        System.out.println("test avec le scarabee alors qu'il n'est pas sur la case :");
        try {
            instance.insecteBloque(scarabee1);
            System.out.println("\u001B[31m" + "\t Failed ✖\n");
        } catch (Exception e) {
            assert (e.getMessage().equals("Cet insecte n'est pas sur cette case"));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        }

        System.out.println("test avec le scarabee sur la case (au dessus de la fourmi) :");
        try {
            instance.addInsecte(scarabee1);
            assertFalse(instance.insecteBloque(scarabee1));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");

            System.out.println("test avec la fourmis alors que le scarabee est au dessus :");
            assertTrue(instance.insecteBloque(fourmi));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");

            System.out.println("test avec un 2eme scarabee au dessus du premier et de la fourmis :");
            instance.addInsecte(scarabee2);
            assertFalse(instance.insecteBloque(scarabee2));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");

            System.out.println("test sur le 1er scarabee :");
            assertTrue(instance.insecteBloque(scarabee1));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");

            System.out.println("test sur la fourmis");
            assertTrue(instance.insecteBloque(fourmi));
            System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        } catch (Exception e) {
            fail("exception anormal");
        }

        System.out.println("");
    }

    /**
     * Test of equals method, of class Case.
     */
    @Test
    public void testEquals() {
        System.out.println("=============================================");
        System.out.println("TEST equals : ==============================>\n");
        Case instance = new Case(new HexaPoint(0, 0, 0));
        System.out.println("test avec une case ayant les mêmes coordonees :");
        assertTrue(instance.equals(new Case(new HexaPoint(0, 0, 0))));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        System.out.println("test avec la même case :");
        assertTrue(instance.equals(instance));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        System.out.println("test avec null :");
        assertFalse(instance.equals(null));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        System.out.println("test avec un objet d'une autre classe :");
        assertFalse(instance.equals(new Plateau()));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");
        System.out.println("test avec une case n'ayant pas les mêmes coordonees :");
        assertFalse(instance.equals(new Case(new HexaPoint(0, 5, 4))));
        System.out.println("\u001B[32m" + "\t Passed ✔ \n");

        System.out.println("");
    }
}
