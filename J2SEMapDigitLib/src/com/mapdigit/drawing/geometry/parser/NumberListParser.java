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
 * The <code>NumberListParser</code> class converts attributes
 * conforming to the SVG Tiny definition of coordinate or number
 * list (see <a href="http://www.w3.org/TR/SVG11/types.html#BasicDataTypes">
 * Basic Data Types</a>)..
 *
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 17/04/10
 * @author      Guidebee Pty Ltd.
 */
public class NumberListParser extends NumberParser {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param listStr the string containing the list of numbers
     * @param sep the separator between number values
     * @return An array of numbers
     */
    public float[] parseNumberList(final String listStr, final char sep) {
        setString(listStr);

        current = read();
        skipSpaces();

        boolean requireMore = false;
        float[] numbers = null;
        int cur = 0;
        for (;;) {
            if (current != -1) {
                float v = parseNumber(false);
                if (numbers == null) {
                    numbers = new float[1];
                } else if (numbers.length <= cur) {
                    float[] tmpNumbers = new float[numbers.length * 2];
                    System.arraycopy(numbers, 0, tmpNumbers, 0, numbers.length);
                    numbers = tmpNumbers;
                }
                numbers[cur++] = v;
            } else {
                if (!requireMore) {
                    break;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            skipSpaces();
            requireMore = (current == sep);
            skipSepSpaces(sep);
        }

        if (numbers != null && cur != numbers.length) {
            float[] tmpNumbers = new float[cur];
            System.arraycopy(numbers, 0, tmpNumbers, 0, cur);
            numbers = tmpNumbers;
        }

        return numbers;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20NOV2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * @param listStr the string containing the list of numbers
     * @return An array of numbers
     */
    public float[] parseNumberList(final String listStr) {
        return parseNumberList(listStr, ',');
    }
}
