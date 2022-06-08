package CommonClasses;

import java.io.Serializable;
import java.util.HashMap;

public class PlainData implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    public String plainName;
    public String aileron;
    public String elevator;
    public String rudder;
    public String flaps;
    public String longitude;
    public String latitude;
    public String airSpeed_kt;
    public String vertSpeed;
    public String throttle_0;
    public String throttle_1;
    public String altitude;
    public String pitchDeg;
    public String rollDeg;
    public String heading;
    public String turnCoordinator;

    public PlainData() {
    }
}