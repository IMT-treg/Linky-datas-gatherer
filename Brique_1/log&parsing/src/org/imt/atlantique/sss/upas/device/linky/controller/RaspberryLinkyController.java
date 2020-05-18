package org.imt.atlantique.sss.upas.device.linky.controller;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import parsing.HistoricParser;
import java.util.HashMap;

import org.imt.atlantique.sss.upas.device.linky.controller.struct.Data;
import org.imt.atlantique.sss.upas.device.linky.controller.struct.DefaultData;
import org.paris11.limsi.iroom.command.CommandContext;
import org.upec.lissi.ubistruct.dpws.common.error.ErrorMessage;
import org.ws4d.java.service.parameter.ParameterValue;

public class RaspberryLinkyController extends AbstractSerialController {
	private HistoricParser dataParser;
	public StringBuffer buffer;
	public List<Data> dataList;

	public RaspberryLinkyController() {
		this.buffer = new StringBuffer();
		//Creating and filling the datalist with empty data.
		this.dataList = new ArrayList<>();
		this.dataList.add(0,new DefaultData("","","",""));
		this.dataList.add(1,new DefaultData("","","",""));
		this.dataList.add(2,new DefaultData("","","",""));
		this.dataList.add(3,new DefaultData("","","",""));
	}

	@Override
	public void update(Observable o, Object object) {
		if (object == null)
			return;
		this.buffer.append(object);
		this.checkBuffer();
	}
	private void checkBuffer() {
		int pos = this.buffer.lastIndexOf("\n");
		if (pos > 0) {
			this.parseReceivedData(this.buffer.substring(0, pos));
			this.buffer.delete(0, pos);
		}
	}
	
	private void parseReceivedData(String buffer) {
		System.out.println("RaspberryLinkyController.parseReceivedData()"+ buffer);
		//calling the historic parser
		this.dataParser = new HistoricParser(buffer);
		dataParser.parse();
		HashMap<String,DefaultData> datas = dataParser.dataHashMap;
		//is the datas are empty we don't send them
		if(datas.get("ADCO").getValue().equals("") == false) {
			this.dataList.set(0,(DefaultData)datas.get("ADCO"));
		}
		if(datas.get("ISOUSC").getValue().equals("") == false) {
			this.dataList.set(1,(DefaultData)datas.get("ISOUSC"));
		}
		if(datas.get("BASE").getValue().equals("") == false) {
			this.dataList.set(2,(DefaultData)datas.get("BASE"));
		}
		if(datas.get("PAPP").getValue().equals("") == false) {
			this.dataList.set(3,(DefaultData)datas.get("PAPP"));
		}
		//Send the datas over the network
		this.sendData(this.dataList);
	}
	
	@Override
	public ErrorMessage callOperation(String arg0, String arg1, ParameterValue arg2, ParameterValue arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorMessage executeCommand(String arg0, CommandContext<Object> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void hibernate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void wake() {
		// TODO Auto-generated method stub

	}

}
