package com.honestwalker.androidutils.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Created by lanzhe on 16-11-15.
 */
public class HostUtil {

    /**
     * 检测host
     * @param host
     * @throws Exception
     */
    private boolean checkHost(String host) {
        if(isIPAddress(host)) {
            return isIpReachable(getIpAddress(host));
        }
        return false;
    }

    private boolean isIpReachable(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            if (addr.isReachable(3000)) {
                return true;
            }
            return false;
        } catch (UnknownHostException e)  {
        } catch (IOException e) {
        }
        return false;
    }

    private String getIpAddress(String host) {
        String ip = host.replace("http://", "");
        if(ip.indexOf(":") > -1) {
            ip = ip.substring(0 , ip.indexOf(":"));
        }
        if(ip.indexOf("/") > -1) {
            ip = ip.substring(0 , ip.indexOf("/"));
        }
        return ip;
    }

    private boolean isIPAddress(String host) {
        String ip = host.replace("http://", "");
        if(ip.indexOf(":") > -1) {
            ip = ip.substring(0 , ip.indexOf(":"));
        }
        if(ip.indexOf("/") > -1) {
            ip = ip.substring(0 , ip.indexOf("/"));
        }
        Pattern pattern = Pattern.compile( "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$" );
        return pattern.matcher( ip ).matches();
    }

}
