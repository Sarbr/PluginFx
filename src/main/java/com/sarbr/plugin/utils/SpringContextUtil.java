package com.sarbr.plugin.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * 提供spring对bean的一些获取函数
 */
public class SpringContextUtil{

	/**
	 * Spring应用上下文环境
	 */
	private static ApplicationContext applicationContext;

	/**
	 * 通过类获取
	 *
	 * @param clazz 注入的类
	 * @param <T> 若存在多个返回值需要使用下面的name获取
	 * @return 返回这个bean
	 * @throws BeansException bean异常
	 */
	public static <T> T getBean(Class<T> clazz) throws BeansException {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 通过名字获取
	 *
	 * @param name 名字
	 * @param <T>  返回类型
	 * @return 返回这个bean
	 * @throws BeansException bean异常
	 */
	public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
	}

	/**
	 * 重写并初始化上下文
	 *
	 * @param applicationContext 应用上下文
	 * @throws BeansException bean异常
	 */
	public static void setApplicationContext(ApplicationContext applicationContext){
		// 初始化applicationContext
		SpringContextUtil.applicationContext = applicationContext;
	}
}