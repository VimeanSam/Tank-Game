/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
/**
 *
 * @author Vimean Sam
 * @author James Andrews
 */

public class Key implements KeyListener {
    boolean [] keys;
    boolean p1up, p1down, p1left, p1right, p1fire, p2up, p2down, p2left, p2right, p2fire;

    public Key() {
        this.keys = new boolean[256];
    }

    public void tick() {
        this.p1up = keys[KeyEvent.VK_W];
        this.p1down = keys[KeyEvent.VK_S];
        this.p1left = keys[KeyEvent.VK_A];
        this.p1right = keys[KeyEvent.VK_D];
        this.p1fire  = keys[KeyEvent.VK_CONTROL];
        
        this.p2up = keys[KeyEvent.VK_UP];
        this.p2down = keys[KeyEvent.VK_DOWN];
        this.p2left = keys[KeyEvent.VK_LEFT];
        this.p2right = keys[KeyEvent.VK_RIGHT];
        this.p2fire  = keys[KeyEvent.VK_ENTER];
    }

  
  public void keyTyped(KeyEvent ke) {

  }

  public void keyPressed(KeyEvent ke) {
    keys[ke.getKeyCode()] = true;
  }

  public void keyReleased(KeyEvent ke) {
    keys[ke.getKeyCode()] = false;
  }
}
