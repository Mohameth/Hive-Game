/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Insectes.Araignee;
import Modele.Insectes.Cloporte;
import Modele.Insectes.Coccinelle;
import Modele.Insectes.Fourmi;
import Modele.Insectes.Insecte;
import Modele.Insectes.Reine;
import Modele.Insectes.Sauterelle;
import Modele.Insectes.Scarabee;
import java.util.ArrayList;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author sylve
 */
public class PionMain implements ObservableVue {

    private ObservateurVue obs;
    private ArrayList<Insecte> pionsIdentique;
    private ImageView imgPion;
    private boolean white;

    public PionMain(Insecte pion, boolean white) {
        pionsIdentique = new ArrayList<>();
        addPion(pion);
        this.white = white;
        this.imgPion = getImgFromClass(pion);
        makeCliquable();

    }

    public boolean isWhite() {
        return white;
    }


    public Insecte getPion() {
        if (!pionsIdentique.isEmpty()) {
            return pionsIdentique.get(0);
        } else {
            return null;
        }
    }

    public void addPion(Insecte pion) {
        // != null
        pionsIdentique.add(pion);
    }

    public ImageView getImgPion() {
        return imgPion;
    }

    private ImageView getImgFromClass(Insecte pion) {
        String m = getImgPath(pion);

        Image img = new Image("pieces/" + m);
        ImageView imgv = new ImageView();
        imgv.setImage(img);

        return imgv;
    }

    public String getImgPath(Insecte pion) {
        String m = null;

        String[] namePiece = new String[]{"araignee", "ladybug", "fourmis", "reine", "sauterelles", "scarabee", "cloporte", "moustique"};
        String[] colorPiece = new String[]{"black", "white"};
        String color = colorPiece[0];
        if (isWhite()) {
            color = colorPiece[1];
        }

        if (pion instanceof Araignee) {
            m = "piontr_" + color + "_" + namePiece[0] + ".png";
        } else if (pion instanceof Coccinelle) {
            m = "piontr_" + color + "_" + namePiece[1] + ".png";
        } else if (pion instanceof Fourmi) {
            m = "piontr_" + color + "_" + namePiece[2] + ".png";
        } else if (pion instanceof Reine) {
            m = "piontr_" + color + "_" + namePiece[3] + ".png";
        } else if (pion instanceof Sauterelle) {
            m = "piontr_" + color + "_" + namePiece[4] + ".png";
        } else if (pion instanceof Scarabee) {
            m = "piontr_" + color + "_" + namePiece[5] + ".png";
        } else if (pion instanceof Cloporte) {
            m = "piontr_" + color + "_" + namePiece[6] + ".png";
        } else {
            m = "piontr_" + color + "_" + namePiece[7] + ".png";
        }

        return m;
    }

    private void makeCliquable() {
        // --- remember initial coordinates of mouse cursor and node
        this.getImgPion().addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            setSelected();
            notifyListenersMousePressed(null, this);
            //NOTIFIER L'HUD POUR PASSER DEVANT
            //lorsqu'on selectionne un emplacement libre sur le plateau créer un nouveau pions et l'ajouter sur la liste des pions plateau
            //remove de la liste actuelle
        }
        );

        this.getImgPion().addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            unSelect();
        }
        );
    }

    public void setSelected() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(0.90);
        dropShadow.setColor(Color.DARKORANGE);
        this.getImgPion().setEffect(dropShadow);
    }

    public void unSelect() {
        this.getImgPion().setEffect(null);
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
    public void notifyListenersMousePressed(Piece p, PionMain pm) {
        obs.updateMousePressPiece(null, pm);
    }

}
