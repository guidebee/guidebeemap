//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 01JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.ajax;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 01JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class encodes a user name and password
 * in the format (base 64) that HTTP Basic
 * Authorization requires.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     1.00, 01/01/09
 * @author      Guidebee Pty Ltd.
 */

public class BasicAuth {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Encode a name/password pair appropriate to use in an HTTP header for
     * Basic Authentication.
     * @param   name     the user's name
     * @param   passwd   the user's password
     * @return  the base64 encoded name:password
     */
    public static String encode(String name,
                         String passwd) {
        byte input[] = (name + ":" + passwd).getBytes();
        byte[] output = new byte[((input.length / 3) + 1) * 4];
        int ridx = 0;
        int chunk = 0;

        /**
         * Loop through input with 3-byte stride. For
         * each 'chunk' of 3-bytes, create a 24-bit
         * value, then extract four 6-bit indices.
         * Use these indices to extract the base-64
         * encoding for this 6-bit 'character'
         */
        for (int i = 0; i < input.length; i += 3) {
            int left = input.length - i;

            // have at least three bytes of data left
            if (left > 2) {
                chunk = (input[i] << 16)|
                        (input[i + 1] << 8) |
                         input[i + 2];
                output[ridx++] = cvtTable[(chunk&0xFC0000)>>18];
                output[ridx++] = cvtTable[(chunk&0x3F000) >>12];
                output[ridx++] = cvtTable[(chunk&0xFC0)   >> 6];
                output[ridx++] = cvtTable[(chunk&0x3F)];
            } else if (left == 2) {
                // down to 2 bytes. pad with 1 '='
                chunk = (input[i] << 16) |
                        (input[i + 1] << 8);
                output[ridx++] = cvtTable[(chunk&0xFC0000)>>18];
                output[ridx++] = cvtTable[(chunk&0x3F000) >>12];
                output[ridx++] = cvtTable[(chunk&0xFC0)   >> 6];
                output[ridx++] = '=';
            } else {
                // down to 1 byte. pad with 2 '='
                chunk = input[i] << 16;
                output[ridx++] = cvtTable[(chunk&0xFC0000)>>18];
                output[ridx++] = cvtTable[(chunk&0x3F000) >>12];
                output[ridx++] = '=';
                output[ridx++] = '=';
            }
        }
        return new String(output);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 01JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  make sure no one can instantiate this class
     */
    private BasicAuth() {}

    // conversion table
    private static byte[] cvtTable = {
        (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E',
        (byte)'F', (byte)'G', (byte)'H', (byte)'I', (byte)'J',
        (byte)'K', (byte)'L', (byte)'M', (byte)'N', (byte)'O',
        (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T',
        (byte)'U', (byte)'V', (byte)'W', (byte)'X', (byte)'Y',
        (byte)'Z',
        (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e',
        (byte)'f', (byte)'g', (byte)'h', (byte)'i', (byte)'j',
        (byte)'k', (byte)'l', (byte)'m', (byte)'n', (byte)'o',
        (byte)'p', (byte)'q', (byte)'r', (byte)'s', (byte)'t',
        (byte)'u', (byte)'v', (byte)'w', (byte)'x', (byte)'y',
        (byte)'z',
        (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4',
        (byte)'5', (byte)'6', (byte)'7', (byte)'8', (byte)'9',
        (byte)'+', (byte)'/'
    };

}

