package com.simonevertsson.columbus;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class Columbus {

  /**
   * A class which will be scanned to find fields with the {@link Mapping} annotation
   * @return A set of all fields annotated with {@link Mapping}.
   */
  private static Set<Field> findMappedFields(Class<?> clazz) {
    Set<Field> set = new HashSet<>();
    Class<?> c = clazz;
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

  /**
   * Tries to map fields, annotated with {@link Mapping} from a source object to a destination object.
   *
   * @param source The source object which has field annotated with {@link Mapping}
   * @param destinations Vararg with destination objects whose fields will be updated with the mapped values.
   */
  public static void mapTo(final Object source, final Object... destinations) {
    final Set<Field> srcMappedFields = findMappedFields(source.getClass());
    for(Field srcField : srcMappedFields) {
      final Mapping srcMapping = srcField.getAnnotation(Mapping.class);
      for(Object destination : destinations) {
        if (srcMapping.clazz() == destination.getClass()) {
          try {
            trySetField(source, destination, srcField, srcMapping);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }

          break;
        }
      }
    }
  }

  /**
   * Tries to set the field specified in the supplied {@link Mapping} using the value of the source field.
   *
   * @param source The source object from which the value will be retrieved
   * @param destination The destination object which field will be set to the retrieved source value
   * @param sourceField The specified field from the source object whose value will be written to the destination field
   * @param sourceMapping The mapping annotation of the sourceField describing which field the value should be written to
   * @throws IllegalAccessException if the field values are incompatible
     */
  private static void trySetField(final Object source, final Object destination,
                                  final Field sourceField, final Mapping sourceMapping) throws IllegalAccessException {
    // Store accessibility value to be able to override and reset field accessibility status.
    final boolean srcAccessible = sourceField.isAccessible();
    boolean destinationAccessible = true;
    Field destinationField = null;
    try {
      destinationField = destination.getClass().getDeclaredField(sourceMapping.field());
      destinationAccessible = destinationField.isAccessible();
      sourceField.setAccessible(true);
      destinationField.setAccessible(true);
      destinationField.set(destination, sourceField.get(source));
    } catch (NoSuchFieldException e) {
      return;
    } catch (IllegalAccessException e) {
      throw e;
    } finally {
      sourceField.setAccessible(srcAccessible);
      if(destinationField != null) {
        destinationField.setAccessible(destinationAccessible);
      }
    }
  }

  /**
   * Tries to map fields to a source object annotated with {@link Mapping} from a destination object.
   *
   * @param destination The destination object annotated which has fields annotated with {@link Mapping} that will be updated with values from source object(s)
   * @param sources Vararg with source objects whose fields will be attempted to be retrieved.
   */
  public static void mapFrom(final Object destination, final Object... sources) {
    final Set<Field> destinationMappedFields = findMappedFields(destination.getClass());

    for(Field destinationField : destinationMappedFields) {
      final Mapping destinationMapping = destinationField.getAnnotation(Mapping.class);
      for(Object source : sources) {
        if (destinationMapping.clazz() == source.getClass()) {
          try {
            tryGetField(destination, source, destinationField, destinationMapping);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }

          break;
        }
      }
    }
  }

  /**
   * Tries to get the value of the field specified in the supplied {@link Mapping}.
   *
   * @param destination The source object from which the value will be retrieved
   * @param source The destination object which field will be set to the retrieved source value
   * @param destinationField The specified field from the source object whose value will be written to the destination field
   * @param destinationMapping The mapping annotation of the sourceField describing which field the value should be written to
   * @throws IllegalAccessException if the field values are incompatible
   */
  private static void tryGetField(final Object destination, final Object source, final Field destinationField,
                                  final Mapping destinationMapping) throws IllegalAccessException {
    // Store accessibility value to be able to override and reset field accessibility status.
    final boolean destinationAccessible = destinationField.isAccessible();
    boolean sourceAccessible = true;
    Field sourceField = null;
    try {
      sourceField = source.getClass().getDeclaredField(destinationMapping.field());
      sourceAccessible = sourceField.isAccessible();
      destinationField.setAccessible(true);
      sourceField.setAccessible(true);
      destinationField.set(destination, sourceField.get(source));
    } catch (NoSuchFieldException e) {
      // Do nothing
    } catch (IllegalAccessException e) {
      throw e;
    } finally {
      destinationField.setAccessible(destinationAccessible);
      if(sourceField != null) {
        sourceField.setAccessible(sourceAccessible);
      }
    }
  }

}
