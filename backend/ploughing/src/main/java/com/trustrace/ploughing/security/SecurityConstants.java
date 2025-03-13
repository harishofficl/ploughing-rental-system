package com.trustrace.ploughing.security;

public class SecurityConstants {
    private static final String SECRET_KEY = "40c1422efa9b771681d576b904340d198155613b945c9deea3c8197862e92502";
    private static final long JWT_EXPIRATION =  86400000; // 1 day

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static long getJwtExpiration() {
        return JWT_EXPIRATION;
    }

}
