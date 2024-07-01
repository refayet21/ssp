import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './pass-type.reducer';

export const PassType = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const passTypeList = useAppSelector(state => state.passType.entities);
  const loading = useAppSelector(state => state.passType.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="pass-type-heading" data-cy="PassTypeHeading">
        <Translate contentKey="cpamainApp.passType.home.title">Pass Types</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpamainApp.passType.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pass-type/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cpamainApp.passType.home.createLabel">Create new Pass Type</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {passTypeList && passTypeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cpamainApp.passType.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="cpamainApp.passType.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('shortName')}>
                  <Translate contentKey="cpamainApp.passType.shortName">Short Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('shortName')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="cpamainApp.passType.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('printedName')}>
                  <Translate contentKey="cpamainApp.passType.printedName">Printed Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('printedName')} />
                </th>
                <th className="hand" onClick={sort('issueFee')}>
                  <Translate contentKey="cpamainApp.passType.issueFee">Issue Fee</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('issueFee')} />
                </th>
                <th className="hand" onClick={sort('renewFee')}>
                  <Translate contentKey="cpamainApp.passType.renewFee">Renew Fee</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('renewFee')} />
                </th>
                <th className="hand" onClick={sort('cancelFee')}>
                  <Translate contentKey="cpamainApp.passType.cancelFee">Cancel Fee</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cancelFee')} />
                </th>
                <th className="hand" onClick={sort('minimumDuration')}>
                  <Translate contentKey="cpamainApp.passType.minimumDuration">Minimum Duration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('minimumDuration')} />
                </th>
                <th className="hand" onClick={sort('maximumDuration')}>
                  <Translate contentKey="cpamainApp.passType.maximumDuration">Maximum Duration</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maximumDuration')} />
                </th>
                <th className="hand" onClick={sort('issueChannelType')}>
                  <Translate contentKey="cpamainApp.passType.issueChannelType">Issue Channel Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('issueChannelType')} />
                </th>
                <th className="hand" onClick={sort('taxCode')}>
                  <Translate contentKey="cpamainApp.passType.taxCode">Tax Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('taxCode')} />
                </th>
                <th className="hand" onClick={sort('passMediaType')}>
                  <Translate contentKey="cpamainApp.passType.passMediaType">Pass Media Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('passMediaType')} />
                </th>
                <th className="hand" onClick={sort('passUserType')}>
                  <Translate contentKey="cpamainApp.passType.passUserType">Pass User Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('passUserType')} />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.passType.agency">Agency</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {passTypeList.map((passType, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pass-type/${passType.id}`} color="link" size="sm">
                      {passType.id}
                    </Button>
                  </td>
                  <td>{passType.name}</td>
                  <td>{passType.shortName}</td>
                  <td>{passType.isActive ? 'true' : 'false'}</td>
                  <td>{passType.printedName}</td>
                  <td>{passType.issueFee}</td>
                  <td>{passType.renewFee}</td>
                  <td>{passType.cancelFee}</td>
                  <td>{passType.minimumDuration}</td>
                  <td>{passType.maximumDuration}</td>
                  <td>
                    <Translate contentKey={`cpamainApp.IssueChannelType.${passType.issueChannelType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cpamainApp.TaxCodeType.${passType.taxCode}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cpamainApp.PassMediaType.${passType.passMediaType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cpamainApp.PassUserType.${passType.passUserType}`} />
                  </td>
                  <td>
                    {passType.agencies
                      ? passType.agencies.map((val, j) => (
                          <span key={j}>
                            <Link to={`/agency/${val.id}`}>{val.id}</Link>
                            {j === passType.agencies.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pass-type/${passType.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/pass-type/${passType.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/pass-type/${passType.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cpamainApp.passType.home.notFound">No Pass Types found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PassType;
