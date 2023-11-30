package com.longqin.business.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

	/**
	 * @Description 方法名
	 */
	String methodName() default "";

	/**
	 * @Description 标题
	 */
	String title() default "";

	/**
	 * @Description 操作描述，内容
	 */
	String content() default "";

	/**
	 * @Description 操作类型： 增-0;删 -1;改-2；启用-3；停用-4；请求-5；响应-6；设置-7
	 **/
	String operationType() default "7";
}
