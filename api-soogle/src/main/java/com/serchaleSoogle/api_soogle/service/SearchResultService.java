package com.serchaleSoogle.api_soogle.service;
import com.serchaleSoogle.api_soogle.model.dto.SearchResultDTO;

import java.util.List;
import java.util.Optional;
public interface SearchResultService {
    List<SearchResultDTO> getSearchResults(String query);
}
