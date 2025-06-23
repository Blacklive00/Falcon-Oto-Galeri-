package com.alemdar_energy.backend.util;

import java.util.UUID;

public class TokenUtil {
  public static String generateResetToken(){
    return UUID.randomUUID().toString();
  }
}
