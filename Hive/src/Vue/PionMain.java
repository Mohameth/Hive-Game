/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.TypeInsecte;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 *
 * @author sylve
 */
public class PionMain extends Piece {

    private int nbPions;
    private Text idTextLab;

    public PionMain(TypeInsecte pion, int nbInsect, boolean white, Text idTextLab) {
        super(pion, white);
        nbPions = nbInsect;
        this.idTextLab = idTextLab;
        setOnClicEvent();
    }

    public int decrNbPion() {
        nbPions--;
        idTextLab.setText("" + nbPions);

        if (nbPions <= 0) {
            idTextLab.setText("X");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOnClicEvent() {
        this.getImgPion().addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                if (getNbPions() > 0) {
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
                if (getNbPions() <= 0) {
                    getImgPion().setCursor(new ImageCursor(new Image("notallowed.png")));
                }
            }
        });

    }

    @Override
    public void snap(PieceHitbox pm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
