package jValid.beanv;

import static org.junit.Assert.assertEquals;
import jValid.mod.Cup;

import org.junit.Test;
import org.vector.jv.vj.base.ValidatorJ;

/**
 * bean的字符串属性 测试案例
 * @author Administrator
 * 用出错行码追踪的测试用例并解决程序bug
 */
public class StringCases {
	
	@Test
	public void required1(){
//		1.required - 不能为空
		Cup cup = new Cup();
		String cfgs = "{name:'required'}";
		assertEquals(false, ValidatorJ.valid(cup, cfgs));
	}
	
	@Test
	public void option(){
//可选的不否和规则结果
		Cup cup0 = new Cup("sfmjp163.com");
		String cfgs = "{name:'email'}";
		assertEquals(false, ValidatorJ.valid(cup0, cfgs));
		
//		可选的
		Cup cup = new Cup("sfmjp@163.com");
		assertEquals(true, ValidatorJ.valid(cup, cfgs));
		
		//空字符串或者null，应该是pass的，也就是true
		Cup cup2 = new Cup("");
		assertEquals(true, ValidatorJ.valid(cup2, cfgs));
		
		Cup cup3 = new Cup();
		assertEquals(true, ValidatorJ.valid(cup3, cfgs));
	}
	
	@Test
	public void required1x(){
//		末尾的;可有可没有
		Cup cup = new Cup();
		String cfgs = "{name:'required;'}";
		assertEquals(false, ValidatorJ.valid(cup, cfgs));
	}
	
	@Test
	public void required2(){
//		1.required - 不能为空
		Cup cup = new Cup("塑料杯");
		String cfgs = "{name:'required'}";
		assertEquals(true, ValidatorJ.valid(cup, cfgs));
	}
	
	@Test
	public void required3(){
//		1.required - 不能为空
		Cup cup = new Cup("塑料杯");
		String cfgs = "{brand:'required'}";
		assertEquals(false, ValidatorJ.valid(cup, cfgs));
	}
	
//	长度-----------------------
	@Test
	public void length1(){
//		1.length - 唯一长度3
		Cup cup1 = new Cup("塑料杯");
		Cup cup2 = new Cup("塑料");
		Cup cup3 = new Cup("塑料杯子");
		String cfgs = "{name:'required;length:3'}";
		assertEquals(true, ValidatorJ.valid(cup1, cfgs));
		assertEquals(false, ValidatorJ.valid(cup2, cfgs));
		assertEquals(false, ValidatorJ.valid(cup3, cfgs));
	}
	
	@Test
	public void length2(){
//		1.length - 最少3个
		Cup cup1 = new Cup("塑料杯");
		Cup cup2 = new Cup("塑料");
		Cup cup3 = new Cup("塑料杯子");
		String cfgs = "{name:'required;length:3,0'}";
		assertEquals(true, ValidatorJ.valid(cup1, cfgs));
		assertEquals(false, ValidatorJ.valid(cup2, cfgs));
		assertEquals(true, ValidatorJ.valid(cup3, cfgs));
	}
	
	@Test
	public void length3(){
//		1.length - 不能多于6个
		Cup cup1 = new Cup("塑料杯塑料杯料杯塑料杯");
		Cup cup2 = new Cup("塑料杯塑料杯");
		Cup cup3 = new Cup("塑料杯塑料");
		String cfgs = "{name:'required;length:0,6'}";
		assertEquals(false, ValidatorJ.valid(cup1, cfgs));//11<=6
		assertEquals(true, ValidatorJ.valid(cup2, cfgs));//6<=6
		assertEquals(true, ValidatorJ.valid(cup3, cfgs));//5<=6
	}
	
	@Test
	public void email4(){
//		email - email格式验证
//		Cup cup1 = new Cup("塑料杯", "s23emaa@162.com");
		Cup cup2 = new Cup("塑料杯", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa@162.com");
//		Cup cup3 = new Cup("塑料杯", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa2@162.com");
		String cfgs = "{brand:'required;length:0,36;email'}";
//		String cfgs = "{name:'required', brand:'required;length:0,36;email'}";
//		assertEquals(true, VliCore.valid(cup1, cfgs));
		assertEquals(false, ValidatorJ.valid(cup2, cfgs));//每个规则的校验结束都要判断，一旦false就立即返回验证结果
//		assertEquals(false, VliCore.valid(cup3, cfgs));
	}
	
	@Test
	public void mobile(){
//		mobile - mobile格式验证
		Cup cup1 = new Cup("塑料杯", "13895065269");
		Cup cup2 = new Cup("塑料杯", "14895065236");
		String cfgs = "{brand:'required;length:11;mobile'}";
		
		assertEquals(true, ValidatorJ.valid(cup1, cfgs));
		assertEquals(false, ValidatorJ.valid(cup2, cfgs));//每个规则的校验结束都要判断，一旦false就立即返回验证结果
	}
	
	@Test
	public void pattern(){
//		mobile - mobile格式验证
		Cup cup1 = new Cup("塑料杯", "13895065269");
		Cup cup2 = new Cup("塑料杯", "14895065236");
		//正则的写法有点特殊：本该是\\的就要写4个\,需要注意
		String cfgs = "{brand:\"required;length:11;pattern:^((13[0-9])|(15[^4,\\\\D])|(18[0,5-9]))\\\\d{8}$\"}";
		
		assertEquals(true, ValidatorJ.valid(cup1, cfgs));
		assertEquals(false, ValidatorJ.valid(cup2, cfgs));//每个规则的校验结束都要判断，一旦false就立即返回验证结果
	}
	
}
