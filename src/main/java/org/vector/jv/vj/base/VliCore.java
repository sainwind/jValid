package org.vector.jv.vj.base;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class VliCore {
	private static boolean required(String val){
		val = val.trim();
		return !("".equals(val) || val.length()==0);
	}
	
	private static boolean email(String email){
		Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配  
        Matcher m = p.matcher(email);  
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
//			
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
	
	public static boolean check(JSONObject obj, JSONObject cfgs){
		boolean result = true;
		
		//然后根据配置校验
		Set<String> keySet = cfgs.keySet();
		Iterator<String> keysIt = keySet.iterator();
		while(keysIt.hasNext()){
			String key = keysIt.next();
			//第一个校验开始
			String option = cfgs.get(key).toString();
			Object attObj = obj.get(key);//要求有值的属性，没有属性在json中的时候
			if(attObj == null){
				return false;
			}
			
			String val = attObj.toString();
			
			if(option.contains(";")){
				String[] options = option.split(";");
				for (int i = 0; i < options.length; i++) {
					result = optionChecker(val, options[i]);
				}
			}else{
				result = optionChecker(val, option);
			}
		}
		
		return result;
	}

	private static boolean optionChecker(String val, String option) {
		boolean result = true;
		
		switch (option) {
			case "required": result = required(val);break;
			case "email": result = email(val);break;
			case "match": result = email(val);break;
			case "range": result = email(val);break;
			case "length": result = email(val);break;
			case "max": result = email(val);break;
			case "min": result = email(val);break;
			default:
				break;
		}
		
		return result;
	}
}
