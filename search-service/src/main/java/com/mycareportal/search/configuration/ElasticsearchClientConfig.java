package com.mycareportal.search.configuration;

import java.io.IOException;

import org.apache.http.HttpHost;
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
		// Step 1: Create the RestClient (used for HTTP communication)
		RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();

		// Step 2: Set up the RestClientTransport to enable communication with
		// Elasticsearch
		RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

		// Step 3: Initialize and return the ElasticsearchClient
		return new ElasticsearchClient(transport);
	}



	// Method to close the client and clean up resources
	static void closeClient() throws IOException {
		if (client != null) {
			client._transport().close();
		}
	}
}