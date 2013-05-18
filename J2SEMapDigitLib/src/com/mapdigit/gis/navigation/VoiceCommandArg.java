//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           
//                     
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.navigation;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.geometry.GeoLatLng;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * voice command argument.
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. </b>
 * @version     2.00, 19/09/09
 * @author      Guidebee Pty Ltd.
 */
public class VoiceCommandArg {

    /**
     * voice command type.
     */
    public int voiceCommandType;

    /**
     * sub voice command type ,give more detail about the voice command.
     */
    public int subVoiceCommandType;

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public VoiceCommandArg(int type, int subType, Object arg) {
        subVoiceCommandType=subType;
        voiceCommandType = type;
        commandArgument = arg;
        optional=true;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param type command type.
     * @param arg command argument
     * @param optional is the command optinal or not.
     */
    public VoiceCommandArg(int type, Object arg,boolean optional) {
        voiceCommandType = type;
        commandArgument = arg;
        this.optional=optional;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     */
    public VoiceCommandArg(int type, Object arg) {
        voiceCommandType = type;
        commandArgument = arg;
        optional=true;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the command type.
     * @return the voice command type.
     */
    public int getCommandType() {
        return voiceCommandType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the calculated command type based on the shape of the route.
     * @return the calculated voice command type.
     */
    public int getCalculatedCommandType() {
        return subVoiceCommandType;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the command argument.
     * @return the command argument.
     */
    public Object getCommandArg() {
        return commandArgument;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * get the command argument.
     * @return the command argument.
     */
    public boolean isOptional() {
        return optional;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * return command location.
     * @return command location.
     */
    public GeoLatLng getLocation(){
        return new GeoLatLng(commandLocation);
    }

    /**
     * optioal or not
     */
    boolean optional=true;
    
    /**
     * voice command argument, normally it's road name (string).
     */
    Object commandArgument;

    /**
     * the index of the route.
     */
    int routeIndex;

    /**
     * the index of the step.
     */
    int stepIndex;

    /**
     * the index of the  point
     */
    int pointIndex;

    /**
     * location where the command should be fired.
     */
    GeoLatLng  commandLocation;

    

}
