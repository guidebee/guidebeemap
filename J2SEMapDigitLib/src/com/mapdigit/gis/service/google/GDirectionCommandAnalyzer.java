//------------------------------------------------------------------------------
//                         COPYRIGHT 2009 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 21AUG2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service.google;

//--------------------------------- IMPORTS ------------------------------------
import com.mapdigit.gis.MapDirectionCommandType;
import com.mapdigit.gis.MapDirectionCommandElement;
import com.mapdigit.gis.MapRoute;
import com.mapdigit.gis.MapStep;
import com.mapdigit.gis.geometry.GeoLatLng;
import java.util.Hashtable;
import java.util.Vector;

import com.mapdigit.util.Log;
//[------------------------------ MAIN CLASS ----------------------------------]
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 21AUG2009  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * Driving diretion command analyser.
 * <hr><b>&copy; Copyright 2009 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 21/08/09
 * @author      Guidebee Pty Ltd.
 */
public abstract class GDirectionCommandAnalyzer {

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Analyse the input direction in text (English).
     * @param direction the input direction description.
     * @return command element list (7 elements).
     * @throws java.lang.IllegalArgumentException
     */
    public static MapDirectionCommandElement[] analyse(String direction) {
        synchronized (syncObject) {
            String[] strings = tokenize(direction, ' ');
            originalDescription = "";
            for (int i = 0; i < strings.length; i++) {
                originalDescription += strings[i] + " ";
            }
            originalDescription = originalDescription.trim();
            //get rid of comma
            direction = originalDescription;
            strings = tokenize(direction, ',');
            originalDescription = "";
            for (int i = 0; i < strings.length; i++) {
                originalDescription += strings[i] + " ";
            }
            originalDescription = originalDescription.trim();
            direction = originalDescription;
            resultDirectionCommandElements
                        =new MapDirectionCommandElement[7];
            if (hasCommand(direction)) {
                
                String conjText="";
                if(commandIndex==0){
                    conjText=direction.substring(commandText.length()).trim();
                }else{
                    conjText=(direction.substring(0, commandIndex-1)+
                            direction
                            .substring(commandIndex+commandText.length())).trim();
                }
                resultDirectionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        =new MapDirectionCommandElement
                        (MapDirectionCommandElement.ELEMENT_COMMAND,
                        commandText);
                resultDirectionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        .directionCommandType=
                        new MapDirectionCommandType(directionCommandType);
                processConjuction(conjText);
                
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
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * analysis last step to see if it contains direction on left/right comannd.
     * @param lastStep
     * @return
     */
    public static MapStep analyseLastStep(MapStep lastStep){
        synchronized (syncObject) {
        String description=lastStep.description;
        MapStep mapStep=null;
        if(lastStep.directionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                .directionCommandType.type!=MapDirectionCommandType.COMMAND_DESTINATION_ON_THE_LEFT
                && lastStep.directionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                .directionCommandType.type!=MapDirectionCommandType.COMMAND_DESTINATION_ON_THE_RIGHT)
        for(int i=0;i<COMMAND_DESTINAION_ALL_LIST.length;i++){
             commandIndex = description.toLowerCase().indexOf(COMMAND_DESTINAION_ALL_LIST[i]);
             if(commandIndex>-1){
                 commandText = COMMAND_DESTINAION_ALL_LIST[i];
                if (isWord(description, commandIndex, commandText)) {
                    directionCommandType = (MapDirectionCommandType)
                            DirectionCommandTypes.get(COMMAND_DESTINAION_ALL_LIST[i]);
                    //found destination on the left
                    String newdescription=description.substring(0, commandIndex);
                    mapStep=MapRoute.newStep();
                    mapStep.bearing=0;
                    mapStep.description=commandText;
                    mapStep.directionCommandElements=new MapDirectionCommandElement[7];
                    mapStep.directionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        =new MapDirectionCommandElement
                        (MapDirectionCommandElement.ELEMENT_COMMAND,
                        commandText);
                    mapStep.directionCommandElements[MapDirectionCommandElement.DIRECTION_COMMAND]
                        .directionCommandType=
                        new MapDirectionCommandType(directionCommandType);
                    mapStep.calculatedDirectionType=new MapDirectionCommandType(directionCommandType);
                    mapStep.distance=0;
                    mapStep.duration=0;
                    mapStep.firstLatLng=new GeoLatLng(lastStep.lastLatLng);
                    mapStep.lastLatLng=new GeoLatLng(lastStep.lastLatLng);
                    mapStep.firstLocationIndex=lastStep.lastLocationIndex;
                    mapStep.lastLocationIndex=lastStep.lastLocationIndex;
                    lastStep.directionCommandElements=analyse(newdescription);
                    lastStep.description=newdescription;
                    if (lastStep.directionCommandElements
                            [MapDirectionCommandElement.TO_ROAD_NAME] != null) {
                        lastStep.currentRoadName =lastStep
                                .directionCommandElements
                                [MapDirectionCommandElement.TO_ROAD_NAME].description;
                    } else if (lastStep.directionCommandElements
                            [MapDirectionCommandElement.FROM_ROAD_NAME] != null) {
                        lastStep.currentRoadName =lastStep
                                .directionCommandElements
                                [MapDirectionCommandElement.FROM_ROAD_NAME].description;
                    }
                    mapStep.currentRoadName=lastStep.currentRoadName;
                    break;
                }
             }
        }
        return mapStep;
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Tokenizes a string with the given delimiter.
     * @return Array of tokens(strings).
     * @param String s String to be tokenized.
     * @param char delimiter Character that delimits the string.
     */
    private static String[] tokenize(String s, char delimiter) {
        Vector v = new Vector();
        int i = s.indexOf(delimiter);
        int currentIndex = -1;
        while (i != -1) {
            v.addElement(new String(s.substring(currentIndex + 1, i)));
            currentIndex = i;
            i = s.indexOf(delimiter, i + 1);
        }
        v.addElement(new String(s.substring(currentIndex + 1)));
        String[] returnArray = new String[v.size()];
        v.copyInto(returnArray);
        return returnArray;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * private constructor.
     */
    private GDirectionCommandAnalyzer() {
    }

    private final static String COMMAND_NO_TURN[] = {
        "continue",
        "continue straight"};

    private final static String COMMAND_BEAR_LEFT[] = {
        "turns slightly left",
        "turn slightly left",
        "slight left"};

    private final static String COMMAND_TURN_LEFT[] = {
        "turns left",
        "turn left"
    };

    private final static String COMMAND_SHARP_LEFT[] = {
        "turns sharply left",
        "turn sharply left",
        "sharp left"};

    private final static String COMMAND_SHARP_RIGHT[] = {
        "turns sharply right",
        "turn sharply right",
        "sharp right"};

    private final static String COMMAND_TURN_RIGHT[] = {
        "turns right",
        "turn right"
    };

    private final static String COMMAND_BEAR_RIGHT[] = {
        "turns slightly right",
        "turn slightly right",
        "slight right"};

    private final static String COMMAND_MERGE[] = {
        "merge",
        "enter"
    };


    private final static String COMMAND_KEEP_LEFT[] = {
        "keep left at the fork to continue",
        "keep left at the fork"
    };

    private final static String COMMAND_KEEP_RIGHT[] = {
        "keep right at the fork to continue",
        "keep right at the fork"
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
        "make a u-turn",
        "take a u-turn",
        "make a uturn",
        "take a uturn",
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
    
    private final static String ELEMENT_CONJUCTION[] = {
        "and becomes",      //0
        "and merge onto",   //1
        "to merge onto",    //2
        "to stay on",       //3
        "to merge onto",    //4
        "follow signs for", //5
        "onto",             //6
        "on",               //7
        "towards",          //8
        "toward",           //9
        "at",               //10
        "to",               //11
        "and stay on",      //12
    };

    private final static String ELEMENT_TO_CONJUCTION[]={
        ELEMENT_CONJUCTION[0],
        ELEMENT_CONJUCTION[1],
        ELEMENT_CONJUCTION[2],
        ELEMENT_CONJUCTION[3],
        ELEMENT_CONJUCTION[4],
        ELEMENT_CONJUCTION[6],
        ELEMENT_CONJUCTION[8],
        ELEMENT_CONJUCTION[9],
        ELEMENT_CONJUCTION[11]
    };


    private final static String ELEMENT_FINISH[]={
        "entering",
        "go through"
    };

    private final static String ELEMENT_TOLL[]={
        "partial toll road",
         "toll road"
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
    private static String originalDescription;
    private static String commandText;
    private static int commandIndex;
    private static MapDirectionCommandType directionCommandType;
    private static String conjuctionText;
    private static int conjuctionIndex;
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
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if the input start with conjuction.
     * @param description
     * @return
     */
    private static boolean startWithConjuction(String description){
         for(int i=0;i<ELEMENT_CONJUCTION.length;i++){
            if(description.toLowerCase().startsWith(ELEMENT_CONJUCTION[i])){
                conjuctionIndex=0;
                conjuctionText=ELEMENT_CONJUCTION[i];
                return true;
            }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if its' to conjuction (for to road).
     * @param conj
     * @return
     */
    private static boolean isToConjuction(String conj){
        for (int i = 0; i < ELEMENT_TO_CONJUCTION.length; i++) {
            if(ELEMENT_TO_CONJUCTION[i].equalsIgnoreCase(conj)){
                return true;
            }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * check to see if contains conjuction string.
     * @param description
     * @return
     */
    private static boolean hasConjuction(String description) {
        for (int i = 0; i < ELEMENT_CONJUCTION.length; i++) {
            conjuctionIndex = description.toLowerCase().indexOf(ELEMENT_CONJUCTION[i]);
            if (conjuctionIndex > -1) {
                conjuctionText = ELEMENT_CONJUCTION[i];
                if (isWord(description, conjuctionIndex, conjuctionText)) {
                    return true;
                }
            }
        }
        return false;
    }

 
    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process road conjuction string.
     * @param description input string.
     */
    private static void processConjuction(String description) {
        String remainDescription = description;
        if (!startWithConjuction(description)) {
            remainDescription = "At " + description;
        }
        //From road
        String extenstion = "";
        if (startWithConjuction(remainDescription)) {
            boolean isToRoad = isToConjuction(conjuctionText);
            if (!isToRoad) {
                resultDirectionCommandElements[MapDirectionCommandElement.FROM_ROAD_CONJUNCTION]
                        = new MapDirectionCommandElement
                        (MapDirectionCommandElement.ELEMENT_CONJUCTION,
                        conjuctionText);

            } else {
                resultDirectionCommandElements[MapDirectionCommandElement.TO_ROAD_CONJUNCTION]
                        = new MapDirectionCommandElement
                        (MapDirectionCommandElement.ELEMENT_CONJUCTION,
                        conjuctionText);
            }
            remainDescription = remainDescription.substring(conjuctionIndex
                    + conjuctionText.length());
            if (hasConjuction(remainDescription)) {//has to road
                String roadName = remainDescription.substring(0, conjuctionIndex);
                processRoadName(roadName, !isToRoad);
                isToRoad=!isToRoad;
                if (isToRoad) {
                    resultDirectionCommandElements[MapDirectionCommandElement.TO_ROAD_CONJUNCTION]
                            = new MapDirectionCommandElement
                            (MapDirectionCommandElement.ELEMENT_CONJUCTION,
                            conjuctionText);
                } else {
                    resultDirectionCommandElements[MapDirectionCommandElement.FROM_ROAD_CONJUNCTION]
                            = new MapDirectionCommandElement
                            (MapDirectionCommandElement.ELEMENT_CONJUCTION,
                            conjuctionText);
                }
                remainDescription = remainDescription.substring(conjuctionIndex
                        + conjuctionText.length());
                extenstion = processRoadName(remainDescription, !isToRoad);
            } else {
                extenstion = processRoadName(remainDescription, !isToRoad);
            }
        }
        processExtenstion(extenstion);

    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * process road name
     * @param description
     * @param from it's from or to road
     * @return the remaining description.
     */
    private static String processRoadName(String description,boolean from){
        int tollTypeIndex=-1;
        String remainText=null;
        String roadName="";
        int tollIndex=-1;
        for(int i=0;i<ELEMENT_TOLL.length;i++){
            tollTypeIndex=description.toLowerCase().indexOf(ELEMENT_TOLL[i]);
            if(tollTypeIndex>-1){
                tollIndex=i;
                break;
            }
        }
        int finishIndex=-1;
        for(int i=0;i<ELEMENT_FINISH.length;i++){
            int extIndex=description.toLowerCase().indexOf(ELEMENT_FINISH[i]);
            if(finishIndex==-1 && extIndex>-1) finishIndex=extIndex;
            if(extIndex>-1 && finishIndex>extIndex){
               finishIndex=extIndex;
            }
        }
        if (tollTypeIndex > -1) {
            roadName = description.substring(0, tollTypeIndex).trim();
        } else if (finishIndex > -1) {

            roadName = description.substring(0, finishIndex).trim();
        } else {
            roadName = description.trim();
        }
        if (finishIndex > -1) {
            remainText = description.substring(finishIndex).trim();
        }
        if (from){
            resultDirectionCommandElements[MapDirectionCommandElement.FROM_ROAD_NAME]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,roadName);
            if(tollTypeIndex>-1){
                resultDirectionCommandElements[MapDirectionCommandElement.FROM_ROAD_NAME].roadProperty
                        =ELEMENT_TOLL[tollIndex];
            }else{
                resultDirectionCommandElements[MapDirectionCommandElement.FROM_ROAD_NAME].roadProperty="";
            }

        }else{
            resultDirectionCommandElements[MapDirectionCommandElement.TO_ROAD_NAME]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,roadName);
            if(tollTypeIndex>-1){
                resultDirectionCommandElements[MapDirectionCommandElement.TO_ROAD_NAME].roadProperty
                        =ELEMENT_TOLL[tollIndex];
            }else{
                resultDirectionCommandElements[MapDirectionCommandElement.TO_ROAD_NAME].roadProperty="";
            }

        }
        return remainText;
    }


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Process extentions (entering or go through xxx roundabout).
     * @param extension
     */
    private static void processExtenstion(String extension){
        if(extension==null || extension.length()==0) return;
        extension=extension.trim();
        int enteringIndex=-1;
        int goThroughIndex=-1;
        enteringIndex=extension.toLowerCase().indexOf(ELEMENT_FINISH[0]);
        goThroughIndex=extension.toLowerCase().indexOf(ELEMENT_FINISH[1]);
        //start with entering.
        if(extension.toLowerCase().startsWith(ELEMENT_FINISH[0])){
            if(goThroughIndex>0){
                resultDirectionCommandElements[MapDirectionCommandElement.EXTENSION_ENTERING]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,extension.substring(0, goThroughIndex));
                resultDirectionCommandElements[MapDirectionCommandElement.EXTENSION_GO_THROUGH_ROUNDABOUT]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,extension.substring(goThroughIndex));
            }else{
                resultDirectionCommandElements[MapDirectionCommandElement.EXTENSION_ENTERING]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,extension);
            }
        }else if(extension.toLowerCase().startsWith(ELEMENT_FINISH[1])){
            if(enteringIndex>0){
                resultDirectionCommandElements[MapDirectionCommandElement.EXTENSION_GO_THROUGH_ROUNDABOUT]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,extension.substring(0, enteringIndex));
                resultDirectionCommandElements[MapDirectionCommandElement.EXTENSION_ENTERING]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,extension.substring(enteringIndex));
            }else{
                resultDirectionCommandElements[MapDirectionCommandElement.EXTENSION_GO_THROUGH_ROUNDABOUT]=
                    new MapDirectionCommandElement(MapDirectionCommandElement
                    .ELEMENT_ROAD_NAME,extension);
            }
        }
        
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 21AUG2009  James Shen                 	          Code review
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
    // 21AUG2009  James Shen                 	          Code review
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
