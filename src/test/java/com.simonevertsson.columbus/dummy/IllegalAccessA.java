package com.simonevertsson.columbus.dummy;

import com.simonevertsson.columbus.Mapping;

public class IllegalAccessA {

  @Mapping(clazz = IllegalAccessB.class, field = "fieldC") // Maps correctly
  private String fieldA;

  @Mapping(clazz = IllegalAccessB.class, field = "fieldD") // Throws exception since field is private
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
