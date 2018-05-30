/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.Insectes.TypeInsecte;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author sylve
 */
public class PionImgView {

    private ImageView imgView;
    private boolean white;
    private boolean visible;
    private boolean selected;
    private boolean lock;
    private TypeInsecte imgType;
    private double curZoom;
    private Cursor cursorType;
    private Color hoverColor;
    private double curWidth, curHeight;
    private double curImgPosX, curImgPosY, prevImgPosX, prevImgPosY;

    public PionImgView(TypeInsecte ti, boolean isWhite, double xImg, double yImg, double currentZoom, double sceneWidth, double sceneHeight) {
        this.white = isWhite;
        this.curZoom = 1;
        this.imgView = getImgFromType(ti);
        this.hoverColor = Color.GRAY;
        this.visible = true;
        this.imgType = ti;
        removeLock();
        setImgPosXY(xImg, yImg);
        setCurrentZoom(currentZoom);
        setMouseEvent();
        setTranslation(sceneWidth, sceneHeight);
    }

    public void affiche() {
        System.out.println("IMAGEVIEW PION: Couleur: " + isWhite() + " PosIMGX: " + getImgPosX() + " PosIMGY: " + getImgPosY() + " PREV x: " + this.prevImgPosX + " PrevY" + this.prevImgPosY + " CurZoom: " + this.curZoom);
    }

    public void settHoverColor(Color c) {
        this.hoverColor = c;
    }

    public void setLock() {
        setLockEffect(true);
    }

    public void setLockEffect(boolean withEffect) {
        this.lock = true;
        if (withEffect) {
            setlockEffect();
        }
        setCursorImageHover(new ImageCursor(new Image("notallowed.png")));
    }

    public boolean isLock() {
        return lock;
    }

    public void setImgVisible() {
        this.visible = true;
        this.getImage().setVisible(true);
        this.getImage().toFront();
    }

    public void setImgInvisible() {
        this.visible = false;
        this.getImage().setVisible(false);
    }

    public void removeLock() {
        this.lock = false;
        removelockEffect();
        setCursorImageHover(Cursor.HAND);
    }

    public boolean isWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public void setTranslation(double sw, double sh) {
        getImage().setTranslateX(sw / 2);
        getImage().setTranslateY(sh / 2);
    }

    public void setCurrentZoom(double totZoom) {
        double imgX, imgY, zoomFactor;
        zoomFactor = totZoom / this.curZoom;
        getImage().setScaleX(getImage().getScaleX() * zoomFactor);  //faire un visiteur
        getImage().setScaleY(getImage().getScaleY() * zoomFactor);

        //mise a jour des coordonnées car l'image est plus grande (grandi donc doit decaller les autres images)
        imgX = getImage().getX() * zoomFactor;
        imgY = getImage().getY() * zoomFactor;

        setImgPosXY(imgX, imgY);

        //update pos previous
        setPrevImgPosXY(imgX, imgY);

        this.curZoom = totZoom;
    }

    public ImageView getImage() {
        return imgView;
    }

    public double getWidthImage() {
        return curWidth;
    }

    public double getImgPosX() {
        return curImgPosX;
    }

    public double getImgPosY() {
        return curImgPosY;
    }

    public double getPrevImgPosX() {
        return prevImgPosX;
    }

    public double getPrevImgPosY() {
        return prevImgPosY;
    }

    public void setPrevImgPosXY(double prevImgPosX, double prevImgPosY) {
        this.prevImgPosX = prevImgPosX;
        this.prevImgPosY = prevImgPosY;
    }

    public void goToPrevPos() {
        setImgPosXY(this.prevImgPosX, this.prevImgPosY);
    }

    public void setImgPosXY(double imgPosX, double imgPosY) {
        this.curImgPosX = imgPosX;
        this.curImgPosY = imgPosY;
        this.getImage().setX(imgPosX);
        this.getImage().setY(imgPosY);
    }

    private ImageView getImgFromType(TypeInsecte pion) {
        String m = getImgPath(pion);
        Image img = new Image("pieces/" + m + ".png");
        ImageView imgv = new ImageView();
        imgv.setCursor(Cursor.HAND);

        imgv.setImage(img);
        this.curWidth = img.getWidth();
        this.curHeight = img.getHeight();
        imgv.setFitWidth(curWidth);
        imgv.setFitHeight(curHeight);
        //centrage du point dans l'image
        imgv.setLayoutX(-(curWidth / 2));
        imgv.setLayoutY(-(curHeight / 2));

        return imgv;
    }

    public void setCursorImageHover(Cursor cursor) {
        this.cursorType = cursor;
        this.getImage().setCursor(cursor);
        //setMouseEvent();
    }

    public String getImgPath(TypeInsecte pion) {
        String m = null;

        String[] namePiece = new String[]{"araignee", "ladybug", "fourmis", "reine", "sauterelles", "scarabee", "cloporte", "moustique"};
        String[] colorPiece = new String[]{"black", "white"};
        String color = colorPiece[0];
        if (isWhite()) {
            color = colorPiece[1];
        }

        if (null == pion) {
            m = "zoneLibre";
        } else {
            switch (pion) {
                case ARAIGNEE:
                    m = "piontr_" + color + "_" + namePiece[0];
                    break;
                case COCCINELLE:
                    m = "piontr_" + color + "_" + namePiece[1];
                    break;
                case FOURMI:
                    m = "piontr_" + color + "_" + namePiece[2];
                    break;
                case REINE:
                    m = "piontr_" + color + "_" + namePiece[3];
                    break;
                case SAUTERELLE:
                    m = "piontr_" + color + "_" + namePiece[4];
                    break;
                case SCARABEE:
                    m = "piontr_" + color + "_" + namePiece[5];
                    break;
                case CLOPORTE:
                    m = "piontr_" + color + "_" + namePiece[6];
                    break;
                case MOUSTIQUE:
                    m = "piontr_" + color + "_" + namePiece[7];
                    break;
                default:
                    break;
            }
        }
        return m;
    }

    public void updateImageDessous(boolean asDessous) {
        String m = getImgPath(this.imgType);
        if (asDessous) {
            Image img = new Image("pieces/" + m + "_dessous.png");
            this.getImage().setImage(img);
        } else {
            Image img = new Image("pieces/" + m + ".png");
            this.getImage().setImage(img);
        }
    }

    private void setHoverEffect() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(0.90);
        dropShadow.setColor(hoverColor);
        this.getImage().setEffect(dropShadow);
    }

    private void playBlinkEffect() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), evt
                -> this.getImage().setOpacity(0.5)
        ),
                new KeyFrame(Duration.seconds(0.2), evt
                        -> this.getImage().setOpacity(1)
                ));
        timeline.setCycleCount(4);
        timeline.play();
    }

    public void blinkImage() {
        playBlinkEffect();
    }

    public void setSelectedEffect() {
        this.selected = true;
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setSpread(0.90);
        dropShadow.setColor(Color.DARKORANGE);
        this.getImage().setEffect(dropShadow);
    }

    private void setlockEffect() {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        colorAdjust.setBrightness(-0.2);
        getImage().setEffect(colorAdjust);
    }

    private void removelockEffect() {
        getImage().setEffect(null);
    }

    public void unSelect() {
        this.selected = false;
        this.getImage().setEffect(null);
    }

    public TypeInsecte getImgType() {
        return this.imgType;
    }

    public void moveDeltaXY(double deltaX, double deltaY) {
        setImgPosXY(curImgPosX + deltaX, curImgPosY + deltaY);
    }

    public void moveToXY(double x, double y) {
        setImgPosXY(x, y);
    }

    private void setMouseEvent() {
        this.getImage().addEventFilter(MouseEvent.MOUSE_ENTERED, (
                final MouseEvent mouseEvent) -> {
            if (!isLock() && !selected) {
                setHoverEffect();
                getImage().setCursor(this.cursorType);
            }
        });

        this.getImage().addEventFilter(MouseEvent.MOUSE_EXITED, (
                final MouseEvent mouseEvent) -> {
            if (!isLock() && !selected) {
                this.getImage().setEffect(null);
            }
        });
    }

}
