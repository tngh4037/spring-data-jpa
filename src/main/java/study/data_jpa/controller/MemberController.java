package study.data_jpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.repository.MemberRepository;

/**
 * Web 확장
 */
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) { // 파라미터로 PK를 받는다. 이럴 때는 도메인 클래스 컨버터라는 기능을 사용할 수 있다. ( 참고. 아래 findMembers )
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) { // HTTP 요청은 회원 id 를 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환한다. ( 도메인 클래스 컨버터도 리파지토리를 사용해서 엔티티를 찾는다. )
        // 참고)
        // 도메인 클래스 컨버터로 엔티티를 파라미터로 받으면, 이 엔티티는 단순 조회용으로만 사용해야 한다.
        // ( 트랜잭션이 없는 범위에서 엔티티를 조회했으므로, 엔티티를 변경해도 DB에 반영되지 않는다. )
        //
        // 참고)
        // 김영한님은 이런 기능의 사용은 아주 간단한 경우가 아니라면 권장하지는 않는다고 한다.
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) { // Pageable 이 있으면 PageRequest 라는 객체를 생성하고 HTTP 파라미터 값을 채워서 주입해준다.
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/members_page")
    public Page<MemberDto> list2(@PageableDefault(size = 5, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) { // 기본값 개별 설정 ( @PageableDefault 어노테이션을 사용 ) ( 참고. default / 글로벌 설정보다 우선한다. )
        Page<Member> page = memberRepository.findAll(pageable); // 엔티티는 API 응답으로 반환하지 말자. 항상 DTO로 변환하자. ( 참고로 Page 는 그냥 반환해도 무관하다. )

        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // Page<MemberDto> map2 = page.map(member -> new MemberDto(member));
        // Page<MemberDto> map2 = page.map(MemberDto::new);

        return map;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}

