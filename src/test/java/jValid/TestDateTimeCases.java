package jValid;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import jValid.mod.Cup;
import jValid.mod.Product;

import org.junit.Test;
import org.vector.jv.vj.base.VliCore;

/**
 * 日期的校验：基本格式的验证+日期数据有效性的验证
 * 
 * 时分秒的校验：基本格式的验证+日期数据有效性的验证
 * 
 * 日期时分秒的校验：基本格式的验证+日期数据有效性的验证
 */
public class TestDateTimeCases {
	
	@Test
	public void date1(){
//		1.date1 - 必填，格式：yyyy-MM-dd
		Cup cup = new Cup("塑料布", new Date(System.currentTimeMillis()));//只存年月日
		String cfgs = "{name:'required', createDate:'required;date'}";
		assertEquals(true, VliCore.check(cup, cfgs));
	}
	
	@Test
	public void date2(){
		//不正确的日期,月份：01-12，不能更大，日期也和月份相匹配，包括闰年
		Cup cup2 = new Cup("塑料布", "2016-13-42");//只存年月日
		String cfgs2 = "{name:'required', brand:'required;date'}";
		assertEquals(false, VliCore.check(cup2, cfgs2));
	}
	
	@Test
	public void date3(){
		//不正确的日期,日期：01-31，不能更大，日期也和月份相匹配，包括闰年
		Cup cup2 = new Cup("塑料布", "2016-09-32");//只存年月日
		String cfgs2 = "{name:'required', brand:'required;date'}";
		assertEquals(false, VliCore.check(cup2, cfgs2));
	}
	
	@Test
	public void date4(){
		//平年，闰年2月日期最大值28,29
		Cup cup1 = new Cup("塑料布", "2015-02-29");//只存年月日
		Cup cup2 = new Cup("塑料布", "2016-02-30");//只存年月日
		String cfgs2 = "{name:'required', brand:'required;date'}";
		assertEquals(false, VliCore.check(cup1, cfgs2));
		assertEquals(false, VliCore.check(cup2, cfgs2));
	}
	
	@Test
	public void date5(){
		//平年，闰年2月日期最大值28,29
		Cup cup1 = new Cup("塑料布", "2015-02-28");//只存年月日
		Cup cup2 = new Cup("塑料布", "2016-02-29");//只存年月日
		String cfgs2 = "{name:'required', brand:'required;date'}";
		assertEquals(true, VliCore.check(cup1, cfgs2));
		assertEquals(true, VliCore.check(cup2, cfgs2));
	}
	
//	^([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$
	@Test
	public void time1(){
		Product p = new Product(2, new Time(System.currentTimeMillis()));
		String cfgs2 = "{id:'required', createTime:'required;time'}";
		assertEquals(true, VliCore.check(p, cfgs2));
	}
	
	@Test
	public void time2(){
		//不正确的时间,小时：01-23，不能更大
		Cup cup2 = new Cup("塑料布", "25:07:30");//只存时分秒
		String cfgs2 = "{name:'required', brand:'required;time'}";
		assertEquals(false, VliCore.check(cup2, cfgs2));
	}
	
	@Test
	public void time3(){
		//不正确的时间,分钟：01-59，不能更大
		Cup cup1 = new Cup("塑料布", "11:60:30");//只存时分秒
		Cup cup2 = new Cup("塑料布", "11:59:30");//只存时分秒
		String cfgs2 = "{name:'required', brand:'required;time'}";
		assertEquals(false, VliCore.check(cup1, cfgs2));
		assertEquals(true, VliCore.check(cup2, cfgs2));
	}
	
	@Test
	public void time4(){
		//不正确的时间,秒数：01-59，不能更大
		Cup cup1 = new Cup("塑料布", "11:07:60");//只存时分秒
		Cup cup2 = new Cup("塑料布", "11:07:30");//只存时分秒
		String cfgs = "{name:'required', brand:'required;time'}";
		assertEquals(false, VliCore.check(cup1, cfgs));
		assertEquals(true, VliCore.check(cup2, cfgs));
	}
	
	@Test
	public void datetime1(){
		//日期时间
		Product p = new Product(2, new java.util.Date());
		String cfgs1 = "{id:'required', createDate:'required;datetime'}";
		assertEquals(true, VliCore.check(p, cfgs1));
	}
	
	@Test
	public void datetime2(){
		//不正确的日期时间，虽然后面多了3位，但是，它是合法的日期时间，就判断为合法
		Cup cup1 = new Cup("塑料布", "2016-09-26 25:59:30:333");//只存时分秒
		String cfgs2 = "{name:'required', brand:'required;datetime'}";
		assertEquals(false, VliCore.check(cup1, cfgs2));
	}
	
	@Test
	public void timestamp1(){
		//时间戳
		Product p2 = new Product(2, new Timestamp(System.currentTimeMillis()));
		String cfgs2 = "{id:'required', createDate:'required;timestamp'}";
		assertEquals(true, VliCore.check(p2, cfgs2));
	}
	
	@Test
	public void timestamp2(){
		//时间戳,因为没有后面的3位，所以是不能匹配的
		Cup cup1 = new Cup("塑料布", "2016-09-26 25:59:30");//只存时分秒
		String cfgs2 = "{name:'required', brand:'required;timestamp'}";
		assertEquals(false, VliCore.check(cup1, cfgs2));
	}
}
