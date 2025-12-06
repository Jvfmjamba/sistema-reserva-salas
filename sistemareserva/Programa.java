package sistemareserva;

import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import sistemareserva.visao.ReservaGUI;

public class Programa {

    public static void main(String[] args){
        try{
            //LookAndFeel pra deixar a janela mais bonita
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception ignored){}

        // inicializa a interface grafica
        SwingUtilities.invokeLater(() -> {
            new ReservaGUI(); 
        });
    }
}
