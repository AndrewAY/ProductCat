package com.andrew.productcatalogue2.properties;

import com.andrew.productcatalogue2.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;



/** Allows property values to be retrieved where statically called methods
 * are more appropriate, or from files that are not configured to be
 * included as application properties files
 *
 * @author Andrew Au-Young
 * @version 1.0.0
 * */
@Component
@PropertySource("classpath:Timezone.properties")
@Slf4j
public class GlobalPropertiesService {

    private static String timeStampZoneId;


    //none-static setter needed to allow Spring to inject value into a static variable
    @Value("${database.timestamp.zoneId}")
    public void setTimeStampZoneId(String zoneId) {
        timeStampZoneId = zoneId;
    }

    /**
     * Retrieves a String intended for convertion into a {@link java.time.ZoneId} to
     * indicate the time zone id that is to used for time-stamping database entries.
     *
     * @return Sting representation of a {@link java.time.ZoneId}
     */
    public static String getTimeStampZoneId() {
        return timeStampZoneId;
    }


}
