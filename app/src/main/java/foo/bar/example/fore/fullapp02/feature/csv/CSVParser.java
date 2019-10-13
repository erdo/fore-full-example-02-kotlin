package foo.bar.example.fore.fullapp02.feature.csv;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import co.early.fore.core.Affirm;


/**
 * Copyright © 2017-2018 early.co. All rights reserved.
 */
public class CSVParser implements Iterable<CSVParser.Line>{

    private final List<CSVParser.Line> lines = new ArrayList<>();

    public CSVParser(String csvData) {
        this(csvData, CSVBuilder.DEFAULT_DELIMITER);
    }

    public CSVParser(String csvData, String delimiter) {

        Affirm.notNull(csvData);
        Affirm.notNull(delimiter);

        if (delimiter.length() == 0){
            throw new RuntimeException("delimiter needs to be at least 1 character");
        }

        parseLines(csvData, delimiter);
    }

    private void parseLines(String csvData, String delimiter){

        StringTokenizer stringTokenizer = new StringTokenizer(csvData, CSVBuilder.LINE_TERMINATOR, false);

        while (stringTokenizer.hasMoreTokens()) {
            lines.add(new Line(stringTokenizer.nextToken(), delimiter));
        }
    }


    @NonNull
    @Override
    public Iterator<CSVParser.Line> iterator() {
        return lines.iterator();
    }

    public static class Line implements Iterable<String>{

        private final List<String> columns = new ArrayList<>();

        private Line(String line, String delimiter) {
            StringTokenizer stringTokenizer = new StringTokenizer(line, delimiter, false);

            while (stringTokenizer.hasMoreTokens()) {

                String token = stringTokenizer.nextToken();

                columns.add(token);
                //columns.add(stringTokenizer.nextToken());
            }
        }

        @NonNull
        @Override
        public Iterator<String> iterator() {
            return columns.iterator();
        }
    }

}
