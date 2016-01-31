package com.simonevertsson.columbus;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
   * Tries to map fields, annotated with {@link Mapping} from a source object to an array of destination object(s).
   *
   * @param source The source object which has field annotated with {@link Mapping}
   * @param destinations Vararg with destination object(s) whose fields will be updated with the mapped values.
   */
  public static void mapTo(final Object source, final Object... destinations) throws IllegalAccessException {
    final Set<Field> sourceMappedFields = findMappedFields(source.getClass());
    for(Field sourceField : sourceMappedFields) {
      final Mapping srcMapping = sourceField.getAnnotation(Mapping.class);
      for(Object destination : destinations) {
        if (srcMapping.clazz() == destination.getClass()) {
          trySetField(source, destination, sourceField, srcMapping.field());
          break;
        }
      }
    }
  }

  /**
   * Tries to set the value of the field specified in the supplied field name, from the source object, and write it to
   * the destination field.
   *
   * @param source The source object from which the value will be retrieved
   * @param destination The destination object whose field, specified by the supplied field name, will be set to the
   *                    retrieved source value
   * @param sourceField The field from the source object whose value will be written to the destination field
   * @param fieldName The name of the field in the destination object to which the value should be written to
   * @throws IllegalAccessException if the field values are incompatible
     */
  private static void trySetField(final Object source, final Object destination,
                                  final Field sourceField, final String fieldName) throws IllegalAccessException {
    // Store accessibility value to be able to override and reset field accessibility status.
    final boolean sourceAccessible = sourceField.isAccessible();
    boolean destinationAccessible = true;
    Field destinationField = null;
    try {
      destinationField = destination.getClass().getDeclaredField(fieldName);
      destinationAccessible = destinationField.isAccessible();
      if(!Modifier.isFinal(destinationField.getModifiers())) {
        destinationField.setAccessible(true);
      }

      sourceField.setAccessible(true);
      destinationField.set(destination, sourceField.get(source));
    } catch (NoSuchFieldException e) {
      return;
    } finally {
      sourceField.setAccessible(sourceAccessible);
      if(destinationField != null) {
        destinationField.setAccessible(destinationAccessible);
      }
    }
  }

  /**
   * Tries to map fields to a destination object annotated with {@link Mapping} from an array of source object(s).
   *
   * @param destination The destination object annotated which has fields annotated with {@link Mapping} that will be updated with values from source object(s)
   * @param sources Vararg with source object(s) whose fields will be attempted to be retrieved.
   */
  public static void mapFrom(final Object destination, final Object... sources) throws IllegalAccessException {
    final Set<Field> destinationMappedFields = findMappedFields(destination.getClass());

    for(Field destinationField : destinationMappedFields) {
      final Mapping destinationMapping = destinationField.getAnnotation(Mapping.class);
      for(Object source : sources) {
        if (destinationMapping.clazz() == source.getClass()) {
          tryGetField(destination, source, destinationField, destinationMapping.field());
          break;
        }
      }
    }
  }

  /**
   * Tries to get the value of the field specified in the supplied field name, from the source object, and write it to
   * the destination field.
   *
   * @param destination The destination object whose field will be set to the retrieved source value
   * @param source The source object from which the value will be retrieved
   * @param destinationField The specified field from the destination object whose value will recieve the value of the
   *                         specified source field.
   * @param fieldName The name of the field in the source object from which the value should be retrieved
   * @throws IllegalAccessException if the field values are incompatible
   */
  private static void tryGetField(final Object destination, final Object source, final Field destinationField,
                                  final String fieldName) throws IllegalAccessException {
    // Store accessibility value to be able to override and reset field accessibility status.
    final boolean destinationAccessible = destinationField.isAccessible();
    boolean sourceAccessible = true;
    Field sourceField = null;
    try {
      sourceField = source.getClass().getDeclaredField(fieldName);
      sourceAccessible = sourceField.isAccessible();
      if(!Modifier.isFinal(destinationField.getModifiers())) {
        destinationField.setAccessible(true);
      }

      sourceField.setAccessible(true);
      destinationField.set(destination, sourceField.get(source));
    } catch (NoSuchFieldException e) {
      // Do nothing
    }  finally {
      destinationField.setAccessible(destinationAccessible);
      if(sourceField != null) {
        sourceField.setAccessible(sourceAccessible);
      }
    }
  }

}
