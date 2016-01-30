package com.simonevertsson.columbus.dummy;

import com.simonevertsson.columbus.Mapping;

public class NoSuchFieldA {

  @Mapping(clazz = NoSuchFieldB.class, field = "fieldE") // Reference an unknown field (skips the mapping)
  private String fieldA;

  @Mapping(clazz = NoSuchFieldB.class, field = "fieldD") // Maps correctly
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
