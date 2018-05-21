/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author wampachs
 */
package Vue;

import Modele.Point3DH;
import javafx.geometry.Point3D;

public interface ObservableVue {

    public void addObserver(ObservateurVue newobserver);

    public void notifyNewZoneLibre(ZoneLibre zLibre);

    public void notifyUpdateZonLibPosition(Point3DH oldKeyPoint3D, Point3DH newPos3D, ZoneLibre zLibre);

    public void notifyNewPionPlateau(PionPlateau2 pionPlateau);

    public void notifyUpdatePionPosition(Point3DH oldKeyPoint3D, Point3DH newPos3D, PionPlateau2 p);

    public void notifyPionPlateauMove(PionPlateau2 pionPlateau);

    public void notifyPionPlateauMousePressed(PionPlateau2 pionPlateau);

    public void notifyPionPlateauMouseReleased(PionPlateau2 pionPlateau);

    public void notifyMousePressedZoneLibreVoisin(ZoneLibre zoneLibre);

    public void notifyPionPlateauAddEnDessous(PionPlateau2 pionPlateau);

    public void notifyPionPlateauRemoveEnDessous(PionPlateau2 pionPlateau);

}
