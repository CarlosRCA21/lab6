package consola;

import java.io.File;

public class Sesion {

    File raiz;
    File actual;

    public Sesion() {
        raiz = new File("ConsolaSimulada");
        if (!raiz.exists()) {
            raiz.mkdir();
        }
        actual = raiz;
    }

    public File getRaiz() {
        return raiz;
    }

    public File getActual() {
        return actual;
    }

    public void setActual(File carpeta) {
        actual = carpeta;
    }

    public File resolver(String nombre) {
        return new File(actual, nombre);
    }

    public boolean dentroDeRaiz(File f) {
        return true;
    }

    public String getPrompt() {
        return actual.getAbsolutePath() + ">";
    }
}
