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
import java.util.Vector;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 11SEP2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////

class JSONPathTokenizer {
    
    private final String expression;
    private final int length;
    
    private int pos;
    
    JSONPathTokenizer(final String expr) {
        if (expr == null) {
            throw new IllegalArgumentException("path cannot be null");
        }
        
        expression = expr;
        length = expression.length();
        pos = 0;
    }
    
    Vector tokenize() {
        final Vector tokens = new Vector();
        String tok;
        for (pos = 0, tok = next(); !"".equals(tok); tok = next()) {
            tokens.addElement(tok);
        }
        return tokens;
    }
    
    private String next() {
        final StringBuffer sbuf = new StringBuffer();
        
        if (pos >= length) {
            return sbuf.toString();
        }
        
        final char del = expression.charAt(pos);
        if (isDelimiter(del)) {
            pos++;
            sbuf.append(del);
            return sbuf.toString();
        }
        
        for (int i = pos; i < length; i++) {
            final char ch = expression.charAt(i);
            if (isDelimiter(ch)) {
                pos = i;
                return sbuf.toString();
            } else {
                sbuf.append(ch);
            }
        }

        pos = length;
        return sbuf.toString();
    }
    
    static boolean isDelimiter(final char ch) {
        switch (ch) {
            case IJSONPath.SEPARATOR:
            case IJSONPath.ARRAY_START:
            case IJSONPath.ARRAY_END:
                return true;
        }
        return false;
    }
}
