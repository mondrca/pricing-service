package com.inditex.pricing.adapter.out.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "PRICES",
    indexes = {
      @Index(
          name = "idx_prices_brand_product_dates",
          columnList = "brandId, productId, startDate, endDate"),
      @Index(name = "idx_prices_priority", columnList = "priority")
    })
public class PriceJpaEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "brand_id", nullable = false)
  private Long brandId;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "price_list", nullable = false)
  private Long priceList;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "priority", nullable = false)
  private Integer priority;

  @Column(name = "price", nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(name = "currency", nullable = false, length = 3)
  private String currency;

  protected PriceJpaEntity() {}

  // getters/setters

  public Long getId() {
    return id;
  }

  public Long getBrandId() {
    return brandId;
  }

  public void setBrandId(Long brandId) {
    this.brandId = brandId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Long getPriceList() {
    return priceList;
  }

  public void setPriceList(Long priceList) {
    this.priceList = priceList;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
