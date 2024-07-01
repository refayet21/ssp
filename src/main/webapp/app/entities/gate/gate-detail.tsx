import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './gate.reducer';

export const GateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gateEntity = useAppSelector(state => state.gate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gateDetailsHeading">
          <Translate contentKey="cpamainApp.gate.detail.title">Gate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{gateEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.gate.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.gate.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{gateEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.gate.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{gateEntity.shortName}</dd>
          <dt>
            <span id="lat">
              <Translate contentKey="cpamainApp.gate.lat">Lat</Translate>
            </span>
          </dt>
          <dd>{gateEntity.lat}</dd>
          <dt>
            <span id="lon">
              <Translate contentKey="cpamainApp.gate.lon">Lon</Translate>
            </span>
          </dt>
          <dd>{gateEntity.lon}</dd>
          <dt>
            <span id="gateType">
              <Translate contentKey="cpamainApp.gate.gateType">Gate Type</Translate>
            </span>
          </dt>
          <dd>{gateEntity.gateType}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.gate.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{gateEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cpamainApp.gate.zone">Zone</Translate>
          </dt>
          <dd>{gateEntity.zone ? gateEntity.zone.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/gate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/gate/${gateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GateDetail;
