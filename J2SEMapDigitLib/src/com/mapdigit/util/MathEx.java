//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 04JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.util;

//--------------------------------- IMPORTS ------------------------------------
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 04JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The class MathEx contains methods for performing basic numeric operations.
 * It's an extention of java.lang.Math, provides more math functions.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     1.00, 04/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MathEx {
    //[------------------------------ CONSTANTS -------------------------------]
    /**
     * The double value limit used to termiate a calcuation.
     */
    private static final double PRECISION = 1e-12;
    /**
     * The double value that is closer than any other to pi, the ratio of the
     * circumference of a circle to its diameter.
     */
    public static final double PI = Math.PI;
    /**
     * The double value that is closer than any other to e, the base of the
     * natural logarithms.
     */
    public static final double E = Math.E;
    //[------------------------------ CONSTRUCTOR -----------------------------]
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Privete constructor,avoid the class to be instantiated.
     */
    private MathEx() {
    }
    //[------------------------------ PUBLIC METHODS --------------------------]
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the absolute value of a <code>double</code> value.
     * If the argument is not negative, the argument is returned.
     * If the argument is negative, the negation of the argument is returned.
     * Special cases:
     * <ul><li>If the argument is positive zero or negative zero, the result
     * is positive zero.
     * <li>If the argument is infinite, the result is positive infinity.
     * <li>If the argument is NaN, the result is NaN.</ul>
     * In other words, the result is equal to the value of the expression:
     * <p><pre>Double.longBitsToDouble((Double.doubleToLongBits(a)<<1)>>>1)</pre>
     * @param a a double value.
     * @return the absolute value of the argument.
     */
    public static double abs(double a) {
        return Math.abs(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the absolute value of a <code>float</code> value.
     * If the argument is not negative, the argument is returned.
     * If the argument is negative, the negation of the argument is returned.
     * Special cases:
     * <ul><li>If the argument is positive zero or negative zero, the
     * result is positive zero.
     * <li>If the argument is infinite, the result is positive infinity.
     * <li>If the argument is NaN, the result is NaN.</ul>
     * In other words, the result is equal to the value of the expression:
     * <p><pre>Float.intBitsToFloat(0x7fffffff & Float.floatToIntBits(a))</pre>
     *<P><DD>
     * @param a a float value.
     * @return the absolute value of the argument.
     */
    public static float abs(float a) {
        return Math.abs(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the absolute value of an <code>int</code> value.
     * If the argument is not negative, the argument is returned.
     * If the argument is negative, the negation of the argument is returned.
     * <p> Note that if the argument is equal to the value of
     * <code>Integer.MIN_VALUE</code>, the most negative representable
     * <code>int</code> value, the result is that same value, which is
     * negative.<P><DD>
     * @param a a int value.
     * @return the absolute value of the argument.
     */
    public static int abs(int a) {
        return Math.abs(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the absolute value of a <code>long</code> value.
     * If the argument is not negative, the argument is returned.
     * If the argument is negative, the negation of the argument is returned.
     * <p> Note that if the argument is equal to the value of
     * <code>Long.MIN_VALUE</code>, the most negative representable
     * <code>long</code> value, the result is that same value, which is
     * negative.<P><DD>
     * @param a a long value.
     * @return the absolute value of the argument.
     */
    public static long abs(long a) {
        return Math.abs(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the smallest (closest to negative infinity)
     * <code>double</code> value that is not less than the argument and is
     * equal to a mathematical integer. Special cases:
     * <ul><li>If the argument value is already equal to a mathematical
     * integer, then the result is the same as the argument.
     * <li>If the argument is NaN or an infinity or positive zero or negative
     * zero, then the result is the same as the argument.
     * <li>If the argument value is less than zero but greater than -1.0,
     * then the result is negative zero.</ul>
     * Note that the value of <code>Math.ceil(x)</code> is exactly the
     * value of <code>-Math.floor(-x)</code>.<P><DD>
     * @param a a double value.
     * @return the smallest (closest to negative infinity) double value that is
     * not less than the argument and is equal to a mathematical integer.
     */
    public static double ceil(double a) {
        return Math.ceil(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the trigonometric cosine of an angle. Special case:
     * <ul><li>If the argument is NaN or an infinity, then the
     * result is NaN.</ul><P><DD>
     * @param a an angle, in radians.
     * @return the cosine of the argument.
     */
    public static double cos(double a) {
        return Math.cos(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the largest (closest to positive infinity)
     * <code>double</code> value that is not greater than the argument and
     * is equal to a mathematical integer. Special cases:
     * <ul><li>If the argument value is already equal to a mathematical
     * integer, then the result is the same as the argument.
     * <li>If the argument is NaN or an infinity or positive zero or
     * negative zero, then the result is the same as the argument.</ul><P><DD>
     * @param a a double value.
     * @return the largest (closest to positive infinity) double value that is
     * not greater than the argument and is equal to a mathematical integer.
     */
    public static double floor(double a) {
        return Math.floor(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the greater of two <code>double</code> values.  That is, the
     * result is the argument closer to positive infinity. If the
     * arguments have the same value, the result is that same value. If
     * either value is <code>NaN</code>, then the result is <code>NaN</code>.
     * Unlike the the numerical comparison operators, this method considers
     * negative zero to be strictly smaller than positive zero. If one
     * argument is positive zero and the other negative zero, the result
     * is positive zero.<P><DD>
     * @param a a double value.
     * @param b a double value.
     * @return the larger of a and b.
     */
    public static double max(double a, double b) {
        return Math.max(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the greater of two <code>float</code> values.  That is, the
     * result is the argument closer to positive infinity. If the
     * arguments have the same value, the result is that same value. If
     * either value is <code>NaN</code>, then the result is <code>NaN</code>.
     * Unlike the the numerical comparison operators, this method considers
     * negative zero to be strictly smaller than positive zero. If one
     * argument is positive zero and the other negative zero, the result
     * is positive zero.<P><DD>
     * @param a a float value.
     * @param b a float value.
     * @return the larger of a and b.
     */
    public static float max(float a, float b) {
        return Math.max(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the greater of two <code>int</code> values. That is, the
     * result is the argument closer to the value of
     * <code>Integer.MAX_VALUE</code>. If the arguments have the same value,
     * the result is that same value.<P><DD>
     * @param a a int value.
     * @param b a int value.
     * @return the larger of a and b.
     */
    public static int max(int a, int b) {
        return Math.max(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the greater of two <code>long</code> values. That is, the
     * result is the argument closer to the value of
     * <code>Long.MAX_VALUE</code>. If the arguments have the same value,
     * the result is that same value.<P><DD>
     * @param a a long value.
     * @param b a long value.
     * @return the larger of a and b.
     */
    public static long max(long a, long b) {
        return Math.max(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the smaller of two <code>double</code> values.  That is, the
     * result is the value closer to negative infinity. If the arguments have
     * the same value, the result is that same value. If either value
     * is <code>NaN</code>, then the result is <code>NaN</code>.  Unlike the
     * the numerical comparison operators, this method considers negative zero
     * to be strictly smaller than positive zero. If one argument is
     * positive zero and the other is negative zero, the result is negative
     * zero.<P><DD>
     * @param a a double value.
     * @param b a double value.
     * @return the smaller of a and b.
     */
    public static double min(double a, double b) {
        return Math.min(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the smaller of two <code>double</code> values.  That is, the
     * result is the value closer to negative infinity. If the arguments have
     * the same value, the result is that same value. If either value
     * is <code>NaN</code>, then the result is <code>NaN</code>.  Unlike the
     * the numerical comparison operators, this method considers negative zero
     * to be strictly smaller than positive zero. If one argument is
     * positive zero and the other is negative zero, the result is negative
     * zero.<P><DD>
     * @param a a double value.
     * @param b a double value.
     * @return the smaller of a and b.
     */
    public static float min(float a, float b) {
        return Math.min(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the smaller of two <code>int</code> values. That is, the
     * result the argument closer to the value of <code>Integer.MIN_VALUE</code>.
     * If the arguments have the same value, the result is that same value.
     *<P><DD>
     * @param a a int value.
     * @param b a int value.
     * @return the smaller of a and b.
     */
    public static int min(int a, int b) {
        return Math.min(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the smaller of two <code>long</code> values. That is, the
     * result is the argument closer to the value of
     * <code>Long.MIN_VALUE</code>. If the arguments have the same value,
     * the result is that same value.<P><DD>
     * @param a a long value.
     * @param b a long value.
     * @return the smaller of a and b.
     */
    public static long min(long a, long b) {
        return Math.min(a, b);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the trigonometric sine of an angle.  Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the
     * result is NaN.
     * <li>If the argument is positive zero, then the result is
     * positive zero; if the argument is negative zero, then the
     * result is negative zero.</ul><P><DD>
     * @param a an angle, in radians
     * @return the sine of the argument.
     */
    public static double sin(double a) {
        return Math.sin(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the correctly rounded positive square root of a
     * <code>double</code> value.
     * Special cases:
     * <ul><li>If the argument is NaN or less than zero, then the result
     * is NaN.
     * <li>If the argument is positive infinity, then the result is positive
     * infinity.
     * <li>If the argument is positive zero or negative zero, then the
     * result is the same as the argument.</ul><P><DD>
     * @param a a double value.
     * @return the positive square root of a. If the argument is NaN or less
     * than zero, the result is NaN.
     */
    public static double sqrt(double a) {
        return Math.sqrt(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Returns the trigonometric tangent of an angle.  Special cases:
     * <ul><li>If the argument is NaN or an infinity, then the result
     * is NaN.
     * <li>If the argument is positive zero, then the result is
     * positive zero; if the argument is negative zero, then the
     * result is negative zero</ul><P><DD>
     * @param a an angle, in radians.
     * @return the tangent of the argument.
     */
    public static double tan(double a) {
        return Math.tan(a);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Converts an angle measured in radians to the equivalent angle
     * measured in degrees.<P><DD
     * @param angrad  an angle, in radians.
     * @return the measurement of the angle angrad in degrees.
     */
    public static double toDegrees(double angrad) {
        return Math.toDegrees(angrad);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Converts an angle measured in degrees to the equivalent angle
     * measured in radians.<P><DD>
     * @param angdeg  an angle, in degrees.
     * @return the measurement of the angle angrad in radians.
     */
    public static double toRadians(double angdeg) {
        return Math.toRadians(angdeg);
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns the arc tangent of an angle, in the range of -<i>pi</i>/2
     * through <i>pi</i>/2.  Special cases:  <ul><li>If the argument is NaN,
     * then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul> <p>
     * A result must be within 1 ulp of the correctly rounded result.  Results
     * must be semi-monotonic.<P><DD>
     * @param a  the value whose arc tangent is to be returned.
     * @return the arc tangent of the argument.
     */
    public static double atan(double a) {
        boolean signChange = false;
        boolean Invert = false;
        if (a < 0.) {
            a = -a;
            signChange = true;
        }
        // check up the invertation
        if (a > 1.) {
            a = 1 / a;
            Invert = true;
        }
        if (a == 0.0) {
            return 0;
        }
        if (a == 1.0) {
            return 0.7853981633974483;
        }
        double a0 = a;
        double n = 1;
        double a1 = (-1) * a * a * (2 * n - 1) / (2 * n + 1) * a0;
        double s = a0;
        while (Math.abs(a1) > PRECISION) {
            s += a1;
            n += 1.0;
            a0 = a1;
            a1 = (-1) * a * a * (2 * n - 1) / (2 * n + 1) * a0;
        }
        // invertation took place
        if (Invert) {
            s = Math.PI / 2 - s;
        }
        // sign change took place
        if (signChange) {
            s = -s;
        }
        return s;
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Converts rectangular coordinates (<code>x</code>,&nbsp;<code>y</code>)
     * to polar (r,&nbsp;<i>theta</i>).
     * This method computes the phase <i>theta</i> by computing an arc tangent
     * of <code>y/x</code> in the range of -<i>pi</i> to <i>pi</i>. Special
     * cases:
     * <ul><li>If either argument is NaN, then the result is NaN.
     * <li>If the first argument is positive zero and the second argument
     * is positive, or the first argument is positive and finite and the
     * second argument is positive infinity, then the result is positive
     * zero.
     * <li>If the first argument is negative zero and the second argument
     * is positive, or the first argument is negative and finite and the
     * second argument is positive infinity, then the result is negative zero.
     * <li>If the first argument is positive zero and the second argument
     * is negative, or the first argument is positive and finite and the
     * second argument is negative infinity, then the result is the
     * <code>double</code> value closest to <i>pi</i>.
     * <li>If the first argument is negative zero and the second argument
     * is negative, or the first argument is negative and finite and the
     * second argument is negative infinity, then the result is the
     * <code>double</code> value closest to -<i>pi</i>.
     * <li>If the first argument is positive and the second argument is
     * positive zero or negative zero, or the first argument is positive
     * infinity and the second argument is finite, then the result is the
     * <code>double</code> value closest to <i>pi</i>/2.
     * <li>If the first argument is negative and the second argument is
     * positive zero or negative zero, or the first argument is negative
     * infinity and the second argument is finite, then the result is the
     * <code>double</code> value closest to -<i>pi</i>/2.
     * <li>If both arguments are positive infinity, then the result is the
     * <code>double</code> value closest to <i>pi</i>/4.
     * <li>If the first argument is positive infinity and the second argument
     * is negative infinity, then the result is the <code>double</code>
     * value closest to 3*<i>pi</i>/4.
     * <li>If the first argument is negative infinity and the second argument
     * is positive infinity, then the result is the <code>double</code> value
     * closest to -<i>pi</i>/4.
     * <li>If both arguments are negative infinity, then the result is the
     * <code>double</code> value closest to -3*<i>pi</i>/4.</ul><p>
     * A result must be within 2 ulps of the correctly rounded result.  Results
     * must be semi-monotonic.<P><DD>
     * @param x  the ordinate coordinate.
     * @param y  the abscissa  coordinate.
     * @return the theta component of the point (r, theta) in polar coordinates
     * that corresponds to the point (x, y) in Cartesian coordinates.
     */
    public static double atan2(double y, double x) {
        // if x=y=0
        if (y == 0. && x == 0.) {
            return 0.;
        }
        // if x>0 atan(y/x)
        if (x > 0.) {
            return atan(y / x);
        }
        // if x<0 sign(y)*(pi - atan(|y/x|))
        if (x < 0.) {
            if (y < 0.) {
                return -(Math.PI - atan(y / x));
            } else {
                return Math.PI - atan(-y / x);
            }
        }
        // if x=0 y!=0 sign(y)*pi/2
        if (y < 0.) {
            return -Math.PI / 2.;
        } else {
            return Math.PI / 2.;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 05SEP2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the closest <code>int</code> to the argument. The
     * result is rounded to an integer by adding 1/2, taking the
     * floor of the result, and casting the result to type <code>int</code>.
     * In other words, the result is equal to the value of the expression:
     * <p>
     * <pre>(int)Math.floor(a + 0.5f)</pre>
     * <p>
     * Special cases:
     * <ul>
     *  <li>If the argument is NaN, the result is 0.
     *  <li>If the argument is negative infinity or any value less than or
     *      equal to the value of <code>Integer.MIN_VALUE</code>, the result is
     *      equal to the value of <code>Integer.MIN_VALUE</code>.
     *  <li>If the argument is positive infinity or any value greater than or
     *      equal to the value of <code>Integer.MAX_VALUE</code>, the result is
     *      equal to the value of <code>Integer.MAX_VALUE</code>.
     * </ul>
     *
     * @param  a - a floating-point value to be rounded to an integer.
     * @return the value of the argument rounded to the nearest <code>int</code> value.
     */
    public static int round(float a) {
        return (int) Math.floor(a + 0.5f);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 05SEP2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the closest <code>long</code> to the argument. The result
     * is rounded to an integer by adding 1/2, taking the floor of the
     * result, and casting the result to type <code>long</code>. In other
     * words, the result is equal to the value of the expression:
     * <p>
     * <pre>(long)Math.floor(a + 0.5d)</pre>
     * <p>
     * Special cases:
     * <ul>
     *  <li>If the argument is NaN, the result is 0.
     *  <li>If the argument is negative infinity or any value less than or
     *      equal to the value of <code>Long.MIN_VALUE</code>, the result is
     *      equal to the value of <code>Long.MIN_VALUE</code>.
     *  <li>If the argument is positive infinity or any value greater than or
     *      equal to the value of <code>Long.MAX_VALUE</code>, the result is
     *      equal to the value of <code>Long.MAX_VALUE</code>.
     * </ul>
     *
     * @param a - a floating-point value to be rounded to a <code>long</code>.
     * @return the value of the argument rounded to the nearest <code>long</code> value.
     */
    public static long round(double a) {
        return (long) Math.floor(a + 0.5);
    }


    static public double exp(double x) {
        if (x == 0.) {
            return 1.;
        }
        //
        double f = 1;
        long d = 1;
        double k;
        boolean isless = (x < 0.);
        if (isless) {
            x = -x;
        }
        k = x / d;
        //
        for (long i = 2; i < 50; i++) {
            f = f + k;
            k = k * x / i;
        }
        //
        if (isless) {
            return 1 / f;
        } else {
            return f;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Returns Euler's number <i>e</i> raised to the power of a
     * <code>double</code> value.  Special cases:
     * <ul><li>If the argument is NaN, the result is NaN.
     * <li>If the argument is positive infinity, then the result is
     * positive infinity.
     * <li>If the argument is negative infinity, then the result is
     * positive zero.</ul> <p>
     * A result must be within 1 ulp of the correctly rounded result.  Results
     * must be semi-monotonic.<P><DD>
     * @param a  the exponent to raise e to.
     * @return the value ea, where e is the base of the natural logarithms.
     */
    public static double exp1(double a) {
        if (a == 0.) {
            return 1.;
        }
        boolean isless = (a < 0.);
        if (isless) {
            a = -a;
        }
        long intPart = (int) a;
        double fractionPart = a - intPart;
        double ret = 1;
        for (long i = 0; i < intPart; i++) {
            ret *= E;
        }
        double n = 1;
        double an = fractionPart;
        double sn = 1;
        double subRes = 1;
        if (fractionPart > 0) {
            subRes += fractionPart;
            while (an > PRECISION) {
                an *= fractionPart;
                n++;
                sn = sn * n;
                subRes += an / sn;

            }
        }
        ret *= subRes;
        if (isless) {
            return 1 / ret;
        } else {
            return ret;
        }
    }

    final static public double LOGdiv2 = -0.6931471805599453094;

    static private double _log(double x) {
        if (!(x > 0.)) {
            return Double.NaN;
        }
        //
        double f = 0.0;
        //
        int appendix = 0;
        while (x > 0.0 && x <= 1.0) {
            x *= 2.0;
            appendix++;
        }
        //
        x /= 2.0;
        appendix--;
        //
        double y1 = x - 1.;
        double y2 = x + 1.;
        double y = y1 / y2;
        //
        double k = y;
        y2 = k * y;
        //
        for (long i = 1; i < 50; i += 2) {
            f += k / i;
            k *= y2;
        }
        //
        f *= 2.0;
        for (int i = 0; i < appendix; i++) {
            f += LOGdiv2;
        }
        //
        return f;
    }

    static public double log(double x) {
        if (!(x > 0.)) {
            return Double.NaN;
        }
        //
        if (x == 1.0) {
            return 0.0;
        }
        // Argument of _log must be (0; 1]
        if (x > 1.) {
            x = 1 / x;
            return -_log(x);
        }
        //
        return _log(x);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Returns the natural logarithm (base <i>e</i>) of a <code>double</code>
     * value.  Special cases:
     * <ul><li>If the argument is NaN or less than zero, then the result
     * is NaN.
     * <li>If the argument is positive infinity, then the result is
     * positive infinity.
     * <li>If the argument is positive zero or negative zero, then the
     * result is negative infinity.</ul><p>
     * A result must be within 1 ulp of the correctly rounded result.  Results
     * must be semi-monotonic.<P><DD>
     * @param a  a number greater than 0.0.
     * @return the value ln a, the natural logarithm of a.
     */
    public static double log1(double a) {
        if (a <= 0.0) {
            return Double.NaN;
        }
        boolean invert = false;
        if (a > 1.) {
            invert = true;
            a = 1 / a;
        }
        if (a == 1.0) {
            return 0.;
        }
        double x = a - 1.0;
        double a0 = x;
        double n = 1;
        double a1 = (-1) * x * n / (n + 1) * a0;
        double s = a0;
        while (Math.abs(a1) > PRECISION) {
            s += a1;
            n += 1.0;
            a0 = a1;
            a1 = (-1) * x * n / (n + 1) * a0;
        }
        if (invert) {
            return -s;
        }
        return s;
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Returns the value of the first argument raised to the power of the
     * second argument. Special cases:
     * <ul><li>If the second argument is positive or negative zero, then the
     * result is 1.0.
     * <li>If the second argument is 1.0, then the result is the same as the
     * first argument.
     * <li>If the second argument is NaN, then the result is NaN.
     * <li>If the first argument is NaN and the second argument is nonzero,
     * then the result is NaN.
     * <li>If
     * <ul>
     * <li>the absolute value of the first argument is greater than 1
     * and the second argument is positive infinity, or
     * <li>the absolute value of the first argument is less than 1 and
     * the second argument is negative infinity,
     * </ul>
     * then the result is positive infinity.
     * <li>If
     * <ul>
     * <li>the absolute value of the first argument is greater than 1 and
     * the second argument is negative infinity, or
     * <li>the absolute value of the
     * first argument is less than 1 and the second argument is positive
     * infinity,
     * </ul>
     * then the result is positive zero.
     * <li>If the absolute value of the first argument equals 1 and the
     * second argument is infinite, then the result is NaN.
     * <li>If
     * <ul>
     * <li>the first argument is positive zero and the second argument
     * is greater than zero, or
     * <li>the first argument is positive infinity and the second
     * argument is less than zero,
     * </ul>
     * then the result is positive zero.
     * <li>If
     * <ul>
     * <li>the first argument is positive zero and the second argument
     * is less than zero, or
     * <li>the first argument is positive infinity and the second
     * argument is greater than zero,
     * </ul>
     * then the result is positive infinity.
     * <li>If
     * <ul>
     * <li>the first argument is negative zero and the second argument
     * is greater than zero but not a finite odd integer, or
     * <li>the first argument is negative infinity and the second
     * argument is less than zero but not a finite odd integer,
     * </ul>
     * then the result is positive zero.
     * <li>If
     * <ul>
     * <li>the first argument is negative zero and the second argument
     * is a positive finite odd integer, or
     * <li>the first argument is negative infinity and the second
     * argument is a negative finite odd integer,
     * </ul>
     * then the result is negative zero.
     * <li>If
     * <ul>
     * <li>the first argument is negative zero and the second argument
     * is less than zero but not a finite odd integer, or
     * <li>the first argument is negative infinity and the second
     * argument is greater than zero but not a finite odd integer,
     * </ul>
     * then the result is positive infinity.
     * <li>If
     * <ul>
     * <li>the first argument is negative zero and the second argument
     * is a negative finite odd integer, or
     * <li>the first argument is negative infinity and the second
     * argument is a positive finite odd integer,
     * </ul>
     * then the result is negative infinity.
     * <li>If the first argument is finite and less than zero
     * <ul>
     * <li> if the second argument is a finite even integer, the
     * result is equal to the result of raising the absolute value of
     * the first argument to the power of the second argument
     * <li>if the second argument is a finite odd integer, the result
     * is equal to the negative of the result of raising the absolute
     * value of the first argument to the power of the second
     * argument
     * <li>if the second argument is finite and not an integer, then
     * the result is NaN.
     * </ul>
     * <li>If both arguments are integers, then the result is exactly equal
     * to the mathematical result of raising the first argument to the power
     * of the second argument if that result can in fact be represented
     * exactly as a <code>double</code> value.</ul>
     * <p>(In the foregoing descriptions, a floating-point value is
     * considered to be an integer if and only if it is finite and a
     * fixed point of the method <A href="/jdk142/api/java/lang/Math.html#ceil
     * (double)"><CODE><tt>ceil</tt></CODE></A> or,
     * equivalently, a fixed point of the method <A href="/jdk142/api/java/lang
     * /Math.html#floor(double)"><CODE><tt>floor</tt></CODE></A>.
     * A value is a fixed point of a one-argument
     * method if and only if the result of applying the method to the
     * value is equal to the value.)
     * <p>A result must be within 1 ulp of the correctly rounded
     * result.  Results must be semi-monotonic.<P><DD>
     * @param a  the base.
     * @param b  the exponent.
     * @return the value <code>a<sup>b</sup></code>.
     */
    public static double pow(double a, double b) {
        if (a == 0.) {
            return 0.;
        }
        if (a == 1.) {
            return 1.;
        }
        if (b == 0.) {
            return 1.;
        }
        if (b == 1.) {
            return a;
        }
        //
        long l = (long) Math.floor(b);
        boolean integerValue = (b == (double) l);
        //
        if (integerValue) {
            boolean neg = false;
            if (b < 0.) {
                neg = true;
            }
            //
            double result = a;
            for (long i = 1; i < (neg ? -l : l); i++) {
                result = result * a;
            }
            //
            if (neg) {
                return 1. / result;
            } else {
                return result;
            }
        } else {
            if (a > 0.) {
                return exp(b * log(a));
            } else {
                return Double.NaN;
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Returns the arc sine of an angle, in the range of -<i>pi</i>/2 through
     * <i>pi</i>/2. Special cases:
     * <ul><li>If the argument is NaN or its absolute value is greater
     * than 1, then the result is NaN.
     * <li>If the argument is zero, then the result is a zero with the
     * same sign as the argument.</ul> <p>
     * A result must be within 1 ulp of the correctly rounded result.  Results
     * must be semi-monotonic.<P><DD>
     * @param a  the value whose arc sine is to be returned.
     * @return the arc sine of the argument.
     */
    public static double asin(double a) {
        if (a < -1. || a > 1.) {
            return Double.NaN;
        }
        if (a == -1.) {
            return -Math.PI / 2;
        }
        if (a == 1) {
            return Math.PI / 2;
        }
        return atan(a / Math.sqrt(1 - a * a));
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 07JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Returns the arc cosine of an angle, in the range of 0.0 through
     * <i>pi</i>.  Special case:
     * <ul><li>If the argument is NaN or its absolute value is greater
     * than 1, then the result is NaN.</ul> <p>
     * A result must be within 1 ulp of the correctly rounded result.  Results
     * must be semi-monotonic.<P><DD>
     * @param a  the value whose arc cosine is to be returned.
     * @return the arc cosine of the argument.
     */
    public static double acos(double a) {
        double f = asin(a);
        if (f == Double.NaN) {
            return f;
        }
        return Math.PI / 2 - f;
    }


    public static double sign(double a){
        if(a==0) return 0;
        if(a>0) {
            return 1.0;
        }else{
            return -1.0;
        }

    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the first floating-point argument with the sign of the
     * second floating-point argument.  Note that unlike the {@link
     * FpUtils#copySign(double, double) copySign} method, this method
     * does not require NaN <code>sign</code> arguments to be treated
     * as positive values; implementations are permitted to treat some
     * NaN arguments as positive and other NaN arguments as negative
     * to allow greater performance.
     *
     * @param magnitude  the parameter providing the magnitude of the result
     * @param sign   the parameter providing the sign of the result
     * @return a value with the magnitude of <code>magnitude</code>
     * and the sign of <code>sign</code>.
     */
    private static double rawCopySign(double magnitude, double sign) {
        return Double.longBitsToDouble((Double.doubleToLongBits(sign) &
                (0x8000000000000000L)) |
                (Double.doubleToLongBits(magnitude) &
                (0x7FF0000000000000L |
                0x000FFFFFFFFFFFFFL)));
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the <code>double</code> value that is closest in value
     * to the argument and is equal to a mathematical integer. If two
     * <code>double</code> values that are mathematical integers are
     * equally close to the value of the argument, the result is the
     * integer value that is even. Special cases:
     * <ul><li>If the argument value is already equal to a mathematical
     * integer, then the result is the same as the argument.
     * <li>If the argument is NaN or an infinity or positive zero or negative
     * zero, then the result is the same as the argument.</ul>
     *
     * @param   a   a value.
     * @return  the closest floating-point value to <code>a</code> that is
     *          equal to a mathematical integer.
     */
    public static double rint(double a) {
        /*
         * If the absolute value of a is not less than 2^52, it
         * is either a finite integer (the double format does not have
         * enough significand bits for a number that large to have any
         * fractional portion), an infinity, or a NaN.  In any of
         * these cases, rint of the argument is the argument.
         *
         * Otherwise, the sum (twoToThe52 + a ) will properly round
         * away any fractional portion of a since ulp(twoToThe52) ==
         * 1.0; subtracting out twoToThe52 from this sum will then be
         * exact and leave the rounded integer portion of a.
         *
         * This method does *not* need to be declared strictfp to get
         * fully reproducible results.  Whether or not a method is
         * declared strictfp can only make a difference in the
         * returned result if some operation would overflow or
         * underflow with strictfp semantics.  The operation
         * (twoToThe52 + a ) cannot overflow since large values of a
         * are screened out; the add cannot underflow since twoToThe52
         * is too large.  The subtraction ((twoToThe52 + a ) -
         * twoToThe52) will be exact as discussed above and thus
         * cannot overflow or meaningfully underflow.  Finally, the
         * last multiply in the return statement is by plus or minus
         * 1.0, which is exact too.
         */
        double twoToThe52 = (double) (1L << 52); // 2^52
        double sign = rawCopySign(1.0, a); // preserve sign info
        a = Math.abs(a);

        if (a < twoToThe52) { // E_min <= ilogb(a) <= 51
            a = ((twoToThe52 + a) - twoToThe52);
        }

        return sign * a; // restore original sign
    }
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 09JUN2007  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Computes the remainder operation on two arguments as prescribed
     * by the IEEE 754 standard.
     * The remainder value is mathematically equal to
     * <code>f1&nbsp;-&nbsp;f2</code>&nbsp;&times;&nbsp;<i>n</i>,
     * where <i>n</i> is the mathematical integer closest to the exact
     * mathematical value of the quotient <code>f1/f2</code>, and if two
     * mathematical integers are equally close to <code>f1/f2</code>,
     * then <i>n</i> is the integer that is even. If the remainder is
     * zero, its sign is the same as the sign of the first argument.
     * Special cases:
     * <ul><li>If either argument is NaN, or the first argument is infinite,
     * or the second argument is positive zero or negative zero, then the
     * result is NaN.
     * <li>If the first argument is finite and the second argument is
     * infinite, then the result is the same as the first argument.</ul>
     *<P><DD>
     * @param   f1 the dividend.
     * @param   f2 the divisor
     * @return  the remainder when f1 is divided by f2
     */
    public static double IEEEremainder(double f1, double f2) {
        if (f2 == 0.0) {
            return Double.NaN;
        }
        double divide = rint(f1 / f2);
        return f1 - divide * f2;
    }
}
