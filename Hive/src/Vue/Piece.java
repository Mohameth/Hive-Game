package Vue;

import Modele.TypeInsecte;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Sylvestre
 */
public abstract class Piece implements ObservableVue {

    protected ObservateurVue obs;
    protected TypeInsecte pionsType;
    protected ImageView imgPion;
    protected boolean white;

    public Piece(TypeInsecte pionType, boolean isWhite) {
        this.pionsType = pionType;
        this.white = isWhite;
        this.imgPion = getImgFromType(pionType);
    }

    private ImageView getImgFromType(TypeInsecte pion) {
        String m = getImgPath(pion);
        Image img = new Image("pieces/" + m);
        ImageView imgv = new ImageView();
        imgv.setCursor(Cursor.HAND);

        imgv.setImage(img);
        imgv.setFitWidth(img.getWidth());
        imgv.setFitHeight(img.getHeight());

        imgv.setLayoutX(-(img.getWidth() / 2));
        imgv.setLayoutY(-(img.getHeight() / 2));
        imgv.setX(0);
        imgv.setY(0);

        return imgv;
    }

    public String getImgPath(TypeInsecte pion) {
        String m = null;

        String[] namePiece = new String[]{"araignee", "ladybug", "fourmis", "reine", "sauterelles", "scarabee", "cloporte", "moustique"};
        String[] colorPiece = new String[]{"black", "white"};
        String color = colorPiece[0];
        if (isWhite()) {
            color = colorPiece[1];
        }

        if (pion == TypeInsecte.ARAIGNEE) {
            m = "piontr_" + color + "_" + namePiece[0] + ".png";
        } else if (pion == TypeInsecte.COCCINELLE) {
            m = "piontr_" + color + "_" + namePiece[1] + ".png";
        } else if (pion == TypeInsecte.FOURMI) {
            m = "piontr_" + color + "_" + namePiece[2] + ".png";
        } else if (pion == TypeInsecte.REINE) {
            m = "piontr_" + color + "_" + namePiece[3] + ".png";
        } else if (pion == TypeInsecte.SAUTERELLE) {
            m = "piontr_" + color + "_" + namePiece[4] + ".png";
        } else if (pion == TypeInsecte.SCARABEE) {
            m = "piontr_" + color + "_" + namePiece[5] + ".png";
        } else if (pion == TypeInsecte.CLOPORTE) {
            m = "piontr_" + color + "_" + namePiece[6] + ".png";
        } else if (pion == TypeInsecte.MOUSTIQUE) {
            m = "piontr_" + color + "_" + namePiece[7] + ".png";
        }
        return m;
    }

    public boolean isWhite() {
        return this.white;
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

    public TypeInsecte getPionsType() {
        return pionsType;
    }

    public ImageView getImgPion() {
        return imgPion;
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

    public abstract void affiche();

    public abstract void setOnClicEvent();

    public abstract void snap(PieceHitbox pm);

    public abstract int decrNbPion();
}
