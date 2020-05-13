
package TileMap;

import Main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 *
 * @author Alice e Vitor
 */
public class Background {
    private BufferedImage image;
    
    private double x;
    private double y;
    private double dx;
    private double dy;
    private double moveScale;
    
    public Background(String s, double ms){

        try{
          //passra a imagem como resource  
          image = ImageIO.read(
                  getClass().getResourceAsStream(s)
          );
          moveScale = ms;
        }catch(Exception e){
           e.printStackTrace(); //sempre print a pilha
        }
    }
    
    public void setPosition(double x, double y){
        this.x = (x * moveScale) % GamePanel.WIDTH;
        this.y = (y * moveScale) % GamePanel.HEIGHT;
    }
    
    //isto é para que o backgroud mova automaticamente
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    public void update(){
        x+= dx;
        y+= dy;
    }
    
    public void draw(Graphics2D g){
        g.drawImage(image, (int)x, (int)y, null);
        //necessário tratar os espaços para que haja sempre preenchimento
        //se x for menor que 0 precisaremos desenhar uma imagem extra a direita
        if(x < 0){
            g.drawImage(
                    image,
                    (int)x + GamePanel.WIDTH,
                    (int)y,
                    null
            );
        }
        //se x for maior que 0 precisaremos desenhar uma imagem extra a esquerda
        if(x > 0){
            g.drawImage(
                    image,
                    (int)x - GamePanel.WIDTH,
                    (int)y,
                    null
            );
        }
    
    }
}
