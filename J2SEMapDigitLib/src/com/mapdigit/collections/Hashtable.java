//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.collections;

//--------------------------------- IMPORTS ------------------------------------


//[------------------------------ MAIN CLASS ----------------------------------]

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 17JAN2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * This class implements a hashtable, which maps keys to values.
 * Any non-null object can be used as a key or as a value.also support vector
 * methods.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     1.00, 17/01/09
 * @author      Guidebee Pty Ltd.
 */
public class Hashtable {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new, empty hashtable with a default capacity and load factor.
     * the max length of the vector is given with the input parameter. if the
     * hashtable exceeds max lenght. half of the elements in the hashtable will
     * be removed based on a FIFO policy.
     * @param maxLength the max lenght of the hashtable.
     */
    public Hashtable(int maxLength) {
        MAX_LENGTH = maxLength;
        hashtable = new java.util.Hashtable(MAX_LENGTH);
        vector = new java.util.Vector(MAX_LENGTH);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new, empty hashtable with a default capacity and load factor.
     * the default max lenght is 10.
     */
    public Hashtable() {
        this(10);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Clears this hashtable so that it contains no keys.
     */
    public void clear() {
        synchronized (syncRoot) {
            vector.removeAllElements();
            hashtable.clear();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if some key maps into the specified value in this hashtable.
     * This operation is more expensive than the containsKey method.
     * @param value a value to search for.
     * @return true if some key maps to the value argument in this hashtable;
     * false otherwise.
     */
    public boolean contains(Object value) {
        synchronized (syncRoot) {
            return hashtable.contains(value);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the number of keys in this hashtable.
     * @return the number of keys in this hashtable.
     */
    public int size() {
        synchronized (syncRoot) {
            return hashtable.size();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if this hashtable maps no keys to values.
     * @return true if this hashtable maps no keys to values; false otherwise.
     */
    public boolean isEmpty() {
        synchronized (syncRoot) {
            return hashtable.isEmpty();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tests if the specified object is a key in this hashtable.
     * @param key possible key.
     * @return true if the specified object is a key in this hashtable;
     * false otherwise.
     */
    public boolean containsKey(Object key) {
        synchronized (syncRoot) {
            return hashtable.containsKey(key);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the value to which the specified key is mapped in this hashtable.
     * @param key a key in the hashtable.
     * @return the value to which the key is mapped in this hashtable; null if
     * the key is not mapped to any value in this hashtable.
     */
    public Object get(Object key) {
        synchronized (syncRoot) {
            return hashtable.get(key);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Adds the specified component to the end of this vector.
     * @param obj the component to be added.
     */
    public void addElement(Object obj) {
        put(obj, obj);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * remove half elements in the hastable, based on FIFO policy.
     */
    public void removeHalfElements() {
        synchronized (syncRoot) {
            int totalSize = hashtable.size();
            for (int i = 0; i < totalSize / 2; i++) {
                Object oldKey = vector.firstElement();
                hashtable.remove(oldKey);
                vector.removeElement(oldKey);
            }

        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Maps the specified key to the specified value in this hashtable.
     * Neither the key nor the value can be null.
     * @param key the hashtable key.
     * @param value the value
     * @return the previous value of the specified key in this hashtable,
     * or null if it did not have one.
     */
    public Object put(Object key, Object value) {
        Object object = null;
        synchronized (syncRoot) {
            if (hashtable.size() > MAX_LENGTH) {
                for (int i = 0; i < MAX_LENGTH / 2; i++) {
                    Object oldKey = vector.firstElement();
                    hashtable.remove(oldKey);
                    vector.removeElement(oldKey);
                }
            }
            object = hashtable.put(key, value);
            if (!vector.contains(key)) {
                vector.addElement(key);
            }

            return object;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  Remove all elements in the hashtable.
     */
    public void removeAllElements() {
        synchronized (syncRoot) {
            vector.removeAllElements();
            hashtable.clear();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Removes the key (and its corresponding value) from this hashtable.
     * This method does nothing if the key is not in the hashtable
     * @param key the key that needs to be removed.
     * @return the value to which the key had been mapped in this hashtable,
     * or null if the key did not have a mapping
     */
    public Object remove(Object key) {
        synchronized (syncRoot) {
            vector.removeElement(key);
            return hashtable.remove(key);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the key at given index.
     * @param index the index of the key.
     * @return key object.
     */
    public Object keyAt(int index) {
        synchronized (syncRoot) {
            return vector.elementAt(index);

        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the first component of this vector.
     * @return the first component of this vector.
     */
    public Object firstElement() {
        synchronized (syncRoot) {
            Object key = vector.firstElement();
            return hashtable.get(key);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the last component of the vector.
     * @return the last component of the vector, i.e.,
     * the component at index size() - 1.
     */
    public Object lastElement() {
        synchronized (syncRoot) {
            Object key = vector.lastElement();
            return hashtable.get(key);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the component at the specified index
     * @param index an index into this vector
     * @return the component at the specified index.
     */
    public Object elementAt(int index) {
        synchronized (syncRoot) {
            if (index < vector.size()) {
                Object key = vector.elementAt(index);
                return hashtable.get(key);
            }
            return null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * convert to a string.
     * @return a string.
     */
    public String toString() {
        return vector.toString() + hashtable.toString();
    }
    private final java.util.Hashtable hashtable;
    private final java.util.Vector vector;
    private final int MAX_LENGTH;
    private final Object syncRoot = new Object();
}
