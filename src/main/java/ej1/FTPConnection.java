
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class FTPConnection {

    private static final String SERVER = "localhost";
    private static final int PORT = 21;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FTPClient ftpClient = new FTPClient();
        String username;
        String password;

        try {
            ftpClient.connect(SERVER, PORT);

            do {
                System.out.println("Ingrese el nombre de usuario (ingrese * para salir): ");
                username = scanner.nextLine();
                if (!username.equals("*")) {

                    System.out.println("Ingrese la contraseña: ");
                    password = scanner.nextLine();

                    if (login(ftpClient, username, password)) {
                        logConnection(username, password);
                        System.out.println("Conexión exitosa.");
                    } else {
                        System.out.println("Nombre de usuario o contraseña incorrectos.");
                    }

                }else{
                    System.out.println("Saliendo...");
                }

            } while (!username.equals("*"));

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                ftpClient.disconnect();

            } catch (IOException e) {

                e.printStackTrace();

            }
            scanner.close();

        }
    }

    private static boolean login(FTPClient ftpClient, String username, String password) throws IOException {
        return ftpClient.login(username, password);
    }

    private static void logConnection(String username, String password) {
        FTPClient ftpClient = new FTPClient();
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            ftpClient.connect(SERVER, PORT);
            ftpClient.login(username, password);

            ftpClient.changeWorkingDirectory("/" + username + "/LOG");

            java.util.Date hora = new java.util.Date(System.currentTimeMillis());
            String logMessage = "Hora de conexión: " + hora + "\n";

            outputStream = ftpClient.storeFileStream("LOG.TXT");
            outputStream.write(logMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
