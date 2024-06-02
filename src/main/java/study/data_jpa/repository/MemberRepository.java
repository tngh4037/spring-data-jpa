package study.data_jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // @Query(name = "Member.findByUsername") // 주석 처리해도 동작한다. => 우선순위: 1) "엔티티.메서드명" 으로된 NamedQuery 를 먼저 찾는다. 있으면 실행하고, 없으면 2) 메소드 이름을 분석해서 JPQL을 생성하고 실행한다.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
