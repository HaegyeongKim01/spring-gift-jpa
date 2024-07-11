package gift.repository;

import gift.vo.Member;
import gift.vo.Product;
import gift.vo.Wish;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WishlistRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    private Member savedMember;
    private Product savedProduct1;
    private Product savedProduct2;

    @BeforeEach
    void setUp() {
        Member member = new Member("kookies@google.com", "@123");
        Product product1 = new Product("Ice Americano",
                4500,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");
        Product product2 = new Product("Hot Americano",
                4200,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg");

        savedMember = memberRepository.save(member); // Member pre-save
        savedProduct1 = productRepository.save(product1); // Product pre-save
        savedProduct2 = productRepository.save(product2); // Product pre-save
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("Wish save (add)")
    void save() {
        // given
        Wish expected = new Wish(savedMember.getId(), savedProduct1.getId());

        // when
        wishlistRepository.save(expected);

        // then
        assertThat(expected.getId()).isNotNull();
    }

    @Test
    @DisplayName("Find All Wish")
    void findAllWish() {
        // given
        Wish wish1 = wishlistRepository.save(new Wish(savedMember.getId(), savedProduct1.getId()));
        Wish wish2 = wishlistRepository.save(new Wish(savedMember.getId(), savedProduct2.getId()));

        // when
        List<Wish> wishList = wishlistRepository.findAll();

        // then
        assertAll(
                () ->  assertThat(wishList.size()).isEqualTo(2),
                () -> assertThat(wishList.contains(wish1)).isTrue(),
                () -> assertThat(wishList.contains(wish2)).isTrue()
        );
    }

    @Test
    @DisplayName("Find Wish By member id (member-pk)")
    void findWishListByMemberId() {
        // given
        wishlistRepository.save(new Wish(savedMember.getId(), savedProduct1.getId()));
        wishlistRepository.save(new Wish(savedMember.getId(), savedProduct2.getId()));

        // when
        List<Wish> all = wishlistRepository.findAll();

        // then
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Delete Wish by id")
    void deleteWishById() {
        // given
        Wish savedWish = wishlistRepository.save(new Wish(savedMember.getId(), savedProduct1.getId()));
        Long wishId = savedWish.getId();

        // when
        wishlistRepository.deleteById(wishId);

        // then
        Optional<Wish> deletedWish = wishlistRepository.findById(wishId);
        assertThat(deletedWish).isEmpty();
    }

}
