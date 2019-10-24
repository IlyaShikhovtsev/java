package mbeanserver;

import mbeanserver.mbean.My;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class App {

  public static void main(String[] args) throws Exception {
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ManagementFactory.getGarbageCollectorMXBeans();
    ObjectName name = new ObjectName("mbeanserver.mbean:type=My");
    My mbean = new My();
    mbs.registerMBean(mbean, name);

    System.out.println("waiting forever...");
    Thread.sleep(Long.MAX_VALUE);
  }
}
