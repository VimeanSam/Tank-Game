/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Vimean Sam
 * @author James Andrews
 */
public class Map extends GlobalState {

    private final int width = 800;
    private final int height = 690;
    Canvas canvas;
    BufferedImage background, breakablewall, brokenwall, unbreakablewall, medkit;
    BufferedReader readmap;
    int worldwidth, worldheight;
    int tiles[][];
    Rectangle rect;
    Tank t;
    boolean unbreakablewalls, breakablewalls, healup = false;
    ArrayList<Rectangle>Unbreakablewalls = new ArrayList<Rectangle>();
    ArrayList<Rectangle>Breakablewalls = new ArrayList<Rectangle>();
    ArrayList<Rectangle>medkits = new ArrayList<Rectangle>();

    public Map() {
      this.background = ImageLoader.loadImage("/Assets/background.png");
      this.breakablewall = ImageLoader.loadImage("/Assets/wall1.png");
      this.brokenwall = ImageLoader.loadImage("/Assets/brokenwall.png");
      this.unbreakablewall = ImageLoader.loadImage("/Assets/wall2.png");
      this.medkit = ImageLoader.loadImage("/Assets/medkit.png");
      this.t = new Tank();
    }

    public void createWindow() {
      setTitle("Tank Game");
      setSize(width, height);
      setResizable(false);
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      this.canvas = new Canvas();

      this.canvas.setMinimumSize(new Dimension(width, height));
      this.canvas.setPreferredSize(new Dimension(width, height));
      this.canvas.setMaximumSize(new Dimension(width, height));
      this.canvas.setFocusable(false);
      add(this.canvas);

      pack();
    }
    
    public void loadMap(String file) {

      try {
        this.readmap = new BufferedReader(new FileReader(file));
        this.worldwidth = Integer.parseInt(readmap.readLine());
        this.worldheight = Integer.parseInt(readmap.readLine());
        this.tiles = new int[worldheight][worldwidth];

        for(int i = 0; i < worldheight; i++) {
          String line = readmap.readLine();
          String[] tokens = line.split(" ");

          for(int j = 0; j < worldwidth; j++) {
            tiles[i][j] = Integer.parseInt(tokens[j]);
            }
          }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    public void renderMap(Graphics2D g) {
      for(int i = 0; i < worldheight; i++){
        for(int j = 0; j < worldwidth; j++){
          int floors = tiles[i][j];

          if(floors == 0) {
            g.drawImage(background, j*30, i*30, null);
          }
          if(floors == 1) {
            g.drawImage(breakablewall, j*30, i*30, null);
            if(!breakablewalls){
                Breakablewalls.add(new Rectangle(j*30, i*30, 30, 30));
            }
          }
          if(floors == 2) {
            g.drawImage(unbreakablewall, j*30, i*30, null);
          }
          if(floors == 3) {
            g.drawImage(medkit, j*30, i*30, null);
            if(!healup){
                medkits.add(new Rectangle(j*30, i*30, 30, 30));
            }
          }
          if(floors == 4) {
            g.drawImage(unbreakablewall, j*30, i*30, null);
            if(!unbreakablewalls){
                Unbreakablewalls.add(new Rectangle(j*30, i*30, 30, 30));  
            }
          }
          if(floors == 5) {
            g.drawImage(brokenwall, j*30, i*30, null);
          }
        }
      }
      unbreakablewalls = true;
      breakablewalls = true;
      healup = true;
    }

    public Canvas returnCanvas(){
      return canvas;
    }
}
