package main.controller;

import main.view.FileChooserWindow;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public abstract class FileChooser {
    private FileChooserWindow window;

    public abstract Optional<File> load();

    public abstract Optional<File> save(String fileName) throws IOException;

    public abstract Optional<File> save() throws IOException;

    public FileChooserWindow getWindow() {
        return window;
    }

    public void setWindow(FileChooserWindow window) {
        this.window = window;
    }
}
