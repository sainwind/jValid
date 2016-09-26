package jValid;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vector.jv.vj.WordDealUtil;

public class TestDemoCases {
	// 1.required
	@Before
	public void init(){
		System.out.println("init..");
	}
	
	@After
	public void destroy(){
		System.out.println("destroy..");
	}
	
//	@Test
	public void strReq() {
		String target = "employeeInfow";
		String result = WordDealUtil.wordFormat4DB(target);

		//成功的处理结果应该是怎么样的，如果和期望的一致则方法成功，否则失败
		assertEquals("employee_info", result);
	}
	
	@Test
	public void name() {
		String a = "1";
		String b = "2";
		String c = "3";
		//compareTo方法的含义就是大于号
		System.out.println((new BigDecimal(b).compareTo(new BigDecimal(a))));
//		new BigDecimal(varr[0]).compareTo(new BigDecimal(val))>0 && new BigDecimal(val).compareTo(new BigDecimal(varr[1]))>0
	}
	
	@Test
	public void str() {
		String a = "[0,50]";
		System.out.println(a.substring(a.indexOf("[")+1, a.indexOf("]")));
	}

}
