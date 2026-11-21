package consola;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ComandosTexto {

    Sesion sesion;

    public ComandosTexto(Sesion sesion) {
        this.sesion = sesion;
    }

    public String date() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return "Fecha actual: " + formato.format(new Date());
    }

    public String time() {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        return "Hora actual: " + formato.format(new Date());
    }

    public String help() {
        String ayuda = "";
        ayuda += "Mkdir <nombre>       Crea una carpeta\n";
        ayuda += "Mfile <nombre.ext>   Crea un archivo\n";
        ayuda += "Rm <nombre>          Borra un archivo o carpeta\n";
        ayuda += "Cd <carpeta>         Entra a una carpeta\n";
        ayuda += "Cd..                 Regresa a la carpeta anterior\n";
        ayuda += "Dir                  Muestra lo que hay en la carpeta\n";
        ayuda += "Date                 Muestra la fecha\n";
        ayuda += "Time                 Muestra la hora\n";
        ayuda += "Wr <archivo.ext>     Escribe en un archivo (terminar con EXIT)\n";
        ayuda += "Rd <archivo.ext>     Lee un archivo\n";
        ayuda += "Ap <archivo.ext>     Agrega texto a un archivo (terminar con EXIT)\n";
        ayuda += "Ren <viejo> <nuevo>  Cambia el nombre\n";
        ayuda += "Copy <orig> <dest>   Copia un archivo\n";
        ayuda += "Find <nombre>        Busca archivos o carpetas\n";
        ayuda += "Info <nombre>        Muestra informacion\n";
        ayuda += "Tree                 Muestra el arbol de carpetas\n";
        ayuda += "Cls                  Limpia la pantalla\n";
        ayuda += "Help                 Muestra esta ayuda\n";
        ayuda += "Exit                 Cierra la consola";
        return ayuda;
    }

    public String rd(String n) {
        File f = sesion.resolver(n);
        if (!sesion.dentroDeRaiz(f) || !f.exists()) {
            return "El sistema no puede encontrar el archivo especificado.";
        }
        if (f.isDirectory()) {
            return "No se puede leer una carpeta.";
        }

        String contenido = "";
        try {
            BufferedReader lector = new BufferedReader(new FileReader(f));
            String linea = lector.readLine();
            while (linea != null) {
                contenido += linea + "\n";
                linea = lector.readLine();
            }
            lector.close();
        } catch (IOException e) {
            return "Error al leer el archivo.";
        }

        if (contenido.equals("")) {
            return "(El archivo esta vacio)";
        }
        return contenido.substring(0, contenido.length() - 1);
    }

    public String copy(String origen, String destino) {
        File f1 = sesion.resolver(origen);
        File f2 = sesion.resolver(destino);

        if (!sesion.dentroDeRaiz(f1) || !f1.exists() || f1.isDirectory()) {
            return "El sistema no puede encontrar el archivo especificado.";
        }
        if (!sesion.dentroDeRaiz(f2)) {
            return "La sintaxis del nombre de archivo, directorio o etiqueta del volumen no es correcta.";
        }
        if (f2.exists()) {
            return "Ya existe un archivo o carpeta con ese nombre.";
        }

        try {
            BufferedReader lector = new BufferedReader(new FileReader(f1));
            FileWriter escritor = new FileWriter(f2);
            String linea = lector.readLine();
            while (linea != null) {
                escritor.write(linea + "\n");
                linea = lector.readLine();
            }
            lector.close();
            escritor.close();
        } catch (IOException e) {
            return "Error al copiar el archivo.";
        }
        return "1 archivo(s) copiado(s).";
    }

    public String validarEscritura(String n) {
        File f = sesion.resolver(n);
        if (!sesion.dentroDeRaiz(f)) {
            return "La sintaxis del nombre de archivo, directorio o etiqueta del volumen no es correcta.";
        }
        if (f.isDirectory()) {
            return "No se puede escribir en una carpeta.";
        }
        return "";
    }

    public String escribir(String n, List<String> lineas, boolean append) {
        File f = sesion.resolver(n);
        if (!sesion.dentroDeRaiz(f)) {
            return "La sintaxis del nombre de archivo, directorio o etiqueta del volumen no es correcta.";
        }
        if (f.isDirectory()) {
            return "No se puede escribir en una carpeta.";
        }

        try {
            FileWriter escritor = new FileWriter(f, append);
            for (int i = 0; i < lineas.size(); i++) {
                escritor.write(lineas.get(i) + "\n");
            }
            escritor.close();
        } catch (IOException e) {
            return "Error al escribir el archivo.";
        }
        return "Archivo guardado.";
    }
}
