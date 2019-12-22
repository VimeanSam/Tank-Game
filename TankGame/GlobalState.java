/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

/**
 *
 * @author Vimean Sam
 * @author James Andrews
 */

public class GlobalState extends JFrame implements Runnable {
  Thread thread;
  BufferStrategy buffer;
  Map map;
  Tank tank;
  private int x= 100;
  private int y = 100;
  private int x2= 1012;
  private int y2 = 474;
  Key keylistener;
  Graphics2D gr2d;
  int angle = 0;
  int angle2 = 180;
  private boolean running = false;
  private boolean pickupMeds = true;
  private boolean breakwall = false;
  private boolean runIntoWall = false;
  private boolean endgame = false;
  int health_widthp1 = 50;
  int health_widthp2 = 50;
  int wallhp = 20;
  private int p1health,p2health,p1lives,p2lives;
  String level;
  int prevX, prevY, prevX2, prevY2;
  int _x, _y, _x2, _y2;
  BreakableWall breakable;
  private Controller c;
  private boolean p1Death,p2Death,tie = false;
  private boolean p1shoot, p2shoot = false;
  Players players;
  Bullet bullet;
  private int speed = 4;
  private int theta_offset = 3;
  MusicPlayer sounds = new MusicPlayer();
  MusicPlayer p1gun = new MusicPlayer();
  MusicPlayer p2gun = new MusicPlayer();

  public void P1restart() {
      x = 100;
      y = 100;
      angle = 0;      
  }

  public void P2restart() {
      x2= 1012;
      y2 = 474;
      angle2 = 180;
  }

  public void reloadMap() {
    runIntoWall = false;
    pickupMeds = true;
    breakwall = false;
    this.map.loadMap(level);
    this.map.Breakablewalls.clear();
    this.map.medkits.clear();
    this.map.breakablewalls = false;
    this.map.healup = false;
  }
  
  public void init() {
    this.map = new Map();
    this.map.createWindow();
    this.level = "Assets/World.txt";
    this.keylistener = new Key();
    this.map.addKeyListener(keylistener);
    this.tank = new Tank();
    this.breakable = new BreakableWall();
    this.map.loadMap(level);
    this.players = new Players();
    this.players.setp1Health(100);
    this.players.setp2Health(100);
    this.players.setp1Lives(3);
    this.players.setp2Lives(3);
    this.c = new Controller(this);
  }

  public void run() {
    init();
    int fps = 60;

    double timePerTick = 1000000000 / fps;
    double delta = 0;
    long currentTime;
    long pastTime = System.nanoTime();
    long timer = 0;
    int ticks = 0;

    while(running) {
      currentTime = System.nanoTime();
      delta += (currentTime - pastTime) / timePerTick;
      timer += (currentTime - pastTime);
      pastTime = currentTime;

    if(delta >= 1) {
    	tick();
    	render();
    	ticks++;
    	delta--;
    }
    if(timer >= 1000000000) {
      ticks = 0;
      timer = 0;
    }
    if(p1Death){
       endgame = true;
    }
    if(p2Death){
       endgame = true;
    }
    }
      stop();
  }

  public void GameLogic() {
    if(p1lives == 0 || p2lives == 0){
      if(p1lives == 0){
          players.setp1Health(0);
          health_widthp1 = -5;
          p1Death = true;
      }

      if(p2lives == 0) {
          players.setp2Health(0);
          health_widthp2 = -5;
          p2Death = true;
      }

      if(p1Death && p2Death){
          tie = true;
      }
    }
  }

  public void tick(){
    p1health = players.getp1Health();
    p2health = players.getp2Health();
    p1lives = players.getp1lives();
    p2lives = players.getp2lives();
    this.keylistener.tick();
    this.c.tick();
    GameLogic();
   
    if(this.keylistener.p1up) {
      Player1_checkWallCollision();
      Player1_PowerUpCollision();
      P1_collideswithP2();
      this.x += (int) Math.round(speed*Math.cos(Math.toRadians(angle)));
      this.y += (int) Math.round(speed*Math.sin(Math.toRadians(angle)));
      CheckOutOfBounds();
    }
    if(this.keylistener.p1down) {
      Player1_checkWallCollision();
      Player1_PowerUpCollision();
      P1_collideswithP2();
      this.x -= (int) Math.round(speed*Math.cos(Math.toRadians(angle)));
      this.y -= (int) Math.round(speed*Math.sin(Math.toRadians(angle)));
      CheckOutOfBounds();
    }
    if(this.keylistener.p1left) {
      this.angle -= theta_offset;
    }

    if(this.keylistener.p1right) {
      this.angle += theta_offset;
    }

    if(this.keylistener.p1fire) {
      p1gun.outPutAudio("Assets/Fire.wav");
      int bullet_x = x +(int) ((tank.p1.getWidth()/2) + 25 * Math.cos(Math.toRadians(angle)));
      int bullet_y = y + (int) ((tank.p1.getHeight()/2) + 19 * Math.sin(Math.toRadians(angle)));
      Bullet p1bullet = new Bullet(bullet_x, bullet_y, angle);
      
      if(!p1shoot) {
        p1shoot = !p1shoot;
        c.p1shot = true;
        p1bullet.setOwner("player1");
        this.c.addBullet(p1bullet);

      }
    }
    if(p1shoot && !this.keylistener.p1fire) {
      p1shoot = !p1shoot;
    }
    
    if(this.keylistener.p2up) {
      Player2_checkWallCollision();
      Player2_PowerUpCollision();
      P2_collideswithP1();
      x2+= (int) Math.round(speed*Math.cos(Math.toRadians(angle2)));
      y2+= (int) Math.round(speed*Math.sin(Math.toRadians(angle2)));
      CheckOutOfBounds();
    }
    if(this.keylistener.p2down) {
      Player2_checkWallCollision();
      Player2_PowerUpCollision();
      P2_collideswithP1();
      x2-= (int) Math.round(speed*Math.cos(Math.toRadians(angle2)));
      y2-= (int) Math.round(speed*Math.sin(Math.toRadians(angle2)));
      CheckOutOfBounds();
    }
    if(this.keylistener.p2left) {
      angle2 -= theta_offset;
    }
    if(this.keylistener.p2right) {
      angle2 += theta_offset;
    }
    if(this.keylistener.p2fire) {
      p2gun.outPutAudio("Assets/Fire.wav");
      int bullet_x = x2 +(int) ((tank.p1.getWidth()/2) + 25 * Math.cos(Math.toRadians(angle2)));
      int bullet_y = y2 + (int) ((tank.p1.getHeight()/2) + 19 * Math.sin(Math.toRadians(angle2)));
      Bullet p2bullet = new Bullet(bullet_x, bullet_y, angle2);
      
      if(!p2shoot) {
        p2shoot = !p2shoot;
        c.p2shot = true;
        p2bullet.setOwner("player2");
        this.c.addBullet(p2bullet);
      }
    }
    if(p2shoot && !this.keylistener.p2fire){
       p2shoot = !p2shoot;
    }
  }
  
  public void CheckOutOfBounds(){
      //Player1's coordinates
      if(x <= 25){
          x = 25;
      }
      if(x >= 1117){
          x = 1117;
      }
      if(y <= 30){
          y = 30;
      }
      if(y >= 610){
          y = 610;
      }
      //Player2's coordinates
      if(x2 <= 25){
          x2 = 25;
      }
      if(x2 >= 1117){
          x2 = 1117;
      }
      if(y2 <= 30){
          y2 = 30;
      }
      if(y2 >= 610){
          y2 = 610;
      }
  }
  
  public void breakWall(int x, int y){
      map.tiles[y][x] = 0;
  }
  
  public void Player1_checkWallCollision(){
      if(!Player1_CollisionWithUnbreakableWall() && !Player1_CollisionWithbreakableWall()){
         prevX = x;
         prevY = y; 
         //System.out.println("prevX: "+x);
         //System.out.println("prevY: "+y);
      }else{
          setP1Blocking(prevX, prevY);
      }
  }
  
  public void P1_collideswithP2(){  
      if(!TankCollisionp1()){
          _x = x;
          _y = y;
          _x2 = x2;
          _y2 = y2;  
      }else{
          setP1Blocking(_x,_y);
          
          if(p1lives != 0 && p2lives !=0){
              
              if(p1health >= 10){
                players.setp1Health(p1health-=10);
                health_widthp1-=5;
              }else if(p1health > 0 && p1health < 10){
                  players.setp1Health(p1health-p1health);
              }
              
              if(p2health >= 10){
                players.setp2Health(p2health-=10);
                health_widthp2-=5;
              }else if(p2health > 0 && p2health < 10){
                  players.setp2Health(p2health-p2health);
              }
              
              if(p1health <= 0){
                  health_widthp1 = -5;
                  P1restart();
                  P2restart();
                  players.setp1Lives(p1lives-1);
                  players.setp1Health(100);
                  health_widthp1 = 50;
                  reloadMap();
              }
              if(p2health <= 0){
                  health_widthp2 = -5;
                  P1restart();
                  P2restart();
                  players.setp2Lives(p2lives-1);
                  players.setp2Health(100);
                  health_widthp2 = 50;
                  reloadMap();
              }
          }
      }
  }
    
  public boolean Player1_CollisionWithUnbreakableWall(){
      //System.out.println(map.Unbreakablewalls.size());
      for(int i = 0; i < map.Unbreakablewalls.size(); i++){
        if(tank.p1.intersects(map.Unbreakablewalls.get(i))){
          return true;
        }
      }
      return false;
  }
  
  public boolean Player1_CollisionWithbreakableWall(){  
      for(int i = 0; i < map.Breakablewalls.size(); i++){
        if(tank.p1.intersects(map.Breakablewalls.get(i))){
            Rectangle temp = (Rectangle)map.Breakablewalls.get(i);
            int temp_x = (int)temp.getX()/30;
            int temp_y = (int)temp.getY()/30;
            map.tiles[temp_y][temp_x] = 5;
            breakable.wallHp--;
            //System.out.println(breakable.wallHp);
            if(breakable.wallHp == 0){
                map.Breakablewalls.remove(i);
                map.tiles[temp_y][temp_x] = 0;
                //System.out.println("rectangle x: "+temp_x);
                //System.out.println("rectangle y: "+temp_x);
                //System.out.println("tiles: "+map.tiles[temp_x][temp_x]);
                breakable.wallHp = 20;
            }
            return true;
          }
        }
        return false;
    }
  
    public boolean TankCollisionp1(){
        return tank.p1.intersects(tank.p2);
    }
  
    public void Player1_PowerUpCollision(){
      if(Player1_pickupHealth()){
          if(p1health >= 81){
              int increaseHP = 100-p1health;
              players.setp1Health(p1health+increaseHP);
              health_widthp1 = 50;
          }else{
              players.setp1Health(p1health+20);
              health_widthp1+=10;
          }
      }
    }
    
    public boolean Player1_pickupHealth(){
       if(p1health < 100){
        for(int i = 0; i < map.medkits.size(); i++){
            if(tank.p1.intersects(map.medkits.get(i))){
                Rectangle temp = map.medkits.get(i);
                int temp_x = (int)temp.getX()/30;
                int temp_y = (int)temp.getY()/30;
                map.tiles[temp_y][temp_x] = 0;
                map.medkits.remove(i);
                return true;
            }
        }
       }
        return false;
    }
    
    public void P2_collideswithP1(){
        if(!TankCollisionp2()){
          _x = x;
          _y = y;
          _x2 = x2;
          _y2 = y2;
      }else{
          setP2Blocking(_x2, _y2);
          if(p1lives != 0 && p2lives !=0){
              
              if(p1health >= 10){
                players.setp1Health(p1health-=10);
                health_widthp1-=5;
              }else if(p1health > 0 && p1health < 10){
                  players.setp1Health(p1health-p1health);
              }
              
              if(p2health >= 10){
                players.setp2Health(p2health-=10);
                health_widthp2-=5;
              }else if(p2health > 0 && p2health < 10){
                  players.setp2Health(p2health-p2health);
              }
              
              if(p1health <= 0){
                  health_widthp1 = -5;
                  P1restart();
                  P2restart();
                  players.setp1Lives(p1lives-1);
                  players.setp1Health(100);
                  health_widthp1 = 50;
                  reloadMap();
              }
              if(p2health <= 0){
                  health_widthp2 = -5;
                  P1restart();
                  P2restart();
                  players.setp2Lives(p2lives-1);
                  players.setp2Health(100);
                  health_widthp2 = 50;
                  reloadMap();
              }
          }
       }
    }
  
    
    public void Player2_checkWallCollision(){
      if(!Player2_CollisionWithUnbreakableWall() && !Player2_CollisionWithbreakableWall()){
         prevX2 = x2;
         prevY2 = y2;
      }else{
          setP2Blocking(prevX2, prevY2);
      }
  }
 
  public boolean Player2_CollisionWithUnbreakableWall(){
    for(int i = 0; i < map.Unbreakablewalls.size(); i++){
      if(tank.p2.intersects(map.Unbreakablewalls.get(i))){
          return true;
      }
    }
      return false;
  }

  public boolean Player2_CollisionWithbreakableWall() {
    for(int i = 0; i < map.Breakablewalls.size(); i++) {
        if(tank.p2.intersects(map.Breakablewalls.get(i))){
          Rectangle temp = map.Breakablewalls.get(i);
          int temp_x = (int)temp.getX()/30;
          int temp_y = (int)temp.getY()/30;
          map.tiles[temp_y][temp_x] = 5;
          breakable.wallHp--;
          
          if(breakable.wallHp == 0){
              map.Breakablewalls.remove(i);
              map.tiles[temp_y][temp_x] = 0;
              breakable.wallHp = 20;
          }
          return true;
        }
      }
      return false;
    }

     public void Player2_PowerUpCollision() {
      if(Player2_pickupHealth()) {
        if(p2health >= 81) {
          int increaseHP = 100-p2health;
          players.setp2Health(p2health+increaseHP);
          health_widthp2 = 50;
        }else{
          players.setp2Health(p2health+20);
          health_widthp2+=10;
        }
      }
    }

    public boolean Player2_pickupHealth() {
      if(p2health < 100) {
        for(int i = 0; i < map.medkits.size(); i++) {
          if(tank.p2.intersects(map.medkits.get(i))){
            Rectangle temp = map.medkits.get(i);
            int temp_x = (int)temp.getX()/30;
            int temp_y = (int)temp.getY()/30;
            map.tiles[temp_y][temp_x] = 0;
            map.medkits.remove(i);
            return true;
          }
        }
      }
        return false;
    }
  
    public boolean TankCollisionp2(){
      return tank.p2.intersects(tank.p1);
    }
    
    public void setP1Blocking(int block_x, int block_y){
      this.x = block_x;
      this.y = block_y;
    }
    
    public void setP2Blocking(int block_x2, int block_y2){
      this.x2 = block_x2;
      this.y2 = block_y2;
    }
    
  public void render() {
   Color p1status, p1statusbar;
   if(p1health <= 40){
       p1status = Color.RED;
       p1statusbar = Color.ORANGE;
       if(p1health <= 0){
           p1statusbar = Color.RED;
       }
   }else if(p1health <= 60 && p1health > 40){
       p1status = Color.ORANGE;
       p1statusbar = Color.ORANGE;
   }else{
       p1status = Color.GREEN;
       p1statusbar = Color.GREEN;
   }
   
   Color p2status, p2statusbar;
   if(p2health <= 40){
       p2status = Color.RED;
       p2statusbar = Color.ORANGE;
       if(p2health <= 0){
           p2statusbar = Color.RED;
       }
   }else if(p2health <= 60 && p2health > 40){
       p2status = Color.ORANGE;
       p2statusbar = Color.ORANGE;
   }else{
       p2status = Color.GREEN;
       p2statusbar = Color.GREEN;
   }
   
   String display_p1health = Integer.toString(p1health);
   String display_p2health = Integer.toString(p2health);
   String display_p1lives = Integer.toString(p1lives);
   String display_p2lives = Integer.toString(p2lives);
   
   this.buffer = map.returnCanvas().getBufferStrategy();
   if(this.buffer == null) {
     this.map.returnCanvas().createBufferStrategy(3);
     return;
   }
     gr2d = (Graphics2D) buffer.getDrawGraphics();
     BufferedImage world = new BufferedImage(1200, 690, BufferedImage.TYPE_INT_RGB);
     Graphics2D g2d = world.createGraphics();
     gr2d.clearRect(0, 0, this.map.getWidth(), this.map.getHeight());
     map.renderMap(g2d);
     tank.updatePlayer1(x, y, angle);
     tank.updatePlayer2(x2, y2, angle2);
     tank.renderTanks(g2d);
     c.render(g2d);
     g2d.setColor(Color.RED);
     g2d.fillRect(x, y-20, 50, 5);
     g2d.drawRect(x, y-20, 50, 5);
     g2d.setColor(p1statusbar);
     g2d.fillRect(x, y-20, health_widthp1, 5);
     g2d.drawRect(x, y-20, health_widthp1, 5);
     //Health Bar for player2
     g2d.setColor(Color.RED);
     g2d.fillRect(x2, y2-20, 50, 5);
     g2d.drawRect(x2, y2-20, 50, 5);
     g2d.setColor(p2statusbar);
     g2d.fillRect(x2, y2-20, health_widthp2, 5);
     g2d.drawRect(x2, y2-20, health_widthp2, 5);
     g2d.setColor(p1status);
     g2d.drawString(display_p1health, x+15, y-25);
     g2d.setColor(p2status);
     g2d.drawString(display_p2health, x2+15, y2-25);
     Image mini = world.getScaledInstance(250, 180, 2);
     
     BufferedImage lhs = world.getSubimage(XCam(x), 0, 400, 690);
     BufferedImage rhs = world.getSubimage(XCam(x2), 0, 400, 690);
     gr2d.drawImage(lhs, 0, 0, this);
     gr2d.drawImage(rhs, 400, 0, this);
     gr2d.setColor(Color.BLACK);
     gr2d.drawRect(0, 0, 400, 690);
     gr2d.drawRect(400, 0, 400, 690);
     gr2d.drawImage(mini, 275, 510, this);
     
     if(endgame){
         if(p1Death && p2Death){
             gr2d.setColor(Color.BLACK);
             gr2d.setFont(new Font(gr2d.getFont().getFontName(), Font.PLAIN, 72));
             gr2d.drawString("DRAW", 280, 330);
             speed = 0;
             theta_offset = 0;
             p1shoot = true;
             p2shoot = true;
         }
                  
         if(!p1Death && p2Death){
             gr2d.setColor(Color.RED);
             gr2d.setFont(new Font(gr2d.getFont().getFontName(), Font.PLAIN, 72));
             gr2d.drawString("PLAYER 1 WINS!", 105, 330);
             speed = 0;
             theta_offset = 0;
             p1shoot = true;
             p2shoot = true;
         }
         
         if(p1Death && !p2Death){
             gr2d.setColor(Color.BLUE);
             gr2d.setFont(new Font(gr2d.getFont().getFontName(), Font.PLAIN, 72));
             gr2d.drawString("PLAYER 2 WINS!", 105, 330);
             speed = 0;
             theta_offset = 0;
             p1shoot = true;
             p2shoot = true;
         }
     }
     
     gr2d.setColor(Color.BLACK);
     gr2d.setFont(new Font(gr2d.getFont().getFontName(), Font.PLAIN, 30));
     gr2d.drawString("Lives: ", 45, 650);
     gr2d.drawString(display_p1lives, 130, 650);
     gr2d.drawString("Lives: ", 670, 650);
     gr2d.drawString(display_p2lives, 755, 650);
     buffer.show();     
     gr2d.dispose();
  }
  
  public int XCam(int x) {
    int screen_x = x - 300; 
    
    if(screen_x < 0){
      screen_x = 0;
    }
    
    if(screen_x > 800){
      screen_x = 800;
    }
    return screen_x;
  }
 
  public synchronized void start() {
    if(running) {
      return;
    }
    this.running = true;
    this.thread = new Thread(this);
    this.thread.start();
  }

  public synchronized void stop() {

    if(!running) {
      return;
    }
    running = false;
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public void music(){
      sounds.MusicStart("Assets/Music.mid");
  }
  
  public static void main(String args[]) {
    new GlobalState().music();
    new GlobalState().start();
  }
}