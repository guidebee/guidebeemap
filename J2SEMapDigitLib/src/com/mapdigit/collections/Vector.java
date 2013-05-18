//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.collections;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 17JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
* The class provides J2SE version alike Vector.
* <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
* @version     1.00, 17/01/09
* @author      Guidebee Pty Ltd.
*/
public class Vector extends java.util.Vector {

    //[------------------------------ CONSTRUCTOR -----------------------------]
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs an empty vector.
     */
    public Vector() {
        super();
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs an empty vector with the specified initial capacity.
     * @param initialCapacity the initial capacity of the vector
     */
    public Vector(int initialCapacity){
        super(initialCapacity);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs an empty vector with the specified initial capacity.
     * @param initialCapacity the initial capacity of the vector
     * @param capacityIncrement the amount by which the capacity is increased
     * when the vector overflows
     */
    public Vector(int initialCapacity, int capacityIncrement){
        super(initialCapacity,capacityIncrement);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *<DD>Inserts the specified element at the specified position in this Vector.
     * Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices).
     *<P><DD>
     * @param index at which the specified element is to be inserted.
     * @param element element to be inserted.
     */
    public void add(int index, Object element){
        insertElementAt(element,index);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Appends the specified element to the end of this Vector.
     * @param o element to be appended to this Vector..
     */
    public boolean add(Object o){
        addElement(o);
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Removes all of the elements from this Vector. The Vector will be
     * empty after this call returns (
     */
    public void clear(){
        removeAllElements();
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the element at the specified position in this Vector.
     * @param index index of element to return.
     * @return object at the specified index.
     */
    public Object get(int index){
        return elementAt(index);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns an array containing all of the elements in this Vector in the
     * correct order; the runtime type of the returned array is that of the
     * specified array.  If the Vector fits in the specified array, it is
     * returned therein.  Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this Vector.<p>
     * If the Vector fits in the specified array with room to spare
     * (i.e., the array has more elements than the Vector),
     * the element in the array immediately following the end of the
     * Vector is set to null.  This is useful in determining the length
     * of the Vector <em>only</em> if the caller knows that the Vector
     * does not contain any null elements.<P><DD>.
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray(){
        Object []retArray=new Object[size()];
        copyInto(retArray);
        return retArray;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * <DD>Returns an array containing all of the elements in this Vector in the
     * correct order; the runtime type of the returned array is that of the
     * specified array.  If the Vector fits in the specified array, it is
     * returned therein.  Otherwise, a new array is allocated with the runtime
     * type of the specified array and the size of this Vector.<p>
     * If the Vector fits in the specified array with room to spare
     * (i.e., the array has more elements than the Vector),
     * the element in the array immediately following the end of the
     * Vector is set to null.  This is useful in determining the length
     * of the Vector <em>only</em> if the caller knows that the Vector
     * does not contain any null elements.<P><DD>.
     * @param a an array to copy to.
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray(Object[] a) {
        int elementCounts = size();
        if (a.length < elementCounts) {
            return (Object[]) Arrays.copyOf(elementData, elementCounts);
        }

        System.arraycopy(elementData, 0, a, 0, elementCounts);

        if (a.length > elementCounts) {
            a[elementCounts] = null;
        }

        return a;


    }





}
