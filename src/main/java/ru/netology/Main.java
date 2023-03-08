package ru.netology;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.net.URL;
import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.awt.Dimension;
import java.awt.image.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class Main {

	public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=oQDtnAuzycBqHCxHprBLffwZEKm1DakJz5z3n2XE";

	public static ObjectMapper mapper = new ObjectMapper();

	public static void downloadFile(URL url, String fileName) throws IOException {
		FileUtils.copyURLToFile(url, new File("fileName.jpg"));
	}

	public static void main(String[] args) throws IOException {

		ImageLoad appwin = new ImageLoad();

		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setUserAgent("My Test Service")
				.setDefaultRequestConfig(RequestConfig.custom()
						.setConnectTimeout(5000) // максимальное время ожидания подключения к серверу
						.setSocketTimeout(3000) // максимальное время ожидания получения данных
						.setRedirectsEnabled(false) // возможность следовать редиректу в ответе
						.build())
				.build();
		// создание объекта запроса с произвольными заголовками
		HttpGet request1 = new HttpGet(REMOTE_SERVICE_URI);
		request1.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		// отправка запроса
		CloseableHttpResponse response1 = httpClient.execute(request1);

		// вывод полученных загловков
		Arrays.stream(response1.getAllHeaders()).forEach(System.out::println);

		String body = new String(response1.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
		NasaMediaResource nasaMediaResource = mapper.readValue(body, NasaMediaResource.class);

		String url = nasaMediaResource.getUrl();

		URL url2 = new URL(url);

		//java.net.Url urlObj = new java.net.Url(url2);
		String urlPath = url2.getPath();
		String fileName = urlPath.substring(urlPath.lastIndexOf('/') + 1);
		System.out.println(fileName);

		downloadFile(url2, "fileName.jpg");

		appwin.setSize(new Dimension(400, 365));
		appwin.setTitle("LoadImageLoad");
		appwin.setVisible(true);
		// System.out.println(url);

		// System.out.println();

		// HttpGet request2 = new HttpGet(url);
		// request2.setHeader(HttpHeaders.ACCEPT,
		// ContentType.APPLICATION_JSON.getMimeType());

		// CloseableHttpResponse response2 = httpClient.execute(request2);

		// Arrays.stream(response2.getAllHeaders()).forEach(System.out::println);

		// System.out.println(url);
		// List<NasaMediaResource> nasaMediaResources = mapper.readValue(
		// response.getEntity().getContent(),
		// new TypeReference<>() {
		// });

		// Stream<NasaMediaResource> stream = facts.stream();
		// stream.filter(value -> value.getUpvotes() != null && value.getUpvotes() > 0)
		// .forEach(System.out::println);;
		// nasaMediaResources.forEach(System.out::println);

		response1.close();
		// response2.close();
		httpClient.close();
	}
}
