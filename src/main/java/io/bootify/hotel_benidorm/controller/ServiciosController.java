package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.model.ServiciosDTO;
import io.bootify.hotel_benidorm.service.ServiciosService;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import io.bootify.hotel_benidorm.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/servicioss")
public class ServiciosController {

    private final ServiciosService serviciosService;

    public ServiciosController(final ServiciosService serviciosService) {
        this.serviciosService = serviciosService;
    }


    @GetMapping
    public String list(final Model model) {
        System.out.println(serviciosService.findAll());
        model.addAttribute("servicioses", serviciosService.findAll());
        return "servicios/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("servicios") final ServiciosDTO serviciosDTO) {
        return "servicios/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("servicios") @Valid final ServiciosDTO serviciosDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "servicios/add";
        }
        serviciosService.create(serviciosDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("servicios.create.success"));
        return "redirect:/servicioss";
    }

    @GetMapping("/edit/{idServicio}")
    public String edit(@PathVariable(name = "idServicio") final Integer idServicio,
            final Model model) {
        model.addAttribute("servicios", serviciosService.get(idServicio));
        return "servicios/edit";
    }

    @PostMapping("/edit/{idServicio}")
    public String edit(@PathVariable(name = "idServicio") final Integer idServicio,
            @ModelAttribute("servicios") @Valid final ServiciosDTO serviciosDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "servicios/edit";
        }
        serviciosService.update(idServicio, serviciosDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("servicios.update.success"));
        return "redirect:/servicioss";
    }

    @PostMapping("/delete/{idServicio}")
    public String delete(@PathVariable(name = "idServicio") final Integer idServicio,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = serviciosService.getReferencedWarning(idServicio);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            serviciosService.delete(idServicio);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("servicios.delete.success"));
        }
        return "redirect:/servicioss";
    }

}
