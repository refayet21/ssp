import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vehicle.reducer';

export const VehicleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vehicleEntity = useAppSelector(state => state.vehicle.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vehicleDetailsHeading">
          <Translate contentKey="cpamainApp.vehicle.detail.title">Vehicle</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.vehicle.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.vehicle.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{vehicleEntity.name}</dd>
          <dt>
            <span id="regNo">
              <Translate contentKey="cpamainApp.vehicle.regNo">Reg No</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.regNo}</dd>
          <dt>
            <span id="zone">
              <Translate contentKey="cpamainApp.vehicle.zone">Zone</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.zone}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="cpamainApp.vehicle.category">Category</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.category}</dd>
          <dt>
            <span id="serialNo">
              <Translate contentKey="cpamainApp.vehicle.serialNo">Serial No</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.serialNo}</dd>
          <dt>
            <span id="vehicleNo">
              <Translate contentKey="cpamainApp.vehicle.vehicleNo">Vehicle No</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.vehicleNo}</dd>
          <dt>
            <span id="chasisNo">
              <Translate contentKey="cpamainApp.vehicle.chasisNo">Chasis No</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.chasisNo}</dd>
          <dt>
            <span id="isPersonal">
              <Translate contentKey="cpamainApp.vehicle.isPersonal">Is Personal</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.isPersonal ? 'true' : 'false'}</dd>
          <dt>
            <span id="isBlackListed">
              <Translate contentKey="cpamainApp.vehicle.isBlackListed">Is Black Listed</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.isBlackListed ? 'true' : 'false'}</dd>
          <dt>
            <span id="logStatus">
              <Translate contentKey="cpamainApp.vehicle.logStatus">Log Status</Translate>
            </span>
          </dt>
          <dd>{vehicleEntity.logStatus}</dd>
          <dt>
            <Translate contentKey="cpamainApp.vehicle.vehicleType">Vehicle Type</Translate>
          </dt>
          <dd>{vehicleEntity.vehicleType ? vehicleEntity.vehicleType.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/vehicle" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vehicle/${vehicleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VehicleDetail;
