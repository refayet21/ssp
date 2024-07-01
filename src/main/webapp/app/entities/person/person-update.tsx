import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { IPerson } from 'app/shared/model/person.model';
import { LogStatusType } from 'app/shared/model/enumerations/log-status-type.model';
import { getEntity, updateEntity, createEntity, reset } from './person.reducer';

export const PersonUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const countries = useAppSelector(state => state.country.entities);
  const personEntity = useAppSelector(state => state.person.entity);
  const loading = useAppSelector(state => state.person.loading);
  const updating = useAppSelector(state => state.person.updating);
  const updateSuccess = useAppSelector(state => state.person.updateSuccess);
  const logStatusTypeValues = Object.keys(LogStatusType);

  const handleClose = () => {
    navigate('/person' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCountries({}));
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
      ...personEntity,
      ...values,
      internalUser: users.find(it => it.id.toString() === values.internalUser?.toString()),
      nationality: countries.find(it => it.id.toString() === values.nationality?.toString()),
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
          logStatus: 'DRAFT',
          ...personEntity,
          internalUser: personEntity?.internalUser?.id,
          nationality: personEntity?.nationality?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.person.home.createOrEditLabel" data-cy="PersonCreateUpdateHeading">
            <Translate contentKey="cpamainApp.person.home.createOrEditLabel">Create or edit a Person</Translate>
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
                  id="person-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.person.name')}
                id="person-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpamainApp.person.shortName')}
                id="person-shortName"
                name="shortName"
                data-cy="shortName"
                type="text"
                validate={{
                  maxLength: { value: 30, message: translate('entity.validation.maxlength', { max: 30 }) },
                }}
              />
              <ValidatedField
                label={translate('cpamainApp.person.dob')}
                id="person-dob"
                name="dob"
                data-cy="dob"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpamainApp.person.email')}
                id="person-email"
                name="email"
                data-cy="email"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('cpamainApp.person.isBlackListed')}
                id="person-isBlackListed"
                name="isBlackListed"
                data-cy="isBlackListed"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('cpamainApp.person.fatherName')}
                id="person-fatherName"
                name="fatherName"
                data-cy="fatherName"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.person.motherName')}
                id="person-motherName"
                name="motherName"
                data-cy="motherName"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.person.logStatus')}
                id="person-logStatus"
                name="logStatus"
                data-cy="logStatus"
                type="select"
              >
                {logStatusTypeValues.map(logStatusType => (
                  <option value={logStatusType} key={logStatusType}>
                    {translate('cpamainApp.LogStatusType.' + logStatusType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="person-internalUser"
                name="internalUser"
                data-cy="internalUser"
                label={translate('cpamainApp.person.internalUser')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="person-nationality"
                name="nationality"
                data-cy="nationality"
                label={translate('cpamainApp.person.nationality')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/person" replace color="info">
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

export default PersonUpdate;
