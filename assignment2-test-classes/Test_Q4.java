import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Q4 of CS1812 CW Assignment 2
 * 
 * @author "Reuben Rowe <reuben.rowe@rhul.ac.uk>"
 */
public class Test_Q4 extends Assignment2Base {

  public static void test01_LongPrefix() {

    // Prefix length to use
    int prefix_length = 4;

    // Items to be loaded by the store
    List<String> items = new ArrayList<String>();
    items.add("Maeve Binchy");
    items.add("Chimamanda Ngozi Adichie");

    // Create temporary file containing these items
    String filename = null;
    try {
      File f = File.createTempFile(getTestName(), null);
      try (PrintWriter w = new PrintWriter(f)) {
        for (String item : items) {
          w.println(item);
        }
      }
      filename = f.getCanonicalPath();
    }
    catch (IOException e) {
      throw new TestFailure(failWithMsg("error creating temporary file"), e);
    }

    // Create a store object that loads the temporary file
    ReadingListItemStore s = null;
    try {
      s = new ReadingListItemStore(filename, prefix_length);
    } catch (Exception e) {
      throw new TestFailure(failWithMsg("your code raised an exception!"), e);
    }

    // Check that the items have been loaded from the file correctly
    for (String item : items) {
      String key = item.substring(0, prefix_length);
      String result = s.getRandomItem(key);
      assert (item.equalsIgnoreCase(result))
        : failWithMsg(
            "item returned was \"" + result + "\" " +
            " but expected \"" + item + "\"!"
        );
      for (char c : result.substring(0, prefix_length).toCharArray()) {
        assert (Character.isUpperCase(c))
          : failWithMsg(
              "expected first " + prefix_length + " characters of result " +
              "\"" + result + "\" to be upper case!"
          );
      }
    }

    System.out.println(testPassed());

  }

  public static void test02_ShortLongPrefix() {

    // Prefix length to use
    int prefix_length = 3;

    // Items to be loaded by the store
    List<String> items = new ArrayList<String>();
    items.add("Maeve Binchy");
    items.add("Chimamanda Ngozi Adichie");

    // Create temporary file containing these items
    String filename = null;
    try {
      File f = File.createTempFile(getTestName(), null);
      try (PrintWriter w = new PrintWriter(f)) {
        for (String item : items) {
          w.println(item);
        }
      }
      filename = f.getCanonicalPath();
    }
    catch (IOException e) {
      throw new TestFailure(failWithMsg("error creating temporary file"), e);
    }

    // Create a store object that loads the temporary file
    ReadingListItemStore s = null;
    try {
      s = new ReadingListItemStore(filename, prefix_length + 2);
    } catch (Exception e) {
      throw new TestFailure(failWithMsg("your code raised an exception!"), e);
    }

    // Check that the items have been loaded from the file correctly
    for (String item : items) {
      for (int i = 1; i <= prefix_length; i++) {
        String key = item.substring(0, i);
        String result = s.getRandomItem(key);
        assert (item.equalsIgnoreCase(result))
          : failWithMsg(
              "item returned was \"" + result + "\" " +
              " but expected \"" + item + "\"!"
          );
        for (char c : result.substring(0, i).toCharArray()) {
          assert (Character.isUpperCase(c))
            : failWithMsg(
                "expected first " + i + " characters of result " +
                "\"" + result + "\" to be upper case!"
            );
        }
      }
    }

    System.out.println(testPassed());

  }

  public static void test03_MultiCharacter() {

    // each of the given lists has something beginning "th"
    // 20 characters makes failure 1/million, right?
    int reps = 10;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < reps; i++)
      sb.append("th");
    final String input = sb.toString();

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner
            .getMethod("main", String[].class)
            .invoke(null, ((Object) new String[] { input, "2" }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);
    
    String[] output = t.getTrimmedOutputStreamLines();
    
    StringBuilder ouputWord = new StringBuilder();
    for (String line : output) {
      assert (line.length() > 0)
        : t.failWithErrorMsg("unexpected blank line in output!");
      assert (
          line.substring(0, 1).equalsIgnoreCase("t") ||
          line.substring(0, 1).equalsIgnoreCase("h") || 
          line.substring(0, 2).equalsIgnoreCase("th")
        ) 
        : t.failWithErrorMsg(
              "expecting all items to begin with \"t\", \"h\", or \"th\"!"
        );
      assert (line.substring(2).equals(line.substring(2).toLowerCase()))
        : t.failWithErrorMsg(
              "expecting all items in the output to be in lowercase " +
              "after the first two characters!"
        );
      assert (line.length() >= 2)
        : t.failWithErrorMsg(
              "expected all items in the output to contain at least two characters!"
        );
    }

    assert (output.length >= reps && output.length != input.length())
      : t.failWithErrorMsg(
            "expected output to contain some multi-character prefixes!"
      );

    System.out.println(testPassed());

  }

  public static void runAll() {
    test01_LongPrefix();
    test02_ShortLongPrefix();
    test03_MultiCharacter();
  }

  public static void main(String[] args) {
    runAll();
  }

}