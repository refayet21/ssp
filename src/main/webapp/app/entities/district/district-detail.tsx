import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './district.reducer';

export const DistrictDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const districtEntity = useAppSelector(state => state.district.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="districtDetailsHeading">
          <Translate contentKey="cpamainApp.district.detail.title">District</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{districtEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.district.name">Name</Translate>
            </span>
          </dt>
          <dd>{districtEntity.name}</dd>
          <dt>
            <Translate contentKey="cpamainApp.district.division">Division</Translate>
          </dt>
          <dd>{districtEntity.division ? districtEntity.division.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/district" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/district/${districtEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DistrictDetail;
