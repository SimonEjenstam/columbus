package com.simonevertsson.columbus.dummy;


import com.simonevertsson.columbus.Mapping;

public class SucceedingA {
  @Mapping(clazz = SucceedingB.class, field = "fieldC") // Maps correctly
  public String fieldA;

  @Mapping(clazz = SucceedingB.class, field = "fieldD") // Maps correctly
  public int fieldB;
}
