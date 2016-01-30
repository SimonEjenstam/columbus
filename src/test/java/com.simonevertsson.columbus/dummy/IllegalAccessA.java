package com.simonevertsson.columbus.dummy;

import com.simonevertsson.columbus.Mapping;

public class IllegalAccessA {

  @Mapping(clazz = IllegalAccessB.class, field = "fieldC") // Maps correctly
  public String fieldA;

  @Mapping(clazz = IllegalAccessB.class, field = "fieldD") // Throws exception since field is private
  public int fieldB;

}
