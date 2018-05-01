/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

/**
 *
 * @author pmaclean
 */
public abstract class GameItem {
    char color;
    public abstract boolean update(int length);
    public abstract int getAge();
}
