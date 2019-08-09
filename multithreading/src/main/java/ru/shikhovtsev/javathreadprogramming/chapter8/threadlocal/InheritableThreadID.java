package ru.shikhovtsev.javathreadprogramming.chapter8.threadlocal;

public class InheritableThreadID {
  public static final int UNIQUE = 101;
  public static final int INHERIT = 102;
  public static final int SUFFIX = 103;

  private ThreadLocal<String> threadLocal;
  private int nextId;

  public InheritableThreadID(int type) {
    nextId = 201;

    switch (type) {
      case UNIQUE:
        threadLocal = ThreadLocal.withInitial(() -> {
          print("in initialValue()");
          return getNewID();
        });
        break;
      case INHERIT:
        threadLocal = new InheritableThreadLocal<>() {
          @Override
          protected String initialValue() {
            print("in initialValue()");
            return getNewID();
          }
        };
        break;
      case SUFFIX:
        threadLocal = new InheritableThreadLocal<>() {
          @Override
          protected String initialValue() {
            print("in initialValue()");
            return getNewID();
          }

          @Override
          protected String childValue(String parentValue) {
            print("in childValue() - " + "parentValue=" + parentValue);
            return parentValue + "-CH";
          }
        };
        break;
      default:
        break;
    }
  }

  private synchronized String getNewID() {
    return "ID" + nextId++;
  }

  private String getID() {
    return threadLocal.get();
  }

  public static Runnable createTarget(InheritableThreadID id) {
    final InheritableThreadID var = id;

    return () -> {
      print("var.getID()=" + var.getID());

      Runnable childRun = () -> print("var.getID()=" + var.getID());

      Thread parentT = Thread.currentThread();
      String parentName = parentT.getName();
      print("creating a child thread of " + parentName);
      Thread childT = new Thread(childRun, parentName + "-child");
      childT.start();
    };
  }

  public static void main(String[] args) {
    try {
      System.out.println("===========ThreadLocal============");

      var varA = new InheritableThreadID(UNIQUE);

      Runnable targetA = createTarget(varA);
      Thread threadA = new Thread(targetA, "threadA");
      threadA.start();

      Thread.sleep(2500);
      System.out.println("\n========= InherThreadLocal==========");

      InheritableThreadID varB = new InheritableThreadID(INHERIT);

      Runnable targerB = createTarget(varB);
      Thread threadB = new Thread(targerB, "threadB");
      threadB.start();

      Thread.sleep(2500);
      System.out.println("\n=================InheritThreadLocal - custom childValue()=========");
      var varC = new InheritableThreadID(SUFFIX);

      Runnable targerC = createTarget(varC);
      Thread threadC = new Thread(targerC, "threadC");
      threadC.start();
    } catch (InterruptedException e) {

    }
  }

  private static void print(String msg) {
    String name = Thread.currentThread().getName();
    System.out.println(name + ": " + msg);
  }
}
