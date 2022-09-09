package com.appdirect.bytebuddy;

import java.util.List;

import com.appdirect.common.Subscription;

public interface SubscriptionApi {
	Subscription findById(String uuid);
	List<Subscription> findAll();
	Subscription create();
}
