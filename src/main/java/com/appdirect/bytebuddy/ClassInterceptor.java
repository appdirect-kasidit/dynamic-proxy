package com.appdirect.bytebuddy;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

import lombok.NoArgsConstructor;

import io.vavr.control.Try;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;

@NoArgsConstructor
public class ClassInterceptor {
	@RuntimeType
	@BindingPriority(Integer.MAX_VALUE)
	public Object intercept(
			@SuperMethod Method method,
			@SuperCall Callable<?> callable
	) throws Exception {
		LocalDateTime start = LocalDateTime.now();
		try {
			// Random sleep
			Try.of(() -> {
				Thread.sleep(ThreadLocalRandom.current().nextLong(500, 3000));
				return 1;
			});

			return callable.call();
		} finally {
			LocalDateTime end = LocalDateTime.now();
			long millis = start.until(end, ChronoUnit.MILLIS);
			System.out.println("Method " + method.getName() + " takes " + millis + " milliseconds to complete");
		}
	}
}
