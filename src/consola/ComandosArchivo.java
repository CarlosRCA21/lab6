package consola;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class ComandosArchivo {

    private static final String SINTAXIS_INCORRECTA = "La sintaxis del comando no es correcta.";
    private static final String NOMBRE_INVALIDO = "La sintaxis del nombre de archivo, directorio o etiqueta del volumen no es correcta.";
    private static final String ARCHIVO_NO_ENCONTRADO = "El sistema no puede encontrar el archivo especificado.";
    private static final String RUTA_NO_ENCONTRADA = "El sistema no puede encontrar la ruta especificada.";
    private static final String YA_EXISTE = "Ya existe un archivo o carpeta con ese nombre.";
    private static final String FUERA_DE_RAIZ = "Acceso denegado: no se puede salir de la carpeta raíz.";
    private static final String CARACTERES_PROHIBIDOS = "/\\:*?\"<>|";

    private Sesion sesion;

    public ComandosArchivo(Sesion sesion) {
        this.sesion = sesion;
    }

    public String mkdir(String nombre) {
        String error = errorDeNombre(nombre);
        if (!error.isEmpty()) {
            return error;
        }
        File carpeta = sesion.resolver(nombre);
        if (carpeta.exists()) {
            return YA_EXISTE;
        }
        if (carpeta.mkdir()) {
            return "Carpeta creada: " + nombre;
        }
        return "No se pudo crear la carpeta.";
    }

    public String mfile(String nombre) {
        String error = errorDeNombre(nombre);
        if (!error.isEmpty()) {
            return error;
        }
        File archivo = sesion.resolver(nombre);
        if (archivo.exists()) {
            return YA_EXISTE;
        }
        try {
            archivo.createNewFile();
            return "Archivo creado: " + nombre;
        } catch (IOException e) {
            return "No se pudo crear el archivo.";
        }
    }

    public String rm(String nombre) {
        String error = errorDeNombre(nombre);
        if (!error.isEmpty()) {
            return error;
        }
        File objetivo = sesion.resolver(nombre);
        if (!objetivo.exists()) {
            return ARCHIVO_NO_ENCONTRADO;
        }
        boolean eraCarpeta = objetivo.isDirectory();
        if (borrarRecursivo(objetivo)) {
            if (eraCarpeta) {
                return "Carpeta eliminada con todo su contenido: " + nombre;
            }
            return "Archivo eliminado: " + nombre;
        }
        return "No se pudo eliminar por completo (puede estar en uso).";
    }

    public String cd(String nombre) {
        String error = errorDeNombre(nombre);
        if (!error.isEmpty()) {
            return error;
        }
        File destino = sesion.resolver(nombre);
        if (!destino.exists()) {
            return RUTA_NO_ENCONTRADA;
        }
        if (!destino.isDirectory()) {
            return "El nombre del directorio no es válido.";
        }
        if (!sesion.dentroDeRaiz(destino)) {
            return FUERA_DE_RAIZ;
        }
        sesion.setActual(destino);
        return "";
    }

    public String cdAtras() {
        if (sesion.getActual().equals(sesion.getRaiz())) {
            return "Ya estás en la carpeta raíz.";
        }
        File padre = sesion.getActual().getParentFile();
        sesion.setActual(padre);
        return "";
    }

    public String dir() {
        File carpeta = sesion.getActual();
        File[] hijos = listarOrdenado(carpeta);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy  HH:mm");
        StringBuilder sb = new StringBuilder();
        long totalBytes = 0;
        int totalArchivos = 0;
        int totalCarpetas = 0;

        sb.append(" Directorio de ").append(carpeta.getAbsolutePath()).append("\n\n");
        for (int i = 0; i < hijos.length; i++) {
            File hijo = hijos[i];
            String fecha = formato.format(new Date(hijo.lastModified()));
            String columna = "";
            if (hijo.isDirectory()) {
                columna = String.format("%-14s", "<DIR>");
                totalCarpetas++;
            } else {
                columna = String.format(Locale.US, "%14d", hijo.length());
                totalArchivos++;
                totalBytes = totalBytes + hijo.length();
            }
            sb.append(fecha).append("  ").append(columna).append(" ").append(hijo.getName()).append("\n");
        }
        sb.append(String.format(Locale.US, "%15d archivos %14d bytes", totalArchivos, totalBytes)).append("\n");
        sb.append(String.format(Locale.US, "%15d carpetas", totalCarpetas));
        return sb.toString();
    }

    public String ren(String actual, String nuevo) {
        String errorActual = errorDeNombre(actual);
        if (!errorActual.isEmpty()) {
            return errorActual;
        }
        String errorNuevo = errorDeNombre(nuevo);
        if (!errorNuevo.isEmpty()) {
            return errorNuevo;
        }
        File origen = sesion.resolver(actual);
        File destino = sesion.resolver(nuevo);
        if (!origen.exists()) {
            return ARCHIVO_NO_ENCONTRADO;
        }
        if (destino.exists() && !mismoElemento(origen, destino)) {
            return YA_EXISTE;
        }
        if (origen.renameTo(destino)) {
            return "Se cambió el nombre de " + actual + " a " + nuevo;
        }
        return "No se pudo cambiar el nombre.";
    }

    public String find(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return SINTAXIS_INCORRECTA;
        }
        StringBuilder sb = new StringBuilder();
        int total = buscar(sesion.getActual(), texto.toLowerCase(), sb);
        if (total == 0) {
            return "No se encontraron archivos ni carpetas que contengan \"" + texto + "\".";
        }
        sb.append("Total: ").append(total).append(" coincidencia(s)");
        return sb.toString();
    }

    public String info(String nombre) {
        String error = errorDeNombre(nombre);
        if (!error.isEmpty()) {
            return error;
        }
        File objetivo = sesion.resolver(nombre);
        if (!objetivo.exists()) {
            return ARCHIVO_NO_ENCONTRADO;
        }
        String tipo = "Archivo";
        if (objetivo.isDirectory()) {
            tipo = "Carpeta";
        }
        long tamano = calcularTamano(objetivo);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fecha = formato.format(new Date(objetivo.lastModified()));

        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(objetivo.getName()).append("\n");
        sb.append("Tipo: ").append(tipo).append("\n");
        sb.append("Ruta: ").append(objetivo.getAbsolutePath()).append("\n");
        sb.append("Tamaño: ").append(tamano).append(" bytes\n");
        sb.append("Última modificación: ").append(fecha);
        return sb.toString();
    }

    public String tree() {
        StringBuilder sb = new StringBuilder();
        sb.append(sesion.getActual().getAbsolutePath());
        arbol(sesion.getActual(), "", sb);
        return sb.toString();
    }

    private boolean borrarRecursivo(File elemento) {
        boolean todoBorrado = true;
        if (puedeEntrar(elemento)) {
            File[] hijos = elemento.listFiles();
            if (hijos != null) {
                for (int i = 0; i < hijos.length; i++) {
                    if (!borrarRecursivo(hijos[i])) {
                        todoBorrado = false;
                    }
                }
            }
        }
        if (!elemento.delete()) {
            todoBorrado = false;
        }
        return todoBorrado;
    }

    private int buscar(File carpeta, String texto, StringBuilder sb) {
        int coincidencias = 0;
        File[] hijos = listarOrdenado(carpeta);
        for (int i = 0; i < hijos.length; i++) {
            File hijo = hijos[i];
            if (hijo.getName().toLowerCase().contains(texto)) {
                sb.append(hijo.getAbsolutePath()).append("\n");
                coincidencias++;
            }
            if (puedeEntrar(hijo)) {
                coincidencias = coincidencias + buscar(hijo, texto, sb);
            }
        }
        return coincidencias;
    }

    private void arbol(File carpeta, String prefijo, StringBuilder sb) {
        File[] hijos = listarOrdenado(carpeta);
        for (int i = 0; i < hijos.length; i++) {
            boolean esUltimo = (i == hijos.length - 1);
            String rama = "|-- ";
            String continuacion = "|   ";
            if (esUltimo) {
                rama = "`-- ";
                continuacion = "    ";
            }
            sb.append("\n").append(prefijo).append(rama).append(hijos[i].getName());
            if (puedeEntrar(hijos[i])) {
                arbol(hijos[i], prefijo + continuacion, sb);
            }
        }
    }

    private long calcularTamano(File elemento) {
        long total = 0;
        if (puedeEntrar(elemento)) {
            File[] hijos = elemento.listFiles();
            if (hijos != null) {
                for (int i = 0; i < hijos.length; i++) {
                    total = total + calcularTamano(hijos[i]);
                }
            }
        } else if (elemento.isFile()) {
            total = elemento.length();
        }
        return total;
    }

    private File[] listarOrdenado(File carpeta) {
        File[] hijos = carpeta.listFiles();
        if (hijos == null) {
            return new File[0];
        }
        Arrays.sort(hijos);
        File[] ordenado = new File[hijos.length];
        int posicion = 0;
        for (int i = 0; i < hijos.length; i++) {
            if (hijos[i].isDirectory()) {
                ordenado[posicion] = hijos[i];
                posicion++;
            }
        }
        for (int i = 0; i < hijos.length; i++) {
            if (!hijos[i].isDirectory()) {
                ordenado[posicion] = hijos[i];
                posicion++;
            }
        }
        return ordenado;
    }

    private String errorDeNombre(String nombre) {
        String error = "";
        if (nombre == null || nombre.trim().isEmpty()) {
            error = SINTAXIS_INCORRECTA;
        } else if (nombre.equals(".") || nombre.contains("..") || tieneCaracterProhibido(nombre)) {
            error = NOMBRE_INVALIDO;
        }
        return error;
    }

    private boolean tieneCaracterProhibido(String nombre) {
        boolean hayProhibido = false;
        for (int i = 0; i < nombre.length(); i++) {
            if (CARACTERES_PROHIBIDOS.indexOf(nombre.charAt(i)) >= 0) {
                hayProhibido = true;
            }
        }
        return hayProhibido;
    }

    private boolean puedeEntrar(File carpeta) {
        return carpeta.isDirectory() && sesion.dentroDeRaiz(carpeta) && !esEnlace(carpeta);
    }

    private boolean esEnlace(File elemento) {
        boolean enlace = false;
        try {
            File padre = elemento.getParentFile();
            if (padre != null) {
                File sinResolver = new File(padre.getCanonicalFile(), elemento.getName());
                enlace = !sinResolver.getCanonicalPath().equalsIgnoreCase(sinResolver.getAbsolutePath());
            }
        } catch (IOException e) {
            enlace = true;
        }
        return enlace;
    }

    private boolean mismoElemento(File a, File b) {
        boolean mismo = false;
        try {
            mismo = a.getCanonicalFile().equals(b.getCanonicalFile());
        } catch (IOException e) {
            mismo = false;
        }
        return mismo;
    }
}
