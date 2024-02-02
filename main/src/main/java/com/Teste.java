package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;

public class Teste extends JPanel {

    // Atributos do aplicação
    private static final int FPS = 60;

    // configurando elevador 1(esquerda)
    private final Image elevador1 = carregarImagem("elevador1.png");
    private final int elevador1Largura = elevador1.getWidth(null);
    private final int elevador1Altura = elevador1.getHeight(null);

    private int elevador1CoordenadaX;
    private int elevador1CoordenadaY;
    private int deslocamentoYElevador1;

    // VAriáveis do moviemtno
    private int pontoFinalElevador1;

    // configurando elevador 2 (direita)
    private final Image elevador2 = carregarImagem("elevador2.png");
    private final int elevador2Largura = elevador2.getWidth(null);
    private final int elevador2Altura = elevador2.getHeight(null);

    private int elevador2CoordenadaX;
    private int elevador2CoordenadaY;
    private int deslocamentoYElevador2;

    // variaveis do movimento
    private int pontoFinalElevador2;

    // Configurando imagem de fundo e imagem do botao
    private final Image imagemDeFundo = carregarImagem("fundo.png");
    private final int larguraFundo = imagemDeFundo.getWidth(null);
    private final int alturaFundo = imagemDeFundo.getHeight(null);

    // Botões para chamar os elevadores
    private JButton[] botoes = new JButton[9];
    private int velocidadeElevadorSubindo;
    private int velocidadeElevadorDescendo;

    // Controles
    private boolean pausado;
    private boolean somLigado;
    private Timer timer;

    /**
     * CONSTRUTOR VAZIO
     */
    public Teste() {
        super();
    }

    /**
     * CONSTRUTOR PRINCIPAL DA APLICAÇÃO
     * 
     * @param largura
     * @param altura
     */
    public Teste(int largura, int altura) {

        this.setPreferredSize(new Dimension(largura, altura));
        this.setSize(largura, altura);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);

        // Inicialização dos componentes
        inicializarComponentes();

        // Inicialização das coordenadas dos elevadores
        this.elevador1CoordenadaX = largura / 4;
        this.elevador1CoordenadaY = altura - 95;

        this.elevador2CoordenadaX = largura - 256;
        this.elevador2CoordenadaY = altura - 95;

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

        // g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());


     

        g2d.drawImage(imagemDeFundo, 0, 0, larguraFundo, alturaFundo, null);

        // Dá o efeito de movimentação do objeto
        this.movimentaObjeto();

        // Desenha o objeto
        desenharObjetos(g2d);

        // Sincroniza a animação com o redesenho
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Método que atualiza as coordenadas do objeto que se move
     * 
     */
    public void movimentaObjeto() {
        // movimento do elevador
        this.elevador1CoordenadaY += deslocamentoYElevador1;
        this.elevador2CoordenadaY += deslocamentoYElevador2;
        if (this.elevador1CoordenadaY < 0 && this.elevador2CoordenadaY < 0) {

            this.elevador1CoordenadaY = 0;
            this.elevador2CoordenadaY = 0;

        }
        if (this.elevador1CoordenadaY + this.elevador1Largura > this.getWidth()
                && this.elevador1CoordenadaY + this.elevador2Largura > this.getWidth()) {
            this.elevador1CoordenadaY = this.getWidth() - this.elevador1Largura;
            this.elevador2CoordenadaY = this.getWidth() - this.elevador2Largura;
        }

    }

    private void inicializarComponentes() {
        // Inicialização dos controles:
        this.pausado = false;
        this.somLigado = true;

        // Set do layout
        setLayout(new GridBagLayout());

        // Definindo a posição padrão dos botões
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 50, 0);

        //Carregando o icone
        String caminho = "botao.png";
        URL url = getClass().getResource(caminho);
        //Verificação simples
        if (url == null) {
            System.out.println("Img não encontrada - " + caminho);
            return;
        }

        ImageIcon icon = new ImageIcon(url);
        
        for (int i = 0; i < 9; i++) {
        
            botoes[i] = new JButton(icon);
            botoes[i].setPreferredSize(new Dimension(100, 30));
            botoes[i].addActionListener(new BotaoListener());


            //manipulando espaçamento dos botões
            if(i % 2 == 0){
                gbc.insets = new Insets(10, 0, 5, 20);
            }else{
                gbc.insets = new Insets(10, 0, 5, 0);
            }

            this.add(botoes[i], gbc);
            gbc.gridy++;
        }
        botoes[0].requestFocusInWindow();

        this.setFocusable(true);

    }

    /**
     * Método que desenha os elevadores
     * 
     * @param g2d
     */
    public void desenharObjetos(Graphics2D g2d) {
        g2d.drawImage(elevador1, elevador1CoordenadaX, elevador1CoordenadaY, elevador1Largura, elevador1Altura, null);
        g2d.drawImage(elevador2, elevador2CoordenadaX, elevador2CoordenadaY, elevador2Largura, elevador2Altura, null);
        

    }

    /**
     * Classe que implementa o método de deslize e chegar até a altura do botão
     * indicado
     */
    private class BotaoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == botoes[i]) {
                    int botaoY = botoes[i].getLocation().y;
                    pontoFinalElevador1 = botaoY - elevador1Altura;
                    pontoFinalElevador2 = botaoY - elevador2Altura;
                    velocidadeElevadorSubindo = -2;
                    velocidadeElevadorDescendo = 2;
                    iniciarMovimentoElevador();

                    break;
                }
            }
            repaint();
        }
    }

    /**
     * Método para carregar as imagens
     * 
     * @param arquivo
     * @return
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
     * Método para iniciar o movimento do elevador
     * 
     * @param args
     */
    private void iniciarMovimentoElevador() {
        Timer timerMovimento = new Timer(100 / FPS, e -> {
            if (elevador1CoordenadaY > pontoFinalElevador1 && elevador2CoordenadaY > pontoFinalElevador2) {
                elevador1CoordenadaY += velocidadeElevadorSubindo;
                elevador2CoordenadaY += velocidadeElevadorSubindo;
                repaint();
            } else {
                ((Timer) e.getSource()).stop(); // Finaliza o timer quando bater a altura
            }
            // if (elevador1CoordenadaY < pontoFinalElevador1 && elevador2CoordenadaY < pontoFinalElevador2) {
            //     elevador1CoordenadaY += velocidadeElevadorDescendo;
            //     elevador2CoordenadaY += velocidadeElevadorDescendo;
            //     repaint();
            // } else {
            //     ((Timer) e.getSource()).stop(); // Finaliza o timer quando bater a altura
            // }

        });
        timerMovimento.start();
    }

    public static void main(String[] args) {
        // Atributos do frame
        int largura = 900;
        int altura = 900;

        // Frame principal
        JFrame frame = new JFrame();
        frame.setTitle("Animação Java 2D");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(largura, altura);

        // Set do frame
        frame.add(new Teste(largura, altura));
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
