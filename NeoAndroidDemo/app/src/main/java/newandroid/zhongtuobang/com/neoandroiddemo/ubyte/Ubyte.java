package newandroid.zhongtuobang.com.neoandroiddemo.ubyte;

import java.nio.charset.Charset;

/**
 * Provides utilities for working with unsigned byte values
 * <p>
 * The Java language does not provide a type for unsigned byte
 * values. It is easier to work with signed int values treating
 * them as to be unsigned byte. The JVM uses 32 bit values anyway
 * to store a byte, so no space is saved with using byte.
 * </p>
 * <p>
 * In addition to conversion method to and from signed byte to
 * an int holding unsigned byte, this class provides formatting
 * of bytes, double bytes or quadruple bytes, and methods
 * for setting  testing bit flags or storing numeric values
 * into a byte at a given position
 * </p>
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
public final class Ubyte {

  private static final int BYTE_MASK = 0x000000ff;
  private static final int DOUBLE_BYTE_MASK = 0x0000ffff;

  private static final short BYTE_LENGTH = 8;

  private static final String HEX_STRING_PREFIX = "0x";
  private static final String HEX_PADDING_FOR_BYTE = "00";
  private static final String HEX_PADDING_FOR_DOUBLE_BYTE = "0000";
  private static final String HEX_PADDING_FOR_QUAD_BYTE = "00000000";

  private static final String ARRAY_START = "{ ";
  private static final String ARRAY_END = " }";
  private static final String ARRAY_SEPARATOR = ", ";

  private Ubyte() {
    // hide
  }

  /**
   * Converts the argument to an unsigned byte stored in an int
   * <p>
   * The argument is converted using only the low-order byte. All the bits in the
   * other bytes i.e. high-order 24 bits, are zero.
   * </p>
   *
   * @param intValue the value to convert to an unsigned byte
   * @return the argument converted to an int holding the unsigned byte value
   */
  public static int toUnsignedByte(final int intValue) {
    return intValue & BYTE_MASK;
  }

  /**
   * Returns true if the given int would be a valid unsigned byte
   * <p>
   * The int value is a valid unsigned byte, if it is in the range from 0 to 256
   * </p>
   *
   * @param intValue the int to test
   * @return true if the int is an unsigned byte, otherwise false
   */
  public static boolean isUnsignedByte(final int intValue) {
    return intValue >= 0x00 && intValue <= 0xff;
  }

  /**
   * Converts the argument to a signed byte
   * <p>
   * The given int value is a converted to a signed byte
   * </p>
   *
   * @param intValue the int value to convert
   * @return a signed byte
   */
  public static byte toSignedByte(final int intValue) {
    return (byte) ((intValue << 24) >> 24);
  }


  /**
   * Returns the lowest byte, treating the given intValue as a two byte value
   * <p>
   * An int is returned, holding "low byte", i.e. the least significant byte from a passed int. The int is treated
   * as a two byte value. The low byte is made up from the rightmost eight bits
   * </p>
   *
   * @param intValue the int from which to extract the low byte
   * @return the low byte
   */
  public static int getLowByteFromDoubleByte(final int intValue) {
    return intValue & BYTE_MASK;
  }

  /**
   * Returns the highest byte, treating the given intValue as a two byte value
   * <p>
   * An int is returned, holding "high byte", i.e. the most significant byte from a passed int. The int is treated
   * as a two byte value. The high byte is made up from the bits 9-16 counted from the right (and starting with one)
   * </p>
   *
   * @param intValue the int from which to extract the high byte
   * @return the high byte
   */
  public static int getHighByteFromDoubleByte(final int intValue) {
    return (intValue >>> BYTE_LENGTH) & BYTE_MASK;
  }

  /**
   * Combines two unsigned bytes into a two byte value stored in an int
   * <p>
   * Uses big endian, and the most significant byte will be the high byte
   * </p>
   *
   * @param mostSignificantByte  the high byte
   * @param leastSignificantByte the low byte
   * @return the two byte value
   */
  public static int combineTwoBytes(final int mostSignificantByte, final int leastSignificantByte) {
    return (mostSignificantByte << BYTE_LENGTH) + leastSignificantByte;
  }

  /**
   * Return a array of ints, containing four unsigned byte values, each one of the bytes of four byte value
   *
   * @param longValue the unsigned four byte value
   * @return the int array of four bytes
   */
  public static int[] getBytesFromQuadByte(final long longValue) {
    int[] intArray = new int[4];

    intArray[0] = (int) ((longValue >>> 3 * BYTE_LENGTH) & BYTE_MASK);
    intArray[1] = (int) ((longValue >>> 2 * BYTE_LENGTH) & BYTE_MASK);
    intArray[2] = (int) ((longValue >>> BYTE_LENGTH) & BYTE_MASK);
    intArray[3] = (int) (longValue & BYTE_MASK);

    return intArray;
  }

  /**
   * Combines four unsigned bytes into a four byte value stored in a long
   * <p>
   * Uses big endian, and the most significant byte will be the high byte
   * </p>
   *
   * @param mostSignificantByte       the high byte
   * @param secondMostSignificantByte the byte after the high byte
   * @param thirdMostSignificantByte  the byte before the low byte
   * @param leastSignificantByte      the low byte
   * @return the two byte value
   */
  public static long combineFourBytes(final int mostSignificantByte,
                                      final int secondMostSignificantByte,
                                      final int thirdMostSignificantByte,
                                      final int leastSignificantByte) {
    return (mostSignificantByte << BYTE_LENGTH * 3)
        + (secondMostSignificantByte << BYTE_LENGTH * 2)
        + (thirdMostSignificantByte << BYTE_LENGTH)
        + leastSignificantByte;
  }

  /**
   * Returns a hexadecimal representation of the unsigned byte value
   * <p>
   * The String is prefixed with 0x and followed by two digits, padded with zero if necessary.
   * The given int value is a treated as an unsigned byte using only the rightmost byte. All bits in the
   * other bytes are ignored. Characters are all lowercase
   * </p>
   *
   * @param intValue the int holding the unsigned byte
   * @return the formatted hex string
   */
  public static String formatByteAsHex(final int intValue) {
    return formatAsHex(toUnsignedByte(intValue), HEX_PADDING_FOR_BYTE);
  }

  private static String formatAsHex(final long value, final String padding) {
    String hexString = Long.toHexString(value).toLowerCase();
    return HEX_STRING_PREFIX + (padding + hexString).substring(hexString.length());
  }

  /**
   * Returns a hexadecimal representation of an int which is assumed to be a two byte value
   * <p>
   * The String is prefixed with 0x and followed by four digits, padded with zero if necessary.
   * The given int value is a treated as an unsigned two byte value using only the two rightmost bytes. All bits in the
   * other bytes are ignored. Characters are all lowercase
   * </p>
   *
   * @param intValue the int holding the two unsigned bytes
   * @return the formatted hex string
   */
  public static String formatDoubleByteAsHex(final int intValue) {
    return formatAsHex(intValue & DOUBLE_BYTE_MASK, HEX_PADDING_FOR_DOUBLE_BYTE);
  }

  /**
   * Returns a hexadecimal representation of a long which is assumed to be a four byte value
   * <p>
   * The String is prefixed with 0x and followed by eight digits, padded with zero if necessary.
   * The given long value is a treated as an unsigned four byte value using only the two rightmost bytes. All bits in the
   * other bytes are ignored. Characters are all lowercase
   * </p>
   *
   * @param longValue the long holding the long unsigned bytes
   * @return the formatted hex string
   */
  public static String formatQuadByteAsHex(final long longValue) {
    return formatAsHex(longValue, HEX_PADDING_FOR_QUAD_BYTE);
  }

  /**
   * Tests whether the bit is set in the given int value
   *
   * @param intValue the value to test
   * @param bit      the bit
   * @return true if the bit is set in the given int
   */
  public static boolean bitIsSet(final int intValue, final Bits bit) {
    return (intValue & bit.getMask()) == bit.getMask();
  }

  /**
   * Sets a bit depending on a flag (boolean value)
   *
   * @param intValue  the value in which to set the bit
   * @param bit       the bit to set
   * @param condition if true the bit is set (to one), otherwise unset (to zero)
   * @return the value with the bit set accordingly
   */
  public static int setFlag(final int intValue, Bits bit, final boolean condition) {
    if (condition) {
      return intValue | bit.getMask();
    } else {
      return intValue & ~bit.getMask();
    }
  }

  /**
   * Sets a bit to 1
   *
   * @param intValue the value in which to set the bit
   * @param bit      the bit to set
   * @return the value with the bit set
   */
  public static int setBit(final int intValue, final Bits bit) {
    return setFlag(intValue, bit, true);
  }

  /**
   * Stores a unsigned byte into another unsigned byte by using a given mask
   * <p>
   * The mask is scanned from right to left and every time a bit is set, the corresponding bit in the target
   * is set with the bits from source, one after the other from right to left
   * </p>
   *
   * @param template the initial value to set the source into
   * @param mask   the mask to use, to decide which bit in the target to set
   * @param source the source to take the bits from
   * @return the resulting value with the source stored under mask into the template
   */
  public static int storeUnderMask(final int template, final int mask, final int source) {
    int sourcePosition = 0;
    int target = template;
    for (int targetPosition = 0; targetPosition < BYTE_LENGTH; targetPosition++) {
      if (bitIsSet(mask, Bits.getBit(targetPosition))) {
        target = setFlag(target, Bits.getBit(targetPosition), bitIsSet(source, Bits.getBit(sourcePosition)));
        sourcePosition++;
      }
    }
    return target;
  }

  /**
   * Get a unsigned byte from another unsigned byte by using a given mask
   * <p>
   * The mask is scanned from right to left and every time a bit is set, the corresponding bit in the source
   * is retrieved and set into the int that will be returned, one after the other from right to left
   * </p>
   *
   * @param source the value to set the source from
   * @param mask   the mask to use, to decide which bit in the target to set
   * @return the extracted number from the source
   */
  public static int getWithMask(final int source, final int mask) {
    int target = 0;
    for (int sourcePosition = 0; sourcePosition < BYTE_LENGTH; sourcePosition++) {
      if (bitIsSet(mask, Bits.getBit(sourcePosition))) {
        target = setFlag(target, Bits.getBit(sourcePosition), bitIsSet(source, Bits.getBit(sourcePosition)));
      }
    }
    return target;
  }

  /**
   * Returns true if each int int the the given array int would be a valid unsigned byte
   * <p>
   * Each int value is a valid unsigned byte, if it is in the range from 0 to 256
   * </p>
   *
   * @param intValueArray the array of int to test
   * @return true if each int is an unsigned byte, otherwise false
   */
  public static boolean isUnsignedByteArray(final int[] intValueArray) {
    for (int intValue : intValueArray) {
      if (!isUnsignedByte(intValue)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Converts the given array of int values to an array of signed byte values, using @link{#toSignedByte(int)}
   * for each value
   *
   * @param intArray the array to convert
   * @return the byte array with the converted values
   */
  public static byte[] toSignedByteArray(final int[] intArray) {
    byte[] byteArray = new byte[intArray.length];
    for (int i = 0; i < intArray.length; i++) {
      byteArray[i] = toSignedByte(intArray[i]);
    }
    return byteArray;
  }

  /**
   * Converts the given array of signed byte values to an array of unsigned byte values, using @link{#toUnsignedByte(int)}
   * for each value
   *
   * @param byteArray the array to convert
   * @return the int array with the converted values
   */

  public static int[] toUnsignedByteArray(final byte[] byteArray) {
    int[] intArray = new int[byteArray.length];
    for (int i = 0; i < byteArray.length; i++) {
      intArray[i] = toUnsignedByte(byteArray[i]);
    }
    return intArray;
  }


  /**
   * Returns a hexadecimal representation of an array of int, for which each int is assumed to be an unsigned byte value
   * <p>
   * The conversion of each value is the same as with {@link #formatByteAsHex(int)}, and values are comma separated
   * and embedded in curly braces
   * </p>
   *
   * @param intValueArray the array of ints
   * @return the formatted hex string
   */
  public static String formatUnsignedByteArray(final int[] intValueArray) {

    StringBuilder string = new StringBuilder(ARRAY_START);

    for (int intValue : intValueArray) {
      string.append(formatByteAsHex(intValue));
      string.append(ARRAY_SEPARATOR);
    }

    if (intValueArray.length > 0) {
      string.setLength(string.length() - ARRAY_SEPARATOR.length());
    } else {
      string.setLength(string.length() - 1);
    }

    string.append(ARRAY_END);

    return string.toString();
  }

  /**
   * Returns an array of unsigned bytes stored in ints, created from a string representation of an array of unsigned
   * byte values
   * <p>
   * The array must be in the format created with {@link #formatUnsignedByteArray(int[])}: values are unsigned
   * byte values in hex, prefixed with 0x and followed by two digits, padded if necessary, they are comma separated
   * with optional white spaces and are optionally embedded in curly braces
   * </p>
   * Note this is the format used by C arrays, e.g. <pre>{ 0x00, 0x01 }</pre>
   *
   * @param formattedArray the formated hex string
   * @return the array of ints
   */
  public static int[] parseUnsignedByteArray(final String formattedArray) {
    String withoutBraces = formattedArray.trim().replaceFirst("\\{\\s*", "").replaceFirst("\\s*\\}", "");
    String[] parts = withoutBraces.split("(\\s*,+[\\s,]*\\s*)|(\\s+)");

    if (parts.length == 1 && parts[0].matches("\\s*")) {
      return new int[0];
    }

    int intArray[] = new int[parts.length];
    for (int i = 0; i < parts.length; i++) {
      intArray[i] = Integer.parseInt(parts[i].replace(HEX_STRING_PREFIX, ""), 16);
    }
    return intArray;
  }

  /**
   * Convert the array of unsigned bytes to a string in "ISO-8859-1" encoding
   *
   * @param array the array to convert
   * @return hey string
   */
  public static String toIso88591String(final int[] array) {
    byte[] byteArray = new byte[array.length];
    for (int pos = 0; pos < byteArray.length; pos++) {
      byteArray[pos] = toSignedByte(array[pos]);
    }
    return new String(byteArray, Charset.forName("ISO-8859-1"));
  }

}
