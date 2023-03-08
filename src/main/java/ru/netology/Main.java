package ru.netology;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Main {

	public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=oQDtnAuzycBqHCxHprBLffwZEKm1DakJz5z3n2XE";

	public static ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setUserAgent("My Test Service")
				.setDefaultRequestConfig(RequestConfig.custom()
						.setConnectTimeout(5000) // максимальное время ожидания подключения к серверу
						.setSocketTimeout(3000) // максимальное время ожидания получения данных
						.setRedirectsEnabled(false) // возможность следовать редиректу в ответе
						.build())
				.build();
		// создание объекта запроса с произвольными заголовками
		HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
		request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		// отправка запроса
		CloseableHttpResponse response = httpClient.execute(request);
		// вывод полученных загловков
		Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

		List<NasaMediaResource> nasaMediaResources = mapper.readValue(
				response.getEntity().getContent(),
				new TypeReference<>() {
				});

		// Stream<NasaMediaResource> stream = facts.stream();
		// stream.filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
		// .forEach(System.out::println);;
		nasaMediaResources.forEach(System.out::println);

		response.close();
		httpClient.close();
	}
}
