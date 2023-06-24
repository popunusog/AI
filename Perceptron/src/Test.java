import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static ArrayList<double[]> singleWithK(ArrayList<Double> listToWorkOn, int k) {
        ArrayList<double[]> outputList = new ArrayList<>();
        int queue = 0;
        for (int p = 0; p < k; p++) {
            double temparr = listToWorkOn.get(p); // initialize temparr with the first element
            int minIndex = p;
            for (int j = p + 1; j < listToWorkOn.size(); j++) { // start from the next element
                double arr = listToWorkOn.get(j);
                if (arr < temparr) {
                    temparr = arr;
                    minIndex = j;
                }

            }
            double[] temporaryarr = {queue, temparr};
            outputList.add(temporaryarr);
            queue++;
            // swap the minimum value with the first element
            double tempVal = listToWorkOn.get(p);
            listToWorkOn.set(p, temparr);
            listToWorkOn.set(minIndex, tempVal);
        }
        return outputList;
    }

    public static ArrayList<ArrayList<Double>> arrlistWithK(ArrayList<ArrayList<double[]>> listToWorkOn, int k) {
        ArrayList<ArrayList<Double>> outputList = new ArrayList<>();
        for (int i = 0; i < listToWorkOn.size(); i++) {
            ArrayList<Double> finalData = new ArrayList<>();
            ArrayList<double[]> distancesForTest = listToWorkOn.get(i);
            for (int p = 0; p < k; p++) {
                double[] temparr = distancesForTest.get(0); // initialize temparr with the first element
                for (int j = 1; j < distancesForTest.size(); j++) { // start from the second element
                    double[] arr = distancesForTest.get(j);
                    if (arr[1] < temparr[1])
                        temparr = arr;
                }
                double temp = temparr[0];
                finalData.add(temp);
                distancesForTest.remove(temparr); // remove the minimum value from distancesForTest
            }
            outputList.add(finalData);
        }
        return outputList;
    }

    public static double distanceCalculation(double a, double b) {
        double distance;
        distance = Math.sqrt(Math.pow((a - b), 2));
        return distance;
    }

    //method to find max index in array
    public static int max(int arr[]) {
        int temp = 0;
        int output = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                temp = arr[i];
            }
            if (arr[i] > temp) {
                temp = arr[i];
                output = i;
            }
        }
        return output;
    }

    public static void main(String[] args) throws Exception {
        //k argument handling
        int k = 0;
        try {
            k = Integer.parseInt(args[0]);
        } catch (Exception nfe) {
            nfe.printStackTrace();
        }
        if (k <= 0)
            throw new Exception("wrong number");
        String trainPath = args[1];
        String testPath = args[2];

        //putting data from training file into arraylist
        List<String[]> dataFromCSVFileToTrain = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(trainPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                dataFromCSVFileToTrain.add(values);
            }
        }
        //putting data from testing file into arraylist
        List<String[]> dataFromCSVFileToTest = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(testPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                dataFromCSVFileToTest.add(values);
            }
        }

        //handling of class in csv file
        HashSet<String> classHashForUnique = new HashSet<>();
        ArrayList<String> classarrlist = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(trainPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(";");
                String lastWord = words[words.length - 1].trim();
                classHashForUnique.add(lastWord);
                classarrlist.add(lastWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //unique words in train.csv array
        String[] uniqueObj = classHashForUnique.toArray(new String[0]);
        //classes in order from train.csv
        String[] ObjInOrderFromTrain = new String[classarrlist.size()];
        for (int i = 0; i < classarrlist.size(); i++) {
            ObjInOrderFromTrain[i] = classarrlist.get(i);
        }


        ArrayList<String> classarrlist2 = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(testPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words2 = line.split(";");
                String lastWord2 = words2[words2.length - 1].trim();
                classarrlist2.add(lastWord2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //classes in order from test.csv
        String[] ObjInOrderFromTest = new String[classarrlist2.size()];
        for (int i = 0; i < classarrlist2.size(); i++) {
            ObjInOrderFromTest[i] = classarrlist2.get(i);
        }

        //for every object to test we go through training data and count distance
        int queueForDataTrainIndex = 1;
        ArrayList<ArrayList<double[]>> map = new ArrayList<>();

        int lengthOfData = dataFromCSVFileToTrain.get(0).length;
        //for list
        for (String[] test : dataFromCSVFileToTest) {
            ArrayList<double[]> distancesForTest = new ArrayList<>();
            for (String[] train : dataFromCSVFileToTrain) {
                double distance = 0;
                for (int i = 0; i < lengthOfData - 1; i++) {
                    distance += distanceCalculation((Double.parseDouble(String.valueOf(test[i]))), (Double.parseDouble(String.valueOf(train[i]))));
                }
                double arr[] = {queueForDataTrainIndex, distance};
                distancesForTest.add(arr);
                queueForDataTrainIndex++;
            }
            queueForDataTrainIndex = 1;
            map.add(distancesForTest);
        }
        //for argument
        String outPut = "";
        // putting k elements to new arraylist
        ArrayList<ArrayList<Double>> testObjwithKelements = arrlistWithK(map, k);


        // printing data with first checking if the value is true
        int flag = 0;
        int howManyCorrect = 0;
        for (int i = 0; i < testObjwithKelements.size(); i++) {
            int[] countingRepliesOfObj = new int[uniqueObj.length];
            String[] ObjArr = new String[uniqueObj.length];
            ArrayList<Double> listForSingleTraining = testObjwithKelements.get(i);

            for (int j = 0; j < listForSingleTraining.size(); j++) {
                double arr = listForSingleTraining.get(j) - 1;
                for (int f = 0; f < countingRepliesOfObj.length; f++) {
                    if (countingRepliesOfObj[f] == 0) {
                        if (flag == 0) {
                            ObjArr[f] = ObjInOrderFromTrain[(int) arr];
                            countingRepliesOfObj[f]++;
                            flag++;

                        }
                    } else if (ObjArr[f].equals(ObjInOrderFromTrain[(int) arr])) {
                        countingRepliesOfObj[f]++;
                        flag++;

                    }
                }
                flag = 0;
            }

            outPut += "wynik oczekiwany:" + ObjInOrderFromTest[i] + "   wynik :" + (ObjArr[max(countingRepliesOfObj)]) + "\n";

            if (ObjInOrderFromTest[i].equals(ObjArr[max(countingRepliesOfObj)])) {
                howManyCorrect++;
            }

        }
        double wynik = ((double) howManyCorrect / ObjInOrderFromTest.length) * 100;
        //outPut += "\n" + "skutecznosc: " + wynik + "%" + ", dla k =" + k;
        String outPut2 ="skutecznosc: " + wynik + "%" + ", dla k =" + k;
        System.out.println(outPut2);
        try {
            FileWriter myWriter = new FileWriter("output.txt");
            myWriter.write(outPut);
            myWriter.close();

        } catch (
                IOException e) {
            System.out.println("error file not created");
            e.printStackTrace();
        }
        //loop for putting extra data to program to test
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Podaj dane: ");
            String argument = scanner.nextLine();
            //for argument
            ArrayList<Double> distancesForArgument = new ArrayList<>();
            int lengthOfArgument = argument.split(";").length;
            String[] argumentArr = argument.split(";");
            double distance = 0;
            //calculating distance for argument
            for (String[] train : dataFromCSVFileToTrain) {
                for (int i = 0; i < lengthOfArgument - 1; i++) {
                    distance += distanceCalculation((Double.parseDouble(String.valueOf(argumentArr[i]))), (Double.parseDouble(String.valueOf(train[i]))));
                }
                double arr = distance;
                distancesForArgument.add(arr);
                distance = 0;
            }

            //reducing the list to only k smallest values
            ArrayList<double[]> listWithKs = singleWithK(distancesForArgument, k);

            ArrayList<double[]> listForSingleTraining = listWithKs;
            ArrayList<String> optionslist = new ArrayList<>();
            for (double[] temporary : listForSingleTraining
            ) {
                double[] getInt = (listForSingleTraining.get((int) temporary[0]));
                int needed = (int) getInt[0];
                optionslist.add(ObjInOrderFromTrain[needed]);
            }
            String whatClass = null;
            int highestCount = 0;
            for (String str : optionslist) {
                int count = 0;
                for (String s : optionslist) {
                    if (s.equals(str)) {
                        count++;
                    }
                }
                if (count > highestCount) {
                    highestCount = count;
                    whatClass = str;
                }
            }
            System.out.println("dla podanych danych wychodzi: " + whatClass);
        }
    }
}
