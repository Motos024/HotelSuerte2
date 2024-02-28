package io.bootify.hotel_benidorm.controller;

import io.bootify.hotel_benidorm.model.RolDTO;
import io.bootify.hotel_benidorm.service.RolService;
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
@RequestMapping("/rols")
public class RolController {

    private final RolService rolService;

    public RolController(final RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("rols", rolService.findAll());
        return "rol/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("rol") final RolDTO rolDTO) {
        return "rol/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("rol") @Valid final RolDTO rolDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rol/add";
        }
        rolService.create(rolDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rol.create.success"));
        return "redirect:/rols";
    }

    @GetMapping("/edit/{idRol}")
    public String edit(@PathVariable(name = "idRol") final Integer idRol, final Model model) {
        model.addAttribute("rol", rolService.get(idRol));
        return "rol/edit";
    }

    @PostMapping("/edit/{idRol}")
    public String edit(@PathVariable(name = "idRol") final Integer idRol,
            @ModelAttribute("rol") @Valid final RolDTO rolDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rol/edit";
        }
        rolService.update(idRol, rolDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rol.update.success"));
        return "redirect:/rols";
    }

    @PostMapping("/delete/{idRol}")
    public String delete(@PathVariable(name = "idRol") final Integer idRol,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = rolService.getReferencedWarning(idRol);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            rolService.delete(idRol);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("rol.delete.success"));
        }
        return "redirect:/rols";
    }

}
