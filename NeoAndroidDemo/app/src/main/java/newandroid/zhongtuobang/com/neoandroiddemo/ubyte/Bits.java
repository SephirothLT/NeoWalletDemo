package newandroid.zhongtuobang.com.neoandroiddemo.ubyte;

/**
 * Individual bits in a (unsigned) byte, starting with the right most bit as bit zero and the left most as bit 7
 * <p>
 * Copyright [2015] [Alexander Koch]
 * </p>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * </p>
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
public enum Bits {

  BIT_0(0b00000001),
  BIT_1(0b00000010),
  BIT_2(0b00000100),
  BIT_3(0b00001000),
  BIT_4(0b00010000),
  BIT_5(0b00100000),
  BIT_6(0b01000000),
  BIT_7(0b10000000),;

  private final int mask;

  Bits(int mask) {
    this.mask = mask;
  }

  /**
   * Returns the bit mask as an int
   *
   * @return the bit mask
   */
  public int getMask() {
    return mask;
  }

  /**
   * Returns the bit for the given number, i.e getBit(1) will return BIT_1
   *
   * @param bitNumber the bit to return
   * @return the bit for the given number
   */
  public static Bits getBit(int bitNumber) {
    switch (bitNumber) {
      case 0:
        return BIT_0;
      case 1:
        return BIT_1;
      case 2:
        return BIT_2;
      case 3:
        return BIT_3;
      case 4:
        return BIT_4;
      case 5:
        return BIT_5;
      case 6:
        return BIT_6;
      case 7:
        return BIT_7;
      default:
        throw new IllegalArgumentException(String.format("A bit with the number %s is not allowed, it must be in the range 0-7", bitNumber));
    }
  }

}
