import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './access-profile.reducer';

export const AccessProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const accessProfileEntity = useAppSelector(state => state.accessProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="accessProfileDetailsHeading">
          <Translate contentKey="cpamainApp.accessProfile.detail.title">AccessProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{accessProfileEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.accessProfile.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.accessProfile.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{accessProfileEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="cpamainApp.accessProfile.description">Description</Translate>
            </span>
          </dt>
          <dd>{accessProfileEntity.description}</dd>
          <dt>
            <span id="startTimeOfDay">
              <Translate contentKey="cpamainApp.accessProfile.startTimeOfDay">Start Time Of Day</Translate>
            </span>
          </dt>
          <dd>{accessProfileEntity.startTimeOfDay}</dd>
          <dt>
            <span id="endTimeOfDay">
              <Translate contentKey="cpamainApp.accessProfile.endTimeOfDay">End Time Of Day</Translate>
            </span>
          </dt>
          <dd>{accessProfileEntity.endTimeOfDay}</dd>
          <dt>
            <span id="dayOfWeek">
              <Translate contentKey="cpamainApp.accessProfile.dayOfWeek">Day Of Week</Translate>
            </span>
          </dt>
          <dd>{accessProfileEntity.dayOfWeek}</dd>
          <dt>
            <span id="action">
              <Translate contentKey="cpamainApp.accessProfile.action">Action</Translate>
            </span>
          </dt>
          <dd>{accessProfileEntity.action}</dd>
          <dt>
            <Translate contentKey="cpamainApp.accessProfile.lane">Lane</Translate>
          </dt>
          <dd>
            {accessProfileEntity.lanes
              ? accessProfileEntity.lanes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {accessProfileEntity.lanes && i === accessProfileEntity.lanes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/access-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/access-profile/${accessProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AccessProfileDetail;
