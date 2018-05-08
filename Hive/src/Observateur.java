/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wampachs
 */
public interface Observateur {

    public void updateMove(Piece p, double deltaX, double deltaY, boolean isBoardMove);

}
