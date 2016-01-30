package com.simonevertsson.columbus.dummy;

import com.simonevertsson.columbus.Mapping;

public class IllegalArgumentA {

  @Mapping(clazz = IllegalArgumentB.class, field = "fieldC") // Maps correctly
  public String fieldA;

  @Mapping(clazz = IllegalArgumentB.class, field = "fieldD") // Should fail, maps int -> boolean
  public int fieldB;

}
