package com.appdirect.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.core.annotation.AnnotationUtils;

import com.appdirect.common.Subscription;
import io.vavr.control.Try;

public class JavaDynamicProxy {
	public static void main(String[] args) {
		SubscriptionApi api = (SubscriptionApi) Proxy.newProxyInstance(
				SubscriptionApi.class.getClassLoader(),
				new Class[] { SubscriptionApi.class },
				new DynamicProxyProcessor());

		Subscription response = api.findById(UUID.randomUUID().toString());
		System.out.println("FindById: " + response);
		System.out.println();

		List<Subscription> response2 = api.findAll();
		System.out.println("FindAll: " + response2.toString());
		System.out.println();

		Subscription response3 = api.create();
		System.out.println("Create: " + response3);
		System.out.println();
	}
}

class DynamicProxyProcessor implements InvocationHandler {
	private MockAPI mockAPI = new MockAPI();

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		LocalDateTime start = LocalDateTime.now();
		try {
			ApiInterface annotation = AnnotationUtils.findAnnotation(proxy.getClass(), ApiInterface.class);
			if (annotation != null) {
				ApiRequest apiRequest = AnnotationUtils.findAnnotation(method, ApiRequest.class);
				if (apiRequest != null) {
					return this.mockAPI.invoke(apiRequest.path(), apiRequest.method(), args);
				}

				return null;
			}

			throw new IllegalArgumentException("Provided class cannot be used with this invocation handler");
		} finally {
			LocalDateTime end = LocalDateTime.now();
			long millis = start.until(end, ChronoUnit.MILLIS);
			System.out.println("Method " + method.getName() + " takes " + millis + " milliseconds to complete");
		}
	}
}

class MockAPI {
	public Object invoke(String path, String method, Object[] args) {
		// Random sleep
		Try.of(() -> {
			Thread.sleep(ThreadLocalRandom.current().nextLong(500, 3000));
			return 1;
		});

		if ("/".equals(path) && "GET".equals(method)) {
			return Optional.ofNullable(args[0])
					.map(id -> Subscription.builder()
							.id(id.toString())
							.build())
					.orElse(null);
		}

		if ("/list".equals(path) && "GET".equals(method)) {
			List<Subscription> result = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				result.add(Subscription.builder().id(UUID.randomUUID().toString()).build());
			}
			return result;
		}

		if ("/create".equals(path) && "POST".equals(method)) {
			return Subscription.builder().id(UUID.randomUUID().toString()).build();
		}

		return null;
	}
}
