import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ward.reducer';

export const WardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const wardEntity = useAppSelector(state => state.ward.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="wardDetailsHeading">
          <Translate contentKey="cpamainApp.ward.detail.title">Ward</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{wardEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.ward.name">Name</Translate>
            </span>
          </dt>
          <dd>{wardEntity.name}</dd>
          <dt>
            <Translate contentKey="cpamainApp.ward.cityCorpPoura">City Corp Poura</Translate>
          </dt>
          <dd>{wardEntity.cityCorpPoura ? wardEntity.cityCorpPoura.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.ward.union">Union</Translate>
          </dt>
          <dd>{wardEntity.union ? wardEntity.union.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/ward" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ward/${wardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WardDetail;
