import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lane.reducer';

export const LaneDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const laneEntity = useAppSelector(state => state.lane.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="laneDetailsHeading">
          <Translate contentKey="cpamainApp.lane.detail.title">Lane</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{laneEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.lane.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.lane.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{laneEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.lane.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{laneEntity.shortName}</dd>
          <dt>
            <span id="direction">
              <Translate contentKey="cpamainApp.lane.direction">Direction</Translate>
            </span>
          </dt>
          <dd>{laneEntity.direction}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.lane.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{laneEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cpamainApp.lane.gate">Gate</Translate>
          </dt>
          <dd>{laneEntity.gate ? laneEntity.gate.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.lane.accessProfile">Access Profile</Translate>
          </dt>
          <dd>
            {laneEntity.accessProfiles
              ? laneEntity.accessProfiles.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {laneEntity.accessProfiles && i === laneEntity.accessProfiles.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/lane" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lane/${laneEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LaneDetail;
