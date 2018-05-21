/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.TypeInsecte;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author sylve
 */
public class PionMain {

    private TypeInsecte pionsType;
    private PionImgView imgPion;
    private boolean locked;
    private int nbPions;
    private boolean white;
    private Text idTextLab;
    private BorderPane bp;
    private VueTerrain vtObserver;

    public PionMain(VueTerrain vt, TypeInsecte typeIns, int nbInsect, boolean white, Text idTextLab, BorderPane bPane) {
        this.white = white;
        this.vtObserver = vt;
        this.pionsType = typeIns;
        this.imgPion = new PionImgView(typeIns, white, 0, 0, 1, 0, 0);
        this.imgPion.getImage().setFitWidth(this.imgPion.getImage().getFitWidth() / 4.5);
        this.imgPion.getImage().setFitHeight(this.imgPion.getImage().getFitHeight() / 4.5);
        this.bp = bPane;
        this.nbPions = nbInsect;
        this.idTextLab = idTextLab;
        setOnClicEvent();
    }

    public void cleanBorPane() {
        //this.bp.getChildren().clear();
        this.bp.setVisible(false);
    }

    public void displayBorPane() {
        //this.bp.getChildren().clear();
        this.bp.setVisible(true);
    }

    public void decrNbPion() {
        nbPions--;
        idTextLab.setText("" + nbPions);

        if (nbPions <= 0) {
            idTextLab.setText("X");
            cleanBorPane();
        }
    }

    public void setNbPion(int nbPion) {
        displayBorPane();
        this.nbPions = nbPion + 1;
        decrNbPion();
    }

    public int getNbPions() {
        return nbPions;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setlock() {
        this.imgPion.setLock();
        this.locked = true;
    }

    public void removelock() {
        this.imgPion.removeLock();
        this.locked = false;
    }

    public boolean isWhite() {
        return this.white;
    }

    public void setSelectedEffect() {
        this.imgPion.setSelectedEffect();
    }

    public void unSelect() {
        this.imgPion.unSelect();
    }

    public TypeInsecte getPionsType() {
        return pionsType;
    }

    public void affiche() {
        System.out.println("NbPions: " + this.nbPions + " Type: " + getPionsType() + " IsWhite: " + isWhite() + " Locked: " + isLocked());
    }

    public ImageView getImage() {
        return this.imgPion.getImage();
    }

    public void setOnClicEvent() {

        getImage().addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            if (!isLocked()) {
                this.vtObserver.updateMouseReleasedMainJoueur(this);
            }
        }
        );
    }

}
