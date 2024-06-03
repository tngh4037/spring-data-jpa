package study.data_jpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // @Query(name = "Member.findByUsername") // 주석 처리해도 동작한다. => 우선순위: 1) "엔티티.메서드명" 으로된 NamedQuery 를 먼저 찾는다. 있으면 실행하고, 없으면 2) 메소드 이름을 분석해서 JPQL을 생성하고 실행한다.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional

    // paging
    Page<Member> findByAge(int age, Pageable pageable); // totalCount
    Slice<Member> findSliceByAge(int age, Pageable pageable); // totalCount x, next page check (limit + 1) o
    List<Member> findListByAge(int age, Pageable pageable); // only my data
    @Query(value = "select m from Member m left join m.team t",
           countQuery = "select count(m.username) from Member m")
    Page<Member> findDivideCountByAge(int age, Pageable pageable); // countQuery 분리

    // bulk update
    @Modifying(clearAutomatically = true) // 이게 있어야 JPA 의 executeUpdate() 를 호출한다. ( 이게 없으면 getResultList() 나 getSingleList() 등을 호출한다. )
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // fetch join
    @Query("select m From Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // entityGraph (1) - 공통 메서드 오버라이드
    @Override // 기본 인터페이스인 findAll 을 통해서 엔티티 그래프를 사용하고 싶은데, findAll 은 상위에 있다. 따라서 이걸 내가 뭔가 따로 하고싶으면 오버라이드해서 사용하면 된다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // entityGraph (2) - 내가 쿼리를 직접 작성하면서, EntityGraph 사용 가능 ( JPQL + EntityGraph )
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // entityGraph (3) - 쿼리 메서드 기능을 사용하면서, EntityGraph 사용 가능
    //@EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // JPA Hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) // 읽기 용도로만 쓰도록 내부에서 최적화 하도록 힌트 적용. (-> 스냅샷을 만들지 않음, 따라서 변경 감지 X)
    Member findReadOnlyByUsername(String username);

    // JPA Lock
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
