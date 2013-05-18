//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry.parser;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 17APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class represents a parser with support for numbers. Note that
 * the parameter-less form of the <code>parseNumber</code> methods is meant
 * for use by subclasses (e.g., <code>TransformListParser</code>).
 *
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 17/04/10
 * @author      Guidebee Pty Ltd.
 */
public class NumberParser extends AbstractParser {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses the content of the input String and converts it to a float.
     *
     * @param numberString the value to parse
     * @return the corresponding single precision floating point value.
     */
    public float parseNumber(final String numberString) {
        setString(numberString);
        return parseNumber(true);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a float value from the current position in the string
     *
     * @return floating point value corresponding to the parsed string.
     */
    public float parseNumber() {
        return parseNumber(false);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses the next float value in the string.
     *
     * @param eos If eos is set to true, then there should be no more
     * characters at the end of the string.
     * @return floating point value corresponding to the parsed string.
     *         An <code>IllegalArgumentException</code> is thrown if
     *         the next number in the string
     *         does not have a valid number syntax or if eos is true
     *         and there are more characters in the string after the
     *         number.
     */
    public float parseNumber(final boolean eos) {
        int mant = 0;
        int mantDig = 0;
        boolean mantPos = true;
        boolean mantRead = false;

        int exp = 0;
        int expDig = 0;
        int expAdj = 0;
        boolean expPos = true;

        // Only read the next character if the
        // current one is -1
        if (current == -1) {
            current = read();
        }

        // Parse the initial +/- sign if any
        switch (current) {
            case '-':
                mantPos = false;
            case '+':
                current = read();
            default:
            // nothing
        }

        // Now, parse the mantisse
        m1:
        switch (current) {
            default:
                throw new IllegalArgumentException("" + (char) current);

            case '.':
                break;

            case '0':
                mantRead = true;
                l:
                for (;;) {
                    current = read();
                    switch (current) {
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            break l;
                        case '.':
                        case 'e':
                        case 'E':
                            break m1;
                        case -1:
                            break m1;
                        default:
                            if (eos) {
                                throw new IllegalArgumentException(">" + (char) current + "<");
                            } else {
                                return 0;
                            }
                        case '0': // <!>
                    }
                }

            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                mantRead = true;
                l:
                for (;;) {
                    if (mantDig < 9) {
                        mantDig++;
                        mant = mant * 10 + (current - '0');
                    } else {
                        expAdj++;
                    }
                    current = read();
                    switch (current) {
                        default:
                            break l;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                    }
                }
        }

        // If we hit a point, parse the fractional part
        if (current == '.') {
            current = read();
            m2:
            switch (current) {
                default:
                case 'e':
                case 'E':
                    if (!mantRead) {
                        throw new IllegalArgumentException();
                    }
                    break;

                case '0':
                    if (mantDig == 0) {
                        l:
                        for (;;) {
                            current = read();
                            expAdj--;
                            switch (current) {
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    break l;
                                default:
                                    break m2;
                                case '0':
                            }
                        }
                    }
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    l:
                    for (;;) {
                        if (mantDig < 9) {
                            mantDig++;
                            mant = mant * 10 + (current - '0');
                            expAdj--;
                        }
                        current = read();
                        switch (current) {
                            default:
                                break l;
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                        }
                    }
            }
        }

        // Parse the exponent
        switch (current) {
            case 'e':
            case 'E':
                current = read();
                switch (current) {
                    default:
                        throw new IllegalArgumentException();
                    case '-':
                        expPos = false;
                    case '+':
                        current = read();
                        switch (current) {
                            default:
                                throw new IllegalArgumentException();
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                        }
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                }

                en:
                switch (current) {
                    case '0':
                        l:
                        for (;;) {
                            current = read();
                            switch (current) {
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    break l;
                                default:
                                    break en;
                                case '0':
                            }
                        }

                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        l:
                        for (;;) {
                            if (expDig < 3) {
                                expDig++;
                                exp = exp * 10 + (current - '0');
                            }
                            current = read();
                            switch (current) {
                                default:
                                    break l;
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                            }
                        }
                }
            default:
        }

        if (eos && current != -1) {
            throw new IllegalArgumentException();
        }

        if (!expPos) {
            exp = -exp;
        }
        exp += expAdj;
        if (!mantPos) {
            mant = -mant;
        }

        return buildFloat(mant, exp);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Computes a float from mantissa and exponent.
     *
     * @param mant the mantissa for the floating point value to create
     * @param exp the exponent for the floating point value to create
     * @return a single precision floating point value
     *         corresponding to the input mantissa/exponent
     *         pair.
     */
    public static float buildFloat(int mant, final int exp) {
        if (exp < -125 || mant == 0) {
            return 0f;
        }

        if (exp > 128) {
            if (mant > 0) {
                return Float.POSITIVE_INFINITY;
            } else {
                return Float.NEGATIVE_INFINITY;
            }
        }

        if (exp == 0) {
            return mant;
        }

        if (mant >= 1 << 26) {
            mant++;  // round up trailing bits if they will be dropped.
        }

        if (exp > 0) {
            return mant * POW_10[exp];
        } else {
            return mant / POW_10[-exp];
        }
    }
    /**
     * Array of powers of ten.
     */
    private static final float[] POW_10 = new float[128];

    static {
        float cur = 0.1f;
        for (int i = 0; i < POW_10.length; i++) {
            POW_10[i] = cur * 10;
            cur = POW_10[i];
        }
    }

   
}

