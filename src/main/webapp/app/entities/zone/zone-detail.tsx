import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './zone.reducer';

export const ZoneDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const zoneEntity = useAppSelector(state => state.zone.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="zoneDetailsHeading">
          <Translate contentKey="cpamainApp.zone.detail.title">Zone</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.zone.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.zone.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{zoneEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.zone.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.shortName}</dd>
          <dt>
            <span id="location">
              <Translate contentKey="cpamainApp.zone.location">Location</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.location}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.zone.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{zoneEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cpamainApp.zone.authority">Authority</Translate>
          </dt>
          <dd>{zoneEntity.authority ? zoneEntity.authority.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/zone" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/zone/${zoneEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ZoneDetail;
