package com.egg.tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.egg.system.hardware.Win32_Printer;


public class PrinterTest {
	public static void main(String[] args) throws IOException{
		List<String> deviceIDs = Win32_Printer.getDeviceIDList();
		Map<String, String> currentPrinter;
		
		System.out.println(deviceIDs);
		
		for(String currentID : deviceIDs) {
			currentPrinter = Win32_Printer.getCurrentPrinter(currentID);
			for(Map.Entry<String, String> entry: currentPrinter.entrySet())
				System.out.println(entry.getKey()+": "+entry.getValue());
			System.out.println();
	    }
	}
}
