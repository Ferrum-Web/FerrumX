package com.ferrumx.ui.primary;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.ferrumx.system.hardware.Win32_NetworkAdapter;
import com.ferrumx.system.networking.Win32_NetworkAdapterConfiguration;
import com.ferrumx.system.networking.Win32_NetworkAdapterSetting;
import com.ferrumx.ui.secondary.ExceptionUI;

final class Network {
	
	private Network() {
		throw new IllegalStateException("Utility Class");
	}
	
	protected static boolean initializeNetwork(JComboBox<String> networkChoice, JTextField...networkFields) {
		try {
			List<String> networkAdapters = Win32_NetworkAdapter.getDeviceIDList();
			for(String adapter: networkAdapters)
				networkChoice.addItem(adapter);
			
			String currentID = networkChoice.getItemAt(networkChoice.getSelectedIndex());
			Map<String, String>networkAdapter = Win32_NetworkAdapter.getNetworkAdapters(currentID);
			String index = Win32_NetworkAdapterSetting.getIndex(currentID);
			Map<String, String>networkAdapterConfiguration = Win32_NetworkAdapterConfiguration.getAdapterConfiguration(index);
			
			networkFields[0].setText(networkAdapter.get("Name"));
			networkFields[1].setText(networkAdapter.get("PNPDeviceID"));
			networkFields[2].setText(networkAdapter.get("MACAddress"));
			networkFields[3].setText(networkAdapter.get("NetConnectionID"));
			
			networkFields[4].setText(networkAdapterConfiguration.get("IPEnabled"));
			networkFields[5].setText(networkAdapterConfiguration.get("IPAddress"));
			networkFields[6].setText(networkAdapterConfiguration.get("IPSubnet"));
			networkFields[7].setText(networkAdapterConfiguration.get("DefaultIPGateway"));
			networkFields[8].setText(networkAdapterConfiguration.get("DHCPEnabled"));
			networkFields[9].setText(networkAdapterConfiguration.get("DHCPServer"));
			networkFields[10].setText(networkAdapterConfiguration.get("DNSHostName"));
			networkFields[11].setText(networkAdapterConfiguration.get("DNSServerSearchOrder"));
		} catch (IndexOutOfBoundsException | IOException e) {
			new ExceptionUI("Network Initialization Error", e.getMessage()).setVisible(true);
			return false;
		}
		addNetworkChoiceActionListener(networkChoice, networkFields);
		return true;
	}
	
	private static void addNetworkChoiceActionListener(JComboBox<String> networkChoice, JTextField...networkFields) {
		networkChoice.addActionListener(e->{
			String currentID = networkChoice.getItemAt(networkChoice.getSelectedIndex());
			Map<String, String> networkAdapter;
			try {
				networkAdapter = Win32_NetworkAdapter.getNetworkAdapters(currentID);
				String index = Win32_NetworkAdapterSetting.getIndex(currentID);
				Map<String, String>networkAdapterConfiguration = Win32_NetworkAdapterConfiguration.getAdapterConfiguration(index);
				
				networkFields[0].setText(networkAdapter.get("Name"));
				networkFields[1].setText(networkAdapter.get("PNPDeviceID"));
				networkFields[2].setText(networkAdapter.get("MACAddress"));
				networkFields[3].setText(networkAdapter.get("NetConnectionID"));
				
				networkFields[4].setText(networkAdapterConfiguration.get("IPEnabled"));
				networkFields[5].setText(networkAdapterConfiguration.get("IPAddress"));
				networkFields[6].setText(networkAdapterConfiguration.get("IPSubnet"));
				networkFields[7].setText(networkAdapterConfiguration.get("DefaultIPGateway"));
				networkFields[8].setText(networkAdapterConfiguration.get("DHCPEnabled"));
				networkFields[9].setText(networkAdapterConfiguration.get("DHCPServer"));
				networkFields[10].setText(networkAdapterConfiguration.get("DNSHostName"));
				networkFields[11].setText(networkAdapterConfiguration.get("DNSServerSearchOrder"));
			} catch (IndexOutOfBoundsException | IOException e1) {
				new ExceptionUI("Network Initialization Error", e1.getMessage()).setVisible(true);
			}
			
		});
	}
}
