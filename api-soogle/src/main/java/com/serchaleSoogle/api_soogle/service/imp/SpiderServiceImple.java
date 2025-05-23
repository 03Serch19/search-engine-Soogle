package com.serchaleSoogle.api_soogle.service.imp;

import com.serchaleSoogle.api_soogle.model.dao.SearchResultRepository;
import com.serchaleSoogle.api_soogle.model.dto.SearchResultDTO;
import com.serchaleSoogle.api_soogle.model.entity.SearchResultEntity;
import com.serchaleSoogle.api_soogle.service.SpiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SpiderServiceImple implements SpiderService {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2) // Prefiere HTTP/2 para mejor rendimiento
            .followRedirects(HttpClient.Redirect.NORMAL) // Sigue redirecciones automáticamente (como un navegador)
            .build();
    @Autowired
    private final SearchResultRepository searchResultRepository;

    @Override
    public void saveSpiderData(SearchResultDTO searchResultDTO) {
        SearchResultEntity searchResultEntity = SearchResultEntity.builder()
                .url(searchResultDTO.url())
                .description(searchResultDTO.description())
                .title(searchResultDTO.title())
                .build();
        searchResultRepository.save(searchResultEntity);
    }

    @Override
    public List<String> indexWebPage(String link) {
        String content = getContent(link);
        List<String> linksCleaning = getLinks(content);
        if (!linksCleaning.isEmpty()){
            linksCleaning.stream().parallel().forEach(l->{
                String contentLink=getContent(l);
                String description=getDescriptionn(contentLink);
                String title=getTitle(contentLink);
                if(description.isEmpty() || title.isEmpty()){
                    System.err.println("No se encontró la descripción o el título en el contenido HTML.");
                    return; // Salir del bucle si no se encuentra descripción o título

                }
                SearchResultDTO searchResultDTO=new SearchResultDTO(title,l,description);
               /* System.out.println("---------------------------------------------------");
                System.out.println("URL: " + searchResultDTO.url());
                System.out.println("Title: " + searchResultDTO.title());
                System.out.println("Description: " + searchResultDTO.description());
                System.out.println("---------------------------------------------------");*/
                saveSpiderData(searchResultDTO);
            });
        }

      return linksCleaning;
        /*String url;
        String title;
        String description;*/

    }

    private String getDescriptionn(String contentLink) {

        Pattern pattern = Pattern.compile(
                "(?i)<meta[^>]*?(?:name|property)=\"(?:description|og:description)\"[^>]*?content=\"([^\"]*)\""
        );
        Matcher matcher = pattern.matcher(contentLink);

        if (matcher.find()) {
            // Si encuentra una coincidencia, el grupo 1 es el contenido de la descripción
            return matcher.group(1);
        } else {
            System.err.println("No se encontró la meta descripción (name o og:description) en el contenido HTML.");
            return ""; // Retorna una cadena vacía si no se encuentra
        }
    }

    private String getTitle(String contentLink) {
        String[] separaterTitle = contentLink.split("(?i)<title[^>]*>");
        List<String> titleHref = new ArrayList<>(Arrays.asList(separaterTitle));
        if (titleHref.size() > 1) {
            titleHref.remove(0);
            String[] separaterTitle2 = titleHref.get(0).split("</title>");
            if (separaterTitle2.length > 0) {
                return separaterTitle2[0];
            } else {
                return "";
            }
        } else {
            System.err.println("No se encontró la etiqueta <title> en el contenido HTML.");
            return ""; // Retorna una cadena vacía si no se encuentra <title>
        }

    }
    private List<String> getLinks(String content) {
        // Regex para encontrar el atributo href dentro de una etiqueta <a>
        // (?i) -> case-insensitive (ignora mayúsculas/minúsculas)
        // <a\\s+[^>]*href\\s*=\\s*["']([^"']+)["'] -> busca <a, luego href="URL" o href='URL'
        // El grupo de captura (1) es lo que está dentro de las comillas.
        Pattern pattern = Pattern.compile("(?i)<a\\s+[^>]*href\\s*=\\s*[\"']([^\"']+)[\"']");
        Matcher matcher = pattern.matcher(content);
        List<String> extractedLinks = new ArrayList<>();

        while (matcher.find()) {
            extractedLinks.add(matcher.group(1)); // El grupo 1 es la URL
        }

        List<String> extensionLink = Arrays.asList(
                ".js", ".css", ".json", ".jpg", ".png", ".gif", ".svg", ".woff",
                ".woff2", ".ttf", ".eot", ".ico", ".pdf", ".docx", ".xlsx",
                ".pptx", ".zip", ".rar"
        );

        return extractedLinks.stream()
                .filter(l -> l.startsWith("http://") || l.startsWith("https://")) // Solo URLs absolutas
                .filter(l -> {
                    String linkHref = l.toLowerCase();
                    for (String ext : extensionLink) {
                        if (linkHref.endsWith(ext)) {
                            return false; // Filtra los enlaces que terminan con las extensiones especificadas
                        }
                    }
                    // Filtra también enlaces que contienen fragmentos como 'target=' o 'img src='
                    // que indican que no es una URL limpia
                    if (linkHref.contains("' target='_blank'>") || linkHref.contains("img src=") || linkHref.contains("<img") || linkHref.contains("javascript:")) {
                        return false;
                    }
                    return true;
                })
                .distinct()
                .collect(Collectors.toList());
    }



    private String getContent(String link) {
        try {
            URI uri = URI.create(link);
            // Construir la solicitud HTTP GET.
            HttpRequest request = HttpRequest.newBuilder()
                    .GET() // Indica que es una petición GET.
                    .uri(uri)
                    .build();
            //  Enviar la solicitud y recibir la respuesta
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            //  Verificar si la respuesta fue exitosa (código 200 OK).
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                // ¡Importante! Pausa después de una solicitud exitosa para no sobrecargar el servidor
                try {
                    // Pausa de 2 a 5 segundos (ejemplo)
                    long delay = 2000 + (long)(Math.random() * 3000); // 2000ms a 4999ms
                    Thread.sleep(delay);
                    System.out.println("Pausa de " + delay + "ms después de rastrear: " + link);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // Reestablece el estado de interrupción
                    System.err.println("Interrupción durante la pausa: " + ie.getMessage());
                }
                return response.body(); // Devuelve el cuerpo de la respuesta, que es el HTML.
            } else {
                // Si el código de estado no es 200 (ej. 404 Not Found, 500 Server Error)
                System.err.println("Error al obtener contenido de " + link + ". Código de estado: " + response.statusCode());
                if (response.statusCode() == 429) {
                    try {
                        System.err.println("Demasiadas solicitudes (429) para " + link + ". Pausando por 10 segundos.");
                        Thread.sleep(10000); // Pausa más larga por 429
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        System.err.println("Interrupción durante pausa por 429: " + ie.getMessage());
                    }
                }
                return "";
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Excepción al obtener contenido de " + link + ": " + e.getMessage());
            // Opcional: e.printStackTrace(); para depuración más detallada.
            return "";
        } catch (IllegalArgumentException e) {
            // Captura si el String 'link' no es una URL válida y URI.create() falla.
            System.err.println("URL proporcionada inválida: " + link + ": " + e.getMessage());
            return "";
        }
    }
    //con splits manuales pero propenso a errores
    private List<String> getLinks2(String content) {
         /* String[] separaterBody0 = content.split("(?i)<body[^>]*>");
        List<String> linksHref0 = new ArrayList<>(Arrays.asList(separaterBody0));
        if(linksHref0.size() > 1) {
            linksHref0.removeFirst();
        }else {
            System.err.println("No se encontró la etiqueta <body> en el contenido HTML.");
            return new ArrayList<>(); // Retorna una lista vacía si no se encuentra <body>
        }
        content= linksHref0.getFirst();*/
        // String[] separaterBody = content.split("<a href=\"");
        String[] separaterBody = content.split("(?i)<a[^>]*?href=[\"']");


        //este lista interna es una lista o modifcable por ende si la creamo y la asignamos directem,tneal list no podremos modufcar el list
        List<String> linksHref = new ArrayList<>(Arrays.asList(separaterBody));
        if(linksHref.size()== 1) {
            System.err.println("No se encontró la etiqueta <a> en el contenido HTML.");
            return new ArrayList<>(); // Retorna una lista vacía si no se encuentra <a>
        }
        // linksHref.removeFirst();
        List<String> extensionLink=Arrays.asList(".js",
                ".css",
                ".json",
                ".jpg",
                ".png",
                ".gif",
                ".svg",
                ".woff",
                ".woff2",
                ".ttf",
                ".eot",
                ".ico",
                ".pdf",
                ".docx",
                ".xlsx",
                ".pptx",
                ".zip",
                ".rar");
        return linksHref.stream().map(linkHref -> {
            String[] separaterLink = linkHref.split("\"");
            if (separaterLink.length > 0) {
                return separaterLink[0];
            } else {
                return "";
            }  //mejor filtro directamente
        }).filter(l -> l.startsWith("https://")
        ).filter(l -> {
            String linkHref = l.toLowerCase();
            for (String ext : extensionLink) {
                if (linkHref.endsWith(ext)) {
                    return false; // Filtra los enlaces que terminan con las extensiones especificadas
                }
            }
            return true;
        }).distinct().collect(Collectors.toList());
        // return  linksHref;//return links;
    }

}