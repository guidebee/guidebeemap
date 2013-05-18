//------------------------------------------------------------------------------
//                         COPYRIGHT 2010 GUIDEBEE
//                           ALL RIGHTS RESERVED.
//                     GUIDEBEE CONFIDENTIAL PROPRIETARY
///////////////////////////////////// REVISIONS ////////////////////////////////
// Date       name                 Tracking #         Description
// ---------  -------------------  ----------         --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
//--------------------------------- PACKAGE ------------------------------------
package com.mapdigit.gis.service;

//--------------------------------- IMPORTS ------------------------------------
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

////////////////////////////////////////////////////////////////////////////////
//--------------------------------- REVISIONS ----------------------------------
// Date       Name                 Tracking #         Description
// --------   -------------------  -------------      --------------------------
// 28DEC2010  James Shen                 	      Code review
////////////////////////////////////////////////////////////////////////////////
/**
 * MapKeyRepsoitory store all map keys used for map service.
 * <hr><b>&copy; Copyright 2010 Guidebee Pty Ltd. All Rights Reserved.</b>
 * @version     2.00, 28/12/10
 * @author      Guidebee Pty Ltd.
 */
public final class MapKeyRepository {


    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add a map key to the user defined repository.
     * @param mapKey the key to be added.
     */
    public static void addMapKey(MapKey mapKey){
        addMapKey(mapKey.getkeyType(),mapKey.getKeyValue());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Add a map key to the user defined repository.
     * @param keyType  the key type.
     * @param keyValue the key value.
     */
    public static void addMapKey(int keyType,String keyValue){
        Integer key=new Integer(keyType);
        Vector keys=(Vector)userDefinedMapKeyRepository.get(key);
        if(keys==null){
            keys=new Vector();
            keys.addElement(keyValue);
            userDefinedMapKeyRepository.put(key, keys);
        }else{
            if(!keys.contains(keyValue)){
                keys.addElement(keyValue);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * remove a map key from user defined map key repository.
     * @param mapKey the key to be removed.
     */
    public static void removeMapKey(MapKey mapKey){
       removeMapKey(mapKey.getkeyType(),mapKey.getKeyValue());
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * remove a map key from user defined map key repository.
     * @param keyType the key type.
     * @param keyValue the key value.
     */
    public static void removeMapKey(int keyType,String keyValue){
        Integer key=new Integer(keyType);
        Vector keys=(Vector)userDefinedMapKeyRepository.get(key);
        if(keys!=null){
            if(keys.contains(keyValue)){
                keys.removeElement(keyValue);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    //--------------------------------- REVISIONS ------------------------------
    // Date       Name                 Tracking #         Description
    // ---------  -------------------  -------------      ----------------------
    // 28DEC2010  James Shen                 	          Code review
    ////////////////////////////////////////////////////////////////////////////
    /**
     * retrieve a key for the map repositories.
     * @param keyType the key type.
     * @return a key for given type.
     */
    public static String getKey(int keyType){
        Vector keyVector=(Vector)userDefinedMapKeyRepository.get(new Integer(keyType));
        Object []keys;
        if(keyVector==null){
            keys=(String [])buildinMapKeyRepository.get(new Integer(keyType));
        }else{
            keys=new Object[keyVector.size()];
            keyVector.copyInto(keys);
        }
        int index=rnd.nextInt(keys.length);
        return (String)keys[index];
    }

    /**
     * build in map key repository.
     */
    private final static Hashtable buildinMapKeyRepository=new Hashtable();

    /**
     * user defined map key repository.
     */
    private final static Hashtable userDefinedMapKeyRepository=new Hashtable();

    /**
     * random select a map key.
     */
    private final static Random rnd=new Random();


    private final static String []mapabcQueryKeys={
        "b0a7db0b3a30f944a21c3682064dc70ef5b738b062f6479a5eca39725798b1ee300bd8d5de3a4ae3"
    };

    private final static String[] googleQueryKeys = {
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRcdv_D3MipQG6beFVt4q3n2KstuxQVPGsK1seABGQPugXw_P7Iua0JYw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRr3VMBZ1cGe19qTgaCju5hrS8dIxSVwolc1mXM0pUIqSvJSNaW7jJUiA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRm_ifamDDETX3GYECVeBf43IL7kxQhowIvbl9G-Mq1Jo874g3vZbr9KA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTdoKf24hPXAkfeSPvoX63LdjNnwhTXeivbZPtE5W6vLnal3MgqR1Q4og",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQHwqwnNik4w_uH95OtQPrGD8h2aRQkX34t6brsYYQjMh5Al7WxZC-uRQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTxDsZgO1TyNw5Fb7lqwb1yrhjwjBRA87P_DQ_K07IWadLOQuyPYDfHIA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQma3cdF9cz-FT2e3x_QfYqxZ-lIBQLKb6_-IocP_EZaz6BpXiLhuD8fg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRm5GNFjZ8GN__mSLFDVmdMUufGqxTxofYdQZGsDgJOJ6_h-Q7HO4WF8w",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTEKgbPjtpwuJgXSRRhfbfuAHQlfRRdwtWTdkWiS7_AQmBiH4zhIHsUTQ",
        "ABQIAAAAi44TY0V29QjeejKd2l3ipRTRERdeAiwZ9EeJWta3L_JZVS0bOBQlextEji5FPvXs8mXtMbELsAFL0w",
        "ABQIAAAAi44TY0V29QjeejKd2l3ipRTRERdeAiwZ9EeJWta3L_JZVS0bOBQlextEji5FPvXs8mXtMbELsAFL0w",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRDf_4UBTZQzNJ26pMj22HE04IfcxRvumhN_7MsuGv_6bIYxpdTOp6kow",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTp2Ya__oT19k37pTiYfLTUXGGkCxSwOMxlANFg35OJ33oFqtkXtJB8Bw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSeBuM33aDSR0Hm-SnlCNz_fjPhdhR3DavNyWRx-kI8QXN-H8NVFCluag",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRITtUgxNwCmOm4jliM1GNE_gzR3xTM3N_7GbA04vKBLvNwkZjtILvj5g",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hT-J3yXxvoJG6nk_OGJxrpUU7wPrBQwXRpc1daBieTE2BLVkUIjG5Lnpw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hS4uYaGP_McRoKz67JAg6wD0yCROxRMWi_3zALSAnKUMb592UbitcY2sg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQWyOCLESCo5X3MLJX0ujK3-t1msxR903Plw_ODfsatZxeSkGDGpOHgbw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSYKmXuLqF_jWYw3AHWbdin1rz5MBRnp5yCj1NdjXM3C2Dkmpd_Ceu9Rw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSyBoGAMUc-aiyQuLmfwqF3h-c4LBQl_hrwjNXg_Wy5m291ErWriGK8VQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSet8kNpAkWebojGW6BHGM4wLfX4BQ8XbFdLAyW_9yv5-lYaDzni8jV1g",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSkISrnBEIhvhfyLOBwkA2kCY-kVBTcBbcPNZ1Z3xjPvSKRS2rShV2dyQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQOoyArg256GSn8hhKDm4AsG05g1xQbfDfzzM2fmoCblPNzePhXFtI_9g",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQ3noxx8Ym5kPfimv0TdPwPefqSxRQXdR8YyrRa-OrRIDpsblZzFCA6JA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQ_wcmvrFFL03jff-XSp86Uj6XQzRRTUKFeOEJWQFLmhXXTZ_Phkrf2HQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTgniKvy0rJpryCTlYMqTrrZfuG9RQxLNVFd32ocLuDdL5aT8akQdBh6Q",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSWqFIofkeCGkg8-JJYgqqTyFd2iRS9MtrzoiCOK0HwOUkBogMNMcI75w",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQBI3pghY_JYYbHHk2bbYwFj5mhohSXu9xM3heMCinZN5XjhGg90aauyw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hR0qnwko6fJ1UYDtYw2Km7FsUThGBRjgrmbSvBXQUhA0kqpMrDgbLl5Vw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTOHb9GHgVIihj-isrUX4oiKXCLuBRP4C2yieN18xxGGYCd0nYEE1dgig",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hR3kV0F_C_o21x_OMa_-OIenMHXHBRESw8knAmK7_6-x1q3X_ST2R-eig",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSIyt8svA7Kt1FD7525NAS6dOgNOBSpYCbwCYG6J7oo7K2wQdDVtEV1bg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQPnHxR8WpDlzNk5z_xwmLEY-9BchSyl-_GUSLTJNgKKBEiZZ6ftHYiqQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSC81erGeMMVlPVZZSQL_FN-xx_rBSk6v4CdF3rdMzJ7KNXjuUHHPy7cA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hR_Yb0P7NaEVTuEJU9D-ZP5KXI_dRTwlL74ZtjvdTvBTU1NB8-7MAaWBw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQvukHiTj9yNWZvCDwOgpvoAjPI-RSxFYevtD2qS2kOs40KHG_VTVVdjg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRNIGMSxTIfRebDiAHKvgbT0wPkGBQR_TNpu7FoVbuhrPiFGITHFkj1zg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hT3qbGMO-UGDGoDsVbjkRTX4pUd1BTvNK0lxncjSuQyvcK5f_ICc377gw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSyOLK1SMJmfpWuDGEJdhAF8mgWmBSuwlfSL-IUaq36wssLXRu_AbY8NA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hR-osOptm9DZM6CMABva0S2PTVLRxSCW_OfuGH_cNyk2HC6wfcWv-8ubA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hS8CPSwh8qjEaSeEiePC5HYqcWE1RRx31lRrpGLmV4UHJ_zQvUKWKtEMA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTLp9f53K8RA3FsZsK9yOLgtO8y0hQV4HFROI9v2p4wcNk6Mn3dFPsNSA",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQsxWYHagJFZexgcfAYEWA-Y8x28xSDlJ9zV8gm_irxBzPYMm6HwbUj7A",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQCnTSO5-oO4AY93lRNkoCCIP9csBSAkkY7zhj6qH7_Wu0zluWJseBBUg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hT18AjU3xIugVspVbvx3oGXFLOv_xRZx-q2qMCz59_cd-5S7p_Gse3wzg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQnsz9St1mxrGDRpes_zMp4TfckGxStE7DaIIJnLlR8phTL1cEEuleZ8g",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTmNjkeluqcBZftuIEvBdVYtvzfMBRyKmnXJ_UFDngPgUyNnAnfCguFHw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSXtASLPAjn8REWle5_1Ci2K0Y4rBRzUJqqA7uFY_FGoAik1E2VgKPdww",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRgj6lJXj4s1zrh78QjdH3v-QrjxxTPm3SWAXJnSpKVFPkiXBO-DXXs2A",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hTuqNRiVCiEmRqZCNPZvXJmqu0m0BTsR6Yg2MsdnpsH5n6dnmiCSl8Qng",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQQS9_9QO1Sq5i42rywM_-GB_l6RhSaTvJo83QK-vy4-NQ_yCOG-aQ3ow",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRvjNH4H18YbUGVONN55oOxH3ZYxhR5VPpIeBLQ0ICj0nGdDpTCf7jYpw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSJwuJ274fyvRPSr79NOK2XerjQ1RR1wSkkQ09w3RiqcxprKNVhpgvZTQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSwXGJH5_QaaMSbzH52k_C_TyBIqBSJX4IDl4P_gED9w64hOjF5r1MVCQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSagCsXpy8wu3NXLNRVWWrtLz41KRSRuUNTSkOmS9A9EXELv2ZI4f648g",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRMoK3qrhlTfDesOFkTuemrafaGERRA1psdvu7aRmUUfFZkA1-N5KiZHg",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSg8Y5bTQdWSF5ifonFz073dFIiJhQ0xJ1YoIxLg5_2m-76BEXmI7QK4g",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hShbBYNnw0imizF9Pb7pijQgCKy9RSu1IgwBjKuzqNkKd9q8CIV9dp0jQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hSXPzllj7I0V08jjeKIZTC7DwFzSxQdU7ELRM1Zm1It5bb87C1KzSRLlQ",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hRq1lYyafwZSYRXZ_ZyEavxKdqtexQwyIZl8yfJSb8s5O9WxKKMsKO5hw",
        "ABQIAAAAHxBdP31K2IukU7-aAA8n5hQdDLs30cgkZG60zJ7b31hQxhPUZBQoooqUte8W4FDLDlx9FZV7PtdI6w"
    };

    private final static String[] cloudeMadeQueryKeys = {
        "8ee2a50541944fb9bcedded5165f09d9"   //cloudmade key
    };

    private final static String[] bingQueryKeys = {
        "AhGSgD1Twhjx9WqxjJZznCBCSzddrrBzkD7k6MjIaLGnp3b3hupQUVbNdv6Wb0qW" //microsoft key

    };

    static{
        buildinMapKeyRepository.put(new Integer(MapKey.MAPKEY_TYPE_GOOGLE), googleQueryKeys);
        buildinMapKeyRepository.put(new Integer(MapKey.MAPKEY_TYPE_MAPABC), mapabcQueryKeys);
        buildinMapKeyRepository.put(new Integer(MapKey.MAPKEY_TYPE_CLOUDMADE), cloudeMadeQueryKeys);
        buildinMapKeyRepository.put(new Integer(MapKey.MAPKEY_TYPE_BING), bingQueryKeys);
    }
}
