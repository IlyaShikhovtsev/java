package ru.shikhovtsev.javathreadprogramming.сhapter5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VisualSuspendResume extends JPanel implements Runnable {

  private static final String[] symbolList = {"|", "/", "-", "\\", "|", "/", "-", "\\"};

  private Thread runThread;
  private JTextField symbolTF;

  public VisualSuspendResume() {
    symbolTF = new JTextField();
    symbolTF.setEditable(false);
    symbolTF.setFont(new Font("Monospaced", Font.BOLD, 26));
    symbolTF.setHorizontalAlignment(JTextField.CENTER);

    final JButton suspendB = new JButton("Suspend");
    final JButton resumeB = new JButton("Resume");
    suspendB.addActionListener(e -> suspendNow());
    resumeB.addActionListener(e -> resumeNow());

    JPanel innerStackP = new JPanel();
    innerStackP.setLayout(new GridLayout(0, 1, 3, 3));
    innerStackP.add(symbolTF);
    innerStackP.add(suspendB);
    innerStackP.add(resumeB);

    this.setLayout(new FlowLayout(FlowLayout.CENTER));
    this.add(innerStackP);
  }

  private void suspendNow() {
    if (runThread != null) {
      runThread.suspend();
    }
  }

  private void resumeNow() {
    if (runThread != null) {
      runThread.resume();
    }
  }

  public void run() {
    try {
      runThread = Thread.currentThread();
      int count = 0;
      while (true) {
        symbolTF.setText(symbolList[count % symbolList.length]);
        Thread.sleep(200);
        count++;
      }
    } catch (InterruptedException e) {

    } finally {
      runThread = null;
    }
  }

  public static void main(String[] args) {
    var vsr = new VisualSuspendResume();
    Thread t = new Thread(vsr);
    t.start();

    JFrame f = new JFrame("Visual Suspend Resume");
    f.setContentPane(vsr);
    f.setSize(320, 200);
    f.setVisible(true);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
  }
}
