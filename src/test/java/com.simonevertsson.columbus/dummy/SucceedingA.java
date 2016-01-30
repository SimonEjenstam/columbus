package com.simonevertsson.columbus.dummy;


import com.simonevertsson.columbus.Mapping;

public class SucceedingA {
  @Mapping(clazz = SucceedingB.class, field = "fieldC") // Maps correctly
  private String fieldA;

  @Mapping(clazz = SucceedingB.class, field = "fieldD") // Maps correctly
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
