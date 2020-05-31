package main.utils;

public enum TableColumnNames {
    ICON("Icon"),
    NAME("Name"),
    DATE("Date"),
    EXTENSION("Extension"),
    SIZE("Size");

    private final String name;

    TableColumnNames(String name) {
        this.name = name;
    }

    public static boolean isNecessaryNameName(String name) {
        return name.equals(TableColumnNames.ICON.getName()) || name.equals(TableColumnNames.NAME.getName());
    }

    public String getName() {
        return name;
    }
}
