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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author sylve
 */
public class PionMain extends Piece {

    private int nbPions;
    private Text idTextLab;
    private boolean locked;
    private BorderPane bp;

    public PionMain(TypeInsecte pion, int nbInsect, boolean white, Text idTextLab, BorderPane bPane) {
        super(pion, white);
        bp = bPane;
        nbPions = nbInsect;
        this.idTextLab = idTextLab;
        setOnClicEvent();
        locked = true;
    }

    public void cleanBorPane() {
        //this.bp.getChildren().clear();
        this.bp.setVisible(false);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setlock() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        colorAdjust.setBrightness(-0.2);
        getImgPion().setEffect(colorAdjust);
        this.locked = true;
    }

    public void removelock() {
        getImgPion().setEffect(null);
        this.locked = false;
    }

    public int decrNbPion() {
        nbPions--;
        idTextLab.setText("" + nbPions);

        if (nbPions <= 0) {
            idTextLab.setText("X");
            cleanBorPane();

        }
        return nbPions;
    }

    public int getNbPions() {
        return nbPions;
    }

    @Override
    public void addObserver(ObservateurVue newobserver) {
        this.obs = newobserver;
    }

    @Override
    public void notifyListenersMove(double deltaX, double deltaY, boolean isBoardMove) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyListenersMousePressed(Piece p) {
        obs.updateMousePressPiece(p);
    }

    @Override
    public void affiche() {
        System.out.println("NbPions: " + this.nbPions + " Type: " + this.getPionsType() + " Locked: " + this.locked + " IsWhite: " + isWhite());
    }

    @Override
    public void setOnClicEvent() {
        this.getImgPion().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                if (getNbPions() > 0 && !isLocked()) {
                    notifyListenersMousePressed(PionMain.this);
                    setSelected();
                } else {
                    getImgPion().setCursor(new ImageCursor(new Image("notallowed.png")));

                }
            }
        });

        this.getImgPion().addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                if (getNbPions() <= 0 || isLocked()) {
                    getImgPion().setCursor(new ImageCursor(new Image("notallowed.png")));
                } else {
                    getImgPion().setCursor(Cursor.HAND);
                }
            }
        });

    }

    @Override
    public void snap(PieceHitbox pm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyListenersMouseReleased() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
