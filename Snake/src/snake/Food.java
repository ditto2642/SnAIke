/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snake;

/**
 *
 * @author pmaclean
 * food for thought
 */
public class Food extends GameItem {
    Food(){
        this.color = 'r';
    }
    
    @Override
    public boolean update(int length){
        return false;
    }

    @Override
    public int getAge() {
        return -1;
    }
    
    @Override
    public String toString(){
        return "Food";
    }    
}
