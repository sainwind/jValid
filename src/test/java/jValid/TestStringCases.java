package jValid;

import static org.junit.Assert.assertEquals;
import jValid.mod.Cup;

import org.junit.Test;
import org.vector.jv.vj.base.VliCore;

/**
 * bean的字符串属性 测试案例
 * @author Administrator
 * 用出错行码追踪的测试用例并解决程序bug
 */
public class TestStringCases {
	
	@Test
	public void required1(){
//		1.required - 不能为空
		Cup cup = new Cup();
		String cfgs = "{name:'required'}";
		assertEquals(false, VliCore.check(cup, cfgs));
	}
	
	@Test
	public void required2(){
//		1.required - 不能为空
		Cup cup = new Cup("塑料杯");
		String cfgs = "{name:'required'}";
		assertEquals(true, VliCore.check(cup, cfgs));
	}
	
	@Test
	public void required3(){
//		1.required - 不能为空
		Cup cup = new Cup("塑料杯");
		String cfgs = "{brand:'required'}";
		assertEquals(false, VliCore.check(cup, cfgs));
	}
	
//	长度-----------------------
	@Test
	public void length1(){
//		1.length - 唯一长度3
		Cup cup1 = new Cup("塑料杯");
		Cup cup2 = new Cup("塑料");
		Cup cup3 = new Cup("塑料杯子");
		String cfgs = "{name:'required;length:3'}";
		assertEquals(true, VliCore.check(cup1, cfgs));
		assertEquals(false, VliCore.check(cup2, cfgs));
		assertEquals(false, VliCore.check(cup3, cfgs));
	}
	
	@Test
	public void length2(){
//		1.length - 最少3个
		Cup cup1 = new Cup("塑料杯");
		Cup cup2 = new Cup("塑料");
		Cup cup3 = new Cup("塑料杯子");
		String cfgs = "{name:'required;length:3,0'}";
		assertEquals(true, VliCore.check(cup1, cfgs));
		assertEquals(false, VliCore.check(cup2, cfgs));
		assertEquals(true, VliCore.check(cup3, cfgs));
	}
	
	@Test
	public void length3(){
//		1.length - 不能多于6个
		Cup cup1 = new Cup("塑料杯塑料杯料杯塑料杯");
		Cup cup2 = new Cup("塑料杯塑料杯");
		Cup cup3 = new Cup("塑料杯塑料");
		String cfgs = "{name:'required;length:0,6'}";
		assertEquals(false, VliCore.check(cup1, cfgs));//11<=6
		assertEquals(true, VliCore.check(cup2, cfgs));//6<=6
		assertEquals(true, VliCore.check(cup3, cfgs));//5<=6
	}
	
	@Test
	public void email4(){
//		email - email格式验证
		Cup cup1 = new Cup("塑料杯", "s23emaa@162.com");
		Cup cup2 = new Cup("塑料杯", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa@162.com");
		Cup cup3 = new Cup("塑料杯", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa2@162.com");
		String cfgs = "{brand:'required;length:0,36;email'}";
//		String cfgs = "{name:'required', brand:'required;length:0,36;email'}";
//		assertEquals(true, VliCore.check(cup1, cfgs));
		assertEquals(false, VliCore.check(cup2, cfgs));//每个规则的校验结束都要判断，一旦false就立即返回验证结果
//		assertEquals(false, VliCore.check(cup3, cfgs));
	}
	
}
