package notifications;

import mbeanserver.mbean.My;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class App {

  public static void main(String[] args) throws Exception {
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ManagementFactory.getGarbageCollectorMXBeans();
    ObjectName name = new ObjectName("notifications:type=Hello");
    Hello mbean = new Hello();
    mbs.registerMBean(mbean, name);

    System.out.println("waiting forever...");
    Thread.sleep(Long.MAX_VALUE);
  }
}
