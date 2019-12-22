/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

/**
 *
 * @author Vimean Sam
 * @author James Andrews
 */
public class Players extends GlobalState{
    private int p1_health;
    private int p2_health;
    private int p1_lives;
    private int p2_lives;
    
    public Players(){}
    
    public void setp1Health(int amount){
        this.p1_health = amount;
    }
    public void setp2Health(int amount){
        this.p2_health = amount;
    }
    public void setp1Lives(int amount){
        this.p1_lives = amount;
    }
    public void setp2Lives(int amount){
        this.p2_lives = amount;
    }
    public int getp1Health(){
        return this.p1_health;
    }
    public int getp2Health(){
        return this.p2_health;
    }
    public int getp1lives(){
        return this.p1_lives;
    }
    public int getp2lives(){
        return this.p2_lives;
    }
    
    public void update(){}
}
