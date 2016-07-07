package rs.net;

import rs.GameShell;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Jaggrab {

    protected static GameShell shell;
    public static Socket socket;

    public static DataInputStream request(String s) throws IOException {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception _ex) {
            }
            socket = null;
        }
        socket = Jaggrab.shell.get_socket(43595);
        socket.setSoTimeout(10000);
        socket.getOutputStream().write(("JAGGRAB /" + s + "\n\n").getBytes());

        return new DataInputStream(socket.getInputStream());
    }

    public static void set_shell(GameShell shell) {
        Jaggrab.shell = shell;
    }

}
