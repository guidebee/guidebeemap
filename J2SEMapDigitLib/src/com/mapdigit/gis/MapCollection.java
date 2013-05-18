//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------


//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 03JAN2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Class MapCollection stands for a collection of map objects.
 * <p></p>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 03/01/09
 * @author      Guidebee Pty Ltd.
 */
public final class MapCollection extends MapObject {
    
    /**
     * The multiPoint part of the collection.
     */
    public MapMultiPoint multiPoint;
    
    /**
     * The multiPline part of the collection.
     */
    public MapMultiPline multiPline;
    
    /**
     * The multiRegion part of the collection.
     */
    public MapMultiRegion multiRegion;
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy constructor.
     * @param mapCollection     map object copy from.
     */
    public MapCollection(MapCollection mapCollection) {
        super(mapCollection);
        setType(MapObject.COLLECTION);
        this.multiPline=null;
        this.multiPoint=null;
        this.multiRegion=null;
        if(mapCollection.multiPline!=null){
            this.multiPline=new MapMultiPline(mapCollection.multiPline);
        }
        if(mapCollection.multiPoint!=null){
            this.multiPoint=new MapMultiPoint(mapCollection.multiPoint);
        }
        if(mapCollection.multiRegion!=null){
            this.multiRegion=new MapMultiRegion(mapCollection.multiRegion);
        }
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor.
     */
    public MapCollection() {
        super();
        setType(MapObject.COLLECTION);
        this.multiPline=null;
        this.multiPoint=null;
        this.multiRegion=null;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the multipoint part the collection.
     * @return the multipoint part the collection
     */
    public MapMultiPoint getMultiPoint() {
        return multiPoint;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the multipoint part the collection.
     * @param multiPoint  the multipoint part the collection.
     */
    public void setMultiPoint(MapMultiPoint multiPoint) {
        this.multiPoint=multiPoint;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the MultiPline part the collection.
     * @return the MultiPline part the collection
     */
    public MapMultiPline getMultiPline() {
        return multiPline;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the multiPline part the collection.
     * @param multiPline  the multiPline part the collection.
     */
    public void setMultiPline(MapMultiPline multiPline) {
        this.multiPline=multiPline;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the multiRegion part the collection.
     * @return the multiRegion part the collection
     */
    public MapMultiRegion getMultiRegion() {
        return multiRegion;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Set the multiRegion part the collection.
     * @param multiRegion  the multiRegion part the collection.
     */
    public void setMultiRegion(MapMultiRegion multiRegion) {
        this.multiRegion=multiRegion;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 03JAN2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Convert to MapInfo String.
     * @return a MapInfo MIF string.
     */
    public String toString(){
        String retStr="COLLECTION ";
        int collectionPart=0;
        if(this.multiPoint!=null) {
            collectionPart++;
        }
        if(this.multiPline!=null) {
            collectionPart++;
        }
        if(this.multiRegion!=null) {
            collectionPart++;
        }
        
        retStr+=collectionPart+CRLF;
        if(multiRegion!=null){
            retStr+=multiRegion.toString();
        }
        
        if(multiPline!=null){
            retStr+=multiPline.toString();
        }
        if(multiPoint!=null){
            retStr+=multiPoint.toString();
        }
        return retStr;
    }
    
}
