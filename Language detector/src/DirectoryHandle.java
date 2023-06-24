import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DirectoryHandle {
    //list to holds path to all dir
    private static List<Path> paths = new ArrayList<>();
    //path to working dir
    Path path;

    long alpahChars = 0;

    public DirectoryHandle(String stringPath) {
        this.path = Path.of(stringPath);
    }

    //method finds all directories in passed directory
    public static List<Path> findDir(Path path) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    paths.add(entry);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in walk method in DirectoryHandle class");
        }
        return paths;
    }

    public LanguageList getData(Path path) {
        //creating object for that will hold LanguageList for specific language
        LanguageList singleLan = new LanguageList();
        //stream through all files in current dir
        Stream<Path> stream = null;
        try {
            stream = Files.list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //filtering data from file
        stream.forEach(file -> {
            String dataFromFile = "";
            try {
                if (file.toString().endsWith(".txt")) {
                    dataFromFile = Files.lines(file)
                            .map(String::toLowerCase)
                            .flatMapToInt(CharSequence::chars)
                            .filter(c -> c >= 'a' && c <= 'z')
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    long totalChars = Files.lines(file)
                            .flatMapToInt(CharSequence::chars)
                            .count();

                    long nonAlphabeticChars = Files.lines(file)
                            .flatMapToInt(CharSequence::chars)
                            .filter(c -> !Character.isLetter(c))
                            .count();
                    alpahChars = totalChars - nonAlphabeticChars;
                }
            } catch (Exception e) {
                System.out.println("Exception in stream for files in dir");
            }
            //adding to list
            if (dataFromFile != "")
                LanguageList.insert(singleLan, dataFromFile);
        });
        return singleLan;
    }
}
