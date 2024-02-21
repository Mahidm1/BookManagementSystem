/**
 * Test class for Q3 of CS1812 CW Assignment 2
 * 
 * @author "Reuben Rowe <reuben.rowe@rhul.ac.uk>"
 */
public class Test_Q3 extends Assignment2Base {
  
  public static void test01_Uppercase() {

    final String input = "JAVAOOP";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner.getMethod("main", String[].class)
                            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    StringBuilder sb = new StringBuilder();
    for (String line : t.getTrimmedOutputStreamLines()) {
      assert (line.length() > 0)
        : t.failWithErrorMsg("unexpected blank line in output!");
      assert (Character.isUpperCase(line.charAt(0)))
        : t.failWithErrorMsg(
            "expected all lines to begin with an uppercase character!"
        );
      sb.append(line.charAt(0));
    }

    assert (sb.toString().equalsIgnoreCase(input))
      : t.failWithErrorMsg(
          "initial characters of the output do not spell input word!"
      );
    
    System.out.println(testPassed());

  }

  public static void test02_Lowercase() {

    final String input = "javaoop";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner.getMethod("main", String[].class)
                            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    StringBuilder sb = new StringBuilder();
    for (String line : t.getTrimmedOutputStreamLines()) {
      assert (line.length() > 0)
        : t.failWithErrorMsg("unexpected blank line in output!");
      assert (Character.isUpperCase(line.charAt(0)))
        : t.failWithErrorMsg(
            "expected all lines to begin with an uppercase character!"
        );
      sb.append(line.charAt(0));
    }

    assert (sb.toString().equalsIgnoreCase(input))
      : t.failWithErrorMsg(
          "initial characters of the output do not spell input word!"
      );
    
    System.out.println(testPassed());

  }

  public static void test03_Mixed_Case() {

    final String input = "jAVaOOp";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner.getMethod("main", String[].class)
                            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    StringBuilder sb = new StringBuilder();
    for (String line : t.getTrimmedOutputStreamLines()) {
      assert (line.length() > 0)
        : t.failWithErrorMsg("unexpected blank line in output!");
      assert (Character.isUpperCase(line.charAt(0)))
        : t.failWithErrorMsg(
            "expected all lines to begin with an uppercase character!"
        );
      sb.append(line.charAt(0));
    }

    assert (sb.toString().equalsIgnoreCase(input))
      : t.failWithErrorMsg(
          "initial characters of the output do not spell input word!"
      );

    System.out.println(testPassed());

  }

  private static void validateSuggestionFormat(TestWithIO t) {

    int num_authors = 0;
    int num_prizes = 0;
    int num_books = 0;

    String[] output = t.getTrimmedOutputStreamLines();

    assert (output.length > 0) 
      : t.failWithMsg("no ouput!");

    for (String line : output) {
      assert (line.length() > 0)
        : t.failWithErrorMsg("unexpected blank line in output!");
      if (line.contains("prize-winner")) {
        num_prizes++;
      } else if (line.contains("classic") || 
                 line.contains("modern") ||
                 line.contains("contemporary")) {
        num_books++;
      } else {
        num_authors++;
      }
    }

    assert (num_authors > 0)
      : t.failWithErrorMsg("output does not contain any authors!");

    assert (output.length == 1 || num_prizes > 0)
      : t.failWithErrorMsg(
          "output contains more than one item but contains no prize winners!"
      );

    assert (output.length <=2 || num_books > 0)
      : t.failWithErrorMsg(
            "output contains more than two items but contains no books!"
      );

    assert (num_prizes >= num_books)
      : t.failWithErrorMsg("output contains more books than prize winners!");

  }

  public static void test04_Suggestion_Format_One_Character() {

    final String input = "M";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner
            .getMethod("main", String[].class)
            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    validateSuggestionFormat(t);

    System.out.println(testPassed());

  }

  public static void test05_Suggestion_Format_Two_Character() {

    final String input = "ER";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner
            .getMethod("main", String[].class)
            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    validateSuggestionFormat(t);

    System.out.println(testPassed());

  }

  public static void test06_Suggestion_Format_Three_Character() {

    final String input = "PWS";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner
            .getMethod("main", String[].class)
            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    validateSuggestionFormat(t);

    System.out.println(testPassed());

  }

  public static void test07_Suggestion_Format_Four_Character() {

    final String input = "AOCE";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner
            .getMethod("main", String[].class)
            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    validateSuggestionFormat(t);

    System.out.println(testPassed());

  }

  public static void test08_Suggestion_Format_Ten_Character() {

    final String input = "DOEMCYZOWP";

    TestWithIO t = new Assignment2Test(getTestName()) {
        @Override
        public void runTest() throws ReflectiveOperationException {
          ReadingListPlanner
            .getMethod("main", String[].class)
            .invoke(null, ((Object) new String[] { input }));
        }
      };
    
    runTestWithIO(t, SHORT_TIMEOUT);

    validateSuggestionFormat(t);

    System.out.println(testPassed());

  }

  public static void runAll() {
    test01_Uppercase();
    test02_Lowercase();
    test03_Mixed_Case();
    test04_Suggestion_Format_One_Character();
    test05_Suggestion_Format_Two_Character();
    test06_Suggestion_Format_Three_Character();
    test07_Suggestion_Format_Four_Character();
    test08_Suggestion_Format_Ten_Character();
  }

  public static void main(String[] args) {
    runAll();
  }
  
}