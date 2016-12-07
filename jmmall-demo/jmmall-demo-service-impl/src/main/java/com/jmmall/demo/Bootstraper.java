package com.jmmall.demo;

import com.alibaba.dubbo.container.Main;

/**
 * <pre>
 * 直接调用dubbo的main方法，可以从日志发现它需要对配置文件的路径有要求
 *  Resolved location pattern [classpath*:META-INF/spring/*.xml] to resources []
 * </pre>
 * 
 * 这个类只是用来方便开发时运行项目的，在生产环境时直接使用dubbo的main方法，具体可见当前项目的maven-shade-plugin配置
 * 
 * @author Jacarri
 * 
 */
public class Bootstraper {
	public static void main(String[] args) {
		Main.main(args);
	}
}
