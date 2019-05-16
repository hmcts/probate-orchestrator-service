package uk.gov.hmcts.probate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.probate.service.BusinessService;
import uk.gov.hmcts.reform.probate.model.multiapplicant.Invitation;
import uk.gov.hmcts.reform.probate.model.multiapplicant.InviteData;

import javax.validation.Valid;

@RestController
public class InvitationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitationController.class);

    @Autowired
    BusinessService businessService;


    @PostMapping(path = "/invite",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String invite(@Valid @RequestBody Invitation invitation,
                         @RequestHeader("Session-Id") String sessionId) {
        return businessService.sendInvitation(invitation, sessionId);
    }

    @PostMapping(path = "/invite/{inviteId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String invite(@PathVariable("inviteId") String inviteId,
                         @Valid @RequestBody Invitation invitation,
                         @RequestHeader("Session-Id") String sessionId) {
        return businessService.resendInvite(inviteId, invitation, sessionId);
    }

    @GetMapping(path = "/invites/allAgreed/{formdataId}")
    public Boolean invitesAllAgreed(@PathVariable("formdataId") String formdataId) {
        return businessService.haveAllIniviteesAgreed(formdataId);
    }

    @PostMapping(path = "/invite/agreed/{formdataId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String inviteAgreed(@PathVariable("formdataId") String formdataId,
                               @Valid @RequestBody Invitation invitation) {
        return businessService.inviteAgreed(formdataId, invitation);
    }

    @PutMapping(path = "/invite/contactdetails/{formdataId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateContactDetails(@PathVariable("formdataId") String formdataId,
                                       @Valid @RequestBody Invitation invitation) {
        return businessService.updateContactDetails(formdataId, invitation);
    }

    @PutMapping(path = "/invite/resetAgreed/{formdataId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String resetAgreedFlags(@PathVariable("formdataId") String formdataId) {
        businessService.resetAgreedFlags(formdataId);
        return formdataId;
    }


    @PutMapping(path = "/invite/delete/{formdataId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String deleteInvite(@PathVariable("formdataId") String formdataId,
                               @Valid @RequestBody Invitation invitation) {
        businessService.resetAgreedFlags(formdataId);
        return formdataId;
    }

    @GetMapping(path = "/invitedata/{inviteId}")
    public InviteData invitedata(@PathVariable String inviteId) {
        //return businessService.inviteData(inviteId);
        return null;
    }

}
