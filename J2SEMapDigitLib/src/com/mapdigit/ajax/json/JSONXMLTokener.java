//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.ajax.json;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The JSONXMLTokener extends the JSONTokener to provide additional methods
 * for the parsing of JSONXML texts.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. </b>
 * @version     2.00, 11/09/10
 * @author      Guidebee Pty Ltd. & JSON.org
 */
class JSONXMLTokener extends JSONTokener {

   /** 
    * The table of entity values. It initially contains Character values for
    * amp, apos, gt, lt, quot.
    */
   public static final java.util.Hashtable entity;

   static {
       entity = new java.util.Hashtable(8);
       entity.put("amp",  JSONXML.AMP);
       entity.put("apos", JSONXML.APOS);
       entity.put("gt",   JSONXML.GT);
       entity.put("lt",   JSONXML.LT);
       entity.put("quot", JSONXML.QUOT);
   }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Construct an JSONXMLTokener from a string.
     * @param s A source string.
     */
    public JSONXMLTokener(String s) {
        super(s);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the text in the CDATA block.
     * @return The string up to the <code>]]&gt;</code>.
     * @throws JSONException If the <code>]]&gt;</code> is not found.
     */
    public String nextCDATA() throws JSONException {
        char         c;
        int          i;
        StringBuffer sb = new StringBuffer();
        for (;;) {
            c = next();
            if (c == 0) {
                throw syntaxError("Unclosed CDATA.");
            }
            sb.append(c);
            i = sb.length() - 3;
            if (i >= 0 && sb.charAt(i) == ']' &&
                          sb.charAt(i + 1) == ']' && sb.charAt(i + 2) == '>') {
                sb.setLength(i);
                return sb.toString();
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the next JSONXML outer token, trimming whitespace. There are two kinds
     * of tokens: the '<' character which begins a markup tag, and the content
     * text between markup tags.
     *
     * @return  A string, or a '<' Character, or null if there is no more
     * source text.
     * @throws JSONException
     */
    public Object nextContent() throws JSONException {
        char         c;
        StringBuffer sb;
        do {
            c = next();
        } while (isWhitespace(c));
        if (c == 0) {
            return null;
        }
        if (c == '<') {
            return JSONXML.LT;
        }
        sb = new StringBuffer();
        for (;;) {
            if (c == '<' || c == 0) {
                back();
                return sb.toString().trim();
            }
            if (c == '&') {
                sb.append(nextEntity(c));
            } else {
                sb.append(c);
            }
            c = next();
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Return the next entity. These entities are translated to Characters:
     *     <code>&amp;  &apos;  &gt;  &lt;  &quot;</code>.
     * @param a An ampersand character.
     * @return  A Character or an entity String if the entity is not recognized.
     * @throws JSONException If missing ';' in JSONXML entity.
     */
    public Object nextEntity(char a) throws JSONException {
        StringBuffer sb = new StringBuffer();
        for (;;) {
            char c = next();
            if (isLetterOrDigit(c) || c == '#') {
                sb.append(Character.toLowerCase(c));
            } else if (c == ';') {
                break;
            } else {
                throw syntaxError("Missing ';' in JSONXML entity: &" + sb);
            }
        }
        String s = sb.toString();
        Object e = entity.get(s);
        return e != null ? e : a + s + ";";
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the next JSONXML meta token. This is used for skipping over <!...>
     * and <?...?> structures.
     * @return Syntax characters (<code>< > / = ! ?</code>) are returned as
     *  Character, and strings and names are returned as Boolean. We don't care
     *  what the values actually are.
     * @throws JSONException If a string is not properly closed or if the JSONXML
     *  is badly structured.
     */
    public Object nextMeta() throws JSONException {
        char c;
        char q;
        do {
            c = next();
        } while (isWhitespace(c));
        switch (c) {
        case 0:
            throw syntaxError("Misshaped meta tag.");
        case '<':
            return JSONXML.LT;
        case '>':
            return JSONXML.GT;
        case '/':
            return JSONXML.SLASH;
        case '=':
            return JSONXML.EQ;
        case '!':
            return JSONXML.BANG;
        case '?':
            return JSONXML.QUEST;
        case '"':
        case '\'':
            q = c;
            for (;;) {
                c = next();
                if (c == 0) {
                    throw syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return Boolean.TRUE;
                }
            }
        default:
            for (;;) {
                c = next();
                if (isWhitespace(c)) {
                    return Boolean.TRUE;
                }
                switch (c) {
                case 0:
                case '<':
                case '>':
                case '/':
                case '=':
                case '!':
                case '?':
                case '"':
                case '\'':
                    back();

                    return Boolean.TRUE;
                }
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the next JSONXML Token. These tokens are found inside of angle
     * brackets. It may be one of these characters: <code>/ > = ! ?</code> or it
     * may be a string wrapped in single quotes or double quotes, or it may be a
     * name.
     * @return a String or a Character.
     * @throws JSONException If the JSONXML is not well formed.
     */
    public Object nextToken() throws JSONException {
        char c;
        char q;
        StringBuffer sb;
        do {
            c = next();
        } while (isWhitespace(c));
        switch (c) {
        case 0:
            throw syntaxError("Misshaped element.");
        case '<':
            throw syntaxError("Misplaced '<'.");
        case '>':
            return JSONXML.GT;
        case '/':
            return JSONXML.SLASH;
        case '=':
            return JSONXML.EQ;
        case '!':
            return JSONXML.BANG;
        case '?':
            return JSONXML.QUEST;

        // Quoted string

        case '"':
        case '\'':
            q = c;
            sb = new StringBuffer();
            for (;;) {
                c = next();
                if (c == 0) {
                    throw syntaxError("Unterminated string.");
                }
                if (c == q) {
                    return sb.toString();
                }
                if (c == '&') {
                    sb.append(nextEntity(c));
                } else {
                    sb.append(c);
                }
            }
        default:

        // Name

            sb = new StringBuffer();
            for (;;) {
                sb.append(c);
                c = next();
                if (isWhitespace(c)) {
                    return sb.toString();
                }
                switch (c) {
                case 0:
                case '>':
                case '/':
                case '=':
                case '!':
                case '?':
                case '[':
                case ']':
                    back();
                    return sb.toString();
                case '<':
                case '"':
                case '\'':
                    throw syntaxError("Bad character in a name.");
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *
     * @param c
     * @return
     */
    private static boolean isWhitespace(char c) {
        switch (c) {
            case ' ':
            case '\r':
            case '\n':
            case '\t':
                return true;
        }
        return false;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 11SEP2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * 
     * @param c
     * @return
     */
    private static boolean isLetterOrDigit(char c) {
        switch (c) {
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

            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':

            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                return true;
        }
        return false;
    }
    
}
