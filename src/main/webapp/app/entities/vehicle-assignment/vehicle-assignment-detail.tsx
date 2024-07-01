import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle-assignment.reducer';

export const VehicleAssignmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleAssignmentEntity = useAppSelector(state => state.vehicleAssignment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleAssignmentDetailsHeading">
          <Translate contentKey="cpamainApp.vehicleAssignment.detail.title">VehicleAssignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleAssignmentEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="cpamainApp.vehicleAssignment.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {vehicleAssignmentEntity.startDate ? (
              <TextFormat value={vehicleAssignmentEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="cpamainApp.vehicleAssignment.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {vehicleAssignmentEntity.endDate ? (
              <TextFormat value={vehicleAssignmentEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isPrimary">
              <Translate contentKey="cpamainApp.vehicleAssignment.isPrimary">Is Primary</Translate>
            </span>
          </dt>
          <dd>{vehicleAssignmentEntity.isPrimary ? 'true' : 'false'}</dd>
          <dt>
            <span id="isRental">
              <Translate contentKey="cpamainApp.vehicleAssignment.isRental">Is Rental</Translate>
            </span>
          </dt>
          <dd>{vehicleAssignmentEntity.isRental ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cpamainApp.vehicleAssignment.agency">Agency</Translate>
          </dt>
          <dd>{vehicleAssignmentEntity.agency ? vehicleAssignmentEntity.agency.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.vehicleAssignment.vehicle">Vehicle</Translate>
          </dt>
          <dd>{vehicleAssignmentEntity.vehicle ? vehicleAssignmentEntity.vehicle.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/vehicle-assignment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle-assignment/${vehicleAssignmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleAssignmentDetail;
