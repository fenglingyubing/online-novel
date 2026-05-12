package com.fengling.common.context;

public class UserContext {
    private static final ThreadLocal<AuthUserInfo> USER_ID_THREAD_LOCAL = new ThreadLocal();

    public static void setAuthUserInfo(AuthUserInfo authUserInfo){
        USER_ID_THREAD_LOCAL.set(authUserInfo);
    }

    public static AuthUserInfo getAuthUserInfo(){
        return USER_ID_THREAD_LOCAL.get();
    }

    public static Long getUserId(){
        AuthUserInfo authUserInfo = USER_ID_THREAD_LOCAL.get();
        return authUserInfo == null ? null : authUserInfo.getUserId();
    }

    public static Integer getUserRole(){
        AuthUserInfo authUserInfo = USER_ID_THREAD_LOCAL.get();
        return authUserInfo == null ? null : authUserInfo.getUserRole();
    }

    public static void remove(){
        USER_ID_THREAD_LOCAL.remove();
    }
}
