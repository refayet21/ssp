import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicleType } from 'app/shared/model/vehicle-type.model';
import { getEntities as getVehicleTypes } from 'app/entities/vehicle-type/vehicle-type.reducer';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { LogStatusType } from 'app/shared/model/enumerations/log-status-type.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle.reducer';

export const VehicleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleTypes = useAppSelector(state => state.vehicleType.entities);
  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  const loading = useAppSelector(state => state.vehicle.loading);
  const updating = useAppSelector(state => state.vehicle.updating);
  const updateSuccess = useAppSelector(state => state.vehicle.updateSuccess);
  const logStatusTypeValues = Object.keys(LogStatusType);

  const handleClose = () => {
    navigate('/vehicle' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVehicleTypes({}));
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
      ...vehicleEntity,
      ...values,
      vehicleType: vehicleTypes.find(it => it.id.toString() === values.vehicleType?.toString()),
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
          ...vehicleEntity,
          vehicleType: vehicleEntity?.vehicleType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.vehicle.home.createOrEditLabel" data-cy="VehicleCreateUpdateHeading">
            <Translate contentKey="cpamainApp.vehicle.home.createOrEditLabel">Create or edit a Vehicle</Translate>
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
                  id="vehicle-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('cpamainApp.vehicle.name')} id="vehicle-name" name="name" data-cy="name" type="text" />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.vehicle.help.name" />
              </UncontrolledTooltip>
              <ValidatedField label={translate('cpamainApp.vehicle.regNo')} id="vehicle-regNo" name="regNo" data-cy="regNo" type="text" />
              <ValidatedField label={translate('cpamainApp.vehicle.zone')} id="vehicle-zone" name="zone" data-cy="zone" type="text" />
              <ValidatedField
                label={translate('cpamainApp.vehicle.category')}
                id="vehicle-category"
                name="category"
                data-cy="category"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicle.serialNo')}
                id="vehicle-serialNo"
                name="serialNo"
                data-cy="serialNo"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicle.vehicleNo')}
                id="vehicle-vehicleNo"
                name="vehicleNo"
                data-cy="vehicleNo"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicle.chasisNo')}
                id="vehicle-chasisNo"
                name="chasisNo"
                data-cy="chasisNo"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicle.isPersonal')}
                id="vehicle-isPersonal"
                name="isPersonal"
                data-cy="isPersonal"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicle.isBlackListed')}
                id="vehicle-isBlackListed"
                name="isBlackListed"
                data-cy="isBlackListed"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicle.logStatus')}
                id="vehicle-logStatus"
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
                id="vehicle-vehicleType"
                name="vehicleType"
                data-cy="vehicleType"
                label={translate('cpamainApp.vehicle.vehicleType')}
                type="select"
              >
                <option value="" key="0" />
                {vehicleTypes
                  ? vehicleTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle" replace color="info">
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

export default VehicleUpdate;
