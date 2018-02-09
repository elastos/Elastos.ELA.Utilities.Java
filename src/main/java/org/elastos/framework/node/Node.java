package org.elastos.framework.node;


/**
 * Created by mdj17 on 2018/1/23.
 */
public class Node {

    public static final String LOCALIP    = "127.0.0.1";
    public static final int LOCALINFOPORT = 40050;
    public static final int LOCALRESTPORT = 40100;
    public static final int LOCALWSPORT   = 40150;
    public static final int LOCALJSONPORT = 40200;
    public static final int HTTPLOCALPORT = 40250;


    public static String getNodePath(int index)  {
        return Runner.NODESPATH + "node" + String.valueOf(index);
    }

    public static String getRPCAddress(int index)  {
        return "http://" + LOCALIP + ":" + String.valueOf(LOCALJSONPORT + index );
    }

    public static String getRestfulAddress(int index)  {
        return "http://" + LOCALIP + ":" + String.valueOf(LOCALRESTPORT + index );
    }

}
