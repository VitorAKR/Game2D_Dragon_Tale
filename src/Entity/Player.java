package Entity;

import TileMap.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
/**
 *
 * @author Alice e Vitor
 */
public class Player extends MapObject{
    //coisas de jogador
    private int health;
    private int maxHealth;
    private int fire; // pq ele é um dragon
    private int maxFire;
    private boolean dead; //se estiver morto
    private boolean flinching; //se vacilar ou tomar hits
    private long flinchTimer; //timer pra animação
    
    //para o ataque de fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
//  private ArrayList<FireBall> fireBalls;  
    
    //para o ataque de scratch
    private boolean scratching;
    private int scratchDamage; //definir o dano 
    private int scratchRange; //definir o range do ataque
    
    //planar (gliding)
    private boolean gliding;
    
    //animações do personagem é feita pro um array de sprites
    private ArrayList<BufferedImage[]> sprites;
    //vetor para mapear a qtde dos frames por linhas em playersprites.gif
    private final int[] numFrames={
        2, 8, 1, 2, 4, 2, 5
    };
    
    //definir as movimentçãoes do personagem
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;
    
    
    //contrutor
    public Player(TileMap tm) {
        super(tm);
        //setar algumas caracterisiticas providas de MapObject
        
        //para o tamanho das sprites
        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;
        
        //velocidade das ações
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15; 
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;
        //prite pra direita
        facingRight = true;
        
        //setar caracterisiticas do player
        health = maxHealth = 5;
        //ataque de fogo
        fire = maxFire = 2500;
        fireCost = 200;
        fireBallDamage = 5;
//      fireBalls = new ArrayList<FireBall>();

        //ataque de scratch
        scratchDamage = 8;
        scratchRange = 40;
        
        //carregar as sprites do personagem
        try {
            BufferedImage spritesheet = ImageIO.read(
             getClass().getResourceAsStream("/Sprites/Player/playersprites.gif")
            );
            
            //criar array
            sprites = new ArrayList<BufferedImage[]>();
            //precisaremos extrair cada animação da spritesheet 
            //para isso usaremos um for com tamanho de 7, pois temos
            //como total no arquivo 7 animações
            for(int i = 0; i < 7; i++){
                //feito isto criamos um novo vetor de imagens 
                //onde usaremos o numero de frames de cada animação
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                //com isso usaremos mais um laço para percorrer os frames
                for(int j = 0; j < numFrames[i]; j++){
                    //e extrair uma subimagem pra dentro do frame
                    if(i != 6){ 
                        //se for diferente de 6 significa que podem ser lidos
                        //respeitando o tamanho do construtor 30x30
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }else{ //do contrario
                    //significa que estamos lendo frames de scratching
                    // e frames de scratch attack são mais largos em width
                        bi[j] = spritesheet.getSubimage(
                                j * width * 2,
                                i * height,
                                width,
                                height
                        );                                      
                    }
                }
                sprites.add(bi); //add a imagem do frame na lista
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        animation = new Animation();
        //setar personagem em espera
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(400);
    }
    
    
    //getters e setters

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getFire() {
        return fire;
    }

    public int getMaxFire() {
        return maxFire;
    }

    public void setFiring() {
        this.firing = true;
    }

    public void setScratching() {
        this.scratching = true;
    }

    public void setGliding(boolean b) {
        this.gliding = b;
    }
    
    //esta function irá determinar qual sera a proxima posicao do player
    private void getNextPosition(){
        //ler através das teclas pressionadas
        //movimentos
        if(left){ //ir pra esquerda
            dx-= moveSpeed;
            if(dx < -maxSpeed){
                dx = -maxSpeed;
            }
        }else if(right){ //ir pra direita
            dx+= moveSpeed;
            if(dx > maxSpeed){
                dx = maxSpeed;
            }
        }else{ //do contrário temos de ficar parados
            if(dx > 0){
                dx -=stopSpeed;
                if(dx < 0){
                    dx = 0;
                }
            }else if(dx < 0){
                dx += stopSpeed;
                if(dx > 0){
                    dx = 0;
                }
            }
        }
        
        //implementação pra não atacar enquanto se move
        //com exceção de estar no ar
        if((currentAction == SCRATCHING || currentAction == FIREBALL) 
                && !(jumping || falling)){
            dx = 0; //não poderemos nos mover!
        }
        
        //pulando
        if(jumping || !falling){
            dy = jumpStart;
            falling = true;
        }
        
        //caindo
        if(falling){
            //checar se está planando
            if(dy > 0 && gliding) dy += fallSpeed * 0.1;
            else dy += fallSpeed;
            
            if(dy > 0) jumping = false;
            if(dy < 0 && !jumping) dy +=stopJumpSpeed; 
            
            if(dy > maxFallSpeed ) dy = maxFallSpeed;
            
        }
    }
    
    //metodo para realizar a atualização das coisas em Player
    public void update(){
        //atualizar a posição
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        //setar as animações
        if(scratching){ //se for scratch attack
            if(currentAction != SCRATCHING){
                currentAction = SCRATCHING;
                //setar o numero do frame na lista pra animar
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 60; //60 pixels
            }
        }else if(firing){ //se for fireBall attack
            if(currentAction != FIREBALL){
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 30;
            }
        }else if (dy > 0){ //se estiver caindo
            //temos duas formas de cair, ou planando ou caindo 
            if(gliding){
                if(currentAction != GLIDING){
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 30;
                }
            }else if(currentAction != FALLING){
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 30;
            }
        }else if (dy < 0){ //se estivermos subindo
            if(currentAction != JUMPING){
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1); //já que há somente uma sprite para pulo
                width = 30;
            }
        }else if (left || right){ //se o player pra esquerda ou direita
            //nós usaremos a animação para andar
            if(currentAction != WALKING){
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }else { //se ele estiver parado
            if(currentAction != IDLE){
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }
        //ao final sempre atualizar a animação
        animation.update();
        
        //muito importante! você não quer o personagem andando enquanto ataca
        //por isso é importante travar ele na direção enquanto executa o ataque
        if(currentAction != SCRATCHING && currentAction != FIREBALL){
            if(right) facingRight = true;
            if(left)  facingRight = false;
        }
    }
    
    public void draw(Graphics2D g){
        //setar o MapObPositon é sempre umas das primeiras coisas a se fazer
        setMapPosition();
        
        //desenhar jogador
        //checar se ele esta vacilando
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 10000000;
            if(elapsed / 100 % 2 == 0){
                return;
            }
        }
        
        if(facingRight){
            g.drawImage(animation.getImage(),
                    (int) (x + xmap - width / 2),
                    (int) (y + ymap - height / 2),
                    null
            );
        }else{ //se estivermos encarado a esquerda
            //vamos inverter o lado da imagem
            g.drawImage(animation.getImage(),
                    (int) (x + xmap - width / 2 + width),
                    (int) (y + ymap - height / 2),
                    -width,
                    height,
                    null 
            );
        }
        
    }
    
}
