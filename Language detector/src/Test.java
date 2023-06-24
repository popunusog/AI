import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static double[] normalizeTestingWector(double arr[]) {
        double sum = 0;

        for (double d : arr)
            sum += Math.pow(d, 2);

        double divider = sum;
        divider = Math.sqrt(divider);

        double arrOut[] = new double[arr.length];

        for (int i = 0; i < arr.length; i++)
            arrOut[i] = arr[i] / divider;

        return arrOut;
    }

    public static void main(String[] args) {
        DirectoryHandle dh = new DirectoryHandle("PASS PATH TO DATA FOLDER");
        List<Path> languageDirPaths = dh.findDir(dh.path);
        List<LanguageList> listOfLanguageLists = new ArrayList<>();
        List<String> LanguageName = new ArrayList<>();
        //loop through directories
        for (Path singleDir : languageDirPaths) {
            //noting the name of language
            LanguageName.add(singleDir.getFileName().toString());
            //adding all data from specific language
            LanguageList langList = dh.getData(singleDir);
            langList.setLanguage(singleDir.getFileName().toString());
            listOfLanguageLists.add(langList);
        }
        //languagePerceptrons will hold perceptron for each language
        ArrayList<Perceptron> languagePerceptrons = new ArrayList<>();
        //passing all languages with text to toShuffle
        ArrayList<String[]> toShuffle = new ArrayList<>();
        for (LanguageList languageList : listOfLanguageLists) {
            LanguageList.LanguageNode currNode = languageList.head;
            while (currNode != null) {
                String[] temp = new String[2];
                temp[0] = languageList.getLanguage();
                temp[1] = currNode.data;
                currNode = currNode.next;
                toShuffle.add(temp);
            }
        }
        Collections.shuffle(toShuffle);
        int j = 0;
        while (j < languageDirPaths.size()) {
            Perceptron perceptron = new Perceptron(26);
            for (int i = 0; i < 10000; i++) {
                for (String[] textTolearn : toShuffle) {
                    //assiging if perceptron shoud return true or false
                    int learningOutput = 0;
                    if (textTolearn[0].equals(LanguageName.get(j)))
                        learningOutput = 1;
                    //creating proportion wector from prepered string
                    double[] proportionWector = LanguageList.proportionWector(textTolearn[1]);
                    perceptron.learn(proportionWector, learningOutput);

                }
            }
            languagePerceptrons.add(perceptron);
            j++;
        }
        for (Perceptron perceptron : languagePerceptrons)
            perceptron.normalize();

        //TESTING
        while (true) {
            System.out.println("Pass the path to txt file you want to determin language\n" +
                    "Check if the language is supported!");
            Scanner scanTxtFile = new Scanner(System.in);
            String path = scanTxtFile.next();
            String dataFromFile = "";

            long totalChars = 0;
            long nonAlphabeticChars = 0;
            try {
                File file = new File(path);
                Path filePath = Paths.get(path);
                if (file.exists() && file.isFile()) {
                    try {
                        if (file.toString().endsWith(".txt")) {
                            dataFromFile = Files.lines(filePath)
                                    .map(String::toLowerCase)
                                    .flatMapToInt(CharSequence::chars)
                                    .filter(c -> c >= 'a' && c <= 'z')
                                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                                    .toString();

                            totalChars = Files.lines(filePath)
                                    .flatMapToInt(CharSequence::chars)
                                    .count();

                            nonAlphabeticChars = Files.lines(filePath)
                                    .flatMapToInt(CharSequence::chars)
                                    .filter(c -> !Character.isLetter(c))
                                    .count();
                        }
                    } catch (Exception e) {
                        System.out.println("Something went wrong with reading the file");
                    }
                } else
                    System.out.println("The file doesnt exist or its not a file");
            } catch (Exception e) {
                System.out.println("file not found try again!");
            }
            long lettersAmount = totalChars - nonAlphabeticChars;
            //putting data from file in string format to proportion wector
            double arr[] = LanguageList.proportionWectorForTest(dataFromFile, lettersAmount);
            double bestScore = -100;
            String name = "";
            int counter = 0;
            double[] arr2 = normalizeTestingWector(arr);
            //Finding best perceptron
            for (Perceptron p : languagePerceptrons) {
                if (p.judge(arr2) > bestScore) {
                    bestScore = p.judge(arr2);
                    name = LanguageName.get(counter);
                }
                counter++;
            }
            System.out.println(bestScore + " " + name);
        }
    }
}

