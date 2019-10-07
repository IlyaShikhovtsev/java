package ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.adapter;

import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.AssertionError;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.OverflowException;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.Tank;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.UnderflowException;

public class AdaptedTank implements Tank {
  protected final Tank delegate;

  public AdaptedTank(Tank t) {
    delegate = t;
  }

  public float getCapacity() {
    return delegate.getCapacity();
  }

  public float getVolume() {
    return delegate.getVolume();
  }

  protected void checkVolumeInvariant() throws AssertionError {
    float v = getVolume();
    float c = getCapacity();
    if (!(v >= 0.0 && v <= c)) {
      throw new AssertionError();
    }
  }

  @Override
  public void transferWater(float amount) throws UnderflowException, OverflowException {
    checkVolumeInvariant();

    try {
      delegate.transferWater(amount);
    } catch (OverflowException | UnderflowException e) {
      throw e;
    } finally {
      checkVolumeInvariant();
    }
  }
}
