package io.bootify.hotel_benidorm.service;

import io.bootify.hotel_benidorm.domain.Descuento;
import io.bootify.hotel_benidorm.domain.Reserva;
import io.bootify.hotel_benidorm.model.DescuentoDTO;
import io.bootify.hotel_benidorm.repos.DescuentoRepository;
import io.bootify.hotel_benidorm.repos.ReservaRepository;
import io.bootify.hotel_benidorm.util.NotFoundException;
import io.bootify.hotel_benidorm.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DescuentoService {

    private final DescuentoRepository descuentoRepository;
    private final ReservaRepository reservaRepository;

    public DescuentoService(final DescuentoRepository descuentoRepository,
            final ReservaRepository reservaRepository) {
        this.descuentoRepository = descuentoRepository;
        this.reservaRepository = reservaRepository;
    }

    public List<DescuentoDTO> findAll() {
        final List<Descuento> descuentoes = descuentoRepository.findAll(Sort.by("idDescuento"));
        return descuentoes.stream()
                .map(descuento -> mapToDTO(descuento, new DescuentoDTO()))
                .toList();
    }

    public DescuentoDTO get(final Integer idDescuento) {
        return descuentoRepository.findById(idDescuento)
                .map(descuento -> mapToDTO(descuento, new DescuentoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DescuentoDTO descuentoDTO) {
        final Descuento descuento = new Descuento();
        mapToEntity(descuentoDTO, descuento);
        return descuentoRepository.save(descuento).getIdDescuento();
    }

    public void update(final Integer idDescuento, final DescuentoDTO descuentoDTO) {
        final Descuento descuento = descuentoRepository.findById(idDescuento)
                .orElseThrow(NotFoundException::new);
        mapToEntity(descuentoDTO, descuento);
        descuentoRepository.save(descuento);
    }

    public void delete(final Integer idDescuento) {
        descuentoRepository.deleteById(idDescuento);
    }

    private DescuentoDTO mapToDTO(final Descuento descuento, final DescuentoDTO descuentoDTO) {
        descuentoDTO.setIdDescuento(descuento.getIdDescuento());
        descuentoDTO.setTipo(descuento.getTipo());
        descuentoDTO.setPorcentaje(descuento.getPorcentaje());
        descuentoDTO.setActivo(descuento.getActivo());
        descuentoDTO.setCodigoPromo(descuento.getCodigoPromo());
        return descuentoDTO;
    }

    private Descuento mapToEntity(final DescuentoDTO descuentoDTO, final Descuento descuento) {
        descuento.setTipo(descuentoDTO.getTipo());
        descuento.setPorcentaje(descuentoDTO.getPorcentaje());
        descuento.setActivo(descuentoDTO.getActivo());
        descuento.setCodigoPromo(descuentoDTO.getCodigoPromo());
        return descuento;
    }

    public ReferencedWarning getReferencedWarning(final Integer idDescuento) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Descuento descuento = descuentoRepository.findById(idDescuento)
                .orElseThrow(NotFoundException::new);
        final Reserva descuentoReserva = reservaRepository.findFirstByDescuento(descuento);
        if (descuentoReserva != null) {
            referencedWarning.setKey("descuento.reserva.descuento.referenced");
            referencedWarning.addParam(descuentoReserva.getIdReserva());
            return referencedWarning;
        }
        return null;
    }

}
