import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agency-license.reducer';

export const AgencyLicenseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agencyLicenseEntity = useAppSelector(state => state.agencyLicense.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agencyLicenseDetailsHeading">
          <Translate contentKey="cpamainApp.agencyLicense.detail.title">AgencyLicense</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agencyLicenseEntity.id}</dd>
          <dt>
            <span id="filePath">
              <Translate contentKey="cpamainApp.agencyLicense.filePath">File Path</Translate>
            </span>
            <UncontrolledTooltip target="filePath">
              <Translate contentKey="cpamainApp.agencyLicense.help.filePath" />
            </UncontrolledTooltip>
          </dt>
          <dd>{agencyLicenseEntity.filePath}</dd>
          <dt>
            <span id="serialNo">
              <Translate contentKey="cpamainApp.agencyLicense.serialNo">Serial No</Translate>
            </span>
          </dt>
          <dd>{agencyLicenseEntity.serialNo}</dd>
          <dt>
            <span id="issueDate">
              <Translate contentKey="cpamainApp.agencyLicense.issueDate">Issue Date</Translate>
            </span>
          </dt>
          <dd>
            {agencyLicenseEntity.issueDate ? (
              <TextFormat value={agencyLicenseEntity.issueDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="expiryDate">
              <Translate contentKey="cpamainApp.agencyLicense.expiryDate">Expiry Date</Translate>
            </span>
          </dt>
          <dd>
            {agencyLicenseEntity.expiryDate ? (
              <TextFormat value={agencyLicenseEntity.expiryDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="cpamainApp.agencyLicense.verifiedBy">Verified By</Translate>
          </dt>
          <dd>{agencyLicenseEntity.verifiedBy ? agencyLicenseEntity.verifiedBy.login : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.agencyLicense.agencyLicenseType">Agency License Type</Translate>
          </dt>
          <dd>{agencyLicenseEntity.agencyLicenseType ? agencyLicenseEntity.agencyLicenseType.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.agencyLicense.belongsTo">Belongs To</Translate>
          </dt>
          <dd>{agencyLicenseEntity.belongsTo ? agencyLicenseEntity.belongsTo.id : ''}</dd>
          <dt>
            <Translate contentKey="cpamainApp.agencyLicense.issuedBy">Issued By</Translate>
          </dt>
          <dd>{agencyLicenseEntity.issuedBy ? agencyLicenseEntity.issuedBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/agency-license" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agency-license/${agencyLicenseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgencyLicenseDetail;
