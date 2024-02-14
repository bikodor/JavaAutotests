package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileUtil {

    public static String getTextFileContent(String path) throws IOException {
        return Files.readString(Path.of(getFileUri(path)));
    }

    private static URI getFileUri(String path) throws FileNotFoundException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        String error = String.format("File not found %s", path);
        if (Objects.isNull(resource)) {
            throw new FileNotFoundException(error);
        }
        try {
            return resource.toURI();
        } catch (URISyntaxException e) {
            throw new FileNotFoundException(error);
        }
    }
}
