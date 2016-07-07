package rs;

import rs.cache.Cache;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Signlink implements Runnable {

    public static boolean active;
    public static Cache.AccessFile cache_file = null;
    public static Cache.AccessFile cache_index[] = new Cache.AccessFile[5];
    public static String dns = null;
    public static String dns_next = null;
    public static boolean error = true;
    public static String error_name = "";
    public static final int REVISION = 317;
    public static Socket socket = null;
    public static InetAddress socket_address;
    public static int socket_port;
    public static Runnable thread = null;
    public static int thread_index;
    public static int thread_priority = 1;
    public static int uid;

    public static void error(String s) {
        if (!error) {
            return;
        }
        if (!active) {
            return;
        }
        System.out.println("Error: " + s);
    }

    public static File get_cache_folder() {
        return new File(get_cache_path());
    }

    public static String get_cache_path() {
        String s = System.getProperty("rt317.cache");
        if (s != null) {
            if (s.charAt(s.length() - 1) != '/') {
                s = s + '/';
            }
            return s;
        }
        return System.getProperty("user.home") + System.getProperty("file.separator") + ".rs317/";
    }

    public static synchronized void get_dns(String s) {
        dns = s;
        dns_next = s;
    }

    public static File get_file(String s) {
        return new File(get_cache_folder(), s);
    }

    public static synchronized Socket get_socket(int port) throws IOException {
        for (socket_port = port; socket_port != 0; ) {
            try {
                Thread.sleep(50L);
            } catch (Exception _ex) {
            }
        }

        if (socket == null) {
            throw new IOException("Error opening socket.");
        } else {
            return socket;
        }
    }

    public static int get_uid(String s) {
        try {
            File file = new File(s + "uid.dat");
            if (!file.exists() || file.length() < 4L) {
                DataOutputStream out = new DataOutputStream(new FileOutputStream(s + "uid.dat"));
                out.writeInt((int) (Math.random() * 99999999D));
                out.close();
            }
        } catch (Exception _ex) {
        }
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(s + "uid.dat"));
            int i = in.readInt();
            in.close();
            return i + 1;
        } catch (Exception _ex) {
            return 0;
        }
    }

    public static void start(InetAddress inetaddress) {
        thread_index = (int) (Math.random() * 99999999D);

        if (active) {
            try {
                Thread.sleep(500L);
            } catch (Exception _ex) {
            }
            active = false;
        }

        socket_port = 0;
        thread = null;
        dns_next = null;
        socket_address = inetaddress;

        Thread t = new Thread(new Signlink());
        t.setDaemon(true);
        t.start();

        while (!active) {
            try {
                Thread.sleep(50L);
            } catch (Exception _ex) {
            }
        }
    }

    public static synchronized void start_thread(Runnable runnable, int i) {
        thread_priority = i;
        thread = runnable;
    }

    public void run() {
        active = true;
        String path = get_cache_path();
        uid = get_uid(path);
        new File(path).mkdirs();

        try {
            cache_file = new Cache.AccessFile(path + "main_file_cache.dat");

            for (int j = 0; j < 5; j++) {
                cache_index[j] = new Cache.AccessFile(path + "main_file_cache.idx" + j);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = thread_index; thread_index == i; ) {
            if (socket_port != 0) {
                try {
                    socket = new Socket(socket_address, socket_port);
                } catch (Exception _ex) {
                    socket = null;
                }
                socket_port = 0;
            } else if (thread != null) {
                Thread t = new Thread(thread);
                t.setDaemon(true);
                t.start();
                t.setPriority(thread_priority);
                thread = null;
            } else if (dns_next != null) {
                try {
                    dns = InetAddress.getByName(dns_next).getHostName();
                } catch (Exception _ex) {
                    dns = "unknown";
                }
                dns_next = null;
            }
            try {
                Thread.sleep(50L);
            } catch (Exception _ex) {
            }
        }

    }

}
