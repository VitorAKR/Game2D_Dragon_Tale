
package TileMap;

import Main.GamePanel;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
 *
 * @author Alice e Vitor
 */
public class TileMap {
    //atributos de posicao
    private double x;
    private double y;
    
    //bounds do game
    private  int xmin;
    private  int ymin;
    private  int xmax;
    private  int ymax;
    
    //usado para intermediar frames
    private double tween;
    
    //atributos do mapa (um mapa bidemencional)
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;
    
    //tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;
    
    //para desenhar
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;
    
    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.07;
    }
    
    //carrega o tileset na memoria
    public void loadTiles(String s){
        try {
            
            tileset = ImageIO.read(
                    getClass().getResourceAsStream(s)
            );
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            
            BufferedImage subimage;
            for(int col = 0; col < numTilesAcross; col++){
                subimage = tileset.getSubimage(
                        col * tileSize,
                        0,
                        tileSize,
                        tileSize
                );
                tiles[0][col] = new Tile(subimage, Tile.NORMAL);
                subimage = tileset.getSubimage(
                        col * tileSize,
                        tileSize,
                        tileSize,
                        tileSize
                );
                tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //carrega o mapa na memoria
    public void loadMap(String s){
      //primeira linha eh o numero de colunas
      //segunda linha o numero de linhas
      //e o resto eh apenas o mapa
      
        try {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader( new InputStreamReader(in));
            
            numCols = Integer.parseInt(br.readLine()); //colunas
            numRows = Integer.parseInt(br.readLine()); //linhas
            map = new int [numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;
            
            //setar xmin e xmax
            xmin =  GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;
            
            //criar delimitadores
            String delims = "\\s+";
            for(int row = 0; row < numRows; row++){
                //cria a linha
                String line = br.readLine();
                //splita ela em tokens usando delimitadores
                String[] tokens = line.split(delims);
                //depois vamos para as colunas
                for(int col=0; col < numCols; col++){
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            } 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
     
    }

        public int getTileSize(){
            return tileSize;
        }
        
        public int getX(){
            return (int)x;
        }
        
        public int getY(){
            return (int)y;
        }
        
        public int getWidth(){
            return width;
        }
        
        public int getHeight(){
            return width;
        }
        
        public int getType(int row, int col){
            int rc = map[row][col]; //coluna linha pra saber qual Tile eh do mapa
            int r = rc / numTilesAcross;
            int c = rc % numTilesAcross;
            
            return tiles[r][c].getType();
        }
        
        public void setPosition(double x, double y){
          //por ter que usar o tween para continuar seguindo o personagem
          //durante o jogo, preciso fazer esta adapatação abaixo para 
          //mover a "camera" junto do personagem qnd setar a posicao
            this.x += (x - this.x) * tween;
            this.y = (y - this.y) * tween;
            //arrumar as bounds do game
            fixBounds();
            
            //definir onde começar a desenhar linha e coluna
            colOffset = (int) -this.x / tileSize;
            rowOffset = (int) -this.y / tileSize;
        }
        
        public void fixBounds(){
            if(x < xmin) x = xmin;
            if(y < xmin) y = ymin;
            if(x > xmax) x = xmax;
            if(y > ymax) y = ymax;
        }
        
        public void draw(Graphics2D g){
            for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
                if(row >= numRows) break; //não precisa continuar desenhando
                
                for(int col = colOffset; col < colOffset + numColsToDraw; col++){
                    if(col >= numCols) break;
                    
                    if(map[row][col] == 0) continue; // para dar skip
                    
                    int rc = map[row][col];
                    int r = rc / numTilesAcross;
                    int c = rc % numTilesAcross;
                    
                    g.drawImage(
                            tiles[r][c].getImage(),
                            (int)x + col * tileSize,
                            (int)y + row * tileSize,
                            null
                    );
                }
            }
        }
        
    
}
