import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './document.reducer';

export const DocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const documentEntity = useAppSelector(state => state.document.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="documentDetailsHeading">
          <Translate contentKey="cpamainApp.document.detail.title">Document</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{documentEntity.id}</dd>
          <dt>
            <span id="isPrimary">
              <Translate contentKey="cpamainApp.document.isPrimary">Is Primary</Translate>
            </span>
          </dt>
          <dd>{documentEntity.isPrimary ? 'true' : 'false'}</dd>
          <dt>
            <span id="serial">
              <Translate contentKey="cpamainApp.document.serial">Serial</Translate>
            </span>
          </dt>
          <dd>{documentEntity.serial}</dd>
          <dt>
            <span id="issueDate">
              <Translate contentKey="cpamainApp.document.issueDate">Issue Date</Translate>
            </span>
          </dt>
          <dd>
            {documentEntity.issueDate ? <TextFormat value={documentEntity.issueDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="expiryDate">
              <Translate contentKey="cpamainApp.document.expiryDate">Expiry Date</Translate>
            </span>
          </dt>
          <dd>
            {documentEntity.expiryDate ? <TextFormat value={documentEntity.expiryDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="filePath">
              <Translate contentKey="cpamainApp.document.filePath">File Path</Translate>
            </span>
          </dt>
          <dd>{documentEntity.filePath}</dd>
          <dt>
            <Translate contentKey="cpamainApp.document.verifiedBy">Verified By</Translate>
          </dt>
          <dd>{documentEntity.verifiedBy ? documentEntity.verifiedBy.login : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.document.documentType">Document Type</Translate>
          </dt>
          <dd>{documentEntity.documentType ? documentEntity.documentType.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.document.person">Person</Translate>
          </dt>
          <dd>{documentEntity.person ? documentEntity.person.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.document.vehicle">Vehicle</Translate>
          </dt>
          <dd>{documentEntity.vehicle ? documentEntity.vehicle.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.document.agency">Agency</Translate>
          </dt>
          <dd>{documentEntity.agency ? documentEntity.agency.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/document" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/document/${documentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DocumentDetail;
