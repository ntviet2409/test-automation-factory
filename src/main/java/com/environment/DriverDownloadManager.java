package com.environment;

import com.utilities.OperatingSystem;
import com.utilities.PropertiesHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class DriverDownloadManager {
    private static final String GOOGLE_DOWNLOAD_URL = "https://chromedriver.storage.googleapis.com/";
    private static final String UNZIP_DIR = "src/main/resources/selenium/chromedriver/";
    private static final String SUB_DIR = System.getProperty("os.name").toLowerCase().replace(" ", "_");
    private static final String VERSION = PropertiesHelper.getConfigProperty("webdriverVersion");
    private static final String SOURCE_FILE = GOOGLE_DOWNLOAD_URL + VERSION + getFileExtensionNameByOS();
    private static final String DOWNLOAD_DIR = "src/main/resources/downloads/";
    private static final String DESTINATION_FILE = (DOWNLOAD_DIR + getFileExtensionNameByOS()).replace("//","/");

    public static void downloadDriver() {
        downloadDriverToLocalMachine();
        unzipFile();
        makeWebDriverExecutable();
        setChromeDriverPath();
    }

    private static void setChromeDriverPath() {
        System.setProperty("webdriver.chrome.driver", getChromeDriverPath());
    }

    private static String getFileExtensionNameByOS() {
        if (OperatingSystem.getCurrentOS().equals(OperatingSystem.WINDOWS)) {
            return "/chromedriver_win32.zip";
        } else if (OperatingSystem.getCurrentOS().equals(OperatingSystem.MAC)) {
            return "/chromedriver_mac64.zip";
        } else {
            return "/chromedriver_linux64.zip";
        }
    }

    public static String getChromeDriverPath() {
        String path = "src/main/resources/selenium/chromedriver/" + SUB_DIR + "/" + VERSION + "/chromedriver";
        if (OperatingSystem.getCurrentOS().equals(OperatingSystem.WINDOWS)) {
            path = path + ".exe";
        }
        return path;
    }

    public static void downloadDriverToLocalMachine() {
        File driverFileForDownload = new File(DESTINATION_FILE);
        if (!driverFileForDownload.exists()) {
            log.info("Downloading chrome driver from: " + SOURCE_FILE);
            try {
                log.info("Saving file to: " + DESTINATION_FILE.replace("//","/"));
                FileUtils.copyURLToFile(new URL(SOURCE_FILE), new File(DESTINATION_FILE).getAbsoluteFile(), 10000, 10000);
                log.info("Done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("Driver has already existed");
        }
    }

    public static void unzipFile() {
        byte[] buffer = new byte[1024];
        try {
            // create output directory is not exists
            File folder = new File(UNZIP_DIR + "/" + SUB_DIR + "/" + VERSION)
                    .getAbsoluteFile();
            if (!folder.exists()) {
                folder.mkdir();
            }

            // get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(DESTINATION_FILE));
            // get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(folder + File.separator + fileName);
                if (!newFile.exists()) {
                    log.info("File to unzip : " + newFile);
                    // create all non exists folders
                    // else you will hit FileNotFoundException for compressed folder
                    new File(newFile.getParent()).mkdirs();

                    FileOutputStream fos = new FileOutputStream(newFile);

                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
            log.info("Unzip is finished");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String makeWebDriverExecutable() {
        if (!OperatingSystem.getCurrentOS().equals(OperatingSystem.WINDOWS)) {
            String pathToChromeDriverPath = UNZIP_DIR + "/" + SUB_DIR + "/" + VERSION + "/chromedriver";
            log.info("Run chmod 777 command for: " + pathToChromeDriverPath);

            StringBuilder output = new StringBuilder();

            Process p;
            try {
                p = Runtime.getRuntime().exec("chmod 777 " + pathToChromeDriverPath);
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return output.toString();
        }

        return "";
    }
}
