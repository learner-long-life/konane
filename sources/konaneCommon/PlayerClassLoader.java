/* PlayerClassLoader.java
 * Konane Game System
 * MIT IEEE IAP Programming Competition 2001
 * Paul Pham, ppham@mit.edu
 */

package konaneCommon;

public final class PlayerClassLoader extends ClassLoader {

    public Class loadClass(String className) throws ClassNotFoundException {
	return findSystemClass(className);
    }

}
