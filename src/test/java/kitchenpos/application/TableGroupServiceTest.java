package kitchenpos.application;

import static kitchenpos.support.fixture.domain.OrderTableFixture.GUEST_ONE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.fixture.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;

    @Autowired
    private JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        private OrderTable orderTable1;
        private OrderTable orderTable2;

        @BeforeEach
        void setUp() {
            orderTable1 = jdbcTemplateOrderTableDao.save(GUEST_ONE.getOrderTable());
            orderTable2 = jdbcTemplateOrderTableDao.save(GUEST_ONE.getOrderTable());
        }

        @Test
        @DisplayName("TableGroup을 생성한다.")
        void success() {
            TableGroup tableGroup = tableGroupService.create(TableGroupFixture.getTableGroup(List.of(orderTable1, orderTable2)));

            TableGroup actual = jdbcTemplateTableGroupDao.findById(tableGroup.getId())
                .orElseThrow();

            assertThat(actual).usingRecursiveComparison()
                .ignoringFields("orderTables")
                .isEqualTo(tableGroup);
        }
    }

    @Nested
    @DisplayName("ungroup 메서드는")
    class Ungroup {

        private TableGroup tableGroup;
        private OrderTable orderTable1;

        @BeforeEach
        void setUp() {
            orderTable1 = jdbcTemplateOrderTableDao.save(GUEST_ONE.getOrderTable());
            OrderTable orderTable2 = jdbcTemplateOrderTableDao.save(GUEST_ONE.getOrderTable());
            tableGroup = tableGroupService.create(TableGroupFixture.getTableGroup(List.of(orderTable1, orderTable2)));
        }

        @Test
        @DisplayName("TableGroup ID를 받으면 포함되어 있는 OrderTable들을 그룹해제한다.")
        void success() {
            Long tableGroupId = tableGroup.getId();

            tableGroupService.ungroup(tableGroupId);

            assertThat(orderTable1.getTableGroupId()).isNull();
        }
    }
}
