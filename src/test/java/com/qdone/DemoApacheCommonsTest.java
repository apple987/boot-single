package com.qdone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.Fraction;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.math.Range;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.validator.routines.DateValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;

/**
 * @author 付为地
 * 
 * 简单测试commons-lang的数据
 *    ArrayUtils – 用于对数组的操作，如添加、查找、删除、子数组、倒序、元素类型转换等；
 *    BitField – 用于操作位元，提供了一些方便而安全的方法；
 *    BooleanUtils – 用于操作和转换boolean或者Boolean及相应的数组；
 *    CharEncoding – 包含了Java环境支持的字符编码，提供是否支持某种编码的判断；
 *    CharRange – 用于设定字符范围并做相应检查；
 *    CharSet – 用于设定一组字符作为范围并做相应检查；
 *    CharSetUtils – 用于操作CharSet；
 *    CharUtils –用于操作char值和Character对象；
 *    ClassUtils – 用于对Java类的操作，不使用反射；
 *    ObjectUtils –用于操作Java对象，提供null安全的访问和其他一些功能；
 *    RandomStringUtils – 用于生成随机的字符串；
 *    SerializationUtils – 用于处理对象序列化，提供比一般Java序列化更高级的处理能力；
 *    StringEscapeUtils –用于正确处理转义字符，产生正确的Java、JavaScript、HTML、XML和SQL代码；
 *    StringUtils –处理String的核心类，提供了相当多的功能； 
 *    SystemUtils –在java.lang.System基础上提供更方便的访问，如用户路径、Java版本、时区、操作系统等判断；
 * 针对动态jexl表达式，可以参考com.qdone.common.util.JexlUtil
 *    利用org.apache.commons.jexl2.JexlEngine，实现动态jexl表达式,简单实例
 */
public class DemoApacheCommonsTest {
	/**
	 * ArrayUtils用于对数组的操作，如添加、查找、删除、子数组、倒序、元素类型转换等
	 */
	@Test
	public void testArrayUtils() {
		// data setup
		int[] intArray1 = { 2, 4, 8, 16 };
		int[][] intArray2 = { { 1, 2 }, { 2, 4 }, { 3, 8 }, { 4, 16 } };
		Object[][] notAMap = { { "A", new Double(100) }, { "B", new Double(80) }, { "C", new Double(60) },
				{ "D", new Double(40) }, { "E", new Double(20) } };

		// printing arrays
		System.out.println("intArray1: " + ArrayUtils.toString(intArray1));
		System.out.println("intArray2: " + ArrayUtils.toString(intArray2));
		System.out.println("notAMap: " + ArrayUtils.toString(notAMap));

		// finding items
		// 判断某个数组数组是不是包含某个元素
		System.out.println("intArray1 contains '8'? " + ArrayUtils.contains(intArray1, 8));
		System.out.println("intArray1 index of '8'? " + ArrayUtils.indexOf(intArray1, 8));
		System.out.println("intArray1 last index of '8'? " + ArrayUtils.lastIndexOf(intArray1, 8));

		// cloning and resversing
		// 数组的克隆和反转
		int[] intArray3 = ArrayUtils.clone(intArray1);
		System.out.println("intArray3: " + ArrayUtils.toString(intArray3));
		ArrayUtils.reverse(intArray3);
		System.out.println("intArray3 reversed: " + ArrayUtils.toString(intArray3));

		// primitive to Object array
		// 转换成包装类操作
		Integer[] integerArray1 = ArrayUtils.toObject(intArray1);
		System.out.println("integerArray1: " + ArrayUtils.toString(integerArray1));

		// build Map from two dimensional array
		// 将Objec[][]二维数组转换成map
		@SuppressWarnings("rawtypes")
		Map map = ArrayUtils.toMap(notAMap);
		System.out.println("object[][] to map:" + map);
		Double res = (Double) map.get("C");
		System.out.println("get 'C' from map: " + res);
	}

	/**
	 * StringUtils 处理String的核心类，提供了相当多的功能；
	 * 
	 *    public static boolean isAlpha(String str); 只由字母组成 
	 *    public static boolean isAlphaSpace(String str); 只有字母和空格组成
	 *    public static boolean isAlphanumeric(String str);只由字母和数字组成
	 *    public static boolean isAlphanumericSpace(String str);只由字母数字和空格组成
	 *    public static boolean isNumeric(String str);只由数字组成
	 *    public static boolean isNumericSpace(String str);只由数字和空格组成
	 */
	@Test
	public void testStringUtils() {
		// data setup
		String str1 = "";
		String str2 = "";
		String str3 = "\t";
		String str4 = null;
		String str5 = "123";
		String str6 = "ABCDEFG";
		String str7 = "Itfeels good to use JakartaCommons.\r\n";

		// check for empty strings
		System.out.println("==============================");
		System.out.println("Is str1 blank? " + StringUtils.isBlank(str1));
		System.out.println("Is str2 blank? " + StringUtils.isBlank(str2));
		System.out.println("Is str3 blank? " + StringUtils.isBlank(str3));
		System.out.println("Is str4 blank? " + StringUtils.isBlank(str4));

		// check for numerics
		System.out.println("==============================");
		System.err.println(StringUtils.isNumeric("1"));
		System.err.println(StringUtils.isNumeric("-1"));
		System.err.println(StringUtils.isNumeric("+1"));
		System.err.println(StringUtils.isNumeric(",1"));
		System.out.println("Is str5 numeric? " + StringUtils.isNumeric(str5));
		System.out.println("Is str6 numeric? " + StringUtils.isNumeric(str6));

		// reverse strings / whole words 倒序生成字符串
		System.out.println("==============================");
		System.out.println("str6: " + str6);
		System.out.println("str6reversed: " + StringUtils.reverse(str6));
		System.out.println("str7: " + str7);
		String str8 = StringUtils.chomp(str7);
		str8 = StringUtils.reverseDelimited(str8, ' ');
		System.out.println("str7 reversed whole words : \r\n" + str8);

		// build header (useful to print logmessages that are easy to locate)
		System.out.println("==============================");
		System.out.println("print header:");
		String padding = StringUtils.repeat("=", 2);
		String msg = StringUtils.center(" Customised Header ", 50, "%");
		Object[] raw = new Object[] { padding, msg, padding };
		String header = StringUtils.join(raw, "\r\n");
		System.out.println(header);
		System.out.println("==========================");
		System.out.println("是否为空isEmpty?" + StringUtils.isEmpty(""));
		System.out.println("是否为空isEmpty?" + StringUtils.isEmpty(" "));
		System.out.println("是否为空isEmpty?" + StringUtils.isEmpty(null));
		System.out.println("==========================");
		// static boolean isBlank(CharSequence str) 判断字符串是否为空或空字符串或null;
		// static boolean isNotBlank(CharSequence str) 判断字符串是否非空或非null;
		System.out.println("是空为空或空字符串isBlank?" + StringUtils.isBlank(""));
		System.out.println("是空为空或空字符串isBlank?" + StringUtils.isBlank(null));
		System.out.println("是空为空或空字符串isBlank?" + StringUtils.isBlank(" "));
		System.out.println("====================");
		System.out.println("不是空为空或空字符串isNotBlank?" + StringUtils.isNotBlank(""));
		System.out.println("不是空为空或空字符串isNotBlank?" + StringUtils.isNotBlank(null));
		System.out.println("不是空为空或空字符串isNotBlank?" + StringUtils.isNotBlank(" "));
		System.out.println("缩进显示abbreviate:" + StringUtils.abbreviate("abcdefg", 20));
		System.out.println("缩进显示abbreviate:" + StringUtils.abbreviate("abcdefg", 4));
		System.out.println("====================");
		System.out.println("首字母大写capitalize:" + StringUtils.capitalize("abcdefg"));
		System.out.println("首字母小写uncapitalize:" + StringUtils.uncapitalize("Abcdefg"));
		System.out.println("====================");
		System.out.println("字符串center显示在一个大字符串的位置:" + StringUtils.center("abcdefg", 20));
		// 总长度20,abcdefg放在中间,其他部分采用*_填充,不够或者多的就自动截取*_
		System.out.println("字符串center显示在一个大字符串的位置:" + StringUtils.center("abcdefg", 20, "*_"));
		System.out.println("字符串leftPad显示在一个大字符串的位置:" + StringUtils.leftPad("abc", 10, "*"));
		System.out.println("字符串rightPad显示在一个大字符串的位置:" + StringUtils.rightPad("abc", 10, "*"));
		System.out.println("====================");
		System.out.println("字符串repeat重复次数:" + StringUtils.repeat("abc", 5));
		System.out.println("====================");
		System.out.println("字符串全小写?" + StringUtils.isAllLowerCase("abC"));
		System.out.println("字符串全小写?" + StringUtils.isAllLowerCase("abc"));
		System.out.println("字符串全大写?" + StringUtils.isAllUpperCase("abC"));
		System.out.println("字符串全大写?" + StringUtils.isAllUpperCase("ABC"));
		System.out.println("====================");
		System.out.println("只有字母组成isAlpha?" + StringUtils.isAlpha("abdefg"));
		System.out.println("只有字母和空格组成isAlphaSpace?" + StringUtils.isAlphaSpace("abdefg "));
		System.out.println("只有字母和数字组成isAlphanumeric?" + StringUtils.isAlphanumeric("a2bdefg"));
		System.out.println("只有数字组成isNumeric?" + StringUtils.isNumeric("a2bdefg"));
		System.out.println("只有数字和空格组成isNumericSpace?" + StringUtils.isNumericSpace("abdefg "));
		System.out.println("====================");
		System.out.println("小字符串[ab]在大字符串[ababsssababa]中出现次数:" + StringUtils.countMatches("ababsssababa", "ab"));
		System.out.println("====================");
		System.out.println("字符串[abcdef]倒序reverse输出" + StringUtils.reverse("abcdef"));
		System.out.println("====================");
		System.out.println("字符串大小写转换,空格不动:" + StringUtils.swapCase("I am a-A*a"));

	}

	/**
	 * 针对ObjectUtils本处放弃apache-commons-lang包 
	 * 采用spring-util包里面的ObjectUtils
	 * org.springframework.util.ObjectUtils 
	 * 常用方法:ObjectUtils.isEmpty(Object obj)
	 *        ObjectUtils.isEmpty(Object[] array) 
	 *        ObjectUtils.isArray(Object obj)
	 *        //Check whether the given array contains the given element.
	 *        ObjectUtils.containsElement(Object[] array, Object element) 
	 *        //Check whether the given array of enum constants contains a constant with the given name, ignoring case when determining a match.
	 *        ObjectUtils.containsConstant(Enum<?>[] enumValues, String constant)
	 *        //Check whether the given array of enum constants contains a constant with the given name, ignoring case when determining a match.
	 *        ObjectUtils.containsConstant(Enum<?>[] enumValues, String constant,boolean caseSensitive) //Return whether the given throwable is a checked
	 *        exception: //that is, neither a RuntimeException nor an Error
	 *        ObjectUtils.isCheckedException(Throwable ex)
	 */
	@Test
	public void testObjectUtils() {
		System.err.println(ObjectUtils.isEmpty(null));
		System.err.println(ObjectUtils.isArray(null));
	}

	/*
	 * org.apache.commons.lang.math 
	 * 在Jakarta Commons中，
	 * 专门处理数学计算的类分别可以在两个地方找到：
	 * 一是Commons Lang的org.apache.commons.lang.math包中，
	 * 二是在Commons Math这个单独的子项目中。
	 *  由于后者主要是处理复数、矩阵等，相对使用比较少，在我的笔记中就只简单讲讲Commons Lang中的math包。
	 *  对后者感兴趣的可以看看 http://jakarta.apache.org/commons/math/
	 * 
	 * org.apache.commons.lang.math包中共有10个类，这些类可以归纳成四组： 
	 * 1- 处理分数的Fraction类；
	 * 2-处理数值的NumberUtils类；
	 * 3-处理数值范围的Range、NumberRange、IntRange、LongRange、FloatRange、DoubleRange类；
	 * 4-处理随机数的JVMRandom和RandomUtils类。
	 */
	@Test
	public void testMathUtils() {
		demoFraction();
        demoNumberUtils();
        demoNumberRange();
        demoRandomUtils();
	}
	/**
	 * 测试apache.commons.lang.math
	 * 处理数值的Fraction处理分数类
	 */
	private static void demoFraction() {
		System.out.println(StringUtils.center(" demoFraction ", 30, "="));
		Fraction myFraction = Fraction.getFraction(144, 90);
		// FractionmyFraction = Fraction.getFraction("1 54/90");
		System.out.println("144/90 as fraction: " + myFraction);
		System.out.println("144/90 to proper: " + myFraction.toProperString());
		System.out.println("144/90 as double: " + myFraction.doubleValue());
		System.out.println("144/90 reduced: " + myFraction.reduce());
		System.out.println("144/90 reduced proper: " + myFraction.reduce().toProperString());
		System.out.println();
	}
	/**
	 * 测试apache.commons.lang.math
	 * 处理数值的NumberUtils类；
	 */
	private static void demoNumberUtils() {
        System.out.println(StringUtils.center(" demoNumberUtils ", 30, "="));
        System.out.println("Is 0x3Fa number? "
                +StringUtils.capitalize(BooleanUtils.toStringYesNo(NumberUtils
                        .isNumber("0x3F")))+ ".");
        double[] array = { 1.0, 3.4, 0.8, 7.1, 4.6 };
        double max = NumberUtils.max(array);
        double min = NumberUtils.min(array);
        String arrayStr =ArrayUtils.toString(array);
        System.out.println("Max of " + arrayStr + " is: " + max);
        System.out.println("Min of " + arrayStr + " is: " + min);
        System.out.println();
    }
	/**
	 * 测试apache.commons.lang.math
	 * 处理数值范围的Range、NumberRange、IntRange、LongRange、FloatRange、DoubleRange类
	 */
	private static void demoNumberRange() {
        System.out.println(StringUtils.center(" demoNumberRange ", 30, "="));
        Range normalScoreRange = new DoubleRange(90, 120);
        double score1 = 102.5;
        double score2 = 79.9;
        System.out.println("Normal score rangeis: " + normalScoreRange);
        System.out.println("Is "
                + score1
                + "a normal score? "
                + StringUtils
                        .capitalize(BooleanUtils.toStringYesNo(normalScoreRange
                                .containsDouble(score1)))+ ".");
        System.out.println("Is "
                + score2
                + "a normal score? "
                + StringUtils
                        .capitalize(BooleanUtils.toStringYesNo(normalScoreRange
                                .containsDouble(score2)))+ ".");
        System.out.println();
    }
    /**
     * 测试apache.commons.lang.math.RandomUtils
     */
	private static void demoRandomUtils() {
        System.out.println(StringUtils.center(" demoRandomUtils ", 30, "="));
        for (int i = 0; i < 5; i++) {
            System.out.println(RandomUtils.nextInt(100));
        }
        System.out.println();
    }
	/**
	 * 测试org.apache.commons.lang.time
	 * 日期工具类
	 * 来看我在Common Lang中最后要讲的一个包：org.apache.commons.lang.time。这个包里面包含了如下5个类：
			DateFormatUtils – 提供格式化日期和时间的功能及相关常量；
			DateUtils – 在Calendar和Date的基础上提供更方便的访问；
			DurationFormatUtils – 提供格式化时间跨度的功能及相关常量；
			FastDateFormat – 为java.text.SimpleDateFormat提供一个的线程安全的替代类；
			StopWatch – 是一个方便的计时器。
	 */
	@Test
	public void testTimeUtils() {
		demoDateUtils();
        demoStopWatch();
	}
	/**
	 * 测试org.apache.commons.validator
	 * 数据验证 
	 */
	@Test
	public void testValidatorUtils() {
		/* 验证日期 */
		// 获取日期验证
		DateValidator dvalidator = DateValidator.getInstance();
		// 验证/转换日期
		Date fooDate = dvalidator.validate("20141141111", "dd/MM/yyyy");
		if (fooDate == null) {
			// 错误 不是日期
			System.err.println("不是日期");
		}
		// 设置参数
		boolean caseSensitive = false;
		String regex1 = "^([A-Z]*)(?:\\-)([A-Z]*)*$";
		String regex2 = "^([A-Z]*)$";
		String[] regexs = new String[] { regex1, regex2 };
		// 创建验证
		RegexValidator validator = new RegexValidator(regexs, caseSensitive);
		// 验证返回boolean
		boolean valid = validator.isValid("abc-def");
		System.err.println(valid);
		// 验证返回字符串
		String result = validator.validate("abc-def");
		System.err.println(result);
		// 验证返回数组
		String[] groups = validator.match("abc-def");
		System.err.println(groups);
		 //4.取得类名  
	    System.out.println(ClassUtils.getShortClassName(DemoApacheCommonsTest.class));  
	     //取得其包名  
	    System.out.println(ClassUtils.getPackageName(DemoApacheCommonsTest.class));  
	}
	
	/**
     * 集合操作
	 * 1、并集:set1跟set2的并集
	 * CollectionUtils.union(set1,set2);
	 * 2、交集主要使用intersection
	 * CollectionUtils.intersection(set1,set2);
	 * CollectionUtils.retainAll(set1,set2);
	 * 3、差集:set1跟set2的差集
	 *  CollectionUtils.subtract(set1,set2);
	 * 4.交集的补集:set1跟set2的交集的补集
	 *  CollectionUtils.disjunction(set1,set2)
     */
    @SuppressWarnings("unchecked")
	@Test
	public void testCollectionsUtils() {
    	 Set<Integer> set1 =new HashSet<Integer>();
         set1.add(1);
         set1.add(2);
         set1.add(3);
         
         Set<Integer> set2 =new HashSet<Integer>();
         set2.add(2);
         set2.add(3);
         set2.add(4);
         //判断集合是否为空
         System.out.println("=========判断集合是否为空============");
         System.out.println(CollectionUtils.isEmpty(set1));   //true
         System.out.println(CollectionUtils.isEmpty(set2));   //true
         //判断集合是否不为空
         System.out.println("=========判断集合是否不为空============");
         System.out.println(CollectionUtils.isNotEmpty(set1));   //true
         System.out.println(CollectionUtils.isNotEmpty(set2));   //true
         //比较两集合值
         System.out.println("=========比较集合内容============");
         System.out.println(CollectionUtils.isEqualCollection(set1,set2));   //false
         System.out.println(CollectionUtils.isEqualCollection(set1,set2));   //false
         //并集
         System.out.println("=========并集============");
         Collection<Integer> col =CollectionUtils.union(set1,set2);
         for(Integer temp:col){
             System.out.println(temp);
         }
         //交集
         System.out.println("=========交集============");               
         col =CollectionUtils.intersection(set1, set2);
         /*col =CollectionUtils.retainAll(set1, set2);*/
         for(Integer temp:col){
             System.out.println(temp);
         }
         //差集
         System.out.println("====set1和set2的=====差集============");       
         col =CollectionUtils.subtract(set1, set1);
         for(Integer temp:col){
             System.out.println(temp);
         }   
         System.out.println("=========交集的补集============");        
         //交集的补集
         System.out.println(CollectionUtils.disjunction(set1,set2)); 
         System.out.println("=========集合包含所有============");
         System.out.println(CollectionUtils.containsAny(set1,set2));
         //祛除重复
         List<String> source=new ArrayList<String>();
         source.add("123456");
         source.add("123456");
         source.add("apple");
         source.add("apple");
         source.add("zhangsan");
         System.out.println("源集合:"+JSON.toJSONString(source));
         List<String> result=new ArrayList<String>(new HashSet<String>(source));
         System.out.println("结果集合:"+JSON.toJSONString(result));
        /* List<String> result1=new ArrayList<String>(new HashSet<String>(null));
         System.out.println("结果集合:"+JSON.toJSONString(result1));*/
	}
    
    /**
     * IoUtils流的操作
     * IOUtils:读取UTF-8数据时,需要注意UTF-8文件中的BOM字节,请采用如下方式来读取
     */
   @Test
   public void testIoUtils() throws IOException{
	   LineIterator line= IOUtils.lineIterator(new BOMInputStream(FileUtils.openInputStream(new File("D://data.txt"))), "UTF-8");
	   System.out.println(line);
   }
   /**
    * FileUtils:针对文件的一些操作
    * @throws Exception 
    */
   @Test
   public void testFileUtils() throws Exception{
		//拷贝文件
		//FileUtils.copyFile(new File("D://data.txt"), new File("E:data.txt"));
		// 拷贝文件夹
		//FileUtils.copyDirectory(new File("D://antetype"), new File("E://data"), true);
		// 删除某个文件夹
		//FileUtils.cleanDirectory(new File("E://data"));
		// 拷贝文件到文件夹
		//FileUtils.copyFileToDirectory(new File("D://data.txt"), new File("D://antetype//file"));
		// 输入流拷贝文件
		//FileUtils.copyInputStreamToFile(FileUtils.openInputStream(new File("D://data.txt")),new File("D://antetype//file.txt"));
		// 输入流拷贝文件
		//FileUtils.copyToFile(FileUtils.openInputStream(new File("D://data.txt")), new File("D://antetype//file.txt"));
		// 拷贝网络文件到文件
		//FileUtils.copyURLToFile(new URL("http://localhost/qdone/html/index.html"), new File("D://antetype//test.html"));
	    //列出目录下的所有文件
	    String[] arr = org.aspectj.util.FileUtil.listFiles(new File("D://qdone"));
		for (int i = 0; i < arr.length; i++) {
			System.err.println(arr[i]);
		}
		System.err.println("=================");
		//获取路径最里层的文件夹名称
		System.err.println(FilenameUtils.getBaseName("D:/360Downloads/Apk"));
		//获取文件类型
		System.err.println(FilenameUtils.getExtension("D:/360Downloads/Apk/data.txt"));
		//获取文件路径的根目录
		System.err.println(FilenameUtils.getPrefix("D:/360Downloads/Apk/data.txt"));
		//获得两个文件的相对路径
		System.err.println(org.apache.tools.ant.util.FileUtils.getRelativePath(new File("D:/360Downloads"), new File("D:/antetype/data.txt")) );
	}
   
   private static void demoDateUtils() {
       System.out.println(StringUtils.center(" demoDateUtils ", 30, "="));
       Date date = new Date();
       String isoDateTime =DateFormatUtils.ISO_DATETIME_FORMAT.format(date);
       String isoTime =DateFormatUtils.ISO_TIME_NO_T_FORMAT.format(date);
       FastDateFormat fdf = FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss S E");
       String customDateTime =fdf.format(date);
       System.out.println("ISO_DATETIME_FORMAT: " + isoDateTime);
       System.out.println("ISO_TIME_NO_T_FORMAT: " + isoTime);
       System.out.println("Custom FastDateFormat: " +customDateTime);
       System.out.println("Default format: " + date);
       System.out.println("Round HOUR: " + DateUtils.round(date,Calendar.HOUR));
       System.out.println("Truncate HOUR: " +DateUtils.truncate(date, Calendar.HOUR));
       System.out.println();
   }
  
   private static void demoStopWatch() {
       System.out.println(StringUtils.center(" demoStopWatch ", 30, "="));
       StopWatch sw = new StopWatch();
       sw.start();
       operationA();
       sw.stop();
       System.out.println("operationA used " + sw.getTime() + " milliseconds.");
       System.out.println();
   }
  
   private static void operationA() {
       try {
           Thread.sleep(999);
       }
       catch (InterruptedException e) {
           // do nothing
       }
   }

}
