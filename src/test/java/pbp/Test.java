/**
 * 
 */
package pbp;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.LinkedHashSet;

import cn.hutool.core.net.NetUtil;

/**
 * @author hlinfo
 *
 */
public class Test {
	public static void main(String[] args) {
		System.out.println(NetUtil.localIpv6s());

	}
}
