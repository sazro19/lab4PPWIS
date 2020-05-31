package main.utils;

import main.model.ElementType;
import main.model.Record;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeHelper {

    public static TreeView<String> createTree(TreeItem<String> fictionalRoot) {
        TreeView<String> tree = new TreeView<>();
        tree.setRoot(fictionalRoot);
        return tree;
    }

    public static TreeView<String> createTree() {
        TreeView<String> tree = new TreeView<>();
        tree.setRoot(new TreeItem<>(""));
        return tree;
    }

    public static void populateTree(TreeView<String> tree) {
        Arrays.stream(File.listRoots()).
                sorted().
                forEach(rootDirectory -> {
                    TreeItem<String> subRoot = new TreeItem<>(rootDirectory.getAbsolutePath());
                    tree.getRoot().getChildren().add(subRoot);
                    populateNodeWithData(subRoot, rootDirectory);
                });
    }

    public static void populateTreeWithDirectories(TreeView<String> tree) {
        Arrays.stream(File.listRoots()).
                sorted().
                forEach(rootDirectory -> {
                    TreeItem<String> subRoot = new TreeItem<>(rootDirectory.getAbsolutePath());
                    tree.getRoot().getChildren().add(subRoot);
                    populateNodeWithData(subRoot, rootDirectory);
                });
    }

    public static void populateNodeWithData(TreeItem<String> root, File file) {
        if (file.listFiles() != null) {
            Arrays.stream(Objects.requireNonNull(file.listFiles())).
                    filter(element -> !element.isHidden()).filter(File::canRead).sorted((first, second) -> {
                        if (first.isDirectory() == second.isDirectory()) {
                            return 0;
                        } else {
                            if (first.isDirectory()) {
                                return -1;
                            }
                        }
                        return 1;
                    }).
                    forEach(correctElement -> {
                                TreeItem<String> subRoot = new TreeItem<>(correctElement.getName());
                                if (correctElement.isDirectory()) {
                                    subRoot.setGraphic(new ImageView("image/folder.jpg"));
                                    if (correctElement.listFiles() != null &&
                                            Objects.requireNonNull(correctElement.listFiles()).length > 0) {
                                        TreeItem<String> mockItem = new TreeItem<>("");
                                        subRoot.getChildren().add(mockItem);
                                        subRoot.expandedProperty().addListener(
                                                ((observable, oldValue, newValue) -> {
                                                    if (!oldValue && newValue) {
                                                        subRoot.getChildren().remove(mockItem);
                                                        populateNodeWithData(subRoot, correctElement);
                                                    } else {
                                                        subRoot.getChildren().clear();
                                                        subRoot.getChildren().add(mockItem);
                                                    }
                                                }
                                                )
                                        );
                                    }
                                } else {
                                    subRoot.setGraphic(new ImageView("image/file.png"));
                                }
                                root.getChildren().add(subRoot);
                            }
                    );
        }
    }

    public static void clearTree(TreeView<String> tree) {
        tree.getRoot().getChildren().clear();
    }

    public static void populateTree(TreeView<String> tree, List<Record> records) {
        Arrays.stream(File.listRoots()).
                sorted().
                forEach(rootDirectory -> {
                    TreeItem<String> subRoot = new TreeItem<>(rootDirectory.getAbsolutePath());
                    tree.getRoot().getChildren().add(subRoot);
                    populateNodeWithDirectories(subRoot, rootDirectory, records);
                });
    }

    public static void populateNodeWithDirectories(TreeItem<String> root, File file, List<Record> records) {
        if (file.listFiles() != null) {
            Arrays.
                    stream(Objects.requireNonNull(file.listFiles())).
                    filter(element -> !element.isHidden()).
                    filter(File::canRead).
                    filter(File::canWrite).
                    filter(File::isDirectory).
                    forEach(
                            correctElement -> {
                                TreeItem<String> subRoot = new TreeItem<>(correctElement.getName());
                                subRoot.setGraphic(new ImageView("image/folder.jpg"));
                                if (correctElement.listFiles() != null
                                        && Objects.requireNonNull(correctElement.listFiles()).length > 0) {
                                    TreeItem<String> mockItem = new TreeItem<>("");
                                    subRoot.getChildren().add(mockItem);
                                    subRoot.expandedProperty().addListener(
                                            ((observable, oldValue, newValue) -> {
                                                if (!oldValue && newValue) {
                                                    subRoot.getChildren().remove(mockItem);
                                                    populateNodeWithDirectories(subRoot, correctElement, records);
                                                } else {
                                                    subRoot.getChildren().clear();
                                                    subRoot.getChildren().add(mockItem);
                                                }
                                            }
                                            )
                                    );
                                }
                                root.getChildren().add(subRoot);
                            }
                    );
        }
    }

    public static void populateListWithData(File file, List<Record> records) {
        if (file.listFiles() != null) {
            Arrays.
                    stream(Objects.requireNonNull(file.listFiles())).
                    filter(element -> !element.isHidden()).
                    filter(File::canRead).
                    filter(File::canWrite).
                    sorted((first, second) -> {
                        if (first.isDirectory() == second.isDirectory()) {
                            return 0;
                        } else {
                            if (first.isDirectory()) {
                                return -1;
                            }
                        }
                        return 1;
                    }).
                    forEach(
                            correctElement -> {
                                ElementType type = ElementType.FOLDER;
                                String name = correctElement.getName();
                                Date date = new Date(correctElement.lastModified());
                                long size = Long.MIN_VALUE;
                                String extension = "";
                                if (!correctElement.isDirectory()) {
                                    type = ElementType.FILE;
                                    if (name.lastIndexOf(".") > 0) {
                                        extension = name.substring(name.lastIndexOf(".") + 1);
                                    }
                                    size = file.length();
                                }
                                records.add(new Record(type, name, date, extension, size));
                            }
                    );
        }
    }

    public static TreeItem<String> getHomeNode(TreeItem<String> root) {
        String pathToHome = System.getProperty("user.home");
        File homeDirectory = new File(pathToHome);
        if (homeDirectory.exists() && homeDirectory.isDirectory()) {
            List<String> pathElements = new ArrayList<>();
            while (homeDirectory != null) {
                if (!homeDirectory.getName().equals("")) {
                    pathElements.add(homeDirectory.getName());
                } else {
                    pathElements.add(homeDirectory.getAbsolutePath());
                }
                homeDirectory = homeDirectory.getParentFile();
            }
            Collections.reverse(pathElements);
            TreeItem<String> homeItem = null;
            for (String pathElement : pathElements) {
                if (root != null) {
                    root = root.
                            getChildren().
                            stream().
                            collect(Collectors.
                                    toMap(TreeItem::getValue,
                                            Function.identity())).
                            getOrDefault(pathElement, null);
                    if (root != null) {
                        root.setExpanded(true);
                    }
                }
            }
            return root;
        }
        return null;
    }
}
