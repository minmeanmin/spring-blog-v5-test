package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception400;
import shop.mtcoding.blog._core.errors.exception.Exception401;
import shop.mtcoding.blog._core.errors.exception.Exception404;

import java.util.Optional;

@RequiredArgsConstructor
@Service // IoC에 등록된다.
public class UserService {
    private final UserJPARepository userJPARepository;

    @Transactional
    public User 회원수정(int id, UserRequest.UpdateDTO reqDTO){
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원 정보를 찾을 수 없습니다."));

        user.setPassword(reqDTO.getPassword());
        user.setEmail(reqDTO.getEmail());
        return user;
    } // 더티체킹

    public User 회원조회(int id){
        // 예외 처리
        User user = userJPARepository.findById(id)
                .orElseThrow(() -> new Exception404("회원정보를 찾을 수 없습니다."));
        return user;
    }

    // 조회라서 @Transactional이 필요 없다.
    @Transactional
    public User 로그인(UserRequest.LoginDTO reqDTO){
        User sessionUser = userJPARepository.findByUsernameAndPassword(reqDTO.getUsername(), reqDTO.getPassword())
                .orElseThrow(() -> new Exception401("인증되지 않았습니다."));
                // 조회를 했을 때 값이 null이면 throw를 날리고, null이 아니면 값을 받겠다는 의미
        return sessionUser;
    }

    @Transactional
    public void 회원가입(UserRequest.JoinDTO reqDTO){
        // 1. 유효성 검사 (컨트롤러 책임)

        // 2. 중복 검사 (서비스에서 체크) - DB 연결이 필요하기 때문에
        Optional<User> userOP = userJPARepository.findByUsername(reqDTO.getUsername());

        if(userOP.isPresent()) {// 있으면 비정상, 없으면 정상
            throw new Exception400("중복된 유저네임입니다.");
        }

        userJPARepository.save(reqDTO.toEntity());
    }
}
