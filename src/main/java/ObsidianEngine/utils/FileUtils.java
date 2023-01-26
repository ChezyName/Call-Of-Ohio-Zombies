package ObsidianEngine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtils {
    public static String loadAsString(String path){
        StringBuilder File = new StringBuilder();
        String line = "";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(path)))){
            while((line = reader.readLine()) != null){
                File.append(line).append("\n");
            }
        }
        catch (IOException e) {
            System.err.println("file not found at " + path);
        }

        return File.toString();
    }
}
