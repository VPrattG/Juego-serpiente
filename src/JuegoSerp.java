import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //Segmentos de la serpiente
import java.util.Random; //Para las coordenadas de los puntos
import javax.swing.*;

public class JuegoSerp extends JPanel implements ActionListener, KeyListener{ //Extends = heredar propiedades
    //Clase para administrar coordenadas de cada casilla
    private class Tile{
        int x;
        int y;

        Tile(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
    
    int boardWidth;
    int boardHeight;
    int tileSize = 25; //Tablero de 25x25 casillas
    //Se multiplicacrán coordenadas x y por 25
    //Dentro del juego, serían coordenadas (x, y)
    //En el código, serían (25x, 25y)
    
    //Serpiente
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //Puntos
    Tile food;
    Random rngsus;

    //Lógica del juego
    Timer gameLoop; //Para "jugar", se debe rehacer la ventana constantemente
    int velocityX; //Lo rápido que se mueve lateralmente
    int velocityY; //Lo rápido que se mueve verticalmente
    boolean gameOver = false;

    JuegoSerp(int boardWidth, int boardHeight){
        //Puntero this necesario para distinguir el parámetro de la variable de la clase
        this.boardWidth = boardWidth; 
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5); //Posición predeterminada arbitaria
        snakeBody = new ArrayList<>();

        food = new Tile(10, 10); //Posición predeterminada arbitraria
        rngsus = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 1; //El juego empezará con la serpiente moviéndose hacia abajo
        
        gameLoop = new Timer(100, this); //Ajusta la velocidad del juego (Menor valor, mayor velocidad)
        gameLoop.start();
    }

    @Override
    public void paintComponent(Graphics gra){
        super.paintComponent(gra);
        draw(gra);
    }

    public void draw(Graphics gra){
        //Pintar división de casillas (Habilitar si se quiere cuadrícula)
        for(int i = 0; i < boardWidth/tileSize; i++){
            gra.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            gra.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
        
        //Puntos
        gra.setColor(Color.red);
        gra.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        //Serpiente (Cabeza)
        gra.setColor(Color.green);
        gra.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //Serpiente (Cuerpo)
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            gra.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //Puntuación
        gra.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            gra.setColor(Color.red);
            gra.drawString("Fin del juego: " + snakeBody.size(), tileSize - 16, tileSize);
        }
        else{
            //Se puede usar String.valueOf(snakeBody.size())
            gra.drawString("Puntuación: " + snakeBody.size(), tileSize - 16, tileSize);
        }
    }

    public void placeFood(){
        //Valores de 0 a 24 si tileSize es 25
        food.x = rngsus.nextInt(boardWidth/tileSize);
        food.y = rngsus.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move(){
        //Puntos
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        
        //Serpiente (Cuerpo)
        for (int i = snakeBody.size() - 1; i >= 0; i--){
            //Iteraciones al revés, de otro modo no funcionaría
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Serpiente (Cabeza)
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //Fin del juego
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //Colisión con la cabeza
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
            //Colisión con alguna esquina
            if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
                gameOver = true;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint(); //Llama a draw() contstantemente
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Las sentencias piden que no se esté moviendo en el sentido contrario, para evitar chocar con el cuerpo propio
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode () == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { 
        //No necesario
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //No necesario
    }
}