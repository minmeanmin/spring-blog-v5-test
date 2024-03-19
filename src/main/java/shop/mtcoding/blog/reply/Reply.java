package shop.mtcoding.blog.reply;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.board.Board;
import shop.mtcoding.blog.user.User;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Table(name = "reply_tb")
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY )
    private User user; //user_id로 만들어진다.

    @ManyToOne(fetch = FetchType.LAZY )
    private Board board;//관계 테이블에서 1에 해당한는 entity를 묶어준다.

    private String comment;

    @CreationTimestamp
    private Timestamp createdAt;

    @Transient
    private boolean isReplyOwner;

    @Builder
    public Reply(Integer id, User user, Board board, Timestamp createdAt) {
        this.id = id;
        this.user = user;
        this.board = board;
        this.createdAt = createdAt;
    }
}

