package com;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;

public class App extends JPanel   {

    //Atributos da aplicação 
    private static final int FPS = 60;


    //configurando elevador 1(esquerda)
    private final Image elevador1 = carregarImagem("elevador1.png");
    private final int elevador1Largura = elevador1.getWidth(null);
    private final int elevador1Altura = elevador1.getHeight(null);
    
    private int elevador1CoordenadaX;
    private int elevador1CoordenadaY;
    private int deslocamentoYElevador1;
    
    //configurando elevador 2 (direita)
    private final Image elevador2 = carregarImagem("elevador2.png");
    private final int elevador2Largura = elevador2.getWidth(null);
    private final int elevador2Altura = elevador2.getHeight(null);

    private int elevador2CoordenadaX;
    private int elevador2CoordenadaY;
    private int deslocamentoYElevador2;



    
    //Configurando imagem de fundo
    private final Image imagemDeFundo = carregarImagem("fundo.png");
    private final int larguraFundo = imagemDeFundo.getWidth(null);
    private final int alturaFundo = imagemDeFundo.getHeight(null);
    
    
    
    
    //Controles
    private boolean pausado;
    private boolean somLigado;
    private Timer timer;
    
    //Botões para chamar o elevador    
    private JButton[] botoes = new JButton[9];
    
    public App() {
        super();
    }
    
    /**
     * Construtor da classe
     * 
     * @param largura largura da área de desenho
     * @param altura  altura da área de desenho
     */
    public App(int largura, int altura) {

        this.setPreferredSize(new Dimension(largura, altura));
        this.setSize(largura, altura);
        this.setBackground(Color.BLACK);
        setFocusable(true);

        //Inicializa as coordenadas do elevador 1
        this.elevador1CoordenadaX = largura / 2;
        this.elevador1CoordenadaY = altura - 50;

        //Inicializa as coordenadas do elevador 2
        this.elevador2CoordenadaX = largura / 3;
        this.elevador2CoordenadaY = altura - 50;

        //Inicializa o som
        this.pausado = false;
        this.somLigado = true;


        //Setando o layout
        setLayout(new GridBagLayout());

        //Definindo a posição dos botoes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 100, 0);

        for (int i = 0; i < 9; i++) {
            botoes[i] = new JButton("Botão " + (i + 1));
            botoes[i].setPreferredSize(new Dimension(100, 30));
            botoes[i].addActionListener(new BotaoListener());
            this.add(botoes[i], gbc);
            gbc.gridy++;
        }

        this.setFocusable(true);

        // invoca o método paint() dessa classe a cada 1000/FPS milissegundos
        this.timer = new Timer(1000 / FPS, e -> {
            repaint();
        });
        // disparando o timer
        this.timer.start();
    }


    /**
     * Atualiza as coordenadas dos objetos e desenha os objetos na tela
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        var g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (!this.pausado) {
            // atualiza as coordenadas do objeto que se move
            this.movimentaObjeto();

            // desenha os objetos na tela
            this.desenharObjetos(g2d);

          

        
        } 
        // sincroniza o contexto gráfico
        Toolkit.getDefaultToolkit().sync();
    }


    /**
     * Atualiza as coordenadas do objeto que se move
     * 
     */
    public void movimentaObjeto() {
        // movimento do elevador 
        this.elevador1CoordenadaY += this.deslocamentoYElevador1;
        this.elevador2CoordenadaY += deslocamentoYElevador2;
        if (this.elevador1CoordenadaY < 0 && this.elevador2CoordenadaY < 0 ) {

            this.elevador1CoordenadaY = 0;
            this.elevador2CoordenadaY = 0;

        }
        if (this.elevador1CoordenadaY + this.elevador1Largura > this.getWidth() && this.elevador1CoordenadaY + this.elevador2Largura > this.getWidth()) {
            this.elevador1CoordenadaY = this.getWidth() - this.elevador1Largura;
            this.elevador2CoordenadaY = this.getWidth() - this.elevador2Largura;
        }

        
    }

  

    /**
     * Desenha os objetos na tela
     * 
     * @param g2d objeto gráfico
     */
    public void desenharObjetos(Graphics2D g2d) {
        
        // Desenhando os elevadores
        g2d.drawImage(elevador1, elevador1CoordenadaX, elevador1CoordenadaY, elevador1Largura, elevador1Altura, null);
        g2d.drawImage(elevador2, elevador2CoordenadaX, elevador2CoordenadaY, elevador2Largura, elevador2Altura, null);
          //renderizando a imagem de fundo
          g2d.drawImage(imagemDeFundo, 0, 0, larguraFundo, alturaFundo, null);


    }

   
    private class BotaoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == botoes[i]) {
                    int botaoY = botoes[i].getLocation().y;
                    elevador1CoordenadaY = botaoY - elevador1Altura; // Ajuste para o topo do elevador ficar alinhado com o botão
                    break;
                }
            }
            repaint();
        }
    }


    /**
     * Carrega uma imagem
     * 
     * @param arquivo nome do arquivo de imagem que deve estar na pasta
     *                /src/main/resources
     * @return imagem carregada
     */
    private Image carregarImagem(String arquivo) {
        URL url = getClass().getResource(arquivo);
        if (url == null) {
            throw new IllegalArgumentException("Imagem " + arquivo + " não encontrada");
        }

        ImageIcon ii = new ImageIcon(url);
        if (ii.getImageLoadStatus() != MediaTracker.COMPLETE) {
            throw new IllegalArgumentException("Erro ao carregar a imagem " + arquivo);
        }

        return ii.getImage();
    }

    /**
     * Reproduz um arquivo de áudio
     * 
     * @param arquivo nome do arquivo de áudio que deve estar na pasta
     *                /src/main/resources
     */
    private void reproduzirAudio(String arquivo) {
        if (somLigado) {
            try (var audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/" + arquivo))) {
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Erro ao carregar o arquivo de áudio " + arquivo);
            }
        }
    }

    

    public static void main(String[] args) {
        //Atributos do frame
        int largura = 900;
        int altura = 900;

        // Frame principal
        JFrame frame = new JFrame();
        frame.setTitle("Animação Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(largura, altura);

       //Set do frame
        frame.add(new App(largura, altura));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }


    public Object getGreeting() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGreeting'");
    }
}
