package study.data_jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

}
