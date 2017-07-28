package com.tout.reptile.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 *
 * @author Tout
 */
public class RegexUtils {


  private RegexUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }


  /**
   * 获取贴吧标题
   *
   * @param input 要匹配的字符串
   * @return 正则匹配的部分
   */
  public static String getTiebaTitle(String input) {
//    String regex = "<title>(.*?)_百度贴吧</title>";
//    return Pattern.matches(regex, context);
    Pattern p = Pattern.compile("<title>(.*?)_百度贴吧</title>", Pattern.CASE_INSENSITIVE);
    Matcher matcher = p.matcher(input);
    if (matcher.find()) {
      return matcher.group();
    } else return "";
  }

  /**
   * 获取正则匹配的部分
   *
   * @param regex 正则表达式
   * @param input 要匹配的字符串
   * @return 正则匹配的部分
   */
  public static List<String> getMatches(String regex, CharSequence input) {
    if (input == null) return null;
    List<String> matches = new ArrayList<>();
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(input);
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches;
  }

  /**
   * 替换所有正则匹配的部分
   *
   * @param input       要替换的字符串
   * @param regex       正则表达式
   * @param replacement 代替者
   * @return 替换所有正则匹配的部分
   */
  public static String getReplaceAll(String input, String regex, String replacement) {
    if (input == null) return null;
    return Pattern.compile(regex).matcher(input).replaceAll(replacement);
  }


  /**
   * 获取真实图片地址
   *
   * @param list 正则后的图片集合
   * @return 替换后的图片集合
   */
  public static List<String> getReallyUrl(List<String> list) {
    String regex = "class.*?src=";
    List<String> urlList = new ArrayList<>();
    for (String s : list) {
      s = getReplaceAll(s, regex, "").replace("\"", "");
      urlList.add(s);
    }
    return urlList;
  }


}
