package gestao.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import gestao.models.geocode.Geocode;

/*
 * 
 * https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
 * 
 * */

@FeignClient(name = "GeocodeClient", url="https://maps.googleapis.com/maps/api/geocode")
public interface GeocodeClient {

	@GetMapping("json?address={address}&key={key}")
	Geocode buscaCoordenadasPor(@PathVariable("address") String endereco,
								@PathVariable("key") String chave);
	
}
