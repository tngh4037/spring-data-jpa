package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import study.data_jpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList(); // 리스트 반환
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class) // 참고) JPA에서 count()는 Long 타입을 return 함
                .getSingleResult(); // 단건 반환
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    // 데이터 조회: 몇 번째 부터 몇 개의 데이터를 조회할지 작성
    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery(
                "select m from Member m " +
                        "where m.age = :age " +
                        "order by m.username desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset) // 몇 번째부터
                .setMaxResults(limit) // 몇 개 가져올것인가?
                .getResultList();
    }

    // 전체 갯수 조회: 현재 내 페이지가 전체 페이지 중에서 몇 번째 페이지인지 등 계산하기 위해 필요
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

}
