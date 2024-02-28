package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.domain.Servicios;
import io.bootify.hotel_benidorm.model.ServiciosContratadosDTO;
import io.bootify.hotel_benidorm.repos.ReservaRepository;
import io.bootify.hotel_benidorm.repos.ServiciosRepository;
import io.bootify.hotel_benidorm.service.ServiciosContratadosService;
import io.bootify.hotel_benidorm.util.CustomCollectors;
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
@RequestMapping("/serviciosContratadoss")
public class ServiciosContratadosController {

    private final ServiciosContratadosService serviciosContratadosService;
    private final ReservaRepository reservaRepository;
    private final ServiciosRepository serviciosRepository;

    public ServiciosContratadosController(
            final ServiciosContratadosService serviciosContratadosService,
            final ReservaRepository reservaRepository,
            final ServiciosRepository serviciosRepository) {
        this.serviciosContratadosService = serviciosContratadosService;
        this.reservaRepository = reservaRepository;
        this.serviciosRepository = serviciosRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("reservaValues", reservaRepository.findAll(Sort.by("idReserva"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Reserva::getIdReserva, Reserva::getIdReserva)));
        model.addAttribute("servicioValues", serviciosRepository.findAll(Sort.by("idServicio"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Servicios::getIdServicio, Servicios::getTipoServicio)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("serviciosContratadoses", serviciosContratadosService.findAll());
        return "serviciosContratados/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("serviciosContratados") final ServiciosContratadosDTO serviciosContratadosDTO) {
        return "serviciosContratados/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("serviciosContratados") @Valid final ServiciosContratadosDTO serviciosContratadosDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "serviciosContratados/add";
        }
        serviciosContratadosService.create(serviciosContratadosDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("serviciosContratados.create.success"));
        return "redirect:/serviciosContratadoss";
    }

    @GetMapping("/edit/{idServicioContratado}")
    public String edit(
            @PathVariable(name = "idServicioContratado") final Integer idServicioContratado,
            final Model model) {
        model.addAttribute("serviciosContratados", serviciosContratadosService.get(idServicioContratado));
        return "serviciosContratados/edit";
    }

    @PostMapping("/edit/{idServicioContratado}")
    public String edit(
            @PathVariable(name = "idServicioContratado") final Integer idServicioContratado,
            @ModelAttribute("serviciosContratados") @Valid final ServiciosContratadosDTO serviciosContratadosDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "serviciosContratados/edit";
        }
        serviciosContratadosService.update(idServicioContratado, serviciosContratadosDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("serviciosContratados.update.success"));
        return "redirect:/serviciosContratadoss";
    }

    @PostMapping("/delete/{idServicioContratado}")
    public String delete(
            @PathVariable(name = "idServicioContratado") final Integer idServicioContratado,
            final RedirectAttributes redirectAttributes) {
        serviciosContratadosService.delete(idServicioContratado);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("serviciosContratados.delete.success"));
        return "redirect:/serviciosContratadoss";
    }

}
