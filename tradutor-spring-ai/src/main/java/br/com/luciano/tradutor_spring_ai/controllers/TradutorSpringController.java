package br.com.luciano.tradutor_spring_ai.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.luciano.tradutor_spring_ai.services.TradutorSpringService;

@RestController
@RequestMapping("/api")
public class TradutorSpringController {
	
	@Autowired
	private TradutorSpringService service;
	
	@GetMapping("/translate")
	public ResponseEntity<String> tradutor(@RequestParam("text") String text, @RequestParam("targetLang") String targetLang) {
		
		try {
			String textoTraduzido = service.tradutorTexto(text, targetLang);
			return ResponseEntity.ok(textoTraduzido);
		} catch (IOException e) {
			System.err.println("Erro ao traduzir: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao traduzir: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro inesperado: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado: " + e.getMessage());
		}
	}
	
	
}
