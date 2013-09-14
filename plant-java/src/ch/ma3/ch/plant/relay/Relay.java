package ch.ma3.ch.plant.relay;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;

public class Relay {
	private InterfaceKitPhidget ik;

	public Relay() throws PhidgetException {

		ik = new InterfaceKitPhidget();
		ik.addErrorListener(new ErrorListener() {
			@Override
			public void error(ErrorEvent ee) {
				System.out.println(ee);
			}
		});
		ik.addAttachListener(new AttachListener() {
			@Override
			public void attached(AttachEvent ae) {
				System.out.println(ae);

			}
		});
		ik.openAny();
		ik.waitForAttachment();

	}

	public void setSwitch(int nr, boolean value) throws PhidgetException {

		ik.setOutputState(nr, value);

	}

}
