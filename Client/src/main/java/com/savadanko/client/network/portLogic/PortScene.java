package com.savadanko.client.network.portLogic;

import com.savadanko.client.network.authorization.LoginScene;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class PortScene {
    private final Stage stage;
    @Getter
    private Scene scene;
    private ResourceBundle bundle;
    private Label label1;
    private Label warningPort;
    private Label warningConnection;
    private TextField textField;
    private Button button;

    public PortScene(Stage stage) {
        this.stage = stage;
        loadLocale(new Locale("en", "US")); // Default locale
        createScene();
    }

    private void createScene() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        ComboBox<String> localeSelector = createLocaleSelector();

        label1 = new Label(bundle.getString("enter_port"));
        label1.getStyleClass().add("label");
        warningPort = new Label();
        warningPort.getStyleClass().add("label");
        warningConnection = new Label();
        warningConnection.getStyleClass().add("label");

        textField = new TextField();
        textField.getStyleClass().add("textField");
        textField.setPromptText(bundle.getString("enter_port"));

        button = getButton();

        vBox.getChildren().addAll(localeSelector, label1, textField, button, warningPort, warningConnection);

        Image backgroundImage = new Image("/animeBoy.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setOpacity(0.5);
        backgroundImageView.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth());
        backgroundImageView.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight());
        backgroundImageView.setPreserveRatio(false);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImageView, vBox);

        scene = new Scene(stackPane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth());
        stage.setHeight(primaryScreenBounds.getHeight());

        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setMaximized(true);
        stage.show();
    }

    private ComboBox<String> createLocaleSelector() {
        ComboBox<String> localeSelector = new ComboBox<>();
        localeSelector.getItems().addAll("English", "Русский", "Polski", "Slovenský");
        localeSelector.setValue("English");
        localeSelector.getStyleClass().add("comboBox"); // Добавляем CSS-класс

        localeSelector.setOnAction(event -> {
            String selectedLocale = localeSelector.getValue();
            switch (selectedLocale) {
                case "English":
                    loadLocale(new Locale("en", "US"));
                    break;
                case "Русский":
                    loadLocale(new Locale("ru", "RU"));
                    break;
                case "Polski":
                    loadLocale(new Locale("pl", "PL"));
                    break;
                case "Slovenský":
                    loadLocale(new Locale("sk", "SK"));
                    break;
            }
            updateTexts();
        });

        return localeSelector;
    }


    private void loadLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("locale", locale, new UTF8Control());
    }

    private void updateTexts() {
        label1.setText(bundle.getString("enter_port"));
        textField.setPromptText(bundle.getString("enter_port"));
        button.setText(bundle.getString("connect"));
        warningPort.setText("");
        warningConnection.setText("");
    }

    private Button getButton() {
        Button button = new Button(bundle.getString("connect"));

        button.setOnAction(actionEvent -> {
            String port = textField.getText();
            if (isNumeric(port)) {
                warningPort.setText(bundle.getString("valid_port") + port);
                warningPort.setStyle("-fx-text-fill: green;");
                try {
                    Socket socket = new Socket("localhost", Integer.parseInt(port));
                    socket.setSoTimeout(5000); // Тайм-аут 5 секунд
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    LoginScene loginScene = new LoginScene(stage, scene, out, in, bundle);

                    stage.setTitle("Login");
                    stage.setScene(loginScene.getScene());
                } catch (IOException e) {
                    warningConnection.setText(bundle.getString("invalid_connection"));
                    warningConnection.setStyle("-fx-text-fill: red;");
                }
            } else {
                warningPort.setText(bundle.getString("invalid_input"));
                warningPort.setStyle("-fx-text-fill: red;");
            }
        });
        return button;
    }


    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static class UTF8Control extends ResourceBundle.Control {
        @Override
        public ResourceBundle newBundle(
                String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle;
            try (InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(loader.getResourceAsStream(resourceName)), StandardCharsets.UTF_8)) {
                bundle = new PropertyResourceBundle(reader);
            }
            return bundle;
        }
    }
}