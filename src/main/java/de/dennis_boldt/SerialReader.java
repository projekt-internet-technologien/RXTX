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
				byte[] bytes = new byte[len];

				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = buffer[i];
				}

				setChanged();
				notifyObservers(bytes);
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