package study.data_jpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // = protected Member() {}
@ToString(of = {"id", "username", "age"}) // team 은 적으면 안된다. 서로 참조하고 있기 때문에 무한루프가 걸릴 수 있다. 따라서 가급적이면 연관관계 필드는 toString 에서 제외하는 게 좋다.
@NamedQuery(
        name="Member.findByUsername",
        query="select m from Member m where m.username = :username"
)
@NamedEntityGraph(
        name = "Member.all",
        attributeNodes = @NamedAttributeNode("team")
)
public class Member /* extends JpaBaseEntity */ extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) // 지연로딩으로 설정했다. 따라서, Member 조회 시에 team 은 조회되지 않는다. 그리고 이때 team 필드에는 null 이 아닌, 프록시 객체를 넣어둔다. 이후 team 의 어떤 데이터를 사용하기 위해 메서드 등을 호출하면, 그 시점에, 그때 team 을 조회하는 SQL 이 별도로 날라간다. 그리고 그 결과를 채운다. (프록시 초기화)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;

        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
