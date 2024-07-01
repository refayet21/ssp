import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVehicleType } from 'app/shared/model/vehicle-type.model';
import { getEntity, updateEntity, createEntity, reset } from './vehicle-type.reducer';

export const VehicleTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vehicleTypeEntity = useAppSelector(state => state.vehicleType.entity);
  const loading = useAppSelector(state => state.vehicleType.loading);
  const updating = useAppSelector(state => state.vehicleType.updating);
  const updateSuccess = useAppSelector(state => state.vehicleType.updateSuccess);

  const handleClose = () => {
    navigate('/vehicle-type');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.numberOfOperators !== undefined && typeof values.numberOfOperators !== 'number') {
      values.numberOfOperators = Number(values.numberOfOperators);
    }

    const entity = {
      ...vehicleTypeEntity,
      ...values,
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
          ...vehicleTypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.vehicleType.home.createOrEditLabel" data-cy="VehicleTypeCreateUpdateHeading">
            <Translate contentKey="cpamainApp.vehicleType.home.createOrEditLabel">Create or edit a VehicleType</Translate>
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
                  id="vehicle-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.vehicleType.name')}
                id="vehicle-type-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.vehicleType.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.vehicleType.numberOfOperators')}
                id="vehicle-type-numberOfOperators"
                name="numberOfOperators"
                data-cy="numberOfOperators"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vehicle-type" replace color="info">
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

export default VehicleTypeUpdate;
