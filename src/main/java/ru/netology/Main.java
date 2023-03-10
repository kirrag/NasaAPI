package ru.netology;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.commons.io.FilenameUtils;

import java.awt.Dimension;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import javax.swing.*;

public class Main {

	public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=oQDtnAuzycBqHCxHprBLffwZEKm1DakJz5z3n2XE";

	public static ObjectMapper mapper = new ObjectMapper();

	private static void downloadFile(String urlString, String fileName) throws IOException {
        
		URL url = new URL(urlString);
              
        BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        
        byte[] buf = new byte[2048];
        int counter = 0;
        
        while ((counter = bufferedInputStream.read(buf, 0, 1024)) != -1) {
            fileOutputStream.write(buf, 0, counter);
        }
        
        fileOutputStream.close();
        bufferedInputStream.close();
        
        System.out.println("File Downloaded Successfully with Java Stream Operation \n");
    }
	public static void viewFile(String fileName) {

		// Открыть сохраненный файл в буффер
		BufferedImage img = null;
		
		try {
    		img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		
		// Открыть изображение из буфера
		JLabel picLabel = new JLabel(new ImageIcon(img));
		JPanel jPanel = new JPanel();
		jPanel.add(picLabel);
		JFrame f = new JFrame();
		f.setSize(new Dimension(img.getWidth(), img.getHeight()));
		f.add(jPanel);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

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
		HttpGet request1 = new HttpGet(REMOTE_SERVICE_URI);
		request1.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		// отправка запроса
		CloseableHttpResponse response1 = httpClient.execute(request1);

		String body = new String(response1.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
		NasaMediaResource nasaMediaResource = mapper.readValue(body, NasaMediaResource.class);

		String url = nasaMediaResource.getUrl();
		
		// Выделение имени файла из url
        String fileName = FilenameUtils.getName(url);

		// Скачать файл
		downloadFile(url, fileName);

		// Открыть файл с изображением для просмотра
		viewFile(fileName);

		response1.close();
		httpClient.close();
	}
}
