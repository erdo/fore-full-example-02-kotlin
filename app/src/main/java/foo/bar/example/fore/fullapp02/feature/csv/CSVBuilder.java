package foo.bar.example.fore.fullapp02.feature.csv;


import co.early.fore.core.Affirm;

/**
 * Copyright © 2017-2018 early.co. All rights reserved.
 */
public class CSVBuilder {

    public static final String LINE_TERMINATOR = "\r\n";
    public static final String DEFAULT_DELIMITER = ",";

    private final String delimiter;
    private StringBuffer stringBuffer = new StringBuffer();

    public CSVBuilder() {
        this(DEFAULT_DELIMITER);
    }

    public CSVBuilder(String delimiter) {
        this.delimiter = Affirm.notNull(delimiter);

        if (delimiter.length() == 0){
            throw new RuntimeException("delimiter needs to be at least 1 character");
        }
    }

    /**
     *
     * @param columns must not contain the delimiter
     *
     */
    public CSVBuilder addLine(String... columns){

        for (int i = 0; i < columns.length; i++) {

            if (columns[i].contains(delimiter)){
                throw new RuntimeException("current restriction is that the columns cannot contain the delimiter [" + delimiter + "]");
            }

            stringBuffer.append(columns[i]);
            stringBuffer.append(delimiter);
        }

        if (stringBuffer.length() > 0) {
            stringBuffer.setLength(stringBuffer.length() - delimiter.length());
        }

        stringBuffer.append(LINE_TERMINATOR);

        return this;
    }


    @Override
    public String toString() {
        return stringBuffer.toString();
    }
}
