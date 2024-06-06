package study.data_jpa.repository;

public class UsernameOnlyDto {

    private final String username;

    public UsernameOnlyDto(String username) { // 생성자의 파라미터 이름(username)으로 매칭해서 프로젝션
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
