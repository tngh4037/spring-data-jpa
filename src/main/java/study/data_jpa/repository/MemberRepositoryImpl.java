package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.data_jpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}

// [ 사용자 정의 구현 클래스명 규칙 ]
// : 스프링 데이터 JPA 리포지토리 인터페이스 명 (MemberRepository) + "Impl"
// 참고) 스프링 데이터 2.x 부터는 사용자 정의 인터페이스 명 + Impl 방식도 지원한다. ( ex. MemberRepositoryCustomImpl )
//  ㄴ 기존 방식보다 이 방식이 사용자 정의 인터페이스 이름과 구현 클래스 이름이 비슷하므로 더 직관적이다. 추가로 여러 인터페이스를 분리해서 구현하는 것도 가능하기 때문에 새롭게 변경된 이 방식을 사용하는 것을 더 권장한다.