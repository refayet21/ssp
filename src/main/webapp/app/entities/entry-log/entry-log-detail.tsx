import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './entry-log.reducer';

export const EntryLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const entryLogEntity = useAppSelector(state => state.entryLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="entryLogDetailsHeading">
          <Translate contentKey="cpamainApp.entryLog.detail.title">EntryLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{entryLogEntity.id}</dd>
          <dt>
            <span id="eventTime">
              <Translate contentKey="cpamainApp.entryLog.eventTime">Event Time</Translate>
            </span>
            <UncontrolledTooltip target="eventTime">
              <Translate contentKey="cpamainApp.entryLog.help.eventTime" />
            </UncontrolledTooltip>
          </dt>
          <dd>{entryLogEntity.eventTime ? <TextFormat value={entryLogEntity.eventTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="direction">
              <Translate contentKey="cpamainApp.entryLog.direction">Direction</Translate>
            </span>
          </dt>
          <dd>{entryLogEntity.direction}</dd>
          <dt>
            <span id="passStatus">
              <Translate contentKey="cpamainApp.entryLog.passStatus">Pass Status</Translate>
            </span>
          </dt>
          <dd>{entryLogEntity.passStatus}</dd>
          <dt>
            <span id="actionType">
              <Translate contentKey="cpamainApp.entryLog.actionType">Action Type</Translate>
            </span>
          </dt>
          <dd>{entryLogEntity.actionType}</dd>
          <dt>
            <Translate contentKey="cpamainApp.entryLog.pass">Pass</Translate>
          </dt>
          <dd>{entryLogEntity.pass ? entryLogEntity.pass.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.entryLog.lane">Lane</Translate>
          </dt>
          <dd>{entryLogEntity.lane ? entryLogEntity.lane.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/entry-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entry-log/${entryLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EntryLogDetail;
