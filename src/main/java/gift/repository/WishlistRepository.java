package gift.repository;

import gift.vo.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMemberId(Long memberId);
}
