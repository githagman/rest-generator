package com.hagman;

import com.hagman.jdbc.TableMetaData;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

/**
 * Tests the generated classes are meeting spec
 * 1. Run the default template on a table called Test
 * 2. Check if generated string is equal to spec file, mostly useful if done line by
 * @author tom hagman
 * @since 09/24/2014
 */
public class SparkApiTest {
    RestGenerator restGenerator;
    TableMetaData tableMetaData;
    @Before
    public void setup() {
        restGenerator = new RestGenerator();
        tableMetaData = new TableMetaData();
        tableMetaData.setTableName("test");
        tableMetaData.setRouteName("test");
        tableMetaData.setPackageName("com.hagman.rest");
    }

    @Test
    public void testSparkApi() throws IOException {
        String renderedTemplate = restGenerator.getRenderedTemplate(tableMetaData, "SparkTest");
        String[] renderedLines = renderedTemplate.split("\n");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(SparkApiTest.class.getResourceAsStream("SparkTestRestApi.java")))) {
            String line;
            int lineNumber = 0;
            while((line = bufferedReader.readLine()) != null) {
                if (renderedLines.length >= lineNumber) {
                    assertEquals(line, renderedLines[lineNumber]);
                } else {
                    break;
                }
            }
        }
    }
}
