package org.vector.jv.vj.base;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * int,long ok
 * 浮点，金额都用BigDecimal,比如金额要小于10万元：money<10 0000
 * double,float不推荐使用
 */
public class Jvalidator {
	private static SimpleDateFormat sfm = new SimpleDateFormat("HH:mm:ss");
	private static SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat yMdsfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat yMdsfmm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
	private static boolean required(String val){
		val = val.trim();
		return !("".equals(val) || val.length()==0);
	}
	
	// 6 length(String val, 6, 6)
	//100以内含100 length(String val, 0, 100)
	// 3个以上 length(String val, 3, 0)
	// 4-8 [包含4，8],等价于不包含的[5,9]length(String val, 5, 7)
	private static boolean length(String val, long len1, long len2){
		if(len1==len2){
			return val.length()==len1;
		}
		if(len1 == 0){
			return val.length()<=len2;
		}
		if(len2 == 0){
			return val.length()>=len1;
		}
		
		return val.length() <=len1 && len2>=val.length();
	}
	
	public static boolean check(Object obj, String config){
		JSONObject jo = (JSONObject) JSON.toJSON(obj);
		JSONObject cfgs = JSON.parseObject(config);
		boolean result = false;
		
		//然后根据配置校验
		Set<String> keySet = cfgs.keySet();
		Iterator<String> keysIt = keySet.iterator();
		while(keysIt.hasNext()){
			String key = keysIt.next();
			//第一个校验开始
			String option = cfgs.get(key).toString();
			Object attObj = jo.get(key);//要求有值的属性，没有属性在json中的时候
			if(attObj == null){
				return false;
			}
			
			String val = null;
			if(attObj instanceof Timestamp){
				val = yMdsfmm.format(attObj);
			}else if(attObj instanceof Time){
				val = sfm.format(attObj);
			}else if(attObj instanceof java.sql.Date){
				val = yMd.format(attObj);
			}else if(attObj instanceof Date){
				val = yMdsfm.format(attObj);
			}else{
				 val = attObj.toString();
			}
			
			if(option.contains(";")){//多则校验
				String[] options = option.split(";");//required;length:0,6
				
				for (int i = 0; i < options.length; i++) {
					if(options[i].contains(":")){
						String[] temp = options[i].split(":");
						
						if("compare".equals(temp[0])){
							//取比较的字段的值：temp[1]=> V,colx
							String[] args = temp[1].split(",");
							Object valObj = jo.get(args[1]);//属性名字：比如birth
							result = optionArgsChecker(val, temp[0], new String[]{args[0], valObj.toString()});//('11:11:23', 'compare', ['V', '12:22:22'])
						}else{
							result = optionArgsChecker(val, temp[0], new String[]{temp[1]});
						}
						
					}else{
						result = optionChecker(val, options[i]);
					}
					
					if(!result){//一旦校验到false就返回
						return result;
					}
				}
				
			}else{//单则校验
				if(option.contains(":")){
					String[] temp = option.split(":");
					result = optionArgsChecker(val, temp[0], new String[]{temp[1]});
//					if("compare".equals(option)){
//						//取比较的字段的值：temp[1]=> : <colx  >colx
//						Object valObj = jo.get(temp[1].substring(1));//属性名字：比如birth
//						result = optionArgsChecker(val, temp[0], temp[1].substring(1)+valObj.toString());//这里不知道日期，金额什么的有什么影响？
//					}else{
//						result = optionArgsChecker(val, temp[0], temp[1]);
//					}
				}else{
					result = optionChecker(val, option);
				}
				if(!result){//一旦校验到false就返回
					return result;
				}
			}
		}
		
		return result;
	}

	private static boolean optionArgsChecker(String val, String option, String[] args) {
		boolean result = false;
		
		switch (option) {
			case "length": result=lenCheck(val, args[0]); break;
			case "number": break;
			case "pattern": result = pattern(val, args[0]);break;
			case "integer": result = integer(val, args[0]);break;//整数
			case "range": result = range(val, args[0]);break;//数字
			//相同相等校验：match:2016-09-09,colx
			case "compare": result = compare(val, args);break;//比较
		}
		return result;
	}

	//1.日期大小 compare:2016-09-09<colx
	//2.金额大小compare:100<10_0000
	//3.时间大小 compare:09:10:11<colx
	//4.日期时间大小 compare:2016-09-09 09:10:11<colx
	//<  >   <=  >= how?
	//V  A   W   M
	private static boolean compare(String val1, String[] args) {
		//W 右转90度，类似<= M右转90度，类似>=
		//V 右转90度，类似<  A右转90度，类似>
		boolean result = false;
//		System.out.println("val1 = "+val1);
//		System.out.println("args=> "+args);
		
//		钱数字符串比不了，50 > 100,因为5-1=4
		if(val1.contains(":") || val1.contains("-")){
			//时间比较ok，没有问题
			switch (args[0]) {
				case "M": result = val1.compareTo(args[1])>=0;break;
				case "W": result = val1.compareTo(args[1])<=0;break;
				case "V": result = val1.compareTo(args[1])<0;break;
				case "A": result = val1.compareTo(args[1])>0;break;
			}
			
		}else if(val1.contains(".") || val1.matches("^[1-9]\\d*$")){//金钱数
			BigDecimal b1 = new BigDecimal(val1);
			BigDecimal b2 = new BigDecimal(args[1]);
			switch (args[0]) {
				case "M": result = b1.compareTo(b2)>=0;break;
				case "W": result = b1.compareTo(b2)<=0;break;
				case "V": result = b1.compareTo(b2)<0;break;
				case "A": result = b1.compareTo(b2)>0;break;
			}
		}
		return result;
	}

	//支持 值域离散
	//支持 值域范围
	//支持 值域离散+值域范围
	private static boolean range(String val, String args) {
		boolean r1 = true;
		boolean f1 = false;
		
		boolean r2 = true;
		boolean f2 = false;
		if(args.contains("[") && args.contains("]")){
			f1 = true;
			//范围域校验
			String vals = args.substring(1+args.indexOf("["), args.indexOf("]"));
			String[] varr = vals.split(",");
			//比范围：比如v=3,  varr[0] <= v <= varr[1]
//			TODO 单边域的支持
//			if(vals.contains(",")){
//			}else if(vals.contains("+")){//[3+] 大于等于3的范围
//			}else if(vals.contains("-")){//[3-] 小于等于3的范围
//			}
			
			//compareTo就是 大于号 的含义 a.compareTo(b) <==> a>b
			r1 = new BigDecimal(val).compareTo(new BigDecimal(varr[0]))>=0 && new BigDecimal(varr[1]).compareTo(new BigDecimal(val))>=0;
		}
		if(args.contains("(") && args.contains(")")) {
			f2 = true;
			//值域校验
			String vals = args.substring(args.indexOf("(")+1, args.indexOf(")"));
			List<String> vs= Arrays.asList(vals.split(","));//比如有：1,2,3,4,5
			r2 = vs.contains(val);//只要包含即可
		}
		
		if(f1 && f2){
			return r1 || r2;//1真俱真
		} else if(f1){
			return r1;
		} else if(f2){
			return r2;
		}
		return false;
	}

	private static boolean integer(String val, String args) {
		boolean result = false;
		if(args.length()==1){//1个参数
			if("0".equals(args)){
				result = Long.parseLong(val)==0;
			}
			if("+".equals(args)){
				result = Long.parseLong(val)>0;
			}
			if("-".equals(args)){
				result = Long.parseLong(val)<0;
			}
		}else if(args.length()==2){//2个参数
			if(args.contains("0") && args.contains("+")){
				result = Long.parseLong(val)==0 || Long.parseLong(val)>0;
			}
			if(args.contains("0") && args.contains("-")){
				result = Long.parseLong(val)==0 || Long.parseLong(val)<0;
			}
		}else{//3个参数
			if(args.contains("0") && args.contains("+") && args.contains("-")){
				result = Long.parseLong(val)==0 || Long.parseLong(val)>0 || Long.parseLong(val)<0;
			}
		}
		return result;
	}

	private static boolean lenCheck(String val, String args) {
		if(args.contains(",")){
			String[] arr = args.split(",");
			return length(val, Long.parseLong(arr[0]), Long.parseLong(arr[1]));
		}
		return length(val, Long.parseLong(args), Long.parseLong(args));
	}

	/**
	 * 无参数校验
	 */
	private static boolean optionChecker(String val, String option) {
		boolean result = false;
		
		switch (option) {
			case "required": result = required(val);break;
			case "email": result = patternEmail(val);break;
			case "mobile": result = mobile(val);break;
			case "integer": result = patternInt(val);break;
			case "date": result = patternDate(val);break;
			case "time": result = patternHHmmss(val);break;
			case "datetime": result = patternDatetime(val);break;
			case "timestamp": result = patternTimestamp(val);break;
		}
		
		return result;
	}
	
	//全范围整数
	private static boolean patternInt(String val) {
		try {
			System.out.println(""+val);
			long i = Long.parseLong(val);
			return true;
		} catch (NumberFormatException e) {
			throw new RuntimeException("整数验证失败异常", e);
		}
	}

	private static boolean pattern(String val, String rule){
		Pattern p =  Pattern.compile(rule);//正则匹配
		Matcher m = p.matcher(val);
		return m.matches();
	}
	
	//号码段：
//	移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
//　联通：130、131、132、152、155、156、185、186
//　电信：133、153、180、189、（1349卫通）
//	"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
//	前3位属于特殊情况，后8位任意
	private static boolean mobile(String val){
		return pattern(val, "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
	}
	
	private static boolean patternEmail(String val){
        return pattern(val, "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");  
	}

	private static boolean patternTimestamp(String val) {
		String[] arr = val.split(" ");
		return patternDate(arr[0]) && patternHHmmssSSS(arr[1]);
	}

	private static boolean patternDatetime(String val) {
		String[] arr = val.split(" ");
		return patternDate(arr[0]) && patternHHmmss(arr[1]);
	}
	
	private static boolean patternHHmmssSSS(String val) {
		return pattern(val, "^(([0-1]?[0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9]):([0-9]{3})$");
	}

	private static boolean patternHHmmss(String val) {
		return pattern(val, "^(([0-1]?[0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9])$");
	}

	private static boolean patternDate(String val) {
		return pattern(val, "^((((19|20)\\d{2})-(0?[13-9]|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$");
	}
}
