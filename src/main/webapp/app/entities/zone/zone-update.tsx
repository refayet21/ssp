import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAgency } from 'app/shared/model/agency.model';
import { getEntities as getAgencies } from 'app/entities/agency/agency.reducer';
import { IZone } from 'app/shared/model/zone.model';
import { getEntity, updateEntity, createEntity, reset } from './zone.reducer';

export const ZoneUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agencies = useAppSelector(state => state.agency.entities);
  const zoneEntity = useAppSelector(state => state.zone.entity);
  const loading = useAppSelector(state => state.zone.loading);
  const updating = useAppSelector(state => state.zone.updating);
  const updateSuccess = useAppSelector(state => state.zone.updateSuccess);

  const handleClose = () => {
    navigate('/zone');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
      ...zoneEntity,
      ...values,
      authority: agencies.find(it => it.id.toString() === values.authority?.toString()),
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
          ...zoneEntity,
          authority: zoneEntity?.authority?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.zone.home.createOrEditLabel" data-cy="ZoneCreateUpdateHeading">
            <Translate contentKey="cpamainApp.zone.home.createOrEditLabel">Create or edit a Zone</Translate>
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
                  id="zone-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('cpamainApp.zone.name')} id="zone-name" name="name" data-cy="name" type="text" />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.zone.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.zone.shortName')}
                id="zone-shortName"
                name="shortName"
                data-cy="shortName"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.zone.location')}
                id="zone-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.zone.isActive')}
                id="zone-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                id="zone-authority"
                name="authority"
                data-cy="authority"
                label={translate('cpamainApp.zone.authority')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/zone" replace color="info">
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

export default ZoneUpdate;
