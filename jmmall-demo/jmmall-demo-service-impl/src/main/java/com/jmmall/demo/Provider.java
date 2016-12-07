package com.jmmall.demo;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 这个类没啥用，只是用来告诉你如何不用dubbo的main方法将项目运行起来
 * 
 * @author Jacarri
 * 
 */
@Deprecated
public class Provider {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });
		context.start();
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		} // 按任意键退出
	}
}
