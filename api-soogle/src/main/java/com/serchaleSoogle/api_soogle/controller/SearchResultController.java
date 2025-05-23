package com.serchaleSoogle.api_soogle.controller;

import com.serchaleSoogle.api_soogle.model.dto.SearchResultDTO;
import com.serchaleSoogle.api_soogle.service.SearchResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/results") // Cambiamos la ruta para que sea más clara para "resultados"
@RequiredArgsConstructor
public class SearchResultController {

    @Autowired
    private final SearchResultService searchResultService;

    @GetMapping
    public ResponseEntity<List<SearchResultDTO>> getSearchResults(@RequestParam String q) {
        // Llama al servicio para obtener los resultados de la base de datos
        List<SearchResultDTO> results = searchResultService.getSearchResults(q);

        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content si no hay resultados
        } else {
            return new ResponseEntity<>(results, HttpStatus.OK); // 200 OK con resultados
        }
    }
}
