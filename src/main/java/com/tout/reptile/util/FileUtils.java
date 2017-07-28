package com.tout.reptile.util;


import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class FileUtils {

  /**
   * 创建文件夹
   *
   */
  public static boolean createFolder(String filePath) {
    if (StringUtils.isEmpty(filePath)) {
      return false;
    }
    File folder = new File(filePath);
    return folder.exists() || folder.mkdirs();
  }
}
