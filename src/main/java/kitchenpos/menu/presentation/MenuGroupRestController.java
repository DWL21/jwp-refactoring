package kitchenpos.menu.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.presentation.dto.MenuGroupCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuGroupRestController {
    private final MenuGroupService menuGroupService;

    public MenuGroupRestController(final MenuGroupService menuGroupService) {
        this.menuGroupService = menuGroupService;
    }

    @PostMapping("/api/menu-groups")
    public ResponseEntity<MenuGroupResponse> create(@RequestBody MenuGroupCreateRequest request) {
        MenuGroupResponse response = menuGroupService.create(request.getName());
        final URI uri = URI.create("/api/menu-groups/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/menu-groups")
    public ResponseEntity<List<MenuGroupResponse>> list() {
        return ResponseEntity.ok().body(menuGroupService.list());
    }
}
