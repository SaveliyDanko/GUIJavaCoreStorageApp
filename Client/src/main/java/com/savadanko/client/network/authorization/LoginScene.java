package com.savadanko.client.network.authorization;

import com.savadanko.client.ClientManager;
import com.savadanko.common.dto.AuthDTO;
import com.savadanko.common.dto.AuthResponse;
import com.savadanko.common.dto.CommandProperties;
import com.savadanko.common.models.Flat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Getter
public class LoginScene {
    private final Stage stage;
    private final Scene portScene;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final ResourceBundle bundle;

    @Getter
    private Scene scene;

    public LoginScene(Stage stage, Scene previousScene, ObjectOutputStream out, ObjectInputStream in, ResourceBundle bundle) {
        this.stage = stage;
        this.portScene = previousScene;
        this.out = out;
        this.in = in;
        this.bundle = bundle;
        createScene();
    }

    private void createScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(bundle.getString("username"));
        nameLabel.getStyleClass().add("label");
        GridPane.setConstraints(nameLabel, 0, 0);

        TextField loginInput = new TextField();
        loginInput.getStyleClass().add("textField");
        GridPane.setConstraints(loginInput, 1, 0);

        Label passLabel = new Label(bundle.getString("password"));
        passLabel.getStyleClass().add("label");
        GridPane.setConstraints(passLabel, 0, 1);

        PasswordField passInput = new PasswordField();
        passInput.getStyleClass().add("textField");
        GridPane.setConstraints(passInput, 1, 1);

        Button loginButton = new Button(bundle.getString("login"));
        loginButton.getStyleClass().add("button");
        GridPane.setConstraints(loginButton, 1, 2);

        loginButton.setOnAction(actionEvent -> {
            String login = loginInput.getText();
            String password = passInput.getText();

            try {
                byte[] hashedPassword = PasswordHasher.hashPassword(password, null);
                AuthDTO authDTO = new AuthDTO(login, hashedPassword, null);
                sendAuthRequest(authDTO);
                AuthResponse authResponse = receiveAuthResponse();

                Map<String, CommandProperties> commandMap = authResponse.getCommandPropertiesMap();
                LinkedHashMap<Long, Flat> flatMap = convertToFlatMap(authResponse.getFlatMap());
                ClientManager clientManager = new ClientManager(commandMap, flatMap, out, in, login, bundle, portScene, stage);
                stage.setTitle(login);
                stage.setScene(clientManager.getScene());
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException e) {
                stage.setScene(portScene);
            }
        });

        grid.getChildren().addAll(nameLabel, loginInput, passLabel, passInput, loginButton);

        // Create the background image
        Image backgroundImage = new Image("/animeBoy.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setOpacity(0.5);
        backgroundImageView.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());
        backgroundImageView.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight());
        backgroundImageView.setPreserveRatio(false);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, grid);

        scene = new Scene(stackPane, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
    }


    private void sendAuthRequest(AuthDTO authDTO) throws IOException {
        out.writeObject(authDTO);
        out.flush();
    }

    private AuthResponse receiveAuthResponse() throws IOException, ClassNotFoundException {
        return (AuthResponse) in.readObject();
    }

    private LinkedHashMap<Long, Flat> convertToFlatMap(LinkedHashMap<Long, Object> originalMap) {
        return originalMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof Flat)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (Flat) entry.getValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
