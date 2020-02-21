package pc.ip.address;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddress {
	private int[] mask;
	private int[] ip;
	private int subnet;
	
	private static final Pattern ipv4= Pattern.compile("(?:\\d{1,3})\\.(?:\\d{1,3})\\.(?:\\d{1,3})\\.(?:\\d{1,3})");
	private final Pattern network = Pattern.compile("(?:\\d{1,3})\\.(?:\\d{1,3})\\.(?:\\d{1,3})\\.(?:\\d{1,3})\\/\\d+");
//	private final Pattern ipv6 = Pattern.compile("([A-F0-9]{4}");
	public IPAddress(String address) {
		Matcher m = network.matcher(address);
		if(!m.find())
			throw new RuntimeException("Not OK network");
		String[] s = address.split("\\/");
		ip = parseIP(s[0]);
		mask = parseMaskInt(s[1], ip.length);
	}
	
	public IPAddress(String ip, String mask) {
		Matcher m1 = ipv4.matcher(ip);
		Matcher m2 = ipv4.matcher(mask);
		if(!(m1.find() && m2.find()))
			throw new RuntimeException("Not OK IPV4");
		this.ip = parseIP(ip);
		this.mask = parseIP(mask);
	}

	private int[] parseMaskInt(String mask, int length) {
		int m = Integer.parseInt(mask);
		subnet = m;
		int[] tmp = new int[length];
		for (int i = tmp.length - 1; i >= 0; i--) {
			if (m >= 8) {
				tmp[i] = (int)255;
				m -= 8;
			}else if (m > 0) {
				tmp[i] = (int)(Math.pow(2, 8) - Math.pow(2, m));
				m = 0;
			} else {
				tmp[i] = 0;
			}
		}
		System.out.println(getString(tmp, false));
		return tmp;
	}

	private int[] parseIP(String ipString) {
		String[] s = ipString.split("\\.");
		int[] tmp = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			tmp[i] = (int)Integer.parseInt(s[i]);
		}
		System.out.println(getString(tmp, false));
		return tmp;
	}
	
	private int[] AND(int[] a, int[] b) {
		int[] tmp = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			tmp[i] = (int)(a[i] & b[i]);
		}
		return tmp;
	}
	
	private int[] OR(int[] a, int[] b) {
		int[] tmp = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			tmp[i] = (int)(a[i] | b[i]);
		}
		return tmp;
	}
	
	private int[] INVERT(int[] a) {
		int[] tmp = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			tmp[i] = (int)~a[i];
		}
		return tmp;
	}
	private int[] calcNetwork() {
		return AND(ip, mask);
	}
	
	private int[] calcHost() {
		return AND(INVERT(mask), ip);
	}
	
	
	private String getString(int[] l, boolean binary) {
			String s = "";
			for(int i = 0; i < l.length; i++) {
				if (i != 0)
					s = "." + s;
				if(binary) {
					s = String.format("%0$8s", Integer.toBinaryString(l[i])).replace(' ', '0') + s;
				}else {
					s = l[i] + s;
				}
			}
			return s;
	}
	public String getNetwork(boolean binary) {
		return getString(calcNetwork(), binary);
	}
	
	public String getWildcard(boolean binary) {
		return getString(calcHost(), binary);
	}
	
	public String getIP(boolean binary) {
		return getString(ip, binary);
	}
	
	public String getMask(boolean binary) {
		return getString(mask, binary);
	}
	
	public String getBroadcast(boolean binary) {
		return getString(calcAddress(0), binary);
	}

	private int[] calcMaxHost() {
		int[] tmp = new int[ip.length];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = (int)255;
		}
		return tmp;
	}
	
	// THIS IS BAD
	private int[] calcAddress(long i) {
		int[] tmp = new int[ip.length];
		if (i == 0) {
			tmp =  calcMaxHost();
		} else if(i < 0){
			tmp = calcMaxHost();
			tmp[tmp.length -1] -= 1;
		} else {
			for (int e = tmp.length - 1; e <= 0; e--) {
				tmp[e] += (int)(i & 255);
				i = i >> 8;
			}
		}
		return OR(calcNetwork(), tmp);
	}
	public String getHost(int i, boolean binary) {
		return getString(calcAddress(i), binary);
	}
	public int getHostCount() {
		return (int)(Math.pow(2, ((ip.length*8) - subnet)) - 2);
	}
	
}
