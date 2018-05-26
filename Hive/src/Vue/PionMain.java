/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Insectes.TypeInsecte;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

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
    private boolean dragging;
    private PionPlateau2 pionPlateauDrag;

    public PionMain(VueTerrain vt, TypeInsecte typeIns, int nbInsect, boolean white, Text idTextLab, BorderPane bPane) {
        this.white = white;
        this.vtObserver = vt;
        this.pionsType = typeIns;
        this.imgPion = new PionImgView(typeIns, white, 0, 0, 1, 0, 0);
        this.imgPion.getImage().setFitWidth(this.imgPion.getImage().getFitWidth() / 4.5);
        this.imgPion.getImage().setFitHeight(this.imgPion.getImage().getFitHeight() / 4.5);
        this.bp = bPane;
        this.dragging = false;
        this.nbPions = nbInsect;
        this.idTextLab = idTextLab;
        this.pionPlateauDrag = null;
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
        idTextLab.setText(nbPions + "⨯");

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

    public void setDragging(boolean isDragging, PionPlateau2 pp2) {
        this.dragging = isDragging;
        this.pionPlateauDrag = pp2;
    }

    public PionPlateau2 getPionPlateauDrag() {
        return this.pionPlateauDrag;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void setOnClicEvent() {

        getImage().addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            if (!isLocked()) {
                this.vtObserver.updateMousePressedMainJoueur(this);
            }
        }
        );

        getImage().addEventFilter(MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent) -> {
            if (!isLocked()) {
                if (pionPlateauDrag != null) {
                    pionPlateauDrag.moveToXY(mouseEvent.getSceneX() - (pionPlateauDrag.getWidth() / 2), mouseEvent.getSceneY() - (pionPlateauDrag.getHeight() / 2));
                }
                this.vtObserver.updateMouseDraggedMainJoueur(this);
            }
        });

        getImage().addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            this.vtObserver.updateMouseReleaseMainJoueur(this);
        }
        );

    }

}
