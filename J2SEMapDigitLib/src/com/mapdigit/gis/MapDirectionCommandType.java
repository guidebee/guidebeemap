//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 17AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 17AUG2009  James Shen                 	          Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Driving diretion types.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 17/08/09
 * @author      Guidebee Pty Ltd.
 */
public class MapDirectionCommandType {

    /**
     * Invalid direction.
     */
    public final static int COMMAND_INVALID = -1;
    /**
     * go straight ahead 
     */
    public final static int COMMAND_NO_TURN = 5001;
    /**
     * turn slightly left
     */
    public final static int COMMAND_BEAR_LEFT = 5002;
    /**
     * turn left
     */
    public final static int COMMAND_TURN_LEFT = 5003;
    /**
     *  turn sharply left
     */
    public final static int COMMAND_SHARP_LEFT = 5004;
    /**
     *  U turn.
     */
    public final static int COMMAND_U_TURN = 5005;
    /**
     *  turn sharply right
     */
    public final static int COMMAND_SHARP_RIGHT = 5006;
    /**
     * turn right
     */
    public final static int COMMAND_TURN_RIGHT = 5007;
    /**
     * turn slightly right
     */
    public final static int COMMAND_BEAR_RIGHT = 5008;
    /**
     * road merge command
     */
    public final static int COMMAND_MERGE = 5009;

    /**
     * keep left at the fork to continue
     */
    public final static int COMMAND_KEEP_LEFT=5010;

    /**
     * keep right at the fork to continue
     */
    public final static int COMMAND_KEEP_RIGHT=5011;

    /**
     * reach the destination.
     */
    public final static int COMMAND_REACH_DESTINATION = 5018;
    

    /**
     * round about 1st exit
     */
    public final static int COMMAND_ROUNDABOUT_1_EXIT = 5021;
    /**
     * round about 2nd exit
     */
    public final static int COMMAND_ROUNDABOUT_2_EXIT = 5022;
    /**
     * round about 3rd exit
     */
    public final static int COMMAND_ROUNDABOUT_3_EXIT = 5023;
    /**
     * round about 4th exit
     */
    public final static int COMMAND_ROUNDABOUT_4_EXIT = 5024;
    /**
     * round about 5th exit
     */
    public final static int COMMAND_ROUNDABOUT_5_EXIT = 5025;
    /**
     * round about 6th exit
     */
    public final static int COMMAND_ROUNDABOUT_6_EXIT = 5026;
    /**
     * round about 7th exit
     */
    public final static int COMMAND_ROUNDABOUT_7_EXIT = 5027;
    /**
     * round about 8th exit
     */
    public final static int COMMAND_ROUNDABOUT_8_EXIT = 5028;
    /**
     * round about 9th exit
     */
    public final static int COMMAND_ROUNDABOUT_9_EXIT = 5029;
    /**
     * round about 10th exit
     */
    public final static int COMMAND_ROUNDABOUT_10_EXIT = 5030;

    /**
     * enter highway.
     */
    public final static int COMMAND_ENTER_HIGHWAY = 5040;
    /**
     * enter highway on the left.
     */
    public final static int COMMAND_ENTER_HIGHWAY_LEFT = 5041;
    /**
     * enter highway on the right.
     */
    public final static int COMMAND_ENTER_HIGHWAY_RIGHT = 5042;

    /**
     * exit the highway
     */
    public final static int COMMAND_LEAVE_HIGHWAY = 5043;
    /**
     * exit the highway on the left
     */
    public final static int COMMAND_LEAVE_HIGHWAY_LEFT = 5044;
    /**
     * exit the highway on the right
     */
    public final static int COMMAND_LEAVE_HIGHWAY_RIGHT = 5045;

    /**
     * start command direction, head north direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_N = 5050;
    /**
     * start command direction, head north east direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_NE = 5051;
    /**
     * start command direction, head east direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_E = 5052;
    /**
     * start command direction, head south east direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_SE = 5053;
    /**
     * start command direction, head south direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_S = 5054;
    /**
     * start command direction, head south west direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_SW = 5055;
    /**
     * start command direction, head west direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_W = 5056;
    /**
     * start command direction, head north west direction.
     */
    public final static int COMMAND_HEAD_DIRECTION_NW = 5057;

    /**
     * take the 1st left.
     */
    public final static int COMMAND_TAKE_1_LEFT=5061;

    /**
     * take the 2nd left.
     */
    public final static int COMMAND_TAKE_2_LEFT=5062;

    /**
     * take the 3rd left.
     */
    public final static int COMMAND_TAKE_3_LEFT=5063;

    /**
     * take the 4th left.
     */
    public final static int COMMAND_TAKE_4_LEFT=5064;

    /**
     * take the 5th left.
     */
    public final static int COMMAND_TAKE_5_LEFT=5065;

    /**
     * take the 6th left.
     */
    public final static int COMMAND_TAKE_6_LEFT=5066;

    /**
     * take the 7th left.
     */
    public final static int COMMAND_TAKE_7_LEFT=5067;

    /**
     * take the 8th left.
     */
    public final static int COMMAND_TAKE_8_LEFT=5068;

    /**
     * take the 9th left.
     */
    public final static int COMMAND_TAKE_9_LEFT=5069;

    /**
     * take the 1st right.
     */
    public final static int COMMAND_TAKE_1_RIGHT=5071;

    /**
     * take the 2nd right.
     */
    public final static int COMMAND_TAKE_2_RIGHT=5072;

    /**
     * take the 3rd right.
     */
    public final static int COMMAND_TAKE_3_RIGHT=5073;

    /**
     * take the 4th right.
     */
    public final static int COMMAND_TAKE_4_RIGHT=5074;

    /**
     * take the 5th right.
     */
    public final static int COMMAND_TAKE_5_RIGHT=5075;

    /**
     * take the 6th right.
     */
    public final static int COMMAND_TAKE_6_RIGHT=5076;

    /**
     * take the 7th right.
     */
    public final static int COMMAND_TAKE_7_RIGHT=5077;

    /**
     * take the 8th right.
     */
    public final static int COMMAND_TAKE_8_RIGHT=5078;

    /**
     * take the 9th right.
     */
    public final static int COMMAND_TAKE_9_RIGHT=5079;

    /**
     * destination on the left.
     */
    public final static int COMMAND_DESTINATION_ON_THE_LEFT=5080;

    /**
     * destination on the right.
     */
    public final static int COMMAND_DESTINATION_ON_THE_RIGHT=5081;

    /**
     * direction type.
     */
    public int type = COMMAND_INVALID;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Copy Constructor.
     * @param direction the direction object copied from
     */
    public MapDirectionCommandType(MapDirectionCommandType direction) {
        this.type = direction.type;

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor.
     * @param type the direction type.
     */
    public MapDirectionCommandType(int type) {
        this.type = type;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if it's round about.
     * @return true,it's roundabout.
     */
    public boolean isRoundAbout() {
        return (type >= COMMAND_ROUNDABOUT_1_EXIT &&
                type <= COMMAND_ROUNDABOUT_10_EXIT);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if a junction command
     * @return true,it's a junction.
     */
    public boolean isJunction() {
        return (type >= COMMAND_NO_TURN &&
                type <= COMMAND_MERGE);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if a enter highway command
     * @return true,it's a enter highway command.
     */
    public boolean isEnterHighway() {
        return (type >= COMMAND_ENTER_HIGHWAY &&
                type <= COMMAND_ENTER_HIGHWAY_RIGHT);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if a leave highway command
     * @return true,it's a leave highway command.
     */
    public boolean isLeaveHighway() {
        return (type >= COMMAND_LEAVE_HIGHWAY &&
                type <= COMMAND_LEAVE_HIGHWAY_RIGHT);
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if a start command
     * @return true,it's a a start command.
     */
    public boolean isStart() {
        return (type >= COMMAND_HEAD_DIRECTION_N &&
                type <= COMMAND_HEAD_DIRECTION_NW);
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 17AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Check if a end command
     * @return true,it's a a end command.
     */
    public boolean isEnd() {
        return (type == COMMAND_REACH_DESTINATION);
    }

    public static String getStringFormat(int type){
        String retStr="";
        switch(type){
            case COMMAND_INVALID:
                retStr= "COMMAND_INVALID";
                break;
            case COMMAND_NO_TURN:
                retStr= "COMMAND_NO_TURN";
                break;
            case COMMAND_BEAR_LEFT:
                retStr= "COMMAND_BEAR_LEFT";
                break;
            case COMMAND_TURN_LEFT:
                retStr= "COMMAND_TURN_LEFT";
                break;
            case COMMAND_SHARP_LEFT:
                retStr= "COMMAND_SHARP_LEFT";
                break;
            case COMMAND_U_TURN:
                retStr= "COMMAND_U_TURN";
                break;
            case COMMAND_SHARP_RIGHT:
                retStr= "COMMAND_SHARP_RIGHT";
                break;
            case COMMAND_TURN_RIGHT:
                retStr= "COMMAND_TURN_RIGHT";
                break;
            case COMMAND_BEAR_RIGHT:
                retStr= "COMMAND_BEAR_RIGHT";
                break;
            case COMMAND_MERGE:
                retStr= "COMMAND_MERGE";
                break;
            case COMMAND_KEEP_LEFT:
                retStr= "COMMAND_KEEP_LEFT";
                break;
            case COMMAND_KEEP_RIGHT:
                retStr= "COMMAND_KEEP_RIGHT";
                break;
            case COMMAND_REACH_DESTINATION:
                retStr= "COMMAND_REACH_DESTINATION";
                break;
            case COMMAND_ROUNDABOUT_1_EXIT:
                retStr= "COMMAND_ROUNDABOUT_1_EXIT";
                break;
            case COMMAND_ROUNDABOUT_2_EXIT:
                retStr= "COMMAND_ROUNDABOUT_2_EXIT";
                break;
            case COMMAND_ROUNDABOUT_3_EXIT:
                retStr= "COMMAND_ROUNDABOUT_3_EXIT";
                break;
            case COMMAND_ROUNDABOUT_4_EXIT:
                retStr= "COMMAND_ROUNDABOUT_4_EXIT";
                break;
            case COMMAND_ROUNDABOUT_5_EXIT:
                retStr= "COMMAND_ROUNDABOUT_5_EXIT";
                break;
            case COMMAND_ROUNDABOUT_6_EXIT:
                retStr= "COMMAND_ROUNDABOUT_6_EXIT";
                break;
            case COMMAND_ROUNDABOUT_7_EXIT:
                retStr= "COMMAND_ROUNDABOUT_7_EXIT";
                break;
            case COMMAND_ROUNDABOUT_8_EXIT:
                retStr= "COMMAND_ROUNDABOUT_8_EXIT";
                break;
            case COMMAND_ENTER_HIGHWAY:
                retStr= "COMMAND_ENTER_HIGHWAY";
                break;
            case COMMAND_ENTER_HIGHWAY_LEFT:
                retStr= "COMMAND_ENTER_HIGHWAY_LEFT";
                break;
            case COMMAND_ENTER_HIGHWAY_RIGHT:
                retStr= "COMMAND_ENTER_HIGHWAY_RIGHT";
                break;
            case COMMAND_LEAVE_HIGHWAY:
                retStr= "COMMAND_LEAVE_HIGHWAY";
                break;
            case COMMAND_LEAVE_HIGHWAY_LEFT:
                retStr= "COMMAND_LEAVE_HIGHWAY_LEFT";
                break;
            case COMMAND_LEAVE_HIGHWAY_RIGHT:
                retStr= "COMMAND_LEAVE_HIGHWAY_RIGHT";
                break;
            case COMMAND_HEAD_DIRECTION_N:
                retStr= "COMMAND_HEAD_DIRECTION_N";
                break;
            case COMMAND_HEAD_DIRECTION_NE:
                retStr= "COMMAND_HEAD_DIRECTION_NE";
                break;
            case COMMAND_HEAD_DIRECTION_E:
                retStr= "COMMAND_HEAD_DIRECTION_E";
                break;
            case COMMAND_HEAD_DIRECTION_SE:
                retStr= "COMMAND_HEAD_DIRECTION_SE";
                break;
            case COMMAND_HEAD_DIRECTION_S:
                retStr= "COMMAND_HEAD_DIRECTION_S";
                break;
            case COMMAND_HEAD_DIRECTION_SW:
                retStr= "COMMAND_HEAD_DIRECTION_SW";
                break;
            case COMMAND_HEAD_DIRECTION_W:
                retStr= "COMMAND_HEAD_DIRECTION_W";
                break;
            case COMMAND_HEAD_DIRECTION_NW:
                retStr= "COMMAND_HEAD_DIRECTION_NW";
                break;
            case COMMAND_TAKE_1_LEFT:
                retStr= "COMMAND_TAKE_1_LEFT";
                break;
            case COMMAND_TAKE_2_LEFT:
                retStr= "COMMAND_TAKE_2_LEFT";
                break;
            case COMMAND_TAKE_3_LEFT:
                retStr= "COMMAND_TAKE_3_LEFT";
                break;
            case COMMAND_TAKE_4_LEFT:
                retStr= "COMMAND_TAKE_4_LEFT";
                break;
            case COMMAND_TAKE_5_LEFT:
                retStr= "COMMAND_TAKE_5_LEFT";
                break;
            case COMMAND_TAKE_6_LEFT:
                retStr= "COMMAND_TAKE_6_LEFT";
                break;
            case COMMAND_TAKE_7_LEFT:
                retStr= "COMMAND_TAKE_7_LEFT";
                break;
            case COMMAND_TAKE_8_LEFT:
                retStr= "COMMAND_TAKE_8_LEFT";
                break;
            case COMMAND_TAKE_9_LEFT:
                retStr= "COMMAND_TAKE_9_LEFT";
                break;
            case COMMAND_TAKE_1_RIGHT:
                retStr= "COMMAND_TAKE_1_RIGHT";
                break;
            case COMMAND_TAKE_2_RIGHT:
                retStr= "COMMAND_TAKE_2_RIGHT";
                break;
            case COMMAND_TAKE_3_RIGHT:
                retStr= "COMMAND_TAKE_3_RIGHT";
                break;
            case COMMAND_TAKE_4_RIGHT:
                retStr= "COMMAND_TAKE_4_RIGHT";
                break;
            case COMMAND_TAKE_5_RIGHT:
                retStr= "COMMAND_TAKE_5_RIGHT";
                break;
            case COMMAND_TAKE_6_RIGHT:
                retStr= "COMMAND_TAKE_6_RIGHT";
                break;
            case COMMAND_TAKE_7_RIGHT:
                retStr= "COMMAND_TAKE_7_RIGHT";
                break;
            case COMMAND_TAKE_8_RIGHT:
                retStr= "COMMAND_TAKE_8_RIGHT";
                break;
            case COMMAND_TAKE_9_RIGHT:
                retStr= "COMMAND_TAKE_9_RIGHT";
                break;
            case COMMAND_DESTINATION_ON_THE_LEFT:
                retStr= "COMMAND_DESTINATION_ON_THE_LEFT";
                break;
            case COMMAND_DESTINATION_ON_THE_RIGHT:
                retStr= "COMMAND_DESTINATION_ON_THE_RIGHT";
                break;
        }
        return retStr;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 20AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * to string.
     * @return a string
     */
    public String toString(){
        return getStringFormat(type);
    }
}
