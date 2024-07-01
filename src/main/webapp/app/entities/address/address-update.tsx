import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICityCorpPoura } from 'app/shared/model/city-corp-poura.model';
import { getEntities as getCityCorpPouras } from 'app/entities/city-corp-poura/city-corp-poura.reducer';
import { IUnion } from 'app/shared/model/union.model';
import { getEntities as getUnions } from 'app/entities/union/union.reducer';
import { IPostOffice } from 'app/shared/model/post-office.model';
import { getEntities as getPostOffices } from 'app/entities/post-office/post-office.reducer';
import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { IPerson } from 'app/shared/model/person.model';
import { getEntities as getPeople } from 'app/entities/person/person.reducer';
import { IAgency } from 'app/shared/model/agency.model';
import { getEntities as getAgencies } from 'app/entities/agency/agency.reducer';
import { IAddress } from 'app/shared/model/address.model';
import { AddressType } from 'app/shared/model/enumerations/address-type.model';
import { getEntity, updateEntity, createEntity, reset } from './address.reducer';

export const AddressUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cityCorpPouras = useAppSelector(state => state.cityCorpPoura.entities);
  const unions = useAppSelector(state => state.union.entities);
  const postOffices = useAppSelector(state => state.postOffice.entities);
  const countries = useAppSelector(state => state.country.entities);
  const people = useAppSelector(state => state.person.entities);
  const agencies = useAppSelector(state => state.agency.entities);
  const addressEntity = useAppSelector(state => state.address.entity);
  const loading = useAppSelector(state => state.address.loading);
  const updating = useAppSelector(state => state.address.updating);
  const updateSuccess = useAppSelector(state => state.address.updateSuccess);
  const addressTypeValues = Object.keys(AddressType);

  const handleClose = () => {
    navigate('/address' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCityCorpPouras({}));
    dispatch(getUnions({}));
    dispatch(getPostOffices({}));
    dispatch(getCountries({}));
    dispatch(getPeople({}));
    dispatch(getAgencies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...addressEntity,
      ...values,
      cityCorpPoura: cityCorpPouras.find(it => it.id.toString() === values.cityCorpPoura?.toString()),
      union: unions.find(it => it.id.toString() === values.union?.toString()),
      postOffice: postOffices.find(it => it.id.toString() === values.postOffice?.toString()),
      country: countries.find(it => it.id.toString() === values.country?.toString()),
      person: people.find(it => it.id.toString() === values.person?.toString()),
      agency: agencies.find(it => it.id.toString() === values.agency?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          addressType: 'PRESENT',
          ...addressEntity,
          cityCorpPoura: addressEntity?.cityCorpPoura?.id,
          union: addressEntity?.union?.id,
          postOffice: addressEntity?.postOffice?.id,
          country: addressEntity?.country?.id,
          person: addressEntity?.person?.id,
          agency: addressEntity?.agency?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.address.home.createOrEditLabel" data-cy="AddressCreateUpdateHeading">
            <Translate contentKey="cpamainApp.address.home.createOrEditLabel">Create or edit a Address</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="address-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.address.addressLineOne')}
                id="address-addressLineOne"
                name="addressLineOne"
                data-cy="addressLineOne"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpamainApp.address.addressLineTwo')}
                id="address-addressLineTwo"
                name="addressLineTwo"
                data-cy="addressLineTwo"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.address.addressLineThree')}
                id="address-addressLineThree"
                name="addressLineThree"
                data-cy="addressLineThree"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.address.addressType')}
                id="address-addressType"
                name="addressType"
                data-cy="addressType"
                type="select"
              >
                {addressTypeValues.map(addressType => (
                  <option value={addressType} key={addressType}>
                    {translate('cpamainApp.AddressType.' + addressType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="address-cityCorpPoura"
                name="cityCorpPoura"
                data-cy="cityCorpPoura"
                label={translate('cpamainApp.address.cityCorpPoura')}
                type="select"
              >
                <option value="" key="0" />
                {cityCorpPouras
                  ? cityCorpPouras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="address-union" name="union" data-cy="union" label={translate('cpamainApp.address.union')} type="select">
                <option value="" key="0" />
                {unions
                  ? unions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="address-postOffice"
                name="postOffice"
                data-cy="postOffice"
                label={translate('cpamainApp.address.postOffice')}
                type="select"
                required
              >
                <option value="" key="0" />
                {postOffices
                  ? postOffices.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="address-country"
                name="country"
                data-cy="country"
                label={translate('cpamainApp.address.country')}
                type="select"
              >
                <option value="" key="0" />
                {countries
                  ? countries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="address-person"
                name="person"
                data-cy="person"
                label={translate('cpamainApp.address.person')}
                type="select"
              >
                <option value="" key="0" />
                {people
                  ? people.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="address-agency"
                name="agency"
                data-cy="agency"
                label={translate('cpamainApp.address.agency')}
                type="select"
              >
                <option value="" key="0" />
                {agencies
                  ? agencies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/address" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AddressUpdate;
