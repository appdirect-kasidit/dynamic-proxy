package com.appdirect.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.http.HttpRequest;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ApiRequest {
	public String method() default "GET";
	public String path() default "/";
}
