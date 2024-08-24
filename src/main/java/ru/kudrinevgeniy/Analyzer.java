package ru.kudrinevgeniy;

import ru.kudrinevgeniy.detect.DetectByDouble;
import ru.kudrinevgeniy.detect.DetectByInteger;
import ru.kudrinevgeniy.detect.DetectByString;
import ru.kudrinevgeniy.receive.ReceiveByDouble;
import ru.kudrinevgeniy.receive.ReceiveByInteger;
import ru.kudrinevgeniy.receive.ReceiveByString;
import ru.kudrinevgeniy.receive.ReceiveByType;

import java.io.*;
import java.util.List;

public class Analyzer {
    private final CLParser parser;
    private final List<ReceiveByType> receiveByTypes;
    private final TypeDetect detect;

    public Analyzer(CLParser parser, List<ReceiveByType> receiveByTypes, TypeDetect detect) {
        this.parser = parser;
        this.receiveByTypes = receiveByTypes;
        this.detect = detect;
    }

    public void load() {
        try (var input = new BufferedReader(new FileReader(parser.inputPath()))) {
            input.lines()
                    .map(detect::of)
                    .forEach(data -> receiveByTypes
                            .forEach(receiveByType -> {
                                if (receiveByType.isGoodFor(data)) {
                                    receiveByType.proceed(data);
                                }
                            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (var byInteger = new PrintWriter(new BufferedWriter(new FileWriter("data/integer.out")));
             var byDouble = new PrintWriter(new BufferedWriter(new FileWriter("data/double.out")));
             var byString = new PrintWriter(new BufferedWriter(new FileWriter("data/string.out")))) {
            new Analyzer(
                    new CLParser(args),
                    List.of(new ReceiveByInteger(byInteger),
                            new ReceiveByDouble(byDouble),
                            new ReceiveByString(byString)),
                    new TypeDetect(
                            List.of(new DetectByInteger(),
                                    new DetectByDouble(),
                                    new DetectByString())
                    )
            ).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
