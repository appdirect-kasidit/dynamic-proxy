package com.appdirect.bytebuddy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.NoArgsConstructor;

import com.appdirect.common.Subscription;

@NoArgsConstructor
public class SubscriptionApiImpl implements SubscriptionApi {
	public Subscription findById(String uuid) {
		return Subscription.builder()
				.id(uuid)
				.build();
	}

	public List<Subscription> findAll() {
		List<Subscription> result = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			result.add(Subscription.builder().id(UUID.randomUUID().toString()).build());
		}
		return result;
	}

	public Subscription create() {
		return Subscription.builder().id(UUID.randomUUID().toString()).build();
	}
}
