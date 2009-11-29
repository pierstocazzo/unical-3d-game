package utils;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class Util {
    public static final Quaternion Z45  = new Quaternion().fromAngleAxis( FastMath.QUARTER_PI, Vector3f.UNIT_Z );
    public static final Quaternion Z90  = new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_Z );
    public static final Quaternion Z180 = new Quaternion().fromAngleAxis( FastMath.PI, Vector3f.UNIT_Z );
    public static final Quaternion Z270 = new Quaternion().fromAngleAxis( FastMath.PI*3/2, Vector3f.UNIT_Z );
    public static final Quaternion Y45  = new Quaternion().fromAngleAxis( FastMath.QUARTER_PI, Vector3f.UNIT_Y );
    public static final Quaternion Y90  = new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_Y );
    public static final Quaternion Y180 = new Quaternion().fromAngleAxis( FastMath.PI, Vector3f.UNIT_Y );
    public static final Quaternion Y270 = new Quaternion().fromAngleAxis( FastMath.PI*3/2, Vector3f.UNIT_Y );
    public static final Quaternion X45  = new Quaternion().fromAngleAxis( FastMath.QUARTER_PI, Vector3f.UNIT_X );
    public static final Quaternion X90  = new Quaternion().fromAngleAxis( FastMath.HALF_PI, Vector3f.UNIT_X );
    public static final Quaternion X180 = new Quaternion().fromAngleAxis( FastMath.PI, Vector3f.UNIT_X);
    public static final Quaternion X270 = new Quaternion().fromAngleAxis( FastMath.PI*3/2, Vector3f.UNIT_X );
    public static final Quaternion INVY = new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X );
}
