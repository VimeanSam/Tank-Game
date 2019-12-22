package TankGame;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class Bullet extends Tank {
  private int x;
  private int y;
  int Angle;
  String ownerName;
  private int speedX;
  private boolean visible;
  AffineTransform transform;
  BufferedImage round;

  public Bullet(int x, int y, int angle) {
    this.x = x;
    this.y = y;
    this.Angle = angle;
    round = ImageLoader.loadImage("/Assets/bullet.png");
  }

  public void tick() {
    x += (int) Math.round(10*Math.cos(Math.toRadians(Angle)));
    y += (int) Math.round(10*Math.sin(Math.toRadians(Angle)));
  }

  public void setOwner(String name){
    this.ownerName = name;
  }

  public String getOwners(){
    return this.ownerName;
  }

  public void render(Graphics2D g) {
      transform = AffineTransform.getTranslateInstance(x, y);
      transform.rotate(Math.toRadians(Angle), round.getWidth()/2, round.getHeight()/2);
      g.drawImage(round, transform, this);
  }
  public int returnX() {
    return x;
  }

  public int returnY() {
    return y;
  }

  public int getBulletWidth(){
      return round.getWidth();
  }

  public int getBulletHeight(){
      return round.getHeight();
  }
}
