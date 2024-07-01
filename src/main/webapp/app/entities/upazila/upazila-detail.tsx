import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './upazila.reducer';

export const UpazilaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const upazilaEntity = useAppSelector(state => state.upazila.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="upazilaDetailsHeading">
          <Translate contentKey="cpamainApp.upazila.detail.title">Upazila</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{upazilaEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.upazila.name">Name</Translate>
            </span>
          </dt>
          <dd>{upazilaEntity.name}</dd>
          <dt>
            <Translate contentKey="cpamainApp.upazila.district">District</Translate>
          </dt>
          <dd>{upazilaEntity.district ? upazilaEntity.district.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/upazila" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/upazila/${upazilaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UpazilaDetail;
