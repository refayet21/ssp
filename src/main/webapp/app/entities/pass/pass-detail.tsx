import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pass.reducer';

export const PassDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const passEntity = useAppSelector(state => state.pass.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="passDetailsHeading">
          <Translate contentKey="cpamainApp.pass.detail.title">Pass</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{passEntity.id}</dd>
          <dt>
            <span id="collectedFee">
              <Translate contentKey="cpamainApp.pass.collectedFee">Collected Fee</Translate>
            </span>
            <UncontrolledTooltip target="collectedFee">
              <Translate contentKey="cpamainApp.pass.help.collectedFee" />
            </UncontrolledTooltip>
          </dt>
          <dd>{passEntity.collectedFee}</dd>
          <dt>
            <span id="fromDate">
              <Translate contentKey="cpamainApp.pass.fromDate">From Date</Translate>
            </span>
          </dt>
          <dd>{passEntity.fromDate ? <TextFormat value={passEntity.fromDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="cpamainApp.pass.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>{passEntity.endDate ? <TextFormat value={passEntity.endDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="cpamainApp.pass.status">Status</Translate>
            </span>
          </dt>
          <dd>{passEntity.status}</dd>
          <dt>
            <span id="passNumber">
              <Translate contentKey="cpamainApp.pass.passNumber">Pass Number</Translate>
            </span>
          </dt>
          <dd>{passEntity.passNumber}</dd>
          <dt>
            <span id="mediaSerial">
              <Translate contentKey="cpamainApp.pass.mediaSerial">Media Serial</Translate>
            </span>
          </dt>
          <dd>{passEntity.mediaSerial}</dd>
          <dt>
            <Translate contentKey="cpamainApp.pass.passType">Pass Type</Translate>
          </dt>
          <dd>{passEntity.passType ? passEntity.passType.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.pass.requestedBy">Requested By</Translate>
          </dt>
          <dd>{passEntity.requestedBy ? passEntity.requestedBy.login : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.pass.assignment">Assignment</Translate>
          </dt>
          <dd>{passEntity.assignment ? passEntity.assignment.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.pass.vehicleAssignment">Vehicle Assignment</Translate>
          </dt>
          <dd>{passEntity.vehicleAssignment ? passEntity.vehicleAssignment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pass" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pass/${passEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PassDetail;
