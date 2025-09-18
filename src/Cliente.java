import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try (
                Socket socket = new Socket("localhost", 8080);
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader lectorServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
        ) {

            System.out.println("Servidor: " + lectorServidor.readLine()); // Mensaje de bienvenida
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

            // Si la autenticación falla, el programa termina
            if (!respuestaAuth.contains("Autenticación exitosa") && !respuestaAuth.contains("registrado exitosamente")) {
                System.out.println("No se pudo iniciar sesión.");
                return;
            }

            while (true) {
                mostrarMenu();
                String opcionMenu = teclado.readLine();
                escritor.println(opcionMenu);

                if ("1".equals(opcionMenu)) { //ENVIAR MENSAJES
                    System.out.println("Servidor: " + lectorServidor.readLine());
                    String destinatario = teclado.readLine();
                    escritor.println(destinatario);

                    String respuestaDestinatario = lectorServidor.readLine();
                    System.out.println("Servidor: " + respuestaDestinatario);

                    if (!respuestaDestinatario.startsWith("Error:")) {
                        String mensaje = teclado.readLine();
                        escritor.println(mensaje);

                        System.out.println("Servidor: " + lectorServidor.readLine());
                    }

                } else if ("2".equals(opcionMenu)) { // LEER MENSAJES
                    String linea;
                    while (!(linea = lectorServidor.readLine()).equals("FIN_MENSAJES")) {
                        System.out.println(linea);
                    }
                }else if ("3".equals(opcionMenu)) { // BORRAR MENSAJE
                    String linea;
                    while (!(linea = lectorServidor.readLine()).equals("FIN_LISTA_BORRAR")) {
                        System.out.println(linea);
                    }
                    String primeraRespuesta = lectorServidor.readLine();
                    if (primeraRespuesta.equals("No tienes mensajes para borrar.")){
                        System.out.println("Servidor: " + primeraRespuesta);
                    } else {
                        System.out.println("Servidor: " + primeraRespuesta);
                        System.out.print("> ");
                        String seleccion = teclado.readLine();
                        escritor.println(seleccion);
                        System.out.println("Servidor: " + lectorServidor.readLine());
                    }

                }else if ("4".equals(opcionMenu)) { // SALIR
                    System.out.println("Desconectando del servidor...");
                    break;

                } else {
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }

        } catch (IOException e) {
            System.out.println("Ocurrió un error en el cliente: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n----- MENÚ PRINCIPAL -----");
        System.out.println("Elige una opción:");
        System.out.println("[1] Enviar un mensaje a otro usuario");
        System.out.println("[2] Leer mis mensajes");
        System.out.println("[3] Borrar un mensaje");
        System.out.println("[4] Salir");
        System.out.print("> ");
    }

}