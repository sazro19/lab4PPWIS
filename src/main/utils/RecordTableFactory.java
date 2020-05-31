package main.utils;

import main.model.Record;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class RecordTableFactory {

    public static TableView<Record> getFullTable() {
        TableView<Record> table = new TableView<>();
        TableColumn<Record, ImageView> iconColumn = new TableColumn<>(TableColumnNames.ICON.getName());
        iconColumn.setResizable(false);
        iconColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getType().getImageView()));
        TableColumn<Record, String> nameColumn = new TableColumn<>(TableColumnNames.NAME.getName());
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        TableColumn<Record, String> dateColumn = new TableColumn<>(TableColumnNames.DATE.getName());
        dateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(new SimpleDateFormat("dd-MM-yyyy").format(cell.getValue().getModified())));
        TableColumn<Record, String> extensionColumn = new TableColumn<>(TableColumnNames.EXTENSION.getName());
        extensionColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getExtension()));
        TableColumn<Record, String> sizeColumn = new TableColumn<>(TableColumnNames.SIZE.getName());
        sizeColumn.setCellValueFactory(cell -> {
            if (cell.getValue().getSize() != Long.MIN_VALUE) {
                return new SimpleStringProperty(Long.toString(cell.getValue().getSize()));
            }
            return new SimpleStringProperty("");
        });
        table.getColumns().addAll(iconColumn, nameColumn, dateColumn, extensionColumn, sizeColumn);
        return table;
    }

    public static TableView<Record> getListTable() {
        TableView<Record> table = new TableView<>();
        TableColumn<Record, ImageView> icon = new TableColumn<>(TableColumnNames.ICON.getName());
        icon.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getType().getImageView()));
        TableColumn<Record, String> nameColumn = new TableColumn<>(TableColumnNames.NAME.getName());
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        table.getColumns().addAll(icon, nameColumn);
        return table;
    }

    public static List<TableColumn<Record, ?>> removeUnnecessaryColumns(TableView<Record> table) {
        List<TableColumn<Record, ?>> toRemove = table.getColumns().
                stream().
                filter(column -> !TableColumnNames.
                        isNecessaryNameName(column.getText())).
                collect(Collectors.toList());
        table.getColumns().removeAll(toRemove);
        return toRemove;
    }
}
