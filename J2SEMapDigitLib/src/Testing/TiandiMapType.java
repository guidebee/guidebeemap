/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Testing;

import com.mapdigit.gis.raster.ICustomMapType;
import com.mapdigit.gis.raster.MapType;

/**
 *
 * @author james
 */
public class TiandiMapType implements ICustomMapType {

    private static int serverIndex=1;

    public String getTileURL(int mtype, int x, int y, int zoomLevel) {
        String returnURL="";
        serverIndex+=1;
        serverIndex%=3;
        int maxTiles=(int)Math.pow(2, zoomLevel);
        switch(mtype){
            case MapType.GENERIC_MAPTYPE_5:
                returnURL= "http://p" + serverIndex+".map.qq.com/maptiles/" ;
                   y=maxTiles-y-1;
                   returnURL+=+zoomLevel+"/"+(int)(x/16)+"/"+(int)(y/16)+"/"+x+"_"+y+".gif";
                break;
            case MapType.GENERIC_MAPTYPE_6:

                if(zoomLevel<11){
                   returnURL= "http://tile" + serverIndex+".tianditu.com/DataServer?T=A0512_EMap";
                   returnURL+="&X="+x+"&Y="+y+"&L="+zoomLevel;

                }else if(zoomLevel<13){
                   returnURL= "http://tile" + serverIndex+".tianditu.com/DataServer?T=B0627_EMap1112";
                   returnURL+="&X="+x+"&Y="+y+"&L="+zoomLevel;
                }else{
                   returnURL= "http://tile" + serverIndex+".tianditu.com/DataServer?T=siwei0608";
                   returnURL+="&X="+x+"&Y="+y+"&L="+zoomLevel;
                }
                 
                break;
            case MapType.GENERIC_MAPTYPE_7:
                if(zoomLevel<11){
                   returnURL= "http://tile" + serverIndex+".tianditu.com/DataServer?T=AB0512_Anno";
                   returnURL+="&X="+x+"&Y="+y+"&L="+zoomLevel;
                }else{
                   returnURL=MapType.EMPTY_TILE_URL;
                }
                 
                break;

        }
        System.out.println(returnURL);
        return returnURL;
    }

}
