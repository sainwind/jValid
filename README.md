#jValid

##一个类搞定javabean的验证【依赖fastjson】

java bean的校验，list<object>的校验，JsonArray的校验【1个类搞定后端验证，依赖fastjson】

配置说明：json对象配置的方式，以{属性名:规则}的格式配置，多个规则之间使用";"分隔如：{属性名:规则1；规则2}

####通用规则

* 必填验证required："{email:required;email}"

* 可选验证，去掉required规则即可："{email:email}"

####字符串的规则

* pattern："{mobile:\"required;pattern:\^((13[0-9])|(15[\^4,\\\\\\\\D])|(18[0,5-9]))\\\\\\\\d{8}$\"}"

* length："{mobile:required;length:11}"

备注：有pattern规则的时候，需要将所有规则用双引号引起来，如上，否则内部解析会有问题，还有\\\\需要写为\\\\\\\\

***

####日期时间规则

* 日期："{createDate:'required;date'}"

* 时间："{createTime:'required;time'}"

* 日期时间："{createDate:'required;datetime'}"

* 时间戳："{createDate:'required;timestamp'}"

***

####integer规则

* 全部整数："{age:'required;integer'}"或"{name:'required;integer:+0-'}"

* 正整数："{age:'required;integer:+'}"

* 负整数："{age:'required;integer:-'}"

* 0和正整数："{age:'required;integer:0+'}"

* 0和负整数："{age:'required;integer:0-'}"

***

####range规则

* 范围域，0-50的闭区间：{amount:'required;range:[0,50]'}

* 离散域[类似枚举]：{amount:'required;range:(-2,-1,0,1,2,3)'}

* 范围域+离散域：{amount:'required;range:(-2,-1,0,1,2,3)[10,100]'}

***

####compare规则

* 小于："{createTime:'required', updateTime:'required;compare:<,createTime'}"

* 大于："{createTime:'required', updateTime:'required;compare:>,createTime'}"

* 小于等于："{createTime:'required', updateTime:'required;compare:<=,createTime'}"

* 大于等于："{createTime:'required', updateTime:'required;compare:>=,createTime'}"

备注：支持字符串的大小，日期，时间，日期时间，数字的大小，金额的大小比较