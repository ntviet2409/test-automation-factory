package com.reports;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class AllureSetup {
    private static final String RESULT_FOLDER = "allure-results";
    private static final String PROPERTIES_FILE_NAME = "environment.properties";
    private static final String REPORT_PROPERTIES_FILE_NAME = RESULT_FOLDER + "/" + PROPERTIES_FILE_NAME;
    private static final String SCREENSHOT_FOLDER = "Screenshots";
    private static final File REPORT_PROPERTIES_FILE = new File(REPORT_PROPERTIES_FILE_NAME);

    public static void prepareAllureResultsDir() {
        log.info("Step 1. delete allure results folder");
        File allureResultsFolder = new File(RESULT_FOLDER);
        deleteAllureResultsFolder(allureResultsFolder);

        log.info("Step 2. create allure results folder");
        createAllureResultsFolder(allureResultsFolder);

        log.info("Step 3. delete allure screenshots folder");
        File allureSnapshotsFolder = new File(SCREENSHOT_FOLDER);
        deleteAllureResultsFolder(allureSnapshotsFolder);

        log.info("Step 4. create allure screenshots folder");
        createAllureResultsFolder(allureSnapshotsFolder);

        log.info("Step 5. create and populate allure report properties file");
        writeToAllureConfigFile("ENVIRONMENT", "Test Environment");
    }

    private static void writeToAllureConfigFile(String propName, String propValue) {

        try {
            if (REPORT_PROPERTIES_FILE.createNewFile()) {
                log.info("File: " + REPORT_PROPERTIES_FILE_NAME + " is created");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // load config file
        Properties props = null;
        try {
            FileInputStream in = new FileInputStream(REPORT_PROPERTIES_FILE_NAME);
            props = new Properties();
            props.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // edit config file
        try {
            FileOutputStream out = new FileOutputStream(REPORT_PROPERTIES_FILE_NAME);
            props.setProperty(propName, propValue);
            props.store(out, null);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteAllureResultsFolder(File allureResultsFolder) {
        if (allureResultsFolder.isDirectory()) {
            // directory is empty, then delete it
            if (allureResultsFolder.list().length == 0) {
                allureResultsFolder.delete();
                log.info("Folder is deleted : " + allureResultsFolder.getAbsolutePath());
            } else {
                // list all the directory contents
                String files[] = allureResultsFolder.list();
                for (String temp : files) {
                    // construct the file structure
                    File fileDelete = new File(allureResultsFolder, temp);
                    // recursive delete
                    deleteAllureResultsFolder(fileDelete);
                }
                // check the directory again, if empty then delete it
                if (allureResultsFolder.list().length == 0) {
                    allureResultsFolder.delete();
                    log.info("Directory is deleted : " + allureResultsFolder.getAbsolutePath());
                }
            }
        } else {
            // if file, then delete it
            allureResultsFolder.delete();
            log.info("File is deleted: " + allureResultsFolder.getName());
        }
    }

    private static void createAllureResultsFolder(File allureResultsFolder) {
        if (!allureResultsFolder.exists()) {
            if (allureResultsFolder.mkdir()) {
                log.info("Directory is created!");
            } else {
                log.error("Failed to create directory!");
            }
        }
    }

    public static void createAllureHistoryFolder(File allureHistoryFolder) {

        if (!allureHistoryFolder.exists()) {
            if (allureHistoryFolder.mkdir()) {
                log.info("Directory is created!");
            } else {
                log.error("Failed to create directory!");
            }
        }
    }
}
