package com.serchaleSoogle.api_soogle.service;

import com.serchaleSoogle.api_soogle.model.dto.SearchResultDTO;

import java.util.List;

public interface SpiderService {
    void saveSpiderData(SearchResultDTO searchResultDTO);

   List<String> indexWebPage(String url);
}
