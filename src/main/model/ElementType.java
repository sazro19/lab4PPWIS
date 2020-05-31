package main.model;

import javafx.scene.image.ImageView;

public enum ElementType {
    FOLDER("/image/folder.jpg"), FILE("/image/file.png");
    private final String path;

    ElementType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public ImageView getImageView() {
        return new ImageView(String.valueOf(this.getClass().getResource(getPath())));
    }
}
