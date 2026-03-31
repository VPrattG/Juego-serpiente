import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;
        //Creación de ventana
        JFrame frame = new JFrame("Serpiente");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //Aparece la pantalla en el centro
        frame.setResizable(false); //No puede cambiar el tamaño
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //Puede usar JFrame

        JuegoSerp juego = new JuegoSerp(boardWidth, boardHeight);
        frame.add(juego);
        //Las dimensiones incluyen la sección de la ventana con el título
        frame.pack(); //Permite que las dimensiones sean correctas
        //(El contenido está en o por encima de los tamaños preferidos)
        juego.requestFocus();
    }
}
