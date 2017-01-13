package de.dennis_boldt;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Observer;

/**
 *
 * @author Dennis Boldt
 *
 * @see: http://angryelectron.com/rxtx-on-raspbian/
 * @see:
 */
public class RXTX  {

	private OutputStream out;
	private int baudRate;

	public RXTX(int baudRate) {
		this.baudRate = baudRate;
	}

	private OutputStream connect(String portName, Observer observer) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {

		OutputStream out = null;
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(this.baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				out = serialPort.getOutputStream();

				SerialReader sr = new SerialReader(in);
				if(observer != null) {
					sr.addObserver(observer);
				}

				(new Thread(sr)).start();

			} else {
				System.out.println("Error: Only serial ports are handled by this app.");
			}
		}

		return out;
	}

	public void start(String ports, String rxtxlib, Observer observer) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {

		// Set some default ports: ttyUSB0...ttyUSB9 and ttyACM0...ttyACM9
		if(ports == null) {
			LinkedList<String> tmpPorts = new LinkedList<String>();
			for (int i = 0; i < 10; i++) {
				tmpPorts.add("/dev/ttyACM" + i); // Linux (e.g., Arduino)
				tmpPorts.add("/dev/ttyUSB" + i); // Linux
				tmpPorts.add("COM" + i); // Windows
			}
			String[] tmpPortsArray = tmpPorts.toArray(new String[tmpPorts.size()]);
			ports = String.join(":", tmpPortsArray);
		}

		System.out.println("########################################################");
		System.out.println("# Ports   : " + ports);
        System.out.println("# RXTX lib: " + rxtxlib);
        System.out.println("########################################################");
        System.out.println();

		// SET java.library.path
		// http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
		System.setProperty( "java.library.path", rxtxlib );
		Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
		fieldSysPath.setAccessible( true );
		fieldSysPath.set( null, null );

		System.getProperties().setProperty("gnu.io.rxtx.SerialPorts", ports);

		@SuppressWarnings("rawtypes")
		Enumeration ports2 = CommPortIdentifier.getPortIdentifiers();
		LinkedList<String> portsList = new LinkedList<String>();

		int i = 0;
		while (ports2.hasMoreElements()) {
			i++;
			CommPortIdentifier port = (CommPortIdentifier) ports2.nextElement();
			String name = port.getName();

			if (name.startsWith("/dev/ttyACM") || name.startsWith("/dev/ttyUSB")) {
				System.out.println(i + ") " + name);
				portsList.push(port.getName());
			}
		}

		for (String port : portsList) {
			System.out.println("Try " + port);
			this.out = this.connect(port, observer);
		}
	}

	public void write(byte[] bytes) throws IOException {
		if(this.out != null) {
			this.out.write(bytes);
		}
	}

	public void write(byte b) throws IOException {
		if(this.out != null) {
			this.out.write(b);
		}
	}

	public void write(int i) throws IOException {
		if(this.out != null) {
			this.out.write(i);
		}
	}
}