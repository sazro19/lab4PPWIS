package main.view;

import main.model.Record;
import main.utils.TreeHelper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileViewContainer {
    private final VBox topContainer;
    private final TreeView<String> tree;
    private final HBox textContainer;
    private final TextField fileName;
    private final VBox filterContainer;
    private final TextField filterRegExpr;
    private final Button filterButton;
    private final Button toHomeButton;
    private final List<Record> records;

    public FileViewContainer(TreeView<String> tree) {
        this.toHomeButton = new Button("To home directory");
        this.records = new ArrayList<>();
        this.topContainer = new VBox();
        this.tree = tree;
        this.textContainer = new HBox();
        this.fileName = new TextField();
        this.filterContainer = new VBox();
        this.filterRegExpr = new TextField();
        this.filterButton = new Button("Filter files by RegExpr");
        configHomeButton();
        configContainers();
    }

    public FileViewContainer(TreeView<String> tree, EventHandler<ActionEvent> filterEvent, List<Record> records) {
        this.toHomeButton = new Button("To home directory");
        this.records = records;
        this.topContainer = new VBox();
        this.tree = tree;
        this.textContainer = new HBox();
        this.fileName = new TextField();
        this.filterContainer = new VBox();
        this.filterRegExpr = new TextField();
        this.filterButton = new Button("Filter files");
        this.filterButton.setOnAction(filterEvent);
        configHomeButton();
        configContainers();
    }

    public void configContainers() {
        this.filterContainer.
                getChildren().
                addAll(
                        filterRegExpr,
                        filterButton);
        this.textContainer.getChildren().addAll(fileName, filterContainer);
        this.topContainer.getChildren().addAll(tree, textContainer, toHomeButton);
    }

    public void configHomeButton() {
        toHomeButton.setOnAction(e -> {
            TreeItem<String> toSelect = TreeHelper.getHomeNode(this.tree.getRoot());
            if (toSelect != null) {
                this.tree.getSelectionModel().select(toSelect);
            }
        });
    }

    public final TreeItem<String> getSelectedFileItem() {
        return tree.getSelectionModel().getSelectedItem();
    }

    public final List<String> getSelectedPath() {
        TreeItem<String> treeItem = this.tree.getSelectionModel().getSelectedItem();
        if (treeItem == null) {
            return null;
        }
        List<String> paths = new ArrayList<>();
        do {
            paths.add(treeItem.getValue());
            treeItem = treeItem.getParent();
        } while (treeItem != null);
        Collections.reverse(paths);
        return paths;
    }

    public VBox getTopContainer() {
        return this.topContainer;
    }

    public TreeView<String> getTree() {
        return this.tree;
    }

    public TextField getFileName() {
        return fileName;
    }

    public TextField getFilterRegExpr() {
        return filterRegExpr;
    }
}
