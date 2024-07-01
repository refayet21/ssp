import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './agency-license.reducer';

export const AgencyLicense = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const agencyLicenseList = useAppSelector(state => state.agencyLicense.entities);
  const loading = useAppSelector(state => state.agencyLicense.loading);
  const totalItems = useAppSelector(state => state.agencyLicense.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="agency-license-heading" data-cy="AgencyLicenseHeading">
        <Translate contentKey="cpamainApp.agencyLicense.home.title">Agency Licenses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpamainApp.agencyLicense.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/agency-license/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cpamainApp.agencyLicense.home.createLabel">Create new Agency License</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {agencyLicenseList && agencyLicenseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cpamainApp.agencyLicense.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('filePath')}>
                  <Translate contentKey="cpamainApp.agencyLicense.filePath">File Path</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('filePath')} />
                </th>
                <th className="hand" onClick={sort('serialNo')}>
                  <Translate contentKey="cpamainApp.agencyLicense.serialNo">Serial No</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('serialNo')} />
                </th>
                <th className="hand" onClick={sort('issueDate')}>
                  <Translate contentKey="cpamainApp.agencyLicense.issueDate">Issue Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('issueDate')} />
                </th>
                <th className="hand" onClick={sort('expiryDate')}>
                  <Translate contentKey="cpamainApp.agencyLicense.expiryDate">Expiry Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expiryDate')} />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.agencyLicense.verifiedBy">Verified By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.agencyLicense.agencyLicenseType">Agency License Type</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.agencyLicense.belongsTo">Belongs To</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.agencyLicense.issuedBy">Issued By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {agencyLicenseList.map((agencyLicense, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/agency-license/${agencyLicense.id}`} color="link" size="sm">
                      {agencyLicense.id}
                    </Button>
                  </td>
                  <td>{agencyLicense.filePath}</td>
                  <td>{agencyLicense.serialNo}</td>
                  <td>
                    {agencyLicense.issueDate ? (
                      <TextFormat type="date" value={agencyLicense.issueDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {agencyLicense.expiryDate ? (
                      <TextFormat type="date" value={agencyLicense.expiryDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{agencyLicense.verifiedBy ? agencyLicense.verifiedBy.login : ''}</td>
                  <td>
                    {agencyLicense.agencyLicenseType ? (
                      <Link to={`/agency-license-type/${agencyLicense.agencyLicenseType.id}`}>{agencyLicense.agencyLicenseType.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {agencyLicense.belongsTo ? <Link to={`/agency/${agencyLicense.belongsTo.id}`}>{agencyLicense.belongsTo.id}</Link> : ''}
                  </td>
                  <td>
                    {agencyLicense.issuedBy ? <Link to={`/agency/${agencyLicense.issuedBy.id}`}>{agencyLicense.issuedBy.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/agency-license/${agencyLicense.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/agency-license/${agencyLicense.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/agency-license/${agencyLicense.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="cpamainApp.agencyLicense.home.notFound">No Agency Licenses found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={agencyLicenseList && agencyLicenseList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default AgencyLicense;
