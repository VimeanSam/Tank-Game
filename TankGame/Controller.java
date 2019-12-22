package TankGame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

public class Controller{
  LinkedList<Bullet> b = new LinkedList<Bullet>();
  LinkedList<Rectangle> p1bullet = new LinkedList<Rectangle>();
  LinkedList<Rectangle> p2bullet = new LinkedList<Rectangle>();
  boolean shootwall, shoot, wallbreak, brokenwall, p1shot, p2shot, tank1shot, tank2shot = false;
  boolean p1property, p2property = false;
  Rectangle bulletrec, temp;
  Bullet TempBullet;
  GlobalState state;
  Graphics2D g2d;
  Map m;

  public Controller(GlobalState state) {
    this.state = state;
    m = new Map();
  }

  public void tick() {
    for(int i = 0; i < b.size(); i++) {
      TempBullet = b.get(i);
      bulletrec = new Rectangle(TempBullet.returnX(),TempBullet.returnY(),27,8);
      p1bullet.clear();
      p2bullet.clear();

      if(TempBullet.getOwners().equals("player1")){
        p1bullet.add(bulletrec);
      }
      if(TempBullet.getOwners().equals("player2")){
        p2bullet.add(bulletrec);
      }
      P1BulletCollision();
      P2BulletCollision();
      if(shoot) {
        shoot = !shoot;
        removeBullet(TempBullet);
      }
      if(brokenwall) {
        removeBullet(TempBullet);
        brokenwall = !brokenwall;
      }
      if(tank2shot) {
        removeBullet(TempBullet);
        tank2shot = !tank2shot;
      }
      if(tank1shot) {
        removeBullet(TempBullet);
        tank1shot = !tank1shot;
      }
      if(TempBullet.returnX() < 30 || TempBullet.returnY() < 30) {
        removeBullet(TempBullet);
      }
      if(TempBullet.returnX() > 1117 || TempBullet.returnY() > 650) {
        removeBullet(TempBullet);
      }
      TempBullet.tick();
    }
  }

  public void P1BulletCollision() {
      for(int i = 0; i < state.map.Unbreakablewalls.size(); i++){
      for(int j = 0; j < p1bullet.size(); j++){
          if(p1bullet.get(j).intersects(state.map.Unbreakablewalls.get(i))){
              shoot = true;
          }
        }
      }
      for(int i = 0; i < state.map.Breakablewalls.size(); i++){
        for(int j = 0; j < p1bullet.size(); j++){
          if(p1bullet.get(j).intersects(state.map.Breakablewalls.get(i))){
            brokenwall = true;
            Rectangle temp = (Rectangle)state.map.Breakablewalls.get(i);
            int temp_x = (int)temp.getX()/30;
            int temp_y = (int)temp.getY()/30;
            state.breakWall(temp_x, temp_y);
            state.map.Breakablewalls.remove(i);
          }
        }
      }

      for(int i = 0; i < p1bullet.size(); i++){
        if(p1bullet.get(i).intersects(state.tank.p2)){
          tank2shot = true;

          if(p1shot){
            p1shot = !p1shot;
            state.players.setp2Health(state.players.getp2Health()-4);
            state.health_widthp2-=2;

            if(state.players.getp2Health() <= 0 && state.players.getp2lives()!=0){
              state.players.setp2Health(0);
              state.players.setp2Health(100);
              state.health_widthp2 = 50;
              state.players.setp2Lives(state.players.getp2lives()-1);
              state.P1restart();
              state.P2restart();
              state.reloadMap();
            }
          }
        }
      }
      //System.out.println(state.players.getp1Health());
  }

  public void P2BulletCollision() {
      for(int i = 0; i < state.map.Unbreakablewalls.size(); i++){
        for(int j = 0; j < p2bullet.size(); j++){
            if(p2bullet.get(j).intersects(state.map.Unbreakablewalls.get(i))){
                shoot = true;
            }
        }
      }
      for(int i = 0; i < state.map.Breakablewalls.size(); i++) {
        for(int j = 0; j < p2bullet.size(); j++){

          if(p2bullet.get(j).intersects(state.map.Breakablewalls.get(i))) {
            brokenwall = true;
            Rectangle temp = state.map.Breakablewalls.get(i);
            int temp_x = (int)temp.getX()/30;
            int temp_y = (int)temp.getY()/30;
            state.breakWall(temp_x, temp_y);
            state.map.Breakablewalls.remove(i);
          }
        }
      }

      for(int i = 0; i < p2bullet.size(); i++) {
        if(p2bullet.get(i).intersects(state.tank.p1)) {
          tank1shot = true;

          if(p2shot) {
            p2shot = !p2shot;
            state.players.setp1Health(state.players.getp1Health()-4);
            state.health_widthp1-=2;

            if(state.players.getp1Health() <= 0 && state.players.getp1lives()!=0) {
              state.players.setp1Health(0);
              state.players.setp1Health(100);
              state.health_widthp1 = 50;
              state.players.setp1Lives(state.players.getp1lives()-1);
              state.P1restart();
              state.P2restart();
              state.reloadMap();
            }
          }
        }
      }
      //System.out.println(state.players.getp1Health());
  }

  public void render(Graphics2D g) {
    for(int i = 0; i < b.size(); i++) {
      TempBullet = b.get(i);
      TempBullet.render(g);
    }
  }

  public void addBullet(Bullet block) {
    b.add(block);
  }

  public void removeBullet(Bullet block) {
    b.remove(block);
  }

  public void addBulletrec(Rectangle block) {
    p1bullet.add(block);
  }

  public void removeBulletrec(Rectangle block) {
    p1bullet.remove(block);
  }
}