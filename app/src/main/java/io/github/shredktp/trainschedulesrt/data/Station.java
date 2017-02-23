package io.github.shredktp.trainschedulesrt.data;

/**
 * Created by Korshreddern on 28-Jan-17.
 */

public class Station {
    private String name;
    private String line;

    public Station() {
        this.name = "";
        this.line = "";
    }

    public Station(String name) {
        this.name = name;
        this.line = "";
    }

    public Station(String name, String line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "Station{" +
                "\name='" + name + '\'' +
                ",\nline='" + line + '\'' +
                "\n}";
    }
}
