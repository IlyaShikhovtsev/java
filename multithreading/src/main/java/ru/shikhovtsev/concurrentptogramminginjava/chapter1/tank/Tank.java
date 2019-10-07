package ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank;

public interface Tank {
  float getCapacity();
  float getVolume();
  void transferWater(float amount) throws OverflowException, UnderflowException;
}
