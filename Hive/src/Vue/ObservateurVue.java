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

public interface ObservateurVue {

    public void UpdateAddNewZoneLibre(ZoneLibre zLibre);

    public void UpdateZonLibPosition(Point3DH oldKeyPoint3D, Point3DH newPos3D, ZoneLibre zLibre);

    public void UpdateAddNewPionPlateau(PionPlateau2 pionPlateau);

    public void UpdatePionPosition(Point3DH oldKeyPoint3D, Point3DH newPos3D, PionPlateau2 p);

    public void updatePionPateauMove(PionPlateau2 p);

    public void updatePionPateauMousePress(PionPlateau2 p);

    public void updatePionPateauMouseReleased(PionPlateau2 p);

    public void updateMousePressedZoneLibre(ZoneLibre zLibre);

}
