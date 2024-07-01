import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDistrict } from 'app/shared/model/district.model';
import { getEntities as getDistricts } from 'app/entities/district/district.reducer';
import { IUpazila } from 'app/shared/model/upazila.model';
import { getEntities as getUpazilas } from 'app/entities/upazila/upazila.reducer';
import { IRMO } from 'app/shared/model/rmo.model';
import { getEntities as getRMOS } from 'app/entities/rmo/rmo.reducer';
import { ICityCorpPoura } from 'app/shared/model/city-corp-poura.model';
import { getEntity, updateEntity, createEntity, reset } from './city-corp-poura.reducer';

export const CityCorpPouraUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const districts = useAppSelector(state => state.district.entities);
  const upazilas = useAppSelector(state => state.upazila.entities);
  const rMOS = useAppSelector(state => state.rMO.entities);
  const cityCorpPouraEntity = useAppSelector(state => state.cityCorpPoura.entity);
  const loading = useAppSelector(state => state.cityCorpPoura.loading);
  const updating = useAppSelector(state => state.cityCorpPoura.updating);
  const updateSuccess = useAppSelector(state => state.cityCorpPoura.updateSuccess);

  const handleClose = () => {
    navigate('/city-corp-poura');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getDistricts({}));
    dispatch(getUpazilas({}));
    dispatch(getRMOS({}));
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
      ...cityCorpPouraEntity,
      ...values,
      district: districts.find(it => it.id.toString() === values.district?.toString()),
      upazila: upazilas.find(it => it.id.toString() === values.upazila?.toString()),
      rmo: rMOS.find(it => it.id.toString() === values.rmo?.toString()),
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
          ...cityCorpPouraEntity,
          district: cityCorpPouraEntity?.district?.id,
          upazila: cityCorpPouraEntity?.upazila?.id,
          rmo: cityCorpPouraEntity?.rmo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.cityCorpPoura.home.createOrEditLabel" data-cy="CityCorpPouraCreateUpdateHeading">
            <Translate contentKey="cpamainApp.cityCorpPoura.home.createOrEditLabel">Create or edit a CityCorpPoura</Translate>
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
                  id="city-corp-poura-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.cityCorpPoura.name')}
                id="city-corp-poura-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="city-corp-poura-district"
                name="district"
                data-cy="district"
                label={translate('cpamainApp.cityCorpPoura.district')}
                type="select"
              >
                <option value="" key="0" />
                {districts
                  ? districts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="city-corp-poura-upazila"
                name="upazila"
                data-cy="upazila"
                label={translate('cpamainApp.cityCorpPoura.upazila')}
                type="select"
              >
                <option value="" key="0" />
                {upazilas
                  ? upazilas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="city-corp-poura-rmo"
                name="rmo"
                data-cy="rmo"
                label={translate('cpamainApp.cityCorpPoura.rmo')}
                type="select"
                required
              >
                <option value="" key="0" />
                {rMOS
                  ? rMOS.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/city-corp-poura" replace color="info">
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

export default CityCorpPouraUpdate;
