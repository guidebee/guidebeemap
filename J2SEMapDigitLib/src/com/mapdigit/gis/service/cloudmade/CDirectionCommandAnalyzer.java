//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 29DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.cloudmade;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.MapDirectionCommandType;
import com.mapdigit.gis.MapDirectionCommandElement;
import java.util.Hashtable;

import com.mapdigit.util.Log;
//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 29DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Driving diretion command analyser.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 29/12/10
 * @author      Guidebee Pty Ltd.
 */
public abstract class CDirectionCommandAnalyzer {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Analyse the input direction in text (English).
     * @param direction the input direction description.
     * @param road Name.
     * @return command element list (7 elements).
     * @throws java.lang.IllegalArgumentException
     */
    public static MapDirectionCommandElement[] analyse(String direction,String roadName) {
        synchronized (syncObject) {

            resultDirectionCommandElements
                        =new MapDirectionCommandElement[7];
            resultDirectionCommandElements[MapDirectionCommandElement.FROM_ROAD_NAME]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,roadName);
            resultDirectionCommandElements[MapDirectionCommandElement.TO_ROAD_NAME]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,roadName);
            if (hasCommand(direction)) {

                resultDirectionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        =new MapDirectionCommandElement
                        (MapDirectionCommandElement.ELEMENT_COMMAND,
                        commandText);
                resultDirectionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        .directionCommandType=
                        new MapDirectionCommandType(directionCommandType);
            }else{
                Log.p("Wrong direction:"+direction, Log.DEBUG);
                resultDirectionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        =new MapDirectionCommandElement
                        (MapDirectionCommandElement.ELEMENT_COMMAND,
                        "merge");
                resultDirectionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        .directionCommandType=
                        new MapDirectionCommandType(MapDirectionCommandType.COMMAND_MERGE);
            }

            return resultDirectionCommandElements;
        }

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * private constructor.
     */
    private CDirectionCommandAnalyzer() {
    }

    private final static String COMMAND_NO_TURN[] = {
        "c",
    };

    private final static String COMMAND_BEAR_LEFT[] = {
        "tsll"
    };

    private final static String COMMAND_TURN_LEFT[] = {
        "tl"
    };

    private final static String COMMAND_SHARP_LEFT[] = {
        "tshl",
       };

    private final static String COMMAND_SHARP_RIGHT[] = {
        "tshr",
     };

    private final static String COMMAND_TURN_RIGHT[] = {
        "tr"
    };

    private final static String COMMAND_BEAR_RIGHT[] = {
        "tslr"
    };

    private final static String COMMAND_MERGE[] = {
        "merge",
        "enter"
    };


    private final static String COMMAND_KEEP_LEFT[] = {
        "keep left"

    };

    private final static String COMMAND_KEEP_RIGHT[] = {
        "keep right"
    };

    private final static String COMMAND_ENTER_HIGHWAY[] = {
        "take the ramp"
    };

    private final static String COMMAND_ENTER_HIGHWAY_LEFT[] = {
        "take the ramp on the left",
        "slight left onto the ramp",
    };

    private final static String COMMAND_ENTER_HIGHWAY_RIGHT[] = {
        "take the ramp on the right",
        "slight right onto the ramp"
    };

    private final static String COMMAND_LEAVE_HIGHWAY[] = {
        "take the exit",
        "take exit",
        "exit",

    };

    private final static String COMMAND_LEAVE_HIGHWAY_LEFT[] = {
        "take the exit on the left"
    };

    private final static String COMMAND_LEAVE_HIGHWAY_RIGHT[] = {
        "take the exit on the right"
    };

    private final static String COMMAND_U_TURN[]={
        "tu",

    };

    //the following are multi commands.
    private final static String COMMAND_ROUNDABOUT_EXIT[] = {
        "take the",
        "exit"
    };

    private final static String COMMAND_TAKE_LEFT[] = {
        "take the",
        "left"
    };

    private final static String COMMAND_TAKE_RIGHT[] = {
        "take the",
        "right"
    };

    private final static String COMMAND_HEAD_DIRECTION[] = {
        "head",
        "north",
        "northeast",
        "east",
        "southeast",
        "south",
        "southwest",
        "west",
        "northwest"
    };

    private final static String COMMAND_DESTINATION_ON_THE_LEFT[]={
        "destination will be on the left",
    };

    private final static String COMMAND_DESTINATION_ON_THE_RIGHT[]={
        "destination will be on the right",
    };


    private final static String COMMAND_LIST[][]={

        COMMAND_ENTER_HIGHWAY,
        COMMAND_ENTER_HIGHWAY_LEFT,
        COMMAND_ENTER_HIGHWAY_RIGHT,
        COMMAND_LEAVE_HIGHWAY,
        COMMAND_LEAVE_HIGHWAY_LEFT,
        COMMAND_LEAVE_HIGHWAY_RIGHT,
        COMMAND_BEAR_LEFT,
        COMMAND_TURN_LEFT,
        COMMAND_SHARP_LEFT,
        COMMAND_SHARP_RIGHT,
        COMMAND_TURN_RIGHT,
        COMMAND_BEAR_RIGHT,
        COMMAND_KEEP_LEFT,
        COMMAND_KEEP_RIGHT,
        COMMAND_MERGE,
        COMMAND_NO_TURN,
        COMMAND_U_TURN,
        //COMMAND_DESTINATION_ON_THE_LEFT,
        //COMMAND_DESTINATION_ON_THE_RIGHT
        //COMMAND_ROUNDABOUT_EXIT,
        //COMMAND_HEAD_DIRECTION,
        //COMMAND_TAKE_RIGHT,
        //COMMAND_TAKE_LEFT
    };

    private static String COMMAND_ALL_LIST[] = null;

    private static String COMMAND_DESTINAION_ALL_LIST[] = null;

    //initialized the all command list.
    static {

        int total = COMMAND_DESTINATION_ON_THE_LEFT.length
                +COMMAND_DESTINATION_ON_THE_RIGHT.length;
        COMMAND_DESTINAION_ALL_LIST=new String[total];
        total=0;
        for (int i = 0; i < COMMAND_DESTINATION_ON_THE_LEFT.length; i++) {
            COMMAND_DESTINAION_ALL_LIST[total++]=COMMAND_DESTINATION_ON_THE_LEFT[i];
        }
        for (int i = 0; i < COMMAND_DESTINATION_ON_THE_RIGHT.length; i++) {
            COMMAND_DESTINAION_ALL_LIST[total++]=COMMAND_DESTINATION_ON_THE_RIGHT[i];
        }


        total=0;
        for (int i = 0; i < COMMAND_LIST.length; i++) {
            String[] commands = COMMAND_LIST[i];
            if (commands != COMMAND_ROUNDABOUT_EXIT &&
                    commands != COMMAND_HEAD_DIRECTION) {
                total += commands.length;
            }
        }

        //add head command.
        total += COMMAND_HEAD_DIRECTION.length - 1;

        //add roadabout command.
        total += 9;

        //add take the left command.
        total += 9;

        //add take the right command.
        total += 9;


        COMMAND_ALL_LIST = new String[total];
        total = 0;
        for (int i = 0; i < COMMAND_LIST.length; i++) {
            String[] commands = COMMAND_LIST[i];
            if (commands != COMMAND_ROUNDABOUT_EXIT &&
                    commands != COMMAND_HEAD_DIRECTION) {
                for (int j = 0; j < commands.length; j++) {
                    COMMAND_ALL_LIST[total++] = commands[j];
                }
            }
        }


        //add head command
        for (int k = 1; k < COMMAND_HEAD_DIRECTION.length; k++) {
            String command = COMMAND_HEAD_DIRECTION[0] + " " + COMMAND_HEAD_DIRECTION[k];
            COMMAND_ALL_LIST[total++] = command;
        }

        //add exit round command
        COMMAND_ALL_LIST[total++] = COMMAND_ROUNDABOUT_EXIT[0]
                + " 1st " + COMMAND_ROUNDABOUT_EXIT[1];
        COMMAND_ALL_LIST[total++] = COMMAND_ROUNDABOUT_EXIT[0]
                + " 2nd " + COMMAND_ROUNDABOUT_EXIT[1];
        COMMAND_ALL_LIST[total++] = COMMAND_ROUNDABOUT_EXIT[0]
                + " 3rd " + COMMAND_ROUNDABOUT_EXIT[1];

        for (int k = 4; k < 10; k++) {
            String command = COMMAND_ROUNDABOUT_EXIT[0]
                    + " " + k + "th " + COMMAND_ROUNDABOUT_EXIT[1];
            COMMAND_ALL_LIST[total++] = command;
        }

        //add take x left command
        COMMAND_ALL_LIST[total++] = COMMAND_TAKE_LEFT[0]
                + " 1st " + COMMAND_TAKE_LEFT[1];
        COMMAND_ALL_LIST[total++] = COMMAND_TAKE_LEFT[0]
                + " 2nd " + COMMAND_TAKE_LEFT[1];
        COMMAND_ALL_LIST[total++] = COMMAND_TAKE_LEFT[0]
                + " 3rd " + COMMAND_TAKE_LEFT[1];

        for (int k = 4; k < 10; k++) {
            String command = COMMAND_TAKE_LEFT[0]
                    + " " + k + "th " + COMMAND_TAKE_LEFT[1];
            COMMAND_ALL_LIST[total++] = command;
        }

        //add take x right command
        COMMAND_ALL_LIST[total++] = COMMAND_TAKE_RIGHT[0]
                + " 1st " + COMMAND_TAKE_RIGHT[1];
        COMMAND_ALL_LIST[total++] = COMMAND_TAKE_RIGHT[0]
                + " 2nd " + COMMAND_TAKE_RIGHT[1];
        COMMAND_ALL_LIST[total++] = COMMAND_TAKE_RIGHT[0]
                + " 3rd " + COMMAND_TAKE_RIGHT[1];

        for (int k = 4; k < 10; k++) {
            String command = COMMAND_TAKE_RIGHT[0]
                    + " " + k + "th " + COMMAND_TAKE_RIGHT[1];
            COMMAND_ALL_LIST[total++] = command;
        }

        for(int i=0;i<COMMAND_ALL_LIST.length-1;i++){
            for(int j=i+1;j<COMMAND_ALL_LIST.length;j++){
                if(COMMAND_ALL_LIST[i].length()<COMMAND_ALL_LIST[j].length()){
                    String temp=COMMAND_ALL_LIST[i];
                    COMMAND_ALL_LIST[i]=COMMAND_ALL_LIST[j];
                    COMMAND_ALL_LIST[j]=temp;
                }
            }
        }

    }

    private final static Object syncObject=new Object();

    private static String commandText;
    private static int commandIndex;
    private static MapDirectionCommandType directionCommandType;
    private static MapDirectionCommandElement[] resultDirectionCommandElements;

    private static Hashtable DirectionCommandTypes=new Hashtable();

    //initialized the hashtable.
    static{
        for(int i=0;i<COMMAND_NO_TURN.length;i++){
            DirectionCommandTypes.put(COMMAND_NO_TURN[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_NO_TURN));
        }
        for(int i=0;i<COMMAND_TURN_LEFT.length;i++){
            DirectionCommandTypes.put(COMMAND_TURN_LEFT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_TURN_LEFT));
        }
        for(int i=0;i<COMMAND_BEAR_LEFT.length;i++){
            DirectionCommandTypes.put(COMMAND_BEAR_LEFT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_BEAR_LEFT));
        }
        for(int i=0;i<COMMAND_SHARP_LEFT.length;i++){
            DirectionCommandTypes.put(COMMAND_SHARP_LEFT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_SHARP_LEFT));
        }
        for(int i=0;i<COMMAND_SHARP_RIGHT.length;i++){
            DirectionCommandTypes.put(COMMAND_SHARP_RIGHT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_SHARP_RIGHT));
        }
        for(int i=0;i<COMMAND_TURN_RIGHT.length;i++){
            DirectionCommandTypes.put(COMMAND_TURN_RIGHT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_TURN_RIGHT));
        }
        for(int i=0;i<COMMAND_BEAR_RIGHT.length;i++){
            DirectionCommandTypes.put(COMMAND_BEAR_RIGHT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_BEAR_RIGHT));
        }
        for(int i=0;i<COMMAND_MERGE.length;i++){
            DirectionCommandTypes.put(COMMAND_MERGE[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_MERGE));
        }
        for(int i=0;i<COMMAND_ENTER_HIGHWAY.length;i++){
            DirectionCommandTypes.put(COMMAND_ENTER_HIGHWAY[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_ENTER_HIGHWAY));
        }
        for(int i=0;i<COMMAND_ENTER_HIGHWAY_LEFT.length;i++){
            DirectionCommandTypes.put(COMMAND_ENTER_HIGHWAY_LEFT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_ENTER_HIGHWAY_LEFT));
        }
        for(int i=0;i<COMMAND_ENTER_HIGHWAY_RIGHT.length;i++){
            DirectionCommandTypes.put(COMMAND_ENTER_HIGHWAY_RIGHT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_ENTER_HIGHWAY_RIGHT));
        }
        for(int i=0;i<COMMAND_LEAVE_HIGHWAY.length;i++){
            DirectionCommandTypes.put(COMMAND_LEAVE_HIGHWAY[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_LEAVE_HIGHWAY));
        }
        for(int i=0;i<COMMAND_LEAVE_HIGHWAY_LEFT.length;i++){
            DirectionCommandTypes.put(COMMAND_LEAVE_HIGHWAY_LEFT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_LEAVE_HIGHWAY_LEFT));
        }
        for(int i=0;i<COMMAND_LEAVE_HIGHWAY_RIGHT.length;i++){
            DirectionCommandTypes.put(COMMAND_LEAVE_HIGHWAY_RIGHT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_LEAVE_HIGHWAY_RIGHT));
        }
        for(int i=0;i<COMMAND_KEEP_LEFT.length;i++){
            DirectionCommandTypes.put(COMMAND_KEEP_LEFT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_KEEP_LEFT));
        }
        for(int i=0;i<COMMAND_KEEP_RIGHT.length;i++){
            DirectionCommandTypes.put(COMMAND_KEEP_RIGHT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_KEEP_RIGHT));
        }
        for(int i=0;i<COMMAND_U_TURN.length;i++){
            DirectionCommandTypes.put(COMMAND_U_TURN[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_U_TURN));
        }
        for(int i=0;i<COMMAND_DESTINATION_ON_THE_LEFT.length;i++){
            DirectionCommandTypes.put(COMMAND_DESTINATION_ON_THE_LEFT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_DESTINATION_ON_THE_LEFT));
        }
        for(int i=0;i<COMMAND_DESTINATION_ON_THE_RIGHT.length;i++){
            DirectionCommandTypes.put(COMMAND_DESTINATION_ON_THE_RIGHT[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_DESTINATION_ON_THE_RIGHT));
        }
        //roundabout
        DirectionCommandTypes.put(COMMAND_ROUNDABOUT_EXIT[0]
                + " 1st "+COMMAND_ROUNDABOUT_EXIT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_ROUNDABOUT_1_EXIT));
        DirectionCommandTypes.put(COMMAND_ROUNDABOUT_EXIT[0]
                + " 2nd "+COMMAND_ROUNDABOUT_EXIT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_ROUNDABOUT_2_EXIT));
        DirectionCommandTypes.put(COMMAND_ROUNDABOUT_EXIT[0]
                + " 3rd "+COMMAND_ROUNDABOUT_EXIT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_ROUNDABOUT_3_EXIT));
        for(int i=4;i<10;i++){
            DirectionCommandTypes.put(COMMAND_ROUNDABOUT_EXIT[0]
                    + " "+i+"th "+COMMAND_ROUNDABOUT_EXIT[1],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_ROUNDABOUT_3_EXIT+i-3));
        }

        //take the xxx left
        DirectionCommandTypes.put(COMMAND_TAKE_LEFT[0]
                + " 1st "+COMMAND_TAKE_LEFT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_TAKE_1_LEFT));
        DirectionCommandTypes.put(COMMAND_TAKE_LEFT[0]
                + " 2nd "+COMMAND_TAKE_LEFT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_TAKE_2_LEFT));
        DirectionCommandTypes.put(COMMAND_TAKE_LEFT[0]
                + " 3rd "+COMMAND_TAKE_LEFT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_TAKE_3_LEFT));
        for(int i=4;i<10;i++){
            DirectionCommandTypes.put(COMMAND_TAKE_LEFT[0]
                    + " "+i+"th "+COMMAND_TAKE_LEFT[1],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_TAKE_3_LEFT+i-3));
        }

        //take the xxx right
        DirectionCommandTypes.put(COMMAND_TAKE_RIGHT[0]
                + " 1st "+COMMAND_TAKE_RIGHT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_TAKE_1_RIGHT));
        DirectionCommandTypes.put(COMMAND_TAKE_RIGHT[0]
                + " 2nd "+COMMAND_TAKE_RIGHT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_TAKE_2_RIGHT));
        DirectionCommandTypes.put(COMMAND_TAKE_RIGHT[0]
                + " 3rd "+COMMAND_TAKE_RIGHT[1],
                new MapDirectionCommandType(MapDirectionCommandType
                .COMMAND_TAKE_3_RIGHT));
        for(int i=4;i<10;i++){
            DirectionCommandTypes.put(COMMAND_TAKE_RIGHT[0]
                    + " "+i+"th "+COMMAND_TAKE_RIGHT[1],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_TAKE_3_RIGHT+i-3));
        }

        //head direction.
        for(int i=1;i<9;i++){
            DirectionCommandTypes.put(COMMAND_HEAD_DIRECTION[0]
                    + " "+COMMAND_HEAD_DIRECTION[i],
                    new MapDirectionCommandType(MapDirectionCommandType
                    .COMMAND_HEAD_DIRECTION_N+i-1));
        }


    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if the description contains any direction command.
     * @param description input string.
     * @param index the start index of the word to check
     * @param word the word string.
     * @return true ,it's a word.
     */
    private static boolean isWord(String description,int index,String word){
        if(index==0 || ((index+word.length()+1)>=description.length())) return true;
        char ch1=description.charAt(index-1);
        char ch2=description.charAt(index+word.length());
        if(ch1==' ' && ch2==' ') return true;
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 29DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if the description contains any direction command.
     * @param description input string.
     * @return true ,if contains direction command.
     */
    private static boolean hasCommand(String description) {

        //check if there's command
        for (int j = 0; j < COMMAND_ALL_LIST.length; j++) {
            commandIndex = description.toLowerCase().indexOf(COMMAND_ALL_LIST[j]);
            if (commandIndex > -1) {
                commandText = COMMAND_ALL_LIST[j];
                if (isWord(description, commandIndex, commandText)) {
                    directionCommandType = (MapDirectionCommandType)
                            DirectionCommandTypes.get(COMMAND_ALL_LIST[j]);
                    return true;
                }

            }
        }

        return false;
    }

}
