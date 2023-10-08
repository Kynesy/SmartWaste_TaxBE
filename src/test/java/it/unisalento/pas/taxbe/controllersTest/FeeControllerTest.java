package it.unisalento.pas.taxbe.controllersTest;

import com.nimbusds.jose.shaded.gson.Gson;
import it.unisalento.pas.taxbe.configurations.SecurityConstants;
import it.unisalento.pas.taxbe.domains.Fee;
import it.unisalento.pas.taxbe.domains.WasteStatistics;
import it.unisalento.pas.taxbe.dto.WasteStatisticsDTO;
import it.unisalento.pas.taxbe.services.IFeeService;
import it.unisalento.pas.taxbe.services.IStatsService;
import it.unisalento.pas.taxbe.utils.FeeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFeeService feeService;

    @MockBean
    private IStatsService statsService;

    @Test
    void createFeeTest() throws Exception {
        WasteStatisticsDTO newWasteStatDTO = new WasteStatisticsDTO();
        newWasteStatDTO.setUserId("mockUserId");
        newWasteStatDTO.setYear(2023);
        newWasteStatDTO.setTotalSortedWaste(200);
        newWasteStatDTO.setTotalUnsortedWaste(200);

        WasteStatistics newWasteStat = new WasteStatistics();
        newWasteStat.setUserId("mockUserId");
        newWasteStat.setYear(2023);
        newWasteStat.setTotalSortedWaste(200);
        newWasteStat.setTotalUnsortedWaste(200);

        WasteStatistics paidStats = new WasteStatistics();
        paidStats.setYear(2023);
        paidStats.setUserId("mockUserId");
        paidStats.setTotalSortedWaste(100);
        paidStats.setTotalUnsortedWaste(100);

        Gson gson = new Gson();
        String json = gson.toJson(new ArrayList<>(List.of(newWasteStatDTO)));


        Fee feetocreate = FeeUtils.calculateFee(newWasteStat, paidStats);
        when(statsService.getAllRegisteredWasteByUserID("mockUserId", 2023)).thenReturn(paidStats);
        when(feeService.createFee(feetocreate)).thenReturn(0);

        mockMvc.perform(post("/api/fee/create/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("admin").authorities(new SimpleGrantedAuthority(SecurityConstants.ADMIN_ROLE_ID))))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFeeTest() throws Exception {
        String feeId = "mockFeeId";

        when(feeService.deleteFee(feeId)).thenReturn(0);

        mockMvc.perform(delete("/api/fee/delete/{feeId}", feeId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority(SecurityConstants.ADMIN_ROLE_ID))))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\": \"Tariffa eliminata con successo\"}"));
    }

    @Test
    void payFeeTest() throws Exception {
        String feeId = "mockFeeId";

        when(feeService.payFee(feeId)).thenReturn(0);

        mockMvc.perform(post("/api/fee/pay/{feeId}", feeId)
                        .with(user("user").authorities(new SimpleGrantedAuthority(SecurityConstants.USER_ROLE_ID))))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\": \"Tariffa pagata\"}"));
    }

    @Test
    void getAllByUserIdTest() throws Exception {
        String userId = "mockUserID";
        Fee fee = new Fee();
        fee.setId("mockFeeId");

        when(feeService.getAllFeeByUserID(userId)).thenReturn(new ArrayList<>(Arrays.asList(fee)));

        mockMvc.perform(get("/api/fee/get/user/{userId}", userId)
                        .with(user("user").authorities(new SimpleGrantedAuthority(SecurityConstants.USER_ROLE_ID))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("mockFeeId"));
    }

    @Test
    void getAllTest() throws Exception {
        Fee fee = new Fee();
        fee.setId("mockFeeId");

        when(feeService.getAllFees()).thenReturn(new ArrayList<>(Arrays.asList(fee)));

        mockMvc.perform(get("/api/fee/get/all")
                        .with(user("admin").authorities(new SimpleGrantedAuthority(SecurityConstants.ADMIN_ROLE_ID))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("mockFeeId"));
    }
}
