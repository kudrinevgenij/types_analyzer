package ru.kudrinevgeniy;

public class IntegerParser {
    public static DataType parse(String line) {
        DataType type = DataType.STRING;
        try {
            Integer.parseInt(line);
            type = DataType.INTEGER;
        } catch (NumberFormatException e) {

        }
        return type;
    }
}