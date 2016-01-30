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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ColumbusTests {

  @Test
  public void testSrcMappedToDestination() {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    succeedingA.fieldA = "test";
    succeedingA.fieldB = 1337;

    SucceedingB succeedingB = new SucceedingB();

    // Act
    try {
      Columbus.mapToDst(succeedingA, succeedingB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertThat(succeedingB.fieldC, is("test"));
    assertThat(succeedingB.fieldD, is(1337));
    assertThat(succeedingA.fieldA, is("test"));
    assertThat(succeedingA.fieldB, is(1337));
  }

  @Test
  public void testSrcMappedFromDestination() {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    SucceedingB succeedingB = new SucceedingB();
    succeedingB.fieldC = "test";
    succeedingB.fieldD = 1337;

    // Act
    try {
      Columbus.mapFromDst(succeedingA, succeedingB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertThat(succeedingA.fieldA, is("test"));
    assertThat(succeedingA.fieldB, is(1337));
    assertThat(succeedingB.fieldC, is("test"));
    assertThat(succeedingB.fieldD, is(1337));
  }

  @Test
  public void testDoesNotAlterDestinationIfDestinationIsWrongClass() {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    SucceedingC succeedingC = new SucceedingC();
    succeedingA.fieldA = "test";
    succeedingA.fieldB = 1337;

    // Act
    try {
      Columbus.mapFromDst(succeedingA, succeedingC);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertNull(succeedingC.fieldC);
    assertThat(succeedingC.fieldD, is(0));

  }

  @Test(expected=IllegalArgumentException.class)
  public void testMapToDstFailsIfDestinationFieldIsWrongType() {
    // Arrange
    IllegalArgumentA illegalArgumentA = new IllegalArgumentA();
    IllegalArgumentB illegalArgumentB = new IllegalArgumentB();
    illegalArgumentA.fieldA = "test";
    illegalArgumentA.fieldB = 1337;

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
    illegalArgumentA.fieldA = "test";
    illegalArgumentA.fieldB = 1337;

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
    noSuchFieldA.fieldA = "test";
    noSuchFieldA.fieldB = 1337;

    // Act
    try {
      Columbus.mapToDst(noSuchFieldA, noSuchFieldB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertThat(noSuchFieldA.fieldA, is("test"));
    assertThat(noSuchFieldA.fieldB, is(1337));
    assertNull(noSuchFieldB.fieldC);
    assertThat(noSuchFieldB.fieldD, is(1337));

  }

  @Test
  public void testMapFromDstSkipsUnknownFields() {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldB.fieldC = "test";
    noSuchFieldB.fieldD = 1337;

    // Act
    try {
      Columbus.mapFromDst(noSuchFieldA, noSuchFieldB);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    // Assert
    assertNull(noSuchFieldA.fieldA);
    assertThat(noSuchFieldA.fieldB, is(1337));
    assertThat(noSuchFieldB.fieldC, is("test"));
    assertThat(noSuchFieldB.fieldD, is(1337));

  }

  @Test
  public void testMapFromDstFailsIfTryingToAccessWrongField() {
    // Arrange
    IllegalAccessA illegalAccessA = new IllegalAccessA();
    IllegalAccessB illegalAccessB = new IllegalAccessB();
    illegalAccessA.fieldA = "test";
    illegalAccessA.fieldB = 1337;

    // Act
    boolean thrown = false;
    try {
      Columbus.mapFromDst(illegalAccessA, illegalAccessB);
    } catch (IllegalAccessException e) {
      thrown = true;
    }

    // Assert
    assertThat(thrown, is(true));

  }

  @Test
  public void testMapToDstFailsIfTryingToAccessWrongField() {
    // Arrange
    IllegalAccessA illegalAccessA = new IllegalAccessA();
    IllegalAccessB illegalAccessB = new IllegalAccessB();
    illegalAccessB.fieldC = "test";
    illegalAccessB.setFieldD(1337);

    // Act
    boolean thrown = false;
    try {
      Columbus.mapToDst(illegalAccessA, illegalAccessB);
    } catch (IllegalAccessException e) {
      thrown = true;
    }

    // Assert
    assertThat(thrown, is(true));

  }
}
