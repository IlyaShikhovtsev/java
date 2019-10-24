package gcmbean;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class App {

  public static void main(String[] args) {
    List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();

    for (GarbageCollectorMXBean gc : garbageCollectorMXBeans) {
      NotificationEmitter emitter = (NotificationEmitter) gc;
      System.out.println(gc.getName());

      NotificationListener listener = ((notification, handback) -> {
        if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
          var info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

          long duration = info.getGcInfo().getDuration();
          String gcType = info.getGcAction();

          System.out.println(gcType + ": - " +
              info.getGcInfo().getId() + ", " +
              info.getGcName() +
              " (from " + info.getGcCause() + ") " + duration + " milliseconds");
        }
      });

      emitter.addNotificationListener(listener, null, null);
    }

    run();
  }

  private static void run() {
    while (true) {
      Object[] array = new Object[1_000_000];
    }
  }
}
