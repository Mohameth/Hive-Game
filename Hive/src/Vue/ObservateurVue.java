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

import Modele.HexaPoint;
import javafx.scene.input.MouseEvent;

public interface ObservateurVue {

    public void UpdateAddNewZoneLibre(ZoneLibre zLibre);

    public void UpdateZonLibPosition(HexaPoint oldKeyPoint3D, HexaPoint newPos3D, ZoneLibre zLibre);

    public void UpdateAddNewPionPlateau(PionPlateau2 pionPlateau);

    public void UpdatePionPosition(HexaPoint oldKeyPoint3D, HexaPoint newPos3D, PionPlateau2 p);

    public void updatePionPateauMove(PionPlateau2 p);

    public void updatePionPateauMousePress(PionPlateau2 p);

    public void updatePionPateauMouseReleased(PionPlateau2 p);

    public void updateMousePressedZoneLibre(ZoneLibre zLibre);

    public void updatePionPlateauAddEnDessous(PionPlateau2 pionPlateau);

    public void updatePionPlateauRemoveEnDessous(PionPlateau2 pionPlateau);

    public void updatePionPlateauHoveInDessous(PionPlateau2 pionPlateau, MouseEvent me);

    public void updatePionPlateauHoveOutDessous(PionPlateau2 pp2);

}
