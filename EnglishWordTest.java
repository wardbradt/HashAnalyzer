import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Created by Ward Bradt, Nhat Pham, and Moe Sunami on 4/27/17.
 */
public class EnglishWordTest {
    @Test
    public void testHashCode(){
        EnglishWord hashTester = new EnglishWord("foobar");
        assertEquals(33, hashTester.hashCode());
    }

    @Test
    public void testBreakUp(){
        String test = "Hello my name is  Doctor     Z34ufelt and I like to eat bananas. () why cant. i, eat 568?";
        ArrayList<EnglishWord> breakUpTester = EnglishWord.breakUp(test);

        int count = 0;
        // Test that no words have whitespace
        for (EnglishWord i : breakUpTester) {
            assertTrue(i.getWord().indexOf(' ') < 0);
            count++;
        }

        // Check that it didn't skip over any words.
        assertEquals(16, count);

        // Check that there are less than or equal many buckets as words.
        int b = 0;
        while (EnglishWord.getHashtable()[b] != null) {
            b++;
        }
        assertTrue(count >= b);

        // Clear the hashtable for a new test.
        EnglishWord.setHashtable(new LinkedList[60]);
        String testTwo = "A hash table has sixty entries (buckets). Design and test a hash function for English words such that all the different words from this paragraph are hashed into the table with no more than four collisions. Do not call any hashCode methods.";
        breakUpTester = EnglishWord.breakUp(testTwo);
        // int i = 0;
        // iterate over the words in Dr. Z's string
        for (EnglishWord i : breakUpTester) {
            // Check that no EnglishWord from breakUp contains whitespace
            assertTrue(i.getWord().indexOf(' ') < 0);

            // Test that the hash code of every EnglishWord from breakUp is in hashTable.
            // The hash code of i
            int iHashed = i.hashCode();
            boolean inHashtable = false;
            for (int a = 0; a < EnglishWord.getHashtable().length; a++) {
                if (EnglishWord.getHashtable()[a].contains(iHashed)) {
                    inHashtable = true;
                    break;
                }
            }
            assertTrue(inHashtable);
        }
    }
}