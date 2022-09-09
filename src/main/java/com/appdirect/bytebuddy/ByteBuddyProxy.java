package com.appdirect.bytebuddy;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

import java.util.List;
import java.util.UUID;

import com.appdirect.common.Subscription;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;

public class ByteBuddyProxy {
	public static void main(String[] args) throws Exception {
		Class<?> newDefinition = new ByteBuddy()
				.subclass(SubscriptionApiImpl.class)
				.method(isDeclaredBy(SubscriptionApiImpl.class))
				.intercept(MethodDelegation.to(new ClassInterceptor()))
				.make()
				.load(SubscriptionApiImpl.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
				.getLoaded();

		SubscriptionApi api = (SubscriptionApi) newDefinition
				.getDeclaredConstructors()[0]
				.newInstance();

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
