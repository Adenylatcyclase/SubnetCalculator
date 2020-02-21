package pc.ip;

import java.util.Scanner;

import pc.ip.address.IPAddress;

public class Main {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		System.out.printf("Please input a network address: ");
		
		IPAddress ip = new IPAddress(scan.nextLine());
		System.out.printf("%-15s - %-15s%n%-15s - %-15s%n","IP Address", "Subnetmask", ip.getIP(false), ip.getMask(false));
		System.out.printf("%n%n");
		System.out.printf("%-20s  %-15s  %-35s%n", "Address:", ip.getIP(false), ip.getIP(true));
		System.out.printf("%-20s  %-15s  %-35s%n", "Netmask:", ip.getMask(false), ip.getMask(true));
		System.out.printf("%-20s  %-15s  %-35s%n", "Wildcard:", ip.getWildcard(false), ip.getWildcard(true));
		System.out.printf("%-20s  %-15s  %-35s%n", "Network Address:", ip.getNetwork(false), ip.getNetwork(true));
		System.out.printf("%-20s  %-15s  %-35s%n", "Broadcast Address:", ip.getBroadcast(false), ip.getBroadcast(true));
		System.out.printf("%-20s  %-15s  %-35s%n", "First Host:", ip.getHost(1, false), ip.getHost(1, true));
		System.out.printf("%-20s  %-15s  %-35s%n", "Last Host:", ip.getHost(-1, false), ip.getHost(-1, true));
		System.out.printf("%-20s  %-15s%n", "Total host count;", ip.getHostCount());
		scan.close();
		
	}

}
