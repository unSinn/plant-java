package ch.ma3.plant.relay;

import java.io.IOException;

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

	public static final int LIGHTPIN = 0;

	private static Logger log = LogManager.getLogger(Relay.class);

	private InterfaceKitPhidget ik;
	private boolean[] switchState = { false, false, false, false };
	private Thread thread;

	public Relay() {
		thread = new Thread(this);
		thread.start();
	}

	public void setSwitch(int pin, boolean onOff) {
		try {
			if (!ik.isAttached()) {
				log.info("switch " + pin + " not turned " + onOff
						+ " because no relay was attached.");
				return;
			}
			if(switchState[pin] = onOff){
				log.info("Turning switch " + pin + " to " + onOff);
			}
			switchState[pin] = onOff;
			ik.setOutputState(pin, onOff);

		} catch (PhidgetException e) {
			log.error(e);
		}
	}

	public boolean getSwitchState(int pin) {
		return switchState[pin];
	}

	private void restoreSwitchStates() throws PhidgetException, IOException {
		for (int i = 0; i < switchState.length; i++) {
			log.info("Restoring switch " + i + " to " + switchState[i]);
			ik.setOutputState(i, switchState[i]);
		}
	}

	public void run() {
		try {
			openPhidget();
			Thread.sleep(1000);
		} catch (PhidgetException | InterruptedException e) {
			log.error(e);
		}
	}

	private void openPhidget() throws PhidgetException {
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

}
