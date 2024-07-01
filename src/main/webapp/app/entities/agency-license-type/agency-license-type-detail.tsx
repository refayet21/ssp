import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agency-license-type.reducer';

export const AgencyLicenseTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agencyLicenseTypeEntity = useAppSelector(state => state.agencyLicenseType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agencyLicenseTypeDetailsHeading">
          <Translate contentKey="cpamainApp.agencyLicenseType.detail.title">AgencyLicenseType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agencyLicenseTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.agencyLicenseType.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.agencyLicenseType.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{agencyLicenseTypeEntity.name}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.agencyLicenseType.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{agencyLicenseTypeEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/agency-license-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agency-license-type/${agencyLicenseTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgencyLicenseTypeDetail;
