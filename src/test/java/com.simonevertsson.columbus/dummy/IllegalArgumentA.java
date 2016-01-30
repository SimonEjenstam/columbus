package com.simonevertsson.columbus.dummy;

import com.simonevertsson.columbus.Mapping;

public class IllegalArgumentA {

  @Mapping(clazz = IllegalArgumentB.class, field = "fieldC") // Maps correctly
  private String fieldA;

  @Mapping(clazz = IllegalArgumentB.class, field = "fieldD") // Should fail, maps int -> boolean
  private int fieldB;

  public String getFieldA() {
    return fieldA;
  }

  public void setFieldA(String fieldA) {
    this.fieldA = fieldA;
  }

  public int getFieldB() {
    return fieldB;
  }

  public void setFieldB(int fieldB) {
    this.fieldB = fieldB;
  }
}
