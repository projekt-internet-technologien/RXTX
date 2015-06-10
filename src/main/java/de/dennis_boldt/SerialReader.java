package de.dennis_boldt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

public class SerialReader extends Observable implements Runnable {

	InputStream in;

	public SerialReader(InputStream in) {
		this.in = in;
	}

	public void run() {
		byte[] buffer = new byte[1024];
		int len = -1;
		try {
			while ((len = this.in.read(buffer)) > -1) {
				String s = new String(buffer, 0, len);
				setChanged();
				notifyObservers(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
	}
}