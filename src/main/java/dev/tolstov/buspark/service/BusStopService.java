package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.repository.BusStopRepository;
import dev.tolstov.buspark.validation.ValidationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Objects;

@Service
public class BusStopService {

    @Autowired
    BusStopRepository busStopRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    ValidationService validationService;


    public BusStop save(BusStop busStop) {
        validationService.busStopValidation(busStop);
        //если создание то не должно быть записи с таким же уникальным значением
//        id - null, нет записи со значением
        //если обновление то не должно быть записи с таким же уникальным значением
//        id содержит значение, id записи равно id по уникальному значению


        // Ниже попытка уложить в один if этой констролябии
//        if (busStopId == null) {
//            if (busStopRepository.existsByName(busStopName)) {
//                throw new EntityExistsException(
//                        String.format("Bus stop with name \"%s\" exists!", busStopName)
//                );
//            }
//        } else {
//            Long idByName = busStopRepository.getIdByName(busStopName);
//            if (idByName != null && !Objects.equals(idByName, busStopId)) {
//                throw new EntityExistsException(
//                        String.format("Bus stop with name \"%s\" exists!", busStopName)
//                );
//            }
//        }
        String busStopName = busStop.getName();
        Long busStopId = busStop.getId();
        Long idByName = busStopRepository.getIdByName(busStopName);
        if ( busStopId == null && busStopRepository.existsByName(busStopName)
                || idByName != null && !Objects.equals(idByName, busStopId)
        ) {
            throw new EntityExistsException(
                    String.format("Bus stop with name \"%s\" exists!", busStopName)
            );
        }

        addressService.save(busStop.getAddress());
        return busStopRepository.save(busStop);
    }

    public List<BusStop> findAll() {
        return busStopRepository.findAll();
    }

    public BusStop findById(Long busStopId) {
        return busStopRepository.findById(busStopId)
                .orElseThrow(
                        () -> new BPEntityNotFoundException( BusStop.class.getSimpleName(), busStopId)
                );
    }

    public BusStop update(Long id, BusStop busStopInfo) {
        BusStop busStop = findById(id);
        BeanUtils.copyProperties(busStopInfo, busStop);
        return save(busStop);
    }

    public void deleteById(Long busStopId) {
        BusStop byId = findById(busStopId);
        busStopRepository.delete(byId);
    }


    public void deleteAll() {
        busStopRepository.deleteAll();
    }
}
