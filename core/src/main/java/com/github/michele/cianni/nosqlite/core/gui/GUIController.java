package com.github.michele.cianni.nosqlite.core.gui;

import com.github.michele.cianni.nosqlite.core.command.CommandResult;
import com.github.michele.cianni.nosqlite.core.factory.NoSQLiteDatabaseFactory;
import com.github.michele.cianni.nosqlite.core.models.Entry;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Stream;

public final class GUIController {

    @FXML
    private TextField keyField;

    @FXML
    private TextArea valueArea;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<Entry> resultTable;

    private final GUICommandProcessor processor;

    public GUIController() {
        NoSQLiteDatabaseFactory factory = new NoSQLiteDatabaseFactory();
        this.processor = new GUICommandProcessor(factory.createDatabase("database.json"));
    }

    @FXML
    public void initialize() {
        resultTable.columnResizePolicyProperty();

        TableColumn<Entry, String> keyColumn = new TableColumn<>("Key");
        keyColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().key())
        );
        resultTable.getColumns().add(keyColumn);

        TableColumn<Entry, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().value().toString())
        );
        resultTable.getColumns().add(valueColumn);

        updateTable();
    }

    @FXML
    public void handleGetAction() {
        String key = keyField.getText().trim();
        String commandLine = "get " + key;

        CommandResult commandResult = processor.process(commandLine);
        statusLabel.setText(commandResult.message());

        clearInputFields();
        updateTable();
    }

    @FXML
    public void handleInsertAction() {
        String key = keyField.getText().trim();
        String value = valueArea.getText().trim();
        String commandLine = "insert " + key + " " + value;

        CommandResult commandResult = processor.process(commandLine);
        statusLabel.setText(commandResult.message());

        clearInputFields();
        updateTable();
    }

    @FXML
    public void handleDeleteAction() {
        String key = keyField.getText().trim();
        String commandLine = "delete " + key;

        CommandResult commandResult = processor.process(commandLine);
        statusLabel.setText(commandResult.message());

        clearInputFields();
        updateTable();
    }

    @FXML
    public void handleBeginAction() {
        String commandLine = "begin";

        CommandResult commandResult = processor.process(commandLine);
        statusLabel.setText(commandResult.message());

        clearInputFields();
        updateTable();
    }

    @FXML
    public void handleCommitAction() {
        String commandLine = "commit";

        CommandResult commandResult = processor.process(commandLine);
        statusLabel.setText(commandResult.message());

        clearInputFields();
        updateTable();
    }

    @FXML
    public void handleRollbackAction() {
        String commandLine = "rollback";

        CommandResult commandResult = processor.process(commandLine);
        statusLabel.setText(commandResult.message());

        clearInputFields();
        updateTable();
    }

    private void clearInputFields() {
        keyField.clear();
        valueArea.clear();
    }

    private void updateTable() {
        resultTable.getItems().clear();
        resultTable.getItems().addAll(getAllValues());
    }

    private List<Entry> getAllValues() {
        return Stream.of(processor.process("getAll").values())
                .map(value -> (Entry) value)
                .toList();
    }

}
