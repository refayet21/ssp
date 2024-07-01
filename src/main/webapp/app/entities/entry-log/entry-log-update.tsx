import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPass } from 'app/shared/model/pass.model';
import { getEntities as getPasses } from 'app/entities/pass/pass.reducer';
import { ILane } from 'app/shared/model/lane.model';
import { getEntities as getLanes } from 'app/entities/lane/lane.reducer';
import { IEntryLog } from 'app/shared/model/entry-log.model';
import { DirectionType } from 'app/shared/model/enumerations/direction-type.model';
import { PassStatusType } from 'app/shared/model/enumerations/pass-status-type.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';
import { getEntity, updateEntity, createEntity, reset } from './entry-log.reducer';

export const EntryLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const passes = useAppSelector(state => state.pass.entities);
  const lanes = useAppSelector(state => state.lane.entities);
  const entryLogEntity = useAppSelector(state => state.entryLog.entity);
  const loading = useAppSelector(state => state.entryLog.loading);
  const updating = useAppSelector(state => state.entryLog.updating);
  const updateSuccess = useAppSelector(state => state.entryLog.updateSuccess);
  const directionTypeValues = Object.keys(DirectionType);
  const passStatusTypeValues = Object.keys(PassStatusType);
  const actionTypeValues = Object.keys(ActionType);

  const handleClose = () => {
    navigate('/entry-log' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPasses({}));
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
    values.eventTime = convertDateTimeToServer(values.eventTime);

    const entity = {
      ...entryLogEntity,
      ...values,
      pass: passes.find(it => it.id.toString() === values.pass?.toString()),
      lane: lanes.find(it => it.id.toString() === values.lane?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          eventTime: displayDefaultDateTime(),
        }
      : {
          direction: 'IN',
          passStatus: 'REQUESTED',
          actionType: 'ALLOW',
          ...entryLogEntity,
          eventTime: convertDateTimeFromServer(entryLogEntity.eventTime),
          pass: entryLogEntity?.pass?.id,
          lane: entryLogEntity?.lane?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.entryLog.home.createOrEditLabel" data-cy="EntryLogCreateUpdateHeading">
            <Translate contentKey="cpamainApp.entryLog.home.createOrEditLabel">Create or edit a EntryLog</Translate>
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
                  id="entry-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.entryLog.eventTime')}
                id="entry-log-eventTime"
                name="eventTime"
                data-cy="eventTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <UncontrolledTooltip target="eventTimeLabel">
                <Translate contentKey="cpamainApp.entryLog.help.eventTime" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.entryLog.direction')}
                id="entry-log-direction"
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
                label={translate('cpamainApp.entryLog.passStatus')}
                id="entry-log-passStatus"
                name="passStatus"
                data-cy="passStatus"
                type="select"
              >
                {passStatusTypeValues.map(passStatusType => (
                  <option value={passStatusType} key={passStatusType}>
                    {translate('cpamainApp.PassStatusType.' + passStatusType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.entryLog.actionType')}
                id="entry-log-actionType"
                name="actionType"
                data-cy="actionType"
                type="select"
              >
                {actionTypeValues.map(actionType => (
                  <option value={actionType} key={actionType}>
                    {translate('cpamainApp.ActionType.' + actionType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="entry-log-pass" name="pass" data-cy="pass" label={translate('cpamainApp.entryLog.pass')} type="select">
                <option value="" key="0" />
                {passes
                  ? passes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="entry-log-lane"
                name="lane"
                data-cy="lane"
                label={translate('cpamainApp.entryLog.lane')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/entry-log" replace color="info">
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

export default EntryLogUpdate;
