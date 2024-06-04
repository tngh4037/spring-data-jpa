package study.data_jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, insertable = true)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(updatable = false, insertable = true)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

}

// [ 스프링 데이터 JPA ]
// - @CreatedDate @LastModifiedDate
// - @CreatedBy @LastModifiedBy
//
// @EnableJpaAuditing -> 스프링 부트 설정 클래스에 적용해야 함
// @EntityListeners(AuditingEntityListener.class) -> 적용해야 함
//
// (참고) 등록자, 수정자(..By) 적용하려면, 위 작업에 더해, AuditorAware 를 구현해서 Bean 으로 등록해줘야 함. ( 그러면 등록되거나 수정될 때 마다 호출해서 결과물을 꺼내감. )