package com.egg.system.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

//Very often there exists more than one network adapter for a single device. For this reason, I am not going to use CIM or WMIC formatter
//Because they can only read a single line
//This also means that this program cannot support detecting more than one hardware at a time. 
//For example, if there are two physical CPUs in a PC, this program will detect only one.
//I plan to modify this behavior later as network adapters and RAMs often have multiple physical hardware.
// I will implement a custom CIM formatter here that will first get the device IDs and then loop through all the device IDs and get the required adapter info
//If this succeeds, I will modify the original adapters sometime later, to incorporate this change
//which will allow detection of multiple hardware of the same class/category
public class Win32_NetworkAdapter {
	
	private Win32_NetworkAdapter() {
		throw new IllegalStateException("Utility Class");
	}
	
	//will retrieve all the adapter IDs which are currently active and providing Internet
	public static ArrayList<String> getDeviceIDList() throws IOException {
		ArrayList<String> deviceIDList = new ArrayList<String>();
		String[] command = {"powershell.exe", "/c", "Get-CimInstance -ClassName Win32_NetworkAdapter -Filter \"NetEnabled='True'\" | Select-Object DeviceID | Format-List"};
		Process process = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
		String currentLine;
			
		while((currentLine=br.readLine())!=null)
			if(!currentLine.isBlank() || !currentLine.isEmpty())
				deviceIDList.add(currentLine);
			
		br.close();
		
		for(int i=0 ; i<deviceIDList.size(); i++) {
			deviceIDList.set(i, deviceIDList.get(i).substring(deviceIDList.get(i).indexOf(":")+1).strip());
		}
		
		return deviceIDList;
		}
	
	//will return a hashmap of the following properties as a key and their corresponding values:
	//Name, Description, PNPDeviceID, MACAddress, Installed, NetEnabled, NetConnectionID, PhysicalAdapter, TimeOfLastReset
	public static HashMap<String, String> getNetworkAdapters(String deviceID) throws IOException {
		String[] command = {"powershell.exe", "/c", "Get-CimInstance -ClassName Win32_NetworkAdapter -Filter \"DeviceID='"+deviceID+"'\" | Select-Object Name, Description, PNPDeviceID, MACAddress, Installed, NetEnabled, NetConnectionID, PhysicalAdapter, TimeOfLastReset | Format-List"};
		Process process = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		String currentLine;
		HashMap<String, String> propertyValues = new HashMap<>();
		
		while((currentLine=br.readLine())!=null)
			if(!currentLine.isBlank() || !currentLine.isEmpty()) {
				propertyValues.put(currentLine.substring(0, currentLine.indexOf(":")).strip(), currentLine.substring(currentLine.indexOf(":")+1).strip());
			}
				
			
		br.close();
		return propertyValues;
	}
}
