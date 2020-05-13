/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;
import java.awt.Rectangle;

/**
 *
 * @author Alice e Vitor
 */
public abstract class MapObject {
    //esta é uma classe raiz/pai para todos os objetos do mapa
    //todo objeto subclasse deve suprir estas caracteristicas
    //desta forma, toda vez que eu colocar objetos como inimigos por exemplo,
    //só precisarei os atributos desta classe
    
    //tiles
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;
    
    //para posicao e vetor
    // dx e dy para direcionamento de escala
    protected double x;
    protected double y;
    protected double dx; 
    protected double dy;
    
    //dimensoes
    //para uso na leitura de Sprite Sheets
    protected int width;
    protected int height;
    
    //collision box
    //basicamente o que fara com que nossos objetos possam ser detectados
    //e envoltos em caixas dentro do ambiente
    protected int cwidth;
    protected int cheight;
    
    //outros atributos para colisao no game
    protected int currRow;
    protected int currCol;
    protected double xdest;
    protected double ydest;
    protected double xtemp;
    protected double ytemp;
    //4-point game method
    //usando assim 4 cantos para determinar colisoes entre blocos
    protected boolean topLeft;
    protected boolean topRight;
    protected boolean bottomLeft;
    protected boolean bottomRight;
    
    //atributos para animacoes
    protected Animation animation;
    protected int currentAction; //pra identificar qual animacao estamos usando
    protected int previousAction;
    //para determinar o lado da Sprite, caso estiver a direita não mudaremos 
    //mas se estiver indo para esquerda é preciso mudar o lado da sprite
    protected boolean facingRight; 
    
    //movimentacoes (booleans para determinar os movimentos de objetos)
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;
    
    //atributos de movimentacoes
    protected double moveSpeed; //quao rapido o objeto acelera
    protected double maxSpeed; //quao rapido o objeto pode ir
    protected double stopSpeed; //desaceleracao
    protected double fallSpeed; //para gravidade
    protected double maxFallSpeed; //para determinar a velocidade de queda
    protected double jumpStart; //quao alto o objeto consegue pular
    protected double stopJumpSpeed; //caso vc mantenha pressionado o botão de pular o objeto continua subindo
    
    //construtor 
    public MapObject(TileMap tm){
        tileMap = tm;
        tileSize = tm.getTileSize();
    }
    
    //checar se cada mapa de objetos pode colidir com outros mapas de objetos
    public boolean intersects(MapObject o){
        //recebemos outro mapa de objetos e utilizamos colisao de 
        //retangulos
        Rectangle r1 = getRectangle();
        //retorna o retangulo do outro objeto
        Rectangle r2 = o.getRectangle();
        
        //checagem de colisao dos dois retangulos
        return r1.intersects(r2);
    }
    
    public Rectangle getRectangle(){
        //para retornar novos retangulos
        return new Rectangle(
                (int) x - cwidth,
                (int) y - cheight,
                cwidth,
                cheight);
    }

     //calcular os cantos faz parte do 4-point game method
    //para calculo de colisoes no game    
    public void calculateCorners(double x, double y){
      int leftTile = (int) (x - cwidth / 2) / tileSize;  
      int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;  
      int topTile = (int) (y - cheight / 2) / tileSize;  
      int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
      
      //quatro cantos mapeados
      int tl = tileMap.getType(topTile, leftTile); // canto superior esquerdo
      int tr = tileMap.getType(topTile, rightTile); // canto superior direito
      int bl = tileMap.getType(bottomTile, leftTile); // canto inferior esquerdo
      int br = tileMap.getType(bottomTile, rightTile); // canto inferior direito
      
      //aqui temos os quatros cantos boleanos
      //desta maneira dá pra usar essa variaveis boleanas para determinar colisoes
      topLeft = tl == Tile.BLOCKED;
      topRight = tr == Tile.BLOCKED;
      bottomLeft = bl == Tile.BLOCKED;
      bottomRight = br == Tile.BLOCKED;
    }
    
    //checar a colisao de tiles
    public void checkTileMapCollision(){
        //primeiro checar em qual linha e coluna estamos
        currCol = (int) x / tileSize;
        currRow = (int) y / tileSize;
        
        //estas sao as proximas posicoes de destino
        xdest = x + dx;
        ydest = y + dy;
        
        //precisaremos de valores temporarios para rastrear e mudar as posicoes
        xtemp = x;
        ytemp = y;
        
        //cacular as direcoes y (subir ou descer)
        calculateCorners(x, ydest);
        if(dy < 0){ //significa que estamos indo para cima
            if(topLeft || topRight){
                dy = 0;
                ytemp = currRow * tileSize + cheight / 2;
            }else{
                ytemp += dy; //do contrário continuaremos subindo
            }
        }
        if(dy > 0){ //significa que estamos indo pra baixo
            if(bottomLeft || bottomRight){
                dy = 0;
                falling = false;
                ytemp = (currRow + 1) * tileSize - cheight / 2;
            }else{
                ytemp += dy; //se não colidir enquanto caimos, continuamos a descer
            }
        }
        
        //calcular as direcoes x (esquerda e direita)
        calculateCorners(xdest, y);
        if(dx < 0 ){ //indo para esquerda
            if(topLeft || bottomLeft){ //se for verdadeiro
                dx = 0; //´significa que acertamos um bloco e devemos parar
                xtemp = currCol * tileSize + cwidth /2;
            }else{ //do contrário estamos livres pra continuar andando
                xtemp += dx;
            }
        }
        if(dx > 0){ //indo pra direita
            if(topRight || bottomRight){ //se for verdadeiro
                dx = 0; //atingimos um bloco a direita e paramos
                xtemp = (currCol + 1) * tileSize - cwidth / 2;
            }
        }else{
            xtemp += dx;
        }
        
        //checar se estamos não estamos caindo
        if(!falling){
            calculateCorners(x, ydest + 1); //pra checar o chão
            if(!bottomLeft && !bottomRight){
                falling = true; // se não houver cantos do chão, estamos caindo
            }
            
        }
    }
    
    //getters e setters
    public int getX(){
       return (int) x;
    }
    
    public int getY(){
       return (int) y;
    }
    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCwidth() {
        return cwidth;
    }

    public int getCheight() {
        return cheight;
    }
    
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    //todo objeto no mapa possui duas posicoes uma global e outra local
    public void setMapPosition(){
        xmap = tileMap.getX(); //é o posicionamento final das 2 posicoes
        ymap = tileMap.getY();
    }

    public void setLeft(boolean b) {
        this.left = b;
    }

    public void setRight(boolean b) {
        this.right = b;
    }

    public void setUp(boolean b) {
        this.up = b;
    }

    public void setDown(boolean b) {
        this.down = b;
    }

    public void setJumping(boolean b) {
        this.jumping = b;
    }
    
    //function pra retornar se o objeto não aparece na tela
    //para não gastar processamento a toa desenhando o que não aparece
    public boolean notOnScreen(){
        //se o objeto estiver além do lado esquerdo da tela
        //ou além do lado direito da tela
        //ou além do topo da tela
        //ou além do chão da tela
        //retornará verdadeiro 
        return x + xmap + width < 0 ||
                x + xmap - width > GamePanel.WIDTH ||
                y + ymap + height < 0 ||
                y + ymap - height > GamePanel.HEIGHT;
    }
}
