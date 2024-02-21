public class Assignment2Base extends TestBase {
  
  protected static abstract class Assignment2Test extends TestWithIO {

    protected Class<?> ReadingListPlanner;

    public Assignment2Test(String testname) {
      super(testname);
    }

    public Assignment2Test(String testname, String[] inputLines) {
      super(testname, inputLines);
    }

    protected void initialise() throws Exception {
      ClassLoader cl = getTestClassLoader();
      Class<?> c = cl.loadClass("ReadingListPlanner");
      this.ReadingListPlanner = c;
    }

  }
  
}
