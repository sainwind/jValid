package jValid;

import static org.junit.Assert.assertEquals;
import jValid.mod.Cup;

import org.junit.Test;
import org.vector.jv.vj.base.Jvalidator;

public class NumberCases {

	//----------------整数------------------------------------------------
	@Test
	public void integerAll1(){
//		任意整数
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;integer'}";
		assertEquals(true, Jvalidator.check(cup, cfgs));
		assertEquals(true, Jvalidator.check(cup2, cfgs));
		assertEquals(true, Jvalidator.check(cup3, cfgs));
		
	}
	
	@Test
	public void integerAll2(){
//		-+0<==>任意整数
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;integer:+-0'}";
		assertEquals(true, Jvalidator.check(cup, cfgs));
		assertEquals(true, Jvalidator.check(cup2, cfgs));
		assertEquals(true, Jvalidator.check(cup3, cfgs));
	}
	
	@Test
	public void integer2(){
//		正整数+0
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;integer:+0'}";
		assertEquals(true, Jvalidator.check(cup, cfgs));
		assertEquals(false, Jvalidator.check(cup2, cfgs));
		assertEquals(true, Jvalidator.check(cup3, cfgs));
	}
	
	@Test
	public void integer3(){
//		负整数+0
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;integer:-0'}";
		assertEquals(false, Jvalidator.check(cup, cfgs));
		assertEquals(true, Jvalidator.check(cup2, cfgs));
		assertEquals(true, Jvalidator.check(cup3, cfgs));
	}
	
	@Test
	public void number4(){
//		负整数
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;integer:-'}";
		assertEquals(false, Jvalidator.check(cup, cfgs));
		assertEquals(true, Jvalidator.check(cup2, cfgs));
		assertEquals(false, Jvalidator.check(cup3, cfgs));
	}
	
	@Test
	public void integer5(){
//		正整数
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;integer:+'}";
		assertEquals(true, Jvalidator.check(cup, cfgs));
		assertEquals(false, Jvalidator.check(cup2, cfgs));
		assertEquals(false, Jvalidator.check(cup3, cfgs));
	}
	
	@Test
	public void integer6(){
//		0
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;integer:0'}";
		assertEquals(false, Jvalidator.check(cup, cfgs));
		assertEquals(false, Jvalidator.check(cup2, cfgs));
		assertEquals(true, Jvalidator.check(cup3, cfgs));
	}
	
	//----------------值域range------------------------------------------------
	//range:(1,2) 离散域包括：1和2，()至少有一个值
	//range:[1,2] 范围域包括：1到2之间，包含1,2；[]有且必有2个值,不支持多范围域验证
	//integer:0 <==> range:[0,0]<==>range:(0) 是等价的
	@Test
	public void range1(){
		
//		范围域的验证
		Cup cup = new Cup("123");
		Cup cup2 = new Cup("-123");
		Cup cup3 = new Cup("0");
		String cfgs = "{name:'required;range:[0,50]'}";
		assertEquals(false, Jvalidator.check(cup, cfgs));
		assertEquals(false, Jvalidator.check(cup2, cfgs));
		assertEquals(true, Jvalidator.check(cup3, cfgs));
	}
	
	@Test
	public void range2(){
//		离散域的验证
		Cup cup = new Cup("1");
		Cup cup2 = new Cup("-1");
		Cup cup3 = new Cup("8");
		Cup cup4 = new Cup("3");
		String cfgs = "{name:'required;range:(-2,-1,0,1,2,3)'}";
		assertEquals(true, Jvalidator.check(cup, cfgs));
		assertEquals(true, Jvalidator.check(cup2, cfgs));
		assertEquals(false, Jvalidator.check(cup3, cfgs));
		assertEquals(true, Jvalidator.check(cup4, cfgs));
	}
	
	@Test
	public void range3(){
//		离散域+范围域的验证
		Cup cup = new Cup("1");
		Cup cup2 = new Cup("-1");
		Cup cup3 = new Cup("8");
		Cup cup4 = new Cup("3");
		Cup cup5 = new Cup("50");
		Cup cup6 = new Cup("150");
		String cfgs = "{name:'required;range:(-2,-1,0,1,2,3)[10,100]'}";
		assertEquals(true, Jvalidator.check(cup, cfgs));
		assertEquals(true, Jvalidator.check(cup2, cfgs));
		assertEquals(false, Jvalidator.check(cup3, cfgs));
		assertEquals(true, Jvalidator.check(cup4, cfgs));
		assertEquals(true, Jvalidator.check(cup5, cfgs));
		assertEquals(false, Jvalidator.check(cup6, cfgs));
	}
}
