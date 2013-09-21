package ch.ma3.plant.relay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;

public class Relay implements Runnable {
	private static Logger log = LogManager.getLogger(Relay.class);

	private InterfaceKitPhidget ik;
	private boolean[] switchState;
	private DateFormat df = DateFormat.getDateInstance(DateFormat.LONG,
			Locale.GERMAN);
	private Thread thread;
	private File switchStatesFile;
	private FileReader fr;
	private FileWriter fw;

	public Relay() {
		switchStatesFile = new File("switchstates");
		try {
			fr = new FileReader(switchStatesFile);
			fw = new FileWriter(switchStatesFile);
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}

		thread = new Thread(this);
		thread.start();
	}

	public void setSwitch(int pin, boolean onOff) {
		try {
			while (!ik.isAttached()) {
				// wait
			}
			log.info(df.format(new Date(System.currentTimeMillis()))
					+ " Turning switch " + pin + " to " + onOff);
			switchState[pin] = onOff;
			ik.setOutputState(pin, onOff);

			saveSwitchState();
		} catch (PhidgetException | IOException e) {
			log.error(e);
		}
	}

	private void saveSwitchState() throws IOException {
		switchStatesFile.createNewFile();
		fw.write(intFromBooleanArray(switchState));
		fw.close();
	}

	private void restoreSwitchStates() throws PhidgetException, IOException {
		booleanArrayFromByte(fr.read());
		fr.close();

		switchState[0] = true;
		for (int i = 0; i < switchState.length; i++) {
			log.info("Restoring switch " + i + " to " + switchState[i]);
			ik.setOutputState(i, switchState[i]);
		}
	}

	public void run() {
		try {
			openPhidget();
		} catch (PhidgetException e) {
			log.error(e);
		}
	}

	private void openPhidget() throws PhidgetException {
		switchState = new boolean[4];

		// Phidget.enableLogging(Phidget.PHIDGET_LOG_ERROR, "");

		ik = new InterfaceKitPhidget();

		ik.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				log.info("Phidget ErrorEvent" + ee);
			}
		});
		ik.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				log.info("attachment of " + ae);
				try {
					restoreSwitchStates();
				} catch (PhidgetException | IOException e) {
					log.error("Error while restoring switch states");
					log.error(e);
				}
			}
		});

		ik.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				log.info("detachment of " + ae);
			}
		});

		ik.openAny();
		log.info("waiting for Phidget attachment...");
		ik.waitForAttachment();
	}

	private boolean[] booleanArrayFromByte(int x) {
		boolean bs[] = new boolean[4];
		bs[0] = ((x & 0x01) != 0);
		bs[1] = ((x & 0x02) != 0);
		bs[2] = ((x & 0x04) != 0);
		bs[3] = ((x & 0x08) != 0);
		return bs;
	}

	private int intFromBooleanArray(boolean[] ba) {
		int by = 0;

		for (int i = 0; i < ba.length; i++) {
			if (ba[i]) {
				by++;
			}
			by = (by << 1);
		}

		return by;
	}
}
