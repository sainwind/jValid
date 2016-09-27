package jValid.listv;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import jValid.mod.Cup;

import org.junit.Test;
import org.vector.jv.vj.base.ValidatorJ;

import com.alibaba.fastjson.JSONArray;

/**
 * 集合验证
 */
public class ListStringCases {
	@Test
	public void list1(){
//		1.required - 不能为空
		Cup cup = new Cup();
		Cup cup2 = new Cup("aaa");
		List cs  = new ArrayList();
		cs.add(cup);
		cs.add(cup2);
		String cfgs = "{name:'required'}";
		assertEquals(false, ValidatorJ.valid(cs, cfgs));
	}
	
	//JsonArray的验证：
	@Test
	public void jsonArray(){
//		1.required - 不能为空
		String css = "[{name:'aa'},{name:'bb'},{name:'cc'},{name:''}]";
		JSONArray cs = JSONArray.parseArray(css);
		String cfgs = "{name:'required'}";
		assertEquals(false, ValidatorJ.valid(cs, cfgs));
	}
	
}
