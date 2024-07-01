import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agency-type.reducer';

export const AgencyTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agencyTypeEntity = useAppSelector(state => state.agencyType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agencyTypeDetailsHeading">
          <Translate contentKey="cpamainApp.agencyType.detail.title">AgencyType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agencyTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.agencyType.name">Name</Translate>
            </span>
          </dt>
          <dd>{agencyTypeEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.agencyType.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{agencyTypeEntity.shortName}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.agencyType.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{agencyTypeEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/agency-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agency-type/${agencyTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgencyTypeDetail;
