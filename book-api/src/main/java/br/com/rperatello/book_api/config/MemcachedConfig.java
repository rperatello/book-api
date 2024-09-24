package br.com.rperatello.book_api.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.spy.memcached.spring.MemcachedClientFactoryBean;

@Configuration
public class MemcachedConfig {

	@Bean
	public MemcachedClientFactoryBean memcachedClientFactoryBean() throws IOException {

		String memcachedHost = System.getenv("MEMCACHED_HOST");

		if (memcachedHost == null || memcachedHost.isEmpty())
			memcachedHost = "127.0.0.1";

		String serverWithPort = String.format("%s:11211", memcachedHost);
		MemcachedClientFactoryBean factoryBean = new MemcachedClientFactoryBean();
		factoryBean.setServers(serverWithPort);
		return factoryBean;

	}
}