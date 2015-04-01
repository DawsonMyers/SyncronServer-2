package fx.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by Dawson on 3/30/2015.
 */
public class FxUtils {
	static              String           nameId = FxUtils.class.getSimpleName();
	public final static org.slf4j.Logger log    = org.slf4j.LoggerFactory.getLogger(nameId);

	public FxUtils() {}


	public static Object cloneObject(Object obj) {
		try {
			Object clone = obj.getClass().newInstance();
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if (field.get(obj) == null || Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				if (field.getType().isPrimitive() || field.getType().equals(String.class)
						|| field.getType().getSuperclass().equals(Number.class)
						|| field.getType().equals(Boolean.class)) {
					field.set(clone, field.get(obj));
				} else {
					Object childObj = field.get(obj);
					if (childObj == obj) {
						field.set(clone, clone);
					} else {
						field.set(clone, cloneObject(field.get(obj)));
					}
				}
			}
			return clone;
		} catch (Exception e) {
			return null;
		}
	}
}
