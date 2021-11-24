package kitchenpos.ui.dto.request.table;

import java.util.List;

public class TableGroupRequestDto {

    private List<Long> orderTables;

    private TableGroupRequestDto() {
    }

    public TableGroupRequestDto(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
