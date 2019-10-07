package ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.templatemethods;

import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.AssertionError;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.OverflowException;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.Tank;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.UnderflowException;

public abstract class AbstractTank implements Tank {

  protected void checkVolumeInvariant() throws AssertionError {
    float v = getVolume();
    float c = getCapacity();
    if (!(v >= 0.0 && v <= c)) {
      throw new AssertionError();
    }
  }

  @Override
  public void transferWater(float amount) throws OverflowException, UnderflowException {
    checkVolumeInvariant();

    try {
      doTransferWater(amount);
    } catch (UnderflowException | OverflowException e) {
      throw e;
    } finally {
      checkVolumeInvariant();
    }
  }

  protected abstract void doTransferWater(float amount) throws OverflowException, UnderflowException;
}
