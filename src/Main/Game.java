/*
 * ****** !IMPORTANTE! ******
 * Para que as imagens funcionem durante a compilação é necessário setar o 
 * pacote Resources como classpath/buildpath no Netbeans é só seguir abaixo:
 * Botão direito no Projeto > Propriedades > Bibliotecas (lado esquerdo)>
 * Vá para Tab Compilar > Botão Adicionar JAR/Pasta
 */
package Main;

import javax.swing.JFrame;

/**
 *
 * @author Alice e Vitor
 */
public class Game {
    
    public static void main(String[] args) {
        
        JFrame window = new JFrame("Dragon Tale");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
