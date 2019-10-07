package ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.templatemethods;

import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.OverflowException;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.UnderflowException;

public class ConcreteTank extends AbstractTank {
  protected final float capacity;
  protected float volume;

  public ConcreteTank(float capacity) {
    this.capacity = capacity;
  }

  @Override
  protected void doTransferWater(float amount) throws OverflowException, UnderflowException {
    //impl code
  }

  @Override
  public float getCapacity() {
    return capacity;
  }

  @Override
  public float getVolume() {
    return volume;
  }
}
