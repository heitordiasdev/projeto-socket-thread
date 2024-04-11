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

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                String command;
                while ((command = (String) in.readObject()) != null) {
                    switch (command) {
                        case "UPLOAD":
                            handleUpload();
                            break;
                        case "DOWNLOAD":
                            handleDownload();
                            break;
                        case "DELETE":
                            handleDelete();
                            break;
                        case "LIST_FILES":
                            sendFilesList();
                            break;
                        default:
                            System.out.println("Comando inválido: " + command);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void handleUpload() throws IOException, ClassNotFoundException {
            String fileName = (String) in.readObject();
            File file = new File(UPLOAD_DIRECTORY, fileName);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            System.out.println("Arquivo recebido: " + fileName);
            sendFilesList();
        }

        private void handleDownload() throws IOException, ClassNotFoundException {
            String fileName = (String) in.readObject();
            File file = new File(UPLOAD_DIRECTORY, fileName);

            boolean fileExists = file.exists() && file.isFile();
            out.writeObject(fileExists);

            if (fileExists) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    List<String> fileContent = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.add(line);
                    }
                    out.writeObject(fileContent);
                }
            }
        }

        private void handleDelete() throws IOException, ClassNotFoundException {
            String fileName = (String) in.readObject();
            File file = new File(UPLOAD_DIRECTORY, fileName);

            boolean fileExists = file.exists() && file.isFile();
            out.writeObject(fileExists);

            if (fileExists) {
                executor.execute(() -> {
                    boolean fileDeleted = file.delete();
                    if (fileDeleted) {
                        System.out.println("Arquivo deletado: " + fileName);
                        try {
                            sendFilesList();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Falha ao deletar o arquivo: " + fileName);
                    }
                });
            }
        }

        private void sendFilesList() throws IOException {
            File directory = new File(UPLOAD_DIRECTORY);
            File[] files = directory.listFiles();

            List<String> filesList = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        filesList.add(file.getName());
                    }
                }
            }

            out.writeObject(filesList);
        }
    }
}
