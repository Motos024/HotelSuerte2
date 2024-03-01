package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.domain.*;
import io.bootify.hotel_benidorm.model.CaracteristicasHabitacionDTO;
import io.bootify.hotel_benidorm.model.ReservaDTO;
import io.bootify.hotel_benidorm.model.ServiciosContratadosDTO;
import io.bootify.hotel_benidorm.model.ServiciosDTO;
import io.bootify.hotel_benidorm.repos.DescuentoRepository;
import io.bootify.hotel_benidorm.repos.HabitacionRepository;
import io.bootify.hotel_benidorm.repos.ReservaRepository;
import io.bootify.hotel_benidorm.repos.UsuarioRepository;
import io.bootify.hotel_benidorm.service.*;
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

import java.util.*;
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

    private final HabitacionService habitacionService;

    private final ServiciosContratadosService serviciosContratadosService;

    private final ReservaRepository reservaRepository;

    public ReservaController(final ReservaService reservaService,
                             final UsuarioRepository usuarioRepository,
                             final HabitacionRepository habitacionRepository,
                             final DescuentoRepository descuentoRepository,
                             final CaracteristicasHabitacionService caracteristicasHabitacionService,
                             final ServiciosService serviciosService,
    final ServiciosContratadosService serviciosContratadosService,
    final ReservaRepository reservaRepository,

    final HabitacionService habitacionService) {
        this.reservaService = reservaService;
        this.usuarioRepository = usuarioRepository;
        this.habitacionRepository = habitacionRepository;
        this.descuentoRepository = descuentoRepository;
        this.caracteristicasHabitacionService=caracteristicasHabitacionService;
        this.serviciosService=serviciosService;
        this.habitacionService=habitacionService;
        this.serviciosContratadosService=serviciosContratadosService;
        this.reservaRepository=reservaRepository;
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


    @GetMapping("/add/cliente")
    public String addCliente(Model model) {
        String email = ReservaService.obtenerNombreUsuarioActual();
        Integer idUsuario = usuarioRepository.findIdByEmail(email);

        ReservaDTO reservaDTO = new ReservaDTO();
        List<CaracteristicasHabitacionDTO> caracteristicasHabitacionDTO = caracteristicasHabitacionService.findAll();
        List<ServiciosDTO> total_servicios = serviciosService.findAll();

        caracteristicasHabitacionService.total_tipos_habitacion();

        model.addAttribute("reserva",  reservaService.prereserva(reservaDTO));
        model.addAttribute("tipos_habitacion", caracteristicasHabitacionDTO);
        model.addAttribute("servicios", total_servicios);

        return "reserva/add_cliente";
    }



    @PostMapping("/add/cliente")
    public String procesarSeleccionHabitacionYServicios(
            @RequestParam(name = "idCaracteristicaSeleccionada", required = false) Integer idCaracteristicaSeleccionada,
            @RequestParam(name = "serviciosSeleccionados", required = false) List<Integer> serviciosSeleccionados,
            @ModelAttribute ReservaDTO reservaDTO, Model model,RedirectAttributes redirectAttributes
            ) {

        redirectAttributes.addFlashAttribute("reserva", reservaDTO);
        redirectAttributes.addFlashAttribute("tipos_habitacion", idCaracteristicaSeleccionada);
        redirectAttributes.addFlashAttribute("servicios", serviciosSeleccionados);

        return "redirect:pago";
    }


    @GetMapping("/add/pago")
    public String addPago() {

        return "reserva/pago";
    }

    @PostMapping("/add/pago")
    public String procesarpago(
            @RequestParam(name = "idCaracteristicaSeleccionada", required = false) Integer idCaracteristicaSeleccionada,
            @RequestParam(name = "serviciosSeleccionados", required = false) String serviciosSeleccionadosStr,
            @ModelAttribute ReservaDTO reservaDTO
    ) {


        List<Integer> serviciosSeleccionados = new ArrayList<>();
        if (serviciosSeleccionadosStr != null && !serviciosSeleccionadosStr.isEmpty()) {
            // Convertir la cadena a una lista de Integer
            serviciosSeleccionados = Arrays.stream(serviciosSeleccionadosStr.replace("[", "").replace("]", "").split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }


        String email = ReservaService.obtenerNombreUsuarioActual();
        Integer idUsuario = usuarioRepository.findIdByEmail(email);

        Date fechaInicio = Date.from(reservaDTO.getFechaInicio().toInstant());
        Date fechaFin = Date.from(reservaDTO.getFechaFin().toInstant());





        reservaDTO.setUsuario(idUsuario);
        reservaDTO.setHabitacion(habitacionService.findHabitacionesDisponibles(idCaracteristicaSeleccionada, fechaInicio, fechaFin));
        reservaService.create(reservaDTO);

        ServiciosContratadosDTO serviciosContratadosDTO = new ServiciosContratadosDTO();
        serviciosContratadosDTO.setReserva( reservaRepository.findUltimaReservaPorUsuarioNative(idUsuario));


        if (serviciosSeleccionados != null && !serviciosSeleccionados.isEmpty()) {
            serviciosSeleccionados.forEach(servicioId -> {
                serviciosContratadosDTO.setServicio(servicioId);
                serviciosContratadosService.create(serviciosContratadosDTO);
            });
        } else {
            System.out.println("No se seleccionaron servicios.");
        }


        return "redirect:/";
    }

    @GetMapping("/add/reservascliente")
    public String verClienteReserca(Model model) {
        String email = ReservaService.obtenerNombreUsuarioActual();
        Integer idUsuario = usuarioRepository.findIdByEmail(email);


        List<ReservaDTO> reservasTotalesPorId = reservaService.findReservasByUsuarioId(idUsuario);

        System.out.println(reservasTotalesPorId);
        model.addAttribute("reservas_totales", reservasTotalesPorId);

        return "reserva/ver_reservas_cliente";
    }
}
