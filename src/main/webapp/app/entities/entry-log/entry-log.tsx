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

import { getEntities } from './entry-log.reducer';

export const EntryLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const entryLogList = useAppSelector(state => state.entryLog.entities);
  const loading = useAppSelector(state => state.entryLog.loading);
  const totalItems = useAppSelector(state => state.entryLog.totalItems);

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
      <h2 id="entry-log-heading" data-cy="EntryLogHeading">
        <Translate contentKey="cpamainApp.entryLog.home.title">Entry Logs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpamainApp.entryLog.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/entry-log/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cpamainApp.entryLog.home.createLabel">Create new Entry Log</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {entryLogList && entryLogList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cpamainApp.entryLog.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('eventTime')}>
                  <Translate contentKey="cpamainApp.entryLog.eventTime">Event Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventTime')} />
                </th>
                <th className="hand" onClick={sort('direction')}>
                  <Translate contentKey="cpamainApp.entryLog.direction">Direction</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('direction')} />
                </th>
                <th className="hand" onClick={sort('passStatus')}>
                  <Translate contentKey="cpamainApp.entryLog.passStatus">Pass Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('passStatus')} />
                </th>
                <th className="hand" onClick={sort('actionType')}>
                  <Translate contentKey="cpamainApp.entryLog.actionType">Action Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('actionType')} />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.entryLog.pass">Pass</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="cpamainApp.entryLog.lane">Lane</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {entryLogList.map((entryLog, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/entry-log/${entryLog.id}`} color="link" size="sm">
                      {entryLog.id}
                    </Button>
                  </td>
                  <td>{entryLog.eventTime ? <TextFormat type="date" value={entryLog.eventTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`cpamainApp.DirectionType.${entryLog.direction}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cpamainApp.PassStatusType.${entryLog.passStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cpamainApp.ActionType.${entryLog.actionType}`} />
                  </td>
                  <td>{entryLog.pass ? <Link to={`/pass/${entryLog.pass.id}`}>{entryLog.pass.id}</Link> : ''}</td>
                  <td>{entryLog.lane ? <Link to={`/lane/${entryLog.lane.id}`}>{entryLog.lane.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/entry-log/${entryLog.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/entry-log/${entryLog.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/entry-log/${entryLog.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="cpamainApp.entryLog.home.notFound">No Entry Logs found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={entryLogList && entryLogList.length > 0 ? '' : 'd-none'}>
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

export default EntryLog;
