package main.view;

import main.controller.ActionType;
import main.model.ElementType;
import main.model.Record;
import main.utils.TreeHelper;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class FileChooserWindow {
    private ActionType actionType;

    private final HBox topContainer;
    private final FileViewContainer treeContainer;
    private final TableContainer tableContainer;
    private final Dialog<String> window;
    private final List<Record> records;

    public FileChooserWindow() {
        this.records = new ArrayList<>();
        this.topContainer = new HBox();
        this.treeContainer = new FileViewContainer(TreeHelper.createTree());
        this.tableContainer = null;
        this.window = new Dialog<>();
        configWindow();
    }

    public FileChooserWindow(List<Record> records) {
        this.actionType = null;
        this.records = records;
        this.topContainer = new HBox();
        this.treeContainer = new FileViewContainer(TreeHelper.createTree(), this::filterEvent, records);
        this.tableContainer = new TableContainer(records);
        this.window = new Dialog<>();
        configWindow();
    }

    public void configWindow() {
        this.treeContainer.getTree().
                getSelectionModel().
                selectedItemProperty().
                addListener(((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.getValue().equals("")) {
                        String path = String.join("\\", Objects.requireNonNull(treeContainer.getSelectedPath()));
                        this.records.clear();
                        TreeHelper.populateListWithData(new File(path), this.records);
                        this.tableContainer.updateTable();
                    }
                }));


        this.topContainer.getChildren().
                addAll(this.treeContainer.getTopContainer(),
                        this.tableContainer.getTopContainer());
        this.window.getDialogPane().setExpandableContent(this.topContainer);
        this.window.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.window.setResultConverter(value -> {
            if (value.equals(ButtonType.OK)) {
                List<String> paths = this.treeContainer.getSelectedPath();
                if (paths == null) {
                    return null;
                }
                paths.remove(0);
                if (ActionType.LOAD.equals(this.actionType)) {
                    this.tableContainer.getSelectedItem().ifPresentOrElse(record -> {
                        if (ElementType.FOLDER.equals(record.getType())) {
                            paths.clear();
                        } else {
                            paths.add(record.getName());
                        }
                    }, paths::clear);
                } else if (ActionType.SAVE.equals(this.actionType)) {
                    this.tableContainer.getSelectedItem().ifPresent(record -> {
                        if (ElementType.FILE.equals(record.getType())) {
                            paths.clear();
                        } else {
                            paths.add(record.getName());
                        }
                    });
                }
                if (paths.size() == 0) {
                    return null;
                }
                return String.join("\\", paths);
            } else {
                TreeHelper.clearTree(this.treeContainer.getTree());
                return null;
            }
        });
    }

    public Optional<String> getPath() {
        TreeHelper.populateTree(this.treeContainer.getTree(), records);
        Optional<String> path = this.window.showAndWait();
        TreeHelper.clearTree(this.treeContainer.getTree());
        return path;
    }

    public String getExtensionToFilter() {
        return this.treeContainer.getFilterRegExpr().getText();
    }

    public String getFileName() {
        return this.treeContainer.getFileName().getText();
    }

    private void filterEvent(ActionEvent e) {
        if (!this.getExtensionToFilter().equals("")) {
            this.records.removeIf(record -> {
                if (ElementType.FILE.equals(record.getType())) {
                    return !record.getExtension().
                            equals(this.getExtensionToFilter());
                }
                return false;
            });
            this.tableContainer.updateTable();
        }
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
