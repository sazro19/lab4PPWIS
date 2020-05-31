package main.view;

import main.model.Record;
import main.utils.RecordTableFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableContainer {
    private final List<TableColumn<Record, ?>> deletedColumns;

    private final VBox topContainer;
    private final TableView<Record> table;
    private final HBox buttonContainer;
    private final Button asTable;
    private final Button asList;
    private final List<Record> records;

    public TableContainer(List<Record> records) {
        this.records = records;
        this.deletedColumns = new ArrayList<>();
        this.topContainer = new VBox();
        this.buttonContainer = new HBox();

        this.table = RecordTableFactory.getFullTable();
        this.table.getItems().addAll(records);

        this.asTable = new Button("Table view");
        this.asList = new Button("List view");
        configureContainers();
    }

    private void configureContainers() {
        configureButtons();
        this.buttonContainer.getChildren().addAll(asTable, asList);
        this.topContainer.getChildren().addAll(table, buttonContainer);
    }

    private void configureButtons() {
        this.asList.setOnAction(e -> {
            this.deletedColumns.clear();
            this.deletedColumns.addAll(RecordTableFactory.removeUnnecessaryColumns(table));
        });
        this.asTable.setOnAction(e -> {
            this.table.getColumns().addAll(deletedColumns);
            this.deletedColumns.clear();
        });
    }

    public VBox getTopContainer() {
        return topContainer;
    }

    public void updateTable() {
        this.table.getItems().clear();
        this.table.getItems().addAll(this.records);
    }

    public Optional<Record> getSelectedItem() {
        return Optional.ofNullable(this.table.
                getSelectionModel().
                getSelectedItem());
    }
}
