package com.advdsp.service.dsp.common;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.time.FastDateFormat;

public class CommonUtils {
    private final static String dsPatterns = "yyyy-MM-dd";
    private final static FastDateFormat dsDateFormat = FastDateFormat.getInstance(dsPatterns);


    public static String hashingMD5(String str) {
        if (null == str) {
            return null;
        }
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString();
    }

    public static String getDsFromUnixTimestamp(long unixTimestamp) {
        return dsDateFormat.format(unixTimestamp * 1000);
    }


}