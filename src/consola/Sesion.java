package consola;

import java.io.File;
import java.io.IOException;

public class Sesion {

    private File raiz;
    private File actual;

    public Sesion() {
        this(elegirCarpetaRaiz());
    }

    public Sesion(File carpetaRaiz) {
        File carpeta = carpetaRaiz;
        carpeta.mkdirs();
        try {
            carpeta = carpetaRaiz.getCanonicalFile();
        } catch (IOException e) {
            carpeta = carpetaRaiz.getAbsoluteFile();
        }
        this.raiz = carpeta;
        this.actual = carpeta;
    }

    private static File elegirCarpetaRaiz() {
        File preferida = new File("C:\\ConsolaSimulada");
        File respaldo = new File("ConsolaSimulada");
        File elegida = respaldo;
        if (preferida.isAbsolute()) {
            preferida.mkdirs();
            if (preferida.isDirectory()) {
                elegida = preferida;
            }
        }
        return elegida;
    }

    public File getRaiz() {
        return raiz;
    }

    public File getActual() {
        return actual;
    }

    public void setActual(File carpeta) {
        if (dentroDeRaiz(carpeta)) {
            actual = carpeta;
        }
    }

    public File resolver(String nombre) {
        return new File(actual, nombre);
    }

    public boolean dentroDeRaiz(File f) {
        boolean dentro = false;
        try {
            String rutaArchivo = f.getCanonicalPath();
            String rutaRaiz = raiz.getCanonicalPath();
            dentro = rutaArchivo.equals(rutaRaiz) || rutaArchivo.startsWith(rutaRaiz + File.separator);
        } catch (IOException e) {
            dentro = false;
        }
        return dentro;
    }

    public String getPrompt() {
        return actual.getAbsolutePath() + ">";
    }
}
