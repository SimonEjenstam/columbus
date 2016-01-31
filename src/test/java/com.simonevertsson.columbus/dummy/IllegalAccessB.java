package com.simonevertsson.columbus.dummy;

public class IllegalAccessB {

  private String fieldC;

  private final int fieldD;

  public IllegalAccessB(String c, int d) {
    this.fieldC = c;
    this.fieldD = d;
  }

  public String getFieldC() {
    return fieldC;
  }

  public int getFieldD() {
    return fieldD;
  }
}
