package com.simonevertsson.columbus.dummy;

import com.simonevertsson.columbus.Mapping;

public class TicketViewModel {

  @Mapping(clazz = Movie.class, field = "title")
  private String movie;

  @Mapping(clazz = Movie.class, field = "cost")
  private float cost;

  @Mapping(clazz = Person.class, field = "name")
  private String boughtBy;

  public String getMovie() {
    return movie;
  }

  public void setMovie(String movie) {
    this.movie = movie;
  }

  public float getCost() {
    return cost;
  }

  public void setCost(float cost) {
    this.cost = cost;
  }

  public String getBoughtBy() {
    return boughtBy;
  }

  public void setBoughtBy(String boughtBy) {
    this.boughtBy = boughtBy;
  }
}
