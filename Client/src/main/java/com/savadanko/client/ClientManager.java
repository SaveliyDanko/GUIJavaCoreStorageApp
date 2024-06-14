package com.savadanko.client;

import com.savadanko.common.dto.CommandProperties;
import com.savadanko.common.dto.Request;
import com.savadanko.common.dto.Response;
import com.savadanko.common.models.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ClientManager {
    @Getter
    private Scene scene;
    private final LinkedHashMap<Long, Flat> flatMap;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final String login;
    private final ResourceBundle bundle;
    private TextArea messageArea; // Область для вывода сообщений
    private TableView<Flat> tableView;
    private Pane visualizationPane; // Область для визуализации объектов

    public ClientManager(
            Map<String, CommandProperties> commandMap,
            LinkedHashMap<Long, Flat> flatMap,
            ObjectOutputStream out,
            ObjectInputStream in,
            String login,
            ResourceBundle bundle) {
        this.flatMap = flatMap;
        this.out = out;
        this.in = in;
        this.login = login;
        this.bundle = bundle;
        createScene(commandMap);
        startSyncTimer();
    }

    private void createScene(Map<String, CommandProperties> commandMap) {
        ComboBox<String> comboBox = createComboBox(commandMap);
        tableView = createTableView();
        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);

        // Создание панели для визуализации
        visualizationPane = new Pane();
        visualizationPane.setPrefSize(400, 400);
        updateVisualizationPane();

        VBox vbox = new VBox(10, comboBox, tableView, messageArea, visualizationPane);
        scene = new Scene(vbox, 800, 600);
    }

    private ComboBox<String> createComboBox(Map<String, CommandProperties> commandMap) {
        ComboBox<String> comboBox = new ComboBox<>();
        for (Map.Entry<String, CommandProperties> entry : commandMap.entrySet()) {
            comboBox.getItems().add(entry.getKey());
        }
        comboBox.setOnAction(e -> {
            String selectedCommand = comboBox.getSelectionModel().getSelectedItem();
            if (selectedCommand != null) {
                int argsCount = commandMap.get(selectedCommand).getArgsCount();
                boolean isObject = commandMap.get(selectedCommand).isObject();
                handleCommand(selectedCommand, argsCount, isObject);
            }
        });
        return comboBox;
    }

    private void handleCommand(String commandName, int argsCount, boolean isObject) {
        if (argsCount == 0 && !isObject) {
            Request request = new Request(commandName, new String[0], null, login);
            requestSender(request);
        } else if (argsCount != 0 && !isObject) {
            handleArgsInput(false, commandName);
        } else if (argsCount == 0) {
            handleObjectInput(commandName, new String[0]);
        } else {
            handleArgsInput(true, commandName);
        }
    }

    private void requestSender(Request request){
        try {
            out.writeObject(request);
            out.flush();

            Response response = (Response) in.readObject();
            // Вывод сообщения из response в TextArea
            messageArea.appendText(response.getMessage() + "\n");

        } catch (IOException | ClassNotFoundException e) {
            messageArea.appendText("Error: " + e.getMessage() + "\n");
        }
    }

    private void handleArgsInput(boolean needsObject, String commandName) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("input_arguments"));
        dialog.setHeaderText(bundle.getString("enter_arguments"));
        dialog.setContentText(bundle.getString("arguments") + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(args -> {
            if (needsObject) {
                handleObjectInput(commandName, new String[]{result.get()});
            } else {
                Request request = new Request(commandName, new String[]{result.get()}, null, login);
                requestSender(request);
            }
        });
    }

    private void handleObjectInput(String commandName, String[] args) {
        Dialog<Flat> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("input_object"));
        dialog.setHeaderText(bundle.getString("enter_object_details"));

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        TextField nameField = new TextField();
        TextField xField = new TextField();
        TextField yField = new TextField();
        TextField areaField = new TextField();
        TextField roomsField = new TextField();
        TextField priceField = new TextField();
        ComboBox<View> viewComboBox = new ComboBox<>();
        viewComboBox.getItems().addAll(View.values());
        ComboBox<Transport> transportComboBox = new ComboBox<>();
        transportComboBox.getItems().addAll(Transport.values());
        TextField houseNameField = new TextField();
        TextField houseYearField = new TextField();
        TextField houseFloorsField = new TextField();
        TextField houseFlatsField = new TextField();
        TextField houseLiftsField = new TextField();

        grid.add(new Label(bundle.getString("name") + ":"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(bundle.getString("coordinate_x") + ":"), 0, 1);
        grid.add(xField, 1, 1);
        grid.add(new Label(bundle.getString("coordinate_y") + ":"), 0, 2);
        grid.add(yField, 1, 2);
        grid.add(new Label(bundle.getString("area") + ":"), 0, 3);
        grid.add(areaField, 1, 3);
        grid.add(new Label(bundle.getString("number_of_rooms") + ":"), 0, 4);
        grid.add(roomsField, 1, 4);
        grid.add(new Label(bundle.getString("price") + ":"), 0, 5);
        grid.add(priceField, 1, 5);
        grid.add(new Label(bundle.getString("view") + ":"), 0, 6);
        grid.add(viewComboBox, 1, 6);
        grid.add(new Label(bundle.getString("transport") + ":"), 0, 7);
        grid.add(transportComboBox, 1, 7);
        grid.add(new Label(bundle.getString("house_name") + ":"), 0, 8);
        grid.add(houseNameField, 1, 8);
        grid.add(new Label(bundle.getString("house_year") + ":"), 0, 9);
        grid.add(houseYearField, 1, 9);
        grid.add(new Label(bundle.getString("number_of_floors") + ":"), 0, 10);
        grid.add(houseFloorsField, 1, 10);
        grid.add(new Label(bundle.getString("flats_on_floor") + ":"), 0, 11);
        grid.add(houseFlatsField, 1, 11);
        grid.add(new Label(bundle.getString("number_of_lifts") + ":"), 0, 12);
        grid.add(houseLiftsField, 1, 12);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    Flat flat = new Flat();
                    flat.setName(validateString(nameField.getText(), bundle.getString("name")));
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(validateFloat(xField.getText(), bundle.getString("coordinate_x")));
                    coordinates.setY(validateLong(yField.getText(), bundle.getString("coordinate_y")));
                    flat.setCoordinates(coordinates);
                    flat.setArea(validateFloat(areaField.getText(), bundle.getString("area")));
                    flat.setNumberOfRooms(validateLong(roomsField.getText(), bundle.getString("number_of_rooms")));
                    flat.setPrice(validateFloat(priceField.getText(), bundle.getString("price")));
                    flat.setView(viewComboBox.getValue());
                    flat.setTransport(validateNotNull(transportComboBox.getValue(), bundle.getString("transport")));
                    House house = new House();
                    house.setName(houseNameField.getText());
                    house.setYear(validateLong(houseYearField.getText(), bundle.getString("house_year")));
                    house.setNumberOfFloors(validateLong(houseFloorsField.getText(), bundle.getString("number_of_floors")));
                    house.setNumberOfFlatsOnFloor(validateInt(houseFlatsField.getText(), bundle.getString("flats_on_floor")));
                    house.setNumberOfLifts(validateInt(houseLiftsField.getText(), bundle.getString("number_of_lifts")));
                    flat.setHouse(house);
                    flat.setOwner(login);

                    Request request = new Request(commandName, args, flat, login);
                    requestSender(request);
                    Platform.runLater(this::updateVisualizationPane); // Обновление визуализации

                } catch (IllegalArgumentException e) {
                    showErrorAlert(e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private String validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " " + bundle.getString("cannot_be_null_or_empty"));
        }
        return value;
    }

    private float validateFloat(String value, String fieldName) {
        try {
            float floatValue = Float.parseFloat(value);
            if (floatValue <= 0) {
                throw new IllegalArgumentException(fieldName + " " + bundle.getString("must_be_greater_than_0"));
            }
            return floatValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " " + bundle.getString("must_be_valid_number"));
        }
    }

    private long validateLong(String value, String fieldName) {
        try {
            long longValue = Long.parseLong(value);
            if (longValue <= 0) {
                throw new IllegalArgumentException(fieldName + " " + bundle.getString("must_be_greater_than_0"));
            }
            return longValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " " + bundle.getString("must_be_valid_number"));
        }
    }

    private int validateInt(String value, String fieldName) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue <= 0) {
                throw new IllegalArgumentException(fieldName + " " + bundle.getString("must_be_greater_than_0"));
            }
            return intValue;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " " + bundle.getString("must_be_valid_number"));
        }
    }

    private <T> T validateNotNull(T value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " " + bundle.getString("cannot_be_null"));
        }
        return value;
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(bundle.getString("validation_error"));
        alert.setHeaderText(bundle.getString("invalid_input"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    private TableView<Flat> createTableView() {
        ObservableList<Flat> flatList = FXCollections.observableArrayList(flatMap.values());
        TableView<Flat> tableView = new TableView<>();

        // Добавление столбцов в TableView
        tableView.getColumns().addAll(
                createColumn(bundle.getString("id"), "id"),
                createColumn(bundle.getString("name"), "name"),
                createCustomColumn(bundle.getString("coordinate_x"), flat -> new SimpleFloatProperty(flat.getCoordinates().getX()).asObject()),
                createCustomColumn(bundle.getString("coordinate_y"), flat -> new SimpleLongProperty(flat.getCoordinates().getY()).asObject()),
                createTimestampColumn(),
                createCustomColumn(bundle.getString("area"), flat -> new SimpleFloatProperty(flat.getArea()).asObject()),
                createCustomColumn(bundle.getString("number_of_rooms"), flat -> new SimpleLongProperty(flat.getNumberOfRooms()).asObject()),
                createCustomColumn(bundle.getString("price"), flat -> new SimpleFloatProperty(flat.getPrice()).asObject()),
                createColumn(bundle.getString("view"), "view"),
                createColumn(bundle.getString("transport"), "transport"),
                createCustomColumn(bundle.getString("house_name"), flat -> new SimpleStringProperty(flat.getHouse().getName())),
                createCustomColumn(bundle.getString("house_year"), flat -> new SimpleLongProperty(flat.getHouse().getYear()).asObject()),
                createCustomColumn(bundle.getString("number_of_floors"), flat -> new SimpleLongProperty(flat.getHouse().getNumberOfFloors()).asObject()),
                createCustomColumn(bundle.getString("flats_on_floor"), flat -> new SimpleIntegerProperty(flat.getHouse().getNumberOfFlatsOnFloor()).asObject()),
                createCustomColumn(bundle.getString("number_of_lifts"), flat -> new SimpleIntegerProperty(flat.getHouse().getNumberOfLifts()).asObject()),
                createColumn(bundle.getString("owner"), "owner")
        );

        // Добавление контекстного меню
        tableView.setRowFactory(tv -> {
            TableRow<Flat> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                    Flat rowData = row.getItem();
                    ContextMenu contextMenu = new ContextMenu();

                    MenuItem deleteItem = new MenuItem(bundle.getString("delete"));
                    deleteItem.setOnAction(e -> {
                        Request request = new Request("remove_key", new String[]{Long.toString(rowData.getId())}, null, login);
                        requestSender(request);
                    });

                    MenuItem updateItem = new MenuItem(bundle.getString("update"));
                    updateItem.setOnAction(e -> {
                        handleObjectInput("update", new String[]{Long.toString(rowData.getId())});
                    });

                    contextMenu.getItems().addAll(deleteItem, updateItem);
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });

        // Установка данных в TableView
        tableView.setItems(flatList);

        return tableView;
    }

    private <T> TableColumn<Flat, T> createColumn(String title, String property) {
        TableColumn<Flat, T> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private <T> TableColumn<Flat, T> createCustomColumn(String title, CustomValueFactory<Flat, T> valueFactory) {
        TableColumn<Flat, T> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> valueFactory.apply(cellData.getValue()));
        return column;
    }

    private TableColumn<Flat, String> createTimestampColumn() {
        TableColumn<Flat, String> column = new TableColumn<>(bundle.getString("timestamp"));
        column.setCellValueFactory(cellData -> {
            ZonedDateTime timestamp = cellData.getValue().getCreationDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
            return new SimpleStringProperty(timestamp.format(formatter));
        });
        return column;
    }

    @FunctionalInterface
    private interface CustomValueFactory<S, T> {
        javafx.beans.value.ObservableValue<T> apply(S source);
    }

    private void startSyncTimer() {
        Timer syncTimer = new Timer(true);
        syncTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                syncWithDatabase();
            }
        }, 0, 5000);
    }

    private void syncWithDatabase() {
        Request request = new Request("sync", new String[0], null, login);
        try {
            out.writeObject(request);
            out.flush();
            Response response = (Response) in.readObject();
            if (response.getFlatMap() != null) {
                Platform.runLater(() -> updateData(convertMap(response.getFlatMap())));
            }
        } catch (IOException | ClassNotFoundException e) {
            Platform.runLater(() -> messageArea.appendText("Error: " + e.getMessage() + "\n"));
        }
    }

    private void updateData(LinkedHashMap<Long, Flat> newData) {
        flatMap.clear();
        flatMap.putAll(newData);
        updateTableView();
        updateVisualizationPane(); // Обновление визуализации
    }

    private void updateTableView() {
        ObservableList<Flat> flatList = FXCollections.observableArrayList(flatMap.values());
        tableView.setItems(flatList);
    }

    private LinkedHashMap<Long, Flat> convertMap(LinkedHashMap<Long, Object> inputMap) {
        LinkedHashMap<Long, Flat> resultMap = new LinkedHashMap<>();

        for (Map.Entry<Long, Object> entry : inputMap.entrySet()) {
            Long key = entry.getKey();
            Object value = entry.getValue();

            try {
                Flat flatValue = (Flat) value;
                resultMap.put(key, flatValue);
            } catch (Exception e) {
                System.err.println("Failed to convert value to Flat for key: " + key);
                System.out.println(e.getMessage());
            }
        }
        return resultMap;
    }

    private void updateVisualizationPane() {
        visualizationPane.getChildren().clear();

        for (Flat flat : flatMap.values()) {
            // Размер прямоугольника в зависимости от цены
            double width = Math.max(50, flat.getPrice() / 10); // Минимальная ширина 50, масштабируемая по цене
            double height = Math.max(30, flat.getPrice() / 20); // Минимальная высота 30, масштабируемая по цене

            // Цвет прямоугольника в зависимости от владельца
            Color color = login.equals(flat.getOwner()) ? Color.BLUE : Color.GREEN;

            Rectangle rect = new Rectangle(width, height, color);
            rect.setX(Math.random() * (visualizationPane.getWidth() - width));
            rect.setY(Math.random() * (visualizationPane.getHeight() - height));

            rect.setOnMouseClicked(event -> {
                tableView.getSelectionModel().select(flat);
                rect.setStroke(Color.RED); // Подсветка прямоугольника
                rect.setStrokeWidth(3);
            });

            visualizationPane.getChildren().add(rect);
        }
    }
}
