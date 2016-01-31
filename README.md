# Columbus - A simple Java object mapper

[![Build Status](https://travis-ci.org/sievertsson/columbus.svg?branch=master)](https://travis-ci.org/sievertsson/columbus)
[![Coverage Status](https://coveralls.io/repos/github/sievertsson/columbus/badge.svg?branch=master)](https://coveralls.io/github/sievertsson/columbus?branch=master)
[![](https://jitpack.io/v/sievertsson/columbus.svg)](https://jitpack.io/#sievertsson/columbus)

In a pure layered application UI-, domain-, and data-entities are completely separated classes. While being beneficial, the process of mapping between these enitities can easily become rather mind numbing work.

This is where Columbus enters. Columbus uses annotations to map fields between classes.

## How to use

Columbus relies on field annotations and reflections to perform the mapping. The Mapping annotation contains two elements. A class reference and a field name to which the annotated field should be mapped to.
```java
@Mapping(clazz = Person.class, field="name")
private String owner;
```
When the fields which should be mapped together have been annotated correctly only two methods are needed:
```java
Columbus.mapTo(sourceObject, destinationObject);  // Retrieves the mapped fields from the sourceObject and writes them to the correct field in the destinationObject

Columbus.mapFrom(destinationObject, sourceObject);  // Retrieves the mapped fields from sourceObject and writes them to the correct field in the destinationObject
```

### One-to-one mapping
A common use case is when you want mapping between your UI entities and your domain entities. However in most cases field names stay the same. Here is an example how to create a mapping between Movie domain-entity and a MovieViewModel UI-entity.

*Movie*

```java
public class Movie {

  private String title;

  private float cost;

  private int rating;
  
}
```
*MovieViewModel* (Note that only one of the classes needs to be annotated.)
```java
public class MovieViewModel {

  @Mapping(clazz = Movie.class, field="title")
  private String title;

  @Mapping(clazz = Movie.class, field="cost")
  private float cost;
  
  @Mapping(clazz = Movie.class, field="rating")
  private int rating;
  
}
```

Now instances of these two classes can be mapped with each other:
```java
// Create a Movie instance (this is probably created somewhere else)
Movie movie = new Movie();
movie.setTitle("Star Wars: The Force Awakens");
movie.setCoset(123.50f);
movie.setRating(5);

// Create the destionation object where the mapped values will be written to
MovieViewModel movieViewModel = new MovieViewModel();

Columbus.mapFrom(movieViewModel, movie);  // Retrieve the mapped fields from movie to moviewViewModel
```
The movieViewModel now contains the values of the mapped fields.

If you want to go the other way around it's just as simple:
```java
MovieViewModel movieViewModel = new MovieViewModel();
movieViewModel.setTitle("Star Wars: The Force Awakens");
movieViewModel.setCoset(123.50f);
movieViewModel.setRating(5);

Movie movie = new Movie();

Columbus.mapTo(movieViewModel, movie);  // Writes the mapped fields from movieViewModel to movie
```

### One-to-many mapping
In some cases you might find yourself in a situation where you want to combine two or more entities into one. With Columbus this is just as simple as in the One-to-one case. As an example, imagine that we want to combine information from a Movie- and a Person-entity to create a TicketViewModel. The Movie-entity is defined as above.

*Person*

```java
public class Movie {

  private String name;

  private float age;
  
}
```

*TicketViewModel*

```java
public class TicketViewModel {

  @Mapping(clazz = Movie.class, field="title")
  private String movie;
  
  @Mapping(clazz = Movie.class, field="cost")
  private float cost;
  
  @Mapping(clazz = Person.class, field="name")
  private String boughtBy;
  
}
```

Mapping the values from a person and a movie object you can now simply be done by:
```java
Movie movie = new Movie();
movie.setTitle("Star Wars: The Force Awakens");
movie.setCost(123.50f);

Person person = new Person();
person.setName("Simon Evertsson");

TicketViewModel ticketViewModel = new TicketViewModel();

Columbus.mapFrom(ticketViewModel, movie, person);  // mapFrom and mapTo accepts varArgs to support one-to-many mappings
```

And to write the corresponding fields back to a Movie and a Merson instance:
```java
TicketViewModel ticketViewModel = new TicketViewModel();
ticketViewModel.setMovie("Star Wars: The Force Awakens");
ticketViewModel.setCost(123.50f);
ticketViewModel.boughtBy("Simon Evertsson");

Movie movie = new Movie();
Person person = new Person();

Columbus.mapTo(ticketViewModel, movie, person);
```

## License

The MIT License (MIT)

Copyright (c) 2016 Simon Evertsson

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
