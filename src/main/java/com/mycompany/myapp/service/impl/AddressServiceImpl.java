package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Address;
import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.domain.Upazila;
import com.mycompany.myapp.repository.AddressRepository;
import com.mycompany.myapp.service.AddressService;
import com.mycompany.myapp.service.dto.AddressDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Address}.
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(Address address) {
        log.debug("Request to save Address : {}", address);
        return addressRepository.save(address);
    }

    @Override
    public Address update(Address address) {
        log.debug("Request to update Address : {}", address);
        return addressRepository.save(address);
    }

    @Override
    public Optional<Address> partialUpdate(Address address) {
        log.debug("Request to partially update Address : {}", address);

        return addressRepository
            .findById(address.getId())
            .map(existingAddress -> {
                if (address.getAddressLineOne() != null) {
                    existingAddress.setAddressLineOne(address.getAddressLineOne());
                }
                if (address.getAddressLineTwo() != null) {
                    existingAddress.setAddressLineTwo(address.getAddressLineTwo());
                }
                if (address.getAddressLineThree() != null) {
                    existingAddress.setAddressLineThree(address.getAddressLineThree());
                }
                if (address.getAddressType() != null) {
                    existingAddress.setAddressType(address.getAddressType());
                }

                return existingAddress;
            })
            .map(addressRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Address> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll(pageable);
    }

    public Page<Address> findAllWithEagerRelationships(Pageable pageable) {
        return addressRepository.findAllWithEagerRelationships(pageable);
    }

    //    @Override
    //    @Transactional(readOnly = true)
    //    public Optional<Address> findOne(Long id) {
    //        log.debug("Request to get Address : {}", id);
    //        return addressRepository.findOneWithEagerRelationships(id);
    //    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        Optional<Address> addressOptional = addressRepository.findOneWithEagerRelationships(id);
        if (!addressOptional.isPresent()) {
            return Optional.empty();
        }

        Address address = addressOptional.get();
        AddressDTO addressDTO = new AddressDTO();
        // Map address properties to DTO
        addressDTO.setId(address.getId());
        addressDTO.setAddressLineOne(address.getAddressLineOne());
        // ... (map other address properties)

        // Access related entities for union, upazila, and district names
        Union union = address.getUnion();
        if (union != null) {
            addressDTO.setUnionName(union.getName());
        }
        Upazila upazila = address.getUnion().getUpazila(); // Assuming upazila is accessed through union
        if (upazila != null) {
            addressDTO.setUpazilaName(upazila.getName());
            District district = upazila.getDistrict(); // Assuming district is accessed through upazila
            if (district != null) {
                addressDTO.setDistrictName(district.getName());
            }
        }

        return Optional.of(addressDTO);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
    }
}
