/**
 *
 */
package com.mapdigit.util;

import java.util.Date;
import java.util.Vector;

//import com.sun.cldc.util.j2me.CalendarImpl;
/**
 * @author Dejan Sakel?ak
 *
 */
public class Utils {

    /**
     * Returns a short converted to a byte array.
     *
     * @param l a short number
     * @param bigendian true if big-endian output
     * @return endian byte array
     */
    public static byte[] shortToBytes(short l, boolean bigendian) {
        byte[] by = new byte[2];
        if (bigendian) {
            by[0] = (byte) (l >> 8);
            by[1] = (byte) (l & 0xff);
        } else {
            by[0] = (byte) (l & 0xff);
            by[1] = (byte) (l >> 8);
        }
        return by;
    }

    /**
     * Returns an int converted to a byte array.
     *
     * @param l an integer number
     * @param bigendian true if big-endian output
     * @return endian byte array
     */
    public static byte[] intToBytes(int l, boolean bigendian) {
        byte[] b = new byte[4];
        if (bigendian) {
            b[0] = (byte) (l >> 24);
            b[1] = (byte) (l >> 16);
            b[2] = (byte) (l >> 8);
            b[3] = (byte) (l & 0xff);
        } else {
            b[0] = (byte) (l & 0xff);
            b[1] = (byte) (l >> 8);
            b[2] = (byte) (l >> 16);
            b[3] = (byte) (l >> 24);
        }
        return b;
    }

    /**
     * Returns an long converted to a byte array.
     *
     * @param l a long integer number
     * @param bigendian true if big-endian output
     * @return endian byte array
     */
    public static byte[] longToBytes(long l, boolean bigendian) {
        byte[] b = new byte[8];
        if (bigendian) {
            b[0] = (byte) (l >> 56);
            b[1] = (byte) (l >> 48);
            b[2] = (byte) (l >> 40);
            b[3] = (byte) (l >> 32);
            b[4] = (byte) (l >> 24);
            b[5] = (byte) (l >> 16);
            b[6] = (byte) (l >> 8);
            b[7] = (byte) (l & 0xff);
        } else {
            b[0] = (byte) (l & 0xff);
            b[1] = (byte) (l >> 8);
            b[2] = (byte) (l >> 16);
            b[3] = (byte) (l >> 24);
            b[4] = (byte) (l >> 32);
            b[5] = (byte) (l >> 40);
            b[6] = (byte) (l >> 48);
            b[7] = (byte) (l >> 54);
        }
        return b;
    }

    /**
     * Returns a short converted from a byte array.
     *
     * @param by a byte array
     * @param bigendian true if big-endian output
     * @return short built from bytes
     */
    public static short bytesToShort(byte[] by, boolean bigendian) {
        short s = 0;
        if (bigendian) {
            s = (short) ((by[0] << 8) | (0xff & by[1]));
        } else {
            s = (short) ((by[1] << 8) | (0xff & by[0]));
        }
        return s;
    }

    /**
     * Returns an int converted to from a byte array.
     * We have to get an int because in java we can only so represent
     * an unsigned short.
     *
     * @param by a byte array
     * @param bigendian true if big-endian output
     * @return unsigned short value.
     */
    public static int bytesToUShort(byte[] by, boolean bigendian) {
        int i = 0;
        if (by.length == 1) {
            i = (0xff & by[0]);
        } else if (bigendian) {
            i = (((by[0] & 0xff) << 8) | (0xff & by[1]));
        } else {
            i = (((by[1] & 0xff) << 8) | (0xff & by[0]));
        }
        return i;
    }

    /**
     * Returns an int converted to from a byte array.
     * We have to get an int because in java we can only so represent
     * an unsigned short.
     *
     * @param b a byte array
     * @param bigendian true if big-endian output
     * @return return integer.
     */
    public static int bytesToInt(byte[] b, boolean bigendian) {
        int i = 0;
        if (b.length == 1) {
            i = (0xff & b[0]);
        } else if (b.length == 2) {
            if (bigendian) {
                i = (((b[0] & 0xff) << 8) | (0xff & b[1]));
            } else {
                i = (((b[1] & 0xff) << 8) | (0xff & b[1]));
            }
        } else if (b.length == 3) {
            if (bigendian) {
                i = (((((b[0] & 0xff) << 8) | (0xff & b[1])) << 8) | (0xff & b[2]));
            } else {
                i = (((((b[2] & 0xff) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        } else {
            if (bigendian) {
                i = (((((((b[0] & 0xff) << 8) | (0xff & b[1])) << 8) | (0xff & b[2])) << 8) | (0xff & b[3]));
            } else {
                i = (((((((b[3] & 0xff) << 8) | (0xff & b[2])) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        }
        return i;
    }

    /**
     * Returns a long converted to from a byte array.
     *
     *
     * @param b a byte array
     * @param bigendian true if big-endian output
     * @return a long value.
     */
    public static long bytesToLong(byte[] b, boolean bigendian) {
        int i = 0;
        if (b.length == 1) {
            i = (0xff & b[0]);
        } else if (b.length == 2) {
            if (bigendian) {
                i = (((b[0] & 0xff) << 8) | (0xff & b[1]));
            } else {
                i = (((b[1] & 0xff) << 8) | (0xff & b[1]));
            }
        } else if (b.length == 3) {
            if (bigendian) {
                i = (((((b[0] & 0xff) << 8) | (0xff & b[1])) << 8) | (0xff & b[2]));
            } else {
                i = (((((b[2] & 0xff) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        } else if (b.length == 4) {
            if (bigendian) {
                i = (((((((b[0] & 0xff) << 8) | (0xff & b[1])) << 8) | (0xff & b[2])) << 8) | (0xff & b[3]));
            } else {
                i = (((((((b[3] & 0xff) << 8) | (0xff & b[2])) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        } else if (b.length == 5) {
            if (bigendian) {
                i = (((((((((b[0] & 0xff) << 8) | (b[1] & 0xff)) << 8) | (0xff & b[2])) << 8) | (0xff & b[3])) << 8) | (0xff & b[4]));
            } else {
                i = (((((((((b[4] & 0xff) << 8) | (b[3] & 0xff)) << 8) | (0xff & b[2])) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        } else if (b.length == 6) {
            if (bigendian) {
                i = (((((((((((b[0] & 0xff) << 8) | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8) | (0xff & b[3])) << 8) | (0xff & b[4])) << 8) | (0xff & b[5]));
            } else {
                i = (((((((((((b[5] & 0xff) << 8) | (b[4] & 0xff)) << 8) | (b[3] & 0xff)) << 8) | (0xff & b[2])) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        } else if (b.length == 7) {
            if (bigendian) {
                i = (((((((((((((b[0] & 0xff) << 8) | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8) | (b[3] & 0xff)) << 8) | (0xff & b[4])) << 8) | (0xff & b[5])) << 8) | (0xff & b[6]));
            } else {
                i = ((((((((((((b[6] & 0xff) << 8) | ((b[5] & 0xff)) << 8) | (b[4] & 0xff)) << 8) | (b[3] & 0xff)) << 8) | (0xff & b[2])) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        } else if (b.length == 8) {
            if (bigendian) {
                i = (((((((((((((((b[0] & 0xff) << 8) | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8) | (b[3] & 0xff)) << 8) | (b[4] & 0xff)) << 8) | (0xff & b[5])) << 8) | (0xff & b[6])) << 8) | (0xff & b[7]));
            } else {
                i = ((((((((((((((b[7] & 0xff) << 8) | (b[6] & 0xff)) << 8) | ((b[5] & 0xff)) << 8) | (b[4] & 0xff)) << 8) | (b[3] & 0xff)) << 8) | (0xff & b[2])) << 8) | (0xff & b[1])) << 8) | (0xff & b[0]));
            }
        }
        return i;
    }

    /**
     * Returns a int converted from a hexadecimal number
     *
     * @param s string representing a hexadecimal number
     * @return return a interger.
     */
    public int hexToInt(String s) {
        int[] n = new int[s.length()];
        char c;
        int sum = 0;
        int koef = 1;
        for (int i = n.length - 1; i >= 0; i--) {
            c = s.charAt(i);
            switch (c) {
                case 48:
                    n[i] = 0;
                    break;
                case 49:
                    n[i] = 1;
                    break;
                case 50:
                    n[i] = 2;
                    break;
                case 51:
                    n[i] = 3;
                    break;
                case 52:
                    n[i] = 4;
                    break;
                case 53:
                    n[i] = 5;
                    break;
                case 54:
                    n[i] = 6;
                    break;
                case 55:
                    n[i] = 7;
                    break;
                case 56:
                    n[i] = 8;
                    break;
                case 57:
                    n[i] = 9;
                    break;
                case 97:
                    n[i] = 10;
                    break;
                case 98:
                    n[i] = 11;
                    break;
                case 99:
                    n[i] = 12;
                    break;
                case 100:
                    n[i] = 13;
                    break;
                case 101:
                    n[i] = 14;
                    break;
                case 102:
                    n[i] = 15;
                    break;
            }

            sum = sum + n[i] * koef;
            koef = koef * 16;
        }
        return sum;
    }

    /**
     * It's just a hack to facilitate the short to byte array conversion
     *
     * @param s a value in the unsigned short range 0 to 65535
     * @return unsigned short
     */
    public static short unsignShort(int s) {
        short b = (short) s;
        s = b;
        s = s + 32768;
        b = (short) (0 - s);
        return b;
    }

    /**
     * Convert a byte[] array to readable string format. This makes the "hex" readable!
     * @return result String buffer in String format
     * @param in byte[] buffer to convert to string format
     */
    public static String byteArrayToHexString(byte in[]) {
        byte ch = 0x00;
        int i = 0;

        if (in == null || in.length <= 0) {
            return null;
        }

        String pseudo[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

        StringBuffer out = new StringBuffer(in.length * 2);
        byte[] chars = new byte[in.length];

        for (int j = 0; j < in.length; j++) {
            if (in[j] < (byte) 0x21 || in[j] > (byte) 0x7E) {
                chars[j] = (byte) 46;
            } else {
                chars[j] = in[j];
            }
        }


        while (i < in.length) {

            ch = (byte) (in[i] & 0xF0); // Strip off high nibble

            ch = (byte) (ch >>> 4);
            // shift the bits down

            ch = (byte) (ch & 0x0F);
//     must do this is high order bit is on!

            out.append(pseudo[ch]); // convert the nibble to a String Character

            ch = (byte) (in[i] & 0x0F); // Strip off low nibble

            out.append(pseudo[ch]); // convert the nibble to a String Character

            i++;

        }
        for (int j = 2; j < in.length * 3; j = j + 2) {
            out.insert(j, ' ');
            j++;
        }
        for (int j = 24; j < (in.length * 3 / 24) + in.length * 3; j = j + 24) {
            out.insert(j, ' ');
            j++;
        }
        for (int j = 50; j < (in.length * 3 / 50) + (in.length * 3 / 24) + in.length * 3; j = j + 50) {
            out.insert(j, '\n');
            j++;
        }


        String txt = new String(chars);
        String r = new String(out);
        int ind1 = 0;
        int ind2 = 50;
        int at1 = 0;
        int at2 = 16;
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < (out.length() / 51); j++) {
            sb.append(r.substring(ind1, ind2) + "   " + txt.substring(at1, at2) + "\n");
            ind1 = ind2 + 1;
            ind2 = ind2 + 51;
            at1 = at1 + 16;
            at2 = at2 + 16;
        }
        ind2 = r.length();
        at2 = txt.length();
        String remains = r.substring(ind1, ind2);
        sb.append(remains);
        for (int j = 0; j < 53 - remains.length(); j++) {
            sb.append(" ");
        }
        sb.append(txt.substring(at1, at2) + "\n");
        out = null;
        String rslt = new String(sb);

        return rslt;

    }


//Ilya: somewhere better to use this function instead of substring(N, M)
    /**
     * split string by separator and return String[] of elements

     * @return result String[]

     * @param separator separator
     * @param str entire string to split by separator

     */
    public static String[] explode(char separator, String str) {
        Vector exploded = new Vector(0, 1);
        String tmpstr = null;
        int beginIndex = 0, endIndex = 0;
        while (endIndex < str.length()) {
            if (str.charAt(endIndex) == separator) {
                if (endIndex > beginIndex) {
                    tmpstr = str.substring(beginIndex, endIndex);
                    exploded.addElement(tmpstr);
                    endIndex++;
                    beginIndex = endIndex;
                    tmpstr = null;
                } else {
                    exploded.addElement(null);
                    endIndex++;
                    beginIndex = endIndex;
                }
            } else {
                endIndex++;
            }
        }
        if (endIndex > beginIndex) {
            tmpstr = str.substring(beginIndex, endIndex);
            exploded.addElement(tmpstr);
        }
        String[] res = new String[exploded.size()];
        exploded.copyInto(res);
        return res;
    }

    /**
     * replace all entries of pattern[N] with value replace[N]
     * length of pattern[] must equal to length of replace[]

     * @return result String with replaced values

     * @param pattern - array of patterns to be replaced
     * @param replace - array of values to be inserted instead of pattern[i]
     * @param source source entire string

     */
    public static String replace(String[] pattern, String[] replace, String source) {
        String result = "";
        if (pattern.length != replace.length) {
            return source;
        }
        result = source;
        for (int i = 0; i < pattern.length; i++) {
            result = replace(pattern[i], replace[i], result);
        }
        return result;
    }

    /**
     * replace all entries of pattern with value replace

     * @return result String with replaced values

     * @param pattern  pattern to be replaced
     * @param replace  value to be inserted instead of pattern[i]
     * @param source entire string

     */
    public static String replace(String pattern, String replace, String source) {
        String result = "";
        int firstIndex = 0;
        while (source.indexOf(pattern, firstIndex) != -1) {
            result += source.substring(firstIndex, source.indexOf(pattern, firstIndex));
            result += replace;
            firstIndex = source.indexOf(pattern, firstIndex) + pattern.length();
        }
        if (firstIndex < source.length()) {
            result += source.substring(firstIndex);
        }
        return result;
    }
    private static String[][] url_encode_map = {
        {" ", "%20"}, {"!", "%21"}, {"\"", "%22"}, {"#", "%23"}, {"$", "%24"}, {"%", "%25"}, {"&", "%26"}, {"'", "%27"}, {"(", "%28"}, {")", "%29"},
        {"*", "%2A"}, {"+", "%2B"}, {",", "%2C"}, {"-", "%2D"}, {".", "%2E"}, {"/", "%2F"},
        {"0", "%30"}, {"1", "%31"}, {"2", "%32"}, {"3", "%33"}, {"4", "%34"}, {"5", "%35"}, {"6", "%36"}, {"7", "%37"}, {"8", "%38"}, {"9", "%39"},
        {":", "%3A"}, {";", "%3B"}, {"<", "%3C"}, {"=", "%3D"},
        {">", "%3E"}, {"?", "%3F"}, {"@", "%40"},
        {"A", "%41"}, {"B", "%42"}, {"C", "%43"}, {"D", "%44"}, {"E", "%45"}, {"F", "%46"}, {"G", "%47"}, {"H", "%48"}, {"I", "%49"}, {"J", "%4A"},
        {"K", "%4B"}, {"L", "%4C"}, {"M", "%4D"}, {"N", "%4E"}, {"O", "%4F"}, {"P", "%50"}, {"Q", "%51"}, {"R", "%52"}, {"S", "%53"}, {"T", "%54"},
        {"U", "%55"}, {"V", "%56"}, {"W", "%57"}, {"X", "%58"}, {"Y", "%59"}, {"Z", "%5A"},
        {"[", "%5B"}, {"\\", "%5C"}, {"]", "%5D"}, {"^", "%5E"}, {"_", "%5F"}, {"`", "%60"},
        {"a", "%61"}, {"b", "%62"}, {"c", "%63"}, {"d", "%64"}, {"e", "%65"}, {"f", "%66"}, {"g", "%67"}, {"h", "%68"}, {"i", "%69"}, {"j", "%6A"}, {"k", "%6B"},
        {"l", "%6C"}, {"m", "%6D"}, {"n", "%6E"}, {"o", "%6F"}, {"p", "%70"}, {"q", "%71"}, {"r", "%72"}, {"s", "%73"}, {"t", "%74"}, {"u", "%75"}, {"v", "%76"},
        {"w", "%77"}, {"x", "%78"}, {"y", "%79"}, {"z", "%7A"},
        {"{", "%7B"}, {"|", "%7C"}, {"}", "%7D"}, {"~", "%7E"},};
    private static String[][] url_encode_map_unsafe = {
        {" ", "%20"}, {"!", "%21"}, {"\"", "%22"}, {"#", "%23"}, {"$", "%24"}, {"%", "%25"}, {"&", "%26"}, {"'", "%27"}, {"(", "%28"}, {")", "%29"},
        {"*", "%2A"}, {"+", "%2B"}, {",", "%2C"}, {"/", "%2F"},
        {":", "%3A"}, {";", "%3B"}, {"<", "%3C"}, {"=", "%3D"},
        {">", "%3E"}, {"?", "%3F"}, {"@", "%40"},
        {"[", "%5B"}, {"\\", "%5C"}, {"]", "%5D"}, {"^", "%5E"}, {"`", "%60"},
        {"{", "%7B"}, {"|", "%7C"}, {"}", "%7D"}, {"~", "%7E"},};

    /**
     * Simple ASCII url encoder.

     * @return String url encoded string

     * @param s  string to be encode
     */
    public static String urlEncode(String s) {
        String[] patterns = new String[url_encode_map_unsafe.length];
        String[] replaces = new String[url_encode_map_unsafe.length];
        for (int i = 0; i < url_encode_map_unsafe.length; i++) {
            patterns[i] = url_encode_map_unsafe[i][0];
            replaces[i] = url_encode_map_unsafe[i][1];
        }
        String res = replace(patterns, replaces, s);
        return res;
    }

    /**
     * Simple ASCII url decoder.

     * @return String url decoded string

     * @param s  string to decode
     */
    public static String urlDecode(String s) {
        String[] patterns = new String[url_encode_map.length];
        String[] replaces = new String[url_encode_map.length];
        for (int i = 0; i < url_encode_map.length; i++) {
            patterns[i] = url_encode_map[i][1];
            replaces[i] = url_encode_map[i][0];
        }
        String res = replace(patterns, replaces, s);
        return res;
    }
    /** XML-like easy parser methods*/
    private static int _fromIndex = 0; //static variable to keep tag search position

    public static String getStringForTag(String Tag, String strTmp) {
        return getStringForTag(Tag, strTmp, true);
    }

    public static String getStringForTag(String Tag, String strTmp, boolean newSearch) {
        int tag1, tag2;
        String untaggedString = null;
        String beginTag = "<" + Tag + ">";
        String endTag = "</" + Tag + ">";
        if (newSearch) {
            _fromIndex = 0;
        }
        if (strTmp != null) {
            tag1 = strTmp.indexOf(beginTag, _fromIndex);
            tag2 = strTmp.indexOf(endTag, _fromIndex);
            if (tag1 != -1 && tag2 != -1) {
                tag1 += beginTag.length();
                untaggedString = strTmp.substring(tag1, tag2);
                _fromIndex = tag2 + endTag.length();
            }
        }
        return untaggedString;
    }

    public static String getStringBetweenTags(String Tag, String ClosingTag, String strTmp) {
        int tag1, tag2;
        String untaggedString = null;
        String beginTag = Tag;
        String endTag = ClosingTag;
        if (strTmp != null) {
            tag1 = strTmp.indexOf(beginTag, 0);
            tag2 = strTmp.indexOf(endTag, tag1);
            if (tag1 != -1 && tag2 != -1) {
                tag1 += beginTag.length();
                untaggedString = strTmp.substring(tag1, tag2);
                _fromIndex = tag2 + endTag.length();
            }
        }
        return untaggedString;
    }

    public static boolean stringContains(String s, String seq) {
        return s.indexOf(seq) >= 0;
    }

    /**
     * Utility to split the given String of characters to two parts, seperated by
     * ch.
     *
     * @param in
     *          Input String
     * @param ch
     *          Separator character
     * @return Array of two Strings
     */
    public static String[] splitString(String in, char ch) {
        String[] result = new String[2];
        int pos = in.indexOf(ch);

        if (pos != -1) {
            result[0] = in.substring(0, pos).trim();
            result[1] = in.substring(pos + 1).trim();
        } else {
            result[0] = in.trim();
        }

        return result;
    }

    /**
     * Get string representation of current timestamp
     * in "hh:mm:ss" format
     *
     * @return Formatted date
     */
    public static String getCurrentTimestamp() {
        return formatDate(new Date());
    }

    public static String formatDate(Date d) {
        /*CalendarImpl cal = new CalendarImpl();
        cal.setTime(d);*/
        return "";
    /*.append(cal.get(Calendar.HOUR_OF_DAY)).append(":")
    .append(padInt(cal.get(Calendar.MINUTE))).append(":")
    .append(padInt(cal.get(Calendar.SECOND))).toString();*/
    }

    private static String padInt(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    public static byte[] subArray(byte[] b, int off, int len) {
        byte[] sub = new byte[len];
        System.arraycopy(b, off, sub, 0, len);
        return sub;
    }

    public static final boolean compareByteArray(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) {
            return false;
        }

        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }

        return true;
    }

    public static final boolean isLetter(char c) {
        if (c >= 65 && c <= 90 || c >= 97 && c <= 122) {
            return true;
        }

        return false;
    }

    /**
     * Tokenizes a string with the default delimiter(whitespace).
     * @return Array of tokens(strings).
     * @param  s String to be tokenized.
     */
    public static String[] tokenize(String s) {
        return Utils.tokenize(s, ' ');
    }

    /**
     * Tokenizes a string with the given delimiter.
     * @return Array of tokens(strings).
     * @param s String to be tokenized.
     * @param delimiter Character that delimits the string.
     */
    public static String[] tokenize(String s, char delimiter) {
        Vector v = new Vector();
        int i = s.indexOf(delimiter);
        int currentIndex = -1;
        while (i != -1) {
            v.addElement(new String(s.substring(currentIndex + 1, i)));
            currentIndex = i;
            i = s.indexOf(delimiter, i + 1);
        }
        v.addElement(new String(s.substring(currentIndex + 1)));
        String[] returnArray = new String[v.size()];
        v.copyInto(returnArray);
        return returnArray;
    }
}
