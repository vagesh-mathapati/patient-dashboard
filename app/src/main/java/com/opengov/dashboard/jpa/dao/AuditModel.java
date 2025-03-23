package com.opengov.dashboard.jpa.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
@Getter
@EqualsAndHashCode
public abstract class AuditModel implements Serializable {

  @Serial
  private static final long serialVersionUID = 959736109190163370L;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", nullable = false, updatable = false)
  @CreatedDate
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at", nullable = false)
  @LastModifiedDate
  private Date updatedAt;

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt != null ? new Date(createdAt.getTime()) : null;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt != null ? new Date(updatedAt.getTime()) : null;
  }
}
