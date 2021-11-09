package kitchenpos.application.dtos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import lombok.Getter;

@Getter
public class TableGroupResponse {
    private final Long id;
    private final LocalDateTime createdDate;
    private final List<OrderTableResponse> orderTables;

    public TableGroupResponse(TableGroup tableGroup, List<OrderTable> orderTables) {
        this.id = tableGroup.getId();
        this.createdDate = tableGroup.getCreatedDate();
        this.orderTables = orderTables.stream()
                .map(OrderTableResponse::new)
                .collect(Collectors.toList());
    }
}
