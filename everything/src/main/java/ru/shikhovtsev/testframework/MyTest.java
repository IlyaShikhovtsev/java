package ru.shikhovtsev.testframework;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MyTest {

  @Test
  public void method() {
    System.out.println("First test!!!");
  }

  @Test
  public void method2() {
    System.out.println("Second test!!!");
  }

  @Test
  public void checkedException() throws Exception {
    throw new Exception("exception");
  }

  @Test
  public void uncheckedException() {
    throw new RuntimeException("runtime");
  }

  public static void main(String[] args) throws Exception {
    findTestMethods(MyTest.class);
    System.out.println(Arrays.toString(MyTest.class.getClassLoader().getDefinedPackages()));
    System.out.println(MyTest.class.getPackageName());
  }

  private static void findTestMethods(Class<?> clazz) throws Exception {
    Method[] methods = clazz.getMethods();

    MyTest instance = MyTest.class.getDeclaredConstructor().newInstance();

    for (int i = 0; i < methods.length; i++) {
      if (methods[i].isAnnotationPresent(Test.class)) {
        try {
          methods[i].invoke(instance);
        } catch (Exception e) {
          e.getCause().printStackTrace();
        }
      }
    }
  }
}
