import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPassType } from 'app/shared/model/pass-type.model';
import { getEntities as getPassTypes } from 'app/entities/pass-type/pass-type.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAssignment } from 'app/shared/model/assignment.model';
import { getEntities as getAssignments } from 'app/entities/assignment/assignment.reducer';
import { IVehicleAssignment } from 'app/shared/model/vehicle-assignment.model';
import { getEntities as getVehicleAssignments } from 'app/entities/vehicle-assignment/vehicle-assignment.reducer';
import { IPass } from 'app/shared/model/pass.model';
import { PassStatusType } from 'app/shared/model/enumerations/pass-status-type.model';
import { getEntity, updateEntity, createEntity, reset } from './pass.reducer';

export const PassUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const passTypes = useAppSelector(state => state.passType.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const assignments = useAppSelector(state => state.assignment.entities);
  const vehicleAssignments = useAppSelector(state => state.vehicleAssignment.entities);
  const passEntity = useAppSelector(state => state.pass.entity);
  const loading = useAppSelector(state => state.pass.loading);
  const updating = useAppSelector(state => state.pass.updating);
  const updateSuccess = useAppSelector(state => state.pass.updateSuccess);
  const passStatusTypeValues = Object.keys(PassStatusType);

  const handleClose = () => {
    navigate('/pass' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPassTypes({}));
    dispatch(getUsers({}));
    dispatch(getAssignments({}));
    dispatch(getVehicleAssignments({}));
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
    if (values.collectedFee !== undefined && typeof values.collectedFee !== 'number') {
      values.collectedFee = Number(values.collectedFee);
    }
    values.fromDate = convertDateTimeToServer(values.fromDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    if (values.passNumber !== undefined && typeof values.passNumber !== 'number') {
      values.passNumber = Number(values.passNumber);
    }

    const entity = {
      ...passEntity,
      ...values,
      passType: passTypes.find(it => it.id.toString() === values.passType?.toString()),
      requestedBy: users.find(it => it.id.toString() === values.requestedBy?.toString()),
      assignment: assignments.find(it => it.id.toString() === values.assignment?.toString()),
      vehicleAssignment: vehicleAssignments.find(it => it.id.toString() === values.vehicleAssignment?.toString()),
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
          fromDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
        }
      : {
          status: 'REQUESTED',
          ...passEntity,
          fromDate: convertDateTimeFromServer(passEntity.fromDate),
          endDate: convertDateTimeFromServer(passEntity.endDate),
          passType: passEntity?.passType?.id,
          requestedBy: passEntity?.requestedBy?.id,
          assignment: passEntity?.assignment?.id,
          vehicleAssignment: passEntity?.vehicleAssignment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.pass.home.createOrEditLabel" data-cy="PassCreateUpdateHeading">
            <Translate contentKey="cpamainApp.pass.home.createOrEditLabel">Create or edit a Pass</Translate>
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
                  id="pass-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.pass.collectedFee')}
                id="pass-collectedFee"
                name="collectedFee"
                data-cy="collectedFee"
                type="text"
              />
              <UncontrolledTooltip target="collectedFeeLabel">
                <Translate contentKey="cpamainApp.pass.help.collectedFee" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.pass.fromDate')}
                id="pass-fromDate"
                name="fromDate"
                data-cy="fromDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('cpamainApp.pass.endDate')}
                id="pass-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('cpamainApp.pass.status')} id="pass-status" name="status" data-cy="status" type="select">
                {passStatusTypeValues.map(passStatusType => (
                  <option value={passStatusType} key={passStatusType}>
                    {translate('cpamainApp.PassStatusType.' + passStatusType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.pass.passNumber')}
                id="pass-passNumber"
                name="passNumber"
                data-cy="passNumber"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.pass.mediaSerial')}
                id="pass-mediaSerial"
                name="mediaSerial"
                data-cy="mediaSerial"
                type="text"
              />
              <ValidatedField
                id="pass-passType"
                name="passType"
                data-cy="passType"
                label={translate('cpamainApp.pass.passType')}
                type="select"
                required
              >
                <option value="" key="0" />
                {passTypes
                  ? passTypes.map(otherEntity => (
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
                id="pass-requestedBy"
                name="requestedBy"
                data-cy="requestedBy"
                label={translate('cpamainApp.pass.requestedBy')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="pass-assignment"
                name="assignment"
                data-cy="assignment"
                label={translate('cpamainApp.pass.assignment')}
                type="select"
                required
              >
                <option value="" key="0" />
                {assignments
                  ? assignments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="pass-vehicleAssignment"
                name="vehicleAssignment"
                data-cy="vehicleAssignment"
                label={translate('cpamainApp.pass.vehicleAssignment')}
                type="select"
              >
                <option value="" key="0" />
                {vehicleAssignments
                  ? vehicleAssignments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pass" replace color="info">
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

export default PassUpdate;
