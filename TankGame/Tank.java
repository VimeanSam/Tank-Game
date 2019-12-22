/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.*;

/**
 * @author James Andrews
 * @author Vimean Sam
 */
public class Tank extends GlobalState {
    int xPos, yPos, theta, xPos2, yPos2, theta2;
    BufferedImage p_1, p_2;
    AffineTransform at, at2;
    Rectangle p1, p2;

    public Tank() {
       p_1 = ImageLoader.loadImage("/Assets/p1.png");
       p_2 = ImageLoader.loadImage("/Assets/p2.png");
       p1 = new Rectangle(xPos, yPos, p_1.getWidth(), p_1.getHeight());
       p2 = new Rectangle(xPos, yPos, p_2.getWidth(), p_2.getHeight());
    }

    public void updatePlayer1(int x, int y, int degree) {
        this.xPos = x;
        this.yPos = y;
        this.theta = degree;
        p1 = new Rectangle(x, y, p_1.getWidth(), p_1.getHeight());
    }

    public void updatePlayer2(int x, int y, int degree) {
        this.xPos2 = x;
        this.yPos2 = y;
        this.theta2 = degree;
        p2 = new Rectangle(x, y, p_2.getWidth(), p_2.getHeight());
    }

    public void Rotate() {
        at = AffineTransform.getTranslateInstance(xPos, yPos);
        at2 = AffineTransform.getTranslateInstance(xPos2, yPos2);
        at.rotate(Math.toRadians(theta), p_1.getWidth()/2, p_1.getHeight()/2);
        at2.rotate(Math.toRadians(theta2), p_2.getWidth()/2, p_2.getHeight()/2);
    }

    public void renderTanks(Graphics2D gr){
        Rotate();
        gr.drawImage(p_1, at, null);
        gr.drawImage(p_2, at2, null);
        //System.out.println("Player x: "+r.getX());
        //System.out.println("Player y: "+r.getY());
    }
}
