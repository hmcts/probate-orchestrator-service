package uk.gov.hmcts.probate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.probate.TestUtils;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = {InvitationController.class}, secure = false)
public class InvitationControllerTest {

    @MockBean
    private BusinessService businessService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String invitationStr;
    private Invitation invitation;
    private String invitationResendStr;
    private Invitation invitationResend;

    @Before
    public void setUp() throws Exception {
        invitationStr = TestUtils.getJSONFromFile("invite/invitation.json");
        this.invitation = objectMapper.readValue(invitationStr, Invitation.class);

        invitationResendStr = TestUtils.getJSONFromFile("invite/invitationResend.json");
        this.invitationResend = objectMapper.readValue(invitationResendStr, Invitation.class);
    }

    @Test
    public void sendInvitation_withValidJson_shouldReturn200() throws Exception {

        when(businessService.sendInvitation(eq(invitation), eq("someSessionId"))).thenReturn("12345");

        mockMvc.perform(post(InvitationController.INVITE_BASEURL)
                .header("Session-Id", "someSessionId")
                .content(invitationStr)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).sendInvitation(eq(invitation), eq("someSessionId"));
    }

    @Test
    public void resendInvitation_withValidJson_shouldReturn200() throws Exception {

        when(businessService.resendInvitation(eq("12345"), eq(invitationResend), eq("someSessionId"))).thenReturn("12345");

        mockMvc.perform(post(InvitationController.INVITE_BASEURL + "/12345")
                .header("Session-Id", "someSessionId")
                .content(invitationResendStr)
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(status().isOk());
        verify(businessService, times(1)).resendInvitation(eq("12345"), eq(invitationResend), eq("someSessionId"));
    }

    @Test
    public void pinRequest_shouldReturn200() throws Exception {

        when(businessService.getPinNumber(eq("07987676542"), eq("someSessionId"))).thenReturn("54321");

        mockMvc.perform(get(InvitationController.INVITE_PIN_URL)
                .header("Session-Id", "someSessionId")
                .param("phoneNumber", "07987676542"))
                .andExpect(status().isOk());
        verify(businessService, times(1)).getPinNumber(eq("07987676542"), eq("someSessionId"));
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
        verify(businessService, times(1)).updateContactDetails(eq("12345"), eq(invitation));
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

}
