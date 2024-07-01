import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agency.reducer';

export const AgencyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agencyEntity = useAppSelector(state => state.agency.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agencyDetailsHeading">
          <Translate contentKey="cpamainApp.agency.detail.title">Agency</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agencyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.agency.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.agency.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{agencyEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.agency.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{agencyEntity.shortName}</dd>
          <dt>
            <span id="isInternal">
              <Translate contentKey="cpamainApp.agency.isInternal">Is Internal</Translate>
            </span>
          </dt>
          <dd>{agencyEntity.isInternal ? 'true' : 'false'}</dd>
          <dt>
            <span id="isDummy">
              <Translate contentKey="cpamainApp.agency.isDummy">Is Dummy</Translate>
            </span>
          </dt>
          <dd>{agencyEntity.isDummy ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="cpamainApp.agency.agencyType">Agency Type</Translate>
          </dt>
          <dd>{agencyEntity.agencyType ? agencyEntity.agencyType.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.agency.passType">Pass Type</Translate>
          </dt>
          <dd>
            {agencyEntity.passTypes
              ? agencyEntity.passTypes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {agencyEntity.passTypes && i === agencyEntity.passTypes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/agency" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agency/${agencyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgencyDetail;
