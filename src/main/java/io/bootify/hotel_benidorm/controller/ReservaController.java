package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.domain.Descuento;
import io.bootify.hotel_benidorm.domain.Habitacion;
import io.bootify.hotel_benidorm.domain.Usuario;

import io.bootify.hotel_benidorm.model.CaracteristicasHabitacionDTO;
import io.bootify.hotel_benidorm.model.ReservaDTO;
import io.bootify.hotel_benidorm.model.ServiciosDTO;
import io.bootify.hotel_benidorm.repos.DescuentoRepository;
import io.bootify.hotel_benidorm.repos.HabitacionRepository;
import io.bootify.hotel_benidorm.repos.UsuarioRepository;
import io.bootify.hotel_benidorm.service.CaracteristicasHabitacionService;
import io.bootify.hotel_benidorm.service.ReservaService;
import io.bootify.hotel_benidorm.service.ServiciosService;
import io.bootify.hotel_benidorm.util.CustomCollectors;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import io.bootify.hotel_benidorm.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.BooleanUtils.forEach;


@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioRepository usuarioRepository;
    private final HabitacionRepository habitacionRepository;
    private final DescuentoRepository descuentoRepository;

    private final CaracteristicasHabitacionService caracteristicasHabitacionService;

    private final ServiciosService serviciosService;

    public ReservaController(final ReservaService reservaService,
                             final UsuarioRepository usuarioRepository,
                             final HabitacionRepository habitacionRepository,
                             final DescuentoRepository descuentoRepository, final CaracteristicasHabitacionService caracteristicasHabitacionService, final ServiciosService serviciosService
                             ) {
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
        this.habitacionRepository = habitacionRepository;
        this.descuentoRepository = descuentoRepository;
        this.caracteristicasHabitacionService=caracteristicasHabitacionService;
        this.serviciosService=serviciosService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("usuarioValues", usuarioRepository.findAll(Sort.by("idUsuario"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getIdUsuario, Usuario::getNombre)));
        model.addAttribute("habitacionValues", habitacionRepository.findAll(Sort.by("idHabitacion"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Habitacion::getIdHabitacion, Habitacion::getIdHabitacion)));
        model.addAttribute("descuentoValues", descuentoRepository.findAll(Sort.by("idDescuento"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Descuento::getIdDescuento, Descuento::getTipo)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("reservas", reservaService.findAll());
        return "reserva/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("reserva") final ReservaDTO reservaDTO) {
        return "reserva/add";
    }



    @PostMapping("/add")
    public String add(@ModelAttribute("reserva") @Valid final ReservaDTO reservaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reserva/add";
        }
        reservaService.create(reservaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reserva.create.success"));
        return "redirect:/reservas";
    }

    @GetMapping("/edit/{idReserva}")
    public String edit(@PathVariable(name = "idReserva") final Integer idReserva,
            final Model model) {
        model.addAttribute("reserva", reservaService.get(idReserva));
        return "reserva/edit";
    }

    @PostMapping("/edit/{idReserva}")
    public String edit(@PathVariable(name = "idReserva") final Integer idReserva,
            @ModelAttribute("reserva") @Valid final ReservaDTO reservaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reserva/edit";
        }
        reservaService.update(idReserva, reservaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reserva.update.success"));
        return "redirect:/reservas";
    }

    @PostMapping("/delete/{idReserva}")
    public String delete(@PathVariable(name = "idReserva") final Integer idReserva,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = reservaService.getReferencedWarning(idReserva);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            reservaService.delete(idReserva);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("reserva.delete.success"));
        }
        return "redirect:/reservas";
    }

 /*   @GetMapping("/add/cliente")

    public String addCliente(@ModelAttribute("reserva") final ReservaDTO reservaDTO,
                             @ModelAttribute("tipos_habitacion") final List<CaracteristicasHabitacionDTO> caracteristicasHabitacionDTO ) {
        String email = ReservaService.obtenerNombreUsuarioActual();
       Integer idUsuario = usuarioRepository.findIdByEmail(email);
        reservaService.prereserva(reservaDTO);
        caracteristicasHabitacionService.total_tipos_habitacion(caracteristicasHabitacionDTO);

        System.out.println("El nombre del usuario actual es: " + idUsuario);
        System.out.println(caracteristicasHabitacionService.findAll());
        System.out.println(caracteristicasHabitacionService.total_tipos_habitacion(caracteristicasHabitacionDTO));
        System.out.println(reservaDTO);

        return "reserva/add_cliente";
    }*/

    @GetMapping("/add/cliente")
    public String addCliente(Model model) {
        String email = ReservaService.obtenerNombreUsuarioActual();
        Integer idUsuario = usuarioRepository.findIdByEmail(email);

        ReservaDTO reservaDTO = new ReservaDTO(); // Asume que tienes una instancia de esto
        List<CaracteristicasHabitacionDTO> caracteristicasHabitacionDTO = caracteristicasHabitacionService.findAll();
        List<ServiciosDTO> total_servicios = serviciosService.findAll();

        //reservaService.prereserva(reservaDTO);
        caracteristicasHabitacionService.total_tipos_habitacion();


        System.out.println("El nombre del usuario actual es: " + idUsuario);
        //System.out.println(caracteristicasHabitacionDTO);
        model.addAttribute("reserva",  reservaService.prereserva(reservaDTO));
        model.addAttribute("tipos_habitacion", caracteristicasHabitacionDTO);
        model.addAttribute("servicios", total_servicios);

        return "reserva/add_cliente";
    }


 /*   @PostMapping("/add/cliente")
    public String addCliente(@ModelAttribute("reserva") @Valid final ReservaDTO reservaDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "reserva/add";
        }
        reservaService.create(reservaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("reserva.create.success"));
        return "redirect:/reservas";
    }*/



    @PostMapping("/add/cliente")
    public String procesarSeleccionHabitacionYServicios(
            @RequestParam(name = "idCaracteristicaSeleccionada", required = false) Integer idCaracteristicaSeleccionada,
            @RequestParam(name = "serviciosSeleccionados", required = false) List<Integer> serviciosSeleccionados,
            @ModelAttribute ReservaDTO reservaDTO, Model model
            ) {

        // Procesar la característica de habitación seleccionada
        if (idCaracteristicaSeleccionada != null) {
            System.out.println("Característica de habitación seleccionada: ID=" + idCaracteristicaSeleccionada);
        } else {
            System.out.println("No se seleccionó ninguna característica de habitación.");
        }

        // Procesar los servicios seleccionados
        if (serviciosSeleccionados != null && !serviciosSeleccionados.isEmpty()) {
            serviciosSeleccionados.forEach(servicioId ->
                    System.out.println("Servicio seleccionado: ID=" + servicioId));
        } else {
            System.out.println("No se seleccionaron servicios.");
        }

        System.out.println(reservaDTO);
        // Redireccionar a una página de confirmación o mostrar mensaje de éxito
        return "redirect:/reservas";
    }


}
