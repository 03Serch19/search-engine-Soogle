package com.serchaleSoogle.api_soogle.service.imp;

import com.serchaleSoogle.api_soogle.model.dao.SearchResultRepository;
import com.serchaleSoogle.api_soogle.model.dto.SearchResultDTO;
import com.serchaleSoogle.api_soogle.model.entity.SearchResultEntity;
import com.serchaleSoogle.api_soogle.service.SearchResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor//necesario si no quiero usar el contrutor explicito, para asi mantenr la mismtarobustes sin tener que explictamente crear el constructor
public class SearchResultServiceImpl implements SearchResultService {
     //@Autowired//no necesario si tengfo solo una entidad
    private final SearchResultRepository searchResultRepository;

    @Override
    public List<SearchResultDTO> getSearchResults(String query) {
        // Usamos el metodo del repositorio para buscar resultados que contengan la consulta en el título o la descripción
        List<SearchResultEntity> entities = searchResultRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);

        // Mapeamos las entidades encontradas a DTOs para devolverlos al controlador
        return entities.stream()
                .map(entity -> new SearchResultDTO(entity.getTitle(),entity.getUrl(), entity.getDescription()))
                .collect(Collectors.toList());
    }
}
