package br.com.luciano.tradutor_spring_ai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.luciano.tradutor_spring_ai.services.TradutorSpringService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class TradutorFormController {
	
	@Autowired
	private TradutorSpringService service;
	
	@GetMapping("/tradutor")
	public String view(Model model) throws IOException {
		model.addAttribute("linguas", service.listarIdiomas());
		return "tradutor";
	}
	
	@PostMapping("/tradutor")
	public String traduzirTexto(@RequestParam("texto") String texto, @RequestParam("idioma") String idioma, Model model) throws IOException {
		String traduzido = service.tradutorTexto(texto, idioma);
		
		model.addAttribute("texto", texto);
		model.addAttribute("traduzido", traduzido);
		model.addAttribute("idiomaSelecionado", idioma);
		model.addAttribute("linguas", service.listarIdiomas());
		
		return "tradutor";
	}
	
	/*private Map<String, String> getIdiomas() {
		Map<String, String> idiomas = new LinkedHashMap<>();
		idiomas.put("en", "Inglês");
		idiomas.put("es", "Espanhol");
		idiomas.put("fr", "Francês");
		idiomas.put("de", "Alemão");
		idiomas.put("it", "Italiano");
		idiomas.put("pt", "Português");
		idiomas.put("ja", "Japonês");
		return idiomas;
	}*/
}
