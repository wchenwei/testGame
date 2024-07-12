/**  
 * Project Name:SLG_Game.  
 * File Name:PubFunc.java  
 * Package Name:com.hm.util  
 * Date:2017年9月20日下午5:45:30  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.util;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**  
 * 通用函数
 * ClassName:PubFunc <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年9月20日 下午5:45:30 <br/>  
 * @author   zigm  
 * @version  1.1  
 * @since    
 */
public class PubFunc {

    static private String configfile = "Config";
    static private String endline = "------分隔符------";
    static public boolean debug = false;  //是否打印调试信息

    /**
    *字符串转成整型,非整型字符转为0
    *@param 要转换的字符串
    *@return 整型
    */
    public static int parseInt(String s)
    {
        try{
            if (s == null)
                return 0;
            else
                return Integer.parseInt(String.valueOf(s));
        }
        catch(Exception e){return 0;}

    }
    
    public static long parseLong(String s)
    {
        try{
            if (s == null)
                return 0;
            else
                return Long.parseLong(String.valueOf(s));
        }
        catch(Exception e){return 0;}

    }

    /* 转为字符串对应的boolean "true" or "1"=true;"false" or "0" or other = false */
    public static boolean parseBoolean(String s)
    {
        if (s==null)
            return false;
        if (s.toLowerCase().trim().equals("true") || s.trim().equals("1"))
            return true;
        else
            return false;
    }
    /*字符串转成浮点型,非浮点型字符转为0*/
    public static double parseDouble(Object s)
    {
        try{
            if (s == null || ((String)s).trim().equals(""))
                return 0;
            else
                return Double.parseDouble(String.valueOf(s));
        }
        catch(Exception e){return 0;}

    }
    /*日期格式化为标准格式*/
    public static String formatDateTime(String dt)
    {
      String def = "1970/01/01 00:00:00";
      if (dt == null || dt.equals("") || dt.length() < 10)
        return def;
      if (dt.length() > 9) {
        int year = getYear(dt);
        int month = getMonth(dt);
        int day = getDay(dt);
        Calendar c = getCal(def);
        c.set(c.YEAR, year);
        c.set(c.MONTH, month-1);
        c.set(c.DAY_OF_MONTH, day);
        if (dt.length() > 12) {
          int hour = getHour(dt);
          c.set(c.HOUR_OF_DAY, hour);
        }
        if (dt.length() > 15) {
          int min = getMinute(dt);
          c.set(c.MINUTE, min);
        }
        if (dt.length() > 18) {
          int s = getSecond(dt);
          c.set(c.SECOND, s);
        }
        return getDate(c);
      }
      else {
          return def;
        }
    }

    /*日期格式化为标准日期格式*/
    public static String formatDate(String dt)
    {
        return formatDateTime(dt).substring(0,10);
    }
    /*日期格式化为标准时间格式*/
    public static String formatTime(String dt)
    {
        return formatDateTime(dt).substring(11,19);
    }
    /*浮点型格式化*/
    public static String formatDouble(double s)
    {
        return formatDouble(s, 2);
    }
    /*浮点型格式化,小数位数为digit*/
    public static String formatDouble(double s, int digit)
    {
/*    	int a=1;
        for (int i=0; i<digit; i++)
        {
            a=a*10;
        }
        return String.valueOf((double)(((long)(s*a))/a));
        */
        NumberFormat f = DecimalFormat.getInstance();
        f.setMaximumFractionDigits(digit);
        f.setGroupingUsed(false);
        return f.format(s);
    }

    /*浮点型格式化,小数位数为digit*/
    public static String formatDouble(Object s, int digit)
    {
        return formatDouble(parseDouble(s), digit);
    }
    /*浮点型格式化,小数位数为2*/
    public static String formatDouble(Object s)
    {
        return formatDouble(s, 2);
    }

    /*浮点型转为两位小数点的字符串*/
    public static String doubleToStr(double f)
    {
        return formatDouble(f); //String.valueOf(parseDouble(Math.round(f*100))/100);
    }

    /*Session字符串转成整型,非整型字符转为0*/
    public static int parseInt(Object s)
    {
        return parseInt(s,0);
    }
    /*Session字符串转成整型,非整型字符转为默认值*/
    public static int parseInt(Object s, int def)
    {
        try{
            if (s == null)
                return def;
            else
                return Integer.parseInt(String.valueOf(s));
        }
        catch(Exception e){return def;}

    }
    /*Object字符串转成字符串*/
    public static String toString(Object s)
    {
        try{
            if (s == null)
                return "";
            else
            	return String.valueOf(s);
        }
        catch(Exception e){return "";}

    }
    /*Object字符串转成字符串,带默认值*/
    public static String toString(Object s, String def)
    {
        try{
            if (s == null)
                return def;
            else
                return (String)s;
        }
        catch(Exception e){return def;}

    }
    public static String[] splitold(String source, String sign)
    {
      Vector vec = new Vector();
      for (int i=0; i<sign.length(); i++)
      {
        Vector veci = new Vector();
        split(veci, source, sign.substring(i, i + 1));
        for (int j=0; j<veci.size(); j++)
          vec.add(veci.elementAt(j));
      }
      String ret[] = new String[vec.size()];
      vec.copyInto(ret);
      vec = null;
      return ret;
    }

    /*分隔字符串 参数说明:source为要分隔的串,sign为分隔符*/
    public static String[] split2(String source, String sign)
    {
        Vector vec = new Vector();
        split(vec, source, sign);
        String ret[] = new String[vec.size()];
        vec.copyInto(ret);
        vec = null;
        return ret;
    }

    /*分隔字符串 参数说明:source为要分隔的串,sign为分隔符*/
    public static String[] split(String source, String sign)
    {
        Vector vec = new Vector();
        split(vec, source, sign);
        String ret[] = new String[vec.size()];
        vec.copyInto(ret);
        vec = null;
        return ret;
    }

    /**
     * 分割字符串 返回分后的vector
     * @param source
     * @param sign
     */
    public static void split(Vector vec, String source, String sign)
    {
        vec.removeAllElements();
        if (source==null || source.length()==0)
            return;
        int splitlen = sign.length();
        int beginidx=0;
        int endidx=0;
        while (endidx < source.length())
        {
            endidx = source.indexOf(sign, beginidx);
            if (endidx == -1)
            {
                String s = source.substring(beginidx);
                if (s.length()>0)
                    vec.addElement(s);
                break;
            }
            else
            {
                if (endidx > 0)
                {
                    String s = source.substring(beginidx,endidx);
                    if (s.length()>0)
                        vec.addElement(s);
                }
            }
            beginidx = endidx + splitlen;
        }
        return;
    }

    /**
     * 分隔字符串
     * @param allStr String
     * @param split char
     * @return Vector
     */
    public static  Vector<String> SplitIntoVector(String allStr, char split)
    {
        Vector<String> aVec=new Vector<String>();
        if (allStr==null || allStr.equals("") || allStr.equals(""+split))//如果是空字符串则直接返回一个包含0个元素的向量
        {
            return aVec;
        }
        int pos = allStr.indexOf(split);
        if (pos == -1)
            aVec.addElement(allStr); //用消息处理器处理接受到的数据
        else
        {
            //分隔处理每条信息
            int len = allStr.length();
            int oldpos = 0;
            while (oldpos < len)
            {
                aVec.addElement(allStr.substring(oldpos, pos));
                oldpos = pos + 1;
                pos = allStr.indexOf(split, oldpos + 1);
                if (pos == -1)
                    pos = len;
            }
        }

        return aVec;
    }


    /*用分隔符合成字符串 参数说明:source为要合成的字串数组,sign为分隔符*/
    public static String unsplit(String[] source, String sign)
    {
        String ret="";
        if (source == null)
            return ret;
        for (int i=0; i<source.length; i++)
        {
            if (i==0)
                ret = source[i];
            else
                ret += sign+source[i];
        }
        return ret;
    }
    /*字符串数组转为字符串*/
    public static String arrayToStr(String[] source)
    {
        return unsplit(source,";");
    }
     /*是否包括索引*/
     public static boolean isChecked(String all,int index)
     {
         String s=String.valueOf(index);
         String s1="{"+s+" ";
         String s2="{"+s+"}";
         String s3=" "+s+" ";
         String s4=" "+s+"}";
         if ((all.indexOf(s1)>=0)||(all.indexOf(s2)>=0)||(all.indexOf(s3)>=0)||(all.indexOf(s4)>=0))
           return true;
         else
           return false;
     }
     /*  是否为数字串*/
     public static boolean isNumeric(String s)
     {
         try
         {
             Integer.parseInt(replace(s,",",""));
             return true;
         }
         catch(Exception e)
         {
             return false;
         }
     }
     /*删除配制项匹配的记录*/
     public static boolean delConfItem(String conffile,String item,String value)
     {
        File f = new File(conffile);
        boolean found = false;
        try
        {
          if (!f.exists()) f.createNewFile();

          deleteFile(conffile+".bak");
          deleteFile(conffile+".tmp");
          FileWriter fw = new FileWriter(conffile+".tmp");
          FileReader cf = new FileReader(conffile);
          BufferedReader is = new BufferedReader(cf);
          String str = is.readLine();
          while (str != null){
              if (str.equals(item+"="+value))
              {
                  found = true;
                str = is.readLine();
                while ((str != null) && (!str.equals(endline)))
                    str = is.readLine();
              }
              else
                fw.write(str+"\r\n");
            if (str != null)
                str = is.readLine();
          }
          is.close();
          cf.close();
          fw.close();
          moveFile(conffile,conffile+".bak");
          moveFile(conffile+".tmp",conffile);
        }
        catch (Exception e)
        {
          System.err.println("不能读或写属性文件: "+conffile+" \n");
          return false;
        }
        return found;
    }
     /*修改配制项匹配的记录*/
     public static boolean updateConfItem(String conffile,String condition,String item,String value)
     {
        if ((condition==null) || (condition.equals("")))
            return writeConf(conffile,item,value);
        value = replace(value,"\r\n","</p><p>");
        File f = new File(conffile);
        boolean found = false;
        try
        {
          if (!f.exists()) f.createNewFile();

          deleteFile(conffile+".bak");
          deleteFile(conffile+".tmp");
          FileWriter fw = new FileWriter(conffile+".tmp");
          FileReader cf = new FileReader(conffile);
          BufferedReader is = new BufferedReader(cf);
          String str = is.readLine();
          while (str != null){
              if (str.equals(condition))
              {
                while ((str != null) && (!str.equals(endline)))
                {
                    if (str.startsWith(item+"=",0))
                    {
                        str=item+"="+value;
                        found=true;
                    }
                    fw.write(str+"\r\n");
                    str = is.readLine();
                }
                if (!found) fw.write(item+"="+value+"\r\n");
                found = true;
                if (str != null) fw.write(str+"\r\n");
              }
              else
                if (str != null) fw.write(str+"\r\n");
            if (str != null)
                str = is.readLine();
          }
          is.close();
          cf.close();
          fw.close();
          moveFile(conffile,conffile+".bak");
          moveFile(conffile+".tmp",conffile);
        }
        catch (Exception e)
        {
          System.err.println("不能读或写属性文件: "+conffile+" \n");
          return false;
        }
        return found;
    }
     /*增加配制项*/
     public static boolean addConfItem(String conffile,String item,String value)
     {
       value = replace(value,"\r\n","</p><p>");
       try
       {
        FileWriter fw = new FileWriter(conffile,true);
        fw.write(item+"=" +  value +"\r\n");
        fw.close();
        return true;
      }
      catch(Exception e)
      {
          return false;
      }
     }
     /*增加分隔行*/
     public static boolean addEndLine(String conffile)
     {
       try
       {
        FileWriter fw = new FileWriter(conffile,true);
        fw.write(endline +"\r\n");
        fw.close();
        return true;
      }
      catch(Exception e)
      {
          return false;
      }
     }
     /*读配制文件中配制项的列表*/
     public static Enumeration getConfList(String conffile,String item)
     {
         Vector ar = new Vector();
         try {
             FileReader cf=new FileReader(conffile);
              BufferedReader is = new BufferedReader(cf);
            String str = is.readLine();
            while (str != null){
                  if (str.startsWith(item+"=",0))
                  {
                      str = str.substring(new String(item+"=").length());
                    ar.addElement(str);
                    str = is.readLine();
                    }
                    else
                    str = is.readLine();
             }
             is.close();
             cf.close();
         }catch (Exception ex)
         {
             if (debug) System.out.println("读文件"+conffile+"出错!"+ex.getMessage());
             return null;
         }
         return ar.elements();

     }
     /*读取文本文件的内容*/
     public static String readFile(String curfile)
     {
        File f = new File(curfile);
        try
        {
          if (!f.exists()) throw new Exception();
          FileReader cf=new FileReader(curfile);
          BufferedReader is = new BufferedReader(cf);
          String filecontent = "";
          String str = is.readLine();
          while (str != null){
              filecontent += str;
            str = is.readLine();
            if (str != null)
                filecontent += "\n";
          }
          is.close();
          cf.close();
          return filecontent;
        }
        catch (Exception e)
        {
          System.err.println("不能读属性文件: "+curfile+" \n"+e.getMessage());
          return "";
        }

     }
     /*读取配制项匹配的记录值*/
     public static String readConfItem(String conffile,String condition,String item)
     {
        if ((condition==null) || (condition.equals("")))
            return readConf(conffile,item);
        String value="";
        File f = new File(conffile);
        boolean found = false;
        try
        {
          if (!f.exists()) return "";
          FileReader cf=new FileReader(conffile);
          BufferedReader is = new BufferedReader(cf);
          String str = is.readLine();
          while (str != null){
              if (str.equals(condition))
              {
                while ((str != null) && (!str.equals(endline)))
                {
                    if (str.startsWith(item+"=",0))
                    {
                        found=true;
                          value = str.substring(new String(item+"=").length());
                        str = null; //找到,结束
                    }
                    else
                        str = is.readLine();
                }
                str = null;   //找到,结束
              }
            if (str != null)
                str = is.readLine();
          }
          is.close();
          cf.close();
        }
        catch (Exception e)
        {
          System.err.println("不能读属性文件: "+conffile+" \n"+e.getMessage());
          return "";
        }
        return replace(value,"</p><p>","\r\n");
    }
    /*读配制文件的项*/
    public static String readConf(String conffile,String item)
    {
        String ret="";
        Properties myProps = new Properties();
        try
        {
          InputStream is = new FileInputStream(conffile);
          myProps.load(is);
          ret=myProps.getProperty(item,"").trim();
          is.close();
        }
        catch (Exception e)
        {
          System.err.println("不能读取属性文件: "+conffile+" \n");
          return "";
        }

        return decodeGB( replace(ret,"</p><p>","\r\n"));

    }
    /*写文件*/
    public static boolean writeFile(String filename, String filecontext)
    {
        File f = new File(filename);
        try
        {
            FileWriter fw;
            if (!f.exists())
            {
                f.createNewFile();
                fw = new FileWriter(f);
            }
            else
            {
                moveFile(filename, filename+".bak");
                fw = new FileWriter(filename);
            }
            fw.write(filecontext);
            fw.close();
            return true;
        }catch(Exception e)
        {
            if (debug) System.err.println("不能写文件: "+filename);
            return false;
        }
    }

    /*写文件,指定编码,如utf-8*/
    public static boolean writeFile(String filename, String filecontext, String encoding)
    {
        File f = new File(filename);
        try
        {
//            FileWriter fw;
            if (!f.exists())
            {
                f.createNewFile();
//                fw = new FileWriter(f);
            }
            else
            {
                moveFile(filename, filename+".bak");
//                fw = new FileWriter(filename);
            }
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(filename), encoding);
            fw.write(filecontext);
            fw.close();
            return true;
        }catch(Exception e)
        {
            if (debug) System.err.println("不能写文件: "+filename);
            return false;
        }
    }

    /*写配制文件的项*/
    public static boolean writeConf(String conffile, String item, String value)
    {
         value = replace(value,"\r\n","</p><p>");
        File f = new File(conffile);
        boolean found = false;
        try
        {
          if (!f.exists()) f.createNewFile();
          deleteFile(conffile+".bak");
          deleteFile(conffile+".tmp");
          FileWriter fw = new FileWriter(conffile+".tmp");
          FileReader cf=new FileReader(conffile);
          BufferedReader is = new BufferedReader(cf);
          String str = is.readLine();
          while (str != null){
              if (str.startsWith(item+"=",0))
              {
                  str=item+"="+value;
                  found = true;
              }
            fw.write(str+"\r\n");
            str = is.readLine();
          }
          if (!found)
          {
              str=item+"="+value;
            fw.write(str+"\r\n");
          }
          is.close();
          cf.close();
          fw.close();
          if (debug)
          {
            System.out.println(item+"="+value);
              System.out.println("rename file "+conffile+" to "+conffile+".bak");
              System.out.println(moveFile(conffile,conffile+".bak"));
              System.out.println("rename file "+conffile+".tmp to "+conffile);
              System.out.println(moveFile(conffile+".tmp",conffile));
          }
        }
        catch (Exception e)
        {
          if (debug) System.err.println("不能读或写属性文件: "+conffile+" \n");
          return false;
        }
        return true;
    }
    /*读config配制文件*/
    public static String readDomainCfg(String confpath, String item)
    {
        String conffile = confpath+"/"+configfile;
        return readConf(conffile,item);
    }
    /*读config配制文件*/
    public static String readDomainCfg(String confpath, String item, String defaultvalue)
    {
        String ret = readDomainCfg(confpath,item);
        if ((ret==null)||(ret.trim().length()==0))
            ret = defaultvalue;
        return ret;
    }
    /*写config配置文件*/
    public static boolean writeDomainCfg(String confpath, String item, String value)
    {
        String conffile = confpath+"/"+configfile;
        return writeConf(conffile,item,value);
    }
    /*读config配制文件*/
    public static String readTemplateCfg(String confpath, String item)
    {
        String conffile = confpath+"/"+configfile;
        return readConf(conffile,item);
    }
    /*读config配制文件*/
    public static String readTemplateCfg(String confpath, String item, String defaultvalue)
    {
        String ret = readTemplateCfg(confpath,item);
        if ((ret==null)||(ret.trim().length()==0))
            ret = defaultvalue;
        return ret;
    }
    /*写config配置文件*/
    public static boolean writeTemplateCfg(String confpath, String item, String value)
    {
        String conffile = confpath+"/"+configfile;
        return writeConf(conffile,item,value);
    }
    /*文件是否存在*/
    public static boolean fileExists(String filename)
    {
        File f=new File(filename);
        return f.exists()&&f.isFile();

    }
    /*目录是否存在*/
    public static boolean folderExists(String pathname)
    {
        File f=new File(pathname);
        return f.exists()&&f.isDirectory();
    }

    /*删除指定的文件*/
    public static boolean deleteFile(String filename)
    {
        File f=new File(filename);
        return f.exists()&&f.isFile()&&f.delete();
    }
    /*删除指定的文件或空目录*/
    public static boolean deleteFileEx(String fileorpath)
    {
        File f=new File(fileorpath);
        return f.exists()&&f.delete();
    }
    /*删除指定的目录及其目录中的文件*/
    public static boolean deletePath(String pathname)
    {
        File f=new File(pathname);
        if (!f.exists() || !f.isDirectory())
            return false;
        String[] listfile=f.list();
        if (listfile != null)
            for (int i=0; i<listfile.length; i++)
            {
                if (!deleteFileEx(pathname+"/"+listfile[i]))
                    return false;
            }
        return f.delete();
    }
    /*删除指定的目录中的文件*/
    public static boolean deletePathFile(String pathname)
    {
        File f=new File(pathname);
        if (!f.exists() || !f.isDirectory())
            return false;
        String[] listfile=f.list();
        if (listfile != null)
            for (int i=0; i<listfile.length; i++)
            {
                if (!deleteFileEx(pathname+"/"+listfile[i]))
                    return false;
            }
        return true;
    }
    /*删除指定的目录（含所有子目录）及其目录中的文件*/
    public static boolean deleteTree(String pathname)
    {
        File f=new File(pathname);
        if (!f.exists() || !f.isDirectory())
            return false;
        String[] listfile=f.list();
        if (listfile != null)
            for (int i=0; i<listfile.length; i++)
            {
                deleteTree(pathname+"/"+listfile[i]);
            }
        return f.delete();
    }
    /*更改文件名或目录名*/
    public static boolean moveFile(String sfilename, String dfilename)
    {
        File fs=new File(sfilename);
        if (!fs.exists()||!fs.isFile())
            return false;
        File fd=new File(dfilename);
        if (fd.exists())
            fd.delete();
        return fs.renameTo(fd);
    }
    /*更改文件名或目录名*/
    public static boolean move(String sfilename, String dfilename)
    {
        File fs=new File(sfilename);
        File fd=new File(dfilename);
        if (!fs.exists())
            return false;
        if (fd.exists())
            fd.delete();
        return fs.renameTo(fd);
    }
    /*创建文件*/
    public static boolean createFile(String filename)
    {
        if (filename==null) return false;
        File f=new File(filename);
        if (f.exists())
            return false;
        try{
            return f.createNewFile();
        }
        catch (Exception e)    {
            if (debug) System.out.println(e.getMessage());
            return false;
        }

    }
    /*创建目录*/
    public static boolean mkdir(String pathname)
    {
        if (pathname==null) return false;
        File f=new File(pathname);
        if (f.exists())
            return false;
        try{
            return f.mkdir();
        }
        catch (Exception e)    {
            if (debug) System.out.println(e.getMessage());
            return false;
        }

    }
    /*创建目录(含不存在的上级及上上级等目录)*/
    public static boolean mkdirs(String pathname)
    {
        if (pathname==null) return false;
        File f=new File(pathname);
        if (f.exists())
            return false;
        try{
            return f.mkdirs();
        }
        catch (Exception e)    {
            if (debug) System.out.println(e.getMessage());
            return false;
        }

    }
    /*复制文件*/
    public static boolean copyFile(String source, String desc)
    {
        try
        {
            File fl = new File(source);
            int length = (int)fl.length();
              FileInputStream is = new FileInputStream(source);
              FileOutputStream os = new FileOutputStream(desc);

            byte[] b = new byte[length];
              is.read(b);
              os.write(b);
              is.close();
              os.close();
              return true;
          }
          catch (Exception e)
          {
            if (debug) System.out.println(e.getMessage());
              return false;
          }
    }

    /*取文件大小*/
    public static int getFileSize(String filename)
    {
        try
        {
            File fl = new File(filename);
            int length = (int)fl.length();
            return length;
          }
          catch (Exception e)
          {
            if (debug) System.out.println(e.getMessage());
              return 0;
          }

    }

    /*取文件全称不含路径的文件名*/
    public static String getFileName(String filePathName)
    {
        if (filePathName==null) return "";
        int pos = 0;
        pos = filePathName.lastIndexOf(47); //  "/"
        if(pos != -1)
            return filePathName.substring(pos + 1, filePathName.length());
        pos = filePathName.lastIndexOf(92); //  "\"
        if(pos != -1)
            return filePathName.substring(pos + 1, filePathName.length());
        else
            return filePathName;
    }
    /*取文件的扩展名*/
    public static String getFileExt(String filePathName)
    {
        if (filePathName==null) return "";
        int pos = 0;
        pos = filePathName.lastIndexOf('.');
        if(pos != -1)
            return filePathName.substring(pos + 1, filePathName.length());
        else
            return "";

    }
    /*取文件的扩展名*/
    public static String setFileExt(String filePathName, String newExt)
    {
        if (filePathName==null) return "";
        int pos = 0;
        pos = filePathName.lastIndexOf('.');
        if(pos != -1)
            return filePathName.substring(0,pos+1)+newExt;
        else
            return "";

    }
    /*取文件全称的路径*/
    public static String getFilePath(String filePathName)
    {
        if (filePathName==null) return "";
        String token = new String();
        String value = new String();
        int pos = 0;
        int i = 0;
        int start = 0;
        int end = 0;
        pos = filePathName.lastIndexOf(47);
        if(pos != -1)
            return filePathName.substring(0,pos);
        pos = filePathName.lastIndexOf(92);
        if(pos != -1)
            return filePathName.substring(0,pos);
        else
            return filePathName;
    }


    /*判断字串desc是否存在于source,存在则返回true,不存在返回false*/
    public static boolean inStr(String source,String desc)
    {
        if (source.indexOf(desc)>=0)
            return true;
        else return false;
    }
    /**
    *判断字串desc是否存在于source数组中,存在则返回true,不存在返回false
    */
    public static boolean inStr(String[] source,String desc)
    {
        return inStr(source, desc, source.length);
    }
    /**
    *判断字串desc是否存在于source数组中,存在则返回true,不存在返回false只比较长度len以内
    */
    public static boolean inStr(String[] source,String desc, int len)
    {
        for (int i=0; i<len&&i<source.length; i++)
        {
            if (desc.equals(source[i]))
                return true;
        }
        return false;
    }
    /*取两整数相除最大值*/
    public static int celling(int a,int b)
    {
        int c = a/b;
        if (a!=b*c) c=c+1;
        return c;
    }
    /*取余数*/
    public static int mod(int a,int b)
    {
        return a%b;
    }
    /*字符串内码转换--用于写入数据库时*/
    public static String encodeGB(String source,String charset)
    {
        if (source == null) return "";
        try
        {
            return (new String(source.getBytes("GB2312"),charset));
        }
        catch(Exception e)
        {
            return source;
        }
    }
    /*字符串内码转换--用于写入数据库时*/
    public static String encodeGB(String source)
    {
        return encodeGB(source, "ISO-8859-1");
    }

    /*字符串内码转换--用于从数据库读取数据时*/
    public static String decodeGB(String source,String charset)
    {
        if (source == null) return "";
        try{
            return (new String(source.getBytes(charset),"GB2312"));
        }catch (Exception E)
        {
              return source;
        }

    }
    /*字符串内码转换--用于从数据库读取数据时*/
    public static String decodeGB(String source)
    {
        return decodeGB(source, "ISO-8859-1");
    }

    /*字符串内码转换--用于从数据库读取数据时*/
    public static String autoDecodeGB(String source)
    {
        if (source == null) source = "";
        String encode = encodeGB(source);
        if (source.equals(decodeGB(encode)))
        {
            return source;
        }
        return decodeGB(source);
    }

    /*URL编码*/
    public static String urlEncode(String source)
    {
        if (source == null) source = "";
        try
        {
            return java.net.URLEncoder.encode(source.trim(), "GB2312");
        }
        catch (UnsupportedEncodingException ex)
        {
            return source;
        }

    }
    /*URL编码*/
    public static String urlEncodeUTF(String source)
    {
        if (source == null) source = "";
        try
        {
            return java.net.URLEncoder.encode(source.trim(), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return source;
        }

    }
    /*URL解码*/
    public static String urlDecode(String source)
    {
        if (source == null) source = "";
        try
        {
            return java.net.URLDecoder.decode(source.trim(), "GB2312");
        }
        catch (UnsupportedEncodingException ex)
        {
            return source;
        }
    }
    /*URL解码*/
    public static String urlDecodeUTF(String source)
    {
        if (source == null) source = "";
        try
        {
            return java.net.URLDecoder.decode(source.trim(), "UTF-8");
        }
        catch (UnsupportedEncodingException ex)
        {
            return source;
        }
    }

    /*转换为HTML格式用来显示,即 '&'转为&amp; ' '转为&nbsp; '"'转为&quot; '<'转为&lt; '>'转为&gt;*/
    //\n=><br>
    public static String toHtmlStr(String s)
    {
        if ( s == null)
            return ""; //原为"&nbsp"
        else
            return replace(replace(replace(replace(replace(replace(s,'&',"&amp;"),' ',"&nbsp;"),'"',"&quot;"),'<',"&lt;"),'>',"&gt;"),'\n',"<br/>");
    }
    /*转换为HTML格式用来显示,即 '&'转为&amp; ' '转为&nbsp; '"'转为&quot; '<'转为&lt; '>'转为&gt;
    \n不转为<br>*/
    public static String toHtmlMemo(String s)
    {
        if ( s == null)
            return ""; //原为"&nbsp"
        else
            return replace(replace(replace(replace(replace(s,'&',"&amp;"),' ',"&nbsp;"),'"',"&quot;"),'<',"&lt;"),'>',"&gt;");
    }
    /*转换为HTML格式用来显示,即 '&'转为&amp; ' '转为&nbsp; '"'转为&quot; '<'转为&lt; '>'转为&gt;*/
    public static String toHtmlStr(Object ob)
    {
        String s = (String)ob;
        if ( s == null)
            return ""; //原为"&nbsp"
        else
            return toHtmlStr(s);
    }

    /*空或空格转换为HTML格式用来显示,*/
    public static String toHtmlNull(Object ob)
    {
        String s = (String)ob;
        if ( s == null || s.trim().equals(""))
            return "&nbsp;";
        else
            return s;
    }

    /*转换html中的script代码,不让其执行*/
    public static String toHtmlScript(String s)
    {
        if ( s == null)
            return "";
        else
        {
            s = replace(s,"</script>","&lt;/script&gt;");
            s = replace(s,"<script","&lt;script");
            s = replace(s,"\n","<br>");
            return s;
        }
    }

    /*字符串转换,即NULL转为"",非空则不变*/
    public static String nullToStr(String s)
    {
        if ( s == null || s.trim().length()<0)
            return "";
        else
            return s;
    }

    /*替换source中的str1为str2*/
    public static String replace(String source,char str1,String str2)
    {
        return replace(source,String.valueOf(str1),str2);
    }
    /*替换source中的str1为str2*/
    public static String replace(String source,String str1,String str2)
    {
        if (source==null)
            return "";
        int posend = source.indexOf(str1,0);
        if (posend<0) //change by 2011-05-17 by sj 优化
        	return source;
        StringBuffer desc = new StringBuffer(source.length()+str2.length()); //change by 2011-05-17 by sj 优化
        int posstart = 0;
        int len = source.length();
        while (posend>=0 && posstart<len)
        {
            desc.append(source.substring(posstart,posend));
            desc.append(str2);
            posstart = posend+str1.length();
            posend = source.indexOf(str1,posstart);
        }
        if (posstart < len)
            desc.append(source.substring(posstart,len));
        return desc.toString();
    }
    /*转换字符串用于SQL串中（把'=>''）*/
    public static String toSqlStr(String source)
    {
        if (source == null) return "";
        return replace(source,'\'',"\'\'");
    }


    /*转换字符串用于SQL串中（把'=>"）*/
    public static String toSqlStr(String source,int flag)
    {
        source = toSqlStr(source);
        source = "'"+source+"'";
        if (flag == 0)
            return ","+source;
        else
            return source;

    }
        /*判断字符串是否为空*/
    public static boolean isNullStr(String s)
    {
        if (s==null || s.trim().length()<=0)
            return true;
        else return false;
    }

        /*判断字符串数组是否为空*/
    public static boolean isNullStr(String[] s)
    {
        if ((s==null) || (s.length<=0))
            return true;
        else return false;
    }

    /*按字段的字段查询值加条件（加LIKE）*/
    public static String strLike(String fieldValue,String field)
    {
        if (isNullStr(fieldValue))
            return " ";
        else
            return " and "+field+" like '%"+toSqlStr(fieldValue.trim())+"%' ";
    }
    /*按字段的字段查询值加条件（加LIKE）*/
    public static String strOrLike(String fieldValue,String field)
    {
        if (isNullStr(fieldValue))
            return " ";
        else
            return " or "+field+" like '%"+toSqlStr(fieldValue.trim())+"%' or '"+toSqlStr(fieldValue.trim())+"' like '%'+"+field+"+'%' ";
    }

    /*按长度把字符串前补0*/
    public static String strLen(String s, int len)
    {
        if (isNullStr(s))
            s="";
		int slen = s.length();
        for (int i=0;i<len-slen;i++)
        {
            s = "0"+s;
        }
        return s;
    }



    /*按长度把字符串后补0*/
    public static String strLen2(String s, int len)
    {
        if (isNullStr(s))
            s="";
		int slen = s.length();
        for (int i=0;i<len-slen;i++)
        {
            s = s+"0";
        }
        return s;
    }
    /*取查询选择项*/
    public static String getInputParam(String[] query_type)
    {
        String s = "";
        if (query_type != null)
          for (int i=0;i<query_type.length;i++)
                s = s+"<input type='hidden' name='query_type' value='"+query_type[i]+"'>";
        return s;
    }
    /*取查询选择项*/
    public static String getInputField(String inputfield,String inputvalue)
    {
        if (inputvalue != null)
            return "<input type='hidden' name='"+inputfield+"'"+" value='"+inputvalue+"'>";
        else return "<input type='hidden' name='"+inputfield+"'>";
    }
    /*取字符串字段的字段数*/
    public static int getFieldCount(String fields)
    {
        if (fields==null || fields.trim().equals("")) return 0;
        int i=0,count=0;
        while (fields.indexOf(",",i) != -1)
        {
            count++;
            i = fields.indexOf(",",i)+1;
        }
        return count;
    }

    /*取日期字符串的时间的整型值*/
    public static long getTime(Object time)
    {
        return getCal(toString(time)).getTime().getTime();
    }
    /*返回统一格式的日期*/
    public static String getLegalDate(String year,String month,String day,String hour,String minute,String secound)
    {
        int iyear = parseInt(year);
        int imonth = parseInt(month)-1;
        int iday = parseInt(day);
        int ihour = parseInt(hour);
        int iminute = parseInt(minute);
        int isecound = parseInt(secound);
        if (iyear<1700)  iyear = 1700;
        if (iday < 1) iday = 1;
        Calendar cal = Calendar.getInstance();
        cal.set(iyear,imonth,iday,ihour,iminute,isecound);
        return getDate(cal);
    }
    /*返回统一格式的日期*/
    public static String getLegalDate(String year, String month, String day)
    {
        return getLegalDate(year,month,day,"0","0","0");
    }

    /*返回日历的年字符串*/
    public static String getYear(Calendar cal)
    {
        return String.valueOf(cal.get(cal.YEAR));
    }
    /*返回日历的月字符串(两位)*/
    public static String getMonth(Calendar cal)
    {
        return strLen(String.valueOf(cal.get(cal.MONTH)+1),2);
    }
    /*返回日历的日字符串(两位)*/
    public static String getDay(Calendar cal)
    {
        return strLen(String.valueOf(cal.get(cal.DAY_OF_MONTH)),2);
    }
    /*返回日历的时字符串(两位)*/
    public static String getHour(Calendar cal)
    {
        return strLen(String.valueOf(cal.get(cal.HOUR_OF_DAY)),2);
    }
    /*返回日历的分字符串(两位)*/
    public static String getMinute(Calendar cal)
    {
        return strLen(String.valueOf(cal.get(cal.MINUTE)),2);
    }
    /*返回日历的秒字符串(两位)*/
    public static String getSecond(Calendar cal)
    {
        return strLen(String.valueOf(cal.get(cal.SECOND)),2);
    }

    /*返回日历的日期字符串（格式:"yyyy/mm/dd"）*/
    public static String getDateStr(Calendar cal)
    {
      return getYear(cal)+"/"+getMonth(cal)+"/"+getDay(cal);
    }
    /*返回日历的日期字符串（格式:"yyyy/mm/dd"）*/
    public static String getDateStr(String date)
    {
      return formatDate(date);
    }
    /*返回日历的时间字符串（格式:"hh:ss:mm"）*/
    public static String getTimeStr(Calendar cal)
    {
      return getHour(cal)+":"+getMinute(cal)+":"+getSecond(cal);
    }
    /*返回日历的时间字符串（格式:"hh:ss:mm"）*/
    public static String getTimeStr(String date)
    {
        return formatTime(date);
    }
    /*返回日历的日期时间字符串（格式:"yyyy/mm/dd hh:ss:mm"）*/
    public static String getDate(Calendar cal)
    {
        return getDateStr(cal)+" "+getTimeStr(cal);
    }
    /*返回日历的日期时间字符串（格式:"yyyy/mm/dd hh:ss:mm"）*/
    public static String getDate(String date)
    {
        return formatDateTime(date);
    }
    /*返回日历的日期时间字符串（格式:"yyyy/mm/dd hh:ss:mm"）*/
    public static String getDate(Date dat)
    {
        if (dat==null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        String time = getTimeStr(cal);
        if ("00:00:00".equals(time))
            return getDateStr(cal);
        else
            return getDateStr(cal)+" "+time;
    }
    /*返回日历的日期字符串（格式:"yyyy/mm/dd"）*/
    public static String getDateStr(Date dat)
    {
        if (dat==null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        return getDateStr(cal);
    }
    /*返回日期字符串("yyyy/mm/dd")的年月*/
    public static String getYearMonth(String s)
    {
        try
        {
            return s.substring(0,4)+s.substring(5,7);
        }
        catch(Exception e)
        {
            return "197001";
        }
    }
    /**
    *返回年月的第一天
    *@param ym 年月 格式:yyyymm
    *@return 日期的第一天格式:yyyy/mm/dd
    */
    public static String getFirstDayForYearMonth(String ym)
    {
        try
        {
            return ym.substring(0,4)+"/"+ym.substring(4,6)+"/01";
        }
        catch(Exception e)
        {
            return "1970/01/01";
        }
    }
    /**
    *返回年月的最后一天
    *@param ym 年月 格式:yyyymm
    *@return 日期的最后一天格式:yyyy/mm/dd
    */
    public static String getLastDayForYearMonth(String ym)
    {
        try
        {
            return getLastDay(getFirstDayForYearMonth(ym));
        }
        catch(Exception e)
        {
            return "1970/01/01";
        }
    }
    /**
    *返回日期的最后一天
    *@param FormatDate 日期 格式:yyyy/mm/dd
    *@return 日期的最后一天格式:yyyy/mm/dd
    */
    public static String getLastDay(String FormatDate)
    {
        try
        {

            int maxDay=28;
            int intMonth = parseInt(FormatDate.substring(5,7));
            int intYear = parseInt(FormatDate.substring(0,4));
            switch(intMonth){
                case 1: maxDay = 31;break;
                case 3: maxDay = 31;break;
                case 5: maxDay = 31;break;
                case 7: maxDay = 31;break;
                case 8: maxDay = 31;break;
                case 10: maxDay = 31;break;
                case 12: maxDay = 31;break;
                case 4: maxDay = 30;break;
                case 6: maxDay = 30;break;
                case 9: maxDay = 30;break;
                case 11: maxDay = 30;break;
                case 2:maxDay = 28;
                    if(intYear % 4 == 0){
                        maxDay = 29;
                        if(intYear%100 ==0){
                            if(intYear % 400!=0){maxDay = 28;}
                        }
                    }
                    break;
            }
            return FormatDate.substring(0,4)+"/"+FormatDate.substring(5,7)+"/"+String.valueOf(maxDay);
        }
        catch(Exception e)
        {
            return "1970/01/01";
        }
    }
    /*返回日期字符串("yyyy/mm/dd hh:ss:mm")的年*/
    public static int getYear(String s)
    {
        try
        {
            return Integer.parseInt(s.substring(0,4));
        }
        catch(Exception e)
        {
            return 1970;
        }
    }
    /*返回日期字符串("yyyy/mm/dd hh:ss:mm")的月*/
    public static int getMonth(String s)
    {
        try
        {
            return Integer.parseInt(s.substring(5,7));
        }
        catch(Exception e)
        {
            return 1;
        }
    }
    /*返回日期字符串("yyyy/mm/dd hh:ss:mm")的日*/
    public static int getDay(String s)
    {
        try
        {
            return Integer.parseInt(s.substring(8,10));
        }
        catch(Exception e)
        {
            return 1;
        }
    }
    /*返回日期字符串("yyyy/mm/dd hh:ss:mm")的时*/
    public static int getHour(String s)
    {
        try
        {
            return Integer.parseInt(s.substring(11,13));
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    /*返回日期字符串("yyyy/mm/dd hh:ss:mm")的分*/
    public static int getMinute(String s)
    {
        try
        {
            return Integer.parseInt(s.substring(14,16));
        }
        catch(Exception e)
        {
            return 0;
        }
    }
    /*返回日期字符串("yyyy/mm/dd hh:ss:mm")的秒*/
    public static int getSecond(String s)
    {
        try
        {
            return Integer.parseInt(s.substring(17,19));
        }
        catch(Exception e)
        {
            return 0;
        }
    }

    /*返回日期时间字符串对应的日历（格式:"yyyy/mm/dd hh:ss:mm"）*/
    public static Calendar getCal(String s)
    {
        Calendar cal=Calendar.getInstance();
        cal.set(getYear(s),getMonth(s)-1,getDay(s),getHour(s),getMinute(s),getSecond(s));
        return cal;
    }
    /*返回日期时间字符串对应的日历（格式:"yyyymmdd"）*/
    public static Calendar getCal2(String s)
    {
        Calendar cal=Calendar.getInstance();
        cal.set(parseInt(s.substring(0,4)),parseInt(s.substring(4,6))-1,parseInt(s.substring(6,8)));
        return cal;
    }
    /*返回日期时间字符串对应的SQL日期（格式:"yyyy/mm/dd hh:ss:mm"）*/
    public static java.sql.Date getSqlDate(String s)
    {
        return new java.sql.Date(PubFunc.getCal(s).getTimeInMillis());
    }
    /*返回当天日期对应的SQL日期（）*/
    public static java.sql.Date getSqlDate()
    {
        return getSqlDate(getNowDate());
    }
    /*取当前日期时间的字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNow()
    {
        Calendar now=Calendar.getInstance();
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取当前日期的字符串,格式为"yyyy/mm/dd"*/
    public static String getNowDate()
    {
        Calendar now=Calendar.getInstance();
         return getDateStr(now);
    }
    //add by syl 2014-03-04
    /*取当前日期的字符串,格式为"yyyy/mm/dd"*/ 
    public static String getBeforeDate()
    {
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,   -1);
        return getDateStr(cal);
    }
    /*取当前时间的字符串,格式为"hh:ss:mm"*/
    public static String getNowTime()
    {
        Calendar now=Calendar.getInstance();
          return getTimeStr(now);
    }
    /*取下一小时字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextHour()
    {
        Calendar now=Calendar.getInstance();
        now.set(now.HOUR_OF_DAY, now.get(now.HOUR_OF_DAY)+1);
          return getDateStr(now)+" "+getTimeStr(now);

    }
    /*取日期的下一小时字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextHour(String date)
    {
        Calendar now = getCal(date);
        now.set(now.HOUR_OF_DAY, now.get(now.HOUR_OF_DAY)+1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取上一小时字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeHour()
    {
        Calendar now = Calendar.getInstance();
        now.set(now.HOUR_OF_DAY, now.get(now.HOUR_OF_DAY)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的上一小时字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeHour(String date)
    {
        Calendar now = getCal(date);
        now.set(now.HOUR_OF_DAY, now.get(now.HOUR_OF_DAY)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取明天字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextDay()
    {
        Calendar now=Calendar.getInstance();
        now.set(now.DATE, now.get(now.DATE)+1);
          return getDateStr(now)+" "+getTimeStr(now);

    }
    /*取日期的第二天字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextDay(String date)
    {
        Calendar now = getCal(date);
        now.set(now.DATE, now.get(now.DATE)+1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取上一天字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeDay()
    {
        Calendar now = Calendar.getInstance();
        now.set(now.DATE, now.get(now.DATE)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的上一天字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeDay(String date)
    {
        Calendar now = getCal(date);
        now.set(now.DATE, now.get(now.DATE)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取本周的第一天*/
    public static String getThisWeek()
    {
        Calendar now=Calendar.getInstance();
        now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取本月的第一天*/
    public static String getThisMonth()
    {
        Calendar now=Calendar.getInstance();
        now.set(now.DATE, 1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的本年的第一天*/
    public static String getThisYear(String date)
    {
        Calendar now=getCal(date);
        now.set(now.DATE, 1);
        now.set(now.MONTH, 0);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的本月的第一天*/
    public static String getThisMonth(String date)
    {
        Calendar now=getCal(date);
        now.set(now.DATE, 1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的本周的第一天*/
    public static String getThisWeek(String date)
    {
        Calendar now=getCal(date);
        now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期是本周的第几天,周一为第一天,周日为第0天*/
    public static int getDayOfWeek(String date)
    {
        Calendar now=getCal(date);
        return now.get(now.DAY_OF_WEEK)-1;
    }
    /*取日期是本年的第几周*/
    public static int getWeekOfYear(String date)
    {
        Calendar now=getCal(date);
        return now.get(now.WEEK_OF_YEAR);
    }
    /*取日期是本月的第几天*/
    public static int getDayOfMonth(String date)
    {
        Calendar now=getCal(date);
        int day = now.get(now.DAY_OF_MONTH);
        return day;
    }
    /*取日期是本旬的第几天*/
    public static int getDayOfMonth3(String date)
    {
      Calendar now=getCal(date);
      int day = getDayOfMonth(date);    //取日期是本月的第几天
      if (day==31)
        return 11;
      else
      if (day==10 || day==20 || day==30)
        return 10;
      else
        return day%10;
    }
    /*取月的最后一天*/
    public static String getMonthLastDay(String date)
    {
        Calendar now=getCal(date);
        now.set(now.DAY_OF_MONTH, 1);
        now.set(now.MONTH, now.get(now.MONTH)+1);
        now.set(now.DAY_OF_YEAR, now.get(now.DAY_OF_YEAR)-1);
        return PubFunc.getDateStr(now);
    }
    /*取旬的最后一天*/
    public static String getMonth3LastDay(String date)
    {
        Calendar now=getCal(date);
        int day = getDayOfMonth(date);
        if (day <= 10)
          now.set(now.DAY_OF_MONTH, 10);
        else
        if (day <= 20)
          now.set(now.DAY_OF_MONTH, 20);
        else
          return getMonthLastDay(date);
        return PubFunc.getDateStr(now);
    }
    /*判断日期是否为月未*/
    public static boolean isMonthLastDay(String date)
    {
        String lastDay = getMonthLastDay(date);
        return (lastDay.equals(PubFunc.getDateStr(date)));
    }
    /*判断日期是否为旬未*/
    public static boolean isMonth3LastDay(String date)
    {
        String lastDay = getMonth3LastDay(date);
        return (lastDay.equals(PubFunc.getDateStr(date)));
    }
    /*取下周的第一天*/
    public static String getNextWeek()
    {
        Calendar now=Calendar.getInstance();
        now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1+7);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的下周的第一天*/
    public static String getNextWeek(String date)
    {
        Calendar now=getCal(date);
        now.set(now.DATE, now.get(now.DATE)-now.get(now.DAY_OF_WEEK)+1+7);
          return getDateStr(now)+" "+getTimeStr(now);
    }

    /*取下一月字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextMonth()
    {
        Calendar now=Calendar.getInstance();
        now.set(now.MONTH, now.get(now.MONTH)+1);
          return getDateStr(now)+" "+getTimeStr(now);

    }
    /*取日期的下一月字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextMonth(String date)
    {
        Calendar now = getCal(date);
        now.set(now.MONTH, now.get(now.MONTH)+1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取上一月字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeMonth()
    {
        Calendar now = Calendar.getInstance();
        now.set(now.MONTH, now.get(now.MONTH)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的上一月字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeMonth(String date)
    {
        Calendar now = getCal(date);
        now.set(now.MONTH, now.get(now.MONTH)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取下一年字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextYear()
    {
        Calendar now=Calendar.getInstance();
        now.set(now.YEAR, now.get(now.YEAR)+1);
          return getDateStr(now)+" "+getTimeStr(now);

    }
    /*取日期的下一年字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getNextYear(String date)
    {
        Calendar now = getCal(date);
        now.set(now.YEAR, now.get(now.YEAR)+1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取上一年字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeYear()
    {
        Calendar now = Calendar.getInstance();
        now.set(now.YEAR, now.get(now.YEAR)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取日期的上一年字符串,格式为"yyyy/mm/dd hh:ss:mm"*/
    public static String getBeforeYear(String date)
    {
        Calendar now = getCal(date);
        now.set(now.YEAR, now.get(now.YEAR)-1);
          return getDateStr(now)+" "+getTimeStr(now);
    }
    /*取当前日期的前几天日期*/
    public static String getBeforeDay(int day)
    {
        Calendar now = Calendar.getInstance();
        now.set(now.DATE, now.get(now.DATE)-day);
        return getDate(now);
    }
    /*本地字符转为unicode*/
    public static String native2Unicode(String s)
    {
        if (s == null || s.length() == 0) {
           return null;
           }
        byte[] buffer = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
           buffer[i] = (byte)s.charAt(i);
        }
        return new String(buffer);
    }
    /*unicode字符转为本地*/
    public static String unicode2Native(String s)
    {
       if (s == null || s.length() == 0) {
          return null;
       }
       char[] buffer = new char[s.length() * 2];
       char c;
       int j = 0;
       for (int i = 0; i < s.length(); i++) {
          if (s.charAt(i) >= 0x100) {
             c = s.charAt(i);
             byte []buf = (""+c).getBytes();
             buffer[j++]  = (char)buf[0];
             buffer[j++]  = (char)buf[1];
          }
          else {
             buffer[j++] = s.charAt(i);
          }
       }
       return new String(buffer, 0, j);
    }
    /*六位上一月    yyyymm*/
    public static  String beforeMonth(String yearmonth)
    {
        if (yearmonth.length()==6)
        {
            String s = yearmonth.substring(0,4)+"/"+yearmonth.substring(4,6)+"/01";
            s = PubFunc.getBeforeMonth(s);
            return s.substring(0,4)+s.substring(5,7);
        }
        else
            return yearmonth;
    }
    /*六位下一月    yyyymm*/
    public static  String nextMonth(String yearmonth)
    {
        if (yearmonth.length()==6)
        {
            String s = yearmonth.substring(0,4)+"/"+yearmonth.substring(4,6)+"/01";
            s = PubFunc.getNextMonth(s);
            return s.substring(0,4)+s.substring(5,7);
        }
        else
            return yearmonth;
    }
    /*输出中国风格的日期*/
    public static String getChnDate(String computerDate)
    {
        String out= String.valueOf(getYear(computerDate))+"年"+String.valueOf(getMonth(computerDate))+"月"+String.valueOf(getDay(computerDate))+"日";
        return out;
    }
    /*取中文当前星期*/
    public String getChinaWeek()
    {
        int i= Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (i)
        {
            case 1: return "日";
            case 2: return "一";
            case 3: return "二";
            case 4: return "三";
            case 5: return "四";
            case 6: return "五";
            case 7: return "六";
            default: return "日";
        }
    }
    /*取数字对应的中文*/
    public static String getChinaNumber(int i)
    {
        switch(i)
        {
            case 1: return "一";
            case 2: return "二";
            case 3: return "三";
            case 4: return "四";
            case 5: return "五";
            case 6: return "六";
            case 7: return "七";
            case 8: return "八";
            case 9: return "九";
            case 10: return "十";
            default: return " ";
        }
    }
    /*取日期的日*/
    public int getDay()
    {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
    /*取当前月*/
    public int getYear()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
    /*取当前月*/
    public int getMonth()
    {
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }

    /**
     * 转换为XML格式用来显示的特殊转义字符,即 '&'转为&amp; '<'转为&lt; '>'转为&gt; // ' '转为&nbsp; '"'转为&quot; "'"转为&apos;
     */
    public static String toXMLStr(String s)
    {
        if ( s == null)
            return ""; //原为"&nbsp"
        String [][] toall = {{"&","&amp;"},{"<","&lt;"},{">","&gt;"},{"'","&apos;"},{"\"","&quot;"}};
        for (int i=0; i<toall.length; i++)
            s = PubFunc.replace(s,toall[i][0],toall[i][1]);
        return s;
    }

    /**
     *
     * @param phone String
     * @return boolean
     */
    public static boolean isPhoneNo(String phone){
    	if (phone==null)
    		return false;
    	if (phone.startsWith("86"))
    		phone = phone.substring(2);
    	if (phone.startsWith("+86"))
    		phone = phone.substring(3);
    	if (phone.length()!=11)
    		return false;
    	try
    	{
    		return Pattern.matches("(13|15|18)\\d{9}",phone);
    	}catch(Exception e)
    	{
    		return false;
    	}
    }
    /**
     * 是否是中国电信电话号码
     * add by lijy 2011-03-11
     * @param phone String
     * @return boolean
     */
    public static boolean isCTPhoneNo(String phone){
        if (phone == null)
            return false;
        if (phone.startsWith("86"))
            phone = phone.substring(2);
        if (phone.startsWith("+86"))
            phone = phone.substring(3);
        if (phone.length() != 11)
            return false;
        try {
            return Pattern.matches("(133|153|189|180)\\d{8}",
                    phone);
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 字符串转换为日期
     *       yyyy-MM-dd 
     * @param dateStr
     */
    public static Date formatDateFromString(String dateStr) {
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    	try {
			Date date = df.parse(dateStr); 
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
    }
    /**
     * add by syl 2013-12-26
     * 获取昨天日期的字符串形式  2013-01-01  返回 20130101
     * @return 
     */
    public static String getYesterdayDate() {
    	Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.DATE,   -1);
    	String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
    	return yesterday;
    }
    /**
     * add by syl 2013-12-26
     * 获取今天日期的字符串形式  2013-01-01  返回 20130101
     * @return
     */
    public static String getToday() {
    	Calendar cal = Calendar.getInstance();
    	String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
    	return yesterday;
    }
    /**
     * add by syl 2013-12-26
     * double四舍五入转为为int
     * @param dSource
     * @return
     */
    public static int getRound(double dSource){
	    double iRound;
	    BigDecimal deSource = new BigDecimal(dSource);
	    iRound= deSource.setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();
	    return (int)iRound;
	}
    
    /** 
     * add by syl 2014-03-10
     * 计算出两个日期之间相差的天数 
     * 建议date1 大于 date2 这样计算的值为正数 
     * @param date1 日期1 
     * @param date2 日期2 
     * @return date1 - date2 
     */ 
    public static int dateInterval(long date1, long date2) {
        if(date2 > date1){
            date2 = date2 + date1;
            date1 = date2 - date1;
            date2 = date2 - date1;
        }
     
         // Canlendar 该类是一个抽象类  
         // 提供了丰富的日历字段 
         // 本程序中使用到了 
         // Calendar.YEAR    日期中的年份 
         // Calendar.DAY_OF_YEAR     当前年中的天数 
         // getActualMaximum(Calendar.DAY_OF_YEAR) 返回今年是 365 天还是366天 
        Calendar calendar1 = Calendar.getInstance(); // 获得一个日历  
        calendar1.setTimeInMillis(date1); // 用给定的 long 值设置此 Calendar 的当前时间值。  
           
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        // 先判断是否同年  
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
           
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if(y1 - y2 > 0){
            day = numerical(maxDays, d1, d2, y1, y2, calendar2);
        }else{
            day = d1 - d2;
        }
        return day;
    }  
       
    /** 
     * add by syl 2014-03-10
     * 日期间隔计算 
     * 计算公式(示例): 
     *      20121201- 20121212
     *      取出20121201这一年过了多少天 d1 = 天数  取出20121212这一年过了多少天 d2 = 天数
     *      如果2012年这一年有366天就要让间隔的天数+1，因为2月份有29日。 
     * @param maxDays   用于记录一年中有365天还是366天 
     * @param d1    表示在这年中过了多少天 
     * @param d2    表示在这年中过了多少天 
     * @param y1    当前为2012年 
     * @param y2    当前为2012年 
     * @param calendar  根据日历对象来获取一年中有多少天 
     * @return  计算后日期间隔的天数 
     */ 
    public static int numerical(int maxDays, int d1, int d2, int y1, int y2, Calendar calendar){
        int day = d1 - d2;
        int betweenYears = y1 - y2;
        List<Integer> d366 = new ArrayList<Integer>();
           
        if(calendar.getActualMaximum(Calendar.DAY_OF_YEAR) == 366){
            day += 1;
        }
           
        for (int i = 0; i < betweenYears; i++) {
            // 当年 + 1 设置下一年中有多少天  
            calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR)) + 1);
            maxDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            // 第一个 366 天不用 + 1 将所有366记录，先不进行加入然后再少加一个  
            if(maxDays != 366){
                day += maxDays;
            }else{
                d366.add(maxDays);
            }
            // 如果最后一个 maxDays 等于366 day - 1  
            if(i == betweenYears-1 && betweenYears > 1 && maxDays == 366){
                day -= 1;
            }
        }
           
        for(int i = 0; i < d366.size(); i++){
            // 一个或一个以上的366天  
            if(d366.size() >= 1){
                day += d366.get(i);
            } 
        }  
        return day;
    }
    
    /**
     * 字符串转换为日期
     *       yyyy-MM-dd 
     * @param dateStr
     */
    public static Date formatDateFromString2(String dateStr) {
    	SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	try {
			Date date = df.parse(dateStr); 
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
    }
    /**
     * 字符串转换为日期
     *       yyyy-MM-dd 
     * @param dateStr
     */
    public static Date formatDateFromString3(String dateStr) {
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			Date date = df.parse(dateStr); 
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
    }
    /**
     * 字符串转换为日期
     *       yyyy-MM-dd 
     * @param dateStr
     */
    public static Date formatDateFromString4(String dateStr) {
    	SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	try {
			Date date = df.parse(dateStr); 
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
    }
    /**
     * 获取一个1-10000的数值
     * @return
     */
    public static int getRandomValue() {
    	return new Random().nextInt(10000)+1;
    }
    
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
    	SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd");
    	try {
			return df.format(date);
		} catch (Exception e) {
		}
    	return "";
    }
    
    public static String getNow2() {
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			return df.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
    
    public static String getNow3() {
    	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    	try {
			return df.format(new Date());
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
    
    public static String getClientDate(Date date) {
    	SimpleDateFormat df=new SimpleDateFormat("MM-dd");
    	try {
			return df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
    }
    
    /**
     * 分割字符串  比如分割 3048_100
     * @param source
     * @param sign
     * @return
     */
    public static String[] splitString(String source, String sign){
    	try{
    		return source.split(sign);	
    	}catch(Exception e){
    		return new String[0];
    	}
    }
    
    /**
	 * 16进制转换成10进制
	 * @param hex
	 * @return
	 */
	public static String hexToInt(String hex) {
		try {
			return Integer.parseInt(hex, 16)+"";
		} catch (Exception e) {
		}
		return "0";
	}
    
    public static String getUserid(String content) {
    	if(content == null) {
    		return "0";
    	}
    	//是最新版的
    	if(content.contains("Z")) {
    		String[] rs = splitString(content, "Z");
    		if(rs.length>1) {
    			return hexToInt(rs[0]);
    		}
    	}else{
    		String[] rs = splitString(content, "_|A");
    		if(rs.length>1) {
    			return rs[0];
    		}
    	}
    	return "0";
    }
    /**
     * 获取版本号
     * @param content
     * @return
     */
    public static String getVersonCode(String content) {
    	if(content == null) {
    		return "0";
    	}
    	//是最新版的
    	if(content.contains("Z")) {
    		String[] rs = splitString(content, "Z");
    		if(rs.length>1) {
    			return hexToInt(rs[1]);
    		}
    	}else{
    		String[] rs = splitString(content, "_|A");
    		if(rs.length>1) {
    			return rs[1];
    		}
    	}
    	return "0";
    }
    
    public static String intToHex(int x) {
    	return Integer.toHexString(x);
    }
    public static String longToHex(long x) {
    	return Long.toHexString(x);
    }
    public static int hexToint(String hex) {
    	return Integer.parseInt(hex, 16);
    }
    
    public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		String idstr = uuid.toString().replace("-", "");  
		System.out.println(idstr.toLowerCase());
		return idstr.toLowerCase();
	}
    
    
    public static boolean isMobileNO(String mobiles){
		//^((13[0-9])|(15[^4,\\D])|(147)|(18[0,5-9]))\\d{8}$
		String str = "^(1[3-8][0-9])\\d{8}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
  
