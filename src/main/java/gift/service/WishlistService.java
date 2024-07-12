package gift.service;

import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import gift.vo.Member;
import gift.vo.Product;
import gift.vo.Wish;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public List<Wish> getWishProductList(Long memberId) {
        return wishlistRepository.findByMemberId(memberId);
    }

    public void addWishProduct(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalAccessError("위시리스트에 추가하려는 회원을 찾을 수 없습니다. "));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("위시리스트에 추가하려는 상품을 찾을 수 없습니다. "));
        Wish wish = new Wish(member, product);
        wishlistRepository.save(wish);
    }

    public void deleteWishProduct(Long id) {
        wishlistRepository.deleteById(id);
    }

}
