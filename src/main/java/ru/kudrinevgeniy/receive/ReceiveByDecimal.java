package ru.kudrinevgeniy.receive;

import ru.kudrinevgeniy.DataType;
import ru.kudrinevgeniy.statistic.Statistics;

import java.io.*;
import java.nio.file.FileSystems;

public class ReceiveByDecimal implements ReceiveByType {
    private final String source;
    private final Statistics statistics;
    private PrintWriter writer;

    public ReceiveByDecimal(String source, String prefix, Statistics statistics) {
        String path = source + FileSystems.getDefault().getSeparator();
        this.source = source.isEmpty() ? prefix + "floats.txt" : path + prefix + "doubles.txt";
        this.statistics = statistics;
    }

    @Override
    public boolean init(boolean add) {
        var success = false;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(source)));
            success = true;
        } catch (IOException e) {
            System.out.println("Can't init resources:" + source);
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public boolean isGoodFor(DataType data) {
        return data.type() == DataType.Types.DECIMAL;
    }

    @Override
    public void proceed(DataType data) {
        writer.println(data.value());
        statistics.add(data.value());
    }

    @Override
    public void destroy() {
        System.out.println("Statistic for floats:");
        System.out.println(statistics.info());
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (statistics.isEmpty()) {
            var deleted = new File(source).delete();
            if (!deleted) {
                System.out.println("Can't delete file: " + source);
            }
        }
    }
}

