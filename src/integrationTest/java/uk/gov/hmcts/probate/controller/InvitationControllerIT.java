package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvitationControllerIT {

    @MockBean
    private BusinessService businessService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String invitationStr;
    private Invitation invitation;
    private Invitation invitation2;
    private List<Invitation> invitationsResult;
    private String invitationResendStr;
    private Invitation invitationResend;
    private String invitationArrayStr;
    private String invitationResendArrayStr;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        invitationStr = TestUtils.getJsonFromFile("invite/invitation.json");
        this.invitation = objectMapper.readValue(invitationStr, Invitation.class);
        this.invitation2 = objectMapper.readValue(invitationStr, Invitation.class);
        invitationArrayStr = new StringBuilder("[").append(invitationStr).append("]").toString();
        invitationsResult = new ArrayList();
        invitationsResult.add(invitation);
        invitationsResult.add(invitation2);

        invitationResendStr = TestUtils.getJsonFromFile("invite/invitationResend.json");
        invitationResendArrayStr = new StringBuilder("[").append(invitationResendStr).append("]").toString();
        this.invitationResend = objectMapper.readValue(invitationResendStr, Invitation.class);
    }

    @Test
    public void sendInvitation_withValidJson_shouldReturn200() throws Exception {

        mockMvc.perform(post(InvitationController.INVITE_BASEURL)
                .header("Session-Id", "someSessionId")
                .content(invitationArrayStr)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).sendInvitations(eq(Arrays.asList(invitation)),
            eq("someSessionId"), eq(Boolean.FALSE));
    }

    @Test
    public void sendBilingualInvitation_withValidJson_shouldReturn200() throws Exception {

        mockMvc.perform(post(InvitationController.INVITE_BILINGUAL_URL)
                .header("Session-Id", "someSessionId")
                .content(invitationArrayStr)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).sendInvitations(eq(Arrays.asList(invitation)),
            eq("someSessionId"), eq(Boolean.TRUE));
    }

    @Test
    public void resendInvitation_withValidJson_shouldReturn200() throws Exception {

        when(businessService.resendInvitation(eq("12345"), eq(invitationResend), eq("someSessionId")))
            .thenReturn("12345");

        mockMvc.perform(post(InvitationController.INVITE_BASEURL)
                .header("Session-Id", "someSessionId")
                .content(invitationResendArrayStr)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).sendInvitations(eq(Arrays.asList(invitationResend)),
            eq("someSessionId"), eq(Boolean.FALSE));
    }

    @Test
    public void pinRequest_shouldReturn200() throws Exception {

        when(businessService.getPinNumber(eq("07987676542"), eq("someSessionId"), eq(Boolean.FALSE)))
            .thenReturn("54321");

        mockMvc.perform(get(InvitationController.INVITE_PIN_URL)
                .header("Session-Id", "someSessionId")
                .param("phoneNumber", "07987676542"))
                .andExpect(status().isOk());
        verify(businessService, times(1)).getPinNumber(eq("07987676542"),
            eq("someSessionId"), eq(Boolean.FALSE));
    }

    @Test
    public void bilingualPinRequest_shouldReturn200() throws Exception {

        when(businessService.getPinNumber(eq("07987676542"), eq("someSessionId"),
            eq(Boolean.TRUE))).thenReturn("54321");

        mockMvc.perform(get(InvitationController.INVITE_PIN_BILINGUAL_URL)
                .header("Session-Id", "someSessionId")
                .param("phoneNumber", "07987676542"))
                .andExpect(status().isOk());
        verify(businessService, times(1)).getPinNumber(eq("07987676542"),
            eq("someSessionId"), eq(Boolean.TRUE));
    }

    @Test
    public void inviteAgreed_withValidJson_shouldReturn200() throws Exception {

        when(businessService.inviteAgreed(eq("12345"), eq(invitation))).thenReturn("54321");

        mockMvc.perform(post(InvitationController.INVITE_AGREED_URL + "/12345")
                .header("Session-Id", "someSessionId")
                .content(invitationStr)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).inviteAgreed(eq("12345"), eq(invitation));
    }

    @Test
    public void inviteResetAgreed_shouldReturn200() throws Exception {

        mockMvc.perform(post(InvitationController.INVITE_RESET_AGREED_URL + "/12345"))
                .andExpect(status().isOk());
        verify(businessService, times(1)).resetAgreedFlags(eq("12345"));
    }

    @Test
    public void inviteAllAgreed_shouldReturn200() throws Exception {

        when(businessService.haveAllIniviteesAgreed(eq("12345"))).thenReturn(Boolean.TRUE);
        mockMvc.perform(get(InvitationController.INVITE_ALLAGREED_URL + "/12345"))
                .andExpect(status().isOk());
        verify(businessService, times(1)).haveAllIniviteesAgreed(eq("12345"));
    }

    @Test
    public void updateContactDetails_withValidJson_shouldReturn200() throws Exception {

        when(businessService.updateContactDetails(eq("12345"), eq(invitation))).thenReturn("12345");
        mockMvc.perform(post(InvitationController.INVITE_CONTACTDETAILS_URL + "/12345")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .content(invitationStr))
                .andExpect(status().isOk());
        verify(businessService, times(1)).updateContactDetails(eq("12345"),
            eq(invitation));
    }

    @Test
    public void deleteInvitation_withValidJson_shouldReturn200() throws Exception {

        when(businessService.deleteInvite(eq("12345"), eq(invitation))).thenReturn("12345");
        mockMvc.perform(post(InvitationController.INVITE_DELETE_URL + "/12345")
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .content(invitationStr))
                .andExpect(status().isOk());
        verify(businessService, times(1)).deleteInvite(eq("12345"), eq(invitation));
    }

    @Test
    public void getInvitationDetails_shouldReturn200() throws Exception {

        when(businessService.getInviteData(eq("12345"))).thenReturn(invitation);
        mockMvc.perform(get(InvitationController.INVITE_DATA_URL + "/12345"))
                .andExpect(status().isOk());
        verify(businessService, times(1)).getInviteData(eq("12345"));
    }

    @Test
    public void getAllInviteData_shouldReturn200() throws Exception {

        when(businessService.getAllInviteData(eq("12345"))).thenReturn(invitationsResult);
        mockMvc.perform(get(InvitationController.INVITES_BASEURL + "/12345"))
                .andExpect(status().isOk());
        verify(businessService, times(1)).getAllInviteData(eq("12345"));
    }

}
