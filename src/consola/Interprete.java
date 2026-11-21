package consola;

public class Interprete {

    Sesion sesion;
    ConsolaGUI gui;
    ComandosTexto texto;
    ComandosArchivo archivos;

    public Interprete(Sesion sesion, ConsolaGUI gui) {
        this.sesion = sesion;
        this.gui = gui;
        texto = new ComandosTexto(sesion);
        archivos = new ComandosArchivo(sesion);
    }

    public String ejecutar(String linea) {
        linea = linea.trim();
        if (linea.equals("")) {
            return "";
        }

        String[] partes = linea.split(" ");
        String comando = partes[0];
        String error = "La sintaxis del comando no es correcta.";

        switch (comando) {
            case "Mkdir":
                if (partes.length != 2) {
                    return error;
                }
                return archivos.mkdir(partes[1]);

            case "Mfile":
                if (partes.length != 2) {
                    return error;
                }
                return archivos.mfile(partes[1]);

            case "Rm":
                if (partes.length != 2) {
                    return error;
                }
                return archivos.rm(partes[1]);

            case "Cd":
                if (partes.length != 2) {
                    return error;
                }
                return archivos.cd(partes[1]);

            case "Cd..":
                if (partes.length != 1) {
                    return error;
                }
                return archivos.cdAtras();

            case "Dir":
                if (partes.length != 1) {
                    return error;
                }
                return archivos.dir();

            case "Ren":
                if (partes.length != 3) {
                    return error;
                }
                return archivos.ren(partes[1], partes[2]);

            case "Find":
                if (partes.length != 2) {
                    return error;
                }
                return archivos.find(partes[1]);

            case "Info":
                if (partes.length != 2) {
                    return error;
                }
                return archivos.info(partes[1]);

            case "Tree":
                if (partes.length != 1) {
                    return error;
                }
                return archivos.tree();

            case "Date":
                if (partes.length != 1) {
                    return error;
                }
                return texto.date();

            case "Time":
                if (partes.length != 1) {
                    return error;
                }
                return texto.time();

            case "Wr":
                if (partes.length != 2) {
                    return error;
                }
                if (!texto.validarEscritura(partes[1]).equals("")) {
                    return texto.validarEscritura(partes[1]);
                }
                gui.iniciarEscritura(partes[1], false);
                return "Escriba el texto. Escriba EXIT para terminar.";

            case "Ap":
                if (partes.length != 2) {
                    return error;
                }
                if (!texto.validarEscritura(partes[1]).equals("")) {
                    return texto.validarEscritura(partes[1]);
                }
                gui.iniciarEscritura(partes[1], true);
                return "Escriba el texto a agregar. Escriba EXIT para terminar.";

            case "Rd":
                if (partes.length != 2) {
                    return error;
                }
                return texto.rd(partes[1]);

            case "Copy":
                if (partes.length != 3) {
                    return error;
                }
                return texto.copy(partes[1], partes[2]);

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
