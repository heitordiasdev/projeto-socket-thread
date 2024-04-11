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


}
