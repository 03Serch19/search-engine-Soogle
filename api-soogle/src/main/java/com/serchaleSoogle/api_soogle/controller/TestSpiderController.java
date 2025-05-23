package com.serchaleSoogle.api_soogle.controller;

import com.serchaleSoogle.api_soogle.model.dto.SearchResultDTO;
import com.serchaleSoogle.api_soogle.service.SpiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/spider")
@RequiredArgsConstructor
public class TestSpiderController {
    @Autowired
    private final SpiderService spiderService;

//    @GetMapping("/test2")
//    public void testSpider() {
//        spiderService.indexWebPage("https://statisticsbyjim.com/probability/benfords-law/");
//    }
    // Este metodo es solo para pruebas, no debería estar en producción
    @GetMapping("/test2")
    public List<String> getSpiderData() {
        return spiderService.indexWebPage("https://www.fragrantica.es/perfume/Jean-Paul-Gaultier/Le-Beau-Le-Parfum-72158.html");
    }

   /* @GetMapping("/test")
    public void saveSpiderData() {
        spiderService.saveSpiderData(new SearchResultDTO(
                "https://statisticsbyjim.com/probability/benfords-law/",
                "La ley de Benford explicada con ejemplos - Statistics By Jim",
                "La ley de Benford describe la distribución de frecuencias relativas de los primeros dígitos de los números en conjuntos de datos. Los primeros dígitos con ..."
        ));
    }*/
   @GetMapping("/test")
   public void saveSpiderData() {
       spiderService.saveSpiderData(new SearchResultDTO(
               "  Le Beau Le Parfum Jean Paul Gaultier Colonia - una nuevo fragancia para Hombres 2022",
               "https://www.fragrantica.es/perfume/Jean-Paul-Gaultier/Le-Beau-Le-Parfum-72158.html",
               "Le Beau Le Parfum de Jean Paul Gaultier es una fragancia de la familia olfativa Oriental Amaderada para Hombres. Esta fragrancia es nueva. Le Beau Le Pa..."
       ));
   }

}
