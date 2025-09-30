import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class Cliente {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 8080);
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader lectorServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Servidor: " + lectorServidor.readLine());
            String opcionLogin = teclado.readLine();
            escritor.println(opcionLogin);
            if ("3".equals(opcionLogin)) {
                String linea;
                while (!(linea = lectorServidor.readLine()).equals("FIN_USUARIOS")) {
                    System.out.println(linea);
                }
                return;
            }
            System.out.println("Servidor: " + lectorServidor.readLine());
            String usuario = teclado.readLine();
            escritor.println(usuario);
            System.out.println("Servidor: " + lectorServidor.readLine());
            String password = teclado.readLine();
            escritor.println(password);
            String respuestaAuth = lectorServidor.readLine();
            System.out.println("Servidor: " + respuestaAuth);
            if (!respuestaAuth.contains("Autenticación exitosa") && !respuestaAuth.contains("registrado exitosamente")) {
                System.out.println("No se pudo iniciar sesión.");
                return;
            }

            if (lectorServidor.ready()) {
                System.out.println(lectorServidor.readLine());
            }

            while (true) {
                mostrarMenu();
                String opcionMenu = teclado.readLine();
                escritor.println(opcionMenu);

                if ("11".equals(opcionMenu)) { // SALIR
                    System.out.println("Desconectando del servidor...");
                    break;
                }

                manejarOpcionMenu(opcionMenu, lectorServidor, escritor, teclado);
            }

        } catch (IOException e) {
            System.out.println("Ocurrió un error en el cliente: " + e.getMessage());
        }
    }
    private static void mostrarMenu() {
        System.out.println("\n----- MENÚ PRINCIPAL -----");
        System.out.println("[1] Enviar un mensaje");
        System.out.println("[2] Leer mis mensajes");
        System.out.println("[3] Borrar un mensaje");
        System.out.println("[4] Borrar todos los mensajes");
        System.out.println("[5] Bloquear a un usuario");
        System.out.println("--- Gestión de Archivos ---");
        System.out.println("[6] Subir un archivo");
        System.out.println("[7] Solicitar ver archivos de otro usuario");
        System.out.println("[8] Solicitar descargar un archivo");
        System.out.println("[9] Revisar mis peticiones pendientes");
        System.out.println("[10] Descargar un archivo aprobado");
        System.out.println("[11] Salir");
        System.out.print("> ");
    }
    private static void manejarOpcionMenu(String opcionMenu, BufferedReader lectorServidor, PrintWriter escritor, BufferedReader teclado) throws IOException {
        switch (opcionMenu) {
            case "1": // ENVIAR MENSAJES
                System.out.println("Servidor: " + lectorServidor.readLine());
                String destinatario = teclado.readLine();
                escritor.println(destinatario);

                String respuestaDestinatario = lectorServidor.readLine();
                System.out.println("Servidor: " + respuestaDestinatario);

                if (!respuestaDestinatario.startsWith("Error:")) {
                    System.out.println("Escribe tu mensaje:");
                    String mensaje = teclado.readLine();
                    escritor.println(mensaje);
                    System.out.println("Servidor: " + lectorServidor.readLine());
                }
                break;

            case "2": // LEER MENSAJES
                String linea;
                while (!(linea = lectorServidor.readLine()).equals("FIN_MENSAJES")) {
                    System.out.println(linea);
                }
                break;

            case "3": // BORRAR MENSAJE
                String lineaInicial = lectorServidor.readLine();
                System.out.println(lineaInicial);

                if (!lineaInicial.contains("No tienes mensajes")) {
                    String lineaBorrar;
                    while (!(lineaBorrar = lectorServidor.readLine()).equals("FIN_LISTA_BORRAR")) {
                        System.out.println(lineaBorrar);
                    }
                    System.out.print("Elige el número del mensaje a borrar > ");
                    String seleccion = teclado.readLine();
                    escritor.println(seleccion);
                    System.out.println("Servidor: " + lectorServidor.readLine());
                }
                break;

            case "4": // BORRAR TODOS LOS MENSAJES
                System.out.println("Servidor: " + lectorServidor.readLine());
                System.out.print("> ");
                String confirmacion = teclado.readLine();
                escritor.println(confirmacion);
                System.out.println("Servidor: " + lectorServidor.readLine());
                break;

            case "5": // BLOQUEAR A UN USUARIO
                System.out.println("Servidor: " + lectorServidor.readLine());
                System.out.print("> ");
                String usuarioABloquear = teclado.readLine();
                escritor.println(usuarioABloquear);
                System.out.println("Servidor: " + lectorServidor.readLine());
                break;
            case "6": // SUBIR ARCHIVO
                System.out.println(lectorServidor.readLine());
                String nombreArchivo = teclado.readLine();
                escritor.println(nombreArchivo);

                System.out.println("Introduce la RUTA COMPLETA del archivo en tu PC:");
                String rutaLocal = teclado.readLine();
                File archivoLocal = new File(rutaLocal);

                if (archivoLocal.exists() && !archivoLocal.isDirectory()) {
                    System.out.println(lectorServidor.readLine());
                    Files.lines(Paths.get(rutaLocal)).forEach(escritor::println);
                    escritor.println();
                    System.out.println(lectorServidor.readLine());
                } else {
                    escritor.println("CANCELAR_SUBIDA");
                    System.out.println("Error: El archivo no existe en la ruta especificada.");
                }
                break;
            case "7": // SOLICITAR VER ARCHIVOS
                System.out.println(lectorServidor.readLine());
                String propietarioVer = teclado.readLine();
                escritor.println(propietarioVer);
                System.out.println(lectorServidor.readLine());
                break;
            case "8": // SOLICITAR DESCARGAR ARCHIVO
                System.out.println(lectorServidor.readLine());
                String propietarioDescarga = teclado.readLine();
                escritor.println(propietarioDescarga);
                System.out.println(lectorServidor.readLine());
                String archivoDescarga = teclado.readLine();
                escritor.println(archivoDescarga);
                System.out.println(lectorServidor.readLine());
                break;
            default:
                System.out.println("Servidor: " + lectorServidor.readLine());
                break;
        }
    }
}