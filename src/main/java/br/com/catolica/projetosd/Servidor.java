package br.com.catolica.projetosd;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private static final String UPLOAD_DIRECTORY = "C:\\Users\\heito\\IdeaProjects\\ProjetoSd\\src\\main\\java\\br\\com\\catolica\\projetosd\\images";
    private static final int PORT = 12346;
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor esperando por conexões...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket);

                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

    }
}
