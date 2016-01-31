package com.simonevertsson.columbus;

import com.simonevertsson.columbus.dummy.*;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ColumbusTests {

  @Test
  public void testSrcMappedToDestination() throws IllegalAccessException {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    succeedingA.setFieldA("test");
    succeedingA.setFieldB(1337);

    SucceedingB succeedingB = new SucceedingB();

    // Act
    Columbus.mapTo(succeedingA, succeedingB);

    // Assert
    assertThat(succeedingB.getFieldC(), is("test"));
    assertThat(succeedingB.getFieldD(), is(1337));
    assertThat(succeedingA.getFieldA(), is("test"));
    assertThat(succeedingA.getFieldB(), is(1337));
  }

  @Test
  public void testSrcMappedFromDestination() throws IllegalAccessException {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    SucceedingB succeedingB = new SucceedingB();
    succeedingB.setFieldC("test");
    succeedingB.setFieldD(1337);

    // Act
    Columbus.mapFrom(succeedingA, succeedingB);

    // Assert
    assertThat(succeedingA.getFieldA(), is("test"));
    assertThat(succeedingA.getFieldB(), is(1337));
    assertThat(succeedingB.getFieldC(), is("test"));
    assertThat(succeedingB.getFieldD(), is(1337));
  }

  @Test
  public void testDoesNotAlterDestinationIfDestinationIsWrongType() throws IllegalAccessException {
    // Arrange
    SucceedingA succeedingA = new SucceedingA();
    SucceedingC succeedingC = new SucceedingC();
    succeedingA.setFieldA("test");
    succeedingA.setFieldB(1337);

    // Act
    Columbus.mapFrom(succeedingA, succeedingC);

    // Assert
    assertNull(succeedingC.getFieldC());
    assertThat(succeedingC.getFieldD(), is(0));

  }

  @Test(expected=IllegalArgumentException.class)
  public void testMapToDstFailsIfDestinationFieldIsWrongType() throws IllegalAccessException {
    // Arrange
    IllegalArgumentA illegalArgumentA = new IllegalArgumentA();
    IllegalArgumentB illegalArgumentB = new IllegalArgumentB();
    illegalArgumentA.setFieldA("test");
    illegalArgumentA.setFieldB(1337);

    // Act
    Columbus.mapTo(illegalArgumentA, illegalArgumentB);

  }

  @Test(expected=IllegalArgumentException.class)
  public void testMapFromDstFailsIfDestinationFieldIsWrongType() throws IllegalAccessException {
    // Arrange
    IllegalArgumentA illegalArgumentA = new IllegalArgumentA();
    IllegalArgumentB illegalArgumentB = new IllegalArgumentB();
    illegalArgumentA.setFieldA("test");
    illegalArgumentA.setFieldB(1337);

    // Act
    Columbus.mapFrom(illegalArgumentA, illegalArgumentB);

  }

  @Test
  public void testMapToDstSkipsUnknownFields() throws IllegalAccessException {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldA.setFieldA("test");
    noSuchFieldA.setFieldB(1337);

    // Act
    Columbus.mapTo(noSuchFieldA, noSuchFieldB);

    // Assert
    assertThat(noSuchFieldA.getFieldA(), is("test"));
    assertThat(noSuchFieldA.getFieldB(), is(1337));
    assertNull(noSuchFieldB.getFieldC());
    assertThat(noSuchFieldB.getFieldD(), is(1337));

  }

  @Test
  public void testMapFromDstSkipsUnknownFields() throws IllegalAccessException {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldB.setFieldC("test");
    noSuchFieldB.setFieldD(1337);

    // Act
    Columbus.mapFrom(noSuchFieldA, noSuchFieldB);

    // Assert
    assertNull(noSuchFieldA.getFieldA());
    assertThat(noSuchFieldA.getFieldB(), is(1337));
    assertThat(noSuchFieldB.getFieldC(), is("test"));
    assertThat(noSuchFieldB.getFieldD(), is(1337));

  }

  @Test
  public void testMapFromDstDoesNotAlterAccessibilityIfEncounteringUnknownFields() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldB.setFieldC("test");
    noSuchFieldB.setFieldD(1337);

    // Act
    Columbus.mapFrom(noSuchFieldA, noSuchFieldB);

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
  public void testMapToDstDoesNotAlterAccessibilityIfEncounteringUnknownFields() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    NoSuchFieldA noSuchFieldA = new NoSuchFieldA();
    NoSuchFieldB noSuchFieldB = new NoSuchFieldB();
    noSuchFieldB.setFieldC("test");
    noSuchFieldB.setFieldD(1337);

    // Act
    Columbus.mapTo(noSuchFieldA, noSuchFieldB);

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
  public void testMapFromDstMultipleClasses() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    Movie movie = new Movie();
    movie.setTitle("Star Wars: The Force Awakens");
    movie.setCost(123.50f);

    Person person = new Person();
    person.setName("Simon Evertsson");
    person.setAge(24);

    TicketViewModel ticketViewModel = new TicketViewModel();

    // Act
    Columbus.mapFrom(ticketViewModel, movie, person);

    // Assert
    assertThat(ticketViewModel.getMovie(), is("Star Wars: The Force Awakens"));
    assertThat(ticketViewModel.getCost(), is(123.50f));
    assertThat(ticketViewModel.getBoughtBy(), is("Simon Evertsson"));
  }

  @Test
  public void testMapToDstMultipleClasses() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    Movie movie = new Movie();
    Person person = new Person();

    TicketViewModel ticketViewModel = new TicketViewModel();
    ticketViewModel.setMovie("Star Wars: The Force Awakens");
    ticketViewModel.setCost(123.50f);
    ticketViewModel.setBoughtBy("Simon Evertsson");

    // Act
    Columbus.mapTo(ticketViewModel, movie, person);

    // Assert
    assertThat(movie.getTitle(), is("Star Wars: The Force Awakens"));
    assertThat(movie.getCost(), is(123.50f));
    assertThat(person.getName(), is("Simon Evertsson"));
  }

  @Test(expected=IllegalAccessException.class)
  public void testMapToDstFinalFields() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    IllegalAccessA illegalAccessA = new IllegalAccessA("test", 123);
    IllegalAccessB illegalAccessB = new IllegalAccessB("should not be overwritten", 0);

    // Act
    Columbus.mapTo(illegalAccessA, illegalAccessB);
  }

  @Test(expected=IllegalAccessException.class)
  public void testMapFromoDstFinalFields() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    IllegalAccessA illegalAccessA = new IllegalAccessA("test", 123);
    IllegalAccessB illegalAccessB = new IllegalAccessB("should not be overwritten", 0);

    // Act
    Columbus.mapFrom(illegalAccessA, illegalAccessB);
  }



}
