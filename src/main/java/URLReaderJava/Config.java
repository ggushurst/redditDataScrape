package URLReaderJava;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Config {
    Properties properties;

    public Config() {
        File configFile = new File("config.properties");
        try {
            FileReader reader = new FileReader(configFile);
            properties = new Properties();
            properties.load(reader);
        } catch (IOException ex) {
            System.out.println("Properties file not found.");
            System.out.println(ex);
        }
    }
}
