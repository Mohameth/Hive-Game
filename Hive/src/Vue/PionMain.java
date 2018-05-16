/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.TypeInsecte;
import javafx.scene.effect.ColorAdjust;
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
        this.getImgPion().addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            setSelected();
            notifyListenersMousePressed(this);
        }
        );

        this.getImgPion().addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            //unSelect();
        }
        );
    }

    @Override
    public void snap(PieceHitbox pm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
