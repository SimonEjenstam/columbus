package com.simonevertsson.columbus;

import com.simonevertsson.columbus.dummy.IllegalAccessA;
import com.simonevertsson.columbus.dummy.IllegalAccessB;
import com.simonevertsson.columbus.dummy.NoSuchFieldA;
import com.simonevertsson.columbus.dummy.NoSuchFieldB;
import com.simonevertsson.columbus.dummy.SucceedingA;
import com.simonevertsson.columbus.dummy.SucceedingB;
import com.simonevertsson.columbus.dummy.SucceedingC;
import com.simonevertsson.columbus.dummy.IllegalArgumentA;
import com.simonevertsson.columbus.dummy.IllegalArgumentB;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ColumbusTests {

  @Test
  public void testSrcMappedToDestination() {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    succeedingA.setFieldA("test");
    succeedingA.setFieldB(1337);;

    SucceedingB succeedingB = new SucceedingB();

    // Act
    try {
      Columbus.mapToDst(succeedingA, succeedingB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertThat(succeedingB.getFieldC(), is("test"));
    assertThat(succeedingB.getFieldD(), is(1337));
    assertThat(succeedingA.getFieldA(), is("test"));
    assertThat(succeedingA.getFieldB(), is(1337));
  }

  @Test
  public void testSrcMappedFromDestination() {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    SucceedingB succeedingB = new SucceedingB();
    succeedingB.setFieldC("test");
    succeedingB.setFieldD(1337);

    // Act
    try {
      Columbus.mapFromDst(succeedingA, succeedingB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertThat(succeedingA.getFieldA(), is("test"));
    assertThat(succeedingA.getFieldB(), is(1337));
    assertThat(succeedingB.getFieldC(), is("test"));
    assertThat(succeedingB.getFieldD(), is(1337));
  }

  @Test
  public void testDoesNotAlterDestinationIfDestinationIsWrongType() {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    SucceedingC succeedingC = new SucceedingC();
    succeedingA.setFieldA("test");
    succeedingA.setFieldB(1337);

    // Act
    try {
      Columbus.mapFromDst(succeedingA, succeedingC);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertNull(succeedingC.getFieldC());
    assertThat(succeedingC.getFieldD(), is(0));

  }

  @Test(expected=IllegalArgumentException.class)
  public void testMapToDstFailsIfDestinationFieldIsWrongType() {
    // Arrange
    IllegalArgumentA illegalArgumentA = new IllegalArgumentA();
    IllegalArgumentB illegalArgumentB = new IllegalArgumentB();
    illegalArgumentA.setFieldA("test");
    illegalArgumentA.setFieldB(1337);

    // Act
    try {
      Columbus.mapToDst(illegalArgumentA, illegalArgumentB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

  }

  @Test(expected=IllegalArgumentException.class)
  public void testMapFromDstFailsIfDestinationFieldIsWrongType() {
    // Arrange
    IllegalArgumentA illegalArgumentA = new IllegalArgumentA();
    IllegalArgumentB illegalArgumentB = new IllegalArgumentB();
    illegalArgumentA.setFieldA("test");
    illegalArgumentA.setFieldB(1337);

    // Act
    try {
      Columbus.mapFromDst(illegalArgumentA, illegalArgumentB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void testMapToDstSkipsUnknownFields() {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldA.setFieldA("test");
    noSuchFieldA.setFieldB(1337);

    // Act
    try {
      Columbus.mapToDst(noSuchFieldA, noSuchFieldB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertThat(noSuchFieldA.getFieldA(), is("test"));
    assertThat(noSuchFieldA.getFieldB(), is(1337));
    assertNull(noSuchFieldB.getFieldC());
    assertThat(noSuchFieldB.getFieldD(), is(1337));

  }

  @Test
  public void testMapFromDstSkipsUnknownFields() {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldB.setFieldC("test");
    noSuchFieldB.setFieldD(1337);

    // Act
    try {
      Columbus.mapFromDst(noSuchFieldA, noSuchFieldB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertNull(noSuchFieldA.getFieldA());
    assertThat(noSuchFieldA.getFieldB(), is(1337));
    assertThat(noSuchFieldB.getFieldC(), is("test"));
    assertThat(noSuchFieldB.getFieldD(), is(1337));

  }

  @Test
  public void testMapFromDstDoesNotAlterAccessibilityIfEncounteringUnknownFields() throws NoSuchFieldException {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldB.setFieldC("test");
    noSuchFieldB.setFieldD(1337);

    // Act
    try {
      Columbus.mapFromDst(noSuchFieldA, noSuchFieldB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    Field fieldA = noSuchFieldA.getClass().getDeclaredField("fieldA");
    Field fieldB = noSuchFieldA.getClass().getDeclaredField("fieldB");
    Field fieldC = noSuchFieldB.getClass().getDeclaredField("fieldC");
    Field fieldD = noSuchFieldB.getClass().getDeclaredField("fieldD");
    assertThat(fieldA.isAccessible(), is(false));
    assertThat(fieldB.isAccessible(), is(false));
    assertThat(fieldC.isAccessible(), is(false));
    assertThat(fieldD.isAccessible(), is(false));
  }

  @Test
  public void testMapToDstDoesNotAlterAccessibilityIfEncounteringUnknownFields() throws NoSuchFieldException {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldB.setFieldC("test");
    noSuchFieldB.setFieldD(1337);

    // Act
    try {
      Columbus.mapToDst(noSuchFieldA, noSuchFieldB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    Field fieldA = noSuchFieldA.getClass().getDeclaredField("fieldA");
    Field fieldB = noSuchFieldA.getClass().getDeclaredField("fieldB");
    Field fieldC = noSuchFieldB.getClass().getDeclaredField("fieldC");
    Field fieldD = noSuchFieldB.getClass().getDeclaredField("fieldD");
    assertThat(fieldA.isAccessible(), is(false));
    assertThat(fieldB.isAccessible(), is(false));
    assertThat(fieldC.isAccessible(), is(false));
    assertThat(fieldD.isAccessible(), is(false));
  }



}
