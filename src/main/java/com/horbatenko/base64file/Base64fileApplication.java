package com.horbatenko.base64file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.horbatenko.base64file.model.Example;
import com.horbatenko.base64file.model.ReqBody;
import com.horbatenko.base64file.model.RespBody;
import com.horbatenko.base64file.model.RespException;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/", produces = APPLICATION_JSON_VALUE)
@SpringBootApplication
//@ApiOperation(value = "Basic API")
public class Base64fileApplication {
	/*
	http://localhost:1000/v3/api-docs - for postman
	http://localhost:1000/swagger-ui/index.html - for browser
	 */

	private static final String DEFAULT_DIR = System.getProperty("user.home");

	public static void main(String[] args) {
		SpringApplication.run(Base64fileApplication.class, args);
	}

	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper mapper = new ObjectMapper();

//	@ApiOperation(value = "Get file by path", notes = "Returns file path")
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Successfully retrieved")
//	})
	@GetMapping(consumes = APPLICATION_JSON_VALUE)
	public RespBody getFile(@RequestHeader HttpHeaders headers,
							@RequestBody ReqBody body) throws IOException {
		ResponseEntity<String> response = executeRequest(body, body.getHttpMethod(), headers);

		JsonNode root = mapper.readTree(response.getBody());
		JsonNode context = root.path(body.getContextField());
		JsonNode ext = root.path(body.getExtensionField());
		byte[] bytes = Base64.getDecoder().decode(context.asText());

		String filePath = getFilePath(body, ext.asText());
		File file = new File(filePath);
		file.createNewFile();

		saveToFile(bytes, file);

		if (body.isOpen()) {
			Runtime.getRuntime().exec("open " + filePath);
		}

		return new RespBody(filePath);
	}

//	@ApiOperation(value = "Example endpoint", notes = "Returns example text file path")
	@GetMapping("/test")
	public Example getExample() {
		return new Example();
	}

	private ResponseEntity<String> executeRequest(ReqBody body, HttpMethod httpMethod, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<>(body.getContent() != null ? body.getContent().toString() : "{}", headers);
		return restTemplate.exchange(body.getUrl(), httpMethod, entity, String.class);
	}

	private String getFilePath(ReqBody body, String ext) throws IOException {
		String filePath = (body.getOutputDir() == null ? DEFAULT_DIR : body.getOutputDir());
		if (!filePath.endsWith("/")) {
			filePath+="/";
		}

		if (filePath.startsWith("~")) {
			filePath = DEFAULT_DIR + filePath;
		}

		if (!Files.exists(Path.of(filePath))) {
			new File(filePath).mkdirs();
		}

		filePath += body.getFileName() != null ?
				body.getFileName() :
				"decoded_file_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS"));

		filePath += "." + (StringUtils.hasText(ext) ? ext :
						(StringUtils.hasText(body.getExtension()) ? body.getExtension() : "new"));

		if (Files.exists(Path.of(filePath))) {
			Files.delete(Path.of(filePath));
		}

		return filePath;
	}

	private void saveToFile(byte[] bytes, File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(bytes, 0, bytes.length);
		fos.close();
	}

	@ExceptionHandler(Exception.class)
	public RespException handleException(Exception e) {
		return new RespException(e.getMessage());
	}
}
