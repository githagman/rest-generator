package com.hagman;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.hagman.jdbc.DatabaseBroker;
import com.hagman.jdbc.TableMetaData;

import java.io.*;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * RestGenerator.java
 * @author tom hagman
 * @since 09/24/2014
 *
 * Output Java Spark-based REST Api Routes based on a mustache-based template for each table in a specified database.
 * Package and run this as a Jar to produce the routes.  See README.md
 * main() method gets run when the jar is run, which highlights the steps
 */

public class RestGenerator {

    public static Logger LOG = Logger.getLogger(RestGenerator.class.getName());

    private Properties properties;
    private DatabaseBroker databaseBroker;
    private File outputDirectory;
    private List<TableMetaData> tables = new ArrayList<>();

    private void initProperties() throws IOException {
        properties = new Properties();
        try (InputStream is = RestGenerator.class.getResourceAsStream("RestGenerator.properties")) {
            properties.load(is);
        }
    }

    private void buildDatabaseBroker() {
        databaseBroker = new DatabaseBroker(properties.getProperty("Connection"), properties.getProperty("Username"), properties.getProperty("Password"));
    }

    /**
     * startup loads the properties, initializes the database connection, and checks the outputdirectory is ready
     * @throws IOException
     */
    public void startup() throws IOException {
        try {
            initProperties();
        } catch (IOException e) {
            throw new RuntimeException("Unable to build properties", e);
        }
        buildDatabaseBroker();

        String outputDirectoryName = properties.getProperty("OutputDirectory");
        outputDirectory = new File(outputDirectoryName);
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }
    }

    private void readTables() {
        try {
            DatabaseMetaData metaData = databaseBroker.getConnection().getMetaData();
            ResultSet metaDataTables = metaData.getTables(properties.getProperty("DatabaseName"), "", "%", null);
            while (metaDataTables.next()) {
                String tableName = metaDataTables.getString("TABLE_NAME");
                //remove all underscores for readability
                String cleanedTableName = tableName.replaceAll("_", "");
                LOG.info("Adding Table : " + cleanedTableName);
                TableMetaData metaTable = new TableMetaData();
                metaTable.setTableName(cleanedTableName);
                metaTable.setInterfacePackage(properties.getProperty("InterfacePackage"));
                tables.add(metaTable);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to process Table SQL", e);
        }
    }


    private void generateRestClasses() {
        for (TableMetaData table : tables) {
            //output java file based on output directory and file name + RestApi.java
            String tableFilePath = outputDirectory + "/" + table.getTableName() + "RestApi.java";
            try {
                MustacheFactory mf = new DefaultMustacheFactory();
                Mustache mustache = mf.compile(new InputStreamReader(RestGenerator.class.getResourceAsStream("templates/" + properties.getProperty("TemplateFileName"))), "api.template");
                mustache.execute(new PrintWriter(new FileOutputStream(tableFilePath)), table).flush();
            } catch (IOException e) {
                throw new RuntimeException("Unable to create file : " + tableFilePath, e);
            }

        }
    }

    /**
     * cleanup is just used to free memory.  it will do things like return db connections if we want
     * to parallel-ize via a connection-pool.
     */
    private void cleanup() {
        System.gc();
    }

    public static void main(String[] args) throws IOException {
        RestGenerator restGenerator = new RestGenerator();
        restGenerator.startup();
        LOG.info("************* Startup complete ************************ ");
        restGenerator.readTables();
        LOG.info("************* Tables read. **************************** ");
        restGenerator.generateRestClasses();
        LOG.info("************* Classes generated. ********************** ");
        restGenerator.cleanup();
        LOG.info("************* Generator Cleanup Complete. ************* ");
    }

}
