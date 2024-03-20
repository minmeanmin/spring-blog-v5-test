package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.errors.exception.Exception403;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog._core.utils.ApiUtil;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor // final이 붙은 친구들의 생성자를 만들어줘
@RestController // new BoardController(IoC에서 BoardRepository를 찾아서 주입) -> IoC 컨테이너 등록
public class BoardController {
    private final BoardService boardService;

    private final HttpSession session;


    // TODO: 글 목록 조회 API 필요 -> @GetMapping("/")
    @GetMapping("/")
    public ResponseEntity<?> main() {
        List<BoardResponse.MainDTO> respDTO = boardService.글목록조회();
        return ResponseEntity.ok(new ApiUtil(respDTO));
    }

    // TODO: 글 상세 보기 API 필요 -> @GetMapping("/api/boards/{id}/detail")
    @GetMapping("/api/boards/{id}/detail")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.글상세보기(id, sessionUser);
        return ResponseEntity.ok(new ApiUtil(board));
    }

    // TODO: 글 조회 API 필요 -> @GetMapping("/api/boards/{id}")
    @GetMapping("/api/boards/{id}")
    public ResponseEntity<?> findOne(@PathVariable Integer id) {
        Board board = boardService.글조회(id);
        return ResponseEntity.ok(new ApiUtil(board));
    }


    @PostMapping("/api/boards")
    public ResponseEntity<?> save(@RequestBody BoardRequest.SaveDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.글쓰기(reqDTO, sessionUser);
        return ResponseEntity.ok(new ApiUtil(board)); // board 를 바로 불러오면 위험한데 그 이유는 메세지 컨버터로 json 으로 바꿔주는 과정에서 Lazy Loading 이 일어나서 모두 터진다. (이걸 무한 순환 창조라 한다.)
    }

    @PutMapping("/api/boards/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody BoardRequest.UpdateDTO reqDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardService.글수정(id, sessionUser.getId(), reqDTO);
        return ResponseEntity.ok(new ApiUtil(board));
    }

    @DeleteMapping("/api/boards/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        boardService.글삭제(id, sessionUser.getId()); //익명 블로그면 id만 넘겨도 된다.
        return ResponseEntity.ok(new ApiUtil(null));
    }

}
