package it.unisalento.pas.taxbe.controllersTest;

import it.unisalento.pas.taxbe.controllers.StatsController;
import it.unisalento.pas.taxbe.domains.FeeStatistics;
import it.unisalento.pas.taxbe.dto.FeeStatisticsDTO;
import it.unisalento.pas.taxbe.services.IStatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStatsService statsService;

    @Test
    void getByPaidStatusForAllTest() throws Exception {
        FeeStatistics statistics = new FeeStatistics();
        statistics.setPaid(1);
        statistics.setTotalSortedTax(100);
        statistics.setTotalUnsortedTax(50);
        statistics.setTotalSortedWaste(200);
        statistics.setTotalUnsortedWaste(100);
        statistics.setYear(2023);

        when(statsService.getSumOfAllFeesByPayment(anyInt(), anyInt())).thenReturn(statistics);

        mockMvc.perform(get("/api/stats/all/2023/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paid").value(1))
                .andExpect(jsonPath("$.totalSortedTax").value(100))
                .andExpect(jsonPath("$.totalUnsortedTax").value(50))
                .andExpect(jsonPath("$.totalSortedWaste").value(200))
                .andExpect(jsonPath("$.totalUnsortedWaste").value(100))
                .andExpect(jsonPath("$.year").value(2023));
    }

    @Test
    void getByPaidStatusForUserTest() throws Exception {
        FeeStatistics statistics = new FeeStatistics();
        statistics.setPaid(1);
        statistics.setTotalSortedTax(100);
        statistics.setTotalUnsortedTax(50);
        statistics.setTotalSortedWaste(200);
        statistics.setTotalUnsortedWaste(100);
        statistics.setYear(2023);

        when(statsService.getSumOfAllUserFeesByPayment(anyString(), anyInt(), anyInt())).thenReturn(statistics);

        mockMvc.perform(get("/api/stats/user/mockUserID/2023/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paid").value(1))
                .andExpect(jsonPath("$.totalSortedTax").value(100))
                .andExpect(jsonPath("$.totalUnsortedTax").value(50))
                .andExpect(jsonPath("$.totalSortedWaste").value(200))
                .andExpect(jsonPath("$.totalUnsortedWaste").value(100))
                .andExpect(jsonPath("$.year").value(2023));
    }
}
