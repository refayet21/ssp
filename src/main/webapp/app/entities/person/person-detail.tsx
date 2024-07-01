import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './person.reducer';

export const PersonDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const personEntity = useAppSelector(state => state.person.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personDetailsHeading">
          <Translate contentKey="cpamainApp.person.detail.title">Person</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.person.name">Name</Translate>
            </span>
          </dt>
          <dd>{personEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.person.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{personEntity.shortName}</dd>
          <dt>
            <span id="dob">
              <Translate contentKey="cpamainApp.person.dob">Dob</Translate>
            </span>
          </dt>
          <dd>{personEntity.dob ? <TextFormat value={personEntity.dob} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="cpamainApp.person.email">Email</Translate>
            </span>
          </dt>
          <dd>{personEntity.email}</dd>
          <dt>
            <span id="isBlackListed">
              <Translate contentKey="cpamainApp.person.isBlackListed">Is Black Listed</Translate>
            </span>
          </dt>
          <dd>{personEntity.isBlackListed ? 'true' : 'false'}</dd>
          <dt>
            <span id="fatherName">
              <Translate contentKey="cpamainApp.person.fatherName">Father Name</Translate>
            </span>
          </dt>
          <dd>{personEntity.fatherName}</dd>
          <dt>
            <span id="motherName">
              <Translate contentKey="cpamainApp.person.motherName">Mother Name</Translate>
            </span>
          </dt>
          <dd>{personEntity.motherName}</dd>
          <dt>
            <span id="logStatus">
              <Translate contentKey="cpamainApp.person.logStatus">Log Status</Translate>
            </span>
          </dt>
          <dd>{personEntity.logStatus}</dd>
          <dt>
            <Translate contentKey="cpamainApp.person.internalUser">Internal User</Translate>
          </dt>
          <dd>{personEntity.internalUser ? personEntity.internalUser.login : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.person.nationality">Nationality</Translate>
          </dt>
          <dd>{personEntity.nationality ? personEntity.nationality.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/person" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/person/${personEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonDetail;
