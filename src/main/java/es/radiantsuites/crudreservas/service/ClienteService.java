package es.radiantsuites.crudreservas.service;

import es.radiantsuites.crudreservas.dto.Cliente;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ClienteService {
    private final RestTemplate restTemplate;

    public ClienteService(){
        this.restTemplate = new RestTemplate();
    }

    public Cliente obtenerClientePorId(Integer id){
        String url = "https://d1kyl7ajtmjgoz.cloudfront.net/clientes/" + id;
        System.out.println(url);
        return restTemplate.getForObject(url, Cliente.class);
    }


}