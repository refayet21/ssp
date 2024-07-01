import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAgencyType } from 'app/shared/model/agency-type.model';
import { getEntities as getAgencyTypes } from 'app/entities/agency-type/agency-type.reducer';
import { IPassType } from 'app/shared/model/pass-type.model';
import { getEntities as getPassTypes } from 'app/entities/pass-type/pass-type.reducer';
import { IAgency } from 'app/shared/model/agency.model';
import { getEntity, updateEntity, createEntity, reset } from './agency.reducer';

export const AgencyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agencyTypes = useAppSelector(state => state.agencyType.entities);
  const passTypes = useAppSelector(state => state.passType.entities);
  const agencyEntity = useAppSelector(state => state.agency.entity);
  const loading = useAppSelector(state => state.agency.loading);
  const updating = useAppSelector(state => state.agency.updating);
  const updateSuccess = useAppSelector(state => state.agency.updateSuccess);

  const handleClose = () => {
    navigate('/agency' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAgencyTypes({}));
    dispatch(getPassTypes({}));
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
      ...agencyEntity,
      ...values,
      agencyType: agencyTypes.find(it => it.id.toString() === values.agencyType?.toString()),
      passTypes: mapIdList(values.passTypes),
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
          ...agencyEntity,
          agencyType: agencyEntity?.agencyType?.id,
          passTypes: agencyEntity?.passTypes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.agency.home.createOrEditLabel" data-cy="AgencyCreateUpdateHeading">
            <Translate contentKey="cpamainApp.agency.home.createOrEditLabel">Create or edit a Agency</Translate>
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
                  id="agency-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.agency.name')}
                id="agency-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.agency.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.agency.shortName')}
                id="agency-shortName"
                name="shortName"
                data-cy="shortName"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.agency.isInternal')}
                id="agency-isInternal"
                name="isInternal"
                data-cy="isInternal"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('cpamainApp.agency.isDummy')}
                id="agency-isDummy"
                name="isDummy"
                data-cy="isDummy"
                check
                type="checkbox"
              />
              <ValidatedField
                id="agency-agencyType"
                name="agencyType"
                data-cy="agencyType"
                label={translate('cpamainApp.agency.agencyType')}
                type="select"
                required
              >
                <option value="" key="0" />
                {agencyTypes
                  ? agencyTypes.map(otherEntity => (
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
                label={translate('cpamainApp.agency.passType')}
                id="agency-passType"
                data-cy="passType"
                type="select"
                multiple
                name="passTypes"
              >
                <option value="" key="0" />
                {passTypes
                  ? passTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/agency" replace color="info">
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

export default AgencyUpdate;
