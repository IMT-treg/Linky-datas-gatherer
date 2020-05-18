package org.imt.atlantique.sss.upas.device.linky;


import org.upec.lissi.ubistruct.dpws.common.DynamicRunDevice;

public class RunLinky extends DynamicRunDevice {
	public static final String DEFAULT_CONFIG_FILE="file:./resources/Raspberry_linky.xml";
	public static final String DEFAULT_SYSTEM_NAME="Upas_raspberry-linky";

	public static void main(String[] args) {
		RunLinky runLinky;
		try {
			runLinky=(RunLinky)RunLinky.newRunDevice(args, RunLinky.class);
			runLinky.createDynamicDevice("Upas_raspberry_Manager");
			if(!runLinky.getDeviceManager().start()) {
				System.out.println("Problem occurred when starting the device.");
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}

}
