import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pass-type.reducer';

export const PassTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const passTypeEntity = useAppSelector(state => state.passType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="passTypeDetailsHeading">
          <Translate contentKey="cpamainApp.passType.detail.title">PassType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="cpamainApp.passType.name">Name</Translate>
            </span>
            <UncontrolledTooltip target="name">
              <Translate contentKey="cpamainApp.passType.help.name" />
            </UncontrolledTooltip>
          </dt>
          <dd>{passTypeEntity.name}</dd>
          <dt>
            <span id="shortName">
              <Translate contentKey="cpamainApp.passType.shortName">Short Name</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.shortName}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="cpamainApp.passType.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="printedName">
              <Translate contentKey="cpamainApp.passType.printedName">Printed Name</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.printedName}</dd>
          <dt>
            <span id="issueFee">
              <Translate contentKey="cpamainApp.passType.issueFee">Issue Fee</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.issueFee}</dd>
          <dt>
            <span id="renewFee">
              <Translate contentKey="cpamainApp.passType.renewFee">Renew Fee</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.renewFee}</dd>
          <dt>
            <span id="cancelFee">
              <Translate contentKey="cpamainApp.passType.cancelFee">Cancel Fee</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.cancelFee}</dd>
          <dt>
            <span id="minimumDuration">
              <Translate contentKey="cpamainApp.passType.minimumDuration">Minimum Duration</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.minimumDuration}</dd>
          <dt>
            <span id="maximumDuration">
              <Translate contentKey="cpamainApp.passType.maximumDuration">Maximum Duration</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.maximumDuration}</dd>
          <dt>
            <span id="issueChannelType">
              <Translate contentKey="cpamainApp.passType.issueChannelType">Issue Channel Type</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.issueChannelType}</dd>
          <dt>
            <span id="taxCode">
              <Translate contentKey="cpamainApp.passType.taxCode">Tax Code</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.taxCode}</dd>
          <dt>
            <span id="passMediaType">
              <Translate contentKey="cpamainApp.passType.passMediaType">Pass Media Type</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.passMediaType}</dd>
          <dt>
            <span id="passUserType">
              <Translate contentKey="cpamainApp.passType.passUserType">Pass User Type</Translate>
            </span>
          </dt>
          <dd>{passTypeEntity.passUserType}</dd>
          <dt>
            <Translate contentKey="cpamainApp.passType.agency">Agency</Translate>
          </dt>
          <dd>
            {passTypeEntity.agencies
              ? passTypeEntity.agencies.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {passTypeEntity.agencies && i === passTypeEntity.agencies.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/pass-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pass-type/${passTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PassTypeDetail;
