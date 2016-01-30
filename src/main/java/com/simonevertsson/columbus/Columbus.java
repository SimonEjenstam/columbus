package com.simonevertsson.columbus;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Columbus {

  /**
   * Tries to map fields, annotated with {@link Mapping} from a source object to a destination object.
   *
   * @param src The source object annotated which has field annotated with {@link Mapping}
   * @param dst The destination object whose fields will be updated with the mapped values.
   * @return The destination object
   */
  public static Object mapToDst(final Object src, final Object dst) throws IllegalAccessException {
    final Set<Field> srcMappedFields = findMappedFields(src.getClass());
    for(Field srcField : srcMappedFields) {
      final Mapping srcMapping = srcField.getAnnotation(Mapping.class);
      if(srcMapping.clazz() != dst.getClass()) continue;
      trySetDstField(src, dst, srcField, srcMapping);
    }

    return dst;
  }

  /**
   * Tries to set the field specified in the supplied {@link Mapping}.
   *
   * @param src The source object from which the value will be retrieved
   * @param dst The destination object which field will be set to the retrieved source value
   * @param srcField The specified field from the source object whose value will be written to the destination field
   * @param srcMapping The mapping annotation of the srcField describing which field the value should be written to
   * @throws IllegalAccessException if the field values are incompatible
     */
  private static void trySetDstField(final Object src, final Object dst, final Field srcField, final Mapping srcMapping) throws IllegalAccessException {
    // Store accessibility value to be able to override and reset field accessibility status.
    final boolean srcAccessible = srcField.isAccessible();
    boolean dstAccessible = true;
    Field dstField = null;
    try {
      dstField = dst.getClass().getDeclaredField(srcMapping.field());
      dstAccessible = dstField.isAccessible();
      srcField.setAccessible(true);
      dstField.setAccessible(true);
      dstField.set(dst, srcField.get(src));
    } catch (NoSuchFieldException e) {
      return;
    } catch (IllegalAccessException e) {
      throw e;
    } finally {
      srcField.setAccessible(srcAccessible);
      if(dstField != null) {
        dstField.setAccessible(dstAccessible);
      }
    }
  }

  /**
   * Tries to set the field specified in the supplied {@link Mapping}.
   *
   * @param src The source object from which the value will be retrieved
   * @param dst The destination object which field will be set to the retrieved source value
   * @param srcField The specified field from the source object whose value will be written to the destination field
   * @param srcMapping The mapping annotation of the srcField describing which field the value should be written to
   * @throws IllegalAccessException if the field values are incompatible
   */
  private static void tryGetDstField(final Object src, final Object dst, final Field srcField, final Mapping srcMapping) throws IllegalAccessException {
    // Store accessibility value to be able to override and reset field accessibility status.
    final boolean srcAccessible = srcField.isAccessible();
    boolean dstAccessible = true;
    Field dstField = null;
    try {
      dstField = dst.getClass().getDeclaredField(srcMapping.field());
      dstAccessible = dstField.isAccessible();
      srcField.setAccessible(true);
      dstField.setAccessible(true);
      srcField.set(src, dstField.get(dst));
    } catch (NoSuchFieldException e) {
      // Do nothing
    } catch (IllegalAccessException e) {
      throw e;
    } finally {
      srcField.setAccessible(srcAccessible);
      if(dstField != null) {
        dstField.setAccessible(dstAccessible);
      }
    }
  }


  /**
   * Tries to map fields to a source object annotated with {@link Mapping} from a destination object.
   *
   * @param src The source object annotated which has fields annotated with {@link Mapping} that will be updated with values from destination object
   * @param dst The destination object whose fields will be attempted to be retrieved
   * @return The source object
   */
  public static Object mapFromDst(final Object src, final Object dst) throws IllegalAccessException {
    final Set<Field> srcMappedFields = findMappedFields(src.getClass());

    for(Field srcField : srcMappedFields) {
      final Mapping srcMapping = srcField.getAnnotation(Mapping.class);
      if(srcMapping.clazz() != dst.getClass()) continue;
      tryGetDstField(src, dst, srcField, srcMapping);
    }

    return src;
  }


  /**
   * @return null safe set
   */
  private static Set<Field> findMappedFields(Class<?> classs) {
    Set<Field> set = new HashSet<>();
    Class<?> c = classs;
    while (c != null) {
      for (Field field : c.getDeclaredFields()) {
        if (field.isAnnotationPresent(Mapping.class)) {
          set.add(field);
        }
      }
      c = c.getSuperclass();
    }

    return set;
  }
}
