package org.vector.jv.vj.base;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class VliCore {
	private static boolean required(String val){
		val = val.trim();
		return !("".equals(val) || val.length()==0);
	}
	
	private static boolean email(String val){
        return pattern(val, "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");  
	}
	
//号码段：
//	移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
//　联通：130、131、132、152、155、156、185、186
//　电信：133、153、180、189、（1349卫通）
//	"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
//	前3位属于特殊情况，后8位任意
	private static boolean mobile(String val){
		System.out.println("val = "+val);
		return pattern(val, "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
	}
	
	private static boolean pattern(String val, String rule){
		Pattern p =  Pattern.compile(rule);//正则匹配
		Matcher m = p.matcher(val);
		return m.matches();
	}
	
	// 6 length(String val, 6, 6)
	//100以内含100 length(String val, 0, 100)
	// 3个以上 length(String val, 3, 0)
	// 4-8 [包含4，8],等价于不包含的[5,9]length(String val, 5, 7)
	private static boolean length(String val, int len1, int len2){
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
	
	// 1, 0, 0  数字验证
	// 3, 5, 0  数字+最小值验证
	// 3, 0, 100  数字+最大值验证
	// 3, 1, 100  数字+最值范围验证
	private static boolean number(String val, long min, long max){
		//1.肯定数字验证：
		Pattern pattern = Pattern.compile("-[0-9]*");
        Matcher match=pattern.matcher(val);
        if(!match.matches()){
        	return false;
        }
//		if(){
//			//-?[0-9]+.?[0-9]+
//		}
		
		//继续范围
		if(min==0 && max!=0){
			
		}
		if(min!=0 && max==0){
			
		}
		if(min!=0 && max!=0){
			
		}
		
		return true;
	}
	
//	private static boolean min(long val, long min){
//		return val >= min;
//	}
//	private static boolean max(long val, long max){
//		return val <= max;
//	}
	
	/**
	 * 相等 
	 */
	private static boolean match(long val, long max){
		return val == max;
	}
	/**
	 * 相等 
	 */
	private static boolean match(String val1, String val2){
		if(required(val1) && required(val2)){
			return val1.equals(val2);
		}
		return false;
	}
	
	private static boolean match(BigDecimal val1, BigDecimal val2){
		if(required(val1.toString()) && required(val2.toString())){
			return val1.compareTo(val2)==0;
		}
		return false;
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
			
			String val = attObj.toString();
			
			if(option.contains(";")){//多校验
				String[] options = option.split(";");//required;length:0,6
				
				for (int i = 0; i < options.length; i++) {
					if(options[i].contains(":")){
						String[] temp = options[i].split(":");
						result = optionArgsChecker(val, temp[0], temp[1]);
					}else{
						result = optionChecker(val, options[i]);
					}
					
					if(!result){//一旦校验到false就返回
						return result;
					}
				}
				
			}else{//单校验
				if(option.contains(":")){
					String[] temp = option.split(":");
					result = optionArgsChecker(val, temp[0], temp[1]);
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

	private static boolean optionArgsChecker(String val, String option, String args) {
		boolean result = false;
		
		switch (option) {
			case "length": result=lenCheck(val, args); break;
			case "number": break;
			case "pattern": result = pattern(val, args);break;
		}
		return result;
	}

	private static boolean lenCheck(String val, String args) {
		if(args.contains(",")){
			String[] arr = args.split(",");
			return length(val, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
		}
		return length(val, Integer.parseInt(args), Integer.parseInt(args));
	}

	private static boolean optionChecker(String val, String option) {
		boolean result = false;
		
		switch (option) {
			case "required": result = required(val);break;
			case "email": result = email(val);break;
			case "mobile": result = mobile(val);break;
			//TODO to finish
			case "match": result = email(val);break;
			case "range": result = email(val);break;
			case "length": result = email(val);break;
			case "max": result = email(val);break;
			case "min": result = email(val);break;
		}
		
		return result;
	}
}
