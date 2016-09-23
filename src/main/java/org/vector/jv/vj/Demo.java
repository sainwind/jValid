package org.vector.jv.vj;


import java.util.Date;

import org.vector.jv.mod.User;
import org.vector.jv.vj.base.VliCore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Demo {
	
	public static void main(String[] args) {
//		test1();
		
//		test2();
	}


	private static void test1() {
//		User user = new User(1, "name", new Date(), "zf126@126.com");
		User user = new User(1, "name", new Date(), "zf126@126.com");
		user.setAge(-1);
		JSONObject jo = (JSONObject) JSON.toJSON(user);
		
		System.out.println("bean json:"+jo.toJSONString());
		//不带参数的只有名字
		//带参数的[ 名字;参数; ]的组合形式，虽然可读性比较差，但是方便程序处理,最后一个;可以省略
		String config = "{name:'required', birth:'required', email:'required;email', age:'required;range;[1,200]' }";
		
		JSONObject cfgs = JSON.parseObject(config);
		
		//1.开始验证直接属性：
		if(!VliCore.check(jo, cfgs)){
			System.out.println("--------not pass---------");
		}else {
			System.out.println("------yes pass-----------");
		}
	}

}
