package io.appflate.restmock;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

/**
 * An implementation of {@link RESTMockFileParser} that allows the retrieval and parsing of files on
 * the local filesystem. This does not require an Android dependencies to be set up, so it can be
 * used when running within Unit Tests.
 */
public class JVMFileParser implements RESTMockFileParser {
    @Override
    public String readJsonFile(String jsonFilePath) throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource(jsonFilePath);
        File file = new File(resource.getPath());
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file, "UTF-8");
        String lineSeparator = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }
}
