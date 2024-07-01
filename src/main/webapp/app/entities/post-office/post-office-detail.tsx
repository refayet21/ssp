import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-office.reducer';

export const PostOfficeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postOfficeEntity = useAppSelector(state => state.postOffice.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postOfficeDetailsHeading">
          <Translate contentKey="cpamainApp.postOffice.detail.title">PostOffice</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postOfficeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.postOffice.name">Name</Translate>
            </span>
          </dt>
          <dd>{postOfficeEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="cpamainApp.postOffice.code">Code</Translate>
            </span>
          </dt>
          <dd>{postOfficeEntity.code}</dd>
          <dt>
            <Translate contentKey="cpamainApp.postOffice.district">District</Translate>
          </dt>
          <dd>{postOfficeEntity.district ? postOfficeEntity.district.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/post-office" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-office/${postOfficeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostOfficeDetail;
