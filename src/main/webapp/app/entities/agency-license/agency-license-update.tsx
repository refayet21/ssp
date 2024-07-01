import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAgencyLicenseType } from 'app/shared/model/agency-license-type.model';
import { getEntities as getAgencyLicenseTypes } from 'app/entities/agency-license-type/agency-license-type.reducer';
import { IAgency } from 'app/shared/model/agency.model';
import { getEntities as getAgencies } from 'app/entities/agency/agency.reducer';
import { IAgencyLicense } from 'app/shared/model/agency-license.model';
import { getEntity, updateEntity, createEntity, reset } from './agency-license.reducer';

export const AgencyLicenseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const agencyLicenseTypes = useAppSelector(state => state.agencyLicenseType.entities);
  const agencies = useAppSelector(state => state.agency.entities);
  const agencyLicenseEntity = useAppSelector(state => state.agencyLicense.entity);
  const loading = useAppSelector(state => state.agencyLicense.loading);
  const updating = useAppSelector(state => state.agencyLicense.updating);
  const updateSuccess = useAppSelector(state => state.agencyLicense.updateSuccess);

  const handleClose = () => {
    navigate('/agency-license' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getAgencyLicenseTypes({}));
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
      ...agencyLicenseEntity,
      ...values,
      verifiedBy: users.find(it => it.id.toString() === values.verifiedBy?.toString()),
      agencyLicenseType: agencyLicenseTypes.find(it => it.id.toString() === values.agencyLicenseType?.toString()),
      belongsTo: agencies.find(it => it.id.toString() === values.belongsTo?.toString()),
      issuedBy: agencies.find(it => it.id.toString() === values.issuedBy?.toString()),
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
          ...agencyLicenseEntity,
          verifiedBy: agencyLicenseEntity?.verifiedBy?.id,
          agencyLicenseType: agencyLicenseEntity?.agencyLicenseType?.id,
          belongsTo: agencyLicenseEntity?.belongsTo?.id,
          issuedBy: agencyLicenseEntity?.issuedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.agencyLicense.home.createOrEditLabel" data-cy="AgencyLicenseCreateUpdateHeading">
            <Translate contentKey="cpamainApp.agencyLicense.home.createOrEditLabel">Create or edit a AgencyLicense</Translate>
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
                  id="agency-license-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.agencyLicense.filePath')}
                id="agency-license-filePath"
                name="filePath"
                data-cy="filePath"
                type="text"
              />
              <UncontrolledTooltip target="filePathLabel">
                <Translate contentKey="cpamainApp.agencyLicense.help.filePath" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.agencyLicense.serialNo')}
                id="agency-license-serialNo"
                name="serialNo"
                data-cy="serialNo"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.agencyLicense.issueDate')}
                id="agency-license-issueDate"
                name="issueDate"
                data-cy="issueDate"
                type="date"
              />
              <ValidatedField
                label={translate('cpamainApp.agencyLicense.expiryDate')}
                id="agency-license-expiryDate"
                name="expiryDate"
                data-cy="expiryDate"
                type="date"
              />
              <ValidatedField
                id="agency-license-verifiedBy"
                name="verifiedBy"
                data-cy="verifiedBy"
                label={translate('cpamainApp.agencyLicense.verifiedBy')}
                type="select"
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
              <ValidatedField
                id="agency-license-agencyLicenseType"
                name="agencyLicenseType"
                data-cy="agencyLicenseType"
                label={translate('cpamainApp.agencyLicense.agencyLicenseType')}
                type="select"
                required
              >
                <option value="" key="0" />
                {agencyLicenseTypes
                  ? agencyLicenseTypes.map(otherEntity => (
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
                id="agency-license-belongsTo"
                name="belongsTo"
                data-cy="belongsTo"
                label={translate('cpamainApp.agencyLicense.belongsTo')}
                type="select"
              >
                <option value="" key="0" />
                {agencies
                  ? agencies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="agency-license-issuedBy"
                name="issuedBy"
                data-cy="issuedBy"
                label={translate('cpamainApp.agencyLicense.issuedBy')}
                type="select"
              >
                <option value="" key="0" />
                {agencies
                  ? agencies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/agency-license" replace color="info">
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

export default AgencyLicenseUpdate;
