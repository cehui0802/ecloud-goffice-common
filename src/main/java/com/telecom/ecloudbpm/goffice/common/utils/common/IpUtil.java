package com.telecom.ecloudbpm.goffice.common.utils.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author Pong weipengxiang@gwssi.com.cn
 * @date 2020/9/9 11:14
 **/

public class IpUtil {

    public static String getIP() {
        try {
            StringBuilder IFCONFIG = new StringBuilder();
            Enumeration en = NetworkInterface.getNetworkInterfaces();

            while(en.hasMoreElements()) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                Enumeration enumIpAddr = intf.getInetAddresses();

                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        IFCONFIG.append(inetAddress.getHostAddress()).append("\n");
                    }
                }
            }

            return IFCONFIG.toString();
        } catch (SocketException socketException) {
            socketException.printStackTrace();

            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException unKnownHostException) {
                unKnownHostException.printStackTrace();
                return null;
            }
        }
    }

}
