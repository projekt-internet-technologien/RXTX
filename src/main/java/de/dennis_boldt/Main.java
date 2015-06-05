package de.dennis_boldt;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.LinkedList;

public class Main {

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				(new Thread(new SerialReader(in))).start();
				(new Thread(new SerialWriter(out))).start();

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
		System.setProperty( "java.library.path", "/usr/lib/jni" );
		Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
		fieldSysPath.setAccessible( true );
		fieldSysPath.set( null, null );
		
		@SuppressWarnings("rawtypes")
		Enumeration ports2 = CommPortIdentifier.getPortIdentifiers();
		LinkedList<String> ports = new LinkedList<String>();

		int i = 0;
		while (ports2.hasMoreElements()) {
			i++;
			CommPortIdentifier port = (CommPortIdentifier) ports2.nextElement();
			String name = port.getName();

			if (name.startsWith("/dev/ttyACM")
					|| name.startsWith("/dev/ttyUSB")) {
				System.out.println(i + ") " + name);
				ports.push(port.getName());
			}
		}

		Main m = new Main();
		try {
			for (String port : ports) {
				System.out.println("Try " + port);
				m.connect(port);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
    */
	}
}