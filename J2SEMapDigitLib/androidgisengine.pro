-injars      dist\J2SEMapDigitLib.jar(com/mapdigit/**)
-outjars     dist\libgisengine.jar
-libraryjars  "C:/Program Files (x86)/Java/jdk1.6.0_39/jre/lib/rt.jar"

-repackageclasses 'com.mapdigit.gisengine'
-target 1.6 
-optimizationpasses 2 
-dontusemixedcaseclassnames 
-dontskipnonpubliclibraryclasses 
-dontpreverify 
-verbose 


-keep public class com.mapdigit.drawing.* {
    public  *;
	protected  *;
}

-keep public class com.mapdigit.drawing.geometry.* {
    public  *;
	protected  *;
}


-keep public class com.mapdigit.gis.drawing.** {
     public  *;
	protected  *;
}

-keep public class com.mapdigit.gis.geometry.** {
     public  *;
	protected  *;
}

-keep public class com.mapdigit.gis.* {
    public  *;
	protected  *;
}

-keep public class com.mapdigit.gis.location.*{
     public  *;
	protected  *;
}

-keep public class com.mapdigit.gis.location.nmea.*{
     public  *;
	protected  *;
}

-keep public class com.mapdigit.gis.navigation.* {
     public  *;
	protected  *;
}



-keep public class com.mapdigit.gis.raster.* {
     public  *;
	protected  *;
}

-keep public class com.mapdigit.gis.service.* {
    public  *;
	protected  *;
}

-keep public class com.mapdigit.gis.vector.* {
    public  *;
	protected  *;
}


-keep public class com.mapdigit.licence.** {
     public  *;
	protected  *;
}



-keep public class com.mapdigit.util.* {
     public  *;
	protected  *;
}


