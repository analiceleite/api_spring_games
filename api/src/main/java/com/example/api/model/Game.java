package com.example.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;


@Entity
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do jogo é obrigatório")
    private String name;

    @NotBlank(message = "A descrição é obrigatória")
    private String description;

    @NotBlank(message = "A data de lançamento é obrigatória")
    private String releaseDate;

    @Min(value = 0, message = "O desconto não pode ser negativo")
    private Double discount;

    @Min(value = 0, message = "O preço antigo não pode ser negativo")
    private Double oldPrice;

    @Min(value = 0, message = "O preço atual não pode ser negativo")
    private Double currentPrice;

    private String category;

    private String platform;

    // Constructor with no arguments
    public Game() {}

    // Constructor with arguments
    public Game(Long id, String name, String description, String releaseDate, Double discount, Double oldPrice, Double currentPrice, String category, String platform) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.discount = discount;
        this.oldPrice = oldPrice;
        this.currentPrice = currentPrice;
        this.category = category;
        this.platform = platform;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @PreUpdate
    public void calculateCurrentPrice() {
        calculateCurrentPrice(this.oldPrice, this.discount);
    }

    public void calculateCurrentPrice(Double oldPrice, Double discount) {
        if (oldPrice == null || oldPrice < 0) {
            throw new IllegalArgumentException("O preço antigo deve ser maior que 0.");
        }
    
        if (discount == null || discount < 0) {
            discount = 0.0;
        }
    
        if (discount > oldPrice) {
            throw new IllegalArgumentException("O desconto não pode ser maior que o preço antigo.");
        }
    
        this.currentPrice = oldPrice - discount;
    }
    
}
