package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.domain.CaracteristicasHabitacion;
import io.bootify.hotel_benidorm.model.CaracteristicasHabitacionDTO;
import io.bootify.hotel_benidorm.model.ReservaDTO;
import io.bootify.hotel_benidorm.service.CaracteristicasHabitacionService;
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

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/caracteristicasHabitacions")
public class CaracteristicasHabitacionController {

    private final CaracteristicasHabitacionService caracteristicasHabitacionService;

    public CaracteristicasHabitacionController(
            final CaracteristicasHabitacionService caracteristicasHabitacionService) {
        this.caracteristicasHabitacionService = caracteristicasHabitacionService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("caracteristicasHabitacions", caracteristicasHabitacionService.findAll());
        return "caracteristicasHabitacion/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("caracteristicasHabitacion") final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO) {
        return "caracteristicasHabitacion/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("caracteristicasHabitacion") @Valid final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "caracteristicasHabitacion/add";
        }
        caracteristicasHabitacionService.create(caracteristicasHabitacionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("caracteristicasHabitacion.create.success"));
        return "redirect:/caracteristicasHabitacions";
    }

    @GetMapping("/edit/{idCaracteristica}")
    public String edit(@PathVariable(name = "idCaracteristica") final Integer idCaracteristica,
            final Model model) {
        model.addAttribute("caracteristicasHabitacion", caracteristicasHabitacionService.get(idCaracteristica));
        return "caracteristicasHabitacion/edit";
    }

    @PostMapping("/edit/{idCaracteristica}")
    public String edit(@PathVariable(name = "idCaracteristica") final Integer idCaracteristica,
            @ModelAttribute("caracteristicasHabitacion") @Valid final CaracteristicasHabitacionDTO caracteristicasHabitacionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "caracteristicasHabitacion/edit";
        }
        caracteristicasHabitacionService.update(idCaracteristica, caracteristicasHabitacionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("caracteristicasHabitacion.update.success"));
        return "redirect:/caracteristicasHabitacions";
    }

    @PostMapping("/delete/{idCaracteristica}")
    public String delete(@PathVariable(name = "idCaracteristica") final Integer idCaracteristica,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = caracteristicasHabitacionService.getReferencedWarning(idCaracteristica);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            caracteristicasHabitacionService.delete(idCaracteristica);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("caracteristicasHabitacion.delete.success"));
        }
        return "redirect:/caracteristicasHabitacions";
    }




}
