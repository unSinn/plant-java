package ch.ma3.plant.relay;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;

public class Relay {
	private InterfaceKitPhidget ik;
	private boolean[] switchState;
	private DateFormat df = DateFormat.getDateInstance(DateFormat.LONG,
			Locale.GERMAN);

	public Relay() throws PhidgetException {
		switchState = new boolean[4];

		// Phidget.enableLogging(Phidget.PHIDGET_LOG_ERROR, "");

		ik = new InterfaceKitPhidget();

		ik.addErrorListener(new ErrorListener() {
			public void error(ErrorEvent ee) {
				System.out.println("Phidget ErrorEvent" + ee);
			}
		});
		ik.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) {
				System.out.println("attachment of " + ae);
				try {
					restoreSwitchStates();
				} catch (PhidgetException e) {
					System.out.println("Error while restoring switch states"
							+ e);
					e.printStackTrace();
				}
			}
		});

		ik.addDetachListener(new DetachListener() {
			public void detached(DetachEvent ae) {
				System.out.println("detachment of " + ae);
			}
		});

		ik.openAny();
		System.out.println("waiting for Phidget attachment...");
		ik.waitForAttachment();

	}

	public void setSwitch(int pin, boolean onOff) throws PhidgetException {
		System.out.println(df.format(new Date(System.currentTimeMillis()))
				+ " Turning switch " + pin + " to " + onOff);
		switchState[pin] = onOff;
		ik.setOutputState(pin, onOff);
	}

	private void restoreSwitchStates() throws PhidgetException {
		for (int i = 0; i < switchState.length; i++) {
			System.out.println("Restoring switch " + i + " to "
					+ switchState[i]);
			ik.setOutputState(i, switchState[i]);
		}

	}
}
