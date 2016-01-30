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

      try {
        Field dstField = dst.getClass().getDeclaredField(srcMapping.field());
        dstField.set(dst, srcField.get(src));
      } catch (NoSuchFieldException e) {
        continue;
      } catch (IllegalAccessException e) {
        throw e;
      }
    }

    return dst;
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

      try {
        Field dstField = dst.getClass().getDeclaredField(srcMapping.field());
        srcField.set(src, dstField.get(dst));
      } catch (NoSuchFieldException e) {
        continue;
      } catch (IllegalAccessException e) {
        throw e;
      }
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
