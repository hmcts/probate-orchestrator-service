package uk.gov.hmcts.probate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;
import uk.gov.hmcts.reform.probate.model.multiapplicant.InvitationsResult;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class InvitationController {

    protected static final String INVITE_BASEURL = "/invite";
    protected static final String INVITES_BASEURL = "/invites";
    protected static final String INVITE_ALLAGREED_URL = INVITE_BASEURL + "/allAgreed";
    protected static final String INVITE_AGREED_URL = INVITE_BASEURL + "/agreed";
    protected static final String INVITE_RESET_AGREED_URL = INVITE_BASEURL + "/resetAgreed";
    protected static final String INVITE_CONTACTDETAILS_URL = INVITE_BASEURL + "/contactdetails";
    protected static final String INVITE_DELETE_URL = INVITE_BASEURL + "/delete";
    protected static final String INVITE_DATA_URL = INVITE_BASEURL + "/data";
    protected static final String INVITE_PIN_URL = INVITE_BASEURL + "/pin";
    protected static final String INVITE_PIN_BILINGUAL_URL = INVITE_PIN_URL + "/bilingual";
    protected static final String INVITE_BILINGUAL_URL = INVITE_BASEURL + "/bilingual";

    @Autowired
    BusinessService businessService;

    @PostMapping(path = INVITE_BASEURL,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public InvitationsResult invite(@Valid @RequestBody List<Invitation> invitations,
                                    @RequestHeader("Session-Id") String sessionId) {
        return InvitationsResult.builder()
            .invitations(businessService.sendInvitations(invitations, sessionId, Boolean.FALSE)).build();
    }

    @PostMapping(path = INVITE_BILINGUAL_URL,
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public InvitationsResult inviteBilingual(@Valid @RequestBody List<Invitation> invitations,
                                             @RequestHeader("Session-Id") String sessionId) {
        return InvitationsResult.builder()
            .invitations(businessService.sendInvitations(invitations, sessionId, Boolean.TRUE)).build();
    }


    @GetMapping(path = INVITE_ALLAGREED_URL + "/{formdataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean invitesAllAgreed(@PathVariable("formdataId") String formdataId) {
        return businessService.haveAllIniviteesAgreed(formdataId);
    }

    @PostMapping(path = INVITE_AGREED_URL + "/{formdataId}",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String inviteAgreed(@PathVariable("formdataId") String formdataId,
                               @Valid @RequestBody Invitation invitation) {
        return businessService.inviteAgreed(formdataId, invitation);
    }

    @PostMapping(path = INVITE_CONTACTDETAILS_URL + "/{formdataId}",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String updateContactDetails(@PathVariable("formdataId") String formdataId,
                                       @Valid @RequestBody Invitation invitation) {
        return businessService.updateContactDetails(formdataId, invitation);
    }

    @PostMapping(path = INVITE_RESET_AGREED_URL + "/{formdataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String resetAgreedFlags(@PathVariable("formdataId") String formdataId) {
        businessService.resetAgreedFlags(formdataId);
        return formdataId;
    }

    @PostMapping(path = INVITE_DELETE_URL + "/{formdataId}",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteInvite(@PathVariable("formdataId") String formdataId,
                               @Valid @RequestBody Invitation invitation) {
        businessService.deleteInvite(formdataId, invitation);
        return formdataId;
    }

    @GetMapping(path = INVITE_DATA_URL + "/{inviteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Invitation invitedata(@PathVariable String inviteId) {
        return businessService.getInviteData(inviteId);
    }

    @GetMapping(path = INVITES_BASEURL + "/{formdataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvitationsResult getAllInviteData(@PathVariable String formdataId) {
        return InvitationsResult.builder().invitations(businessService.getAllInviteData(formdataId)).build();
    }

    @GetMapping(path = INVITE_PIN_URL)
    public String invitePin(@RequestParam("phoneNumber") String phoneNumber,
                            @RequestHeader("Session-Id") String sessionId) {
        return businessService.getPinNumber(phoneNumber, sessionId, Boolean.FALSE);
    }

    @GetMapping(path = INVITE_PIN_BILINGUAL_URL)
    public String invitePinBilingual(@RequestParam("phoneNumber") String phoneNumber,
                                     @RequestHeader("Session-Id") String sessionId) {
        return businessService.getPinNumber(phoneNumber, sessionId, Boolean.TRUE);
    }


}
