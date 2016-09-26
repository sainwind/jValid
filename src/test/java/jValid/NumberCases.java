package jValid;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import jValid.mod.Cup;
import jValid.mod.Product;

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
	
	//----------------比较验证------------------------------------------------
	/**
	 * < <=> V 小于
	 * > <=> A 大于
	 * <= <=> W 小于等于
	 * >= <=> M 大于等于
	 * 把字母顺时针旋转90，就可以大概猜到什么符号了
	 * 或者把符号逆时针旋转90度，就知道是哪个字母了
	 */
	@Test
	public void compare1(){
		//字符串的ok
		//1.日期大小 compare:2016-09-09<colx
		//2.时间大小 compare:09:10:11<colx
		//3.日期时间大小 compare:2016-09-09 09:10:11<colx
		//4.金额大小compare:100<10_0000
		Cup cup1 = new Cup("22:08:35", "22:07:35");//只存时分秒
		Cup cup2 = new Cup("2016-09-09", "2016-09-08");//只存年月日
		Cup cup3 = new Cup("2016-09-19 22:08:35", "2016-09-08 22:07:35");//只存时分秒
		Cup cup4 = new Cup("120.36", "110.36");//只存时分秒
		
		String cfgs1 = "{name:'required', brand:'required;compare:V,name'}";//要求brand<name
		String cfgs2 = "{name:'required', brand:'required;compare:W,name'}";//要求brand<=name
		assertEquals(true, Jvalidator.check(cup1, cfgs1));
		assertEquals(true, Jvalidator.check(cup2, cfgs2));
		
		String cfgs3 = "{name:'required', brand:'required;compare:A,name'}";//要求brand>name
		String cfgs4 = "{name:'required', brand:'required;compare:M,name'}";//要求brand>=name
		assertEquals(false, Jvalidator.check(cup3, cfgs3));
		assertEquals(false, Jvalidator.check(cup4, cfgs4));
	}
	
	@Test
	public void compare2(){
		//字符串的ok
		//1.时间大小 compare:09:10:11<colx
		//2.日期大小 compare:2016-09-09<colx
		//3.日期时间大小 compare:2016-09-09 09:10:11<colx
		//4.金额大小compare:100<100000
		
		//时间大小
		Product p1 = new Product(new Time(System.currentTimeMillis()), new Time((10000+System.currentTimeMillis())));
		String cfgs1 = "{createTime:'required', updateTime:'required;compare:A,createTime'}";//时间要求updateTime>createTime
		assertEquals(true, Jvalidator.check(p1, cfgs1));
		
		//日期大小
		Product p2 = new Product(new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
		String cfgs2 = "{createdm:'required', updatedm:'required;compare:M,createdm'}";//时间要求updateDate>=createDate,可以等于
		assertEquals(true, Jvalidator.check(p2, cfgs2));
		
		Product p3 = new Product(new Timestamp(System.currentTimeMillis()), new Timestamp((10000+System.currentTimeMillis())));
		String cfgs3 = "{createdtm:'required', updatedtm:'required;compare:V,createdtm'}";//时间要求brand<name
		assertEquals(false, Jvalidator.check(p3, cfgs3));
		
		Product p4 = new Product(new BigDecimal("100"), new BigDecimal("50"));
//		Product p4 = new Product(new BigDecimal("100"), new BigDecimal("100"));
		String cfgs4 = "{amount:'required', price:'required;compare:W,amount'}";//时间要求brand<name
		assertEquals(true, Jvalidator.check(p4, cfgs4));
		
	}
	
}
