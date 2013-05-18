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
import java.io.IOException;
import java.io.Reader;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 04JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class convert html string to plain text.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     1.00, 04/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class HTML2Text {


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public HTML2Text(){

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Remove double "\\" and change to one "\"
     * @param source the string need to change
     * @return the result string.
     */
    public String removeDoubleBackSlash(String source){
        StringBuffer result2 = new StringBuffer();
        StringReader input = new StringReader(source);

        try {
            String text = null;
            int c = input.read();

            while (c != -1) // Convert until EOF
            {
                text = "";
                if (c == '\\') {
                    c = input.read();
                    if(c=='\\'){
                       text = "\\";
                    }else{
                        text = "\\" + (char)c;
                    }


                } else {
                    text = "" + (char)c;
                }

                StringBuffer s =  result2;
                s.append(text);

                c = input.read();
            }
        } catch (Exception e) {
            input.close();

        }

        StringBuffer s = result2;
        return s.toString().trim();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * convert \\uxxxx to it's string format
     * @param source string to convert.
     * @return result string.
     */
    public String convertUTF8(String source){

        StringBuffer result2 = new StringBuffer();
        StringReader input = new StringReader(source);

        try {
            String text = null;
            int c = input.read();

            while (c != -1) // Convert until EOF
            {
                text = "";
                if (c == '\\') {
                    c = input.read();
                    switch (c) {
                       case 'u':
                            text="";
                            for(int i=0;i<4;i++){
                               text+= String.valueOf((char)input.read());
                            }
                            text=String.valueOf((char)Integer.parseInt(text, 16));
                            break;
                        case 'x' :
                            text="";
                            for(int i=0;i<2;i++){
                               text+= String.valueOf((char)input.read());
                            }
                            text=String.valueOf((char)Integer.parseInt(text, 16));
                            break;
                        default:
                             text = "\\" + (char)c;

                    }

                } else {
                    text = "" + (char)c;
                }

                StringBuffer s =  result2;
                s.append(text);

                c = input.read();
            }
        } catch (Exception e) {
            input.close();

        }

        StringBuffer s = result2;
        return s.toString().trim();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * a simple UTF8 decoder
     * @param input string like %ee%ff
     * @return a string for the UTF8 decoded.
     */
    public static String utf8decoder(String input) {
        String[] letters = Utils.tokenize(input, '%');
        if (letters.length > 1) {
            try {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < letters.length; i++) {
                    if (letters[i].length() > 0) {
                        sb.append(String.valueOf((char) Integer.parseInt(letters[i], 16)));
                    }
                }
                return sb.toString();
            } catch (Exception e) {
                return input;
            }
        } else {
            return input;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * convert to utf8 string
     * @param b byte array
     * @return result string.
     */
    public static String encodeutf8(byte[] b){
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                   "%"+ Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    //[------------------------------ PUBLIC METHODS --------------------------]
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 04JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert html to plain text
     * @param source HTML string.
     * @return plain text.
     */
    public String convert(String source) {
        StringBuffer result = new StringBuffer();
        StringBuffer result2 = new StringBuffer();
        StringReader input = new StringReader(source);

        try {
            String text = null;
            int c = input.read();

            while (c != -1) // Convert until EOF
            {
                text = "";
                if (c == '<') // It's a tag!!
                {
                    String CurrentTag = getTag(input); // Get the rest of the tag
                    text = convertTag(CurrentTag);
                } else if (c == '&') {
                    String specialchar = getSpecial(input);
                    if (specialchar.equals("lt;") || specialchar.equals("#60")) {
                        text = "<";
                    }
                    else if (specialchar.equals("gt;") || specialchar.equals("#62")) {
                        text = ">";
                    }
                    else if (specialchar.equals("amp;") || specialchar.equals("#38")) {
                        text = "&";
                    }
                    else if (specialchar.equals("nbsp;")) {
                        text = " ";
                    }else if (specialchar.equals("#39")) {
                        text = "'";
                    }
                    else if (specialchar.equals("quot;") || specialchar.equals("#34")) {
                        text = "\"";
                    }
                    else if (specialchar.equals("copy;") || specialchar.equals("#169")) {
                        text = "[Copyright]";
                    }
                    else if (specialchar.equals("reg;") || specialchar.equals("#174")) {
                        text = "[Registered]";
                    }
                    else if (specialchar.equals("trade;") || specialchar.equals("#153")) {
                        text = "[Trademark]";
                    }
                    else {
                        text = "&" + specialchar;
                    }
                } else if (!pre && isWhitespace((char)c)) {
                    StringBuffer s = in_body ? result : result2;
                    if (s.length() > 0 && isWhitespace(s.charAt(s.length()-1))) {
                        text = "";
                    }
                    else {
                        text = " ";
                    }
                } else {
                    text = "" + (char)c;
                }

                StringBuffer s = in_body ? result : result2;
                s.append(text);

                c = input.read();
            }
        } catch (Exception e) {
            input.close();

        }

        StringBuffer s = body_found ? result : result2;
        return s.toString().trim();
    }


    private boolean body_found = false;
    private boolean in_body = false;
    private boolean pre = false;
    private boolean center = false;
    private String href = "";

    private String getTag(Reader r) throws IOException {
        StringBuffer result = new StringBuffer();
        int level = 1;

        result.append('<');
        while (level > 0) {
            int c = r.read();
            if (c == -1) {
                break; // EOF
            } // EOF
            result.append((char)c);
            if (c == '<') {
                level++;
            } else if (c == '>') {
                level--;
            }
        }

        return result.toString();
    }

    private String getSpecial(Reader r) throws IOException {
        StringBuffer result = new StringBuffer();
        r.mark(1);//Mark the present position in the stream
        int c = r.read();

        while (isLetter((char)c)) {
            result.append((char)c);
            r.mark(1);
            c = r.read();
        }

        if (c == ';') {
            result.append(';');
        }
        else {
            r.reset();
        }

        return result.toString();
    }



   private boolean isTag(String s1, String s2) {
        s1 = s1.toLowerCase();
        String t1 = "<" + s2.toLowerCase() + ">";
        String t2 = "<" + s2.toLowerCase() + " ";

        return s1.startsWith(t1) || s1.startsWith(t2);
    }

   private String convertTag(String t) throws IOException {
        String result = "";

        if (isTag(t,"body")) {
            in_body = true; body_found = true; } else if (isTag(t,"/body")) {
            in_body = false; result = ""; } else if (isTag(t,"center")) {
            result = ""; center = true; } else if (isTag(t,"/center")) {
            result = ""; center = false; } else if (isTag(t,"pre")) {
            result = ""; pre = true; } else if (isTag(t,"/pre")) {
            result = ""; pre = false; } else if (isTag(t,"p")) {
            result = "";
        }
            else if (isTag(t,"br")) {
            result = "";
        }
            else if(isTag(t,"h1")||isTag(t,"h2")||isTag(t,"h3")||isTag(t,"h4")||isTag(t,"h5")||isTag(t,"h6")||isTag(t,"h7")) {
            result = "";
        }
            else if(isTag(t,"/h1")||isTag(t,"/h2")||isTag(t,"/h3")||isTag(t,"/h4")||isTag(t,"/h5")||isTag(t,"/h6")||isTag(t,"/h7")) {
            result = "";
        }
            else if (isTag(t,"/dl")) {
            result = "";
        }
            else if (isTag(t,"dd")) {
            result = "  * ";
        }
            else if (isTag(t,"dt")) {
            result = "      ";
        }
            else if (isTag(t,"li")) {
            result = "  * ";
        }
            else if (isTag(t,"/ul")) {
            result = "";
        }
            else if (isTag(t,"/ol")) {
            result = "";
        }
            else if (isTag(t,"hr")) {
            result = "_________________________________________";
        }
            else if (isTag(t,"table")) {
            result = "";
        }
            else if (isTag(t,"/table")) {
            result = "";
        }
            else if (isTag(t,"form")) {
            result = "";
        }
            else if (isTag(t,"/form")) {
            result = "";
        }
            else if (isTag(t,"b")) {
            result = "";
        }
            else if (isTag(t,"/b")) {
            result = "";
        }
            else if (isTag(t,"i")) {
            result = "\"";
        }
            else if (isTag(t,"/i")) {
            result = "\"";
        }
            else if (isTag(t,"img")) {
            int idx = t.indexOf("alt=\"");
            if (idx != -1) {
                idx += 5;
                int idx2 = t.indexOf("\"",idx);
                result = t.substring(idx,idx2);
            }
            } else if (isTag(t,"a")) {
            int idx = t.indexOf("href=\"");
            if (idx != -1) {
                idx += 6;
                int idx2 = t.indexOf("\"",idx);
                href = t.substring(idx,idx2);
            } else {
                href = "";
            }
            } else if (isTag(t,"/a")) {
            if (href.length() > 0) {
                result = " [ " + href + " ]";
                href = "";
            }
            }

        return result;
    }

    private boolean isWhitespace(char ch){
        if(ch==' ' || ch =='\t' || ch =='\n') {
            return true;
        }
        return false;

    }

    private boolean isLetter(char ch){
        if( (ch>='a' && ch<='z') || (ch>='A' && ch<='Z')) {
            return true;
        }
        return false;
    }

    private class StringReader extends Reader {

        private String str;
        private int length;
        private int next = 0;
        private int mark = 0;
        private final Object stringLock=new Object();

        /**
         * Creates a new string reader.
         *
         * @param s  String providing the character stream.
         */
        public StringReader(String s) {
            this.str = s;
            this.length = s.length();
        }

        /** Check to make sure that the stream has not been closed */
        private void ensureOpen() throws IOException {
            if (str == null) {
                throw new IOException("Stream closed");
            }
        }

        /**
         * Reads a single character.
         *
         * @return     The character read, or -1 if the end of the stream has been
         *             reached
         *
         * @exception  IOException  If an I/O error occurs
         */
        public int read() throws IOException {
            synchronized (stringLock) {
                ensureOpen();
                if (next >= length) {
                    return -1;
                }
                return str.charAt(next++);
            }
        }

        /**
         * Reads characters into a portion of an array.
         *
         * @param      cbuf  Destination buffer
         * @param      off   Offset at which to start writing characters
         * @param      len   Maximum number of characters to read
         *
         * @return     The number of characters read, or -1 if the end of the
         *             stream has been reached
         *
         * @exception  IOException  If an I/O error occurs
         */
        public int read(char cbuf[], int off, int len) throws IOException {
            synchronized (stringLock) {
                ensureOpen();
                if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                        ((off + len) > cbuf.length) || ((off + len) < 0)) {
                    throw new IndexOutOfBoundsException();
                } else if (len == 0) {
                    return 0;
                }
                if (next >= length) {
                    return -1;
                }
                int n = Math.min(length - next, len);
                str.getChars(next, next + n, cbuf, off);
                next += n;
                return n;
            }
        }

        /**
         * Skips the specified number of characters in the stream. Returns
         * the number of characters that were skipped.
         *
         * <p>The <code>ns</code> parameter may be negative, even though the
         * <code>skip</code> method of the {@link Reader} superclass throws
         * an exception in this case. Negative values of <code>ns</code> cause the
         * stream to skip backwards. Negative return values indicate a skip
         * backwards. It is not possible to skip backwards past the beginning of
         * the string.
         *
         * <p>If the entire string has been read or skipped, then this method has
         * no effect and always returns 0.
         *
         * @exception  IOException  If an I/O error occurs
         */
        public long skip(long ns) throws IOException {
            synchronized (stringLock) {
                ensureOpen();
                if (next >= length) {
                    return 0;
                }
                // Bound skip by beginning and end of the source
                long n = Math.min(length - next, ns);
                n = Math.max(-next, n);
                next += n;
                return n;
            }
        }

        /**
         * Tells whether this stream is ready to be read.
         *
         * @return True if the next read() is guaranteed not to block for input
         *
         * @exception  IOException  If the stream is closed
         */
        public boolean ready() throws IOException {
            synchronized (stringLock) {
                ensureOpen();
                return true;
            }
        }

        /**
         * Tells whether this stream supports the mark() operation, which it does.
         */
        public boolean markSupported() {
            return true;
        }

        /**
         * Marks the present position in the stream.  Subsequent calls to reset()
         * will reposition the stream to this point.
         *
         * @param  readAheadLimit  Limit on the number of characters that may be
         *                         read while still preserving the mark.  Because
         *                         the stream's input comes from a string, there
         *                         is no actual limit, so this argument must not
         *                         be negative, but is otherwise ignored.
         *
         * @exception  IllegalArgumentException  If readAheadLimit is < 0
         * @exception  IOException  If an I/O error occurs
         */
        public void mark(int readAheadLimit) throws IOException {
            if (readAheadLimit < 0){
                throw new IllegalArgumentException("Read-ahead limit < 0");
            }
            synchronized (stringLock) {
                ensureOpen();
                mark = next;
            }
        }

        /**
         * Resets the stream to the most recent mark, or to the beginning of the
         * string if it has never been marked.
         *
         * @exception  IOException  If an I/O error occurs
         */
        public void reset() throws IOException {
            synchronized (stringLock) {
                ensureOpen();
                next = mark;
            }
        }

        /**
         * Closes the stream and releases any system resources associated with
         * it. Once the stream has been closed, further read(),
         * ready(), mark(), or reset() invocations will throw an IOException.
         * Closing a previously closed stream has no effect.
         */
        public void close() {
            str = null;
        }
    }


}
