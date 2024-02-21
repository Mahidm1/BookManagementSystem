import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

/**
 * Superclass for implementing CS1812 CW Assignment tests
 * 
 * @author "Reuben Rowe <reuben.rowe@rhul.ac.uk>"
 */
public class TestBase {

  // Duration of short timeout (in ms)
  public static final long SHORT_TIMEOUT = 1000;
  // Duration of long timeout (in ms)
  public static final long LONG_TIMEOUT = 5000;
  
  // Utility class for wrapping up a test case that needs to perform I/O.
  protected static abstract class TestWithIO extends Thread
      implements AutoCloseable, Thread.UncaughtExceptionHandler {

    private String testName;

    // Save standard output/input streams so we can restore them later
    protected final PrintStream stdOut = System.out;
    protected final PrintStream stdErr = System.err;
    protected final InputStream stdIn = System.in;

    // The streams we create to mock up the input/output
    ByteArrayOutputStream newOut = new ByteArrayOutputStream();
    ByteArrayOutputStream newErr = new ByteArrayOutputStream();
    TestInputStream newIn;

    // Class loader for the test
    protected ClassLoader loader;

    private enum ExitStatus {
      NORMAL,
      TIMEOUT,
      FAILED_ASSERTION,
      UNCAUGHT_EXCEPTION,
      INITIALISATION_ERROR;
    }

    private ExitStatus exitStatus = ExitStatus.NORMAL;

    private Throwable uncaughtException;

    private String[] inputLines;

    public TestWithIO(String testName) {
      this(testName, null);
    }

    public TestWithIO(String testName, String[] inputLines) {
      super();
      // Set the thread to be a daemon, so if the test code blocks, then the
      // main program will still exit
      this.setDaemon(true);
      this.setUncaughtExceptionHandler(this);
      this.testName = testName;
      this.inputLines = inputLines;
    }

    public String getTestName() {
      return this.testName;
    }

    public Throwable getUncaughtException() {
      return this.uncaughtException;
    }

    protected ClassLoader getTestClassLoader() {
      return this.loader;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
      if (e instanceof AssertionError) {
        this.exitStatus = ExitStatus.FAILED_ASSERTION;
      } else {
        this.exitStatus = ExitStatus.UNCAUGHT_EXCEPTION;
      }
      this.uncaughtException = e;
    }

    public void checkTimeout() {
      if (this.isAlive()) {
        this.exitStatus = ExitStatus.TIMEOUT;
      }
    }

    @Override
    public void start() {
      try {
        // Set up new class loader for the test
        String[] paths =
        System.getProperty("java.class.path").split(File.pathSeparator);
        URL[] urls = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
          urls[i] = new File(paths[i]).toURI().toURL();
        }
        ClassLoader parent = TestWithIO.class.getClassLoader().getParent();
        this.loader = new URLClassLoader(urls, parent);

        // Set up new streams to capture/provide different output/input
        System.setOut(new PrintStream(this.newOut));
        System.setErr(new PrintStream(this.newErr));
        this.newIn = new TestInputStream(inputLines);
        System.setIn(newIn);

        // Test initialisation
        this.initialise();

        // Start the test thread
        super.start();

        // Start the test input
        this.newIn.start();
      }
      catch (Exception e) {
        this.exitStatus = ExitStatus.INITIALISATION_ERROR;
        this.uncaughtException = e;
      }
    }

    protected abstract void initialise() throws Exception;

    protected abstract void runTest() throws Exception;

    @Override
    public final void run() {
      try {
        runTest();
      } catch (Exception e) {
        this.exitStatus = ExitStatus.UNCAUGHT_EXCEPTION;
        this.uncaughtException = e;
      }
    }

    public boolean exitedNormally() {
      return this.exitStatus == ExitStatus.NORMAL;
    }

    public boolean timedOut() {
      return this.exitStatus == ExitStatus.TIMEOUT;
    }

    public boolean failedAssertion() {
      return this.exitStatus == ExitStatus.FAILED_ASSERTION;
    }

    public String getOutputStreamContents() {
      return this.newOut.toString();
    }

    public String[] getOutputStreamLines() {
      return getOutputStreamContents().split("\\R", -1);
    }

    /**
     * Returns an array of the output lines minus any leading or trailing blank
     * lines.
     */
    public String[] getTrimmedOutputStreamLines() {
      String[] output = getOutputStreamLines();
      List<String> trimmed = new ArrayList<String>(output.length);
      Deque<String> queue = new ArrayDeque<String>(output.length);
      boolean input_start = false;
      boolean input_end = false;
      for (String line : output) {
        boolean empty = line.length() == 0;
        if (!empty)
          input_start = true;
        if (input_start && empty)
          input_end = true;
        if (input_end && empty)
          queue.add(line);
        if (input_end && !empty) {
          trimmed.addAll(queue);
          queue.clear();
          input_end = false;
        }
        if (input_start && !empty)
          trimmed.add(line);
      }
      return trimmed.toArray(new String[] {});
    }

    public String getErrorStreamContents() {
      return this.newErr.toString();
    }

    public String[] getErrorStreamLines() {
      return this.getErrorStreamContents().split("\\R", -1);
    }

    public String getOutput() {
      return getOutput(null);
    }

    public String getOutput(String prefix) {
      if (prefix == null) {
        return 
          String.join(System.lineSeparator(),
                        "Standard output of program:",
                        this.newOut.toString(),
                        "Standard error output of program:",
                        this.newErr.toString(),
                        "End of program output");
      } else {
        return 
          String.join(System.lineSeparator(),
                        prefix, 
                        "Standard output of program:",
                        this.newOut.toString(),
                        "Standard error output of program:",
                        this.newErr.toString(),
                        "End of program output");
      }
    }

    public String failWithMsg(String msg) {
      StringBuilder sb = new StringBuilder();
      sb.append("Test ");
      sb.append(this.getTestName());
      sb.append(" failed: ");
      sb.append(msg);
      return sb.toString();
    }

    public String errorMsg(String prefix) {
      StringBuilder errStr = new StringBuilder(prefix);
      if (this.uncaughtException != null) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(os);
        p.println(this.uncaughtException.getMessage());
        this.uncaughtException.printStackTrace(p);
        p.close();
        errStr.append(os.toString());
      }
      return String.join(System.lineSeparator(), errStr, getOutput());
    }

    public String failWithErrorMsg(String prefix) {
      return this.errorMsg(this.failWithMsg(prefix));
    }

    private void restoreStandardStreams() {
      System.setOut(stdOut);
      System.setErr(stdErr);
      System.setIn(stdIn);
    }

    public void close() throws IOException {
      restoreStandardStreams();
      this.newOut.close();
      this.newErr.close();
      if (this.newIn != null) {
        this.newIn.close();
      }
    }

  }

  protected static class TestFailure extends RuntimeException {
    public TestFailure(String msg) {
      super(msg);
    }
    public TestFailure(String msg, Throwable cause) {
      super(msg, cause);
    }
  }

  protected static void runTestWithIO(TestWithIO t, long timeout) {
    try (t) {
      // Start the test
      t.start();
      // Wait for the test to finish
      t.join(timeout);
      t.checkTimeout();
    }
    catch (InterruptedException | IOException e) {
      // This shouldn't happen, so just bubble up
      throw new RuntimeException(e);
    }

    if (t.timedOut()) {
      throw new TestFailure(
          t.getOutput("Test \"" + t.getTestName() + "\" timed out!")
        );
    } else if (t.failedAssertion()) {
      throw new TestFailure(
          t.getOutput("Test \"" + t.getTestName() + "\" failed!"),
          t.getUncaughtException()
        );
    } else if (!t.exitedNormally()) {
      throw new TestFailure(
          t.getOutput("Test \"" + t.getTestName() + "\" caught an exception!"),
          t.getUncaughtException()
        );
    }
  }

  private static String getMethodName(int lvl) {
    Optional<StackWalker.StackFrame> f = 
      StackWalker.getInstance().walk(s -> s.skip(lvl).findFirst());
    if (!f.isPresent()) return null;
    return f.get().getMethodName();
  }

  /**
   * Method to be called from directly withing test methods to return the name
   * of the method as a test name. This relies on reflection to walk one frame
   * up the stack and get the method name.
   */
  protected static String getTestName() {
    return getMethodName(2);
  }

  protected static String failWithMsg(String msg) {
    StringBuilder sb = new StringBuilder();
    sb.append("Test \"");
    sb.append(getMethodName(2));
    sb.append("\" failed: ");
    sb.append(msg);
    return sb.toString();
  }

  protected static String testPassed() {
    StringBuilder sb = new StringBuilder();
    sb.append("Test \"");
    sb.append(getMethodName(2));
    sb.append("\" passed");
    return sb.toString();
  }

}

class TestInputStream extends PipedInputStream implements Runnable {
  
  public static final long DEFAULT_DELAY = 75;

  private PrintWriter feeder;
  private String[] lines;
  private long delay;
  
  public TestInputStream(String[] lines, long delay) throws IOException {
    this.feeder = new PrintWriter(new PipedOutputStream(this), true);
    this.delay = delay;
    this.lines = lines;
  }

  public TestInputStream(String[] lines) throws IOException {
    this(lines, DEFAULT_DELAY);
  }

  public void start() {
    (new Thread(this)).start();
  }

  @Override public void run() {
    if (this.lines == null) return;
    try {
      for (String line : this.lines) {
        Thread.sleep(this.delay);
        this.feeder.println(line);
      }
    }
    catch (InterruptedException e) {}
    finally {
      if (this.feeder != null) {
        this.feeder.close();
        this.feeder = null;
      }
    }
  }

}