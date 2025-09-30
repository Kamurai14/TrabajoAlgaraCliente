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