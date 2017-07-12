import java.util.ArrayList;

/**
 * Ward Bradt, Jimmy Pham, Moe Sunami
 * April 26, 2017
 *
 */
public class EnglishWord {
    private static LinkedList<Integer>[] hashtable = new LinkedList[60];
    private String word;

    public EnglishWord(String word) {
        this.word = word;
    }

    /**
     * Places the hash value of a word in the static field hashtable
     *
     * @param word the EnglishWord whose hashCode() is to be added to hashtable.
     * @return if word's hashCode() was added.
     */
    public static boolean placeInTable(EnglishWord word) {
        int hashed = word.hashCode();
        for (int i = 0; i < hashtable.length; i++) {
            // get(0) because each column in the array will always hold the same values.
            if (hashtable[i] == null) hashtable[i] = new LinkedList<Integer>(hashed);
            if (hashtable[i].get(0).equals(hashed)) {
                hashtable[i].add(hashed);
                return true;
            }
        }
        // if there are 60 different hashbuckets already being used in the array, then it cant be added.
        return false;
    }

    /**
     * The hashCode for EnglishWord adds up the ascii values of its word
     * field's characters and then %= 60.
     *
     * @return the hashCode of this EnglishWord object
     */
    public int hashCode() {
        int result = 0;
        // adds up the ascii values of this.word's characters.
        for (int i = 0; i < word.length(); i++) {
            // adds the ascii value of the character at i
            // (int) of a char returns its ascii value.
            result += (int)word.charAt(i);
        }
        // return result mod 60
        return result %= 60;
    }

    /**
     * Takes any String and strips it of all characters except for letters and whitespace. Then
     * returns an EnglishWord ArrayList of all words (Separated by whitespace)
     * Also feeds the hashCode of each EnglishWord in parag into hashtable.
     *
     * @param parag the String to be converted into an ArrayList
     * @return an ArrayList of each word in the paragraph.
     */
    public static ArrayList<EnglishWord> breakUp(String parag) {
        ArrayList<EnglishWord> result = new ArrayList<>();
        // Received help on the replaceAll line from Dr.Z and this link:
        // http://stackoverflow.com/questions/1805518/replacing-all-non-alphanumeric-characters-with-empty-strings
        // Removes all characters from parag except letters and spaces.
        parag = parag.replaceAll("[^A-Za-z\\s]+", "");

        while (parag.length() > 0) {
            int spaceIndex = parag.indexOf(' ');
            // if there is only one more character and it is a space, do nothing.
            if (spaceIndex == parag.length()) break;

            // If there is still a space
            if (spaceIndex > 0){
                // the word to be added, the first word.
                EnglishWord addedWord = new EnglishWord(parag.substring(0, spaceIndex));
                // add the first word to the table
                placeInTable(addedWord);
                // add the word to the result.
                result.add(addedWord);
                // shorten parag, removing the word that was just added and the space after it.
                parag = parag.substring(spaceIndex+1);
            }

            /*
             * If the first character is a space - only
             * happens if there is multiple spaces in a row/
             * whitespace in parag.
             */
            else if (spaceIndex == 0) {
                parag = parag.substring(1);
            }

            // only get here if there is no more whitespace in parag.
            else {
                // the rest of the String parag.
                EnglishWord addedWord = new EnglishWord(parag);
                placeInTable(addedWord);
                result.add(addedWord);
                break;
            }
        }
        return result;
    }

    public String getWord() {
        return word;
    }

    public static LinkedList<Integer>[] getHashtable() {
        return hashtable;
    }

    public static void setHashtable(LinkedList<Integer>[] hashtable) {
        EnglishWord.hashtable = hashtable;
    }
}