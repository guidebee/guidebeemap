//------------------------------------------------------------------------------
//                         COPYRIGHT 2008 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 01NOV2008  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 01NOV2008  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * J2ME Fixed-Point Math Library. default all number shall be limited in range
 * between -8388608.999999 to 8388608.999999 (precision bit 20).
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 01/11/08
 * @author      Guidebee Pty Ltd.
 */
abstract class MathFP {

    /**
     * Default precision lenght.(1/ 2^21).
     */
    public static final int DEFAULT_PRECISION = 20;
    /**
     * Constant for ONE, HALF etc for the fixed-point math.
     */
    public static long ONE,  HALF,  TWO,  E,  PI,  PI_HALF,  PI_TWO;
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @return the precision
     */
    public static int getPrecision() {
        return (int)precision;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Sets the precision for all fixed-point operations.
     * The maximum precision is 31 bits.
     *
     * @param precision the desired precision in number of bits
     */
    public static void setPrecision(int precision) {
        if (precision > MAX_PRECISION || precision < 0) {
            return;
        }
        int i;
        MathFP.precision = precision;
        ONE = 1 << precision;
        HALF = ONE >> 1;
        TWO = ONE << 1;
        PI = (precision <= PI_PRECISION) ? 
            PI_VALUE >> (PI_PRECISION - precision)
            : PI_VALUE << (precision - PI_PRECISION);
        PI_HALF = PI >> 1;
        PI_TWO = PI << 1;
        E = (precision <= E_PRECISION) ? 
            E_VALUE >> (E_PRECISION - precision)
            : E_VALUE >> (precision - E_PRECISION);
        for (i = 0; i < SK_VALUE.length; i++) {
            SK[i] = (precision <= SK_PRECISION) ?
                SK_VALUE[i] >>(SK_PRECISION - precision)
                    : SK_VALUE[i] << (precision - SK_PRECISION);
        }
        for (i = 0; i < AS_VALUE.length; i++) {
            AS[i] = (precision <= AS_PRECISION) ?
                AS_VALUE[i] >>(AS_PRECISION - precision)
                : AS_VALUE[i] << (precision - AS_PRECISION);
        }
        LN2 = (precision <= LN2_PRECISION) ?
            LN2_VALUE >>(LN2_PRECISION - precision)
                : LN2_VALUE << (precision - LN2_PRECISION);
        LN2_INV = (precision <= LN2_PRECISION) ? 
            LN2_INV_VALUE >>(LN2_PRECISION - precision) :
            LN2_INV_VALUE << (precision - LN2_PRECISION);
        for (i = 0; i < LG_VALUE.length; i++) {
            LG[i] = (precision <= LG_PRECISION) ? 
                LG_VALUE[i] >>(LG_PRECISION - precision)
                : LG_VALUE[i] << (precision - LG_PRECISION);
        }
        for (i = 0; i < EXP_P_VALUE.length; i++) {
            EXP_P[i] = (precision <= EXP_P_PRECISION) 
                    ? EXP_P_VALUE[i] >>(EXP_P_PRECISION - precision)
                    : EXP_P_VALUE[i] << (precision - EXP_P_PRECISION);
        }
        fracMask = ONE - 1;
        piOverOneEighty = div(PI, toFP(180));
        oneEightyOverPi = div(toFP(180), PI);

        maxDigitsMul = 1;
        maxDigitsCount = 0;
        for (long j = ONE; j != 0;) {
            j /= 10;
            maxDigitsMul *= 10;
            maxDigitsCount++;
        }
    }

    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts a fixed-point value to the current set precision.
     *
     * @param fp the fixed-point value to convert.
     * @param precision the precision of the fixed-point value passed in.
     * @return a fixed-point value of the current precision
     */
    public static long convert(long fp, int precision) {
        long num, xabs = Math.abs(fp);
        if (precision > MAX_PRECISION || precision < 0) {
            return fp;
        }
        if (precision > MathFP.precision) {
            num = xabs >> (precision - MathFP.precision);
        } else {
            num = xabs << (MathFP.precision - precision);
        }
        if (fp < 0) {
            num = -num;
        }
        return num;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts an long to a fixed-point long.
     *
     * @param i long to convert.
     * @return the converted fixed-point value.
     */
    public static long toFP(int i) {
        return (i < 0) ? -(-i << precision) : i << precision;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts a string to a fixed-point value. <br>
     * The string should trimmed of any whitespace before-hand. <br>
     * A few examples of valid strings:<br>
     *
     * <pre>
     * .01
     * 0.01
     * 10
     * 130.0
     * -30000.12345
     * </pre>
     *
     * @param s the string to convert.
     * @return the fixed-point value.
     */
    public static long toFP(String s) {
        long fp, i, integer, frac = 0;
        String fracString = null;
        boolean neg = false;
        if (s.charAt(0) == '-') {
            neg = true;
            s = s.substring(1);
        }
        int index = s.indexOf('.');

        if (index < 0) {
            integer = Integer.parseInt(s);
        } else if (index == 0) {
            integer = 0;
            fracString = s.substring(1);
        } else if (index == s.length() - 1) {
            integer = Integer.parseInt(s.substring(0, index));
        } else {
            integer = Integer.parseInt(s.substring(0, index));
            fracString = s.substring(index + 1);
        }

        if (fracString != null) {
            if (fracString.length() > maxDigitsCount) {
                fracString = fracString.substring(0, (int)maxDigitsCount);
            }
            if (fracString.length() > 0) {
                frac = Integer.parseInt(fracString);
                for (i = maxDigitsCount - fracString.length(); i > 0; --i) {
                    frac *= 10;
                }
            }
        }
        fp = (integer << precision) + (frac << precision+HALF) / maxDigitsMul;
        if (neg) {
            fp = -fp;
        }
        return fp;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts a fixed-point value to an long.
     *
     * @param fp fixed-point value to convert
     * @return the converted long value.
     */
    public static int toInt(long fp) {
        return (int)((fp < 0) ? -(-fp >> precision) : fp >> precision);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts a fixed-point value to a string.
     *
     * Same as <code>toString(x, 0, max_possible_digits)</code>
     *
     * @param fp the fixed-point value to convert.
     * @return a string representing the fixed-point value with a minimum of
     *         decimals in the string.
     */
    public static String toString(long fp) {
        boolean neg = false;
        if (fp < 0) {
            neg = true;
            fp = -fp;
        }
        long integer = fp >> precision;
        long fp1=(long)((fp & fracMask)) * maxDigitsMul;
        long fp2=(long)(fp1>>precision);
        String fracString = String.valueOf(fp2);

        long len = maxDigitsCount - fracString.length();
        for (long i = len; i > 0; --i) {
            fracString = "0" + fracString;
        }
        if ((neg && integer != 0)) {
            integer = -integer;
        }
        return String.valueOf(integer) + "." + fracString.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the smallest (closest to negative infinity) fixed-point value
     * that is greater than or equal to the argument and is equal to a
     * mathematical integer.
     *
     * @param fp a fixed-point value.
     * @return the smallest (closest to negative infinity) fixed-point value
     *         that is greater than or equal to the argument and is equal to a
     *         mathematical integer.
     */
    public static long ceil(long fp) {
        boolean neg = false;
        if (fp < 0) {
            fp = -fp;
            neg = true;
        }
        if ((fp & fracMask) == 0) {
            return (neg) ? -fp : fp;
        }
        if (neg) {
            return -(fp & ~fracMask);
        }
        return (fp & ~fracMask) + ONE;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the largest (closest to positive infinity) fixed-point value
     * value that is less than or equal to the argument and is equal to a
     * mathematical integer.
     *
     * @param fp a fixed-point value.
     * @return the largest (closest to positive infinity) fixed-point value that
     *         less than or equal to the argument and is equal to a mathematical
     *         integer.
     */
    public static long floor(long fp) {
        boolean neg = false;
        if (fp < 0) {
            fp = -fp;
            neg = true;
        }
        if ((fp & fracMask) == 0) {
            return (neg) ? -fp : fp;
        }
        if (neg) {
            return -(fp & ~fracMask) - ONE;
        }
        return (fp & ~fracMask);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Removes the fractional part of a fixed-point value.
     *
     * @param fp the fixed-point value to truncate.
     * @return a truncated fixed-point value.
     */
    public static long trunc(long fp) {
        return (fp < 0) ? -(-fp & ~fracMask) : fp & ~fracMask;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the fractional part of a fixed-point value.
     *
     * @param fp a fixed-point value to get fractional part of.
     * @return positive fractional fixed-point value if input is positive,
     *         negative fractional otherwise.
     */
    public static long frac(long fp) {
        return (fp < 0) ? -(-fp & fracMask) : fp & fracMask;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts a fixed-point integer to an int with only the decimal value.
     * <p>
     * For example, if <code>fp</code> represents <code>12.34</code> the
     * method returns <code>34</code>
     * </p>
     *
     * @param fp the fixed-point integer to be converted
     * @return a int in a normal integer representation
     */
    public static int fracAsInt(long fp) {
        if (fp < 0) {
            fp = -fp;
        }
        return (int) ((maxDigitsMul * (fp & fracMask)) >> precision);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the closest integer to the argument.
     *
     * @param fp the fixed-point value to round
     * @return the value of the argument rounded to the nearest integer value.
     */
    public static long round(long fp) {
        boolean neg = false;
        if (fp < 0) {
            fp = -fp;
            neg = true;
        }
        fp += HALF;
        fp &= ~fracMask;
        return (neg) ? -fp : fp;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the smaller of two values.
     *
     * @param fp1 the fixed-point value.
     * @param fp2 the fixed-point value.
     * @return the smaller of fp1 and fp2.
     */
    public static long min(long fp1, long fp2) {
        return fp2 >= fp1 ? fp1 : fp2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the greater of two values.
     *
     * @param fp1 the fixed-point value.
     * @param fp2 the fixed-point value.
     * @return the greater of fp1 and fp2.
     */
    public static long max(long fp1, long fp2) {
        return fp1 >= fp2 ? fp1 : fp2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the absolute value of a fix float value.
     *
     * @param fp the fixed-point value.
     * @return the absolute value of the argument.
     */
    public static long abs(long fp) {
        if (fp < 0) {
            return -fp;
        } else {
            return fp;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * add two fixed-point values.
     *
     * @param fp1 first fixed-point value.
     * @param fp2 second fixed-point value.
     * @return the result of the addition.
     */
    public static long add(long fp1, long fp2) {
        return fp1 + fp2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * substract two fixed-point values.
     *
     * @param fp1 first fixed-point value.
     * @param fp2 second fixed-point value.
     * @return the result of the substraction.
     */
    public static long sub(long fp1, long fp2) {
        return fp1 - fp2;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes the remainder operation on two arguments .
     *
     * @param fp1 first fixed-point value.
     * @param fp2 second fixed-point value.
     * @return the remainder when fp1 is divided by fp2
     */
    public static long IEEERemainder(long fp1, long fp2) {
        return fp1 - mul(floor(div(fp1, fp2)), fp2);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Multiplies two fixed-point values.
     *
     * @param fp1 first fixed-point value.
     * @param fp2 second fixed-point value.
     * @return the result of the multiplication.
     */
    public static long mul(long fp1, long fp2) {
        long fp= fp1 *  fp2;
        return  (fp >> precision);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Divides two fixed-point values.
     *
     * @param fp1 mumerator fixed-point value.
     * @param fp2 denominator fixed-point value.
     * @return the result of the division.
     */
    public static long div(long fp1, long fp2) {
        if (fp1 == 0) {
            return 0;
        }
        if (fp2 == 0) {
            return (fp1 < 0) ? -INFINITY : INFINITY;
        }
        long xneg = 0, yneg = 0;
        if (fp1 < 0) {
            xneg = 1;
            fp1 = -fp1;
        }
        if (fp2 < 0) {
            yneg = 1;
            fp2 = -fp2;
        }
        long msb = 0, lsb = 0;
        while ((fp1 & (1 << MAX_PRECISION - msb)) == 0) {
            msb++;
        }
        while ((fp2 & (1 << lsb)) == 0) {
            lsb++;
        }
        long shifty = precision - (msb + lsb);
        long res = ((fp1 << msb) / (fp2 >> lsb));
        if (shifty > 0) {
            res <<= shifty;
        } else {
            res >>= -shifty;
        }
        if ((xneg ^ yneg) == 1) {
            res = -res;
        }
        return res;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the correctly rounded positive square root of a fixed-point
     * value.
     *
     * @param fp a fixed-point value.
     * @return the positive square root of <code>fp</code>. If the argument
     *         is NaN or less than zero, the result is NaN.
     */
    public static long sqrt(long fp) {
        long s = (fp + ONE) >> 1;
        for (int i = 0; i < 8; i++) {
            s = (s + div(fp, s)) >> 1;
        }
        return s;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the trigonometric sine of an angle.
     *
     * @param fp the angle in radians
     * @return the sine of the argument.
     */
    public static long sin(long fp) {
        long sign = 1;
        fp %= PI * 2;
        if (fp < 0) {
            fp = PI * 2 + fp;
        }
        if ((fp > PI_HALF) && (fp <= PI)) {
            fp = PI - fp;
        } else if ((fp > PI) && (fp <= (PI + PI_HALF))) {
            fp = fp - PI;
            sign = -1;
        } else if (fp > (PI + PI_HALF)) {
            fp = (PI << 1) - fp;
            sign = -1;
        }

        long sqr = mul(fp, fp);
        long result = SK[0];
        result = mul(result, sqr);
        result -= SK[1];
        result = mul(result, sqr);
        result += ONE;
        result = mul(result, fp);
        return sign * result;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the trigonometric cosine of an angle.
     *
     * @param fp the angle in radians
     * @return the cosine of the argument.
     */
    public static long cos(long fp) {
        return sin(PI_HALF - fp);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the trigonometric tangent of an angle.
     *
     * @param fp the angle in radians
     * @return the tangent of the argument.
     */
    public static long tan(long fp) {
        return div(sin(fp), cos(fp));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the arc sine of a value; the returned angle is in the range
     * -<i>pi</i>/2 through <i>pi</i>/2.
     *
     * @param fp the fixed-point value whose arc sine is to be returned.
     * @return the arc sine of the argument.
     */
    public static long asin(long fp) {
        boolean neg = false;
        if (fp < 0) {
            neg = true;
            fp = -fp;
        }

        long fRoot = sqrt(ONE - fp);
        long result = AS[0];

        result = mul(result, fp);
        result += AS[1];
        result = mul(result, fp);
        result -= AS[2];
        result = mul(result, fp);
        result += AS[3];
        result = PI_HALF - (mul(fRoot, result));
        if (neg) {
            result = -result;
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the arc cosine of a value; the returned angle is in the range 0.0
     * through <i>pi</i>.
     *
     * @param fp the fixed-point value whose arc cosine is to be returned.
     * @return the arc cosine of the argument.
     */
    public static long acos(long fp) {
        return PI_HALF - asin(fp);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the arc tangent of a value; the returned angle is in the range
     * -<i>pi</i>/2 through <i>pi</i>/2.
     *
     * @param fp the fiexed-point value whose arc tangent is to be returned.
     * @return the arc tangent of the argument.
     */
    public static long atan(long fp) {
        return asin(div(fp, sqrt(ONE + mul(fp, fp))));
    }    // This is a finely tuned error around 0. The inaccuracies stabilize at
    //around this value.
    private static int ATAN2_ZERO_ERROR = 65;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the angle <i>theta</i> from the conversion of rectangular
     * coordinates (<code>fpX</code>,&nbsp;<code>fpY</code>) to polar
     * coordinates (r,&nbsp;<i>theta</i>).
     *
     * @param fpX the ordinate coordinate
     * @param fpY the abscissa coordinate
     * @return the <i>theta</i> component of the point
     * (<i>r</i>,&nbsp;<i>theta</i>)
     * in polar coordinates that corresponds to the point
     * (<i>fpX</i>,&nbsp;<i>fpY</i>) in Cartesian coordinates.
     */
    public static long atan2(long fpX, long fpY) {
        if (fpX == 0) {
            if (fpY >= 0) {
                return 0;
            } else if (fpY < 0) {
                return PI;
            }
        } else if (fpY >= -ATAN2_ZERO_ERROR && fpY <= ATAN2_ZERO_ERROR) {
            return (fpX > 0) ? PI_HALF : -PI_HALF;
        }
        long z = atan(Math.abs(div(fpX, fpY)));
        if (fpY > 0) {
            return (fpX > 0) ? z : -z;
        } else {
            return (fpX > 0) ? PI - z : z - PI;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns Euler's number <i>e</i> raised to the power of a fixed-point
     * value.
     *
     * @param fp the exponent to raise <i>e</i> to.
     * @return the value <i>e</i><sup><code>fp</code></sup>, where <i>e</i>
     *         is the base of the natural logarithms.
     */
    public static long exp(long fp) {
        if (fp == 0) {
            return ONE;
        }
        long xabs = Math.abs(fp);
        long k = mul(xabs, LN2_INV);
        k += HALF;
        k &= ~fracMask;
        if (fp < 0) {
            k = -k;
        }
        fp -= mul(k, LN2);
        long z = mul(fp, fp);
        long R = TWO + mul(z, EXP_P[0] + mul(z, EXP_P[1] + mul(z, EXP_P[2]
                + mul(z, EXP_P[3] + mul(z, EXP_P[4])))));
        long xp = ONE + div(mul(TWO, fp), R - fp);
        if (k < 0) {
            k = ONE >> (-k >> precision);
        } else {
            k = ONE << (k >> precision);
        }
        return mul(k, xp);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the natural logarithm (base e) of a fixed-point value.
     *
     * @param x a fixed-point value
     * @return the value ln&nbsp;<code>a</code>, the natural logarithm of
     *         <code>fp</code>.
     */
    public static long log(long x) {
        if (x < 0) {
            return 0;
        }
        if (x == 0) {
            return -INFINITY;
        }
        long log2 = 0, xi = x;
        while (xi >= TWO) {
            xi >>= 1;
            log2++;
        }
        long f = xi - ONE;
        long s = div(f, TWO + f);
        long z = mul(s, s);
        long w = mul(z, z);
        long R = mul(w, LG[1] + mul(w, LG[3] + mul(w, LG[5])))
                + mul(z, LG[0] + mul(w, LG[2] + mul(w, LG[4] + mul(w, LG[6]))));
        return mul(LN2, (log2 << precision)) + f - mul(s, f - R);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the logarithm (base <code>base</code>) of a fixed-point value.
     *
     * @param fp a fixed-point value
     * @param base
     * @return the value log&nbsp;<code>a</code>, the logarithm of
     *         <code>fp</code>
     */
    public static long log(long fp, long base) {
        return div(log(fp), log(base));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the value of the first argument raised to the power of the second
     * argument
     *
     * @param fp1 the base
     * @param fp2 the exponent
     * @return the value <code>a<sup>b</sup></code>.
     */
    public static long pow(long fp1, long fp2) {
        if (fp2 == 0) {
            return ONE;
        }
        if (fp1 < 0) {
            return 0;
        }
        return exp(mul(log(fp1), fp2));
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts an angle measured in degrees to an approximately equivalent
     * angle measured in radians.
     *
     * @param fp a fixed-point angle in degrees
     * @return the measurement of the angle angrad in radians.
     */
    public static long toRadians(long fp) {
        return mul(fp, piOverOneEighty);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01NOV2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Converts an angle measured in radians to an approximately equivalent
     * angle measured in degrees.
     *
     * @param fp a fixed-point angle in radians
     * @return the measurement of the angle angrad in degrees.
     */
    public static long toDegrees(long fp) {
        return mul(fp, oneEightyOverPi);
    }
    private static final int MAX_PRECISION = 30;
    /**
     * largest possible number
     */
    public static final int INFINITY = 0x7fffffff;
     // 2.7182818284590452353602874713527 * 2^29
    private static final int E_PRECISION = 29;
    private static final int E_VALUE = 1459366444;
     // 3.1415926535897932384626433832795 * 2^29
    private static final int PI_PRECISION = 29;
    private static final int PI_VALUE = 1686629713;
    /**
     * number of fractional bits in all operations, do not modify directly
     */
    private static long precision = 0;
    private static long fracMask = 0;
    private static long oneEightyOverPi;
    private static long piOverOneEighty;
    private static long maxDigitsCount;
    private static long maxDigitsMul;
    private static final int SK_PRECISION = 31;
    private static final int SK_VALUE[] = {
        16342350, //7.61e-03 * 2^31
        356589659, //1.6605e-01 * 2^31
    };
    private static int SK[] = new int[SK_VALUE.length];
    private static final int AS_PRECISION = 30;
    private static final int AS_VALUE[] = {
        -20110432, //-0.0187293 * 2^30
        79737141, //0.0742610 * 2^30
        227756102, //0.2121144 * 2^30
        1686557206 //1.5707288 * 2^30
    };
    private static int AS[] = new int[AS_VALUE.length];
     //0.69314718055994530941723212145818 * 2^30
    private static final int LN2_PRECISION = 30;
    private static final int LN2_VALUE = 744261117;
    //1.4426950408889634073599246810019 * 2^30
    private static final int LN2_INV_VALUE = 1549082004;
    private static int LN2,  LN2_INV;
    private static final int LG_PRECISION = 31;
    private static final int LG_VALUE[] = {
        1431655765, //6.666666666666735130e-01 * 2^31
        858993459, //3.999999999940941908e-01 * 2^31
        613566760, //2.857142874366239149e-01 * 2^31
        477218077, //2.222219843214978396e-01 * 2^31
        390489238, //1.818357216161805012e-01 * 2^31
        328862160, //1.531383769920937332e-01 * 2^31
        317788895 //1.479819860511658591e-01 * 2^31
    };
    private static int LG[] = new int[LG_VALUE.length];
    private static final int EXP_P_PRECISION = 31;
    private static final int EXP_P_VALUE[] = {
        357913941, //1.66666666666666019037e-01 * 2^31
        -5965232, //-2.77777777770155933842e-03 * 2^31
        142029, //6.61375632143793436117e-05 * 2^31
        -3550, //-1.65339022054652515390e-06 * 2^31
        88, //4.13813679705723846039e-08 * 2^31
    };
    private static int EXP_P[] = new int[EXP_P_VALUE.length];
    // Init the default precision


    static {
        setPrecision(DEFAULT_PRECISION);
    }
}
