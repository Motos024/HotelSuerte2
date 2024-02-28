package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.model.DescuentoDTO;
import io.bootify.hotel_benidorm.service.DescuentoService;
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
@RequestMapping("/descuentos")
public class DescuentoController {

    private final DescuentoService descuentoService;

    public DescuentoController(final DescuentoService descuentoService) {
        this.descuentoService = descuentoService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("descuentoes", descuentoService.findAll());
        return "descuento/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("descuento") final DescuentoDTO descuentoDTO) {
        return "descuento/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("descuento") @Valid final DescuentoDTO descuentoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "descuento/add";
        }
        descuentoService.create(descuentoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("descuento.create.success"));
        return "redirect:/descuentos";
    }

    @GetMapping("/edit/{idDescuento}")
    public String edit(@PathVariable(name = "idDescuento") final Integer idDescuento,
            final Model model) {
        model.addAttribute("descuento", descuentoService.get(idDescuento));
        return "descuento/edit";
    }

    @PostMapping("/edit/{idDescuento}")
    public String edit(@PathVariable(name = "idDescuento") final Integer idDescuento,
            @ModelAttribute("descuento") @Valid final DescuentoDTO descuentoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "descuento/edit";
        }
        descuentoService.update(idDescuento, descuentoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("descuento.update.success"));
        return "redirect:/descuentos";
    }

    @PostMapping("/delete/{idDescuento}")
    public String delete(@PathVariable(name = "idDescuento") final Integer idDescuento,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = descuentoService.getReferencedWarning(idDescuento);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            descuentoService.delete(idDescuento);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("descuento.delete.success"));
        }
        return "redirect:/descuentos";
    }

}
