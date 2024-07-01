import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assignment.reducer';

export const AssignmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const assignmentEntity = useAppSelector(state => state.assignment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assignmentDetailsHeading">
          <Translate contentKey="cpamainApp.assignment.detail.title">Assignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="cpamainApp.assignment.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {assignmentEntity.startDate ? (
              <TextFormat value={assignmentEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="cpamainApp.assignment.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {assignmentEntity.endDate ? <TextFormat value={assignmentEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isPrimary">
              <Translate contentKey="cpamainApp.assignment.isPrimary">Is Primary</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.isPrimary ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cpamainApp.assignment.person">Person</Translate>
          </dt>
          <dd>{assignmentEntity.person ? assignmentEntity.person.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.assignment.designation">Designation</Translate>
          </dt>
          <dd>{assignmentEntity.designation ? assignmentEntity.designation.shortName : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.assignment.agency">Agency</Translate>
          </dt>
          <dd>{assignmentEntity.agency ? assignmentEntity.agency.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/assignment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/assignment/${assignmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssignmentDetail;
