import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILane } from 'app/shared/model/lane.model';
import { getEntities as getLanes } from 'app/entities/lane/lane.reducer';
import { IAccessProfile } from 'app/shared/model/access-profile.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';
import { getEntity, updateEntity, createEntity, reset } from './access-profile.reducer';

export const AccessProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const lanes = useAppSelector(state => state.lane.entities);
  const accessProfileEntity = useAppSelector(state => state.accessProfile.entity);
  const loading = useAppSelector(state => state.accessProfile.loading);
  const updating = useAppSelector(state => state.accessProfile.updating);
  const updateSuccess = useAppSelector(state => state.accessProfile.updateSuccess);
  const actionTypeValues = Object.keys(ActionType);

  const handleClose = () => {
    navigate('/access-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLanes({}));
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
    if (values.startTimeOfDay !== undefined && typeof values.startTimeOfDay !== 'number') {
      values.startTimeOfDay = Number(values.startTimeOfDay);
    }
    if (values.endTimeOfDay !== undefined && typeof values.endTimeOfDay !== 'number') {
      values.endTimeOfDay = Number(values.endTimeOfDay);
    }
    if (values.dayOfWeek !== undefined && typeof values.dayOfWeek !== 'number') {
      values.dayOfWeek = Number(values.dayOfWeek);
    }

    const entity = {
      ...accessProfileEntity,
      ...values,
      lanes: mapIdList(values.lanes),
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
          action: 'ALLOW',
          ...accessProfileEntity,
          lanes: accessProfileEntity?.lanes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.accessProfile.home.createOrEditLabel" data-cy="AccessProfileCreateUpdateHeading">
            <Translate contentKey="cpamainApp.accessProfile.home.createOrEditLabel">Create or edit a AccessProfile</Translate>
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
                  id="access-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.accessProfile.name')}
                id="access-profile-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.accessProfile.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.accessProfile.description')}
                id="access-profile-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.accessProfile.startTimeOfDay')}
                id="access-profile-startTimeOfDay"
                name="startTimeOfDay"
                data-cy="startTimeOfDay"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.accessProfile.endTimeOfDay')}
                id="access-profile-endTimeOfDay"
                name="endTimeOfDay"
                data-cy="endTimeOfDay"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.accessProfile.dayOfWeek')}
                id="access-profile-dayOfWeek"
                name="dayOfWeek"
                data-cy="dayOfWeek"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.accessProfile.action')}
                id="access-profile-action"
                name="action"
                data-cy="action"
                type="select"
              >
                {actionTypeValues.map(actionType => (
                  <option value={actionType} key={actionType}>
                    {translate('cpamainApp.ActionType.' + actionType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.accessProfile.lane')}
                id="access-profile-lane"
                data-cy="lane"
                type="select"
                multiple
                name="lanes"
              >
                <option value="" key="0" />
                {lanes
                  ? lanes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/access-profile" replace color="info">
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

export default AccessProfileUpdate;
