package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.domain.CaracteristicasHabitacion;
import io.bootify.hotel_benidorm.model.HabitacionDTO;
import io.bootify.hotel_benidorm.repos.CaracteristicasHabitacionRepository;
import io.bootify.hotel_benidorm.service.HabitacionService;
import io.bootify.hotel_benidorm.util.CustomCollectors;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import io.bootify.hotel_benidorm.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/habitacions")
public class HabitacionController {

    private final HabitacionService habitacionService;
    private final CaracteristicasHabitacionRepository caracteristicasHabitacionRepository;

    public HabitacionController(final HabitacionService habitacionService,
            final CaracteristicasHabitacionRepository caracteristicasHabitacionRepository) {
        this.habitacionService = habitacionService;
        this.caracteristicasHabitacionRepository = caracteristicasHabitacionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("caracteristicaValues", caracteristicasHabitacionRepository.findAll(Sort.by("idCaracteristica"))
                .stream()
                .collect(CustomCollectors.toSortedMap(CaracteristicasHabitacion::getIdCaracteristica, CaracteristicasHabitacion::getTipo)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("habitacions", habitacionService.findAll());
        return "habitacion/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("habitacion") final HabitacionDTO habitacionDTO) {
        return "habitacion/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("habitacion") @Valid final HabitacionDTO habitacionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "habitacion/add";
        }
        habitacionService.create(habitacionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("habitacion.create.success"));
        return "redirect:/habitacions";
    }

    @GetMapping("/edit/{idHabitacion}")
    public String edit(@PathVariable(name = "idHabitacion") final Integer idHabitacion,
            final Model model) {
        model.addAttribute("habitacion", habitacionService.get(idHabitacion));
        return "habitacion/edit";
    }

    @PostMapping("/edit/{idHabitacion}")
    public String edit(@PathVariable(name = "idHabitacion") final Integer idHabitacion,
            @ModelAttribute("habitacion") @Valid final HabitacionDTO habitacionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "habitacion/edit";
        }
        habitacionService.update(idHabitacion, habitacionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("habitacion.update.success"));
        return "redirect:/habitacions";
    }

    @PostMapping("/delete/{idHabitacion}")
    public String delete(@PathVariable(name = "idHabitacion") final Integer idHabitacion,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = habitacionService.getReferencedWarning(idHabitacion);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            habitacionService.delete(idHabitacion);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("habitacion.delete.success"));
        }
        return "redirect:/habitacions";
    }

}
