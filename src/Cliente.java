import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String [] args) throws IOException {
        Socket salida = new Socket("localHost", 8080);
        PrintWriter escritor = new PrintWriter(salida.getOutputStream(), true);
        BufferedReader lector = new BufferedReader(new InputStreamReader(salida.getInputStream()));
        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

        String cadena = teclado.readLine();
        String mensaje;
        while (!cadena.equalsIgnoreCase("FIN")){
            escritor.println(cadena);
            mensaje = lector.readLine();
            System.out.println(mensaje);
            if(mensaje.equalsIgnoreCase("FIN")){
                break;
            }
            cadena = teclado.readLine();
        }
        salida.close();
    }
}