package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.dto.product.ProductResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductRestController.class)
class ProductRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @DisplayName("Product 생성 요청 테스트")
    @Test
    void create() throws Exception {
        ProductResponse productResponse = new ProductResponse(1L, "강정치킨", BigDecimal.valueOf(17000));

        given(productService.create(any())).willReturn(productResponse);

        mockMvc.perform(post("/api/products")
                .content("{\n"
                        + "  \"name\": \"강정치킨\",\n"
                        + "  \"price\": 17000\n"
                        + "}")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(header().string("Location", "/api/products/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.instanceOf(Number.class)))
                .andExpect(jsonPath("$.name", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.price", Matchers.instanceOf(Number.class)));
    }

    @DisplayName("Product 목록 조회 요청 테스트")
    @Test
    void list() throws Exception {
        List<ProductResponse> productResponses = new ArrayList<>();
        productResponses.add(new ProductResponse(1L, "강정치킨", BigDecimal.valueOf(17000)));
        productResponses.add(new ProductResponse(2L, "양념치킨", BigDecimal.valueOf(17000)));

        given(productService.list()).willReturn(productResponses);

        mockMvc.perform(get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }
}