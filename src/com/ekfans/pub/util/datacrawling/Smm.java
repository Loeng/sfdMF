package com.ekfans.pub.util.datacrawling;

public enum Smm {
	TOG("铜","https://hq.smm.cn/tong","ff8080815397d464015397dd77fe00a2"),
	LV("铝","https://hq.smm.cn/lv","ff8080815397d464015397dd9ce400a3"),
	QIAN("铅","https://hq.smm.cn/qian","ff8080815397d464015397ddca3e00a4"),
	XIN("铝","https://hq.smm.cn/xin","ff8080815397d464015397dde5cd00a5"),
	XI("锡","https://hq.smm.cn/xi","ff8080815397d464015397de34fc00a6"),
	NIE("镍","https://hq.smm.cn/nie","ff8080815397d464015397de4d2600a7"),
	MENG("锰","https://hq.smm.cn/meng","ff8080815397d464015397de631600a8"),
	GUI("硅","https://hq.smm.cn/gui","ff8080815397d464015397de789300a9"),
	GL("钴锂","https://hq.smm.cn/gl","ff8080815397d464015397de8de500aa"),
	TI("锑","https://hq.smm.cn/ti","ff8080815397d464015397dea13400ab"),
	WU("钨","https://hq.smm.cn/wu","ff8080815397d464015397decf8200ac"),
	YJZ("铟锗镓","https://hq.smm.cn/yjz","ff8080815397d464015397deefbc00ad"),
	BXT("铋硒碲","https://hq.smm.cn/bxt","ff8080815397d464015397df041900ae"),
	XT("稀土","https://hq.smm.cn/xt","ff8080815397d464015397df580a00b2");
	//金属元素品类名称
    private String name;
    //上海有色对应的元素地址
    private String url;
    //三分地环保后台对应的元素ID
    private String id;
	private Smm(String name,String url,String id) {
		this.name=name;
		this.url=url;
		this.id=id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
