package consola;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ConsolaGUI extends JFrame implements ActionListener {

    JTextArea salida;
    JLabel lblPrompt;
    JTextField entrada;
    Sesion sesion;
    Interprete interprete;
    boolean modoEscritura = false;
    String archivoDestino = "";
    boolean modoAppend = false;
    List<String> lineas = new ArrayList<>();

    public ConsolaGUI() {
        sesion = new Sesion();
        interprete = new Interprete(sesion, this);

        setTitle("Consola de Comandos");
        setSize(750, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Font fuente = new Font("Consolas", Font.PLAIN, 14);

        salida = new JTextArea();
        salida.setEditable(false);
        salida.setFont(fuente);
        salida.setBackground(Color.BLACK);
        salida.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(salida);

        lblPrompt = new JLabel();
        lblPrompt.setFont(fuente);
        lblPrompt.setForeground(Color.WHITE);

        entrada = new JTextField();
        entrada.setFont(fuente);
        entrada.setBackground(Color.BLACK);
        entrada.setForeground(Color.WHITE);
        entrada.setCaretColor(Color.WHITE);
        entrada.addActionListener(this);

        JPanel panelAbajo = new JPanel(new BorderLayout());
        panelAbajo.setBackground(Color.BLACK);
        panelAbajo.add(lblPrompt, BorderLayout.WEST);
        panelAbajo.add(entrada, BorderLayout.CENTER);

        add(scroll, BorderLayout.CENTER);
        add(panelAbajo, BorderLayout.SOUTH);

        imprimir("Consola de Comandos");
        imprimir("Escriba Help para ver los comandos.");
        imprimir("");
        actualizarPrompt();

        setVisible(true);
        entrada.requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String linea = entrada.getText();
        imprimir(sesion.getPrompt() + linea);

        String resultado = interprete.ejecutar(linea);
        if (!resultado.equals("")) {
            imprimir(resultado);
        }
        imprimir("");

        entrada.setText("");
        actualizarPrompt();
        entrada.requestFocusInWindow();
    }

    public void imprimir(String texto) {
        salida.append(texto + "\n");
        salida.setCaretPosition(salida.getDocument().getLength());
    }

    public void limpiar() {
        salida.setText("");
    }

    public void actualizarPrompt() {
        lblPrompt.setText(sesion.getPrompt());
    }

    public void iniciarEscritura(String archivo, boolean append) {
    }
}
