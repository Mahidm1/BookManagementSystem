/**
 * Test class for Q1 of CS1812 CW Assignment 2
 * 
 * @author "Reuben Rowe <reuben.rowe@rhul.ac.uk>"
 */
public class Test_Q1 extends TestBase {
  
  public static void test01_MainStore_ContainsKey() {

    ReadingListItemStore s = new ReadingListItemStore();
    
    s.put("a", "Allen Ginsburg");
    s.put("be", "Beryl Bainbridge");

    assert (s.containsKey("a"))
      : failWithMsg("expected store to contain key \"a\"!");
    assert (s.containsKey("A"))
      : failWithMsg("expected store to contain key \"A\"!");
    assert (s.containsKey("be"))
      : failWithMsg("expected store to contain key \"be\"!");
    assert (s.containsKey("Be"))
      : failWithMsg("expected store to contain key \"Be\"!");
    assert (s.containsKey("bE"))
      : failWithMsg("expected store to contain key \"bE\"!");
    assert (s.containsKey("BE"))
      : failWithMsg("expected store to contain key \"BE\"!");
    assert (!s.containsKey("z"))
      : failWithMsg("expected store to not contain key \"z\"!");
    assert (!s.containsKey("Z"))
      : failWithMsg("expected store to not contain key \"Z\"!");

    System.out.println(testPassed());

  }

  public static void test02_MainStore_GetRandomItem() {

    ReadingListItemStore s = new ReadingListItemStore();
    
    s.put("a", "Allen Ginsburg");
    s.put("be", "Beryl Bainbridge");
    s.put("be", "Ben Jonson");
    s.put("a", "Anne Brontë");

    String item = s.getRandomItem("a");

    assert (item != null)
      : failWithMsg("item returned for key \"a\" is null!");
    
    assert (
      item.equalsIgnoreCase("Allen Ginsburg") ||
      item.equalsIgnoreCase("Anne Brontë")
    )
      : failWithMsg(
          "getRandomItem(\"a\") return incorrect value \"" + item + "\"!"
      );
    
    assert (item.startsWith("A"))
      : failWithMsg(
          "expected getRandomItem(\"a\") to return a value starting with \"A\"!"
      );

    item = s.getRandomItem("be");

    assert (item != null) 
      : failWithMsg("item returned for key \"be\" is null!");
    
    assert (
      item.equalsIgnoreCase("Beryl Bainbridge") ||
      item.equalsIgnoreCase("Ben Jonson")
    ) 
      : failWithMsg(
          "getRandomItem(\"be\") return incorrect value \"" + item + "\"!"
      );
    
    assert (item.startsWith("BE"))
      : failWithMsg(
          "expected getRandomItem(\"be\") to return a value starting with \"BE\"!" 
      );

    System.out.println(testPassed());

  }

  public static void test03_MainStore_GetRandomItem_CaseInsensitive() {

    ReadingListItemStore s1 = new ReadingListItemStore();
    s1.put("a", "Allen Ginsburg");
    ReadingListItemStore s2 = new ReadingListItemStore();
    s2.put("A", "Allen Ginsburg");

    String result;

    result = s1.getRandomItem("a");
    assert (result != null && result.equalsIgnoreCase("Allen Ginsburg"))
      : failWithMsg(
          "expected s1.getRandomItem(\"a\") to match \"Allen Ginsburg\"!"
      );

    result = s1.getRandomItem("A");
    assert (result != null && result.equalsIgnoreCase("Allen Ginsburg"))
      : failWithMsg(
          "expected s1.getRandomItem(\"A\") to match \"Allen Ginsburg\"!"
      );

    result = s2.getRandomItem("a");
    assert (result != null && result.equalsIgnoreCase("Allen Ginsburg"))
      : failWithMsg(
          "expected s2.getRandomItem(\"a\") to match \"Allen Ginsburg\"!"
      );

    result = s2.getRandomItem("A");
    assert (result != null && result.equalsIgnoreCase("Allen Ginsburg"))
      : failWithMsg(
          "expected s2.getRandomItem(\"A\") to match \"Allen Ginsburg\"!"
      );

    System.out.println(testPassed());

  }

  public static void test04_MainStore_NonExisting() {

    ReadingListItemStore s = new ReadingListItemStore();
    
    s.put("a", "Allen Ginsburg");
    s.put("be", "Beryl Bainbridge");
    s.put("be", "Ben Jonson");
    s.put("a", "Anne Brontë");

    assert (s.getRandomItem("z") == null)
      : failWithMsg("item returned for key \"z\" is not null!");
    assert (s.getRandomItem("Z") == null)
      : failWithMsg("item returned for key \"Z\" is not null!");

    System.out.println(testPassed());
  
  }

  public static void test05_MainStore_IsRandom() {

    ReadingListItemStore s = new ReadingListItemStore();
    
    s.put("a", "Allen Ginsburg");
    s.put("be", "Beryl Bainbridge");
    s.put("be", "Ben Jonson");
    s.put("a", "Anne Brontë");

    String result = s.getRandomItem("a");
    boolean random = false;
    for (int i = 0; i < 20; i++) {
      if (!s.getRandomItem("a").equals(result)) {
        random = true;
        break;
      }
    }

    // Chances we get same value 21 times is 1/1000000
    assert (random)
      : failWithMsg("output of getRandomItem does not appear to be random!");

    System.out.println(testPassed());

  }

  public static void test06_MainStoreTest() {
    // Just testing that class exists and has default constructor
    assert (new ReadingListItemStoreTest() != null)
      : failWithMsg("could not construct ReadingListItemStoreTest object!");
    System.out.println(testPassed());
  }

  public static void runAll() {
    test01_MainStore_ContainsKey();
    test02_MainStore_GetRandomItem();
    test03_MainStore_GetRandomItem_CaseInsensitive();
    test04_MainStore_NonExisting();
    test05_MainStore_IsRandom();
    test06_MainStoreTest();
  }

  public static void main(String[] args) {
    runAll();
  }

}