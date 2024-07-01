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
import { IPassType } from 'app/shared/model/pass-type.model';
import { IssueChannelType } from 'app/shared/model/enumerations/issue-channel-type.model';
import { TaxCodeType } from 'app/shared/model/enumerations/tax-code-type.model';
import { PassMediaType } from 'app/shared/model/enumerations/pass-media-type.model';
import { PassUserType } from 'app/shared/model/enumerations/pass-user-type.model';
import { getEntity, updateEntity, createEntity, reset } from './pass-type.reducer';

export const PassTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agencies = useAppSelector(state => state.agency.entities);
  const passTypeEntity = useAppSelector(state => state.passType.entity);
  const loading = useAppSelector(state => state.passType.loading);
  const updating = useAppSelector(state => state.passType.updating);
  const updateSuccess = useAppSelector(state => state.passType.updateSuccess);
  const issueChannelTypeValues = Object.keys(IssueChannelType);
  const taxCodeTypeValues = Object.keys(TaxCodeType);
  const passMediaTypeValues = Object.keys(PassMediaType);
  const passUserTypeValues = Object.keys(PassUserType);

  const handleClose = () => {
    navigate('/pass-type');
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
    if (values.issueFee !== undefined && typeof values.issueFee !== 'number') {
      values.issueFee = Number(values.issueFee);
    }
    if (values.renewFee !== undefined && typeof values.renewFee !== 'number') {
      values.renewFee = Number(values.renewFee);
    }
    if (values.cancelFee !== undefined && typeof values.cancelFee !== 'number') {
      values.cancelFee = Number(values.cancelFee);
    }
    if (values.minimumDuration !== undefined && typeof values.minimumDuration !== 'number') {
      values.minimumDuration = Number(values.minimumDuration);
    }
    if (values.maximumDuration !== undefined && typeof values.maximumDuration !== 'number') {
      values.maximumDuration = Number(values.maximumDuration);
    }

    const entity = {
      ...passTypeEntity,
      ...values,
      agencies: mapIdList(values.agencies),
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
          issueChannelType: 'ONPREM',
          taxCode: 'VAT',
          passMediaType: 'BARCODE',
          passUserType: 'HUMAN',
          ...passTypeEntity,
          agencies: passTypeEntity?.agencies?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.passType.home.createOrEditLabel" data-cy="PassTypeCreateUpdateHeading">
            <Translate contentKey="cpamainApp.passType.home.createOrEditLabel">Create or edit a PassType</Translate>
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
                  id="pass-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('cpamainApp.passType.name')} id="pass-type-name" name="name" data-cy="name" type="text" />
              <UncontrolledTooltip target="nameLabel">
                <Translate contentKey="cpamainApp.passType.help.name" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('cpamainApp.passType.shortName')}
                id="pass-type-shortName"
                name="shortName"
                data-cy="shortName"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.isActive')}
                id="pass-type-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.printedName')}
                id="pass-type-printedName"
                name="printedName"
                data-cy="printedName"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.issueFee')}
                id="pass-type-issueFee"
                name="issueFee"
                data-cy="issueFee"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.renewFee')}
                id="pass-type-renewFee"
                name="renewFee"
                data-cy="renewFee"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.cancelFee')}
                id="pass-type-cancelFee"
                name="cancelFee"
                data-cy="cancelFee"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.minimumDuration')}
                id="pass-type-minimumDuration"
                name="minimumDuration"
                data-cy="minimumDuration"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.maximumDuration')}
                id="pass-type-maximumDuration"
                name="maximumDuration"
                data-cy="maximumDuration"
                type="text"
              />
              <ValidatedField
                label={translate('cpamainApp.passType.issueChannelType')}
                id="pass-type-issueChannelType"
                name="issueChannelType"
                data-cy="issueChannelType"
                type="select"
              >
                {issueChannelTypeValues.map(issueChannelType => (
                  <option value={issueChannelType} key={issueChannelType}>
                    {translate('cpamainApp.IssueChannelType.' + issueChannelType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.passType.taxCode')}
                id="pass-type-taxCode"
                name="taxCode"
                data-cy="taxCode"
                type="select"
              >
                {taxCodeTypeValues.map(taxCodeType => (
                  <option value={taxCodeType} key={taxCodeType}>
                    {translate('cpamainApp.TaxCodeType.' + taxCodeType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.passType.passMediaType')}
                id="pass-type-passMediaType"
                name="passMediaType"
                data-cy="passMediaType"
                type="select"
              >
                {passMediaTypeValues.map(passMediaType => (
                  <option value={passMediaType} key={passMediaType}>
                    {translate('cpamainApp.PassMediaType.' + passMediaType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.passType.passUserType')}
                id="pass-type-passUserType"
                name="passUserType"
                data-cy="passUserType"
                type="select"
              >
                {passUserTypeValues.map(passUserType => (
                  <option value={passUserType} key={passUserType}>
                    {translate('cpamainApp.PassUserType.' + passUserType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('cpamainApp.passType.agency')}
                id="pass-type-agency"
                data-cy="agency"
                type="select"
                multiple
                name="agencies"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pass-type" replace color="info">
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

export default PassTypeUpdate;
