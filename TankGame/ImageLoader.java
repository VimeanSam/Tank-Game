/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TankGame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author James Andrews
 * @author Vimean Sam
 */
public class ImageLoader {
    public static BufferedImage loadImage(String path) {

      try {
        return ImageIO.read(ImageLoader.class.getResource(path));

      } catch(IOException e) {
        e.printStackTrace();
        System.exit(1);
      }

      return null;
  }
}
