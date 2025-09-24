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

            if (!respuestaAuth.contains("Autenticación exitosa") && !respuestaAuth.contains("registrado exitosamente")) {
                System.out.println("No se pudo iniciar sesión.");
                return;
            }

            while (true) {
                mostrarMenu();
                String opcionMenu = teclado.readLine();
                escritor.println(opcionMenu);

                if ("1".equals(opcionMenu)) { // ENVIAR MENSAJES
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
                } else if ("3".equals(opcionMenu)) { // BORRAR MENSAJE
                    String lineaInicial = lectorServidor.readLine();
                    System.out.println(lineaInicial);

                    if (lineaInicial.contains("No tienes mensajes")) {
                    } else {
                        String linea;
                        while (!(linea = lectorServidor.readLine()).equals("FIN_LISTA_BORRAR")) {
                            System.out.println(linea);
                        }
                        System.out.print("Elige el número del mensaje a borrar > ");
                        String seleccion = teclado.readLine();
                        escritor.println(seleccion);

                        String confirmacion = lectorServidor.readLine();
                        System.out.println("Servidor: " + confirmacion);
                    }

                }else if ("4".equals(opcionMenu)) { //BORRAR TODOS LOS MENSAJES ENVIADOS
                    System.out.println("Servidor: " + lectorServidor.readLine());
                    System.out.print("> ");
                    String confirmacion = teclado.readLine();
                    escritor.println(confirmacion);
                    System.out.println("Servidor: " + lectorServidor.readLine());

                } else if ("5".equals(opcionMenu)) { //BLOQUEAR A UN USUARIO
                    System.out.println("Servidor: " + lectorServidor.readLine());
                    System.out.println(">");
                    String usuarioABloquear = teclado.readLine();
                    escritor.println(usuarioABloquear);
                    System.out.println("Servidor: " + lectorServidor.readLine());
                } else if ("6".equals(opcionMenu)) { // SALIR
                    System.out.println("Desconectando del servidor...");
                    break;

                } else {
                    System.out.println("Servidor: " + lectorServidor.readLine());
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
        System.out.println("[4] Borrar todos los mensajes");
        System.out.println("[5] Bloquear a un usuario");
        System.out.println("[6] Salir");
        System.out.print("> ");
    }
}