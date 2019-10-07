package ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.subclassing;

import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.AssertionError;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.OverflowException;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.UnderflowException;
import ru.shikhovtsev.concurrentptogramminginjava.chapter1.tank.WaterTank;

public class SubclassedTank extends WaterTank {
  public SubclassedTank(float capacity) {
    super(capacity);
  }

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
      super.transferWater(amount);
    } catch (OverflowException | UnderflowException e) {
      throw e;
    } finally {
      checkVolumeInvariant();
    }
  }
}
