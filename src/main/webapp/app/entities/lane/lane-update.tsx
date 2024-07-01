import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGate } from 'app/shared/model/gate.model';
import { getEntities as getGates } from 'app/entities/gate/gate.reducer';
import { IAccessProfile } from 'app/shared/model/access-profile.model';
import { getEntities as getAccessProfiles } from 'app/entities/access-profile/access-profile.reducer';
import { ILane } from 'app/shared/model/lane.model';
import { DirectionType } from 'app/shared/model/enumerations/direction-type.model';
import { getEntity, updateEntity, createEntity, reset } from './lane.reducer';

export const LaneUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const gates = useAppSelector(state => state.gate.entities);
  const accessProfiles = useAppSelector(state => state.accessProfile.entities);
  const laneEntity = useAppSelector(state => state.lane.entity);
  const loading = useAppSelector(state => state.lane.loading);
  const updating = useAppSelector(state => state.lane.updating);
  const updateSuccess = useAppSelector(state => state.lane.updateSuccess);
  const directionTypeValues = Object.keys(DirectionType);

  const handleClose = () => {
    navigate('/lane');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGates({}));
    dispatch(getAccessProfiles({}));
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
      ...laneEntity,
      ...values,
      gate: gates.find(it => it.id.toString() === values.gate?.toString()),
      accessProfiles: mapIdList(values.accessProfiles),
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
          direction: 'IN',
          ...laneEntity,
          gate: laneEntity?.gate?.id,
          accessProfiles: laneEntity?.accessProfiles?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.lane.home.createOrEditLabel" data-cy="LaneCreateUpdateHeading">
            <Translate contentKey="cpamainApp.lane.home.createOrEditLabel">Create or edit a Lane</Translate>
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
                  id="lane-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('cpamainApp.lane.name')} id="lane-name" name="name" data-cy="name" type="text" />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.lane.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.lane.shortName')}
                id="lane-shortName"
                name="shortName"
                data-cy="shortName"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.lane.direction')}
                id="lane-direction"
                name="direction"
                data-cy="direction"
                type="select"
              >
                {directionTypeValues.map(directionType => (
                  <option value={directionType} key={directionType}>
                    {translate('cpamainApp.DirectionType.' + directionType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.lane.isActive')}
                id="lane-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField id="lane-gate" name="gate" data-cy="gate" label={translate('cpamainApp.lane.gate')} type="select">
                <option value="" key="0" />
                {gates
                  ? gates.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.lane.accessProfile')}
                id="lane-accessProfile"
                data-cy="accessProfile"
                type="select"
                multiple
                name="accessProfiles"
              >
                <option value="" key="0" />
                {accessProfiles
                  ? accessProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/lane" replace color="info">
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

export default LaneUpdate;
