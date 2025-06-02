package br.com.luciano.tradutor_spring_ai.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.translate.v3.GetSupportedLanguagesRequest;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.SupportedLanguage;
import com.google.cloud.translate.v3.SupportedLanguages;
import com.google.cloud.translate.v3.TranslationServiceClient;

@Service
public class TradutorSpringService {
	
	@Value("${google.cloud.project.id}")
	private String id;
	
	private static final String LOCATION = "global";
	
	public String tradutorTexto(String text, String targetLanguage) throws IOException {
		if (text == null || text.isBlank() || targetLanguage == null || targetLanguage.isBlank()) {
			return "Texto ou idioma de destino não podem estar vazios!";
		}
		
		try (TranslationServiceClient client = TranslationServiceClient.create()){
			LocationName parent = LocationName.of(id, LOCATION);
			
			TranslateTextRequest request = TranslateTextRequest.newBuilder()
					.setParent(parent.toString()).setMimeType("text/plain")
					.setTargetLanguageCode(targetLanguage).addContents(text).build();
			
			TranslateTextResponse response = client.translateText(request);
			
			if (response.getTranslationsList().isEmpty()) {
				return "Tradução não encontrada";
			} else {
				return response.getTranslationsList().get(0).getTranslatedText();
			}
		} catch (ApiException e) {
			return "Erro na Api: " + e.getMessage();
		} 
	}
	
	public Map<String, String> listarIdiomas() throws IOException {
		
		Map<String, String> idiomas = new HashMap<>();
		
		try (TranslationServiceClient client = TranslationServiceClient.create()) {
			LocationName parent = LocationName.of(id, LOCATION);
			
			GetSupportedLanguagesRequest request = GetSupportedLanguagesRequest.newBuilder()
					.setParent(parent.toString())
					.setDisplayLanguageCode("pt-br").build();
			
			SupportedLanguages supportedLanguages = client.getSupportedLanguages(request);
			List<SupportedLanguage> linguagems = supportedLanguages.getLanguagesList();
			
			for (SupportedLanguage supportedLanguage : linguagems) {
				idiomas.put(supportedLanguage.getLanguageCode(), supportedLanguage.getDisplayName());
			}
		}
		return idiomas.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(String.CASE_INSENSITIVE_ORDER))
				.collect(
						LinkedHashMap::new,
						(map, entry) -> map.put(entry.getKey(),entry.getValue()),
						LinkedHashMap::putAll);
	}
	
}
