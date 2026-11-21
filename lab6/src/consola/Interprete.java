package consola;

public class Interprete {

    Sesion sesion;
    ConsolaGUI gui;
    ComandosTexto texto;

    public Interprete(Sesion sesion, ConsolaGUI gui) {
        this.sesion = sesion;
        this.gui = gui;
        texto = new ComandosTexto(sesion);
    }

    public String ejecutar(String linea) {
        linea = linea.trim();
        if (linea.equals("")) {
            return "";
        }

        String[] partes = linea.split(" ");
        String comando = partes[0];

        switch (comando) {
            case "Date":
                if (partes.length != 1) {
                    return "La sintaxis del comando no es correcta.";
                }
                return texto.date();

            case "Time":
                if (partes.length != 1) {
                    return "La sintaxis del comando no es correcta.";
                }
                return texto.time();

            case "Help":
                return texto.help();

            case "Cls":
                gui.limpiar();
                return "";

            case "Exit":
                System.exit(0);
                return "";

            default:
                return "'" + comando + "' no se reconoce como un comando interno o externo,\n"
                        + "programa o archivo por lotes ejecutable.";
        }
    }
}
