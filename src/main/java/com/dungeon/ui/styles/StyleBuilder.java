package com.dungeon.ui.styles;

/**
 * Constructeur de styles CSS pour JavaFX
 */
public class StyleBuilder {
    private final StringBuilder style;

    public StyleBuilder() {
        this.style = new StringBuilder();
    }

    public StyleBuilder backgroundColor(String color) {
        style.append("-fx-background-color: ").append(color).append(";");
        return this;
    }

    public StyleBuilder backgroundGradient(String direction, String... colors) {
        style.append("-fx-background-color: linear-gradient(").append(direction);
        for (String color : colors) {
            style.append(", ").append(color);
        }
        style.append(");");
        return this;
    }

    public StyleBuilder textFill(String color) {
        style.append("-fx-text-fill: ").append(color).append(";");
        return this;
    }

    public StyleBuilder fontSize(int size) {
        style.append("-fx-font-size: ").append(size).append("px;");
        return this;
    }

    public StyleBuilder fontWeight(String weight) {
        style.append("-fx-font-weight: ").append(weight).append(";");
        return this;
    }

    public StyleBuilder fontFamily(String family) {
        style.append("-fx-font-family: ").append(family).append(";");
        return this;
    }

    public StyleBuilder padding(String padding) {
        style.append("-fx-padding: ").append(padding).append(";");
        return this;
    }

    public StyleBuilder padding(int padding) {
        style.append("-fx-padding: ").append(padding).append(";");
        return this;
    }

    public StyleBuilder border(String color, int width) {
        style.append("-fx-border-color: ").append(color).append(";");
        style.append("-fx-border-width: ").append(width).append(";");
        return this;
    }

    public StyleBuilder borderRadius(int radius) {
        style.append("-fx-border-radius: ").append(radius).append(";");
        style.append("-fx-background-radius: ").append(radius).append(";");
        return this;
    }

    public StyleBuilder custom(String property, String value) {
        style.append(property).append(": ").append(value).append(";");
        return this;
    }

    public String build() {
        return style.toString();
    }

    @Override
    public String toString() {
        return build();
    }
}

