//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.drawing.geometry.parser;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.drawing.geometry.Path;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 17APR2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The <code>PathParser</code> class converts attributes conforming to the
 * SVG <a href="http://www.w3.org/TR/SVG11/paths.html#PathDataBNF">path
 * syntax</a> with the
 * <a href="http://www.w3.org/TR/SVGMobile/#sec-shapes">limitation</a> of SVG
 * Tiny which says that SVG Tiny does not support <code>arc to</code> commands.
 *
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 17/04/10
 * @author      Guidebee Pty Ltd.
 */
public class PathParser extends NumberParser {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the current working path. This can be used,
     * for example, when the parsePath method throws an error
     * to retrieve the state of the path at the time the error
     * occured
     * @return the <code>Path</code> built from the parsed
     *         <code>String</code>
     */
    public Path getPath() {
        return p;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses the input <code>String</code> and returns the corresponding
     * <code>Path</code>.
     *
     * @param s the <code>String</code> to parse.
     * @return the <code>GeneralPath</code> built from the parsed
     * <code>String</code>.
     */
    public Path parsePoints(final String s) {
        setString(s);
        p = new Path();
        current = read();

        skipSpaces();
        if (current == -1) {
            // No coordinate pair
            return p;
        }

        // Initial moveTo
        float x = parseNumber();
        skipCommaSpaces();
        float y = parseNumber();
        p.moveTo((int) x, (int) y);
        lastMoveToX = x;
        lastMoveToY = y;

        while (current != -1) {
            skipSpaces();
            if (current != -1) {
                skipCommaSpaces();
                x = parseNumber();
                skipCommaSpaces();
                y = parseNumber();
                p.lineTo((int) x, (int) y);
            }
        }

        return p;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses the input <code>String</code> and returns the corresponding
     * <code>Path</code> if no error is found. If an error occurs,
     * this method throws an <code>IllegalArgumentException</code>.
     *
     * @param s the <code>String</code> to parse.
     * @return the <code>Path</code> built from the parsed
     *         <code>String</code>
     */
    public Path parsePath(final String s) {
        setString(s);
        p = new Path();


        currentX = 0;
        currentY = 0;
        smoothQCenterX = 0;
        smoothQCenterY = 0;
        smoothCCenterX = 0;
        smoothCCenterY = 0;

        current = read();
        skipSpaces();

        // Multiple coordinate pairs after a moveto
        // are like a moveto followed by lineto
        switch (current) {
            case 'm':
                parsem();
                parsel();
                break;
            case 'M':
                parseM();
                parseL();
                break;
            case -1:
                //an empty path is valid.
                break;
            default:
                throw new IllegalArgumentException();
        }

        loop:
        for (;;) {
            switch (current) {
                case 0xD:
                case 0xA:
                case 0x20:
                case 0x9:
                    current = read();
                    break;
                case 'z':
                case 'Z':
                    current = read();
                    p.closePath();
                    currentX = lastMoveToX;
                    currentY = lastMoveToY;
                    break;
                case 'm':
                    parsem();
                case 'l':
                    parsel();
                    break;
                case 'M':
                    parseM();
                case 'L':
                    parseL();
                    break;
                case 'h':
                    parseh();
                    break;
                case 'H':
                    parseH();
                    break;
                case 'v':
                    parsev();
                    break;
                case 'V':
                    parseV();
                    break;
                case 'c':
                    parsec();
                    break;
                case 'C':
                    parseC();
                    break;
                case 'q':
                    parseq();
                    break;
                case 'Q':
                    parseQ();
                    break;
                case 's':
                    parses();
                    break;
                case 'S':
                    parseS();
                    break;
                case 't':
                    parset();
                    break;
                case 'T':
                    parseT();
                    break;
                case -1:
                    break loop;
                default:
                    throw new IllegalArgumentException();
            }

        }

        skipSpaces();
        if (current != -1) {
            throw new IllegalArgumentException();
        }

        return p;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'm' command.
     */
    protected final void parsem() {
        current = read();
        skipSpaces();

        final float x = parseNumber();
        skipCommaSpaces();
        final float y = parseNumber();

        currentX += x;
        smoothQCenterX = currentX;
        smoothCCenterX = currentX;
        currentY += y;
        smoothQCenterY = currentY;
        smoothCCenterY = currentY;
        p.moveTo((int) smoothCCenterX, (int) smoothCCenterY);
        lastMoveToX = smoothCCenterX;
        lastMoveToY = smoothCCenterY;

        skipCommaSpaces();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'l' command.
     */
    protected final void parsel() {
        if (current == 'l') {
            current = read();
        }
        skipSpaces();
        for (;;) {
            switch (current) {
                case '+':
                case '-':
                case '.':
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
                    float x = parseNumber();
                    skipCommaSpaces();
                    float y = parseNumber();

                    currentX += x;
                    smoothQCenterX = currentX;
                    smoothCCenterX = currentX;

                    currentY += y;
                    smoothQCenterY = currentY;
                    smoothCCenterY = currentY;
                    p.lineTo((int) smoothCCenterX, (int) smoothCCenterY);
                    break;
                default:
                    return;
            }
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'M' command.
     */
    protected final void parseM() {
        current = read();
        skipSpaces();

        float x = parseNumber();
        skipCommaSpaces();
        float y = parseNumber();

        currentX = x;
        smoothQCenterX = x;
        smoothCCenterX = x;

        currentY = y;
        smoothQCenterY = y;
        smoothCCenterY = y;
        p.moveTo((int) x, (int) y);
        lastMoveToX = x;
        lastMoveToY = y;

        skipCommaSpaces();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'L' command.
     */
    protected final void parseL() {
        if (current == 'L') {
            current = read();
        }
        skipSpaces();
        for (;;) {
            switch (current) {
                case '+':
                case '-':
                case '.':
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
                    float x = parseNumber();
                    skipCommaSpaces();
                    float y = parseNumber();

                    currentX = x;
                    smoothQCenterX = x;
                    smoothCCenterX = x;

                    currentY = y;
                    smoothQCenterY = y;
                    smoothCCenterY = y;

                    p.lineTo((int) smoothCCenterX, (int) smoothCCenterY);
                    break;
                default:
                    return;
            }
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'h' command.
     */
    protected final void parseh() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                case '+':
                case '-':
                case '.':
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
                    float x = parseNumber();
                    currentX += x;
                    smoothQCenterX = currentX;
                    smoothCCenterX = currentX;

                    smoothQCenterY = currentY;
                    smoothCCenterY = currentY;
                    p.lineTo((int) smoothCCenterX, (int) smoothCCenterY);
                    break;
                default:
                    return;
            }
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'H' command.
     */
    protected final void parseH() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                case '+':
                case '-':
                case '.':
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
                    float x = parseNumber();
                    currentX = x;
                    smoothQCenterX = x;
                    smoothCCenterX = x;

                    smoothQCenterY = currentY;
                    smoothCCenterY = currentY;
                    p.lineTo((int) smoothCCenterX, (int) smoothCCenterY);
                    break;
                default:
                    return;
            }
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'v' command.
     */
    protected final void parsev() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                case '+':
                case '-':
                case '.':
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
                    float y = parseNumber();
                    smoothQCenterX = currentX;
                    smoothCCenterX = currentX;

                    currentY += y;
                    smoothQCenterY = currentY;
                    smoothCCenterY = currentY;
                    p.lineTo((int) smoothCCenterX, (int) smoothCCenterY);
                    break;
                default:
                    return;
            }
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'V' command.
     */
    protected final void parseV() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                case '+':
                case '-':
                case '.':
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
                    float y = parseNumber();
                    smoothQCenterX = currentX;
                    smoothCCenterX = currentX;

                    currentY = y;
                    smoothQCenterY = y;
                    smoothCCenterY = y;
                    p.lineTo((int) smoothCCenterX, (int) smoothCCenterY);
                    break;
                default:
                    return;
            }
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'c' command.
     */
    protected final void parsec() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x1 = parseNumber();
            skipCommaSpaces();
            float y1 = parseNumber();
            skipCommaSpaces();
            float x2 = parseNumber();
            skipCommaSpaces();
            float y2 = parseNumber();
            skipCommaSpaces();
            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            smoothCCenterX = currentX + x2;
            smoothCCenterY = currentY + y2;
            smoothQCenterX = currentX + x;
            smoothQCenterY = currentY + y;
            p.curveTo((int) (currentX + x1), (int) (currentY + y1),
                    (int) smoothCCenterX, (int) smoothCCenterY,
                    (int) smoothQCenterX, (int) smoothQCenterY);
            currentX = smoothQCenterX;
            currentY = smoothQCenterY;
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'C' command.
     */
    protected final void parseC() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x1 = parseNumber();
            skipCommaSpaces();
            float y1 = parseNumber();
            skipCommaSpaces();
            float x2 = parseNumber();
            skipCommaSpaces();
            float y2 = parseNumber();
            skipCommaSpaces();
            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            smoothCCenterX = x2;
            smoothCCenterY = y2;
            currentX = x;
            currentY = y;
            p.curveTo((int) x1, (int) y1, (int) smoothCCenterX, (int) smoothCCenterY,
                    (int) currentX, (int) currentY);
            smoothQCenterX = currentX;
            smoothQCenterY = currentY;
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'q' command.
     */
    protected final void parseq() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x1 = parseNumber();
            skipCommaSpaces();
            float y1 = parseNumber();
            skipCommaSpaces();
            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            smoothQCenterX = currentX + x1;
            smoothQCenterY = currentY + y1;
            currentX += x;
            currentY += y;
            p.quadTo((int) smoothQCenterX, (int) smoothQCenterY, (int) currentX, (int) currentY);
            smoothCCenterX = currentX;
            smoothCCenterY = currentY;

            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'Q' command.
     */
    protected final void parseQ() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x1 = parseNumber();
            skipCommaSpaces();
            float y1 = parseNumber();
            skipCommaSpaces();
            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            smoothQCenterX = x1;
            smoothQCenterY = y1;
            currentX = x;
            currentY = y;
            p.quadTo((int) smoothQCenterX, (int) smoothQCenterY, (int) currentX, (int) currentY);
            smoothCCenterX = currentX;
            smoothCCenterY = currentY;
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 's' command.
     */
    protected final void parses() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x2 = parseNumber();
            skipCommaSpaces();
            float y2 = parseNumber();
            skipCommaSpaces();
            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            float smoothX = currentX * 2 - smoothCCenterX;
            float smoothY = currentY * 2 - smoothCCenterY;
            smoothCCenterX = currentX + x2;
            smoothCCenterY = currentY + y2;
            currentX += x;
            currentY += y;

            p.curveTo((int) smoothX, (int) smoothY,
                    (int) smoothCCenterX, (int) smoothCCenterY,
                    (int) currentX, (int) currentY);

            smoothQCenterX = currentX;
            smoothQCenterY = currentY;
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'S' command.
     */
    protected final void parseS() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x2 = parseNumber();
            skipCommaSpaces();
            float y2 = parseNumber();
            skipCommaSpaces();
            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            float smoothX = currentX * 2 - smoothCCenterX;
            float smoothY = currentY * 2 - smoothCCenterY;
            currentX = x;
            currentY = y;
            p.curveTo((int) smoothX, (int) smoothY,
                    (int) x2, (int) y2,
                    (int) currentX, (int) currentY);
            smoothCCenterX = x2;
            smoothCCenterY = y2;
            smoothQCenterX = currentX;
            smoothQCenterY = currentY;

            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 't' command.
     */
    protected final void parset() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            smoothQCenterX = currentX * 2 - smoothQCenterX;
            smoothQCenterY = currentY * 2 - smoothQCenterY;
            currentX += x;
            currentY += y;
            p.quadTo((int) smoothQCenterX, (int) smoothQCenterY, (int) currentX, (int) currentY);
            smoothCCenterX = currentX;
            smoothCCenterY = currentY;
            skipCommaSpaces();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17APR2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Parses a 'T' command.
     */
    protected final void parseT() {
        current = read();
        skipSpaces();

        for (;;) {
            switch (current) {
                default:
                    return;
                case '+':
                case '-':
                case '.':
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

            float x = parseNumber();
            skipCommaSpaces();
            float y = parseNumber();

            smoothQCenterX = currentX * 2 - smoothQCenterX;
            smoothQCenterY = currentY * 2 - smoothQCenterY;
            currentX = x;
            currentY = y;
            p.quadTo((int) smoothQCenterX, (int) smoothQCenterY,
                    (int) currentX, (int) currentY);
            smoothCCenterX = currentX;
            smoothCCenterY = currentY;
            skipCommaSpaces();
        }
    }
    
    /**
     * Current x and y positions in the path, set by
     * commands such as moveTo or lineTo.
     */
    private float currentX, currentY;
    /**
     * Last moveTo command.
     */
    private float lastMoveToX, lastMoveToY;
    /**
     * The smoothQCenter point is used for smootg quad curves
     */
    private float smoothQCenterX, smoothQCenterY;
    /**
     * The smoothQCenter point is used for smooth cubic curves
     */
    private float smoothCCenterX, smoothCCenterY;
    /**
     * The GeneralPath under construction
     */
    private Path p;
}
