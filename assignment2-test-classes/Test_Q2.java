import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Q2 of CS1812 CW Assignment 2
 * 
 * @author "Reuben Rowe <reuben.rowe@rhul.ac.uk>"
 */
public class Test_Q2 extends TestBase {
  
  public static void test01_LoadFile() {

    ReadingListItemStore s = null;
    try {
      s = new ReadingListItemStore("authors.txt");
    } catch (Exception e) {
      throw new TestFailure(failWithMsg("your code raised an exception!"), e);
    }

    assert (s != null)
      : failWithMsg(
          "could not construct ReadingListItemStore object from a file!"
      );

    System.out.println(testPassed());

  }

  public static void test02_LoadFileThenRetrieve() {

    ReadingListItemStore s = null;
    try {
      s = new ReadingListItemStore("authors.txt");
    } catch (Exception e) {
      throw new TestFailure(failWithMsg("your code raised an exception!"), e);
    }

    String item;
    
    item = s.getRandomItem("c");

    assert (item != null)
      : failWithMsg("item returned for key \"c\" is null!");

    assert (item.startsWith("C"))
      : failWithMsg(
          "expected getRandomItem(\"c\") to return a value starting with \"C\"!"
      );
  
    item = s.getRandomItem("h");

    assert (item != null)
      : failWithMsg("item returned for key \"h\" is null!");

    assert (item.startsWith("H"))
      : failWithMsg(
          "expected getRandomItem(\"h\") to return a value starting with \"H\"!"
      );
  
    System.out.println(testPassed());

  }

  public static void test03_MainStore_LoadFromFile_CaseInsensitive() {

    // Items to be loaded by the store
    List<String> items = new ArrayList<String>();
    items.add("alfred Tennyson");
    items.add("Blake Morrison");

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
      s = new ReadingListItemStore(filename);
    } catch (Exception e) {
      throw new TestFailure(failWithMsg("error creating temporary file"), e);
    }

    // Check that the items have been loaded from the file correctly
    for (String item : items) {
      StringBuilder key = new StringBuilder(item.substring(0, 1));
      if (Character.isLowerCase(key.charAt(0))) {
        key.setCharAt(0, Character.toUpperCase(key.charAt(0)));
      } else {
        key.setCharAt(0, Character.toLowerCase(key.charAt(0)));
      }
      String result = s.getRandomItem(key.toString());
      assert (result != null)
        : failWithMsg("item returned is null!");
      assert (result.startsWith(key.toString().toUpperCase()))
        : failWithMsg(
            "expected \"" + result + "\" to begin with \"" +
            key.toString().toUpperCase()  + "\""
        );
      assert (result.substring(1).equals(result.substring(1).toLowerCase()))
        : failWithMsg("expected item to have been converted to lowercase");
    }

    System.out.println(testPassed());

  }

  public static void test04_PrizeWinnerStore() {

    PrizeWinnerStore s = new PrizeWinnerStore();

    s.put("b", "Ben Okri");

    String result = s.getRandomItem("b");

    assert (result != null)
      : failWithMsg("item returned is null!");

    assert (result.trim().endsWith("(prize-winner)"))
      : failWithMsg(
          "expected returned item returned to end with \"(prize-winner)\"!"
      );

    System.out.println(testPassed());

  }

  public static void test05_BookStore() {

    List<String> items = new ArrayList<String>();
    List<String> suffixes = new ArrayList<String>();
    
    items.add("Alice's Adventures in Wonderland by Lewis Carroll 1865");
    suffixes.add("(classic)");

    items.add("Cold Comfort Farm by Stella Gibbons 1932");
    suffixes.add("(modern)");

    items.add("Snow Crash by Neal Stephenson 1992");
    suffixes.add("(contemporary)");

    BookStore s = new BookStore();

    for (String item : items) {
      s.put(item.substring(0, 1).toLowerCase(), item);
    }
    
    for (String item : items) {
      String key = item.substring(0, 1).toLowerCase();
      String result = s.getRandomItem(key);
      String suffix = suffixes.remove(0);

      assert (result != null)
        : failWithMsg("item returned for key \"" + key + "\" is null!");

      assert (result.trim().endsWith(suffix))
        : failWithMsg(
            "expected item returned for key \"" + key + "\"" +
            " to end with \"" + suffix + "\"!"
        );
    }

    System.out.println(testPassed());

  }

  public static void runAll() {
    test01_LoadFile();
    test02_LoadFileThenRetrieve();
    test03_MainStore_LoadFromFile_CaseInsensitive();
    test04_PrizeWinnerStore();
    test05_BookStore();
  }

  public static void main(String[] args) {
    runAll();
  }

}