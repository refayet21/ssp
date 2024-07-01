import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './union.reducer';

export const UnionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const unionEntity = useAppSelector(state => state.union.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="unionDetailsHeading">
          <Translate contentKey="cpamainApp.union.detail.title">Union</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{unionEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.union.name">Name</Translate>
            </span>
          </dt>
          <dd>{unionEntity.name}</dd>
          <dt>
            <Translate contentKey="cpamainApp.union.upazila">Upazila</Translate>
          </dt>
          <dd>{unionEntity.upazila ? unionEntity.upazila.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/union" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/union/${unionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UnionDetail;
