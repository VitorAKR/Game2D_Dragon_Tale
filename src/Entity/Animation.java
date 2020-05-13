/*
 * Essa classe cuidará de todas animações de sprites
 */
package Entity;

import java.awt.image.BufferedImage;

/**
 *
 * @author Alice e Vitor
 */
public class Animation {
    //usaremos um array de imagens para cuidar de todos os frames
    private BufferedImage[] frames;
    private int currentFrame; //pra rastrear frame atual
    private long startTime; //timer entre os frames
    private long delay; //quanto tempo entre cada frame
    private boolean playedOnce; //pra dizer se a animção tinha sido rodada ou por exemplo em ataques 1x
    
    //construtor
    public void Animation(){
        playedOnce = false;
    }
    
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        currentFrame = 0; //começar no frame zero
        startTime = System.nanoTime();
        playedOnce = false;
    }
    
    public void setDelay(long d){
        delay = d;
    }
    
    public void setFrame(int i){
        currentFrame = i;
    }
    
    //esta function é importante para determinar a atualizacao dos frames da animacao
    public void update(){
        if(delay == -1) return; //não há razão de ter animacao
        
        //do contrário precisamos definir quanto tempo entre um frame de outro
        //isso dividido por 1000000 para ter milisegundos
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > delay){
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame == frames.length){ //checar se os frames não passarão do numero máximo 
            currentFrame = 0; //voltar pro inicio
            playedOnce = true; //desta maneira a animção rodou 1x
        }
    }
    
    //getters
    public int getFrame(){
        return currentFrame;
    }
    
    public BufferedImage getImage(){
        return frames[currentFrame];
    }
    
    public boolean hasPlayedOnce(){
        return playedOnce; 
    }
    
}
