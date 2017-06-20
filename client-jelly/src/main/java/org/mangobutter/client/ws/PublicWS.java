package org.mangobutter.client.ws;

import javax.inject.Inject;

import org.mangobutter.core.service.ServiceProcessor;
import org.mangobutter.core.utils.Request;
import org.mangobutter.core.utils.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;


@RestController
@RequestMapping("public/")
public class PublicWS {

	@Inject
	protected EurekaClient client;
	
	@Inject
	protected RestTemplateBuilder restTemplateBuilder;
	
	@Inject
	protected ApplicationContext context;
	
	@RequestMapping(value = "service", method = RequestMethod.POST)
	public @ResponseBody Response service(@RequestBody Request request){
		Response response = new Response();
		
		String service = (String) request.getParams().get("service");
		String className = "";
		switch (service) {
			case "PEANUT_SVC":
				// remote service
				RestTemplate restTemplate = restTemplateBuilder.build();
				InstanceInfo instanceInfo = client.getNextServerFromEureka("service-jelly", false);
				String baseUrl = instanceInfo.getHomePageUrl();
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<Request> entity = new HttpEntity<>(request,headers);
				ResponseEntity<Response> result = restTemplate.exchange(baseUrl + "public/service", HttpMethod.POST, entity, Response.class);
				
				response = result.getBody();
				
				String test = "";
				break;
			case "LOCAL_SVC":
				// local
				className = "LocalSvcImpl";
				break;
		}
		if (!"".equals(className)){
			ServiceProcessor processor = (ServiceProcessor) context.getBean(className);	
			processor.process(request, response);
		}
		
		return response;
	}
}
