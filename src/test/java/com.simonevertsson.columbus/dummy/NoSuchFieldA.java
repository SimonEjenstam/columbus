package com.simonevertsson.columbus.dummy;

import com.simonevertsson.columbus.Mapping;

public class NoSuchFieldA {

  @Mapping(clazz = NoSuchFieldB.class, field = "fieldE") // Reference an unknown field (skips the mapping)
  public String fieldA;

  @Mapping(clazz = NoSuchFieldB.class, field = "fieldD") // Maps correctly
  public int fieldB;

}
