package mods.railcraft.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class ReflectionUtil {

  public static boolean isAnnotated(Class<? extends Annotation> annotation, Object obj) {
    return obj.getClass().isAnnotationPresent(annotation);
  }

  public static boolean isAnnotatedDeepSearch(Class<? extends Annotation> annotation, Object obj) {
    if (isAnnotated(annotation, obj))
      return true;
    Class<?> objClass = obj.getClass();
    do {
      if (Arrays.stream(objClass.getInterfaces()).anyMatch(c -> c.isAnnotationPresent(annotation)))
        return true;
    } while ((objClass = objClass.getSuperclass()) != Object.class);
    return false;
  }
}
