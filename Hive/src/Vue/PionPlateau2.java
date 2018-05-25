/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.HexaPoint;
import Modele.Insectes.TypeInsecte;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 *
 * @author sylve
 */
public class PionPlateau2 implements ObservableVue {

    private ArrayList<ZoneLibre> zonesLibresListe;
    private PionImgView imagePion;
    private TypeInsecte pionType;
    private HexaPoint coordPion;
    private boolean white;
    private boolean locked;
    private PionPlateau2 pionEnDessous;
    private double totZoom, scWidth, scHeight;
    private boolean dragging;
    private ObservateurVue vtObservateur;

    public PionPlateau2(ObservateurVue vtObservateur, TypeInsecte ti, boolean white, HexaPoint coodPionPlateau, double imgX, double imgY, double totZoom, double scWidth, double scHeight) {
        this.zonesLibresListe = new ArrayList<>();
        this.pionType = ti;
        this.pionEnDessous = null;
        this.imagePion = new PionImgView(ti, white, imgX, imgY, totZoom, scWidth, scHeight);
        this.coordPion = coodPionPlateau;
        this.white = white;
        this.dragging = false;
        this.totZoom = totZoom;
        this.scWidth = scWidth;
        this.scHeight = scHeight;

        addObserver(vtObservateur);
        removeLock();

        this.coordPion = coodPionPlateau;
        this.imagePion.setImgPosXY(imgX, imgY);

        initZoneLibreVoisin();
        updateZoneLibreVoisin();

        setOnClicEvent();
        setMouseHoverEvent();
        notifyNewPionPlateau(this);
    }

    public ArrayList<ZoneLibre> getZonesLibresListe() {
        return zonesLibresListe;
    }

    public void setToFront() {
        this.getImage().toFront();
    }

    public boolean isWhite() {
        return white;
    }

    public void setPionEnDessous(PionPlateau2 pDessous) {
        this.pionEnDessous = pDessous;
        this.imagePion.updateImageDessous(true);
        notifyPionPlateauAddEnDessous(this);
    }

    public ArrayList<PionPlateau2> getDessousList(ArrayList<PionPlateau2> am) {
        am.add(this);
        if (getPionEnDessous() != null) {
            //am.add(getPionEnDessous());
            am = getPionEnDessous().getDessousList(am);
        }
        return am;
    }

    public void removePionEnDessous() {
        this.pionEnDessous = null;
        this.imagePion.updateImageDessous(false);
        notifyPionPlateauRemoveEnDessous(this);
    }

    public PionPlateau2 getPionEnDessous() {
        if (this.pionEnDessous != null) {
            return this.pionEnDessous;
        } else {
            return null;
        }
    }

    public HexaPoint getCoordPion() {
        return coordPion;
    }

    public void setLock() {
        this.locked = true;
        imagePion.setLockEffect(false);
    }

    public boolean isLocked() {
        return locked;
    }

    public void removeLock() {
        this.locked = false;
        imagePion.removeLock();
    }

    public void setPionPosition(HexaPoint newCoord, double imgX, double imgY) {
        //notifier l'observateur avant d'appliquer les changements
        notifyUpdatePionPosition(this.getCoordPion(), newCoord, this);
        this.coordPion = newCoord;
        this.imagePion.setImgPosXY(imgX, imgY);
        updateZoneLibreVoisin();
    }

    public void updateZoneLibreVoisin() {
        int i = 0;
        for (ZoneLibre zoneLibre : zonesLibresListe) {
            double result[] = getCenterOfHitbox(i, this.totZoom);
            HexaPoint newCoord = getHitboxCoord(i);
            //notifier la vueterrain avant de changer les coordonnées uniquement si la position change
//            if (!newCoord.equals(zoneLibre.getCoordZoneLibre())) {
//            }
            //notifyUpdateZonLibPosition(zoneLibre.getCoordZoneLibre(), newCoord, zoneLibre);
            zoneLibre.updatePosition(newCoord, result[0], result[1]);

            i++;
        }
        if (this.pionEnDessous != null) {
            this.pionEnDessous.updateZoneLibreVoisin();
        }
    }

    public ImageView getImage() {
        return this.imagePion.getImage();
    }

    public void affiche() {
        System.out.println("PionPlateau:" + getCoordPion() + "Type:" + this.getPionType() + "Image POS X:" + this.getImage().getX() + " Y: " + this.getImage().getY() + "Scene Width:" + scWidth + "Scene Height:" + scHeight + "Dessous:" + (this.pionEnDessous != null));
        this.imagePion.affiche();
        if (this.pionEnDessous != null) {
            this.pionEnDessous.affiche();
        }
    }

    public void setOnClicEvent() {
        MouseLocation lastMouseLocation = new MouseLocation();

        // --- remember initial coordinates of mouse cursor and node
        getImage().addEventFilter(MouseEvent.MOUSE_PRESSED, (
                final MouseEvent mouseEvent) -> {
            if (!isLocked()) {
                lastMouseLocation.x = mouseEvent.getSceneX();
                lastMouseLocation.y = mouseEvent.getSceneY();

                notifyPionPlateauMousePressed(this);
            }
        }
        );

        getImage().addEventFilter(MouseEvent.MOUSE_RELEASED, (
                final MouseEvent mouseEvent) -> {
            //todo c'est la vue qui met a false le dragging
            //notifier le ctrl que fin du drag and drop uniquement si drag = true
            //affiche();
            notifyPionPlateauMouseReleased(this);
        }
        );
        // --- Shift node calculated from mouse cursor movement
        getImage().addEventFilter(MouseEvent.MOUSE_DRAGGED, (
                final MouseEvent mouseEvent) -> {
            if (!isLocked()) {
                double deltaX = mouseEvent.getSceneX() - lastMouseLocation.x;
                double deltaY = mouseEvent.getSceneY() - lastMouseLocation.y;
                //set drag&drop = true
                //System.out.println("Notifier vue ctrl ce pion drag = true");

                moveToXY(mouseEvent.getSceneX() - (this.scWidth / 2), mouseEvent.getSceneY() - (this.scHeight / 2));
                //moveDeltaXY(deltaX, deltaY);

                lastMouseLocation.x = mouseEvent.getSceneX();
                lastMouseLocation.y = mouseEvent.getSceneY();
                notifyPionPlateauMove(this);
            }
        });
    }

    private void setMouseHoverEvent() {
        this.getImage().addEventFilter(MouseEvent.MOUSE_ENTERED, (
                final MouseEvent mouseEvent) -> {
            if (this.getPionEnDessous() != null) {
                //notify vueTerrain afficher les cases
                notifyPionPlateauHoveInDessous(this, mouseEvent);
            }
        });

        this.getImage().addEventFilter(MouseEvent.MOUSE_EXITED, (
                final MouseEvent mouseEvent) -> {
            if (this.getPionEnDessous() != null) {
                //notify vueTerrain cacher les cases
                notifyPionPlateauHoveOutDessous(this);
            }
        });
    }

    public void setDragging(boolean isDragging) {
        this.dragging = isDragging;
    }

    public boolean isDragging() {
        return dragging;
    }

    public PionImgView getImgViewPion() {
        return this.imagePion;
    }

    /**
     * **
     */
    private void initZoneLibreVoisin() {
        this.zonesLibresListe.clear();
        for (int i = 0; i < 6; i++) {
            double result[] = getCenterOfHitbox(i, this.totZoom);
            ZoneLibre zonLib = new ZoneLibre(this, result[0], result[1], getHitboxCoord(i));
            this.zonesLibresListe.add(zonLib);
            notifyNewZoneLibre(zonLib);
            //todo notififier la vueTerrain ctrl
            //todo afficher sur le plateau
        }
    }

    public TypeInsecte getPionType() {
        return pionType;
    }

    public void updateZoomWidthHeight(double totZoom, double scWidth, double scHeight) {
        this.totZoom = totZoom;
        this.scWidth = scWidth;
        this.scHeight = scHeight;
        this.imagePion.setCurrentZoom(totZoom);
        this.imagePion.setTranslation(scWidth, scHeight);
        for (ZoneLibre zoneLibre : zonesLibresListe) {
            zoneLibre.updateImageZoomWidthHeight(totZoom, scWidth, scHeight);
        }
        if (this.pionEnDessous != null) {
            this.pionEnDessous.updateZoomWidthHeight(totZoom, scWidth, scHeight);
        }

    }

    public double getZoom() {
        return this.totZoom;
    }

    public double getWidth() {
        return this.scWidth;
    }

    public double getHeight() {
        return this.scHeight;
    }

    private HexaPoint getHitboxCoord(int pos) { //en fonction de la position du coin et de la position de la piece sur la grille
        int x = 0;
        int y = 0;
        int z = 0;
        int coordX = this.coordPion.getX();
        int coordY = this.coordPion.getY();
        int coordZ = this.coordPion.getZ();
        switch (pos) {
            case 0:
                x = coordX;
                y = coordY + 1;
                z = coordZ - 1;
                break;
            case 1:
                x = coordX + 1;
                y = coordY;
                z = coordZ - 1;
                break;
            case 2:
                x = coordX + 1;
                y = coordY - 1;
                z = coordZ;
                break;
            case 3:
                x = coordX;
                y = coordY - 1;
                z = coordZ + 1;
                break;
            case 4:
                x = coordX - 1;
                y = coordY;
                z = coordZ + 1;
                break;
            case 5:
                x = coordX - 1;
                y = coordY + 1;
                z = coordZ;
                break;
        }
        return new HexaPoint(x, y, z);
    }

    public double[] getCenterOfHitbox(int pos, double totZoom) {
        double x = this.imagePion.getImgPosX();
        double y = this.imagePion.getImgPosY();
        double scale = this.imagePion.getWidthImage() + 20; //todo 20 nbpixels espace entre
        switch (pos) {
            case 0:
                x = x;
                //y = y - 0.87 * scale * totZoom;
                y = y - 0.85 * scale * totZoom;
                break;

            case 1:
                x = x + 0.75 * scale * totZoom;
                y = y - 0.43 * scale * totZoom;
                break;

            case 2:
                x = x + 0.75 * scale * totZoom;
                y = y + 0.43 * scale * totZoom;
                break;

            case 3:
                x = x;
                // y = y + 0.87 * scale * totZoom;
                y = y + 0.85 * scale * totZoom;
                break;
            case 4:
                x = x - 0.75 * scale * totZoom;
                y = y + 0.43 * scale * totZoom;
                break;
            case 5:
                x = x - 0.75 * scale * totZoom;
                y = y - 0.43 * scale * totZoom;
                break;

        }
        return new double[]{x, y};
    }

    public void notifyMousePressedZoneLibreVoisin(ZoneLibre zLibre) {
        this.vtObservateur.updateMousePressedZoneLibre(zLibre);
    }

    public void moveDeltaXY(double deltaX, double deltaY) {
        this.imagePion.moveDeltaXY(deltaX, deltaY);
        if (this.pionEnDessous != null) {
            this.pionEnDessous.moveDeltaBoard(deltaX, deltaY);
        }
    }

    public void moveToXY(double x, double y) {
        this.imagePion.moveToXY(x, y);
        if (this.pionEnDessous != null) {
            //pour le drag and drop ne pas deplacer le pion en dessous
            //this.pionEnDessous.moveToXY(x, y);
        }
    }

    public void afficheZoneLibre(boolean isvisible) {
        for (ZoneLibre zoneLibre : zonesLibresListe) {
            if (isvisible) {
                zoneLibre.setZoneLibreVisible();
            } else {
                zoneLibre.setZoneLibreCachee();
            }
        }
    }

    public void moveDeltaBoard(double x, double y) { //garde les zones libres affiché si visible
        moveDeltaXY(x, y);
        //this.imagePion.moveDeltaXY(x, y);
        updateZoneLibreVoisin();
        validCurrentPosXY();
    }

    public void setSelectedEffect() {
        this.imagePion.setSelectedEffect();
    }

    public void unSelect() {
        this.imagePion.unSelect();
    }

    public void goToPrevPos() {
        this.imagePion.goToPrevPos();
        if (this.pionEnDessous != null) {
            this.pionEnDessous.goToPrevPos();
        }
    }

    public void validCurrentPosXY() {
        this.imagePion.setPrevImgPosXY(this.imagePion.getImgPosX(), this.imagePion.getImgPosY());
        if (this.pionEnDessous != null) {
            this.pionEnDessous.validCurrentPosXY();
        }
    }

    @Override
    public void addObserver(ObservateurVue newobserver) {
        this.vtObservateur = newobserver;
    }

    @Override
    public void notifyNewZoneLibre(ZoneLibre zLibre) {
        this.vtObservateur.UpdateAddNewZoneLibre(zLibre);
    }

    @Override
    public void notifyUpdateZonLibPosition(HexaPoint oldKeyPoint3D, HexaPoint newPos3D, ZoneLibre zLibre) {
        this.vtObservateur.UpdateZonLibPosition(oldKeyPoint3D, newPos3D, zLibre);
    }

    @Override
    public void notifyNewPionPlateau(PionPlateau2 pionPlateau) {
        this.vtObservateur.UpdateAddNewPionPlateau(pionPlateau);
    }

    @Override
    public void notifyUpdatePionPosition(HexaPoint oldKeyPoint3D, HexaPoint newPos3D, PionPlateau2 p) {
        this.vtObservateur.UpdatePionPosition(oldKeyPoint3D, newPos3D, p);
    }

    @Override
    public void notifyPionPlateauMove(PionPlateau2 pionPlateau) {
        this.vtObservateur.updatePionPateauMove(pionPlateau);
    }

    @Override
    public void notifyPionPlateauMousePressed(PionPlateau2 pionPlateau) {
        this.vtObservateur.updatePionPateauMousePress(this);
    }

    @Override
    public void notifyPionPlateauMouseReleased(PionPlateau2 pionPlateau) {
        this.vtObservateur.updatePionPateauMouseReleased(pionPlateau);
    }

    @Override
    public void notifyPionPlateauAddEnDessous(PionPlateau2 pionPlateau) {
        this.vtObservateur.updatePionPlateauAddEnDessous(pionPlateau);
    }

    @Override
    public void notifyPionPlateauRemoveEnDessous(PionPlateau2 pionPlateau) {
        this.vtObservateur.updatePionPlateauRemoveEnDessous(pionPlateau);
    }

    @Override
    public void notifyPionPlateauHoveInDessous(PionPlateau2 pionPlateau, MouseEvent me) {
        this.vtObservateur.updatePionPlateauHoveInDessous(pionPlateau, me);
    }

    @Override
    public void notifyPionPlateauHoveOutDessous(PionPlateau2 pp2) {
        this.vtObservateur.updatePionPlateauHoveOutDessous(pp2);
    }

    /**
     * **
     */
    private static final class MouseLocation {

        public double x, y;
    }

}
