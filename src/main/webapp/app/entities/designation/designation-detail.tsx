import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './designation.reducer';

export const DesignationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const designationEntity = useAppSelector(state => state.designation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="designationDetailsHeading">
          <Translate contentKey="cpamainApp.designation.detail.title">Designation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{designationEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.designation.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.designation.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{designationEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.designation.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{designationEntity.shortName}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.designation.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{designationEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/designation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/designation/${designationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DesignationDetail;
