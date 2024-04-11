package br.com.catolica.projetosd;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Application {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 12346;
    private static final String UPLOAD_DIRECTORY = "C:\\Users\\heito\\IdeaProjects\\ProjetoSd\\src\\main\\java\\br\\com\\catolica\\projetosd\\images";

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private TextArea logTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Client");

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Button uploadButton = new Button("Upload");
        uploadButton.setOnAction(e -> uploadFile());

        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(e -> downloadFile());

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteFile());

        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setPrefHeight(200);

        root.getChildren().addAll(uploadButton, downloadButton, deleteButton, logTextArea);

        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.show();

        // Conecta ao servidor
        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_ADDRESS, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            log("Conectado ao servidor.");
        } catch (IOException e) {
            log("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }

    private void updateFilesList() {
        Platform.runLater(() -> {
            try {
                out.writeObject("LIST_FILES");
                List<String> filesList = (List<String>) in.readObject();

                // Atualiza a lista de arquivos disponíveis na interface gráfica do usuário
                // (Você precisa implementar isso de acordo com a interface gráfica que está usando)
            } catch (IOException | ClassNotFoundException e) {
                log("Erro ao atualizar lista de arquivos disponíveis: " + e.getMessage());
            }
        });
    }

    private void uploadFile() {
        Platform.runLater(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Escolha o arquivo para upload");
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                try (FileInputStream fis = new FileInputStream(selectedFile)) {
                    out.writeObject("UPLOAD");
                    out.writeObject(selectedFile.getName());

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }

                    log("Arquivo enviado com sucesso: " + selectedFile.getAbsolutePath());
                    updateFilesList(); // Atualiza a lista de arquivos disponíveis
                } catch (IOException e) {
                    log("Erro ao enviar arquivo: " + e.getMessage());
                }
            } else {
                log("Upload cancelado pelo usuário.");
            }
        });
    }



    private void downloadFile() {
        Platform.runLater(() -> {
            try {
                out.writeObject("LIST_FILES");
                List<String> filesList = (List<String>) in.readObject();

                ChoiceDialog<String> dialog = new ChoiceDialog<>("", filesList);
                dialog.setTitle("Escolha o arquivo para download");
                dialog.setHeaderText("Arquivos disponíveis para download");
                dialog.setContentText("Escolha o arquivo:");

                // Mostra o diálogo e aguarda a escolha do usuário
                dialog.showAndWait().ifPresent(selectedFile -> {
                    try {
                        out.writeObject("DOWNLOAD");
                        out.writeObject(selectedFile);

                        boolean fileExists = (boolean) in.readObject();
                        if (fileExists) {
                            List<String> fileContent = (List<String>) in.readObject();
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Salvar arquivo");
                            fileChooser.setInitialFileName(selectedFile);
                            File selectedFileLocation = fileChooser.showSaveDialog(null);

                            if (selectedFileLocation != null) {
                                try (PrintWriter writer = new PrintWriter(selectedFileLocation)) {
                                    for (String line : fileContent) {
                                        writer.println(line);
                                    }
                                    log("Arquivo baixado com sucesso: " + selectedFileLocation.getAbsolutePath());
                                } catch (IOException e) {
                                    log("Erro ao salvar arquivo: " + e.getMessage());
                                }
                            } else {
                                log("Download cancelado pelo usuário.");
                            }
                        } else {
                            log("O arquivo selecionado não existe no servidor.");
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        log("Erro ao baixar arquivo: " + e.getMessage());
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                log("Erro ao listar arquivos disponíveis para download: " + e.getMessage());
            }
        });
    }



    private void deleteFile() {
        Platform.runLater(() -> {
            try {
                out.writeObject("LIST_FILES");
                List<String> filesList = (List<String>) in.readObject();

                ChoiceDialog<String> dialog = new ChoiceDialog<>("", filesList);
                dialog.setTitle("Escolha o arquivo para deletar");
                dialog.setHeaderText("Arquivos disponíveis para deletar");
                dialog.setContentText("Escolha o arquivo:");

                // Mostra o diálogo e aguarda a escolha do usuário
                dialog.showAndWait().ifPresent(selectedFile -> {
                    try {
                        out.writeObject("DELETE");
                        out.writeObject(selectedFile);

                        updateFilesList(); // Atualiza a lista de arquivos disponíveis após a exclusão
                    } catch (IOException e) {
                        log("Erro ao deletar arquivo: " + e.getMessage());
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                log("Erro ao listar arquivos disponíveis para deletar: " + e.getMessage());
            }
        });
    }


    private void log(String message) {
        Platform.runLater(() -> logTextArea.appendText(message + "\n"));
    }
}
