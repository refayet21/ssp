import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICityCorpPoura } from 'app/shared/model/city-corp-poura.model';
import { getEntities as getCityCorpPouras } from 'app/entities/city-corp-poura/city-corp-poura.reducer';
import { IUnion } from 'app/shared/model/union.model';
import { getEntities as getUnions } from 'app/entities/union/union.reducer';
import { IWard } from 'app/shared/model/ward.model';
import { getEntity, updateEntity, createEntity, reset } from './ward.reducer';

export const WardUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cityCorpPouras = useAppSelector(state => state.cityCorpPoura.entities);
  const unions = useAppSelector(state => state.union.entities);
  const wardEntity = useAppSelector(state => state.ward.entity);
  const loading = useAppSelector(state => state.ward.loading);
  const updating = useAppSelector(state => state.ward.updating);
  const updateSuccess = useAppSelector(state => state.ward.updateSuccess);

  const handleClose = () => {
    navigate('/ward');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCityCorpPouras({}));
    dispatch(getUnions({}));
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
      ...wardEntity,
      ...values,
      cityCorpPoura: cityCorpPouras.find(it => it.id.toString() === values.cityCorpPoura?.toString()),
      union: unions.find(it => it.id.toString() === values.union?.toString()),
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
          ...wardEntity,
          cityCorpPoura: wardEntity?.cityCorpPoura?.id,
          union: wardEntity?.union?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="cpamainApp.ward.home.createOrEditLabel" data-cy="WardCreateUpdateHeading">
            <Translate contentKey="cpamainApp.ward.home.createOrEditLabel">Create or edit a Ward</Translate>
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
                  id="ward-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('cpamainApp.ward.name')}
                id="ward-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="ward-cityCorpPoura"
                name="cityCorpPoura"
                data-cy="cityCorpPoura"
                label={translate('cpamainApp.ward.cityCorpPoura')}
                type="select"
              >
                <option value="" key="0" />
                {cityCorpPouras
                  ? cityCorpPouras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="ward-union" name="union" data-cy="union" label={translate('cpamainApp.ward.union')} type="select">
                <option value="" key="0" />
                {unions
                  ? unions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ward" replace color="info">
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

export default WardUpdate;
