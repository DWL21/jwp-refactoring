package kitchenpos.dao;

import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE_EMPTY_TRUE;
import static kitchenpos.support.fixture.domain.TableGroupFixture.getTableGroup;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.support.fixture.domain.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JdbcTemplateOrderDaoTest extends JdbcTemplateTest{

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("주문을 저장한다.")
        void success() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            Order order = OrderFixture.COMPLETION.getOrder(orderTable.getId());

            Order savedOrder = jdbcTemplateOrderDao.save(order);

            Long actual = savedOrder.getId();
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    @DisplayName("findById 메서드는")
    class FindById {

        private Order order;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            order = jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
        }

        @Test
        @DisplayName("아이디로 주문을 단일 조회한다.")
        void success() {
            Long id = order.getId();

            Order actual = jdbcTemplateOrderDao.findById(id)
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .isEqualTo(order);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(getTableGroup());
            OrderTable orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COOKING.getOrder(orderTable.getId()));
        }

        @Test
        @DisplayName("주문 전체 목록을 조회한다.")
        void success() {
            List<Order> orders = jdbcTemplateOrderDao.findAll();

            assertThat(orders).hasSize(2);
        }
    }

    @Nested
    @DisplayName("existsByOrderTableIdAndOrderStatusIn 메서드는")
    class ExistsByOrderTableIdAndOrderStatusIndById {

        private OrderTable orderTable;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(getTableGroup());
            orderTable = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable.getId()));
        }

        @Test
        @DisplayName("아이디 하나와 주문 상태 목록을 받아 일치하는 주문이 있으면 true를 반환한다.")
        void success_true() {
            Long orderTableId = orderTable.getId();
            OrderStatus orderStatus = OrderStatus.COMPLETION;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                List.of(orderStatus.name()));

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("아이디 하나와 주문 상태 목록을 받아 일치하는 주문이 없으면 false를 반환한다.")
        void success_false() {
            Long orderTableId = orderTable.getId();
            OrderStatus orderStatus = OrderStatus.COOKING;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                List.of(orderStatus.name()));

            assertThat(actual).isFalse();
        }
    }

    @Nested
    @DisplayName("existsByOrderTableIdInAndOrderStatusIn 메서드는")
    class ExistsByOrderTableIdInAndOrderStatusIn {

        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            TableGroup tableGroup = jdbcTemplateTableGroupDao.save(getTableGroup());
            orderTable1 = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            orderTable2 = jdbcTemplateOrderTableDao.save(GUEST_ONE_EMPTY_TRUE.getOrderTable(tableGroup.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable1.getId()));
            jdbcTemplateOrderDao.save(OrderFixture.COMPLETION.getOrder(orderTable1.getId()));
        }

        @Test
        @DisplayName("아이디 목록과 주문 상태 목록을 받아 일치하는 주문이 있으면 true를 반환한다.")
        void success_true() {
            List<Long> orderTableIds = List.of(orderTable1.getId(), orderTable2.getId());
            OrderStatus orderStatus = OrderStatus.COMPLETION;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                List.of(orderStatus.name()));

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("아이디 목록과 주문 상태 목록을 받아 일치하는 주문이 없으면 false를 반환한다.")
        void success_false() {
            List<Long> orderTableIds = List.of(orderTable2.getId());
            OrderStatus orderStatus = OrderStatus.COMPLETION;

            Boolean actual = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                List.of(orderStatus.name()));

            assertThat(actual).isFalse();
        }
    }
}
