package ro.teamnet.bootstrap.web.rest.account;

import com.codahale.metrics.annotation.Timed;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.service.AccountService;
import ro.teamnet.bootstrap.service.MailService;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(value = "/app/rest/publicAccount")
public class PublicAccountResource{

    private AccountService accountService;


    @Inject
    public PublicAccountResource(AccountService accountService) {
        this.accountService=accountService;
    }

    @Inject
    private ServletContext servletContext;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SpringTemplateEngine templateEngine;

    @Inject
    private MailService mailService;


    /**
     * POST  /rest/register -> register the user.
     *
     * TODO refactor to execute in a single transaction
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody AccountDTO accountDTO, HttpServletRequest request,
                                    HttpServletResponse response) {
        Account account = accountService.findOne(accountDTO.getId());
        if (account != null) {
            return new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST);
        } else {
            if (accountService.findOneByEmail(accountDTO.getEmail()) != null) {
                return new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST);
            }
            account = accountService.createUserInformation(accountDTO.getLogin(), accountDTO.getPassword(), accountDTO.getFirstName(),
                    accountDTO.getLastName(), accountDTO.getEmail().toLowerCase(), accountDTO.getLangKey(), accountDTO.getGender());
            final Locale locale = Locale.forLanguageTag(account.getLangKey());
            String content = createHtmlContentFromTemplate(account, locale, request, response);
            mailService.sendActivationEmail(account.getEmail(), content, locale);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    private String createHtmlContentFromTemplate(final Account account, final Locale locale, final HttpServletRequest request,
                                                 final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", account);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
                request.getServerName() +       // "myhost"
                ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
                locale, variables, applicationContext);
        return templateEngine.process("activationEmail", context);
    }

    /**
     * GET  /rest/authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        return request.getRemoteUser();
    }


}
