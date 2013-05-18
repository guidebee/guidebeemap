//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 18AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 18AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Driving diretion command element.A direction for a MapStep consists at most
 * 5 direction command elements and has following syntax
 * [Command]-[Adj]-[Road Name]-[Adj]-[Road Name].
 * loosely speaking, 3 elements is enough for a whole command.
 * [Command]-[Road Name]-[Road Name]
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 18/08/09
 * @author      Guidebee Pty Ltd.
 */
public class MapDirectionCommandElement {

    /**
     * no elements.
     */
    public final static int ELEMENT_NONE=0;

    /**
     * Road name elements.
     */
    public final static int ELEMENT_ROAD_NAME=1;

    /**
     * Roundaboout is treated as special road name in the command syntax.
     */
    public final static int ELEMENT_ROAD_NAME_ROUNDABOUT=4;

    /**
     * Conjunction element. Conjunction elements is just used for elaboration.
     * Conjunctions is  onto, on ,towards etc.
     */
    public final static int ELEMENT_CONJUCTION=2;

    /**
     * Command element.
     */
    public final static int ELEMENT_COMMAND=3;

    /**
     * extenstion. entering
     */
    public final static int ELEMENT_EXTENTION_ENTERING=4;


    /**
     * extenstion. go through how many roundabout.
     */
    public final static int ELEMENT_EXTENTION_GO_THROUGH_ROUNDABOUT=5;


    /**
     * From road conjuction index in array
     */
    public static final int FROM_ROAD_CONJUNCTION=0;
    /**
     * From road name index in array
     */
    public static final int FROM_ROAD_NAME=1;
    /**
     * Direction command type index in array
     */
    public static final int DIRECTION_COMMAND=2;
    /**
     * To road conjuection index in array.
     */
    public static final int TO_ROAD_CONJUNCTION=3;
    /**
     * To road name index in array.
     */
    public static final int TO_ROAD_NAME=4;
    /**
     * Extention (entering) index in array.
     */
    public static final int EXTENSION_ENTERING=5;
    /**
     * Extension(go through .. roundabout) index in array.
     */
    public static final int EXTENSION_GO_THROUGH_ROUNDABOUT=6;

    
    /**
     * Description.
     */
    public String description;

    /**
     * Element type.
     */
    public int elementType=ELEMENT_NONE;

    /**
     * Direction command type.
     */
    public MapDirectionCommandType directionCommandType;

    /**
     * road property ,toll road or partial toll road.
     */
    public String roadProperty;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy Constructor.
     * @param element the element object copied from
     */
    public MapDirectionCommandElement(MapDirectionCommandElement element) {
        this.elementType = element.elementType;
        this.description = element.description;

    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 18AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     *  Constructor.
     * @param elementType the element type.
     * @param description 
     */
    public MapDirectionCommandElement(int elementType,String description) {
        this.elementType = elementType;
        this.description = description;

    }

}
