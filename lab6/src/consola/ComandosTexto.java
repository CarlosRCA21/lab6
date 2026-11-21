package consola;

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
        return "";
    }

    public String copy(String origen, String destino) {
        return "";
    }

    public String escribir(String n, List<String> lineas, boolean append) {
        return "";
    }
}
