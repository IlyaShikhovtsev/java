package mbeanserver.mbean;

public interface MyMBean {

  String getMyName();

  void setSomeValue(int value);

  int getSomeValue();

  void writeToConsole(String message);

  String concat(String str1, String str2);

}
