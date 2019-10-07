package ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank;

public class WaterTank implements Tank {
  private float capacity;
  private float currentVolume = 0.0f;
  private WaterTank overflow;

  public WaterTank(float capacity) {
    this.capacity = capacity;
  }

  @Override
  public float getCapacity() {
    return 0;
  }

  @Override
  public float getVolume() {
    return 0;
  }

  @Override
  public void transferWater(float amount) throws OverflowException, UnderflowException {

  }
}
