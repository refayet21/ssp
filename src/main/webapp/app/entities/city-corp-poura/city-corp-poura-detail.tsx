import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './city-corp-poura.reducer';

export const CityCorpPouraDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cityCorpPouraEntity = useAppSelector(state => state.cityCorpPoura.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cityCorpPouraDetailsHeading">
          <Translate contentKey="cpamainApp.cityCorpPoura.detail.title">CityCorpPoura</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cityCorpPouraEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.cityCorpPoura.name">Name</Translate>
            </span>
          </dt>
          <dd>{cityCorpPouraEntity.name}</dd>
          <dt>
            <Translate contentKey="cpamainApp.cityCorpPoura.district">District</Translate>
          </dt>
          <dd>{cityCorpPouraEntity.district ? cityCorpPouraEntity.district.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.cityCorpPoura.upazila">Upazila</Translate>
          </dt>
          <dd>{cityCorpPouraEntity.upazila ? cityCorpPouraEntity.upazila.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.cityCorpPoura.rmo">Rmo</Translate>
          </dt>
          <dd>{cityCorpPouraEntity.rmo ? cityCorpPouraEntity.rmo.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/city-corp-poura" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/city-corp-poura/${cityCorpPouraEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CityCorpPouraDetail;
