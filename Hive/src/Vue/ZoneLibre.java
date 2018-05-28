/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.HexaPoint;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author sylve
 */
public class ZoneLibre {

    private PionImgView imageZoneLibre;
    private boolean visible;
    private PionPlateau2 pionParent;
    private HexaPoint coordZoneLibre;

    //premiere zone libre
    public ZoneLibre(double totZoom, double width, double height) {
        this.imageZoneLibre = new PionImgView(null, false, 0, 0, totZoom, width, height);
        this.imageZoneLibre.settHoverColor(Color.rgb(0, 255, 0, 0.5));
        setZoneLibreCachee();
        this.pionParent = null;
        this.coordZoneLibre = new HexaPoint(0, 0, 0);
        setMouseEvent();
        //notifier la vueTerrain
    }

    //zones libre coté d'un pion
    public ZoneLibre(PionPlateau2 pionParent, double imgZoneLiX, double imgZoneLiY, HexaPoint coordZoneLibre) {
        this.pionParent = pionParent;
        this.coordZoneLibre = coordZoneLibre;
        this.imageZoneLibre = new PionImgView(null, pionParent.isWhite(), imgZoneLiX, imgZoneLiY, pionParent.getZoom(), pionParent.getWidth(), pionParent.getHeight());
        this.imageZoneLibre.settHoverColor(Color.rgb(0, 255, 0, 0.5));
        setZoneLibreCachee();
        setMouseEvent();
        //notifier la vueTerrain depuis le pion parent
    }

    public boolean asParentNull() {
        return pionParent == null;
    }

    public ImageView getImage() {
        return imageZoneLibre.getImage();
    }

    public double getImgPosX() {
        return imageZoneLibre.getImgPosX();
    }

    public double getImgPosY() {
        return imageZoneLibre.getImgPosY();
    }

    public HexaPoint getCoordZoneLibre() {
        return this.coordZoneLibre;
    }

    public void setZoneLibreVisible() {
        this.visible = true;
        this.imageZoneLibre.setImgVisible();
    }

    public void setZoneLibreCachee() {
        this.visible = false;
        this.imageZoneLibre.setImgInvisible();
    }

    public void updatePosition(HexaPoint newCoord, double imgZoneLibreX, double imgZoneLibreY) {
        this.coordZoneLibre = newCoord;
        this.imageZoneLibre.moveToXY(imgZoneLibreX, imgZoneLibreY);
        //this.imageZoneLibre = new PionImgView(null, false, imgZoneLibreX, imgZoneLibreY, pionParent.getZoom(), pionParent.getWidth(), pionParent.getHeight());
    }

    public void updateImageZoomWidthHeight(double totZoom, double scWidth, double scHeight) {
        this.imageZoneLibre.setTranslation(scWidth, scHeight);
        this.imageZoneLibre.setCurrentZoom(totZoom);
    }

    public void affiche() {
        System.out.println("Zone Libre XYZ: " + getCoordZoneLibre().getX() + ", " + getCoordZoneLibre().getY() + ", " + getCoordZoneLibre().getZ() + " \t IMGX: " + getImgPosX() + " IMGY: " + getImgPosY());
    }

    public void setMouseEvent() {

        this.getImage().addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            //valider le coup au mouse release:
            //System.out.println("Mouse pressed Zone Libre");
            this.pionParent.notifyMousePressedZoneLibreVoisin(this);
            //affiche();
        });
    }

}
