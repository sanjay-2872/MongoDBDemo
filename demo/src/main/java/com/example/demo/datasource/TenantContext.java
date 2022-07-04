package com.example.demo.datasource;

public class TenantContext {
	
	private static ThreadLocal<String> currentTenant = new InheritableThreadLocal<>();
	private static ThreadLocal<String> currentUser = new InheritableThreadLocal<>();

	public static String getCurrentTenant() {
		return currentTenant.get();
	}

	public static void setCurrentTenant(String tenant) {
		currentTenant.set(tenant);
	}
	
	public static String getCurrentUser() {
		return currentUser.get();
	}

	public static void setCurrentUser(String user) {
		currentUser.set(user);
	}

	public static void clear() {
		currentTenant.remove();
		currentUser.remove();
	}
}
