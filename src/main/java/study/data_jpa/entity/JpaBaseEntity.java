package study.data_jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class JpaBaseEntity {

    @Column(updatable = false, insertable = true)
    private LocalDateTime createdDate;
    @Column(updatable = true, insertable = true)
    private LocalDateTime updatedDate;

    @PrePersist // persist 하기 전 호출될 이벤트 정의
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate // 업데이트 하기 전 호출될 이벤트 정의
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}

// [ JPA 주요 이벤트 어노테이션 ]
// - @PrePersist, @PostPersist
// - @PreUpdate, @PostUpdate

// 이벤트 정의하고, 엔티티에서 상속받으면 끝
