package com.tout.reptile;

import com.tout.reptile.util.FileUtils;
import com.tout.reptile.util.RegexUtils;
import com.tout.reptile.util.RequestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SpringBootApplication
public class ReptileApplication implements CommandLineRunner {

  @Value("${file.picture}")
  private String dir;


  public static void main(String[] args) {
    SpringApplication.run(ReptileApplication.class, args);
  }


  @Override
  public void run(String... strings) throws Exception {
    //获取地址
    String url = judgeUrl();
    //创建文件夹
    String folder = createFolder(url);
    while (!"".equals(folder)) {
      //获取页码存储图片
      page(url, folder);
    }

  }

  private void page(String url, String folder) {
    System.out.println("开始页码：");
    int start = new Scanner(System.in).nextInt();
    System.out.println("结束页码：");
    int end = new Scanner(System.in).nextInt();
    while (start <= end) {
      //获取图片
      fetch_pictures(url, folder, start);
      //遍历存储图片
      start++;
      try {
        //设置随机延迟时间，防止频繁的爬取导致百度封锁ID
        Thread.sleep(new Random().nextInt(3) * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.exit(0);

  }

  private void fetch_pictures(String url, String folder, int start) {
    //添加url页码参数
    String context = RequestUtils.getReturnString(url + "?pn=" + start);
    String match = "class=\"BDE_Image\" (?:[a-zA_Z0-9=\"^>]+ )*src=\"(.*?)\" ";
    List<String> regexPicUrlList = RegexUtils.getMatches(match, context);
    List<String> pictureUrlList = RegexUtils.getReallyUrl(regexPicUrlList);
    if (pictureUrlList == null || pictureUrlList.size() == 0) {
      System.out.println("第" + start + "页没有发现图片!");
    } else {
      System.out.println("正在下载第" + start + "页的图片。");
    }
    String pictureName;
    if (pictureUrlList != null) {
      for (int i = 0; i < pictureUrlList.size(); i++) {
        pictureName = start + "__" + (i + 1) + ".jpg";
        RequestUtils.saveToFile(pictureUrlList.get(i), folder, pictureName);
      }
    }
  }

  private String createFolder(String url) {

    String context = RequestUtils.getReturnString(url);
    String title = RegexUtils.getTiebaTitle(context);
    String tag = "<.*?>";
    title = RegexUtils.getReplaceAll(title, tag, "").replace("_百度贴吧", "");
    if (!"".equals(title)) {
      String location = "E:\\reptile" + File.separator + title + File.separator;
      if (!FileUtils.createFolder(location)) {
        System.err.println("文件夹创建失败");
        return "";
      }
      return location;
    } else {
      System.out.println("找不到标题，请检查地址！");
      return "";
    }

  }

  private String judgeUrl() {

    String url = "";
    boolean judge = false;
    while (!judge) {
      System.out.println("请输入要获取图片的贴吧地址：");
      //https://tieba.baidu.com/p/4731418202
      Scanner scanner = new Scanner(System.in);
      url = scanner.nextLine().trim();
      String url1 = url.substring(0, 26);
      String url2 = "https://tieba.baidu.com/p/";
      judge = url.length() >= 36 && url1.equals(url2);
      if (!judge) {
        System.out.println("输入地址有误，请重新输入！");
      }
      url = url.substring(0, 36);   //只截取“http://tieba.baidu.com/p/数字 ”部分
    }
    return url;
  }
}
