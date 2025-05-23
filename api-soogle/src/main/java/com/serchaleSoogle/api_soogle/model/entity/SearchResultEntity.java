package com.serchaleSoogle.api_soogle.model.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "search_results")
@Getter
@NoArgsConstructor // Lombok: genera constructor sin argumentos
@AllArgsConstructor
@Builder
public class SearchResultEntity {
    @Id //  clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID interno de la base de datos

    private String url;
    private String title;
    private String description;
}
