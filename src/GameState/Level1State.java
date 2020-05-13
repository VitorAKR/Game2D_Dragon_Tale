/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Entity.Player;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Alice e Vitor
 */
public class Level1State extends GameState{
    //atributos
    private TileMap tileMap;
    private Background bg;
    
    private Player player;
    
    //construtor utiliza GSM pra criar a fase
    public Level1State(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    @Override
    public void init() {
        //inicializar o TileMap
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        
        //colocar o fundo do game com move scale de 0.1
        bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
        
        player = new Player(tileMap);
        player.setPosition(100, 100);
    }

    @Override
    public void update() {
        //update player
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getX(),
                GamePanel.HEIGHT / 2 - player.getY()        );
    }

    @Override
    public void draw(Graphics2D g) {
        //limpar a tela
        //g.setColor(Color.WHITE);
        //g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        
        //desenha o background
        bg.draw(g);
        
        //desenha tileMap
        tileMap.draw(g);
        
        //desenha o player
        player.draw(g);
    }

    @Override
    public void keyPressed(int key) {
        //setar os comandos do teclado aqui
      if(key == KeyEvent.VK_LEFT) player.setLeft(true);  
      if(key == KeyEvent.VK_RIGHT) player.setRight(true); 
      if(key == KeyEvent.VK_UP) player.setUp(true);
      if(key == KeyEvent.VK_DOWN) player.setDown(true);
      if(key == KeyEvent.VK_W) player.setJumping(true);
      if(key == KeyEvent.VK_E) player.setGliding(true);
      if(key == KeyEvent.VK_R) player.setScratching();
      if(key == KeyEvent.VK_F) player.setFiring();
      
    }

    @Override
    public void keyReleased(int key) {
      //tudo aqui é setado com false pra não ter diversas ações ao mesmo tempo
      if(key == KeyEvent.VK_LEFT) player.setLeft(false);  
      if(key == KeyEvent.VK_RIGHT) player.setRight(false); 
      if(key == KeyEvent.VK_UP) player.setUp(false);
      if(key == KeyEvent.VK_DOWN) player.setDown(false);
      if(key == KeyEvent.VK_W) player.setJumping(false);
      if(key == KeyEvent.VK_E) player.setGliding(false);
        
    }
    
}
