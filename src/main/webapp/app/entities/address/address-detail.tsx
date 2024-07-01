import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './address.reducer';

export const AddressDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const addressEntity = useAppSelector(state => state.address.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="addressDetailsHeading">
          <Translate contentKey="cpamainApp.address.detail.title">Address</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{addressEntity.id}</dd>
          <dt>
            <span id="addressLineOne">
              <Translate contentKey="cpamainApp.address.addressLineOne">Address Line One</Translate>
            </span>
          </dt>
          <dd>{addressEntity.addressLineOne}</dd>
          <dt>
            <span id="addressLineTwo">
              <Translate contentKey="cpamainApp.address.addressLineTwo">Address Line Two</Translate>
            </span>
          </dt>
          <dd>{addressEntity.addressLineTwo}</dd>
          <dt>
            <span id="addressLineThree">
              <Translate contentKey="cpamainApp.address.addressLineThree">Address Line Three</Translate>
            </span>
          </dt>
          <dd>{addressEntity.addressLineThree}</dd>
          <dt>
            <span id="addressType">
              <Translate contentKey="cpamainApp.address.addressType">Address Type</Translate>
            </span>
          </dt>
          <dd>{addressEntity.addressType}</dd>
          <dt>
            <Translate contentKey="cpamainApp.address.cityCorpPoura">City Corp Poura</Translate>
          </dt>
          <dd>{addressEntity.cityCorpPoura ? addressEntity.cityCorpPoura.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.address.union">Union</Translate>
          </dt>
          <dd>{addressEntity.union ? addressEntity.union.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.address.postOffice">Post Office</Translate>
          </dt>
          <dd>{addressEntity.postOffice ? addressEntity.postOffice.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.address.country">Country</Translate>
          </dt>
          <dd>{addressEntity.country ? addressEntity.country.name : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.address.person">Person</Translate>
          </dt>
          <dd>{addressEntity.person ? addressEntity.person.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.address.agency">Agency</Translate>
          </dt>
          <dd>{addressEntity.agency ? addressEntity.agency.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/address/${addressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AddressDetail;
