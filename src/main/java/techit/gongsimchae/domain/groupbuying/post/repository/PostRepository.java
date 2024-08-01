package techit.gongsimchae.domain.groupbuying.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import techit.gongsimchae.domain.groupbuying.post.entity.Post;

public interface PostRepository extends JpaRepository<Post,Long> {
}
