/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

/**
 * @author James Andrews
 * @author Vimean Sam
 */
public class BreakableWall {
   int wallHp = 20;

public BreakableWall() {
   this.wallHp = wallHp;
}

public void setWallHP(int health){
   this.wallHp = health;
}

public int getWallHP() {
   return this.wallHp;
}
}
