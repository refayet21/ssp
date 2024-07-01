import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAgency } from 'app/shared/model/agency.model';
import { getEntities as getAgencies } from 'app/entities/agency/agency.reducer';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { getEntities as getVehicles } from 'app/entities/vehicle/vehicle.reducer';
import { IVehicleAssignment } from 'app/shared/model/vehicle-assignment.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-assignment.reducer';

export const VehicleAssignmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agencies = useAppSelector(state => state.agency.entities);
  const vehicles = useAppSelector(state => state.vehicle.entities);
  const vehicleAssignmentEntity = useAppSelector(state => state.vehicleAssignment.entity);
  const loading = useAppSelector(state => state.vehicleAssignment.loading);
  const updating = useAppSelector(state => state.vehicleAssignment.updating);
  const updateSuccess = useAppSelector(state => state.vehicleAssignment.updateSuccess);

  const handleClose = () => {
    navigate('/vehicle-assignment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAgencies({}));
    dispatch(getVehicles({}));
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
      ...vehicleAssignmentEntity,
      ...values,
      agency: agencies.find(it => it.id.toString() === values.agency?.toString()),
      vehicle: vehicles.find(it => it.id.toString() === values.vehicle?.toString()),
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
          ...vehicleAssignmentEntity,
          agency: vehicleAssignmentEntity?.agency?.id,
          vehicle: vehicleAssignmentEntity?.vehicle?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.vehicleAssignment.home.createOrEditLabel" data-cy="VehicleAssignmentCreateUpdateHeading">
            <Translate contentKey="cpamainApp.vehicleAssignment.home.createOrEditLabel">Create or edit a VehicleAssignment</Translate>
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
                  id="vehicle-assignment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.vehicleAssignment.startDate')}
                id="vehicle-assignment-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('cpamainApp.vehicleAssignment.endDate')}
                id="vehicle-assignment-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicleAssignment.isPrimary')}
                id="vehicle-assignment-isPrimary"
                name="isPrimary"
                data-cy="isPrimary"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('cpamainApp.vehicleAssignment.isRental')}
                id="vehicle-assignment-isRental"
                name="isRental"
                data-cy="isRental"
                check
                type="checkbox"
              />
              <ValidatedField
                id="vehicle-assignment-agency"
                name="agency"
                data-cy="agency"
                label={translate('cpamainApp.vehicleAssignment.agency')}
                type="select"
              >
                <option value="" key="0" />
                {agencies
                  ? agencies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="vehicle-assignment-vehicle"
                name="vehicle"
                data-cy="vehicle"
                label={translate('cpamainApp.vehicleAssignment.vehicle')}
                type="select"
              >
                <option value="" key="0" />
                {vehicles
                  ? vehicles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-assignment" replace color="info">
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

export default VehicleAssignmentUpdate;
