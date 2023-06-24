import java.util.Map;
import java.util.TreeMap;

public class LanguageList {
    LanguageNode head;
    String language;

    //method for inserting node that represents one text for specific language
    public static LanguageList insert(LanguageList list, String text) {
        LanguageNode newNode = new LanguageNode(text);

        //when list is empty we create head
        if (list.head == null) {
            list.head = newNode;
        } else {
            //going from start of list to find the element with no next
            LanguageNode goingThrough = list.head;
            while (goingThrough.next != null) {
                goingThrough = goingThrough.next;
            }

            //adding node
            goingThrough.next = newNode;
        }

        // Return the list by head
        return list;
    }

    public static double[] proportionWector(String text) {
        Map<Character, Integer> wecMap = new TreeMap<>();
        int textLength = text.length();

        for (int i = 0; i < textLength; i++) {
            if (!wecMap.containsKey(text.charAt(i)))
                wecMap.put(text.charAt(i), 1);
            else
                wecMap.put(text.charAt(i), wecMap.get(text.charAt(i)) + 1);
        }
        for (int k = 0; k < 26; k++) {
            char temp = (char) (97 + k);
            if (!wecMap.containsKey(temp)) {
                wecMap.put((char) (97 + k), 0);
            }
        }
        int sum = 0;
        int going_up = 0;
        double arr[] = new double[wecMap.size()];
        for (Map.Entry<Character, Integer> entry : wecMap.entrySet()) {
            arr[going_up] = entry.getValue();
            sum += entry.getValue();
            going_up++;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] / sum;
        }


        return arr;
    }

    //method to convert string to proportion vector with letters amount as
    public static double[] proportionWectorForTest(String data, long lettersAmount) {
        Map<Character, Integer> wecMap = new TreeMap<>();
        double arr[] = new double[26];
        double lettersAmountD = (double) lettersAmount;
        for (int i = 0; i < data.length(); i++) {
            if (!wecMap.containsKey(data.charAt(i)))
                wecMap.put(data.charAt(i), 1);
            else
                wecMap.put(data.charAt(i), wecMap.get(data.charAt(i)) + 1);
        }
        for (int k = 0; k < 26; k++) {
            char temp = (char) (97 + k);
            if (!wecMap.containsKey(temp)) {
                wecMap.put((char) (97 + k), 0);
            }
        }
        int going_up = 0;
        for (Map.Entry<Character, Integer> entry : wecMap.entrySet()) {
            arr[going_up] = entry.getValue() / lettersAmountD;
            going_up++;
        }
        return arr;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    static class LanguageNode {
        String data;
        LanguageNode next;

        LanguageNode(String text) {
            data = text;
            next = null;
        }
    }

}
