import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IZone } from 'app/shared/model/zone.model';
import { getEntities as getZones } from 'app/entities/zone/zone.reducer';
import { IGate } from 'app/shared/model/gate.model';
import { GateType } from 'app/shared/model/enumerations/gate-type.model';
import { getEntity, updateEntity, createEntity, reset } from './gate.reducer';

export const GateUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const zones = useAppSelector(state => state.zone.entities);
  const gateEntity = useAppSelector(state => state.gate.entity);
  const loading = useAppSelector(state => state.gate.loading);
  const updating = useAppSelector(state => state.gate.updating);
  const updateSuccess = useAppSelector(state => state.gate.updateSuccess);
  const gateTypeValues = Object.keys(GateType);

  const handleClose = () => {
    navigate('/gate');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getZones({}));
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
    if (values.lat !== undefined && typeof values.lat !== 'number') {
      values.lat = Number(values.lat);
    }
    if (values.lon !== undefined && typeof values.lon !== 'number') {
      values.lon = Number(values.lon);
    }

    const entity = {
      ...gateEntity,
      ...values,
      zone: zones.find(it => it.id.toString() === values.zone?.toString()),
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
          gateType: 'HUMAN',
          ...gateEntity,
          zone: gateEntity?.zone?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.gate.home.createOrEditLabel" data-cy="GateCreateUpdateHeading">
            <Translate contentKey="cpamainApp.gate.home.createOrEditLabel">Create or edit a Gate</Translate>
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
                  id="gate-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('cpamainApp.gate.name')} id="gate-name" name="name" data-cy="name" type="text" />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.gate.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.gate.shortName')}
                id="gate-shortName"
                name="shortName"
                data-cy="shortName"
                type="text"
              />
              <ValidatedField label={translate('cpamainApp.gate.lat')} id="gate-lat" name="lat" data-cy="lat" type="text" />
              <ValidatedField label={translate('cpamainApp.gate.lon')} id="gate-lon" name="lon" data-cy="lon" type="text" />
              <ValidatedField
                label={translate('cpamainApp.gate.gateType')}
                id="gate-gateType"
                name="gateType"
                data-cy="gateType"
                type="select"
              >
                {gateTypeValues.map(gateType => (
                  <option value={gateType} key={gateType}>
                    {translate('cpamainApp.GateType.' + gateType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.gate.isActive')}
                id="gate-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField id="gate-zone" name="zone" data-cy="zone" label={translate('cpamainApp.gate.zone')} type="select">
                <option value="" key="0" />
                {zones
                  ? zones.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/gate" replace color="info">
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

export default GateUpdate;
