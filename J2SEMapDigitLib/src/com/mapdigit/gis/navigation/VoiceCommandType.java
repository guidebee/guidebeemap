//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       Name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.navigation;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.MapDirectionCommandType;

//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 19SEP2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * define all voice command type. some command type is duplicated (as defined
 * in MapDirectionCommandType).
 * <P>
 * <hr>
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 19/09/09
 * @author      Guidebee Pty Ltd.
 */
public abstract class VoiceCommandType {

    /**
     * 50 meters or 50 yards.
     */
    public final static int DISTANCE_050 = 50;
    /**
     * 100 meters or 100 yards.
     */
    public final static int DISTANCE_100 = 100;
    /**
     * 150 meters or 150 yards.
     */
    public final static int DISTANCE_150 = 150;
    /**
     * 200 meters or 200 yards.
     */
    public final static int DISTANCE_200 = 200;
    /**
     * 250 meters or 250 yards.
     */
    public final static int DISTANCE_250 = 250;
    /**
     * 300 meters or 300 yards.
     */
    public final static int DISTANCE_300 = 300;
    /**
     * 400 meters or 400 yards.
     */
    public final static int DISTANCE_400 = 400;
    /**
     * 500 meters or 500 yards.
     */
    public final static int DISTANCE_500 = 500;
    /**
     * 600 meters or 600 yards.
     */
    public final static int DISTANCE_600 = 600;
    /**
     * 700 meters or 700 yards.
     */
    public final static int DISTANCE_700 = 700;
    /**
     * 800 meters or 800 yards.
     */
    public final static int DISTANCE_800 = 800;
    /**
     * 900 meters or 900 yards.
     */
    public final static int DISTANCE_900 = 900;

    /**
     * 1000 meters or 1000 yards.
     */
    public final static int DISTANCE_1000 = 1000;

    /**
     * 1100 meters or 1100 yards.
     */
    public final static int DISTANCE_1100 = 1100;

    /**
     * 1200 meters or 1200 yards.
     */
    public final static int DISTANCE_1200 = 1200;

    /**
     * 1300 meters or 1300 yards.
     */
    public final static int DISTANCE_1300 = 1300;

    /**
     * 1400 meters or 1400 yards.
     */
    public final static int DISTANCE_1400 = 1400;

    /**
     * 1500 meters or 1500 yards.
     */
    public final static int DISTANCE_1500 = 1500;

    /**
     * 1600 meters or 1600 yards.
     */
    public final static int DISTANCE_1600 = 1600;


    /**
     * 1700 meters or 1700 yards.
     */
    public final static int DISTANCE_1700 = 1700;


    /**
     * 1800 meters or 1800 yards.
     */
    public final static int DISTANCE_1800 = 1800;


    /**
     * 1900 meters or 1900 yards.
     */
    public final static int DISTANCE_1900 = 1900;

     /**
     * Distance , 2 km or 2 mile
     */
    public final static int DISTANCE_002K = 2000;
    /**
     * Distance , 3 km or 3 mile
     */
    public final static int DISTANCE_003K = 3000;
    /**
     * Distance , 4 km or 4 mile
     */
    public final static int DISTANCE_004K = 4000;
    /**
     * Distance , 5 km or 5 mile
     */
    public final static int DISTANCE_005K = 5000;
    /**
     * Distance , 6 km or 6 mile
     */
    public final static int DISTANCE_006K = 6000;
    /**
     * Distance , 7 km or 7 mile
     */
    public final static int DISTANCE_007K = 7000;
    /**
     * Distance , 8 km or 8 mile
     */
    public final static int DISTANCE_008K = 8000;
    /**
     * Distance , 9 km or 9 mile
     */
    public final static int DISTANCE_009K = 9000;
    /**
     * Distance , 10 km or 10 mile
     */
    public final static int DISTANCE_010K = 10000;
    /**
     * Distance , 15 km or 15 mile
     */
    public final static int DISTANCE_015K = 15000;
    /**
     * Distance , 20 km or 20 mile
     */
    public final static int DISTANCE_020K = 20000;
    /**
     * Distance , 25 km or 25 mile
     */
    public final static int DISTANCE_025K = 25000;
    /**
     * Distance , 30 km or 30 mile
     */
    public final static int DISTANCE_030K = 30000;
    /**
     * Distance , 35 km or 35 mile
     */
    public final static int DISTANCE_035K = 35000;
    /**
     * Distance , 40 km or 40 mile
     */
    public final static int DISTANCE_040K = 40000;
    /**
     * Distance , 45 km or 45 mile
     */
    public final static int DISTANCE_045K = 45000;
    /**
     * Distance , 50 km or 50 mile
     */
    public final static int DISTANCE_050K = 50000;
    /**
     * Distance , 55 km or 55 mile
     */
    public final static int DISTANCE_055K = 55000;
    /**
     * Distance , 60 km or 60 mile
     */
    public final static int DISTANCE_060K = 60000;
    /**
     * Distance , 65 km or 65 mile
     */
    public final static int DISTANCE_065K = 65000;
    /**
     * Distance , 70 km or 70 mile
     */
    public final static int DISTANCE_070K = 70000;
    /**
     * Distance , 75 km or 75 mile
     */
    public final static int DISTANCE_075K = 75000;
    /**
     * Distance , 80 km or 80 mile
     */
    public final static int DISTANCE_080K = 80000;
    /**
     * Distance , 85 km or 85 mile
     */
    public final static int DISTANCE_085K = 85000;
    /**
     * Distance , 90 km or 90 mile
     */
    public final static int DISTANCE_090K = 90000;
    /**
     * Distance , 95 km or 95 mile
     */
    public final static int DISTANCE_095K = 95000;
     /**
     * Distance , 100 km or 100 mile
     */
    public final static int DISTANCE_100K = 100000;

    /**
     * reached destiation.
     */
    public final static int DESTINATION = MapDirectionCommandType.COMMAND_REACH_DESTINATION;
    /**
     * take the exit.
     */
    public final static int ROUNDABOUT_TAKE_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_1_EXIT;
    /**
     * take the first exit.
     */
    public final static int ROUNDABOUT_TAKE_1_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_1_EXIT;
    /**
     * take the second exit.
     */
    public final static int ROUNDABOUT_TAKE_2_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_2_EXIT;
    /**
     * take the third exit.
     */
    public final static int ROUNDABOUT_TAKE_3_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_3_EXIT;
    /**
     * take the 4th exit.
     */
    public final static int ROUNDABOUT_TAKE_4_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_4_EXIT;
    /**
     * take the 5th exit.
     */
    public final static int ROUNDABOUT_TAKE_5_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_5_EXIT;
    /**
     * take the 6th exit.
     */
    public final static int ROUNDABOUT_TAKE_6_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_6_EXIT;
    /**
     * take the 7th exit.
     */
    public final static int ROUNDABOUT_TAKE_7_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_7_EXIT;
    /**
     * take the 8th exit.
     */
    public final static int ROUNDABOUT_TAKE_8_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_8_EXIT;
    /**
     * take the 9th exit.
     */
    public final static int ROUNDABOUT_TAKE_9_EXIT = MapDirectionCommandType.COMMAND_ROUNDABOUT_9_EXIT;
    /**
     * Go straight.
     */
    public final static int GO_STRAIGHT = MapDirectionCommandType.COMMAND_NO_TURN;
    /**
     * keep left.
     */
    public final static int KEEP_LEFT = MapDirectionCommandType.COMMAND_KEEP_LEFT;
    /**
     * turn slight left
     */
    public final static int BEAR_LEFT = MapDirectionCommandType.COMMAND_BEAR_LEFT;
    /**
     * turn left.
     */
    public final static int TURN_LEFT = MapDirectionCommandType.COMMAND_TURN_LEFT;
    /**
     * turn sharp left
     */
    public final static int SHARP_LEFT = MapDirectionCommandType.COMMAND_SHARP_LEFT;
    /**
     * keep right
     */
    public final static int KEEP_RIGHT = MapDirectionCommandType.COMMAND_KEEP_RIGHT;
    /**
     * turn slight left
     */
    public final static int BEAR_RIGHT = MapDirectionCommandType.COMMAND_BEAR_RIGHT;
    /**
     * turn right
     */
    public final static int TURN_RIGHT = MapDirectionCommandType.COMMAND_TURN_RIGHT;
    /**
     * turn sharp right
     */
    public final static int SHARP_RIGHT = MapDirectionCommandType.COMMAND_SHARP_RIGHT;
    /**
     * turn around
     */
    public final static int TURN_AROUND = 9999;
    /**
     * make a uturn.
     */
    public final static int MAKE_U_TURN = MapDirectionCommandType.COMMAND_U_TURN;
    /**
     * enter the motoway.
     */
    public final static int ENTER_MOTOWAY = MapDirectionCommandType.COMMAND_ENTER_HIGHWAY;
    /**
     * enter the motoway on the left
     */
    public final static int ENTER_MOTOWAY_LEFT = MapDirectionCommandType.COMMAND_ENTER_HIGHWAY_LEFT;
    /**
     * enter the motoway on the right
     */
    public final static int ENTER_MOTOWAY_RIGHT = MapDirectionCommandType.COMMAND_ENTER_HIGHWAY_RIGHT;
    /**
     * exit the motoway.
     */
    public final static int EXIT_MOTOWAY = MapDirectionCommandType.COMMAND_LEAVE_HIGHWAY;
    /**
     * exit the motoway
     */
    public final static int EXIT_MOTOWAY_LEFT = MapDirectionCommandType.COMMAND_LEAVE_HIGHWAY_LEFT;
    /**
     * exit the motoway.
     */
    public final static int EXIT_MOTOWAY_RIGHT = MapDirectionCommandType.COMMAND_LEAVE_HIGHWAY_RIGHT;
    /**
     * follow the moto way.
     */
    public final static int FOLLOW_MOTOWAY = MapDirectionCommandType.COMMAND_NO_TURN;
    /**
     * change the motoway.
     */
    public final static int CHANGE_MOTOWAY = MapDirectionCommandType.COMMAND_MERGE;
    /**
     * in the middle lane.
     */
    public final static int IN_MIDDLE_MOTOWAY = MapDirectionCommandType.COMMAND_NO_TURN;
    /**
     * head north.
     */
    public final static int HEAD_NORTH = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_N;
    /**
     * head north east.
     */
    public final static int HEAD_NORTHEAST = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_NE;
    /**
     * head north west.
     */
    public final static int HEAD_NORTHWEST = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_NW;
    /**
     * head east.
     */
    public final static int HEAD_EAST = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_E;
    /**
     * head south.
     */
    public final static int HEAD_SOUTH = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_S;
    /**
     * head west.
     */
    public final static int HEAD_WEST = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_W;
    /**
     * head south east.
     */
    public final static int HEAD_SOUTHEAST = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_SE;
    /**
     * head south west.
     */
    public final static int HEAD_SOUTHWEST = MapDirectionCommandType.COMMAND_HEAD_DIRECTION_SW;
    /**
     * take 1st left
     */
    public final static int TAKE_1_LEFT=MapDirectionCommandType.COMMAND_TAKE_1_LEFT;
    /**
     * take 2nd left
     */
    public final static int TAKE_2_LEFT=MapDirectionCommandType.COMMAND_TAKE_2_LEFT;
    /**
     * take 3rd left
     */
    public final static int TAKE_3_LEFT=MapDirectionCommandType.COMMAND_TAKE_3_LEFT;
    /**
     * take 4th left
     */
    public final static int TAKE_4_LEFT=MapDirectionCommandType.COMMAND_TAKE_4_LEFT;
    /**
     * take 5th left
     */
    public final static int TAKE_5_LEFT=MapDirectionCommandType.COMMAND_TAKE_5_LEFT;
    /**
     * take 6th left
     */
    public final static int TAKE_6_LEFT=MapDirectionCommandType.COMMAND_TAKE_6_LEFT;
    /**
     * take 7th left
     */
    public final static int TAKE_7_LEFT=MapDirectionCommandType.COMMAND_TAKE_7_LEFT;
    /**
     * take 8th left
     */
    public final static int TAKE_8_LEFT=MapDirectionCommandType.COMMAND_TAKE_8_LEFT;
    /**
     * take 9th left
     */
    public final static int TAKE_9_LEFT=MapDirectionCommandType.COMMAND_TAKE_9_LEFT;
    /**
     * take 1st right
     */
    public final static int TAKE_1_RIGHT=MapDirectionCommandType.COMMAND_TAKE_1_RIGHT;
    /**
     * take 2nd right
     */
    public final static int TAKE_2_RIGHT=MapDirectionCommandType.COMMAND_TAKE_2_RIGHT;
    /**
     * take 3rd right
     */
    public final static int TAKE_3_RIGHT=MapDirectionCommandType.COMMAND_TAKE_3_RIGHT;
    /**
     * take 4th right
     */
    public final static int TAKE_4_RIGHT=MapDirectionCommandType.COMMAND_TAKE_4_RIGHT;
    /**
     * take 5th right
     */
    public final static int TAKE_5_RIGHT=MapDirectionCommandType.COMMAND_TAKE_5_RIGHT;
    /**
     * take 6th right
     */
    public final static int TAKE_6_RIGHT=MapDirectionCommandType.COMMAND_TAKE_6_RIGHT;
    /**
     * take 7th right
     */
    public final static int TAKE_7_RIGHT=MapDirectionCommandType.COMMAND_TAKE_7_RIGHT;
    /**
     * take 8th right
     */
    public final static int TAKE_8_RIGHT=MapDirectionCommandType.COMMAND_TAKE_8_RIGHT;
    /**
     * take 9th right
     */
    public final static int TAKE_9_RIGHT=MapDirectionCommandType.COMMAND_TAKE_9_RIGHT;

    /**
     * destination on the left.
     */
    public final static int DESTINATION_ON_THE_LEFT=MapDirectionCommandType
            .COMMAND_DESTINATION_ON_THE_LEFT;

    /**
     * destination on the right.
     */
    public final static int DESTINATION_ON_THE_RIGHT=MapDirectionCommandType
            .COMMAND_DESTINATION_ON_THE_RIGHT;

    //Off road navigation voice command type.
    /**
     * Target at 1 o'clock direction,
     */
    public final static int TARGET_AT_01_OCLOCK=8001;

    /**
     * Target at 2 o'clock direction,
     */
    public final static int TARGET_AT_02_OCLOCK=8002;

    /**
     * Target at 3 o'clock direction,
     */
    public final static int TARGET_AT_03_OCLOCK=8003;

    /**
     * Target at 4 o'clock direction,
     */
    public final static int TARGET_AT_04_OCLOCK=8004;

    /**
     * Target at 5 o'clock direction,
     */
    public final static int TARGET_AT_05_OCLOCK=8005;

    /**
     * Target at 6 o'clock direction,
     */
    public final static int TARGET_AT_06_OCLOCK=8006;

    /**
     * Target at 7 o'clock direction,
     */
    public final static int TARGET_AT_07_OCLOCK=8007;

    /**
     * Target at 8 o'clock direction,
     */
    public final static int TARGET_AT_08_OCLOCK=8008;

    /**
     * Target at 9 o'clock direction,
     */
    public final static int TARGET_AT_09_OCLOCK=8009;


    /**
     * Target at 10 o'clock direction,
     */
    public final static int TARGET_AT_10_OCLOCK=8010;

    /**
     * Target at 11 o'clock direction,
     */
    public final static int TARGET_AT_11_OCLOCK=8011;

    /**
     * Target at 12 o'clock direction,
     */
    public final static int TARGET_AT_12_OCLOCK=8012;

    /**
     * closing target.
     */
    public final static int CLOSING_TARGET=8020;

    /**
     * away from target.
     */
    public final static int AWAYFROM_TARGET=8021;

    /**
     * reached target.
     */
    public final static int REACHED_TARGET=8022;

    /**
     * none command.
     */
    public final static int NONE = -1;


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if it the distance command uses kilometer/meters.
     * @param commandType the command type
     * @return ture uses kilometer/meter otherwise uses mile/yard.
     */
    public static boolean isKilometer(int commandType){
        if(isDistanceCommand(commandType)){
            return !((commandType & DISTANCE_UNIT_MILE)==DISTANCE_UNIT_MILE);
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if it a disntace command.
     * @param type the command type
     * @return ture its a distance command.
     */
    public static boolean isDistanceCommand(int type){
        int newType=type & 0x7FFFFFF;
        switch(newType){
            case DISTANCE_050:
            case DISTANCE_100:
            case DISTANCE_150:
            case DISTANCE_200:
            case DISTANCE_250:
            case DISTANCE_300:
            case DISTANCE_400:
            case DISTANCE_500:
            case DISTANCE_600:
            case DISTANCE_700:
            case DISTANCE_800:
            case DISTANCE_900:
            case DISTANCE_1000:
            case DISTANCE_1100:
            case DISTANCE_1200:
            case DISTANCE_1300:
            case DISTANCE_1400:
            case DISTANCE_1500:
            case DISTANCE_1600:
            case DISTANCE_1700:
            case DISTANCE_1800:
            case DISTANCE_1900:
            case DISTANCE_002K:
            case DISTANCE_003K:
            case DISTANCE_004K:
            case DISTANCE_005K:
            case DISTANCE_006K:
            case DISTANCE_007K:
            case DISTANCE_008K:
            case DISTANCE_009K:
            case DISTANCE_010K:
            case DISTANCE_015K:
            case DISTANCE_020K:
            case DISTANCE_025K:
            case DISTANCE_030K:
            case DISTANCE_035K:
            case DISTANCE_040K:
            case DISTANCE_045K:
            case DISTANCE_050K:
            case DISTANCE_055K:
            case DISTANCE_060K:
            case DISTANCE_065K:
            case DISTANCE_070K:
            case DISTANCE_075K:
            case DISTANCE_080K:
            case DISTANCE_085K:
            case DISTANCE_090K:
            case DISTANCE_095K:
            case DISTANCE_100K:
                return true;

        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 19SEP2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Get the string format
     * @param type voice command type.
     * @return it's string format.
     */
    public static String getStringFormat(int type) {
        String retStr = "";
        int distanceType = 0x7FFFFFFF & type;
        switch (distanceType) {
            case GO_STRAIGHT:
                retStr = "GO_STRAIGHT";
                break;
            case BEAR_LEFT:
                retStr = "BEAR_LEFT";
                break;
            case TURN_LEFT:
                retStr = "TURN_LEFT";
                break;
            case SHARP_LEFT:
                retStr = "SHARP_LEFT";
                break;
            case MAKE_U_TURN:
                retStr = "MAKE_U_TURN";
                break;
            case SHARP_RIGHT:
                retStr = "SHARP_RIGHT";
                break;
            case TURN_RIGHT:
                retStr = "TURN_RIGHT";
                break;
            case BEAR_RIGHT:
                retStr = "BEAR_RIGHT";
                break;
            case KEEP_LEFT:
                retStr = "KEEP_LEFT";
                break;
            case KEEP_RIGHT:
                retStr = "KEEP_RIGHT";
                break;
            case DESTINATION:
                retStr = "DESTATION";
                break;
            case ROUNDABOUT_TAKE_1_EXIT:
                retStr = "ROUNDABOUT_TAKE_1_EXIT";
                break;
            case ROUNDABOUT_TAKE_2_EXIT:
                retStr = "ROUNDABOUT_TAKE_2_EXIT";
                break;
            case ROUNDABOUT_TAKE_3_EXIT:
                retStr = "ROUNDABOUT_TAKE_3_EXIT";
                break;
            case ROUNDABOUT_TAKE_4_EXIT:
                retStr = "ROUNDABOUT_TAKE_4_EXIT";
                break;
            case ROUNDABOUT_TAKE_5_EXIT:
                retStr = "ROUNDABOUT_TAKE_5_EXIT";
                break;
            case ROUNDABOUT_TAKE_6_EXIT:
                retStr = "ROUNDABOUT_TAKE_6_EXIT";
                break;
            case ROUNDABOUT_TAKE_7_EXIT:
                retStr = "ROUNDABOUT_TAKE_7_EXIT";
                break;
            case ROUNDABOUT_TAKE_8_EXIT:
                retStr = "ROUNDABOUT_TAKE_8_EXIT";
                break;
            case ENTER_MOTOWAY:
                retStr = "ENTER_MOTOWAY";
                break;
            case ENTER_MOTOWAY_LEFT:
                retStr = "ENTER_MOTOWAY_LEFT";
                break;
            case ENTER_MOTOWAY_RIGHT:
                retStr = "ENTER_HIGHWAY_RIGHT";
                break;
            case EXIT_MOTOWAY:
                retStr = "EXIT_MOTOWAY";
                break;
            case EXIT_MOTOWAY_LEFT:
                retStr = "EXIT_MOTOWAY_LEFT";
                break;
            case EXIT_MOTOWAY_RIGHT:
                retStr = "EXIT_MOTOWAY_RIGHT";
                break;
            case HEAD_NORTH:
                retStr = "HEAD_NORTH";
                break;
            case HEAD_NORTHEAST:
                retStr = "HEAD_NORTHEAST";
                break;
            case HEAD_EAST:
                retStr = "HEAD_EAST";
                break;
            case HEAD_SOUTHEAST:
                retStr = "HEAD_SOUTHEAST";
                break;
            case HEAD_SOUTH:
                retStr = "HEAD_SOUTH";
                break;
            case HEAD_SOUTHWEST:
                retStr = "HEAD_SOUTHWEST";
                break;
            case HEAD_WEST:
                retStr = "HEAD_WEST";
                break;
            case HEAD_NORTHWEST:
                retStr = "HEAD_NORTHWEST";
                break;
            case DISTANCE_050:
                 retStr = "50";
                break;
            case DISTANCE_100:
                 retStr = "100";
                break;
            case DISTANCE_150:
                 retStr = "150";
                break;
            case DISTANCE_200:
                 retStr = "200";
                break;
            case DISTANCE_250:
                 retStr = "250";
                break;
            case DISTANCE_300:
                 retStr = "300";
                break;
            case DISTANCE_400:
                 retStr = "400";
                break;
            case DISTANCE_500:
                 retStr = "500";
                break;
            case DISTANCE_600:
                 retStr = "600";
                break;
            case DISTANCE_700:
                 retStr = "700";
                break;
            case DISTANCE_800:
                 retStr = "800";
                break;
            case DISTANCE_900:
                 retStr = "900";
                break;
            case DISTANCE_1000:
                retStr = "1000";
                break;
            case DISTANCE_1100:
                retStr = "1100";
                break;
            case DISTANCE_1200:
                retStr = "1200";
                break;
            case DISTANCE_1300:
                retStr = "1300";
                break;
            case DISTANCE_1400:
                retStr = "1400";
                break;
            case DISTANCE_1500:
                retStr = "1500";
                break;
            case DISTANCE_1600:
                retStr = "1600";
                break;
            case DISTANCE_1700:
                retStr = "1700";
                break;
            case DISTANCE_1800:
                retStr = "1800";
                break;
            case DISTANCE_1900:
                retStr = "1900";
                break;
            case DISTANCE_002K:
                retStr = "2K";
                break;
            case DISTANCE_003K:
                retStr = "3K";
                break;
            case DISTANCE_004K:
                retStr = "4K";
                break;
            case DISTANCE_005K:
                retStr = "5K";
                break;
            case DISTANCE_006K:
                retStr = "6K";
                break;
            case DISTANCE_007K:
                retStr = "7K";
                break;
            case DISTANCE_008K:
                retStr = "8K";
                break;
            case DISTANCE_009K:
                retStr = "9K";
                break;
            case DISTANCE_010K:
                retStr = "10K";
                break;
            case DISTANCE_015K:
                retStr = "15K";
                break;
            case DISTANCE_020K:
                retStr = "20K";
                break;
            case DISTANCE_025K:
                retStr = "25K";
                break;
            case DISTANCE_030K:
                retStr = "30K";
                break;
            case DISTANCE_035K:
                retStr = "30K";
                break;
            case DISTANCE_040K:
                retStr = "40K";
                break;
            case DISTANCE_045K:
                retStr = "45K";
                break;
            case DISTANCE_050K:
                retStr = "50K";
                break;
            case DISTANCE_055K:
                retStr = "55K";
                break;
            case DISTANCE_060K:
                retStr = "60K";
                break;
            case DISTANCE_065K:
                retStr = "65K";
                break;
            case DISTANCE_070K:
                retStr = "70K";
                break;
            case DISTANCE_075K:
                retStr = "75K";
                break;
            case DISTANCE_080K:
                retStr = "80K";
                break;
            case DISTANCE_085K:
                retStr = "85K";
                break;
            case DISTANCE_090K:
                retStr = "90K";
                break;
            case DISTANCE_095K:
                retStr = "95K";
                break;
            case DISTANCE_100K:
                retStr = "100K";
                break;
            case TAKE_1_LEFT:
                 retStr = "TAKE_1_LEFT";
                break;
            case TAKE_2_LEFT:
                 retStr = "TAKE_2_LEFT";
                break;
            case TAKE_3_LEFT:
                 retStr = "TAKE_3_LEFT";
                break;
            case TAKE_4_LEFT:
                 retStr = "TAKE_4_LEFT";
                break;
            case TAKE_5_LEFT:
                 retStr = "TAKE_5_LEFT";
                break;
            case TAKE_6_LEFT:
                 retStr = "TAKE_6_LEFT";
                break;
            case TAKE_7_LEFT:
                 retStr = "TAKE_7_LEFT";
                break;
            case TAKE_8_LEFT:
                 retStr = "TAKE_8_LEFT";
                break;
            case TAKE_9_LEFT:
                 retStr = "TAKE_9_LEFT";
                break;
            case TAKE_1_RIGHT:
                 retStr = "TAKE_1_RIGHT";
                break;
            case TAKE_2_RIGHT:
                 retStr = "TAKE_2_RIGHT";
                break;
            case TAKE_3_RIGHT:
                 retStr = "TAKE_3_RIGHT";
                break;
            case TAKE_4_RIGHT:
                 retStr = "TAKE_4_RIGHT";
                break;
            case TAKE_5_RIGHT:
                 retStr = "TAKE_5_RIGHT";
                break;
            case TAKE_6_RIGHT:
                 retStr = "TAKE_6_RIGHT";
                break;
            case TAKE_7_RIGHT:
                 retStr = "TAKE_7_RIGHT";
                break;
            case TAKE_8_RIGHT:
                 retStr = "TAKE_8_RIGHT";
                break;
            case TAKE_9_RIGHT:
                 retStr = "TAKE_9_RIGHT";
                break;
            case DESTINATION_ON_THE_LEFT:
                 retStr = "DESTINATION_ON_THE_LEFT";
                break;
            case DESTINATION_ON_THE_RIGHT:
                 retStr = "DESTINATION_ON_THE_RIGHT";
                break;
            case TARGET_AT_01_OCLOCK:
                 retStr = "TARGET_AT_01_OCLOCK";
                break;
            case TARGET_AT_02_OCLOCK:
                 retStr = "TARGET_AT_02_OCLOCK";
                break;
            case TARGET_AT_03_OCLOCK:
                 retStr = "TARGET_AT_03_OCLOCK";
                break;
            case TARGET_AT_04_OCLOCK:
                 retStr = "TARGET_AT_04_OCLOCK";
                break;
            case TARGET_AT_05_OCLOCK:
                 retStr = "TARGET_AT_05_OCLOCK";
                break;
            case TARGET_AT_06_OCLOCK:
                 retStr = "TARGET_AT_06_OCLOCK";
                break;
            case TARGET_AT_07_OCLOCK:
                 retStr = "TARGET_AT_07_OCLOCK";
                break;
            case TARGET_AT_08_OCLOCK:
                 retStr = "TARGET_AT_08_OCLOCK";
                break;
            case TARGET_AT_09_OCLOCK:
                 retStr = "TARGET_AT_09_OCLOCK";
                break;
            case TARGET_AT_10_OCLOCK:
                 retStr = "TARGET_AT_10_OCLOCK";
                break;
            case TARGET_AT_11_OCLOCK:
                 retStr = "TARGET_AT_11_OCLOCK";
                break;
            case TARGET_AT_12_OCLOCK:
                 retStr = "TARGET_AT_12_OCLOCK";
                break;
            case CLOSING_TARGET:
                retStr = "CLOSING_TARGET";
                break;
            case AWAYFROM_TARGET:
                retStr = "AWAYFROM_TARGET";
                break;
            case REACHED_TARGET:
                retStr = "REACHED_TARGET";
                break;
            case TURN_AROUND:
                retStr = "TURN_AROUND";
                break;
        }
       return retStr;
   }

    /**
     * Distance unit is kilometer/meters ,the highest bit is 0.
     */
    final static int DISTANCE_UNIT_KILOMETER = 0x00000000;
    /**
     * Distance unit is mile/yard ,the highest bit is 1.
     */
    final static int DISTANCE_UNIT_MILE = 0x80000000;

}
