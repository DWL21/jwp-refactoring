package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static kitchenpos.support.fixture.domain.ProductFixture.APPLE_1000;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private JdbcTemplateProductDao jdbcTemplateProductDao;

    @NestedApplicationTest
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("Product를 생성한다.")
        void success() {
            Product product = productService.create(APPLE_1000.getProduct());

            Product actual = jdbcTemplateProductDao.findById(product.getId())
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(product);
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            jdbcTemplateProductDao.save(APPLE_1000.getProduct());
            jdbcTemplateProductDao.save(APPLE_1000.getProduct());
        }

        @Test
        @DisplayName("Product 전체 목록을 조회한다.")
        void success() {
            List<Product> products = productService.list();

            assertThat(products).hasSize(2);
        }
    }
}
