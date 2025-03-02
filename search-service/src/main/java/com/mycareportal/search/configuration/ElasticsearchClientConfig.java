package com.mycareportal.search.configuration;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ElasticsearchClientConfig {

	private static ElasticsearchClient client;

	// Define the ElasticsearchClient as a Spring Bean
	@Bean
	ElasticsearchClient elasticsearchClient() {
		// Cấu hình thông tin xác thực
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new org.apache.http.auth.AuthScope("localhost", 9200),
				new UsernamePasswordCredentials("elastic", "elasticsearch") // username và password
		);

		// Tạo HttpClient với thông tin xác thực
		RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http"))
				.setHttpClientConfigCallback(
						httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
				.build();

		// Set up the RestClientTransport to enable communication with
		// Elasticsearch
		RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		// Step 4: Initialize and return the ElasticsearchClient
		return new ElasticsearchClient(transport);
	}

	// Method to close the client and clean up resources
	static void closeClient() throws IOException {
		if (client != null) {
			client._transport().close();
		}
	}
}