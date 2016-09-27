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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * int,long ok
 * 浮点，金额都用BigDecimal
 * double,float不使用
 */
public class ValidatorJ {
	private SimpleDateFormat sfm = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat yMd = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat yMdsfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat yMdsfmm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
	
	public static boolean valid(JSONArray array, String config){
		return new ValidatorJ().check(array, config);
	}
	public static boolean valid(List<Object> list, String config){
		return new ValidatorJ().check(list, config);
	}
	public static boolean valid(Object obj, String config){
		return new ValidatorJ().check(obj, config);
	}
	/**
	 * 集合验证
	 */
	public boolean check(JSONArray array, String config){
		boolean result = false;
		for(Object obj : array){
			result = check(obj, config);
			if(!result) break;
		}
		return result;
	}
	/**
	 * 集合验证
	 */
	public boolean check(List<Object> list, String config){
		boolean result = false;
		for(Object obj : list){
			result = check(obj, config);
			if(!result) break;
		}
		return result;
	}
	/**
	 * bean验证
	 */
	public boolean check(Object obj, String config){
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
				String[] rules = option.split(";");//required;length
				for (int i = 0; i < rules.length; i++) {//规则循环校验
					result = singleValid(rules[i], val, jo, result);
					if(!result)return result;//一旦校验到false就返回
				}
			}else{
				result = singleValid(option, val, jo, result);
				if(!result)return result;//一旦校验到false就返回
			}
		}
		
		return result;
	}
	
	/**
	 *单则校验
	 */
	private boolean singleValid(String rule, String val, JSONObject jo, boolean result) {
		if(rule.contains(":")){//带参数规则
			String[] temp = rule.split(":");
			
			if("compare".equals(temp[0])){
				String[] args = temp[1].split(",");
				Object valObj = jo.get(args[1]);//属性名字：比如birth
				result = optionArgsChecker(val, temp[0], new String[]{args[0], valObj.toString()});
			}else{
				result = optionArgsChecker(val, temp[0], new String[]{temp[1]});
			}
			
		}else{//不带参数规则
			result = optionChecker(val, rule);
		}
		return result;
	}
	/**
	 * 无参数校验
	 */
	private boolean optionChecker(String val, String option) {
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
	
	/**
	 * 有参数校验
	 */
	private boolean optionArgsChecker(String val, String option, String[] args) {
		boolean result = false;
		
		switch (option) {
			case "length": result=lengthCheck(val, args[0]); break;
			case "pattern": result = pattern(val, args[0]);break;
			case "integer": result = integer(val, args[0]);break;//整数
			case "range": result = range(val, args[0]);break;//数字
			case "compare": result = compare(val, args);break;//比较
		}
		return result;
	}

	/**
	 * 非空校验
	 */
	private boolean required(String val){
		val = val.trim();
		return !("".equals(val) || val.length()==0);
	}
	
	/**
	 * 大小比较，可以比较：日期，金额，时间，日期时间
	 */
	private boolean compare(String val1, String[] args) {
		boolean result = false;
		if(val1.contains(":") || val1.contains("-")){
			switch (args[0]) {
				case ">=": result = val1.compareTo(args[1])>=0;break;
				case "<=": result = val1.compareTo(args[1])<=0;break;
				case "<": result = val1.compareTo(args[1])<0;break;
				case ">": result = val1.compareTo(args[1])>0;break;
			}
		}else if(val1.contains(".") || val1.matches("^[1-9]\\d*$")){//金钱和数
			BigDecimal b1 = new BigDecimal(val1);
			BigDecimal b2 = new BigDecimal(args[1]);
			switch (args[0]) {
				case ">=": result = b1.compareTo(b2)>=0;break;
				case "<=": result = b1.compareTo(b2)<=0;break;
				case "<": result = b1.compareTo(b2)<0;break;
				case ">": result = b1.compareTo(b2)>0;break;
			}
		}
		return result;
	}

	/**
	 * 值域判断：值域离散，值域范围，值域离散+值域范围
	 */
	private boolean range(String val, String args) {
		boolean r1 = true;
		boolean f1 = false;
		
		boolean r2 = true;
		boolean f2 = false;
		if(args.contains("[") && args.contains("]")){
			f1 = true;
			String vals = args.substring(1+args.indexOf("["), args.indexOf("]"));
			String[] varr = vals.split(",");
			
			//compareTo就是 大于号 的含义 a.compareTo(b) <==> a>b
			//[3,+] 大于等于3的范围
			//[-,3] 小于等于3的范围
			if(varr[0].contains("-")){
				r1 = new BigDecimal(varr[1]).compareTo(new BigDecimal(val))>=0;
			} else if(varr[1].contains("+")){
				r1 = new BigDecimal(val).compareTo(new BigDecimal(varr[0]))>=0;
			}else{
				r1 = new BigDecimal(val).compareTo(new BigDecimal(varr[0]))>=0 && new BigDecimal(varr[1]).compareTo(new BigDecimal(val))>=0;
			}
		}
		if(args.contains("(") && args.contains(")")) {
			f2 = true;
			//值域校验
			String vals = args.substring(args.indexOf("(")+1, args.indexOf(")"));
			List<String> vs= Arrays.asList(vals.split(","));//比如有：1,2,3,4,5
			r2 = vs.contains(val);//只要包含即可
		}
		
		if(f1 && f2){
			return r1 || r2;
		} else if(f1){
			return r1;
		} else if(f2){
			return r2;
		}
		return false;
	}

	/**
	 * 整数校验
	 * 全整数
	 * 0
	 * 负整数
	 * 正整数
	 * 0 0+ 0- 0+-[不推荐]
	 */
	private boolean integer(String val, String args) {
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

	/**
	 * 字符串长度校验
	 */
	private boolean lengthCheck(String val, String args) {
		if(args.contains(",")){
			String[] arr = args.split(",");
			return length(val, Long.parseLong(arr[0]), Long.parseLong(arr[1]));
		}
		return length(val, Long.parseLong(args), Long.parseLong(args));
	}
	
	/**
	 * 6个长度 length(String val, 6, 6)
	 * 100以内含100个长度 length(String val, 0, 100)
	 * 3个以上个长度 length(String val, 3, 0)
	 * 4-8个长度[包含4，8],等价于不包含的[5,9]length(String val, 5, 7)
	 */
	private boolean length(String val, long len1, long len2){
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

	/**
	 * 全范围整数
	 */
	private boolean patternInt(String val) {
		try {
			Long.parseLong(val);//解析看是否是有效整数字符串
			return true;
		} catch (NumberFormatException e) {
			System.out.println("整数校验出错：param="+val);
			return false;
		}
	}
	
	/**
	 * 正则匹配
	 */
	private boolean pattern(String val, String rule){
		Pattern p =  Pattern.compile(rule);
		Matcher m = p.matcher(val);
		return m.matches();
	}
	
//	移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
//　联通：130、131、132、152、155、156、185、186
//　电信：133、153、180、189、（1349卫通）
//	前3位属于特殊情况，后8位任意
	private boolean mobile(String val){
		return pattern(val, "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
	}
	
	private boolean patternEmail(String val){
        return pattern(val, "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");  
	}

	private boolean patternTimestamp(String val) {
		String[] arr = val.split(" ");
		return patternDate(arr[0]) && patternHHmmssSSS(arr[1]);
	}

	private boolean patternDatetime(String val) {
		String[] arr = val.split(" ");
		return patternDate(arr[0]) && patternHHmmss(arr[1]);
	}
	
	private boolean patternHHmmssSSS(String val) {
		return pattern(val, "^(([0-1]?[0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9]):([0-9]{3})$");
	}

	private boolean patternHHmmss(String val) {
		return pattern(val, "^(([0-1]?[0-9])|(2[0-3])):([0-5][0-9]):([0-5][0-9])$");
	}

	private boolean patternDate(String val) {
		return pattern(val, "^((((19|20)\\d{2})-(0?[13-9]|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$");
	}
}
