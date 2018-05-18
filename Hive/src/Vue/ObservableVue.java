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

public interface ObservableVue {

    public void addObserver(ObservateurVue newobserver);

    public void notifyListenersMove(double deltaX, double deltaY, boolean isBoardMove);

    public void notifyListenersMousePressed(Piece p);

    public void notifyListenersMouseReleased();
}
