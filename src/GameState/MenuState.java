
package GameState;

import TileMap.Background;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 *
 * @author Alice e Vitor
 */
public class MenuState extends GameState{
    
    private Background bg;
    //para saber qual opção escolhida
    private int currentChoice = 0; //começa setando em start
    //vetor com as opções de menu do game
    private String[] options = {
        "Start",
        "Help",
        "Quit"
    }; 
    //cor do titulo
    private Color titleColor;
    //fonte do titulo e comum
    private Font titleFont;
    private Font font;
    
    //trazer tudo isso para o construtor do Menu do game
    public MenuState(GameStateManager gsm){
        this.gsm = gsm;
        
        try {
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector(-0.1, 0); //mover backgroud para a esquerda
            
            //setar cores e fontes do menu
            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Arial", Font.PLAIN, 12);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void init() {
        
    }

    @Override
    public void update() {
        bg.update();
    }

    @Override
    public void draw(Graphics2D g) {
        //desenhar o fundo
        bg.draw(g);
        //desenhar o titulo
        g.setColor(titleColor);
        g.setFont(titleFont);
        //escrever titulo do game e posição no menu
        g.drawString("Dragon Tale", 80, 70);
        
        //desenhar as opções do menu
        //usaremos a fonte regular
        g.setFont(font);
        //percorrer a lista de opções e mudar a cor pra opção desejada
        for(int i = 0; i < options.length; i++){
            if(i == currentChoice){
                g.setColor(Color.RED);
            }else{
               g.setColor(Color.BLACK);
            }
            //desenhar as opções do menu com 15 pixels entre elas
            g.drawString(options[i], 145, 145 + i * 15);
        }
        
    }

    private void select(){
        if(currentChoice == 0){ //start
            //se escolha for start setamos o state pro level 1
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if(currentChoice == 1){ //help
            //
        }
        if(currentChoice == 2){ //quit
            System.exit(0);
        }
    }
    
    @Override
    public void keyPressed(int key) {
        if(key == KeyEvent.VK_ENTER){ //se a tecla for enter selecionar a option
            select();
        }
        if(key == KeyEvent.VK_UP){ //se a tecla for seta pra cima
            currentChoice --; //escolha diminui pra subir
            if(currentChoice == -1){ //se for negativa,descemos ao final do menu
                currentChoice = options.length - 1;
            }   
        }
        if(key == KeyEvent.VK_DOWN){ //se a tecla for seta pra baixo
            currentChoice ++; //escolha aumenta pra descer no menu
            if(currentChoice == options.length){ //se for ultrpassar o limite
                currentChoice = 0; //colocamos de volta pro inicio
            }
        }
    }

    @Override
    public void keyReleased(int key) {
        
    }
    
}
