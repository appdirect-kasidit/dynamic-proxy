package com.appdirect.java;

import java.util.List;

import com.appdirect.common.Subscription;

@ApiInterface
public interface SubscriptionApi {
	@ApiRequest
	Subscription findById(String uuid);

	@ApiRequest(path = "/list")
	List<Subscription> findAll();

	@ApiRequest(path = "/create", method = "POST")
	Subscription create();
}
