package study.data_jpa.dto;

import lombok.Data;
import study.data_jpa.entity.Member;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) { // DTO에서 엔티티를 바라바도 무관하다. (단, 엔티티가 DTO 내에 필드로 들어가면 안된다.)
        this.id = member.getId();
        this.username = member.getUsername();
    }

}
