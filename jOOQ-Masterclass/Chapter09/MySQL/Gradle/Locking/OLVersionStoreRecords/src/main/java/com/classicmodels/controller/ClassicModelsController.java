package com.classicmodels.controller;

import com.classicmodels.service.ClassicModelsService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jooq.generated.tables.records.PaymentRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({ClassicModelsController.PAYMENT_ATTR})
public class ClassicModelsController {

    protected static final String ALL_PAYMENT_ATTR = "all";
    protected static final String PAYMENT_ATTR = "payment";
    private static final String BINDING_RESULT = "org.springframework.validation.BindingResult." + PAYMENT_ATTR;

    private final ClassicModelsService classicModelsService;

    public ClassicModelsController(ClassicModelsService classicModelsService) {
        this.classicModelsService = classicModelsService;
    }

    // load all payments for customer 103
    @GetMapping("/payments")
    public String loadAllPayment(Model model) {

        model.addAttribute(ALL_PAYMENT_ATTR,
                classicModelsService.loadAllPayment103());

        if (!model.containsAttribute(PAYMENT_ATTR)) {
            PaymentRecord pr = new PaymentRecord();
            pr.setCustomerNumber(103L);
            pr.setCheckNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            pr.setInvoiceAmount(BigDecimal.ONE);
            pr.setCachingDate(LocalDateTime.now());

            model.addAttribute(PAYMENT_ATTR, pr);
        }

        return "payments";
    }

    @GetMapping("/payment/{nr}/{ch}")
    public String loadPayment(@PathVariable(name = "nr") Long nr,
            @PathVariable(name = "ch") String ch, Model model) {

        model.addAttribute(PAYMENT_ATTR,
                classicModelsService.loadPayment(nr, ch));

        return "redirect:/payments";
    }

    @PostMapping("/store")
    public String storePayment(SessionStatus sessionStatus, RedirectAttributes redirectAttributes,
            @ModelAttribute(PAYMENT_ATTR) PaymentRecord pr, BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            try {
                classicModelsService.storePayment(pr);
                sessionStatus.setComplete();
            } catch (org.jooq.exception.DataChangedException e) {
                bindingResult.reject("", "Another user updated the data.");
            }
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BINDING_RESULT, bindingResult);            
        }

        return "redirect:payments";
    }

    @GetMapping(value = "/refresh")
    public String refreshPayment(Model model) {

        if (model.containsAttribute(PAYMENT_ATTR)) {
            classicModelsService.refreshPayment(
                    (PaymentRecord) model.getAttribute(PAYMENT_ATTR));
        } 

        return "redirect:payments";
    }
    
    @InitBinder
    void allowFields(WebDataBinder webDataBinder) {
        webDataBinder.setAllowedFields("checkNumber", "customerNumber",
                "invoiceAmount", "${_csrf.parameterName}");
    }
}
