import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document-type.reducer';

export const DocumentTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentTypeEntity = useAppSelector(state => state.documentType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentTypeDetailsHeading">
          <Translate contentKey="cpamainApp.documentType.detail.title">DocumentType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{documentTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.documentType.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.documentType.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{documentTypeEntity.name}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.documentType.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{documentTypeEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="cpamainApp.documentType.description">Description</Translate>
            </span>
          </dt>
          <dd>{documentTypeEntity.description}</dd>
          <dt>
            <span id="documentMasterType">
              <Translate contentKey="cpamainApp.documentType.documentMasterType">Document Master Type</Translate>
            </span>
          </dt>
          <dd>{documentTypeEntity.documentMasterType}</dd>
          <dt>
            <span id="requiresVerification">
              <Translate contentKey="cpamainApp.documentType.requiresVerification">Requires Verification</Translate>
            </span>
          </dt>
          <dd>{documentTypeEntity.requiresVerification ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/document-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document-type/${documentTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentTypeDetail;
