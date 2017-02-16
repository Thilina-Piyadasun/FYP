package org.abithana.prescription.impl.Redistricting;import com.univocity.parsers.csv.CsvParser;import com.univocity.parsers.csv.CsvParserSettings;

import java.io.*;
import java.lang.IllegalStateException;import java.lang.Integer;import java.lang.String;import java.util.List;

import com.univocity.parsers.csv.*;

import java.io.*;
import java.util.List;

/**
 * Created by malakaganga on 1/19/17.
 */
public class CsvReader implements Serializable{

    /*
    * To get an inputstream reader for given relativepath
    * */

    public static InputStreamReader getReader(String relativePath) {
        try {
            File initialFile = new File(relativePath);
            return new InputStreamReader(new FileInputStream(initialFile));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to read input", e);
        }
    }

    public List<String[]> readCsv(String relativePath, Integer[] columnsWanted) {

        CsvParserSettings settings = new CsvParserSettings();
        //uses '\n' as the line separator sequence.
        //are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
        settings.setMaxCharsPerColumn(100000);
        settings.getFormat().setLineSeparator("\n");

        //To read just the wanted columns
        settings.selectIndexes(columnsWanted);

        // creates a CSV parser
        CsvParser parser = new CsvParser(settings);

        // parses all rows in one go.
        List<String[]> allRows = parser.parseAll(CsvReader.getReader(relativePath));

        return allRows;
    }
}
