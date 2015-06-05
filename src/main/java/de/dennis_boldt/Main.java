package de.dennis_boldt;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.LinkedList;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * 
 * @author Dennis Boldt
 * 
 * @see: http://angryelectron.com/rxtx-on-raspbian/
 * @see: 
 */
public class Main {

	@Option(name="--ports",usage="Set USB ports")
    public String ports = null;

	@Option(name="--rxtxlib",usage="Set RXTX lib")
    public String rxtxlib = "/usr/lib/jni";
	
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
				System.out.println("Error: Only serial ports are handled by this app.");
			}
		}
	}
	
	public void start() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

		// Set some default ports: ttyUSB0...ttyUSB9 and ttyACM0...ttyACM9
		if(this.ports == null) {
			LinkedList<String> tmpPorts = new LinkedList<String>();
			for (int i = 0; i < 10; i++) {
				tmpPorts.add("/dev/ttyUSB" + i);
				tmpPorts.add("/dev/ttyACM" + i);
			}
			
			String[] tmpPortsArray = tmpPorts.toArray(new String[tmpPorts.size()]);
			this.ports = String.join(":", tmpPortsArray);
		}
		
		System.out.println("########################################################");
		System.out.println("# Ports   : " + this.ports);
        System.out.println("# RXTX lib: " + this.rxtxlib);
        System.out.println("########################################################");
        System.out.println();
		
		// SET java.library.path
		// http://blog.cedarsoft.com/2010/11/setting-java-library-path-programmatically/
		System.setProperty( "java.library.path", rxtxlib );
		Field fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
		fieldSysPath.setAccessible( true );
		fieldSysPath.set( null, null );

		System.getProperties().setProperty("gnu.io.rxtx.SerialPorts", this.ports);
		
		@SuppressWarnings("rawtypes")
		Enumeration ports2 = CommPortIdentifier.getPortIdentifiers();
		LinkedList<String> ports = new LinkedList<String>();

		int i = 0;
		while (ports2.hasMoreElements()) {
			i++;
			CommPortIdentifier port = (CommPortIdentifier) ports2.nextElement();
			String name = port.getName();

			if (name.startsWith("/dev/ttyACM") || name.startsWith("/dev/ttyUSB")) {
				System.out.println(i + ") " + name);
				ports.push(port.getName());
			}
		}

		try {
			for (String port : ports) {
				System.out.println("Try " + port);
				this.connect(port);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Main m = new Main();
        CmdLineParser parser = new CmdLineParser(m);
        try {
                parser.parseArgument(args);
                m.start();
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
        
	}
}