//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 06MAR2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.location;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 14MAR2008  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * The Orientation class represents the physical orientation of the terminal.
 * Orientation is described by azimuth to north (the horizontal pointing 
 * direction), pitch (the vertical elevation angle) and roll (the rotation of 
 * the terminal around its own longitudinal axis).
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2008 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 14/03/08
 * @author      Guidebee Pty Ltd.
 */
public class Orientation {
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructs a new Orientation object with the compass azimuth, pitch and 
     * roll parameters specified.
     * The values are expressed in degress using floating point values.
     * If the pitch or roll is undefined, the parameter shall be given as 
     * Float.NaN.
     * @param azimuth The compass azimuth relative to true or magnetic north. 
     *        Valid range: [0.0, 360.0). 
     * @param isMagnetic a boolean stating whether the compass azimuth is 
     *        given as relative to the magnetic field of the Earth (=true) or to
     *        true north and gravity (=false)
     * @param pitch the pitch of the terminal in degrees, valid range: 
     *        [-90.0, 90.0]
     * @param roll the roll of the terminal in degrees, valid range: 
     *        [-180.0, 180.0)
     */
    public Orientation(float azimuth,boolean isMagnetic,float pitch,float roll){
        
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the terminal's horizontal compass azimuth in degrees relative 
     * to either magnetic or true north. The value is always in the range 
     * [0.0, 360.0) degrees. The isOrientationMagnetic() method indicates 
     * whether the returned azimuth is relative to true north or magnetic north.
     * @return the terminal's compass azimuth in degrees relative to true or 
     * magnetic north. 
     */
    public float getCompassAzimuth(){
        return Float.NaN;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the terminal's tilt in degrees defined as an angle in the 
     * vertical plane orthogonal to the ground, and through the longitudinal 
     * axis of the terminal. The value is always in the range [-90.0, 90.0] 
     * degrees. A negative value means that the top of the terminal screen is 
     * pointing towards the ground. 
     * @return the terminal's pitch in degrees or Float.NaN if not available.
     */
    public float getPitch(){
        return Float.NaN;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns the terminal's rotation in degrees around its own longitudinal 
     * axis. The value is always in the range [-180.0, 180.0) degrees. 
     * A negative value means that the terminal is orientated anti-clockwise 
     * from its default orientation, looking from direction of the bottom of 
     * the screen. 
     * @return the terminal's roll in degrees or Float.NaN if not available.
     */
    public float getRoll(){
        return Float.NaN;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 14MAR2008  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Returns a boolean value that indicates whether this Orientation is 
     * relative to the magnetic field of the Earth or relative to true north 
     * and gravity. If this method returns true, the compass azimuth and pitch 
     * are relative to the magnetic field of the Earth. If this method returns 
     * false, the compass azimuth isrelative to true north and pitch is relative
     * to gravity. 
     * @return true if this Orientation is relative to the magnetic field of 
     * the Earth, false if this Orientation is relative to true north and 
     * gravity.
     */
    public boolean isOrientationMagnetic(){
        return false;
    }
}
