package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CityCorpPoura;
import com.mycompany.myapp.repository.CityCorpPouraRepository;
import com.mycompany.myapp.service.CityCorpPouraService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.CityCorpPoura}.
 */
@Service
@Transactional
public class CityCorpPouraServiceImpl implements CityCorpPouraService {

    private final Logger log = LoggerFactory.getLogger(CityCorpPouraServiceImpl.class);

    private final CityCorpPouraRepository cityCorpPouraRepository;

    public CityCorpPouraServiceImpl(CityCorpPouraRepository cityCorpPouraRepository) {
        this.cityCorpPouraRepository = cityCorpPouraRepository;
    }

    @Override
    public CityCorpPoura save(CityCorpPoura cityCorpPoura) {
        log.debug("Request to save CityCorpPoura : {}", cityCorpPoura);
        return cityCorpPouraRepository.save(cityCorpPoura);
    }

    @Override
    public CityCorpPoura update(CityCorpPoura cityCorpPoura) {
        log.debug("Request to update CityCorpPoura : {}", cityCorpPoura);
        return cityCorpPouraRepository.save(cityCorpPoura);
    }

    @Override
    public Optional<CityCorpPoura> partialUpdate(CityCorpPoura cityCorpPoura) {
        log.debug("Request to partially update CityCorpPoura : {}", cityCorpPoura);

        return cityCorpPouraRepository
            .findById(cityCorpPoura.getId())
            .map(existingCityCorpPoura -> {
                if (cityCorpPoura.getName() != null) {
                    existingCityCorpPoura.setName(cityCorpPoura.getName());
                }

                return existingCityCorpPoura;
            })
            .map(cityCorpPouraRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CityCorpPoura> findAll() {
        log.debug("Request to get all CityCorpPouras");
        return cityCorpPouraRepository.findAll();
    }

    public Page<CityCorpPoura> findAllWithEagerRelationships(Pageable pageable) {
        return cityCorpPouraRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CityCorpPoura> findOne(Long id) {
        log.debug("Request to get CityCorpPoura : {}", id);
        return cityCorpPouraRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CityCorpPoura : {}", id);
        cityCorpPouraRepository.deleteById(id);
    }
}
